package br.gov.jfrj.siga.vraptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xerces.impl.dv.util.Base64;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.ex.ext.AbstractConversorHTMLFactory;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExUtilController extends ExController {

	private static final Logger LOGGER = Logger.getLogger(ExUtilController.class);

	/**
	 * @deprecated CDI eyes only
	 */
	public ExUtilController() {
		super();
	}

	@Inject
	public ExUtilController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em, Validator validator) {

		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("/public/app/util/test")
	public void assinadorExternoTest() throws Exception {
		class Test {
			String category;
			String service;
			String url;
			String responsable;
			boolean available;
		}

		try {
			Test resp = new Test();
			resp.category = "webservice";
			resp.service = "sigadocutil";
			resp.url = request.getRequestURI();
			resp.responsable = null;
			resp.available = true;
			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}

	}

	protected static class Html2PdfResp {
		byte[] pdf;
		int pages;
		int size;
	}

	@Post("/public/app/util/html-pdf")
	public void html2pdf(String html, int conv) throws Exception {
		try {
			if (html == null) {
				JSONObject req = getJsonReq(request);
				html = req.getString("html");
				conv = Integer.parseInt(req.getString("conv"));
			}

			assertPassword();
			Html2PdfResp resp = new Html2PdfResp();

			if (html == null || html.trim().length() == 0)
				throw new Exception("Parâmetro 'html' precisa ser informado.");

			final byte pdf[];
			AbstractConversorHTMLFactory fabricaConvHtml = AbstractConversorHTMLFactory.getInstance();
			ConversorHtml conversor = fabricaConvHtml.getConversor(conv);

			try {
				pdf = Documento.generatePdf(html, conversor);
				resp.pdf = pdf;
				resp.pages = Documento.getNumberOfPages(pdf);
				resp.size = pdf.length;
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro na geração do PDF. Por favor, verifique se existem recursos de formatação não suportados.",
						0, e);
			}
			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	@Post("/public/app/util/pdf-info")
	public void pdf2info(String pdf) throws Exception {
		class Resp {
			int pages;
			int size;
		}

		try {
			assertPassword();
			Resp resp = new Resp();

			if (pdf == null)
				throw new Exception("Parâmetro 'pdf' precisa ser informado.");

			final byte abPdf[] = Base64.decode(pdf);
			try {
				resp.pages = Documento.getNumberOfPages(abPdf);
				resp.size = abPdf.length;
			} catch (Exception e) {
				throw new AplicacaoException("Erro na contagem de páginas de PDF.", 0, e);
			}
			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private void assertPassword() throws Exception {
		String pwd = Prop.get("util.webservice.password");
		if (pwd != null && !pwd.equals(this.request.getHeader("Authorization")))
			throw new Exception("Falha de autenticação.");
	}

	public static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);

	public static final Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
			.registerTypeHierarchyAdapter(Date.class, new DateToStringTypeAdapter()).setPrettyPrinting().create();

	private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return Base64.decode(json.getAsString());
		}

		public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(Base64.encode(src));
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

	public static String format(Date date) {
		return isoFormatter.format(date);
	}

	public static Date parse(String date) {
		try {
			return isoFormatter.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	protected void jsonSuccess(final Object resp) {
		String s = gson.toJson(resp);
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(200);
	}

	protected void jsonError(final Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String errstack = sw.toString(); // stack trace as a string

		JSONObject json = new JSONObject();
		try {
			json.put("errormsg", e.getMessage());

			// Error Details
			JSONArray arr = new JSONArray();
			JSONObject detail = new JSONObject();
			detail.put("context", context);
			detail.put("service", "sigadocsigner");
			detail.put("stacktrace", errstack);
			json.put("errordetails", arr);
		} catch (JSONException e1) {
			throw new RuntimeException(e1);
		}


		String s = json.toString();
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(500);
	}

	private static JSONObject getJsonReq(HttpServletRequest request) {
		try {
			JSONObject req = new JSONObject();

			try {
				String sJson = getBody(request);
				req = new JSONObject(sJson);
			} catch (Exception ex) {

			}
			for (Object key : request.getParameterMap().keySet())
				if (key instanceof String && request.getParameter((String) key) instanceof String
						&& !req.has((String) key))
					req.put((String) key, request.getParameter((String) key));

			return req;
		} catch (Exception ex) {
			throw new RuntimeException("Cannot parse request body as JSON", ex);
		}
	}

	private static String getBody(HttpServletRequest request) throws IOException {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}
}
