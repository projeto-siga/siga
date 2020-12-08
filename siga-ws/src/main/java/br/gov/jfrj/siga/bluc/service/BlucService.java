package br.gov.jfrj.siga.bluc.service;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

public class BlucService {
	static boolean bInitialized = false;
	private String endpoint;

	public BlucService(String endpoint) {
		this.endpoint = endpoint;
		if (!bInitialized) {
			bInitialized = true;
			Unirest.setObjectMapper(new ObjectMapper() {
				private Gson gson = new GsonBuilder()
						.registerTypeHierarchyAdapter(Date.class, new DateToStringTypeAdapter()).setPrettyPrinting()
						.create();

				@Override
				public <T> T readValue(String value, Class<T> valueType) {
					return gson.fromJson(value, valueType);
				}

				@Override
				public String writeValue(Object value) {
					return gson.toJson(value);
				}
			});
		}
	}

	public static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);

	public static String format(Date date) {
		synchronized (isoFormatter) {
			return isoFormatter.format(date);
		}
	}

	public static Date parse(String date) {
		if (date == null)
			return null;
		try {
			synchronized (isoFormatter) {
				return isoFormatter.parse(date);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static class DateToStringTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return parse(json.getAsString());
		}

		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(format(src));
		}
	}

	public static <T> T json2json(Object req, Class<? extends T> respClass, String url) throws Exception {

		// String s = Unirest.post(url).header("accept",
		// "application/json").body(req)
		// .asString().getBody();

		return Unirest.post(url).header("accept", "application/json").header("Content-Type", "application/json")
				.body(req).asObject(respClass).getBody();

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

		// HttpClient client = HttpClientBuilder.create().build();
		//
		// HttpGet get = new HttpGet(url);
		// HttpResponse response = client.execute(get);
		//
		// if (response == null)
		// return null;
		//
		// System.out.println("Response Code : "
		// + response.getStatusLine().getStatusCode());
		// InputStream in = response.getEntity().getContent(); // Get the data
		//
		// BufferedReader rd = new BufferedReader(new InputStreamReader(in));
		//
		// StringBuffer result = new StringBuffer();
		// String line = "";
		// while ((line = rd.readLine()) != null) {
		// result.append(line);
		// }
		//
		// return result.toString();
	}

	public HashResponse hash(HashRequest req) throws Exception {
		return json2json(req, HashResponse.class, this.endpoint + "/hash");
	}

	public EnvelopeResponse envelope(EnvelopeRequest req) throws Exception {
		return json2json(req, EnvelopeResponse.class, this.endpoint + "/envelope");
	}

	public ValidateResponse validate(ValidateRequest req) throws Exception {
		return json2json(req, ValidateResponse.class, this.endpoint + "/validate");
	}

	public static String bytearray2b64(byte[] ab) {
		return Base64.encodeBase64String(ab);
	}

	public static byte[] b642bytearray(String s) {
		return Base64.decodeBase64(s);
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
}
