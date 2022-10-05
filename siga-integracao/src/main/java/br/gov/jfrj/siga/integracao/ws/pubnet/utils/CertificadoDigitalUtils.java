package br.gov.jfrj.siga.integracao.ws.pubnet.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

public class CertificadoDigitalUtils {
	
	public static void decodePrivateKey(byte[] privKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
		System.out.println("Private Key Spec " + privSpec);
		
		PrivateKey privKey = keyFactory.generatePrivate(privSpec);
		System.out.println("Private Key CONTENT:::::: " + privKey);
		
	}
	
	public static void decodeCertificate(byte[] certificate) throws IOException, CertificateEncodingException {
		List<X509CertificateHolder> certList = new ArrayList<X509CertificateHolder>();
		X509CertificateHolder certificateHolder = new X509CertificateHolder(certificate);
		certList.add(certificateHolder);
		Store certs = new JcaCertStore(certList);
		
	}

	public String assinarConteudoComCertificado(X509Certificate cert, String pass, String text) throws Exception {
		char[] passwordChar = pass.toCharArray();
		Security.addProvider(new BouncyCastleProvider());
		
		//System.out.println("certificate is: " + cert);
		
		
		Certificate[] certChain = new Certificate[1];
        certChain[0] = cert;
		
//		KeyStore ks = KeyStore.getInstance("pkcs12");
//		ks.setKeyEntry(alias, key, chain);
//		ks.load(null, passwordChar);
		//ks.setKeyEntry("fernando_henrique_pascott_32417729857.p12", pass.getBytes(), new Certificate[] {cert});
		//ks.setCertificateEntry("fernando_henrique_pascott_32417729857.p12", cert);
		//ks.setKeyEntry("fernando_henrique_pascott_32417729857.p12", new KeyStore.PasswordProtection(passwordChar), certChain);
//		String alias = ks.aliases().nextElement();
        
        byte[] privateKeyBytes = pass.getBytes();
//        String encodedPrivateKey = Base64.getUrlEncoder().encodeToString(privateKeyBytes);
//
//        KeyFactory factory = KeyFactory.getInstance("RSA");
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pass.getBytes());
//        RSAPrivateKey privateKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
		

		PrivateKey pKey = null;
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
	
	public static String include(byte[] signByte, byte [] textByte) throws CMSException, IOException {
		
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		
		CMSTypedData sign = new CMSProcessableByteArray(CMSObjectIdentifiers.signedData, signByte);		
		
		CMSTypedData text = new CMSProcessableByteArray(CMSObjectIdentifiers.envelopedData, textByte);	
		CMSSignedData ss = gen.generate(sign, true);
		
		CMSSignedData s = gen.generate(text, true);
		//s.getSignerInfos().getSigners().add(signByte);
		
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DEROutputStream dOut = new DEROutputStream(bOut);
		//dOut.writeObject(s.toASN1Structure().toASN1Primitive());
		dOut.writeObject(ss.toASN1Structure().toASN1Primitive());
		dOut.close();
		byte[] encoded = bOut.toByteArray();
		
		saveFile(signByte, "C:/temp/wspn/outputFilePos.p7s");
		String textCms = byteArrayToHex(signByte);
		System.out.println("Print CMS: " + textCms);
		return textCms;
		
	}
	
	public static void saveFile(byte[] encodedPKCS7) {
		saveFile(encodedPKCS7, "C:/temp/wspn/outputfile.p7s");
	}

	public static void saveFile(byte[] encodedPKCS7, String nomeFile) {
		File file = new File(nomeFile);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(encodedPKCS7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveHexFile(String str) {
		byte[] bte = DatatypeConverter.parseHexBinary(str);
		saveFile(bte, "C:/temp/wspn/outputfileByte.p7s");
		
	}

	public static String byteArrayToHex(byte[] byteArray) {
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
	
	public static byte[] calcSha1(byte[] content) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}
	
	public static byte[] calcSha256(byte[] content) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}
	
	public static String bytearray2b64(byte[] ab) {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(ab);
	}

}
