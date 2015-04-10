package br.gov.jfrj.siga.cd.ext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cd.ChainValidationException;
import br.gov.jfrj.siga.cd.ICdAssinaturaDigital;
import br.gov.jfrj.siga.cd.X509ChainValidator;
import br.gov.jfrj.siga.cd.ac.FachadaDeCertificadosAC;

import com.ittru.ccws.webservice.SignCompare;
import com.ittru.ccws.webservice.SignService;
import com.ittru.ccws.webservice.SignServiceImplService;

public class CdAssinaturaDigitalExt implements ICdAssinaturaDigital {
	private static final int FALLBACK_LIMIT = 2048;

	private java.net.URL getResource(String name) {
		name = resolveName(name);
		ClassLoader cl = this.getClass().getClassLoader();
		if (cl == null) {
			return ClassLoader.getSystemResource(name); // A system class.
		}
		return cl.getResource(name);
	}

	private String resolveName(String name) {
		if (name == null) {
			return name;
		}
		if (!name.startsWith("/")) {
			Class c = this.getClass();
			while (c.isArray()) {
				c = c.getComponentType();
			}
			String baseName = c.getName();
			int index = baseName.lastIndexOf('.');
			if (index != -1) {
				name = baseName.substring(0, index).replace('.', '/') + "/"
						+ name;
			}
		} else {
			name = name.substring(1);
		}
		return name;
	}

	private String obtemPolitica(byte[] assinatura) {
		String politica = null;
		try {
			SignService signServ = getSignService();
			SignCompare sc = signServ.extractSignCompare(Base64
					.encodeBase64String(assinatura));

			politica = sc.getPsOid();
			// CMSSignedMessage cms = (CMSSignedMessage) CMSSignedMessage
			// .getCMSInstance(assinatura);
			// CMSSignerInformation sigInfo = cms.getSignerInformation(0);
			// X509Attribute attrPol =
			// sigInfo.getSignedAttributes().getAttribute(
			// ETSISignaturePolicyAttribute.OID);
			// if (attrPol == null)
			// return null;
			// ETSISignaturePolicyAttribute spa = (ETSISignaturePolicyAttribute)
			// attrPol;
			// ETSISignaturePolicyId policyId = spa.getSignaturePolicyId();
			// politica = policyId.getSigPolicyId();
		} catch (Exception e) {
		}
		return politica;
	}

	@Override
	public String validarAssinatura(byte[] assinatura, byte[] documento,
			Date dtAssinatura, boolean verificarLCRs) throws Exception {
		SignService signServ = getSignService();
		String politica = obtemPolitica(assinatura);
		
		String sAssinatura = Base64.encodeBase64String(assinatura);
		
		String sCert = signServ.extractSignerCert(sAssinatura);
		String cn = signServ.getCertSubjectCn(sCert);
		String nome = obterNomeExibicao(cn);
		
		if (politica == null) {
			boolean f = signServ.validateSign(
					sAssinatura,
					Base64.encodeBase64String(documento), encodeDate(dtAssinatura),
					verificarLCRs);
			if (!f)
				return Service.ERRO
						+ "Não foi possível validar a assinatura digital";
			return nome;
			// return AssinaturaDigital.validarAssinaturaPKCS7(MessageDigest
			// .getInstance("SHA1").digest(documento), "1.3.14.3.2.26",
			// assinatura, dtAssinatura, verificarLCRs);
		} else {
			if (signServ.validateSignatureByPolicy(
					sAssinatura, null))
				return nome + " (" + recuperarNomePolitica(politica) + ")";
			else
				return Service.ERRO
						+ "Não foi possível validar a assinatura com política";
		}
	}
	
	private static String obterNomeExibicao(String s) {
		s = s.split(",")[0];
		// Retira o CPF, se houver
		String[] splitted = s.split(":");
		if (splitted.length == 2 && Pattern.compile("[0-9]{11}").matcher(splitted[1]).matches())
			return splitted[0];
		return s;
	}

	@Override
	public String recuperarCPF(byte[] cms) throws Exception {
		return null;
	}

	public byte[] produzPacoteAssinavel(byte[] certificado,
			byte[] certificadoHash, byte[] documento, boolean politica,
			Date dtAssinatura) throws Exception {
		try {
			SignService signServ = getSignService();
			X509Certificate c = loadCert(certificado);
			RSAPublicKey pubKey = (RSAPublicKey) c.getPublicKey();
			byte[] res = null;
			if (pubKey.getModulus().bitLength() >= FALLBACK_LIMIT) {
				byte[] origHash256 = calcSha256(documento);
				String resTmp = signServ.hashSignedAttribSha256(
						Base64.encodeBase64String(origHash256), encodeDate(dtAssinatura),
						Base64.encodeBase64String(c.getEncoded()));
				res = Base64.decodeBase64(resTmp);

			} else {
				byte[] origHash1 = calcSha1(documento);
				String resTmp = signServ.hashSignedAttribSha1(
						Base64.encodeBase64String(origHash1), encodeDate(dtAssinatura),
						Base64.encodeBase64String(c.getEncoded()));
				res = Base64.decodeBase64(resTmp);
			}

			return res;
		} catch (Exception e) {
			throw e;
		}
	}

