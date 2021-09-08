package br.gov.jfrj.siga.vraptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoHash;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoList;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoListItem;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoSave;
import br.gov.jfrj.siga.ex.bl.ExAssinavelDoc;
import br.gov.jfrj.siga.ex.bl.ExAssinavelMov;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExAssinadorExternoController extends ExController {

	private static final Logger LOGGER = Logger.getLogger(ExAssinadorExternoController.class);


	/**
	 * @deprecated CDI eyes only
	 */
	public ExAssinadorExternoController() {
		super();
	}

	@Inject	public ExAssinadorExternoController(HttpServletRequest request, HttpServletResponse response,
			ServletContext context, Result result, SigaObjects so, EntityManager em, Validator validator) {

		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	private class ExAssinadorExternoTest {
		String category;
		String service;
		String url;
		String responsable;
		boolean available;
	}

	@Get("/public/app/assinador-externo/test")
	public void assinadorExternoTest() throws Exception {
		try {
			ExAssinadorExternoTest resp = new ExAssinadorExternoTest();
			resp.category = "webapp";
			resp.service = "sigadocsigner";
			resp.url = request.getRequestURI();
			resp.responsable = null;
			resp.available = true;
			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}

	}

	@Get("/public/app/assinador-externo/doc/list")
	public void assinadorExternoList() throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			assertPassword();

			String urlapi = req.getString("urlapi");
			String sCpf = req.getString("cpf");

			String permalink = urlapi.split("sigaex/public/app/")[0] + "siga/permalink/";

			Long cpf = Long.valueOf(sCpf);
			List<DpPessoa> pessoas = dao().listarPorCpf(cpf);
			if (pessoas == null)
				throw new Exception("Nenhuma pessoa localizada com o CPF: " + sCpf);
			List<ExAssinadorExternoListItem> list = new ArrayList<ExAssinadorExternoListItem>();
			for (DpPessoa pes : pessoas) {
				boolean apenasComSolicitacaoDeAssinatura = !Ex.getInstance().getConf().podePorConfiguracao(pes, ExTipoDeConfiguracao.PODE_ASSINAR_SEM_SOLICITACAO);
				List<ExAssinavelDoc> assinaveis = Ex.getInstance().getBL().obterAssinaveis(pes, pes.getLotacao(), apenasComSolicitacaoDeAssinatura);
				for (ExAssinavelDoc ass : assinaveis) {
					if (ass.isPodeAssinar()) {
						String solicitantesDeAssinatura = ass.getDoc().getSolicitantesDeAssinaturaCompleto();
						if (solicitantesDeAssinatura != null)
							solicitantesDeAssinatura = solicitantesDeAssinatura.replace("Revisado por", "Assinatura solicitada por");
						ExAssinadorExternoListItem aei = new ExAssinadorExternoListItem();
						aei.setId(makeId(ass.getDoc().getCodigoCompacto()));
						aei.setSecret(docSecret(ass.getDoc()));
						aei.setCode(ass.getDoc().getCodigo());
						aei.setDescr(ass.getDoc().getDescrDocumento() + (solicitantesDeAssinatura.length() != 0 ? " (" + solicitantesDeAssinatura + ")" : ""));
						aei.setKind(ass.getDoc().getTipoDescr());
						aei.setOrigin("Siga-Doc");
						aei.setUrlView(permalink + ass.getDoc().getReferencia());
						aei.setUrlHash("sigadoc/doc/" + aei.getId() + "/hash");
						aei.setUrlSave("sigadoc/doc/" + aei.getId() + "/sign");
						list.add(aei);
					}
					if (ass.getMovs() == null)
						continue;
					for (ExAssinavelMov assmov : ass.getMovs()) {
						ExAssinadorExternoListItem aei = new ExAssinadorExternoListItem();
						aei.setId(makeId(assmov.getMov().getReferencia()));
						aei.setSecret(movSecret(assmov.getMov()));
						aei.setCode(assmov.getMov().getReferencia());
						aei.setDescr(assmov.getMov().getObs());
						aei.setKind(assmov.getMov().getTipoDescr());
						aei.setOrigin("Siga-Doc");
						aei.setUrlView(permalink + assmov.getMov().getReferencia().replace(":", "/"));
						aei.setUrlHash("sigadoc/doc/" + aei.getId() + "/hash");
						aei.setUrlSave("sigadoc/doc/" + aei.getId() + "/sign");
						list.add(aei);
					}
				}

			}

			ExAssinadorExternoList resp = new ExAssinadorExternoList();
			resp.setList(list);

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}

	}

	@Get("/public/app/assinador-externo/doc/{id}/pdf")
	public void assinadorExternoPdf(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);
			assertPassword();
			PdfData pdfd = getPdf(id);
			result.use(Results.http()).addHeader("Content-Type", "application/pdf")
			.addHeader("Content-Length", Integer.toString(pdfd.pdf.length))
			.addHeader("Doc-Secret", pdfd.secret)
			.body(new ByteArrayInputStream(pdfd.pdf)).setStatusCode(200);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private class PdfData {
		byte[] pdf;
		String secret;
	}

	private PdfData getPdf(String id)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception, SQLException {
		PdfData pdfd = new PdfData();

		String sigla = null;
		if (id != null) {
			sigla = id2sigla(id);
			sigla += ".pdf";
		}

		ExMobil mob = Documento.getMobil(sigla);
		ExMovimentacao mov = Documento.getMov(mob, sigla);

		if (mov != null) {
			pdfd.pdf = mov.getConteudoBlobpdf();
			pdfd.secret = movSecret(mov);
		} else if (mob != null) {
			pdfd.pdf = mob.doc().getConteudoBlobPdf();
			pdfd.secret = docSecret(mob.doc());
		}

		if (pdfd.pdf == null)
			throw new Exception("Não foi possível localizar o PDF.");
		return pdfd;
	}

	@Transacional
	@Post("/app/assinador-popup/doc/{id}/hash")
	public void assinadorPopupHash(String id) throws Exception {
		try {
			String sigla = id2sigla(id) + ".pdf";
			ExMobil mob = Documento.getMobil(sigla);
			ExMovimentacao mov = Documento.getMov(mob, sigla);
			ExDocumento doc = mob.getDoc();
			
			if (mov == null && !doc.isFinalizado()) {
				DpPessoa cadastrante = obterCadastrante(null, mob, mov);
				Ex.getInstance().getBL().finalizar(cadastrante, cadastrante.getLotacao(), doc);
			}
			
			PdfData pdfd = getPdf(id);

			ExAssinadorExternoHash resp = new ExAssinadorExternoHash();

			resp.setSha1(BlucService.bytearray2b64(BlucService.calcSha1(pdfd.pdf)));
			resp.setSha256(BlucService.bytearray2b64(BlucService.calcSha256(pdfd.pdf)));

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	@Get("/public/app/assinador-externo/doc/{id}/hash")
	public void assinadorExternoHash(String id) throws Exception {
		try {
			assertPassword();
			JSONObject req = getJsonReq(request);

			PdfData pdfd = getPdf(id);

			ExAssinadorExternoHash resp = new ExAssinadorExternoHash();

			resp.setSha1(BlucService.bytearray2b64(BlucService.calcSha1(pdfd.pdf)));
			resp.setSha256(BlucService.bytearray2b64(BlucService.calcSha256(pdfd.pdf)));
			resp.setSecret(pdfd.secret);

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	@Transacional
	@Put("/public/app/assinador-externo/doc/{id}/sign")
	public void assinadorExternoSave(String id) throws Exception {
		try {
			assertPassword();


		} catch (Exception e) {
			jsonError(e);
		}
		assinadorPopupSave(id);
	}

	@Transacional
	@Put("/app/assinador-popup/doc/{id}/sign")
	public void assinadorPopupSave(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			String envelope = req.getString("envelope");
			String time = req.getString("time");
			String extra = req.optString("extra", null);
			Boolean autenticar = false;
			Boolean juntar = null;
			Boolean tramitar = null;
			if (extra != null) {
				if (extra.contains("autenticar"))
					autenticar = true;
				if (extra.contains("nao_juntar"))
					juntar = false;
				else if (extra.contains("juntar"))
					juntar = true;
				if (extra.contains("nao_tramitar"))
					tramitar = false;
				else if (extra.contains("tramitar"))
					tramitar = true;
			}

			byte[] assinatura = Base64.decode(envelope);
			Date dt = dao().consultarDataEHoraDoServidor();

			if (id == null)
				throw new Exception("Id não informada.");
			Long cpf = req.getLong("cpf");
			String sigla = id2sigla(id) + ".pdf";

			ExMobil mob = Documento.getMobil(sigla);
			ExMovimentacao mov = Documento.getMov(mob, sigla);

			DpPessoa cadastrante = obterCadastrante(cpf, mob, mov);

			boolean apenasComSolicitacaoDeAssinatura = !Ex.getInstance().getConf().podePorConfiguracao(cadastrante, ExTipoDeConfiguracao.PODE_ASSINAR_SEM_SOLICITACAO);
			if (apenasComSolicitacaoDeAssinatura && !mob.doc().isAssinaturaSolicitada())
				throw new Exception("Documento requer solicitação de assinatura. Provavelmente, o documento foi editado após a solicitação.");

			String msg = null;

			DpLotacao lotaCadastrante = cadastrante != null ? cadastrante.getLotacao() : null;
			if (mov != null) {
				long tpMov = autenticar ? ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
						: ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO;

				Ex.getInstance().getBL().assinarMovimentacao(cadastrante, mov.getLotaTitular(), mov, dt, assinatura, null,
						tpMov);
				msg = "OK";
			} else if (mob != null) {
				long tpMov = autenticar ? ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
						: ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;
				// Nato: Assinatura externa não deve produzir transferência. 
				// Se preferir a configuração default, deveria trocar o último parâmetro por null.
				msg = Ex.getInstance().getBL().assinarDocumento(cadastrante, getLotaTitular(), mob.doc(), dt, assinatura,
						null, tpMov, juntar, tramitar == null ? false : tramitar, null, getTitular());
				if (msg != null)
					msg = "OK: " + msg;
				else
					msg = "OK";
			} else {
				throw new Exception("Não foi possível localizar o documento para gravar a assinatura.");
			}

			ExAssinadorExternoSave resp = new ExAssinadorExternoSave();
			resp.setStatus(msg);

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private DpPessoa obterCadastrante(Long cpf, ExMobil mob, ExMovimentacao mov) throws Exception {
		DpPessoa cadastrante = getCadastrante();
		if (cadastrante == null && cpf != null) {
			List<DpPessoa> pessoas = ExDao.getInstance().consultarPessoasAtivasPorCpf(cpf);
			for (DpPessoa p : pessoas) {
				if (mov != null && mov.getResp() != null) {
					if (p.equivale(mov.getResp())) {
						cadastrante = p;
						break;
					}
				} else if (p.equivale(mob.doc().getSubscritor())) {
					cadastrante = p;
				}
			}
			if (cadastrante == null && pessoas.size() >= 1)
				cadastrante = pessoas.get(0);
			if (cadastrante == null && mov == null)
				throw new Exception("Não foi possível localizar a pessoa que representa o subscritor.");
		}
		return cadastrante;
	}

	private class Signature {
		public String ref;
		public String signer;
		public String kind;
	}

	private class Movement {
		public Date time;
		public String department;
		public String kind;
	}

	private class DocIdGetResponse {
		public String status;
		public List<Signature> signature;
		public List<Movement> movement;
	}

	@Get("/public/app/assinador-externo/doc/{id}/info")
	public void assinadorExternoDocInfo(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			assertPassword();

			if (id == null)
				throw new Exception("Id não informada.");
			String sigla = id2sigla(id) + ".pdf";
			ExMobil mob = Documento.getMobil(sigla);
			ExMovimentacao mov = Documento.getMov(mob, sigla);

			DocIdGetResponse resp = new DocIdGetResponse();
			resp.signature = new ArrayList<>();
			for (ExMovimentacao m : mob.doc().getAssinaturasDigitais()) {
				Signature signature = new Signature();
				signature.ref = makeId(m.getReferencia());
				signature.kind = m.getExTipoMovimentacao().getDescricao();
				signature.signer = m.getObs();
				resp.signature.add(signature);
			}

			if (mov == null) {
				if (mob.getDoc().isProcesso())
					mob = mob.getDoc().getUltimoVolume();
				if (mob.getDoc().isExpediente())
					mob = mob.getDoc().getPrimeiraVia();
				resp.status = mob.getMarcadores();
				resp.movement = new ArrayList<>();

				for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
					if (m.isCancelada() || m.isCanceladora())
						continue;
					Movement movement = new Movement();
					movement.time = m.getDtIniMov();
					movement.department = m.getLotaCadastrante().getSigla();
					movement.kind = m.getExTipoMovimentacao().getDescricao();
					resp.movement.add(movement);
				}
			}

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private class SignRefGetResponse {
		public byte[] envelope;
		public Date time;
	}

	@Get("/public/app/assinador-externo/sign/{ref}")
	public void assinadorExternoSignCms(String ref) throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			assertPassword();

			if (ref == null)
				throw new Exception("Ref. não informada.");
			ExMovimentacao mov = ExDao.getInstance().consultar(Long.parseLong(ref.split("_")[1]), ExMovimentacao.class,
					false);
			if (!(mov.getExTipoMovimentacao().getId()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO)
					|| mov.getExTipoMovimentacao().getId()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO))) {
				throw new Exception("Não é assinatura digital");
			}
			SignRefGetResponse resp = new SignRefGetResponse();
			resp.envelope = mov.getConteudoBlobMov2();
			resp.time = mov.getDtMov();
			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private void assertPassword() throws Exception {
		String pwd = Prop.get("assinador.externo.password");
		if (pwd == null) 
			throw new Exception("Antes de utilizar o assinador externo é necessário configurar a propriedade sigaex.assinador.externo.senha");
		if (!pwd.equals(this.request.getHeader("Authorization")))
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

	protected void jsonError(final Exception e) throws Exception {
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
			arr.put(detail);
			json.put("errordetails", arr);
		} catch (JSONException e1) {
			throw new RuntimeException(e1);
		}

		String s = json.toString();
		response.setStatus(500);
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(s);
		response.flushBuffer();
		throw e;
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

	private String makeId(String sigla) {
		if (sigla == null)
			return null;
		return sigla.replace(":", "_");
	}

	private String id2sigla(String id) {
		if (id == null)
			return null;
		return id.replace("_", ":");
	}

	private String dateSecret(Date dt) {
		if (dt == null)
			return "null";
		else
			return Long.toString(dt.getTime());
	}

	private String docSecret(ExDocumento doc) {
		StringBuilder sb = new StringBuilder();
		sb.append(dateSecret(doc.getDtRegDoc()));
		sb.append(doc.getCadastrante().getId());
		return sb.toString();
	}

	private String movSecret(ExMovimentacao mov) {
		StringBuilder sb = new StringBuilder();
		sb.append(dateSecret(mov.getExDocumento().getDtRegDoc()));
		sb.append(dateSecret(mov.getDtIniMov()));
		sb.append(mov.getCadastrante().getId());
		return sb.toString();
	}

}
