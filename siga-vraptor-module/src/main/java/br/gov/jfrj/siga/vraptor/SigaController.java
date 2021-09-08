package br.gov.jfrj.siga.vraptor;

import static br.com.caelum.vraptor.view.Results.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.encoding.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.log.RequestExceptionLogger;
import br.gov.jfrj.siga.base.util.Paginador;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SigaController {
	protected SigaObjects so;

	protected Result result;

	protected HttpServletRequest request;

	private EntityManager em;
	protected CpDao dao;

	private Paginador p = new Paginador();
		
	private Integer postback;
	
	
	private String mensagemAguarde = null;
	
	private HttpServletResponse response;
	private ServletContext context;
	
	//Todo: verificar se após a migração do vraptor se ainda necessita deste atributo "par"
	private Map<String, String[]> par;
	
	private Validator validator;
	
	protected Map<String, String[]> getPar() {
		return par;
	}
	
	protected void setParam(final String parameterName, final String parameterValue) {
		final String as[] = { parameterValue };
		getPar().put(parameterName, as);
		return;
	}	
	
	protected String getUrlEncodedParameters()
			throws UnsupportedEncodingException, IOException {
		if (getPar() != null) {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				for (final String key : getPar().keySet()) {
					final String as[] = getPar().get(key);
					for (final String val : as) {
						if (baos.size() > 0)
							baos.write('&');
						baos.write(URLEncoder.encode(key, "utf-8").getBytes());
						baos.write('=');
						baos.write(URLEncoder.encode(val, "utf-8").getBytes());
					}
				}
				return new String(baos.toByteArray());
			}
		}
		return null;
	}
	
	protected CpDao dao() {
		return CpDao.getInstance();
	}

	protected Integer getPostback() {
		return postback;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	public SigaController() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public SigaController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super();
		this.setRequest(request);
		this.dao = dao;
		this.setPar(new HashMap<>(getRequest().getParameterMap()));
		this.result = result;
		this.so = so;
		this.em = em;

		try {
			result.on(AplicacaoException.class).forwardTo(this).appexception();
			result.on(Exception.class).forwardTo(this).exception();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		
		result.include("cadastrante", getCadastrante());
		result.include("lotaCadastrante", getLotaCadastrante());
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());
		result.include("meusTitulares", getMeusTitulares());
		result.include("meusDelegados", getMeusDelegados());
		result.include("identidadeCadastrante", getIdentidadeCadastrante());
	}

	@Inject
	private void setValidator(Validator validator) throws Throwable {
		this.validator = validator;
	}
	
	protected Validator getValidator() {
		return validator;
	}
	
	protected List<DpSubstituicao> getMeusTitulares() {
		try {
			List<DpSubstituicao> substituicoes = so.getMeusTitulares();
			if (substituicoes == null) {
				return Lists.newArrayList();
			}
			resolveLazy(substituicoes);
			return substituicoes;
		} catch (Exception e) {
			throw new AplicacaoException("Erro", 500, e);
		}
	}
	
	protected List<DpVisualizacao> getMeusDelegados() {
		try {
			List<DpVisualizacao> visualizacoes = so.getMeusDelegados();
			if (visualizacoes == null) {
				return Lists.newArrayList();
			}
//			resolveLazy(visualizacoes);
			return visualizacoes;
		} catch (Exception e) {
			throw new AplicacaoException("Erro", 500, e);
		}
	}
	
	protected byte[] toByteArray(final UploadedFile upload) throws IOException {
		try (InputStream is = upload.getFile()) {
			// Get the size of the file
			final long tamanho = upload.getSize();
	
			// Não podemos criar um array usando o tipo long.
			// é necessário usar o tipo int.
			if (tamanho > Integer.MAX_VALUE)
				throw new IOException("Arquivo muito grande");
	
			// Create the byte array to hold the data
			final byte[] meuByteArray = new byte[(int) tamanho];
	
			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < meuByteArray.length && (numRead = is.read(meuByteArray, offset, meuByteArray.length - offset)) >= 0) {
				offset += numRead;
			}
	
			// Ensure all the bytes have been read in
			if (offset < meuByteArray.length)
				throw new IOException("Não foi possível ler o arquivo completamente " + upload.getFileName());
	
			return meuByteArray;
		}
	}



	private void resolveLazy(List<DpSubstituicao> substituicoes) {
		for (DpSubstituicao dpSubstituicao : substituicoes) {
			if (dpSubstituicao.getTitular() != null) {
				dpSubstituicao.getTitular().getId();
			}
			
			if (dpSubstituicao.getLotaTitular() != null) {
				dpSubstituicao.getLotaTitular().getId();
			}
		}
	}
	
	public void appexception() throws Throwable {
 		configurarHttpResult(400);
 		throw (Throwable) result.included().get("exception");
 	}
	
	public void exception() throws Throwable {
 		configurarHttpResult(500);
		throw (Throwable) result.included().get("exception");
		// new RequestExceptionLogger(request, (Exception) result.included().get("exception"), 0L, this.getClass().getName()).logar();
 	}	

	private void configurarHttpResult(int statusCode) throws Exception {
		HttpResult res = this.result.use(http());
		res.setStatusCode(statusCode);
		definirPaginaDeErro();
	}
    
	private void definirPaginaDeErro() throws Exception {
		if (!response.isCommitted()) {
			if (requisicaoEhAjax()) {
				Exception t = (Exception) request.getAttribute("exception");
				if (t == null && validator.hasErrors())
					t = new Exception(validator.getErrors().get(0).toString());
				jsonError(t);
			} else
				result.forwardTo("/WEB-INF/page/erroGeral.jsp");
		}
	}
	
	private boolean requisicaoEhAjax() {
		return request.getHeader("X-Requested-With") != null || (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"));
	}
    
	protected DpLotacao getLotaTitular() {
		return so.getLotaTitular();
	}
	
	protected void setLotaTitular(DpLotacao lotaTitular) {
		so.setLotaTitular(lotaTitular);
	}	

	protected DpPessoa getTitular() {
		return so.getTitular();
	}
	
	protected void setTitular(DpPessoa titular) {
		so.setTitular(titular);
	}
	
	protected DpLotacao getLotaCadastrante(){
		if(null != so.getCadastrante()) {
			return so.getCadastrante().getLotacao();
		} else {
			return null;
		}
	}

	protected DpPessoa getCadastrante() {
		return so.getCadastrante();
	}
	
	protected void setCadastrante(DpPessoa cadastrante) {
		so.setCadastrante(cadastrante);		
	}

	protected CpIdentidade getIdentidadeCadastrante() {
		return so.getIdentidadeCadastrante();
	}

	protected String param(final String parameterName) {
		final String[] as = getRequest().getParameterValues(parameterName);
		if (as == null || as[0].equals("null"))
			return null;
		return as[0];
	}

	protected Integer paramInteger(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Integer.parseInt(s);
	}

	protected Long paramLong(final String parameterName) {
		final String s = param(parameterName);
		try {
			return Long.parseLong(s);
		} catch (Throwable t) {
			return null;
		}
	}

	protected Short paramShort(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Short.parseShort(s);
	}

	protected Date paramDate(final String parameterName) {
		Date dt = null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			dt = df.parse(param(parameterName));
		} catch (final ParseException e) {
			return null;
		} catch (final NullPointerException e) {
			return null;
		}
		return dt;
	}

	protected DpPessoa daoPes(long id) {
		return so.daoPes(id);
	}

	protected DpPessoa daoPes(String sigla) {
		return so.daoPes(sigla);
	}

	protected DpLotacao daoLot(long id) {
		return so.daoLot(id);
	}

	protected DpLotacao daoLot(String sigla) {
		return so.daoLot(sigla);
	}

	protected CpOrgaoUsuario daoOU(String sigla) {
		return so.daoOU(sigla);
	}

	protected EntityManager em() {
		return this.em;
	}

	protected int redirectToHome() {
		result.redirectTo("/../siga/app/principal");
		return 0;
	}

	protected void resultOK() {
		result.use(Results.http()).body("OK").setStatusCode(200);
	}

	protected void setMensagem(String mensagem) {
		result.include("mensagem", mensagem);
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	protected void assertAcesso(final String pathServico) {
		so.assertAcesso(pathServico);
	}	
	
	protected void setP(Paginador p) {
		this.p = p;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setPar(final Map par) {
		this.par = par;
	}

	protected void setPostback(final Integer postback) {
		this.postback = postback;
	}
	
	protected String getMensagemAguarde() {
		return mensagemAguarde;
	}
	
	protected void setMensagemAguarde(String mensagemAguarde) {
		this.mensagemAguarde = mensagemAguarde;
	}

	protected List<CpOrgaoUsuario> getOrgaosUsu() throws AplicacaoException {
		return dao().listarOrgaosUsuarios();
	}
	
	protected Paginador getP() {
		return p;
	}
	
	protected boolean podeUtilizarServico(String servico)
			throws Exception {
		return Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), servico);
	}
	
	// Recursos para possibilitar o retorno de JSON
	//
	public static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);

	public static final Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return false;
		}

	}).registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
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
	
	protected void jsonError(final Exception e) throws Exception {
		String errstack = RequestExceptionLogger.simplificarStackTrace(e);

		JSONObject json = new JSONObject();
		try {
			json.put("errormsg", e.getMessage());

			// Error Details
			JSONArray arr = new JSONArray();
			JSONObject detail = new JSONObject();
			detail.put("context", context.getContextPath());
			detail.put("service", "sigadocsigner");
			detail.put("stacktrace", errstack);
			arr.put(detail);
			json.put("errordetails", arr);
		} catch (JSONException e1) {
			throw new RuntimeException(e1);
		}

		String s = json.toString(4);
		response.setStatus(500);
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(s);
		request.setAttribute("jsonError", s);
		response.flushBuffer();
		throw e;
	}

	@Inject
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	@Inject
	public void setContext(ServletContext context) {
		this.context = context;
	}

}
