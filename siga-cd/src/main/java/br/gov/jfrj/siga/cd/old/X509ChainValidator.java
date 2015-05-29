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
package br.gov.jfrj.siga.cd.old;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.bouncycastle.jce.provider.X509CRLObject;

/**
 * Classe responsável por realizar a validação de um certificado. É verificada
 * toda a cadeia de certificação, assim como as CRLs dos certificados existentes
 * na cadeia. Por default, X509ChainValidator não faz a verificação das CRLs.
 * Para tanto deve-se chamar o método checkCRL(true).
 * 
 * @author mparaiso
 */
public class X509ChainValidator {
	/** */
	private Vector<X509Certificate> certChain;

	/** */
	private Set trustedAnchors;

	/** */
	private Collection crls;

	/** */
	private boolean checkCRL = false;

	private X509Certificate certificates[];

	public Collection getCrls() {
		return crls;
	}

	/**
	 * Método interno para ler todas CRLs de todos os certificados passados para
	 * o objeto.
	 * 
	 * Este método varre todos os certificados passados para este objeto no
	 * construtor e monta uma lista com as CRLs dos certificados. Isso é feito
	 * para que se possa testar o status de revogação dos mesmos. É importante
	 * lembrar que este método normalmente só é utilizado caso a verificação de
	 * CRL estiver habilitada.
	 * 
	 * Este método usa o CRLLocator para abrir o certificado e baixar a CRL.
	 * 
	 * @see br.gov.jfrj.siga.cd.old.com.certisign.utlis.CRLLocator
	 * 
	 * @throws ChainValidationException
	 */
	public static Collection<X509CRLObject> getCRLs(X509Certificate[] certChain)
			throws ChainValidationException {
		Collection<X509CRLObject> crls = new Vector<X509CRLObject>();

		try {
			for (X509Certificate cert : certChain) {
				final CRLLocator crl = new CRLLocator(cert);
				if (crl.uri != null)
					crls.add(crl.getCRL());
			}
		} catch (final InvalidCRLException e) {
			throw new ChainValidationException(
					"Falha ao carregar a CRL do certificado! ", e);
		} catch (CRLException e) {
			throw new ChainValidationException("Falha na CRL do certificado! ",
					e);
		}
		return crls;
	}

	/**
	 * Este método gera, a partir de uma cadeia de certificados completa, uma
	 * cadeia de certificados válida para o algorítmo de validação de cadeias
	 * PKIX. É levado em consideração que a cadeia de certificados passada como
	 * parâmetro esteja bem formada e em ordem crescente, ou seja, o primeiro
	 * certificado deve ser o certificado a ser autenticado, seguido do
	 * certificado do seu emissor (AC Intermediária) e assim por diante.
	 * 
	 * @param certificados -
	 *            Cadeia de certificados completa, incluindo o certificado raiz.
	 * @return A cadeia de certificados sem o certificado raiz.
	 */
	private Vector getWellFormedChain(final X509Certificate certificates[]) {

		final Vector wellFormedChain = new Vector();

		// O certificado raiz é descartado, considerando-se que é o último da
		// matriz de certificados passada.
		for (int index = 0; index < certificates.length - 1; index++) {
			wellFormedChain.add(certificates[index]);
		}

		return wellFormedChain;
	}

	/**
	 * Construtor para X509ChainValidator. São passados a cadeia de certificados
	 * a ser validada, assim como os roots válidos para a validação da cadeia.
	 * 
	 * @param certCadeia
	 *            Deve ser uma cadeia de certificados válida, de acordo com o
	 *            PKIX.
	 * @param trustedAnchors
	 *            Um ou mais certificados raiz.
	 */
	public X509ChainValidator(final X509Certificate certChain[],
			final Set trustedAnchors, final X509CRLObject crlArray[]) {
		this.certificates = certChain;
		this.certChain = this.getWellFormedChain(certChain);
		this.trustedAnchors = trustedAnchors;
		if (crlArray != null) {
			crls = new Vector();

			for (X509CRLObject crl : crlArray) {
				this.crls.add(crl);
			}
		}
	}

