package br.gov.jfrj.siga.arquivo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

public class BufferedInputStreamHash extends BufferedInputStream {
//	private BufferedInputStream bis = null;
	private MessageDigest calcSha256 = null;
	private byte[] sha256 = null;

	public BufferedInputStreamHash(InputStream in, int size) {
		super(in, size);
		try {
			calcSha256 = MessageDigest.getInstance("SHA-256");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
//	public int read(byte[] b, int off, int len) throws IOException {
	public int read() throws IOException {
		int count = super.read(super.buf);
		System.out.println("count: " + Integer.valueOf(count).toString() 
//				+ " off: " + Integer.valueOf(off).toString()
//				+ " len: " + Integer.valueOf(len).toString()
		);

		if (count > 0) 
			calcSha256.update(super.buf);
		
		if (count == -1) 
			sha256 = calcSha256.digest();
		
		return count;
	}

	@Override
	public void close() throws IOException {
		if (sha256 == null) {
			sha256 = calcSha256.digest();
		}
		super.close();
	}
	
	public byte[] getSha256() {
		return sha256;
	}

}