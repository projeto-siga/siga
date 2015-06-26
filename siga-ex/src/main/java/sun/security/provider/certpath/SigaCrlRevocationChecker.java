/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * @(#)CrlRevocationChecker.java	1.16 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.security.provider.certpath;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRLSelector;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import sun.security.provider.certpath.CertPathHelper;
import sun.security.util.Debug;
import sun.security.x509.CRLReasonCodeExtension;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X509CRLEntryImpl;

/**
 * CrlRevocationChecker is a <code>PKIXCertPathChecker</code> that checks
 * revocation status information on a PKIX certificate using CRLs obtained from
 * one or more <code>CertStores</code>. This is based on revision draft three
 * of rfc2459
 * (http://www.ietf.org/internet-drafts/draft-ietf-pkix-new-part1-02.txt).
 * 
 * @version 1.16, 12/19/03
 * @since 1.4
 * @author Seth Proctor
 * @author Steve Hanna
 */
public class SigaCrlRevocationChecker extends PKIXCertPathChecker {

	private static final Debug debug = Debug.getInstance("certpath");
	private final PublicKey mInitPubKey;
	private final List<CertStore> mStores;
	private final String mSigProvider;
	private final Date mCurrentTime;
	private PublicKey mPrevPubKey;
	private boolean mCRLSignFlag;
	private HashSet<X509CRL> mPossibleCRLs;
	private HashSet<X509CRL> mApprovedCRLs;
	private final PKIXParameters mParams;
	private final Collection<X509Certificate> mExtraCerts;
	private static final boolean[] mCrlSignUsage = { false, false, false,
			false, false, false, true };

	/**
	 * Creates a <code>CrlRevocationChecker</code>.
	 * 
	 * @param initPubKey
	 *            initial PublicKey in the path
	 * @param params
	 *            <code>PKIXParameters</code> to be used for finding
	 *            certificates and CRLs, etc.
	 */
	public SigaCrlRevocationChecker(PublicKey initPubKey, PKIXParameters params)
			throws CertPathValidatorException {
		this(initPubKey, params, null);
	}

	/**
	 * Creates a <code>CrlRevocationChecker</code>, allowing extra
	 * certificates to be supplied beyond those contained in the
	 * <code>PKIXParameters</code>.
	 * 
	 * @param initPubKey
	 *            initial PublicKey in the path
	 * @param params
	 *            <code>PKIXParameters</code> to be used for finding
	 *            certificates and CRLs, etc.
	 * @param certs
	 *            a <code>Collection</code> of certificates that may be
	 *            useful, beyond those available through <code>params</code> (<code>null</code>
	 *            if none)
	 */
	SigaCrlRevocationChecker(PublicKey initPubKey, PKIXParameters params,
			Collection certs) throws CertPathValidatorException {
		mInitPubKey = initPubKey;
		mParams = params;
		mStores = params.getCertStores();
		mSigProvider = params.getSigProvider();
		mExtraCerts = certs;
		Date testDate = params.getDate();
		mCurrentTime = (testDate != null ? testDate : new Date());
		init(false);
	}

	/**
	 * Initializes the internal state of the checker from parameters specified
	 * in the constructor
	 */
	public void init(boolean forward) throws CertPathValidatorException {
		if (!forward) {
			mPrevPubKey = mInitPubKey;
			mCRLSignFlag = true;
		} else {
			throw new CertPathValidatorException("forward checking "
					+ "not supported");
		}
	}

	public boolean isForwardCheckingSupported() {
		return false;
	}

	public Set<String> getSupportedExtensions() {
		return null;
	}

	/**
	 * Performs the revocation status check on the certificate using its
	 * internal state.
	 * 
	 * @param cert
	 *            the Certificate
	 * @param unresolvedCritExts
	 *            a Collection of the unresolved critical extensions
	 * @exception CertPathValidatorException
	 *                Exception thrown if certificate does not verify
	 */
	public void check(Certificate cert, Collection<String> unresolvedCritExts)
			throws CertPathValidatorException {
		X509Certificate currCert = (X509Certificate) cert;
		verifyRevocationStatus(currCert, mPrevPubKey, mCRLSignFlag, true);

		// Make new public key if parameters are missing
		PublicKey cKey = currCert.getPublicKey();
		if (cKey instanceof DSAPublicKey
				&& ((DSAPublicKey) cKey).getParams() == null) {
			// cKey needs to inherit DSA parameters from prev key
			cKey = BasicChecker.makeInheritedParamsKey(cKey, mPrevPubKey);
		}
		mPrevPubKey = cKey;
		mCRLSignFlag = certCanSignCrl(currCert);
	}

	/**
	 * Performs the revocation status check on the certificate using the
	 * provided state variables, as well as the constant internal data.
	 * 
	 * @param currCert
	 *            the Certificate
	 * @param prevKey
	 *            the previous PublicKey in the chain
	 * @param signFlag
	 *            a boolean as returned from the last call, or true if this is
	 *            the first cert in the chain
	 * @return a boolean specifying if the cert is allowed to vouch for the
	 *         validity of a CRL for the next iteration
	 * @exception CertPathValidatorException
	 *                Exception thrown if certificate does not verify.
	 */
	public boolean check(X509Certificate currCert, PublicKey prevKey,
			boolean signFlag) throws CertPathValidatorException {
		verifyRevocationStatus(currCert, prevKey, signFlag, true);
		return certCanSignCrl(currCert);
	}

	/**
	 * Checks that a cert can be used to verify a CRL.
	 * 
	 * @param currCert
	 *            an X509Certificate to check
	 * @return a boolean specifying if the cert is allowed to vouch for the
	 *         validity of a CRL
	 */
	public boolean certCanSignCrl(X509Certificate currCert) {
		// if the cert doesn't include the key usage ext, or
		// the key usage ext asserts cRLSigning, set CRL_sign_flag to
		// true, otherwise set it to false.
		try {
			boolean[] kbools = currCert.getKeyUsage();

			if (kbools != null) {
				KeyUsageExtension ku = new KeyUsageExtension(kbools);
				Boolean b = (Boolean) ku.get(KeyUsageExtension.CRL_SIGN);
				return b.booleanValue();
			} else {
				return true;
			}
		} catch (Exception e) {
			if (debug != null) {
				debug.println("CrlRevocationChecker.certCanSignCRL() "
						+ "unexpected exception");
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Internal method to start the verification of a cert
	 */
	private void verifyRevocationStatus(X509Certificate currCert,
			PublicKey prevKey, boolean signFlag, boolean allowSeparateKey)
			throws CertPathValidatorException {
		verifyRevocationStatus(currCert, prevKey, signFlag, allowSeparateKey,
				null);
	}

	/**
	 * Internal method to start the verification of a cert
	 * 
	 * @param stackedCerts
	 *            a <code>Set</code> of <code>X509Certificate</code>s>
	 *            whose revocation status depends on the non-revoked status of
	 *            this cert. To avoid circular dependencies, we assume they're
	 *            revoked while checking the revocation status of this cert.
	 */
	private void verifyRevocationStatus(X509Certificate currCert,
			PublicKey prevKey, boolean signFlag, boolean allowSeparateKey,
			Set<X509Certificate> stackedCerts)
			throws CertPathValidatorException {

		String msg = "revocation status";
		if (debug != null) {
			debug.println("CrlRevocationChecker.verifyRevocationStatus()"
					+ " ---checking " + msg + "...");
		}

		// reject circular dependencies - RFC 3280 is not explicit on how
		// to handle this, so we feel it is safest to reject them until
		// the issue is resolved in the PKIX WG.
		if ((stackedCerts != null) && stackedCerts.contains(currCert)) {
			throw new CertPathValidatorException("circular dependency - "
					+ "cert can't vouch for CRL");
		}

		if (!signFlag) {
			if (allowSeparateKey
					&& verifyWithSeparateSigningKey(currCert, prevKey,
							signFlag, stackedCerts)) {
				return;
			} else {
				throw new CertPathValidatorException("cert can't vouch for CRL");
			}
		}

		// to start, get the entire list of possible CRLs
		X500Principal certIssuer = currCert.getIssuerX500Principal();

		// init the state for this run
		mPossibleCRLs = new HashSet<X509CRL>();
		mApprovedCRLs = new HashSet<X509CRL>();

		try {
			X509CRLSelector sel = new X509CRLSelector();
			sel.setCertificateChecking(currCert);
			sel.setDateAndTime(mCurrentTime);

			// add the default issuer string
			//CertPathHelper.addIssuer(sel, certIssuer);

			for (CertStore mStore : mStores) {
				mPossibleCRLs.addAll((Collection<X509CRL>) mStore.getCRLs(sel));
			}
//			DistributionPointFetcher store = DistributionPointFetcher.getInstance();
			boolean[] reasonsMask = new boolean[9];
			//mPossibleCRLs.addAll(store.getCRLs(sel, prevKey, mSigProvider, mStores, reasonsMask));
		} catch (Exception e) {
			if (debug != null) {
				debug.println("CrlRevocationChecker.verifyRevocationStatus() "
						+ "unexpected exception: " + e.getMessage());
				e.printStackTrace();
			}
			throw new CertPathValidatorException(e);
		}

		if (mPossibleCRLs.isEmpty()) {
			if (allowSeparateKey
					&& verifyWithSeparateSigningKey(currCert, prevKey,
							signFlag, stackedCerts)) {
				return;
			} else {
				// we are assuming the directory is not secure,
				// so someone may have removed all the CRLs.
				throw new CertPathValidatorException(msg
						+ " check failed: no CRL found");
			}
		}

		if (debug != null) {
			debug.println("CrlRevocationChecker.verifyRevocationStatus() "
					+ "crls.size() = " + mPossibleCRLs.size());
		}
		// Now that we have a list of possible CRLs, see which ones can
		// be approved
		for (X509CRL crl : mPossibleCRLs) {
			if (verifyPossibleCRL(crl, certIssuer, prevKey)) {
				mApprovedCRLs.add(crl);
			}
		}

		// make sure that we have at least one CRL that _could_ cover
		// the certificate in question
		if (mApprovedCRLs.isEmpty()) {
			if (allowSeparateKey
					&& verifyWithSeparateSigningKey(currCert, prevKey,
							signFlag, stackedCerts)) {
				return;
			} else {
				throw new CertPathValidatorException("no possible CRLs");
			}
		}

		/*
		 * SECTION 6.3.3.4
		 */

		// See if the cert is in the set of approved crls. If the
		// cert is listed on hold in one crl, and revoked in another, ignore
		// the hold.
		BigInteger sn = currCert.getSerialNumber();
		if (debug != null) {
			debug.println("starting the final sweep...");
			debug.println("CrlRevocationChecker.verifyRevocationStatus"
					+ " cert SN: " + sn.toString());
		}

		boolean hold = false;
		for (X509CRL crl : mApprovedCRLs) {
			X509CRLEntry entry = (X509CRLEntry) crl.getRevokedCertificate(sn);
			if (entry != null) {
				if (debug != null) {
					debug.println("CrlRevocationChecker.verifyRevocationStatus"
							+ " CRL entry: " + entry.toString());
				}

				int reasonCode = 0;
				try {
					X509CRLEntryImpl entryImpl = X509CRLEntryImpl.toImpl(entry);
					Integer reason = entryImpl.getReasonCode();
					// if reasonCode extension is absent, this is equivalent
					// to a reasonCode value of unspecified (0)
					reasonCode = (reason == null ? CRLReasonCodeExtension.UNSPECIFIED
							: reason.intValue());
				} catch (Exception e) {
					throw new CertPathValidatorException(e);
				}

				/*
				 * If reason code is CERTIFICATE_HOLD, continue to look for
				 * other revoked entries with different reasons before exiting
				 * loop.
				 */
				hold = (reasonCode == CRLReasonCodeExtension.CERTIFICATE_HOLD);

				/*
				 * The certificate fails the revocation check if it is not on
				 * hold and the reason code is not REMOVE_FROM_CRL, which
				 * indicates a certificate that used to be but is no longer on
				 * hold status. It should not be considered fatal.
				 */
				if (!hold
						&& reasonCode != CRLReasonCodeExtension.REMOVE_FROM_CRL) {
					throw new CertPathValidatorException("Certificate has been"
							+ " revoked, reason: " + reasonToString(reasonCode));
				}

				/*
				 * Throw an exception if any unresolved critical extensions
				 * remain in the CRL entry
				 */
				Set<String> unresCritExts = entry.getCriticalExtensionOIDs();
				if (unresCritExts != null && !unresCritExts.isEmpty()) {
					/* remove any that we have processed */
					unresCritExts.remove(PKIXExtensions.ReasonCode_Id
							.toString());
					if (!unresCritExts.isEmpty()) {
						throw new CertPathValidatorException(
								"Unrecognized "
										+ "critical extension(s) in revoked CRL entry: "
										+ unresCritExts);
					}
				}
			}
		}

		if (hold) {
			throw new CertPathValidatorException("Certificate is on hold");
		}
	}

	/**
	 * We have a cert whose revocation status couldn't be verified by a CRL
	 * issued by the cert that issued the CRL. See if we can find a valid CRL
	 * issued by a separate key that can verify the revocation status of this
	 * certificate.
	 * <p>
	 * Note that this does not provide support for indirect CRLs, only CRLs
	 * signed with a different key (but the same issuer name) as the certificate
	 * being checked.
	 * 
	 * @param currCert
	 *            the <code>X509Certificate</code> to be checked
	 * @param prevKey
	 *            the <code>PublicKey</code> that failed
	 * @param signFlag
	 *            <code>true</code> if that key was trusted to sign CRLs
	 * @param stackedCerts
	 *            a <code>Set</code> of <code>X509Certificate</code>s>
	 *            whose revocation status depends on the non-revoked status of
	 *            this cert. To avoid circular dependencies, we assume they're
	 *            revoked while checking the revocation status of this cert.
	 * @return <code>true</code> if the cert's revocation status was verified
	 *         successfully, <code>false</code> otherwise
	 */
	private boolean verifyWithSeparateSigningKey(X509Certificate currCert,
			PublicKey prevKey, boolean signFlag,
			Set<X509Certificate> stackedCerts) {
		String msg = "revocation status";
		if (debug != null) {
			debug.println("CrlRevocationChecker.verifyWithSeparateSigningKey()"
					+ " ---checking " + msg + "...");
		}

		// reject circular dependencies - RFC 3280 is not explicit on how
		// to handle this, so we feel it is safest to reject them until
		// the issue is resolved in the PKIX WG.
		if ((stackedCerts != null) && stackedCerts.contains(currCert)) {
			return false;
		}

		// If prevKey wasn't trusted, maybe we just didn't have the right
		// path to it. Don't rule that key out.
		if (!signFlag) {
			prevKey = null;
		}

		// Skip revocation during this build to detect circular
		// references. But check revocation afterwards, using the
		// key (or any other that works).

		// Try to build a path to the issuer name that has the crlSign
		// bit set in the EE cert and has a different public key.
		// If we can do that, then check revocation using that key.
		// If it doesn't check out, try to find a different key.
		// And if we can't find a key, then return false.
		try {
			Set<PublicKey> badKeys = new HashSet<PublicKey>();
			if (prevKey != null) {
				badKeys.add(prevKey);
			}
			while (true) {
				// Try to find another key that might be able to sign
				// CRLs vouching for this cert. On failure, this will
				// throw an exception caught below.
				PublicKey newKey = buildToNewKey(currCert, badKeys,
						stackedCerts);
				try {
					verifyRevocationStatus(currCert, newKey, true, false);
					// If that passed, the cert is OK!
					return true;
				} catch (CertPathValidatorException cpve) {
					// If it failed, ignore the exception and
					// try to get another key.
				}
				badKeys.add(newKey);
			}
		} catch (Exception e) {
			// If an exception happens during this method, ignore it and
			// fall through to return false.
			if (debug != null) {
				debug
						.println("CrlRevocationChecker.verifyWithSeparateSigningKey()"
								+ " got exception " + e);
			}
		}

		return false;
	}

	/**
	 * Tries to find a CertPath that establishes a key that can be used to
	 * verify the revocation status of a given certificate. Ignores keys that
	 * have previously been tried. Throws a CertPathBuilderException if no such
	 * key could be found.
	 * 
	 * @param currCert
	 *            the <code>X509Certificate</code> to be checked
	 * @param badKeys
	 *            a <code>Set</code> of <code>PublicKey</code>s that should
	 *            be ignored
	 * @param stackedCerts
	 *            a <code>Set</code> of <code>X509Certificate</code>s>
	 *            whose revocation status depends on the establishment of this
	 *            path.
	 * @throws CertPathBuilderException
	 *             on failure
	 */
	private PublicKey buildToNewKey(X509Certificate currCert,
			Set<PublicKey> badKeys, Set<X509Certificate> stackedCerts)
			throws CertPathBuilderException {

		if (debug != null) {
			debug.println("CrlRevocationChecker.buildToNewKey()"
					+ " starting work");
		}

		try {
			X509CertSelector certSel = new RejectKeySelector(badKeys);
			certSel.setSubject(currCert.getIssuerX500Principal().getName());
			certSel.setKeyUsage(mCrlSignUsage);

			PKIXBuilderParameters builderParams;
			if (mParams instanceof PKIXBuilderParameters) {
				builderParams = (PKIXBuilderParameters) mParams.clone();
				builderParams.setTargetCertConstraints(certSel);
				// Policy qualifiers must be rejected, since we don't have
				// any way to convey them back to the application.
				builderParams.setPolicyQualifiersRejected(true);
			} else {
				// It's unfortunate that there's no easy way to make a
				// PKIXBuilderParameters object from a PKIXParameters
				// object. This might miss some things if parameters
				// are added in the future or the validatorParams object
				// is a custom class derived from PKIXValidatorParameters.
				builderParams = new PKIXBuilderParameters(mParams
						.getTrustAnchors(), certSel);

				builderParams.setInitialPolicies(mParams.getInitialPolicies());
				builderParams.setCertStores(mParams.getCertStores());
				builderParams.setExplicitPolicyRequired(mParams
						.isExplicitPolicyRequired());
				builderParams.setPolicyMappingInhibited(mParams
						.isPolicyMappingInhibited());
				builderParams.setAnyPolicyInhibited(mParams
						.isAnyPolicyInhibited());
				// Policy qualifiers must be rejected, since we don't have
				// any way to convey them back to the application.
				// That's the default, so no need to write code.
				builderParams.setDate(mParams.getDate());
				builderParams
						.setCertPathCheckers(mParams.getCertPathCheckers());
				builderParams.setSigProvider(mParams.getSigProvider());
			}
			// If mInitPubKey is not null, we should only trust
			// trust anchors that match that public key. Otherwise,
			// we could get a CRL from a different PKI.
			if (mInitPubKey != null) {
				Set<TrustAnchor> oldAnchors = builderParams.getTrustAnchors();
				Set<TrustAnchor> newAnchors = new HashSet<TrustAnchor>();
				for (TrustAnchor ta : oldAnchors) {
					PublicKey pubKey = ta.getCAPublicKey();
					if (pubKey != null) {
						if (pubKey.equals(mInitPubKey))
							newAnchors.add(ta);
					} else {
						X509Certificate cert = ta.getTrustedCert();
						pubKey = cert.getPublicKey();
						if (pubKey.equals(mInitPubKey))
							newAnchors.add(ta);
					}
				}
				builderParams.setTrustAnchors(newAnchors);
			}

			// We'll come back to check revocation later.
			builderParams.setRevocationEnabled(false);
			// Make sure that any extra certs supplied (like the ones
			// in a path being validated) are available.
			if (mExtraCerts != null) {
				CollectionCertStoreParameters ccsp = new CollectionCertStoreParameters(
						mExtraCerts);
				CertStore extraCertStore = CertStore.getInstance("Collection",
						ccsp);

				builderParams.addCertStore(extraCertStore);
			}
			CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");

			if (debug != null) {
				debug.println("CrlRevocationChecker.buildToNewKey()"
						+ " about to try build ...");
			}
			PKIXCertPathBuilderResult cpbr = (PKIXCertPathBuilderResult) builder
					.build(builderParams);

			if (debug != null) {
				debug.println("CrlRevocationChecker.buildToNewKey()"
						+ " about to check revocation ...");
			}
			// Now check revocation of all certs in path, assuming that
			// the stackedCerts are revoked.
			if (stackedCerts == null) {
				stackedCerts = new HashSet<X509Certificate>();
			} else {
				stackedCerts = new HashSet<X509Certificate>(stackedCerts);
			}
			stackedCerts.add(currCert);
			TrustAnchor ta = cpbr.getTrustAnchor();
			PublicKey prevKey = ta.getCAPublicKey();
			if (prevKey == null) {
				X509Certificate cert = ta.getTrustedCert();
				prevKey = cert.getPublicKey();
			}
			boolean signFlag = true;
			CertPath cp = cpbr.getCertPath();
			List<X509Certificate> cpList = (List<X509Certificate>) cp
					.getCertificates();
			for (int i = cpList.size() - 1; i >= 0; i--) {
				X509Certificate cert = cpList.get(i);

				if (debug != null) {
					debug.println("CrlRevocationChecker.buildToNewKey()"
							+ " index " + i + " checking " + cert);
				}
				verifyRevocationStatus(cert, prevKey, signFlag, true,
						stackedCerts);
				signFlag = certCanSignCrl(currCert);
				prevKey = cert.getPublicKey();
			}

			if (debug != null) {
				debug.println("CrlRevocationChecker.buildToNewKey()"
						+ " got key " + cpbr.getPublicKey());
			}
			return cpbr.getPublicKey();
		} catch (InvalidAlgorithmParameterException iape) {
			throw new CertPathBuilderException(iape);
		} catch (IOException ioe) {
			throw new CertPathBuilderException(ioe);
		} catch (NoSuchAlgorithmException nsae) {
			throw new CertPathBuilderException(nsae);
		} catch (CertPathValidatorException cpve) {
			throw new CertPathBuilderException(cpve);
		}
	}

	/*
	 * This inner class extends the X509CertSelector to add an additional check
	 * to make sure the subject public key isn't on a particular list. This
	 * class is used by CrlRevocationChecker.verifyWithSeparateSigningKey() to
	 * make sure the builder doesn't end up with a CertPath to a public key that
	 * has already been rejected.
	 */
	static class RejectKeySelector extends X509CertSelector {
		private final Set<PublicKey> badKeySet;

		/**
		 * Creates a new <code>RejectKeySelector</code>.
		 * 
		 * @param badPublicKeys
		 *            a <code>Collection</code> of <code>PublicKey</code>s
		 *            that should be rejected (or <code>null</code> if no such
		 *            check should be done
		 */
		RejectKeySelector(Collection<PublicKey> badPublicKeys) {
			this.badKeySet = new HashSet<PublicKey>(badPublicKeys);
		}

		/**
		 * Decides whether a <code>Certificate</code> should be selected.
		 * 
		 * @param cert
		 *            the <code>Certificate</code> to be checked
		 * @return <code>true</code> if the <code>Certificate</code> should
		 *         be selected, <code>false</code> otherwise
		 */
		public boolean match(Certificate cert) {
			if (!super.match(cert))
				return (false);

			if (badKeySet.contains(cert.getPublicKey())) {
				if (debug != null)
					debug.println("RejectCertSelector.match: bad key");
				return false;
			}

			if (debug != null)
				debug.println("RejectCertSelector.match: returning true");
			return true;
		}

		/**
		 * Return a printable representation of the <code>CertSelector</code>.
		 * 
		 * @return a <code>String</code> describing the contents of the
		 *         <code>CertSelector</code>
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("RejectCertSelector: [\n");
			sb.append(super.toString());
			sb.append(badKeySet);
			sb.append("]");
			return sb.toString();
		}
	}

	/**
	 * Return a String describing the reasonCode value
	 */
	private static String reasonToString(int reasonCode) {
		switch (reasonCode) {
		case CRLReasonCodeExtension.UNSPECIFIED:
			return "unspecified";
		case CRLReasonCodeExtension.KEY_COMPROMISE:
			return "key compromise";
		case CRLReasonCodeExtension.CA_COMPROMISE:
			return "CA compromise";
		case CRLReasonCodeExtension.AFFLIATION_CHANGED:
			return "affiliation changed";
		case CRLReasonCodeExtension.SUPERSEDED:
			return "superseded";
		case CRLReasonCodeExtension.CESSATION_OF_OPERATION:
			return "cessation of operation";
		case CRLReasonCodeExtension.CERTIFICATE_HOLD:
			return "certificate hold";
		case CRLReasonCodeExtension.REMOVE_FROM_CRL:
			return "remove from CRL";
		default:
			return "unrecognized reason code";
		}
	}

	/**
	 * Internal method that accepts a crl from the list of possible_crls, and
	 * sees if it is approved, based on the cert.
	 * 
	 * @param crl
	 *            a possible CRL to test for acceptability
	 * @param certIssuer
	 *            the issuer DN of the certificate whose revocation status is
	 *            being checked
	 * @param prevKey
	 *            the public key of the issuer of cert
	 * @return true if the crl is acceptable, false otherwise.
	 */
	private boolean verifyPossibleCRL(X509CRL crl, X500Principal certIssuer,
			PublicKey prevKey) throws CertPathValidatorException {
		/*
		 * SECTION 6.3.3.1.b (verify that the CRL issuer matches the certificate
		 * issuer)
		 */
		if (!crl.getIssuerX500Principal().equals(certIssuer)) {
			if (debug != null)
				debug.println("CRL issuer does not match cert issuer");
			return false;
		}

		/*
		 * SECTION 6.3.3.1.d (validate the signature on the CRL)
		 */
		try {
			crl.verify(prevKey, mSigProvider);
		} catch (Exception e) {
			if (debug != null) {
				debug.println("CRL signature failed to verify");
				e.printStackTrace();
			}
			return false;
		}

		/*
		 * SECTION 6.3.3.2.a (if the value of the next update field is before
		 * the current time, obtain an appropriate delta CRL or discard the
		 * CRL).
		 */
		Date nextUpdate = crl.getNextUpdate();
		if (nextUpdate != null && nextUpdate.before(mCurrentTime)) {
			if (debug != null)
				debug.println("discarding stale CRL (nextUpdate is before "
						+ "required validation time)");
			return false;
		}

		/*
		 * Section 6.3.3.2.b-d omitted since Delta CRLs not supported
		 */

		/*
		 * Throw an exception if any unresolved critical extensions remain in
		 * the CRL.
		 */
		Set unresCritExts = crl.getCriticalExtensionOIDs();
		if (unresCritExts != null && !unresCritExts.isEmpty()) {
			if (debug != null) {
				Iterator i = unresCritExts.iterator();
				while (i.hasNext())
					debug.println((String) i.next());
			}
			throw new CertPathValidatorException("Unrecognized "
					+ "critical extension(s) in CRL: " + unresCritExts);
		}

		return true;
	}
}