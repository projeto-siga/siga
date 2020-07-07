package br.gov.jfrj.itextpdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.crivano.swaggerservlet.SwaggerUtils;

public class Status implements Serializable {
	public String mensagem;
	public int indice;
	public int contador;
	public Long bytes;
	public Exception ex;

	public static Status update(String key, String message, int index, int count, Long bytes) throws IOException {
		Status as = new Status();
		as.mensagem = message;
		as.indice = index;
		as.contador = count;
		as.bytes = bytes;
		// SwaggerUtils.log(Status.class).info(message + " (" + index + "/" + count +
		// ")");
		return update(key, as);
	}

	public static Status update(String key, Status as) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(as);
		oos.flush();
		byte[] data = bos.toByteArray();

		SwaggerUtils.memCacheStore(key, data);
		return as;
	}

	public static Status get(String key) throws IOException, ClassNotFoundException {
		byte[] data = SwaggerUtils.memCacheRetrieve(key);
		if (data == null)
			return null;
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Status as = (Status) ois.readObject();
		return as;
	}

}