	public byte[] validarECompletarPacoteAssinavel(byte[] certificado,
			byte[] documento, byte[] assinatura, boolean politica,
			Date dtAssinatura) throws Exception {
		try {
			byte[] res = null;
			SignService signServ = getSignService();
			X509Certificate c = loadCert(certificado);
			RSAPublicKey pubKey = (RSAPublicKey) c.getPublicKey();

			// String signStr =
			// signServ.extractSignature(Base64.encodeBase64String(assinatura));
			// byte[] sign = Base64.decodeBase64(signStr);
			byte[] sign = assinatura;

			if (pubKey.getModulus().bitLength() == FALLBACK_LIMIT) {
				byte[] origHash256 = calcSha256(documento);
				String resStr = signServ.composeBodySha256(
						Base64.encodeBase64String(sign),
						Base64.encodeBase64String(c.getEncoded()),
						Base64.encodeBase64String(origHash256), encodeDate(dtAssinatura));

				res = Base64.decodeBase64(resStr);
			} else {
				byte[] origHashSha1 = calcSha1(documento);
				String resStr = signServ.composeBodySha1(
						Base64.encodeBase64String(sign),
						Base64.encodeBase64String(c.getEncoded()),
						Base64.encodeBase64String(origHashSha1), encodeDate(dtAssinatura));

				res = Base64.decodeBase64(resStr);
			}
			return res;
		} catch (Exception e) {
			throw e;
		}
	}

	public com.ittru.ccws.webservice.SignService getSignService() {
		SignService signServ = SignServiceImplService.getIttruService();
		return signServ;
	}

	public X509Certificate loadCert(byte[] certificado)
			throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		X509Certificate c = (X509Certificate) cf
				.generateCertificate(new ByteArrayInputStream(certificado));
		return c;
	}

	private XMLGregorianCalendar encodeDate(Date now)
			throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(now);
		XMLGregorianCalendar date2 = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(c);
		return date2;
	}

	private static byte[] calcSha1(byte[] content)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	private static byte[] calcSha256(byte[] content)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA256");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	public static String recuperarNomePolitica(String oid) {
		try {
			Map<String, String> mapArquivosPolitica = SigaCdExtProperties
					.getArquivosPolitica();
			String s = mapArquivosPolitica.get(oid);
			if (s == null)
				return null;
			try {
				s = (String) s.subSequence(s.lastIndexOf("/") + 1,
						s.lastIndexOf(".der"));
			} catch (Exception e) {
				return null;
			}
			return s.replace("PA_", "").replace("_", "-");
		} catch (Exception e) {
			return null;
		}
	}

	// private static X509CertificateImpl getCertificadoDoAssinante(
	// PKCS7SignedMessage pacoteAssinatura) throws Exception {
	//
	// // Recupera o SignerInformation de índice 0.
	// SignerInformation sigInfo = pacoteAssinatura.getSignerInformation(0);
	//
	// // Busca o certificado do assinante através do serialNumber e issuer
	// X509CertificateImpl signerCert = findCertificate(pacoteAssinatura,
	// sigInfo.getSerialNumber(), sigInfo.getIssuer());
	//
	// // Se o certificado não existir, lança erro.
	// if (signerCert == null)
	// throw new Exception("O certificado do assinante não foi encontrado");
	//
	// return signerCert;
	// }
	//
	// private static X509CertificateImpl findCertificate(
	// PKCS7SignedMessage signedMessage, BigInteger serialNumber,
	// Principal issuer) {
	//
	// // Carrega lista de certificados contidas no PKCS#7
	// CertificateSet certSet = signedMessage.getCertificates();
	// X509Certificate[] certs = certSet.getCertificates();
	// for (int i = 0; i < certs.length; i++) {
	// // Para cada um dos certificados da lista, verifica se o
	// // serialNumber e issuer
	// // coincidem com o apresentado por parâmetro. Caso sim, retorna o
	// // certificado.
	// if (certs[i].getSerialNumber().equals(serialNumber)
	// && certs[i].getIssuerDN().equals(issuer))
	// return (X509CertificateImpl) certs[i];
	// }
	// // Retorna nothing para dizer que nenhum certificado foi encontrado
	// // com o serialNumber e issuer solicitados.
	// return null;
	// }

}
