package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.io.InputStream;

import com.crivano.swaggerservlet.IUploadHandler;
import com.google.common.io.ByteStreams;

public class ArquivoUploadHandler implements IUploadHandler {

	@Override
	public Object upload(String filename, String contenttype, InputStream stream) {
		// Grava o conteúdo do arquivo
		byte[] bytes;
		try {
			bytes = ByteStreams.toByteArray(stream);
			return bytes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] toByteArray(InputStream in) throws IOException {
		byte[] bytes = ByteStreams.toByteArray(in);
		return bytes;
	}
}