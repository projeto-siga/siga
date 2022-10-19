package br.gov.jfrj.siga.integracao.ws.pubnet.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CertificadoDigitalUtils {
	
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
