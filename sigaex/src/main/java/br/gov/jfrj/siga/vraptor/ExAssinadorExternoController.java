package br.gov.jfrj.siga.vraptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xerces.impl.dv.util.Base64;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoHash;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoList;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoListItem;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoPdf;
import br.gov.jfrj.siga.ex.bl.ExAssinadorExternoSave;
import br.gov.jfrj.siga.ex.bl.ExAssinavelDoc;
import br.gov.jfrj.siga.ex.bl.ExAssinavelMov;
import br.gov.jfrj.siga.hibernate.ExDao;

import com.google.gson.Gson;

@Resource
public class ExAssinadorExternoController extends ExController {

	private static final Logger LOGGER = Logger
			.getLogger(ExAssinadorExternoController.class);

	public ExAssinadorExternoController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em, Validator validator) {

		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("/public/app/assinador-externo/doc/list")
	public void assinadorExternoList() throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			assertPassword(req);

			String urlapi = req.getString("urlapi");
			String sCpf = req.getString("cpf");

			String permalink = urlapi.split("sigaex/public/app/")[0]
					+ "siga/permalink/";

			Long cpf = Long.valueOf(sCpf);
			DpPessoa pes = dao().consultarPorCpf(cpf);
			if (pes == null)
				throw new Exception("Nenhuma pessoa localizada com o CPF: "
						+ sCpf);
			List<ExAssinadorExternoListItem> list = new ArrayList<ExAssinadorExternoListItem>();
			List<ExAssinavelDoc> assinaveis = Ex.getInstance().getBL()
					.obterAssinaveis(pes, pes.getLotacao());
			for (ExAssinavelDoc ass : assinaveis) {
				if (ass.isPodeAssinar()) {
					ExAssinadorExternoListItem aei = new ExAssinadorExternoListItem();
					aei.setId(makeId(cpf, ass.getDoc().getCodigoCompacto()));
					aei.setCode(ass.getDoc().getCodigo());
					aei.setDescr(ass.getDoc().getDescrDocumento());
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
					aei.setId(makeId(cpf, assmov.getMov().getReferencia()));
					aei.setCode(assmov.getMov().getReferencia());
					aei.setDescr(assmov.getMov().getObs());
					aei.setKind(assmov.getMov().getTipoDescr());
					aei.setOrigin("Siga-Doc");
					aei.setUrlView(permalink
							+ assmov.getMov().getReferencia().replace(":", "/"));
					aei.setUrlHash("sigadoc/doc/" + aei.getId() + "/hash");
					aei.setUrlSave("sigadoc/doc/" + aei.getId() + "/sign");
					list.add(aei);
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
			assertPassword(req);

			byte[] pdf = getPdf(id);

			ExAssinadorExternoPdf resp = new ExAssinadorExternoPdf();

			resp.setDoc(BlucService.bytearray2b64(pdf));

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private byte[] getPdf(String id) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, Exception,
			SQLException {
		byte[] pdf = null;

		String sigla = null;
		if (id != null) {
			sigla = id2sigla(id);
			sigla += ".pdf";
		}

		ExMobil mob = Documento.getMobil(sigla);
		ExMovimentacao mov = Documento.getMov(mob, sigla);

		if (mov != null)
			pdf = mov.getConteudoBlobpdf();
		else if (mob != null) {
			pdf = mob.doc().getConteudoBlobPdf();
		}

		if (pdf == null)
			throw new Exception("Não foi possível localizar o PDF.");
		return pdf;
	}

	@Get("/public/app/assinador-externo/doc/{id}/hash")
	public void assinadorExternoHash(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);
			assertPassword(req);

			byte[] pdf = getPdf(id);

			ExAssinadorExternoHash resp = new ExAssinadorExternoHash();

			// Descomentar para forçar PKCS7
			// resp.setPolicy("PKCS7");
			// resp.setDoc(BlucService.bytearray2b64(pdf));

			resp.setSha1(BlucService.bytearray2b64(BlucService.calcSha1(pdf)));
			resp.setSha256(BlucService.bytearray2b64(BlucService
					.calcSha256(pdf)));

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	@Put("/public/app/assinador-externo/doc/{id}/sign")
	public void assinadorExternoSave(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			assertPassword(req);

			String envelope = req.getString("envelope");
			String time = req.getString("time");
			String extra = req.optString("extra", null);
			boolean autenticar = "autenticar".equals(extra);

			byte[] assinatura = Base64.decode(envelope);
			Date dt = dao().consultarDataEHoraDoServidor();

			if (id == null)
				throw new Exception("Id não informada.");
			Long cpf = id2cpf(id);
			String sigla = id2sigla(id) + ".pdf";

			ExMobil mob = Documento.getMobil(sigla);
			ExMovimentacao mov = Documento.getMov(mob, sigla);

			DpPessoa cadastrante = null;
			List<DpPessoa> pessoas = ExDao.getInstance()
					.consultarPessoasAtivasPorCpf(cpf);
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
			if (cadastrante == null)
				throw new Exception(
						"Não foi possível localizar a pessoa que representa o subscritor.");

			String msg = null;

			if (mov != null) {
				long tpMov = autenticar ? ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
						: ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO;

				Ex.getInstance()
						.getBL()
						.assinarMovimentacao(cadastrante,
								cadastrante.getLotacao(), mov, dt, assinatura,
								null, tpMov);
				msg = "OK";
			} else if (mob != null) {
				long tpMov = autenticar ? ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
						: ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;
				msg = Ex.getInstance()
						.getBL()
						.assinarDocumento(cadastrante,
								cadastrante.getLotacao(), mob.doc(), dt,
								assinatura, null, tpMov);
				if (msg != null)
					msg = "OK: " + msg;
				else
					msg = "OK";
			} else {
				throw new Exception(
						"Não foi possível localizar o documento para gravar a assinatura.");
			}

			ExAssinadorExternoSave resp = new ExAssinadorExternoSave();
			resp.setStatus(msg);

			jsonSuccess(resp);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	private void assertPassword(JSONObject req) throws Exception {
		String pwd = SigaExProperties.getAssinadorExternoPassword();
		if (pwd != null && !pwd.equals(this.request.getHeader("Authorization")))
			throw new Exception("Falha de autenticação.");
	}

	protected void jsonSuccess(final Object resp) {
		Gson gson = new Gson();
		String s = gson.toJson(resp);
		result.use(Results.http())
				.addHeader("Content-Type", "application/json").body(s)
				.setStatusCode(200);
	}

	protected void jsonError(final Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String errstack = sw.toString(); // stack trace as a string

		JSONObject json = new JSONObject();
		json.put("errormsg", e.getMessage());

		// Error Details
		JSONArray arr = new JSONArray();
		JSONObject detail = new JSONObject();
		detail.put("context", context);
		detail.put("service", "sigadocsigner");
		detail.put("stacktrace", errstack);
		json.put("errordetails", arr);

		String s = json.toString();
		result.use(Results.http())
				.addHeader("Content-Type", "application/json").body(s)
				.setStatusCode(500);
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
				if (key instanceof String
						&& request.getParameter((String) key) instanceof String
						&& !req.has((String) key))
					req.put((String) key, request.getParameter((String) key));

			return req;
		} catch (Exception ex) {
			throw new RuntimeException("Cannot parse request body as JSON", ex);
		}
	}

	private static String getBody(HttpServletRequest request)
			throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
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

	private String makeId(Long cpf, String sigla) {
		if (sigla == null)
			return null;
		return cpf.toString() + "__" + sigla.replace(":", "_");
	}

	private Long id2cpf(String id) {
		if (id == null)
			return null;
		return Long.valueOf(id.split("__")[0]);
	}

	private String id2sigla(String id) {
		if (id == null)
			return null;
		return id.split("__")[1].replace("_", ":");
	}

}
