package br.gov.jfrj.siga.integracao.ws.pubnet.teste;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

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

import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

public class CertificadoDigitalTeste {
	
	public static X509Certificate obterCertificado(String pass) throws Exception{
		String path = "C:/Users/pascott/Documents/pascott informatica/certificado_digital/FERNANDO_HENRIQUE_PASCOTT_32417729857.p12";
		Security.addProvider(new BouncyCastleProvider());
		char[] password = pass.toCharArray();

		FileInputStream fis = new FileInputStream(path);
		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(fis, password);

		String alias = ks.aliases().nextElement();
		//PrivateKey pKey = (PrivateKey) ks.getKey(alias, password);
		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		//cert.getEncoded();
		return cert;
	}

	public String obterReciboAssinadoHex(String text) throws Exception {
		String path = "C:/Users/pascott/Documents/pascott informatica/certificado_digital/FERNANDO_HENRIQUE_PASCOTT_32417729857.p12";
		String passFile = "123456";
		Security.addProvider(new BouncyCastleProvider());
		char[] password = passFile.toCharArray();

		FileInputStream fis = new FileInputStream(path);
		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(fis, password);

		String alias = ks.aliases().nextElement();
		PrivateKey pKey = (PrivateKey) ks.getKey(alias, password);
		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		// System.out.println("certificate is: " + cert);

		java.util.List certList = new ArrayList();

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
		//System.out.println(s.getEncoded());

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DEROutputStream dOut = new DEROutputStream(bOut);
		dOut.writeObject(s.toASN1Structure().toASN1Primitive());
		dOut.close();
		byte[] encoded = bOut.toByteArray();

		saveFile(encoded);

		String textCms = byteArrayToHex(encoded);
		System.out.println("Print Hex: " + textCms);
		return textCms;
	}

	public String lerCertificado(String texto) throws Exception {

//		String path = JOptionPane.showInputDialog("Informe o caminho do certificado\nExemplo:C:\\Users\\pascott\\Documents\\cert.pfx");
//		String passFile = JOptionPane.showInputDialog("Senha Certificado");
		// "C:/Users/pascott/Documents/pascott
		// informatica/certificado_digital/PASCOTT_SERVICOS_DE_INFORMATICA_LTDA_39845457000132_1616600455627051100.pfx"
		// "Mar@2021"

		String path = "C:/Users/pascott/Documents/pascott informatica/certificado_digital/pascott_teste.pfx";
		String passFile = "123456";
		FileInputStream fis = new FileInputStream(path);
		java.security.KeyStore ks = java.security.KeyStore.getInstance("PKCS12");
		ks.load(fis, passFile.toCharArray());

		Enumeration<String> enumeration = ks.aliases();
		String nameAlias = "";
		while (enumeration.hasMoreElements()) {
			String alias = enumeration.nextElement();
			//System.out.println("alias name: " + alias);
			nameAlias = alias;
//            Certificate certificate = ks.getCertificate(alias);
//            System.out.println(certificate.toString());

		}
		// Certificate cert = (X509Certificate)
		// ks.getCertificate("4bdd9195-43bb-4404-ad9f-48c5777c9367");

//		System.out.println("certificate is: " + cert);

		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(passFile.toCharArray());
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(nameAlias, entryPassword);

		// sign
		PrivateKey privKey = privateKeyEntry.getPrivateKey();
		Signature sign = Signature.getInstance("SHA256withRSA");
		sign.initSign(privKey);
		byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
		sign.update(bytes);
		byte[] signature = sign.sign();

		saveFile(signature);

		String text = byteArrayToHex(signature);
		System.out.println(text);
		return text;
	}

