package br.gov.jfrj.siga.bluc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BlucService {
	// ResteasyClient client = new ResteasyClientBuilder().build();
	private String endpoint;

	public BlucService(String endpoint) {
		this.endpoint = endpoint;
	}

	private static String toJson(Object req) {
		final Gson gson = new Gson();
		String jsonReq = gson.toJson(req);
		return jsonReq;
	}

	public static <T> T fromJson(final InputStream inputStream,
			final Class<T> clazz) {
		try {
			if (inputStream != null) {
				final Gson gson = new Gson();
				final BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				return gson.fromJson(reader, clazz);
			}
		} catch (final Exception e) {
		}
		return null;
	}

	public static <T> T json2json(Object req, Class<? extends T> respClass,
			String url) throws Exception {

//		String s = Unirest.post(url).header("accept", "application/json").body(req)
//				.asString().getBody();
		
		return Unirest.post(url).header("accept", "application/json").header("Content-Type", "application/json").body(req)
				.asObject(respClass).getBody();

		// HttpClient client = HttpClientBuilder.create().build();
		//
		// String jsonReq = toJson(req);
		//
		// HttpPost post = new HttpPost(url);
		// StringEntity se = new StringEntity(jsonReq);
		// se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
		// "application/json"));
		// post.setEntity(se);
		// HttpResponse response = client.execute(post);
		//
		// if (response == null)
		// return null;
		//
		// System.out.println("Response Code : "
		// + response.getStatusLine().getStatusCode());
		// InputStream in = response.getEntity().getContent(); // Get the data
		// // in the entity
		// T resp = fromJson(in, respClass);
		// return resp;
	}

	public static String void2string(String url) throws Exception {
		return Unirest.get(url).asString().getBody();
		
//		HttpClient client = HttpClientBuilder.create().build();
//
//		HttpGet get = new HttpGet(url);
//		HttpResponse response = client.execute(get);
//
//		if (response == null)
//			return null;
//
//		System.out.println("Response Code : "
//				+ response.getStatusLine().getStatusCode());
//		InputStream in = response.getEntity().getContent(); // Get the data
//
//		BufferedReader rd = new BufferedReader(new InputStreamReader(in));
//
//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
//
//		return result.toString();
	}

	public HashResponse hash(HashRequest req) throws Exception {
		return json2json(req, HashResponse.class, this.endpoint + "/hash");
	}

	public EnvelopeResponse envelope(EnvelopeRequest req) throws Exception {
		return json2json(req, EnvelopeResponse.class, this.endpoint
				+ "/envelope");
	}

	public ValidateResponse validate(ValidateRequest req) throws Exception {
		return json2json(req, ValidateResponse.class, this.endpoint
				+ "/validate");
	}

	public boolean test() throws Exception {
		String r = void2string(this.endpoint + "/test");
		return r.contains("OK");
	}

	public String date2string(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return javax.xml.bind.DatatypeConverter.printDateTime(cal);
	}

	public String bytearray2b64(byte[] ab) {
		return Base64.encodeBase64String(ab);
	}

	public static byte[] calcSha1(byte[] content)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	public static byte[] calcSha256(byte[] content)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA256");
		md.reset();
		md.update(content);
		byte[] output = md.digest();
		return output;
	}

	// public HashResponse hash(HashRequest req) {
	// ResteasyWebTarget target = client.target(endpoint);
	//
	// Response response = target.request().post(
	// Entity.entity(req, "application/json"));
	//
	// if (response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }
	//
	// HashResponse resp = response.readEntity(HashResponse.class);
	//
	// response.close();
	//
	// return resp;
	// }
	//
	//
	// public EnvelopeResponse validate(EnvelopeRequest req) {
	// ResteasyWebTarget target = client.target(endpoint);
	//
	// Response response = target.request().post(
	// Entity.entity(req, "application/json"));
	//
	// if (response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }
	//
	// EnvelopeResponse resp = response.readEntity(EnvelopeResponse.class);
	//
	// response.close();
	//
	// return resp;
	// }
	//
	// public ValidateResponse validate(ValidateRequest req) {
	// ResteasyWebTarget target = client.target(endpoint);
	//
	// Response response = target.request().post(
	// Entity.entity(req, "application/json"));
	//
	// if (response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }
	//
	// ValidateResponse resp = response.readEntity(ValidateResponse.class);
	//
	// response.close();
	//
	// return resp;
	// }

	// public boolean test() {
	// ResteasyWebTarget target = client.target(endpoint);
	//
	// Response response = target.request().get();
	//
	// if (response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }
	//
	// String resp = response.readEntity(String.class);
	//
	// response.close();

	// return resp.contains("OK");
	// }

}
