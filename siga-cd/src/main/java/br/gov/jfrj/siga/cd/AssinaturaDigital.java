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
package br.gov.jfrj.siga.cd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.axis.encoding.Base64;
import org.apache.commons.io.output.NullOutputStream;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.util.Store;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cd.ac.FachadaDeCertificadosAC;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;

/**
 * Links uteis:
 * 			http://www.bouncycastle.org/wiki/display/JA1/BC+Version+2+APIs
 * 			http://www.bouncycastle.org/wiki/display/JA1/Porting+from+earlier+BC+releases+to+1.47+and+later
 * 			https://www.bouncycastle.org/docs/pkixdocs1.5on/org/bouncycastle/cms/CMSSignedDataGenerator.html
 *
 */
public class AssinaturaDigital {

	protected static String verificarAssinatura(final byte[] conteudo,
			final byte[] assinatura, String sMimeType, Date dtAssinatura)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		if (sMimeType.equals("application/cms-signature"))
			return verificarAssinaturaCMSeCarimboDeTempo(conteudo, assinatura,
					dtAssinatura);
		else if (sMimeType.equals("application/pkcs7-signature"))
			return verificarAssinaturaCMS(conteudo, assinatura, dtAssinatura);
		// return verificarAssinaturaPKCS(conteudo, assinatura, dtAssinatura);

		throw new AplicacaoException("formato de assinatura desconhecido: '"
				+ sMimeType + "'");