	public String lerCertificado(String texto, boolean roda) throws Exception {

//		String path = JOptionPane.showInputDialog("Informe o caminho do certificado\nExemplo:C:\\Users\\pascott\\Documents\\cert.pfx");
//		String passFile = JOptionPane.showInputDialog("Senha Certificado");

		String path = "C:/Users/pascott/Documents/pascott informatica/certificado_digital/pascott_teste.pfx";
		String passFile = "123456";

		// First load the keystore object by providing the p12 file path
		KeyStore clientStore = KeyStore.getInstance("Windows-MY");
		// replace testPass with the p12 password/pin
		// clientStore.load(new FileInputStream(path), passFile.toCharArray());

		clientStore.load(null, null);
		Enumeration<String> aliases = clientStore.aliases();
		String aliaz = "";
		while (aliases.hasMoreElements()) {
			aliaz = aliases.nextElement();
			System.out.println("---> alias : " + aliaz);
			if (clientStore.isKeyEntry(aliaz)) {
				System.out.println("---> alias escolhido : " + aliaz);
				break;
			}
		}
		X509Certificate c = (X509Certificate) clientStore.getCertificate(aliaz);

//        System.out.println("CERTIFICADO INICIO*******************************************");
//        
//        System.out.println("certificate is: " + c);
//        
//        System.out.println("CERTIFICADO FIM*******************************************");

		System.out.println("*****************>>>>>>>texto: " + texto);

		// Data to sign
		byte[] dataToSign = texto.getBytes("UTF8");

		int nread = dataToSign.length;
		// compute signature:
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign((PrivateKey) clientStore.getKey(aliaz, passFile.toCharArray()));
		signature.update(dataToSign, 0, nread);
		byte[] signedData = signature.sign();

		System.out.println(byteArrayToHex(dataToSign));
		// load X500Name
		X500Name xName = X500Name.asX500Name(c.getSubjectX500Principal());

		// load serial number
		BigInteger serial = c.getSerialNumber();

		// laod digest algorithm
		AlgorithmId digestAlgorithmId = new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid);// AlgorithmId.RSAEncryption_oid);
		// load signing algorithm
		AlgorithmId signAlgorithmId = new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid);// AlgorithmId.RSAEncryption_oid);

		// Create SignerInfo:
		SignerInfo sInfo = new SignerInfo(xName, serial, digestAlgorithmId, signAlgorithmId, signedData);
//		SignerInfo sInfo2 = new SignerInfo(xName1, serial, digestAlgorithmId, signAlgorithmId, signedData);
		// Create ContentInfo:
		ContentInfo cInfo = new ContentInfo(ContentInfo.DATA_OID, new DerValue(DerValue.TAG_CONTEXT, dataToSign));
		// Create PKCS7 Signed data
		PKCS7 p7 = new PKCS7(new AlgorithmId[] { digestAlgorithmId }, cInfo,
				new java.security.cert.X509Certificate[] { c }, new SignerInfo[] { sInfo });

		// Write PKCS7 to bYteArray

//		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//		DEROutputStream dOut = new DEROutputStream(bOut);
//		dOut.writeObject(s.toASN1Structure().toASN1Primitive()); 
//		dOut.close();
//		byte[] encoded = bOut.toByteArray();

		ByteArrayOutputStream bOut = new DerOutputStream();
		p7.encodeSignedData(bOut);

		byte[] encodedPKCS7 = bOut.toByteArray();
		System.out.println(encodedPKCS7);

		saveFile(encodedPKCS7);

		String text = byteArrayToHex(encodedPKCS7);
		System.out.println(text);
		return text;
	}

	public byte[] calcSha256(byte[] content) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}
	
	private void saveFile(byte[] encodedPKCS7) {
		File file = new File("C:/temp/wspn/outputfile.p7s");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			// Writes bytes from the specified byte array to this file output stream
			fos.write(encodedPKCS7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String lerCertificadoCms(String texto) throws Exception {
		String path = "C:/Users/pascott/Documents/pascott informatica/certificado_digital/pascott_teste.pfx";
		String passFile = "123456";
		Security.addProvider(new BouncyCastleProvider());
		char[] password = passFile.toCharArray();
		String text = texto;
		FileInputStream fis = new FileInputStream(path);
		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(fis, password);

		String alias = ks.aliases().nextElement();
		PrivateKey pKey = (PrivateKey) ks.getKey(alias, password);
		X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
		java.util.List certList = new ArrayList();

		Store certs = new JcaCertStore(certList);

		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		JcaSimpleSignerInfoGeneratorBuilder builder = new JcaSimpleSignerInfoGeneratorBuilder().setProvider("BC")
				.setDirectSignature(true);

		gen.addSignerInfoGenerator(builder.build("SHA1withRSA", pKey, cert));
		gen.addCertificates(certs);

		CMSTypedData msg = new CMSProcessableByteArray(text.getBytes());
		CMSSignedData s = gen.generate(msg, true);
		System.out.println(s.getEncoded());

		saveFile(s.getEncoded());

		String text1 = byteArrayToHex(s.getEncoded());
		System.out.println(text1);
		return text1;
	}

	public static String byteArrayToHex(byte[] byteArray) {
		StringBuilder hex = new StringBuilder(byteArray.length * 2);
		for (byte b : byteArray)
			hex.append(String.format("%02x", b));
		//System.out.println("Print Hex: " + hex.toString().toLowerCase());
		return hex.toString().toLowerCase();
	}

	public static String CreateMD5(String input) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(input.getBytes());
		byte[] digest = md.digest();
		return byteArrayToHex(digest);
	}

}
