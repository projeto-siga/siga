package br.gov.jfrj.siga.arquivo;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class InputStreamHash extends InputStream {
	private InputStream is;
	private MessageDigest calcSha256 = null;
	private byte[] sha256 = null;

	public InputStreamHash(InputStream iStream) {
		this.is = iStream;
		try {
			calcSha256 = MessageDigest.getInstance("SHA-256");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int count = super.read(b, off, len);
		
		if (count >= 0) 
			calcSha256.update(b, off, count);
		
		if (count == -1) {
			sha256 = calcSha256.digest();
		}
		
		return count;
	}

	public int read() throws IOException {
		return this.is.read();
	}

	public void mark(int pos) {
		super.mark(pos);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public void close() throws IOException {
		if (sha256 == null) {
			sha256 = calcSha256.digest();
		}
		super.close();
	}
	
	@Override
	public void reset() throws IOException {
		if (sha256 == null) {
			calcSha256.reset();
		}
		super.reset();
	}
	
	public byte[] getSha256() {
		return sha256;
	}

}