		// final PKCS7SignedData signedData = new PKCS7SignedData(assinatura);
		// signedData.update(conteudo, 0, conteudo.length); // Update checksum
		// if (!signedData.verify())
		// throw new AplicacaoException("Assinatura inválida");
		//
		// final CertificateFactory cf =
		// CertificateFactory.getInstance("X.509");
		// final ArrayList<X509Certificate> certsList = new
		// ArrayList<X509Certificate>();
		// for (final Certificate cert : signedData.getCertificates()) {
		// final ByteArrayInputStream bais = new ByteArrayInputStream(cert
		// .getEncoded());
		// final X509Certificate x509 = (X509Certificate) cf
		// .generateCertificate(bais);
		// certsList.add(0, x509);
		// }
		//
		// X509Certificate[] cadeiaTotal =
		// montarCadeiaOrdenadaECompleta(certsList);
		//
		// final X509ChainValidator cadeia = new X509ChainValidator(cadeiaTotal,
		// trustedAnchors, null);
		// cadeia.checkCRL(true);
		// cadeia.validateChain(dtAssinatura);
		//
		// String s =
		// signedData.getSigningCertificate().getSubjectDN().getName();
		// s = s.split("CN=")[1];
		// s = s.split(",")[0];
		//
		// return s;
	}

	// private static String verificarAssinaturaPKCS(final byte[] conteudo,
	// final byte[] assinatura, Date dtAssinatura)
	// throws InvalidKeyException, SecurityException, CRLException,
	// CertificateException, NoSuchProviderException,
	// NoSuchAlgorithmException, SignatureException, AplicacaoException,
	// ChainValidationException, IOException, Exception {
	//
	// final PKCS7SignedData signedData = new PKCS7SignedData(assinatura);
	// signedData.update(conteudo, 0, conteudo.length); // Update
	//
	// // checksum
	// if (!signedData.verify())
	// throw new AplicacaoException("Assinatura inválida");
	//
	// final CertificateFactory cf = CertificateFactory.getInstance("X.509");
	// final ArrayList<X509Certificate> certsList = new
	// ArrayList<X509Certificate>();
	// for (final Certificate cert : signedData.getCertificates()) {
	// final ByteArrayInputStream bais = new ByteArrayInputStream(cert
	// .getEncoded());
	// final X509Certificate x509 = (X509Certificate) cf
	// .generateCertificate(bais);
	// certsList.add(0, x509);
	// }
	//
	// X509Certificate[] cadeiaTotal = montarCadeiaOrdenadaECompleta(certsList);
	//
	// final X509ChainValidator cadeia = new X509ChainValidator(cadeiaTotal,
	// trustedAnchors, null);
	// cadeia.checkCRL(true);
	// cadeia.validateChain(dtAssinatura);
	//
	// String s = signedData.getSigningCertificate().getSubjectDN().getName();
	// s = s.split("CN=")[1];
	// s = s.split(",")[0];
	//
	// return s;
	// }
	private static X509Certificate[] montarCadeiaOrdenadaECompleta(
			Collection<X509CertificateHolder> certs) throws Exception {
		return FachadaDeCertificadosAC.montarCadeiaOrdenadaECompleta(certs);
	}

	/*
	 * private static X509Certificate[] montarCadeiaOrdenadaECompleta_old(
	 * Collection<X509Certificate> certs) throws Exception { final
	 * CertificateFactory cf = CertificateFactory.getInstance("X.509");
	 * 
	 * // Cria um objeto TrustAnchor indicando qual é o certificado root da //
	 * cadeia // final X509Certificate x509Root; // final Set<TrustAnchor>
	 * trustedAnchors = new HashSet<TrustAnchor>(); // { // final
	 * ByteArrayInputStream bais = new ByteArrayInputStream( //
	 * AssinaturaDigital.AC_RAIZ_ICPBRASIL); // x509Root = (X509Certificate)
	 * cf.generateCertificate(bais); // final TrustAnchor trustedCert = new
	 * TrustAnchor(x509Root, null); // trustedAnchors.add(trustedCert); // }
	 * 
	 * final ArrayList<X509Certificate> certsList = new
	 * ArrayList<X509Certificate>(); for (final Certificate cert : certs) {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(cert
	 * .getEncoded()); final X509Certificate x509 = (X509Certificate) cf
	 * .generateCertificate(bais); certsList.add(0, x509); }
	 * 
	 * final ArrayList<X509Certificate> knownCertsList = new
	 * ArrayList<X509Certificate>(); { final ByteArrayInputStream bais = new
	 * ByteArrayInputStream( AssinaturaDigital.AC_RAIZ_ICPBRASIL);
	 * knownCertsList.add((X509Certificate) cf.generateCertificate(bais)); } {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(
	 * AssinaturaDigital.AC_RAIZ_ICPBRASIL_V1);
	 * knownCertsList.add((X509Certificate) cf.generateCertificate(bais)); } {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(
	 * AssinaturaDigital.AC_JUS); knownCertsList.add((X509Certificate)
	 * cf.generateCertificate(bais)); } { final ByteArrayInputStream bais = new
	 * ByteArrayInputStream( AssinaturaDigital.AC_CAIXA_JUS);
	 * knownCertsList.add((X509Certificate) cf.generateCertificate(bais)); } {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(
	 * AssinaturaDigital.AC_CAIXA_JUS_V1); knownCertsList.add((X509Certificate)
	 * cf.generateCertificate(bais)); } { final ByteArrayInputStream bais = new
	 * ByteArrayInputStream( AssinaturaDigital.AC_JUS_V3);
	 * knownCertsList.add((X509Certificate) cf.generateCertificate(bais)); } {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(
	 * AssinaturaDigital.AC_CERTISIGN_JUS_G2);
	 * knownCertsList.add((X509Certificate) cf.generateCertificate(bais)); } {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(
	 * AssinaturaDigital.AC_CERTISIGN_RFB_G3);
	 * knownCertsList.add((X509Certificate) cf.generateCertificate(bais)); } {
	 * final ByteArrayInputStream bais = new ByteArrayInputStream(
	 * AssinaturaDigital.AC_RFB_V2); knownCertsList.add((X509Certificate)
	 * cf.generateCertificate(bais)); }
	 * 
	 * // Acrescenta os certificados faltantes para completar a cadeia //
	 * boolean fContinue; do { fContinue = false; for (X509Certificate x509 :
	 * certsList) { boolean fFound = false; X500Principal issuer =
	 * x509.getIssuerX500Principal(); for (X509Certificate otherX509 :
	 * certsList) { if (issuer.equals(otherX509.getSubjectX500Principal())) {
	 * fFound = true; break; } } if (!fFound) { for (X509Certificate otherX509 :
	 * knownCertsList) { if (issuer.equals(otherX509.getSubjectX500Principal()))
	 * { certsList.add(otherX509); fFound = true; fContinue = true; break; } } }
	 * if (fContinue) break; if (!fFound) { throw new AplicacaoException(
	 * "Não foi possível montar a cadeia completa de certificação"); } } } while
	 * (fContinue);
	 * 
	 * // APARENTEMENTE ISSO ESTÁ CORRETO, NO ENTANTO, NO ACROBAT O CARIMBO NÃO
	 * // APARECE. DEVE SER PORQUE ESTÁ DANDO ERRO DE VALIDAÇÃO POR FALTA DE UM
	 * // FLAG "CRICTICAL".
	 * 
	 * int cCerts = certsList.size(); final ArrayList<X509Certificate>
	 * certsListSorted = new ArrayList<X509Certificate>(); boolean
	 * hasTrustedAnchor = false; for (X509Certificate x509 : certsList) { for
	 * (TrustAnchor trustedAnchor : trustedAnchors) { if (!hasTrustedAnchor &&
	 * trustedAnchor.getTrustedCert().equals(x509)) { hasTrustedAnchor = true;
	 * certsListSorted.add(trustedAnchor.getTrustedCert()); } } // if
	 * (trustedAnchors.contains(x509)) } if (!hasTrustedAnchor) throw new
	 * AplicacaoException(
	 * "Cadeia de certificação não está relacionada com a raíz da ICP-Brasil");
	 * 
	 * boolean fExit = false; while (!fExit) { fExit = true; for
	 * (X509Certificate x509 : certsList) { if
	 * (x509.getIssuerX500Principal().equals(
	 * certsListSorted.get(0).getSubjectX500Principal()) &&
	 * !x509.equals(certsListSorted.get(0))) { certsListSorted.add(0, x509);
	 * fExit = false; } } }
	 * 
	 * if (certsListSorted.size() != cCerts) throw new AplicacaoException(
	 * "Cadeia de certificação não está corretamente encadeada ou não está relacionada com a raíz da ICP-Brasil"
	 * );
	 * 
	 * // X509Certificate cadeiaTotal[]; final X509Certificate cadeiaTotal[] =
	 * new X509Certificate[certsListSorted .size()];
	 * certsListSorted.toArray(cadeiaTotal); return cadeiaTotal; }
	 */

	/**
	 * Interpreta um dado do tipo otherName. Obs. O JDK 5.0 não tem classes que
	 * lidem com um dado do tipo OtherName. É necessário usar o BouncyCastle.
	 * 
	 * @param encoded
	 *            O dado em ASN.1.
	 * @return Um par contendo o OID e o conteúdo.
	 */
	/*
	 * @SuppressWarnings("unchecked") private static Pair<DERObjectIdentifier,
	 * String> getOtherName(byte[] encoded) throws IOException { // O JDK 5.0
	 * não tem classes que lidem com um dado do tipo OtherName. // É necessário
	 * usar o BouncyCastle. ASN1InputStream inps = new ASN1InputStream(encoded);
	 * DERSequence seq = null; DERObjectIdentifier oid = null; String conteudo =
	 * ""; seq = (DERSequence) inps.readObject(); inps.close(); Enumeration en =
	 * seq.getObjects(); oid = (DERObjectIdentifier) en.nextElement(); DERObject
	 * obj = ((ASN1TaggedObject) ((ASN1TaggedObject) en
	 * .nextElement()).getObject()).getObject(); if (obj instanceof DERString) {
	 * // Certificados antigos SERASA - // incorretos conteudo = ((DERString)
	 * obj).getString(); } else if (obj instanceof DEROctetString) { //
	 * Certificados corretos conteudo = new String(((DEROctetString)
	 * obj).getOctets(), "ISO-8859-1"); } return new Pair<DERObjectIdentifier,
	 * String>(oid, conteudo); }
	 */
	@SuppressWarnings("unchecked")
	protected static Properties recuperaNomesAlternativos(final byte[] assinatura)
			throws InvalidKeyException, SecurityException, CRLException,
			CertificateException, NoSuchProviderException,
			NoSuchAlgorithmException, SignatureException, AplicacaoException,
			ChainValidationException, IOException, CMSException,
			CertStoreException {

		final CMSSignedData signedData = new CMSSignedData(assinatura);

		//		CertStore certs = signedData.getCertificatesAndCRLs("Collection", "BC");
		Store certs = signedData.getCertificates();
		SignerInformationStore signers = signedData.getSignerInfos();
		Collection<SignerInformation> c = signers.getSigners();
		Iterator<SignerInformation> it = c.iterator();

		@SuppressWarnings("unused")
		String sCN = "";

		while (it.hasNext()) {
			SignerInformation signer = it.next();
			//			Collection certCollection = certs.getCertificates(signer.getSID());
			Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());

			@SuppressWarnings("unused")
			String ss = signer.getDigestAlgOID();
			@SuppressWarnings("unused")
			String sss = signer.getDigestAlgorithmID().getObjectId().getId();

			Iterator<X509CertificateHolder> certIt = certCollection.iterator();
			X509CertificateHolder certHolder = certIt.next();
			X509Certificate cert = AssinaturaDigital.getX509Certificate(certHolder);

			/*
			 *  *** código comentado movido para
			 * Certificado.recuperarPropriedadesNomesAlteranativos(cert)*****
			 * ATENÇÃO: Código sempre retorna na primeira iteração do for ?!!***
			 * (LAGS) Properties props = new Properties(); for (List<?>
			 * subjectAlternativeName : cert .getSubjectAlternativeNames()) {
			 * String email; Pair<DERObjectIdentifier, String> otherName;
			 * 
			 * @SuppressWarnings("unused") int pos;
			 * 
			 * // O primeiro elemento é um Integer com o valor 0 = otherName, 1
			 * // = // rfc822name etc. // O segundo valor é um byte array ou uma
			 * String. Veja o javadoc // de // getSubjectAlternativeNames.
			 * switch (((Number) subjectAlternativeName.get(0)).intValue()) {
			 * case 0: // OtherName - contém CPF, CNPJ etc. // o OID fica em
			 * otherName.first otherName = getOtherName((byte[])
			 * subjectAlternativeName .get(1));
			 * props.put(otherName.first.getId(), otherName.second); break; case
			 * 1: // rfc822Name - usado para email email = (String)
			 * subjectAlternativeName.get(1); props.put("email", email); break;
			 * default: break; } } return props;
			 */
			return CertificadoUtil.recuperarPropriedadesNomesAlteranativos(cert);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	static protected SignedData includeCrls(byte[] assinatura, Collection crls)
			throws IOException, Exception, SecurityException, CRLException,
			NoSuchProviderException, NoSuchAlgorithmException {

		org.bouncycastle.asn1.pkcs.SignedData pkcs7 = pkcs7SignedData(assinatura);

		ContentInfo content = new ContentInfo(CMSObjectIdentifiers.data, null);

		SignedData signedCms = new SignedData(pkcs7.getDigestAlgorithms(),
				content, pkcs7.getCertificates(), pkcs7.getCRLs(),
				pkcs7.getSignerInfos());

		ASN1EncodableVector vec = new ASN1EncodableVector();

		for (X509CRLObject crl : (Collection<X509CRLObject>) crls)
			vec.add(ASN1Primitive.fromByteArray(crl.getEncoded()));

		DERSet set = new DERSet(vec);

		// for (X509CRLObject crl : (Collection<X509CRLObject>) crls)
		// set.addObject(ASN1Object.fromByteArray(crl.getEncoded()));

		SignedData signedCmsWithCrls = new SignedData(
				signedCms.getDigestAlgorithms(),
				signedCms.getEncapContentInfo(), signedCms.getCertificates(),
				set, signedCms.getSignerInfos());
		signedCmsWithCrls.getCertificates();
		signedCmsWithCrls.getCRLs();
		return signedCmsWithCrls;
	}

	/**
	 * Read an existing PKCS#7 object from a DER encoded byte array
	 */
	protected static org.bouncycastle.asn1.pkcs.SignedData pkcs7SignedData(byte[] in) {
		ASN1InputStream din = new ASN1InputStream(new ByteArrayInputStream(in));

		//
		// Basic checks to make sure it's a PKCS#7 SignedData Object
		//
		ASN1Primitive pkcs;

		try {
			pkcs = din.readObject();
		} catch (IOException e) {
			throw new SecurityException("can't decode PKCS7SignedData object");
		}finally{
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!(pkcs instanceof ASN1Sequence)) {
			throw new SecurityException(
					"Not a valid PKCS#7 object - not a sequence");
		}

		ContentInfo content = ContentInfo.getInstance(pkcs);

		org.bouncycastle.asn1.pkcs.SignedData data = org.bouncycastle.asn1.pkcs.SignedData
				.getInstance(content.getContent());

		return data;
	}

	/**
	 * Read an existing PKCS#7 object from a DER encoded byte array
	 */
	protected static org.bouncycastle.asn1.cms.SignedData cmsSignedData(byte[] in) {
		ASN1InputStream din = new ASN1InputStream(new ByteArrayInputStream(in));

		//
		// Basic checks to make sure it's a PKCS#7 SignedData Object
		//
		ASN1Primitive cms;

		try {
			cms = din.readObject();
		} catch (IOException e) {
			throw new SecurityException("can't decode CMSSignedData object");
		}finally{
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!(cms instanceof ASN1Sequence)) {
			throw new SecurityException(
					"Not a valid PKCS#7 object - not a sequence");
		}

		ContentInfo content = ContentInfo.getInstance(cms);

		org.bouncycastle.asn1.cms.SignedData data = org.bouncycastle.asn1.cms.SignedData
				.getInstance(content.getContent());

		return data;
	}

	@SuppressWarnings("unchecked")
	protected static Store buscarCrlParaCadaCertificado(Store certs)throws CertStoreException, Exception {
		X509Certificate[] cadeiaTotal = montarCadeiaOrdenadaECompleta(certs.getMatches(null));

		List certList = new ArrayList();
		for (X509Certificate cert : cadeiaTotal)
			certList.add(cert);
		for (X509CRLObject crl : (Collection<X509CRLObject>) X509ChainValidator.getCRLs(cadeiaTotal))
			certList.add(crl);
		// certList.add(ASN1Object.fromByteArray(crl.getEncoded()));


		return new JcaCertStore(certList);
	}

	@SuppressWarnings("static-access")
	protected static byte[] converterPkcs7EmCMSComCertificadosECRLs(
			final byte[] assinatura) throws Exception {
		CMSSignedData cmssd = new CMSSignedData(assinatura);

		Store certs = cmssd.getCertificates();
		Store certsAndCrls = buscarCrlParaCadaCertificado(certs);
		CMSSignedData cmssdcrl = cmssd.replaceCertificatesAndCRLs(cmssd, certsAndCrls, certsAndCrls, certsAndCrls);

		return cmssdcrl.getEncoded();
	}

	protected static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);

		for (int i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		return strbuf.toString();
	}

	@SuppressWarnings("unchecked")
	protected static void main(String[] args) throws Exception {
		byte[] pdf;
		{
			File f = new File("c:/trabalhos/java/teste.pdf");
			FileInputStream fin = new FileInputStream(f);
			pdf = new byte[(int) f.length()];
			fin.read(pdf);
			fin.close();
		}

		PdfReader reader = new PdfReader(pdf);
		FileOutputStream fout = new FileOutputStream(
				"c:/trabalhos/java/teste_assinado.pdf");

		final int SIZE = 256000;

		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
		PdfSignatureAppearance sap = stp.getSignatureAppearance();

		PdfDictionary dic = new PdfDictionary();
		dic.put(PdfName.TYPE, PdfName.SIG);
		dic.put(PdfName.FILTER, new PdfName("Adobe.PPKMS"));
		dic.put(PdfName.SUBFILTER, new PdfName("adbe.pkcs7.detached"));

		sap.setCryptoDictionary(dic);
		HashMap exc = new HashMap();
		exc.put(PdfName.CONTENTS, new Integer(SIZE));
		sap.setSignDate(Calendar.getInstance());
		sap.preClose(exc);

		byte[] data = streamToByteArray(sap.getRangeStream());
		FileOutputStream fout2 = new FileOutputStream(
				"c:/trabalhos/java/teste_hash.b64");
		fout2.write(Base64.encode(data).getBytes());
		fout2.close();
		File f = new File("c:/trabalhos/java/teste_sign.b64");
		FileInputStream fin = new FileInputStream(f);
		byte[] signatureB64 = new byte[(int) f.length()];
		fin.read(signatureB64);
		@SuppressWarnings("unused")
		StringBuilder sb = new StringBuilder();
		byte[] signature1 = Base64.decode(new String(signatureB64));
		fin.close();
		byte[] A_CP = converterPkcs7EmCMSComCertificadosECRLs(signature1);
		CMSSignedData A_T = TimeStamper.addTimestamp(new CMSSignedData(A_CP));
		// verificarAssinaturaCMS(conteudo, A_T.getEncoded(), dtAssinatura);
		byte[] signature = A_T.getEncoded();

		byte[] outc = new byte[(SIZE - 2) / 2];
		System.arraycopy(signature, 0, outc, 0, signature.length);
		PdfDictionary dic2 = new PdfDictionary();

		dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		sap.close(dic2);
	}

	@SuppressWarnings("unchecked")
	protected static void addSignatureToPDF(byte[] pdf, byte[] signature)
			throws Exception {
		PdfReader reader = new PdfReader(pdf);
		FileOutputStream fout = new FileOutputStream(
				"c:/trabalhos/java/teste_assinado.pdf");

		final int SIZE = 128000;

		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
		PdfSignatureAppearance sap = stp.getSignatureAppearance();

		PdfDictionary dic = new PdfDictionary();
		dic.put(PdfName.TYPE, PdfName.SIG);
		dic.put(PdfName.FILTER, new PdfName("Adobe.PPKMS"));
		dic.put(PdfName.SUBFILTER, new PdfName("adbe.pkcs7.detached"));

		sap.setCryptoDictionary(dic);
		HashMap exc = new HashMap();
		exc.put(PdfName.CONTENTS, new Integer(SIZE));
		sap.preClose(exc);

		byte[] data = streamToByteArray(sap.getRangeStream());
		FileOutputStream fout2 = new FileOutputStream(
				"c:/trabalhos/java/teste_hash.b64");
		fout2.write(Base64.encode(data).getBytes());
		fout2.close();
		File f = new File("c:/trabalhos/java/teste_sign.b64");
		FileInputStream fin = new FileInputStream(f);
		byte[] signatureB64 = new byte[(int) f.length()];
		fin.read(signatureB64);
		@SuppressWarnings("unused")
		StringBuilder sb = new StringBuilder();
		byte[] signature1 = Base64.decode(new String(signatureB64));
		fin.close();
		byte[] A_CP = converterPkcs7EmCMSComCertificadosECRLs(signature1);
		CMSSignedData A_T = TimeStamper.addTimestamp(new CMSSignedData(A_CP));
		// verificarAssinaturaCMS(conteudo, A_T.getEncoded(), dtAssinatura);
		signature = A_T.getEncoded();

		byte[] outc = new byte[(SIZE - 2) / 2];
		System.arraycopy(signature, 0, outc, 0, signature.length);
		PdfDictionary dic2 = new PdfDictionary();

		dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		sap.close(dic2);
	}

	@SuppressWarnings("unchecked")
	protected static byte[] getHasheableRangeFromPDF(byte[] pdf) throws Exception {
		PdfReader reader = new PdfReader(pdf);
		OutputStream fout = new NullOutputStream();

		final int SIZE = 128000;

		PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
		PdfSignatureAppearance sap = stp.getSignatureAppearance();

		PdfDictionary dic = new PdfDictionary();
		dic.put(PdfName.TYPE, PdfName.SIG);
		dic.put(PdfName.FILTER, new PdfName("Adobe.PPKMS"));
		dic.put(PdfName.SUBFILTER, new PdfName("adbe.pkcs7.detached"));

		sap.setCryptoDictionary(dic);
		HashMap exc = new HashMap();
		exc.put(PdfName.CONTENTS, new Integer(SIZE));
		sap.preClose(exc);

		byte[] data = streamToByteArray(sap.getRangeStream());

		byte[] outc = new byte[(SIZE - 2) / 2];
		PdfDictionary dic2 = new PdfDictionary();

		dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
		sap.close(dic2);
		System.out
		.println("Hash: "
				+ MessageDigest.getInstance("MD5").digest(data, 0,
						data.length));
		return data;
	}

	private static byte[] streamToByteArray(InputStream stream)
			throws Exception {
		if (stream == null) {
			return null;
		} else {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024];
			int c = 0;
			while ((c = stream.read(buffer)) > 0) {
				byteArray.write(buffer, 0, c);
			}
			byteArray.flush();
			return byteArray.toByteArray();
		}
	}

	@SuppressWarnings("unchecked")
	protected static String validarAssinaturaCMS(byte[] digest,
			String digestAlgorithm, byte[] assinatura, Date dtAssinatura)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		final CMSSignedData s;
		if (digest != null) {
			Map<String, byte[]> map = new HashMap<String, byte[]>();
			map.put(digestAlgorithm, digest);
			s = new CMSSignedData(map, assinatura);
		} else {
			s = new CMSSignedData(assinatura);
		}


		Store certs = s.getCertificates();
		SignerInformationStore signers = s.getSignerInfos();
		Collection<SignerInformation> c = signers.getSigners();
		Iterator<SignerInformation> it = c.iterator();
		X509CertificateHolder firstSignerCert = null;

		while (it.hasNext()) {
			SignerInformation signer = it.next();
			Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());

			Iterator<X509CertificateHolder> certIt = certCollection.iterator();
			X509CertificateHolder cert = certIt.next();
			if (firstSignerCert == null)
				firstSignerCert = cert;

			if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert)))
				throw new Exception("Assinatura inválida!");


			System.out.println("\nSigner Info: \n");
			System.out.println("Is Signature Valid? true");
			System.out.println("Digest: " + asHex(signer.getContentDigest()));
			System.out.println("Enc Alg Oid: " + signer.getEncryptionAlgOID());
			System.out.println("Digest Alg Oid: " + signer.getDigestAlgOID());
			System.out.println("Signature: " + asHex(signer.getSignature()));

		}

		//		X509Certificate[] cadeiaTotal = montarCadeiaOrdenadaECompleta((Collection<X509Certificate>) (certs.getCertificates(null)));
		X509Certificate[] cadeiaTotal = montarCadeiaOrdenadaECompleta(certs.getMatches(null));

		List<X509CRLObject> crls = new ArrayList<>();
		if (certs.getMatches(null) != null){
			Enumeration ec = ASN1Set.getInstance(certs.getMatches(null)).getObjects();

			while (ec.hasMoreElements()){
				crls.add(new X509CRLObject(CertificateList.getInstance(ec.nextElement())));
			}
		}

		final X509ChainValidator cadeia = new X509ChainValidator(cadeiaTotal,
				/* trustedAnchors */new HashSet(
						FachadaDeCertificadosAC.getTrustAnchors()), crls.toArray(new X509CRLObject[0]));
		
		cadeia.checkCRL(true);
		
		try {
			cadeia.validateChain(dtAssinatura);
		} catch (Exception e1) {
			if (e1.getMessage().endsWith("Validation time is in future.")) {
				String s1 = e1.getMessage() + " Current date: ["
						+ new Date().toString() + "]. Record date: ["
						+ dtAssinatura + "]. LCRs' dates [";
				for (X509CRLObject crl : (Collection<X509CRLObject>) certs.getMatches(null)) {
					String s2 = crl.getIssuerX500Principal().getName();
					s2 = s2.split(",")[0];

					s1 += s2 + " (" + crl.getThisUpdate() + " - "
							+ crl.getNextUpdate() + ") ";
				}
				s1 += "]";
				throw new AplicacaoException(s1, 0, e1);
			} else
				throw e1;
		}


		//		String s1 = firstSignerCert.getSubjectDN().getName();
		String s1 = firstSignerCert.getSubject().toString();
		s1 = obterNomeExibicao(s1);

		return s1;
	}

	@SuppressWarnings("unchecked")
	protected static String validarAssinaturaCMSeCarimboDeTempo(
			final byte[] digest, final String digestAlgorithm,
			final byte[] assinatura, Date dtAssinatura)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		String nome = validarAssinaturaCMS(digest, digestAlgorithm, assinatura,
				dtAssinatura);

		Map<String, byte[]> map = new HashMap<String, byte[]>();
		map.put(digestAlgorithm, digest);
		final CMSSignedData s = new CMSSignedData(map, assinatura);

		Collection ss = s.getSignerInfos().getSigners();
		SignerInformation si = (SignerInformation) ss.iterator().next();

		Attribute attr = si.getUnsignedAttributes().get(
				PKCSObjectIdentifiers.id_aa_signatureTimeStampToken);
		CMSSignedData cmsTS = new CMSSignedData(attr.getAttrValues().getObjectAt(0).toASN1Primitive().getEncoded());

		TimeStampToken tok = new TimeStampToken(cmsTS);
		Store cs = tok.getCertificates();

		SignerId signer_id = tok.getSID();
		BigInteger cert_serial_number = signer_id.getSerialNumber();
		Collection certs = cs.getMatches(null);
		Iterator iter = certs.iterator();
		X509Certificate certificate = null;
		while (iter.hasNext()) {
			X509Certificate cert = (X509Certificate) iter.next();
			if (cert_serial_number != null) {
				if (cert.getSerialNumber().equals(cert_serial_number)) {
					certificate = cert;
				}
			} else {
				if (certificate == null) {
					certificate = cert;
				}
			}
		}

		tok.validate(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(certificate));
		// Nato: falta validar as CRLs do carimbo de tempo

		if (!Arrays.equals(tok.getTimeStampInfo().getMessageImprintDigest(),
				MessageDigest.getInstance("SHA1").digest(si.getSignature()))) {
			throw new Exception(
					"Carimbo de tempo não confere com o resumo do documento");
		}

		try {
			validarAssinaturaCMS(null, null, cmsTS.getEncoded(), tok
					.getTimeStampInfo().getGenTime());
		} catch (Exception e) {
			throw new Exception("Carimbo de tempo inválido!", e);
		}

		return nome;
	}

	@SuppressWarnings("unchecked")
	public static String validarAssinaturaPKCS7(final byte[] digest,
			final String digestAlgorithm, final byte[] assinatura,
			Date dtAssinatura, boolean verificarLCRs)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		Map<String, byte[]> map = new HashMap<String, byte[]>();
		map.put(digestAlgorithm, digest);
		final CMSSignedData signedData = new CMSSignedData(map, assinatura);

		Store certs = signedData.getCertificates();
		SignerInformationStore signers = signedData.getSignerInfos();
		Collection<SignerInformation> c = signers.getSigners();
		Iterator<SignerInformation> it = c.iterator();

		String sCN = "";

		while (it.hasNext()) {
			SignerInformation signer = it.next();
			Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());

			@SuppressWarnings("unused")
			String ss = signer.getDigestAlgOID();
			@SuppressWarnings("unused")
			String sss = signer.getDigestAlgorithmID().getObjectId().getId();

			Iterator<X509CertificateHolder> certIt = certCollection.iterator();
			X509CertificateHolder certHolder = certIt.next();
			X509Certificate cert = AssinaturaDigital.getX509Certificate(certHolder);

			if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(certHolder)))
				throw new Exception("Assinatura inválida!");

			X509Certificate[] cadeiaTotal = montarCadeiaOrdenadaECompleta(certCollection);

			final X509ChainValidator cadeia = new X509ChainValidator(
					cadeiaTotal, /* trustedAnchors */new HashSet(
							FachadaDeCertificadosAC.getTrustAnchors()), null);
			cadeia.checkCRL(verificarLCRs);
			cadeia.validateChain(dtAssinatura);

			String s2 = cert.getSubjectDN().getName();
			s2 = obterNomeExibicao(s2);
			if (sCN.length() != 0)
				sCN += ", ";
			sCN += s2;
		}

		return sCN.length() == 0 ? null : sCN;
	}

	private static String obterNomeExibicao(String s) {
		s = s.split("CN=")[1];
		s = s.split(",")[0];
		// Retira o CPF, se houver
		String[] splitted = s.split(":");
		if (splitted.length == 2 && Pattern.compile("[0-9]{11}").matcher(splitted[1]).matches())
			return splitted[0];
		return s;
	}

	private static String verificarAssinaturaCMS(final byte[] conteudo,
			final byte[] assinatura, Date dtAssinatura)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		return validarAssinaturaCMS(
				MessageDigest.getInstance("SHA1").digest(conteudo),
				"1.3.14.3.2.26", assinatura, dtAssinatura);

	}

	protected static String verificarAssinaturaCMSeCarimboDeTempo(
			final byte[] conteudo, final byte[] assinatura, Date dtAssinatura)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		return validarAssinaturaCMSeCarimboDeTempo(
				MessageDigest.getInstance("SHA1").digest(conteudo),"1.3.14.3.2.26", assinatura, dtAssinatura);
	}

	@SuppressWarnings("unused")
	private static String verificarAssinaturaPKCS7(final byte[] conteudo,
			final byte[] assinatura, Date dtAssinatura, boolean verificarLCRs)
					throws InvalidKeyException, SecurityException, CRLException,
					CertificateException, NoSuchProviderException,
					NoSuchAlgorithmException, SignatureException, AplicacaoException,
					ChainValidationException, IOException, Exception {

		return validarAssinaturaPKCS7(
				MessageDigest.getInstance("SHA1").digest(conteudo),
				"1.3.14.3.2.26", assinatura, dtAssinatura, verificarLCRs);

	}

	protected static byte[] converterPkcs7EmCMSComCertificadosCRLsECarimboDeTempo(
			byte[] pkcs7) throws Exception {
		byte[] A_CP = converterPkcs7EmCMSComCertificadosECRLs(pkcs7);
		CMSSignedData A_T = TimeStamper.addTimestamp(new CMSSignedData(A_CP));
		return A_T.getEncoded();

		// verificarAssinaturaCMS(conteudo, A_T.getEncoded(), dtAssinatura);
		//
		// addSignatureToPDF(conteudo, A_T.getEncoded());
		//
		// FileOutputStream fout = new FileOutputStream(
		// "c:/trabalhos/java/sign.pdf");
		// fout.write(conteudo);
		// fout.close();
		//
		// FileOutputStream fout2 = new FileOutputStream(
		// "c:/trabalhos/java/sign.cms");
		// fout2.write(A_T.getEncoded());
		// fout2.close();
	}

	public static String recuperarCPF(final byte[] assinatura) throws Exception {
		try {
			Properties props = AssinaturaDigital.recuperaNomesAlternativos(assinatura);
			String sCPF = props.getProperty("2.16.76.1.3.1").substring(8, 19);
			@SuppressWarnings("unused")
			long lCPF = Long.valueOf(sCPF);
			return sCPF;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível obter o CPF do assinante", 0, e);
		}
	}
	
	public static X509Certificate getX509Certificate(X509CertificateHolder holder) throws CertificateException{
		return new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate( holder );
	}

}
