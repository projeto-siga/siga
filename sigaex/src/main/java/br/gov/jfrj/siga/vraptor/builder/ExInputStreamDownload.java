package br.gov.jfrj.siga.vraptor.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;

public abstract class ExInputStreamDownload implements Download {

	public static final String MEDIA_TYPE_RTF = "application/rtf";
	public static final String MEDIA_TYPE_ZIP = "multipart/x-zip";
	public static final String MEDIA_TYPE_OCTET_STREAM = "application/octet-stream";
	
	private String contentType;
	private byte[] bytes;

	public ExInputStreamDownload(String contentType, byte[] bytes) {
		this.contentType = contentType;
		this.bytes = bytes;
	}

	@Override
	public void write(HttpServletResponse response) throws IOException {
		InputStreamDownload download = new InputStreamDownload(new ByteArrayInputStream(bytes), getContentType(), getFileName());
		download.write(response);
	}

	protected abstract String getFileName();

	public String getContentType() {
		return contentType;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
}