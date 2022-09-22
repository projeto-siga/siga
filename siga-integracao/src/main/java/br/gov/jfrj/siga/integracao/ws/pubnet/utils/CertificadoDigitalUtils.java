package br.gov.jfrj.siga.integracao.ws.pubnet.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

public class CertificadoDigitalUtils {

	public String assinarConteudoComCertificado(X509Certificate cert, String pass, String text) throws Exception {
		char[] passwordChar = pass.toCharArray();
		Security.addProvider(new BouncyCastleProvider());
		
		Certificate[] certChain = new Certificate[1];
        certChain[0] = cert;
		
		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(null, passwordChar);
		//ks.setKeyEntry("fernando_henrique_pascott_32417729857.p12", pass.getBytes(), new Certificate[] {cert});
		//ks.setCertificateEntry("fernando_henrique_pascott_32417729857.p12", cert);
		//ks.setKeyEntry("fernando_henrique_pascott_32417729857.p12", new KeyStore.PasswordProtection(passwordChar), certChain);
		String alias = ks.aliases().nextElement();
		

		PrivateKey pKey = (PrivateKey) ks.getKey(alias, null);
		// System.out.println("certificate is: " + cert);

		List<X509CertificateHolder> certList = new ArrayList<X509CertificateHolder>();

		X509CertificateHolder certificateHolder = new X509CertificateHolder(cert.getEncoded());
		certList.add(certificateHolder);
		Store certs = new JcaCertStore(certList);

		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		JcaSimpleSignerInfoGeneratorBuilder builder = new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC")
				.setDirectSignature(true);

		gen.addSignerInfoGenerator(builder.build("SHA1withRSA", pKey, cert));
		gen.addCertificates(certs);

		CMSTypedData msg = new CMSProcessableByteArray(text.getBytes());
		CMSSignedData s = gen.generate(msg, true);

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DEROutputStream dOut = new DEROutputStream(bOut);
		dOut.writeObject(s.toASN1Structure().toASN1Primitive());
		dOut.close();
		byte[] encoded = bOut.toByteArray();

		saveFile(encoded);

		String textCms = byteArrayToHex(encoded);
		System.out.println("Print CMS" + textCms);
		return textCms;
	}

	private void saveFile(byte[] encodedPKCS7) {
		File file = new File("C:/temp/wspn/outputfile.p7s");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(encodedPKCS7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String byteArrayToHex(byte[] byteArray) {
		StringBuilder hex = new StringBuilder(byteArray.length * 2);
		for (byte b : byteArray)
			hex.append(String.format("%02x", b));
		System.out.println("Print Hex: " + hex.toString().toLowerCase());
		return hex.toString().toLowerCase();
	}

	public static String CriarMD5DevolverHex (String texto) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(texto.getBytes());
		byte[] digest = md.digest();
		return byteArrayToHex(digest);
	}

}
