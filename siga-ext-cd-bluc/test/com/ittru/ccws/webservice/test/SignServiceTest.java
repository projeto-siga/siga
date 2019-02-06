package com.ittru.ccws.webservice.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ittru.ccws.webservice.SignService;
import com.ittru.ccws.webservice.SignServiceImplService;

public class SignServiceTest {
	private class BASE64Encoder {

		public String encode(byte[] origHash256) {
			return new String(Base64.encodeBase64(origHash256));
		}
		
	}

	private class BASE64Decoder {

		public byte[] decodeBuffer(String resTmp) {
			return Base64.decodeBase64(resTmp);
		}
		
	}

	
	private static final int FALLBACK_LIMIT = 2048;
	private static final String DATA_PATH = "c:/Temp/Ittru/";
	private static final String OUT_PATH =  "c:/Temp/Ittru/";
	private static final int MAXLENGTH = 100000000;

	SignService signServ = null;


	@Before
	public void setUp() throws Exception {

		signServ = SignServiceImplService.getIttruService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void runStep1() {
		String contentPath =  DATA_PATH + "JFRJMEM201311332.pdf";
		String certPath = DATA_PATH + "apt2.crt";
		String outPathStep1 = OUT_PATH + "step1.sa";
		Date now = new Date(1376955307722l);
//		Date now = new Date(1388677516906l);
		

		String  outPathStep2 = OUT_PATH + "step2.p7s";
		
		
		String outPathStep3 = OUT_PATH + "JFRJMEM201311332.pdf.p7s";		

		File f = new File(outPathStep1);
		f.delete();
		
		f = new File(outPathStep2);
		f.delete();
		
		f = new File(outPathStep3);
		f.delete();
		
		Step1WS(contentPath, certPath, outPathStep1, now);
	}


	@Test
	public void runStep3() {
		String contentPath =  DATA_PATH + "JFRJMEM201311332.pdf";
		String certPath = DATA_PATH + "apt2.crt";
		Date now = new Date(1376955307722l);
//		Date now = new Date(1388677516906l);

		String  outPathStep2 = OUT_PATH + "step2.p7s";
		String outPathStep3 = OUT_PATH + "JFRJMEM201311332.pdf.p7s";		
		Step3WS(outPathStep2, certPath, contentPath, outPathStep3, now);
	}
	
	
	
	public void Step1WS(String contentPath, String certPath, String outPath, Date now) {

		try {

			System.out.println("\n** STEP 1");
			System.out.println(contentPath);
			System.out.println(certPath);
			System.out.println(outPath);
			System.out.println(now.getTime());
			
			X509Certificate c = loadCert(certPath);
			
			
			
			BASE64Encoder b64enc = new BASE64Encoder();
			BASE64Decoder b64dec = new BASE64Decoder();
			
			
			RSAPublicKey pubKey = (RSAPublicKey) c.getPublicKey();
			byte[] res = null;
			if (pubKey.getModulus().bitLength() >= FALLBACK_LIMIT) {
				byte[] origHash256 = calcSha256(getBytesFromFile(new File(
						contentPath)));
				String resTmp = signServ.hashSignedAttribSha256(
						b64enc.encode( origHash256),
						encodeDate(now), encodeb64(c));
				res = b64dec.decodeBuffer(resTmp);
				
			} else {
				byte[] origHash1 = calcSha1(getBytesFromFile(new File(
						contentPath)));
				String resTmp = signServ.hashSignedAttribSha1(
						b64enc.encode( origHash1),
						encodeDate(now), encodeb64(c));
				res = b64dec.decodeBuffer(resTmp);
			}

			writeToFile(res, outPath);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void Step3WS(String p7sPath, String certPath, String contentPath, String outPath, Date now) {

		byte[] res = null;

		try {
			
			System.out.println("\n** STEP 3");
			System.out.println(p7sPath);
			System.out.println(certPath);
			System.out.println(contentPath);
			System.out.println(outPath);
			System.out.println(now);
			
			InputStream is = new FileInputStream(p7sPath);
			byte[] signFile = new byte[is.available()];
			is.read(signFile);
			is.close();
			
			BASE64Encoder b64enc = new BASE64Encoder();
			BASE64Decoder b64dec = new BASE64Decoder();
			
			String signStr = signServ.extractSignature(
					b64enc.encode(signFile ));
			byte[] sign = b64dec.decodeBuffer(signStr);
			X509Certificate c = loadCert(certPath);

			RSAPublicKey pubKey = (RSAPublicKey) c.getPublicKey();

			if (pubKey.getModulus().bitLength() == FALLBACK_LIMIT) {
				byte[] origHash256 = calcSha256(getBytesFromFile(new File(
						contentPath)));
				String resStr = signServ.composeBodySha256(b64enc.encode( sign),
						encodeb64(c), 
						b64enc.encode( origHash256),
						encodeDate(now));
				
				res = b64dec.decodeBuffer(resStr);
			} else {
				byte[] origHashSha1 = calcSha1(getBytesFromFile(new File(
						contentPath)));
				String resStr = signServ.composeBodySha1(b64enc.encode( sign),
						encodeb64(c), 
						b64enc.encode( origHashSha1),
						encodeDate(now));
				
				res = b64dec.decodeBuffer(resStr);
			}

			writeToFile(res, outPath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	*****************************
//	 --- UTILITARIOS
//	*****************************

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = null;
		byte[] ret = null;
		try {
			long length = file.length();
			if (length > MAXLENGTH)
				throw new IllegalArgumentException("File is too big");
			ret = new byte[(int) length];
			is = new FileInputStream(file);
			is.read(ret);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException ex) {
				}
		}
		return ret;
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

	private static void writeToFile(byte[] ret, String envPath)
			throws FileNotFoundException, IOException {
		OutputStream os = new FileOutputStream(new File(envPath));
		os.write(ret);
		os.flush();
		os.close();
	}
	private X509Certificate loadCert(String certPath)
			throws FileNotFoundException, CertificateException, IOException {
		InputStream is = new FileInputStream(certPath);
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		X509Certificate c = (X509Certificate) cf.generateCertificate(is);
		is.close();
		return c;
	}
	private XMLGregorianCalendar encodeDate(Date now) throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(now);
		XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return date2;
	}

	private String encodeb64(X509Certificate c) throws CertificateEncodingException {
		byte[] b = c.getEncoded();
		BASE64Encoder b64enc = new BASE64Encoder();
		String certB64 = b64enc.encode(b);
		
		return certB64;
	}
	

}
