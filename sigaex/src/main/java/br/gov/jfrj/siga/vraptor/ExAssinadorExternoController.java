package br.gov.jfrj.siga.vraptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xerces.impl.dv.util.Base64;
import org.jboss.logging.Logger;
import org.json.JSONObject;

import br.com.caelum.vraptor.Post;
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

	@Post("/public/app/assinador-externo/list")
	public void assinadorExternoList(String certificate, String time,
			String proof) throws Exception {
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
					aei.setId(sigla2id(ass.getDoc().getCodigoCompacto()));
					aei.setCode(ass.getDoc().getCodigo());
					aei.setDescr(ass.getDoc().getDescrDocumento());
					aei.setKind(ass.getDoc().getTipoDescr());
					aei.setOrigin("Siga-Doc");
					aei.setUrlView(permalink + ass.getDoc().getReferencia());
					aei.setUrlHash("sigadoc/hash/" + aei.getId());
					aei.setUrlSave("sigadoc/save/" + aei.getId());
					list.add(aei);
				}
				if (ass.getMovs() == null)
					continue;
				for (ExAssinavelMov assmov : ass.getMovs()) {
					ExAssinadorExternoListItem aei = new ExAssinadorExternoListItem();
					aei.setId(sigla2id(assmov.getMov().getReferencia()));
					aei.setCode(assmov.getMov().getReferencia());
					aei.setDescr(assmov.getMov().getDescrMov());
					aei.setKind(assmov.getMov().getTipoDescr());
					aei.setOrigin("Siga-Doc");
					aei.setUrlView(permalink + assmov.getMov().getReferencia());
					aei.setUrlHash("sigadoc/hash/" + aei.getId());
					aei.setUrlSave("sigadoc/save/" + aei.getId());
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

	@Post("/public/app/assinador-externo/hash/{id}")
	public void assinadorExternoHash(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);
			assertPassword(req);

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

	@Post("/public/app/assinador-externo/save/{id}")
	public void assinadorExternoSave(String id) throws Exception {
		try {
			JSONObject req = getJsonReq(request);

			assertPassword(req);

			String envelope = req.getString("envelope");
			String certificate = req.getString("certificate");
			String time = req.getString("time");
			String subject = req.optString("subject", null);
			String cpf = req.optString("cpf", null);
			String cn = req.optString("cn", null);

			byte[] assinatura = Base64.decode(envelope);
			byte[] certificado = Base64.decode(certificate);
			Date dt = dao().consultarDataEHoraDoServidor();

			String sigla = null;
			if (id != null) {
				sigla = id2sigla(id);
				sigla += ".pdf";
			}

			ExMobil mob = Documento.getMobil(sigla);
			ExMovimentacao mov = Documento.getMov(mob, sigla);

			String msg = null;

			if (mov != null) {
				long tpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO;

				Ex.getInstance()
						.getBL()
						.assinarMovimentacao(getCadastrante(),
								getLotaTitular(), mov, dt, assinatura, null,
								tpMov);
				msg = "OK";
			} else if (mob != null) {
				long tpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;
				msg = Ex.getInstance()
						.getBL()
						.assinarDocumento(getCadastrante(), getLotaTitular(),
								mob.doc(), dt, assinatura, null, tpMov);
				msg = "OK: " + msg;
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
		if (SigaExProperties.getAssinadorExternoPassword() != null
				&& !SigaExProperties.getAssinadorExternoPassword().equals(
						req.optString("password", null)))
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
		JSONObject json = new JSONObject();
		json.put("error", e.getMessage());
		String s = json.toString();
		result.use(Results.http())
				.addHeader("Content-Type", "application/json").body(s)
				.setStatusCode(500);
	}

	private static JSONObject getJsonReq(HttpServletRequest request) {
		try {
			String sJson = getBody(request);
			return new JSONObject(sJson);
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

	private String sigla2id(String sigla) {
		if (sigla == null)
			return null;
		return sigla.replace(":", "_");
	}

	private String id2sigla(String id) {
		if (id == null)
			return null;
		return id.replace("_", ":");
	}

}