	/**
	 * Este método realiza a validação da cadeia de certificados. Caso checkCRL
	 * esteja true, então é feita a validação de revogação dos certificados em
	 * relação as suas CRLs. Todos os certificados da cadeia são verifificados,
	 * não apenas o certificados apresentado, mas também os das ACs
	 * intermediárias. Caso a cadeia de certificados esteja válida, então
	 * validateChain retorna void. Caso contrário uma exceção é lançada.
	 * 
	 * @throws ChainValidationException
	 *             indica que houve um problema na validação da cadeia.
	 */
	public void validateChain(Date dtSigned) throws ChainValidationException {
		try {
			final CertificateFactory cf = CertificateFactory.getInstance(
					"X.509", "BC");
			// CertificateFactory cf = CertificateFactory.getInstance( "X.509");
			final CertPath cp = cf.generateCertPath(this.certChain);

			final PKIXParameters params = new PKIXParameters(trustedAnchors);

			params.setExplicitPolicyRequired(false);// Nao obrigatorio, pois
			// false e o default
			params.setRevocationEnabled(this.checkCRL);
			// params.setRevocationEnabled(false);

			if (this.checkCRL) {
				if (crls == null)
					crls = getCRLs(certChain
							.toArray(new X509Certificate[certChain.size()]));
				final Collection col = new ArrayList();
				col.addAll(this.crls);
				for (X509Certificate cert : this.certificates)
					col.add(cert);
				final CollectionCertStoreParameters csParams = new CollectionCertStoreParameters(
						col);
				final CertStore cs = CertStore.getInstance("Collection",
						csParams);
				final List certStores = new Vector();
				certStores.add(cs);
				params.setCertStores(certStores);
			}

			params.setTrustAnchors(this.trustedAnchors);
			params.setDate(dtSigned);
			
			final CertPathValidator cpv = CertPathValidator.getInstance("PKIX",
					"BC");
			// CertPathValidator cpv = CertPathValidator.getInstance("PKIX");

			PKIXCertPathValidatorResult result = null;

			// Estamos com o seguinte problema: Quando utilizamos as rotinas da
			// SUN, funciona, mas seria necessário possuir todas as CRLs,
			// inclusive as mais antiga, pois quando informamos a data, ele
			// exclui CRLs que nao estão válidas nessa data.

			result = (PKIXCertPathValidatorResult) cpv.validate(cp, params);
		} catch (final CertificateException e) {
			throw new ChainValidationException(
					"Falha na criação do caminho dos certificados (CertPath)!"
							+ " Verifique se a cadeia de certificados existente é uma válida!\n",
					e);
		} catch (final InvalidAlgorithmParameterException e) {
			throw new ChainValidationException(
					"Falha na leitura dos certificados raizes (TrustedAnchor)! "
							+ "Verifique se os certificados raizes passados são válidos!\n",
					e);
		} catch (final NoSuchAlgorithmException e) {
			throw new ChainValidationException(
					"Falha na criação do CertStore! Os parâmetros passados"
							+ " para a criação do CertStore podem estar com problemas!\n",
					e);
		} catch (final NoSuchProviderException e) {
			throw new ChainValidationException(
					"O provedor criptográfico especificado não está disponível!\n",
					e);
		} catch (final CertPathValidatorException e) {
			// for (X509CRLObject x : (Collection<X509CRLObject>) this.crls)
			// System.out.println(x.getIssuerDN() + " - " + x.getThisUpdate()
			// + " - " + x.getNextUpdate());
			//
			throw new ChainValidationException(
					"Não foi possível validar a cadeia de certificados!\n Caso as CRLs"
							+ " tenham sido verificadas é possível que algum certificado da cadeia esteja revogado!\n"
							+ e);
		}
	}

	/**
	 * Por padrão a verificação de CRLs fica desabilitada. Para habilitá-la é
	 * necessário setar checkCRL(true)
	 * 
	 * @param checkCRL -
	 *            True para verificar as CRLs da cadeia de certificados
	 */
	public void checkCRL(final boolean checkCRL) {
		this.checkCRL = checkCRL;
	}

	/**
	 * Método para descobrir se a verificação de CRLs será usada ou não.
	 * 
	 * @return Estado atual da verificação de CRL.
	 */
	public boolean isCheckCRL() {
		return this.checkCRL;
	}

}
