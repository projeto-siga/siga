package br.gov.jfrj.siga.wf.vraptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.encoding.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.SigaSelecionavelControllerSupport;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeResponsavel;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.util.NaoSerializar;

@Controller
@Path("app/diagrama")
public class WfDiagramaController
		extends SigaSelecionavelControllerSupport<WfDefinicaoDeProcedimento, DaoFiltroSelecionavel> {

	private static final String VERIFICADOR_ACESSO = "DEFP:Gerenciar Diagramas";
	private static final String UTF8 = "utf-8";

	public static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);

	public static final Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			System.out.println(f.getName());
			if (f.getName().startsWith("hisIdc"))
				return true;
			return f.getAnnotation(NaoSerializar.class) != null;
		}

	}).registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
			.registerTypeHierarchyAdapter(Date.class, new DateToStringTypeAdapter())
			.registerTypeHierarchyAdapter(WfDefinicaoDeResponsavel.class, new WfDefinicaoDeResponsavelTypeAdapter())
			.registerTypeHierarchyAdapter(DpLotacao.class, new DpLotacaoTypeAdapter())
			.registerTypeHierarchyAdapter(DpPessoa.class, new DpPessoaTypeAdapter()).setPrettyPrinting().create();

	private static class DpLotacaoTypeAdapter implements JsonSerializer<DpLotacao> {
		public JsonElement serialize(DpLotacao src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject o = new JsonObject();
			JsonObject oo = new JsonObject();
			o.add("originalObject", oo);
			oo.addProperty("key", Long.toString(src.getId()));
			oo.addProperty("firstLine", src.getSigla());
			oo.addProperty("secondLine", src.getDescricao());
			return oo;
		}
	}

	private static class DpPessoaTypeAdapter implements JsonSerializer<DpPessoa> {
		public JsonElement serialize(DpPessoa src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject o = new JsonObject();
			JsonObject oo = new JsonObject();
			o.add("originalObject", oo);
			oo.addProperty("key", Long.toString(src.getId()));
			oo.addProperty("firstLine", src.getSigla());
			oo.addProperty("secondLine", src.getDescricao());
			return oo;
		}
	}

	private static class WfDefinicaoDeResponsavelTypeAdapter implements JsonSerializer<WfDefinicaoDeResponsavel> {
		public JsonElement serialize(WfDefinicaoDeResponsavel src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.getIdInicial());
		}
	}

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

	private HttpServletResponse response;
	private ServletContext context;

	/**
	 * @deprecated CDI eyes only
	 */
	public WfDiagramaController() {
		super();
	}

	@Inject
	public WfDiagramaController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, EntityManager em,
			HttpServletResponse response, ServletContext context) {
		super(request, result, dao, so, em);
		this.response = response;
		this.context = context;
	}

	@Get("listar")
	public void lista() throws Exception {
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			List<WfDefinicaoDeProcedimento> modelos = dao().listarAtivos(WfDefinicaoDeProcedimento.class, "nome");
			result.include("itens", modelos);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		} catch (Exception ex) {
			throw new AplicacaoException(ex.getMessage(), 0, ex);
		}
	}

	@Get("editar")
	public void edita(final Long id) throws UnsupportedEncodingException {
		assertAcesso(VERIFICADOR_ACESSO);
		if (id != null)
			result.include("pd", WfDefinicaoDeProcedimento.AR.findById(id));
	}

	@Get("{id}/carregar")
	public void carregar(final Long id) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		try {
			WfDefinicaoDeProcedimento pd = buscar(id);
			jsonSuccess(pd);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	@Transacional
	@Post("gravar")
	public void editarGravar(Long id, WfDefinicaoDeProcedimento pd) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);

		if (pd.getOrgaoUsuario() == null)
			pd.setOrgaoUsuario(getTitular().getOrgaoUsuario());

		SortedSet<Sincronizavel> setDepois = new TreeSet<>();
		SortedSet<Sincronizavel> setAntes = new TreeSet<>();

		setDepois.add(pd);

		// Prepara a definição para ser sincronizada
		if (pd.getDefinicaoDeTarefa() != null) {
			for (WfDefinicaoDeTarefa td : pd.getDefinicaoDeTarefa()) {
				td.setDefinicaoDeProcedimento(pd);
				setDepois.add(td);
				if (td.getSeguinteIde() != null) {
					for (WfDefinicaoDeTarefa td2 : pd.getDefinicaoDeTarefa()) {
						if (td.getSeguinteIde().equals(td2.getIdExterna())) {
							td.setSeguinte(td2);
							break;
						}
					}
				}
				if (td.getPessoaId() != null)
					td.setPessoa(dao().consultar(td.getPessoaId(), DpPessoa.class, false));
				if (td.getLotacaoId() != null)
					td.setLotacao(dao().consultar(td.getLotacaoId(), DpLotacao.class, false));
				if (td.getDefinicaoDeResponsavelId() != null)
					td.setDefinicaoDeResponsavel(
							dao().consultar(td.getDefinicaoDeResponsavelId(), WfDefinicaoDeResponsavel.class, false));

				if (td.getDefinicaoDeVariavel() != null) {
					for (WfDefinicaoDeVariavel vd : td.getDefinicaoDeVariavel()) {
						vd.setDefinicaoDeTarefa(td);
						setDepois.add(vd);
					}
				}
				if (td.getDefinicaoDeDesvio() != null) {
					for (WfDefinicaoDeDesvio dd : td.getDefinicaoDeDesvio()) {
						dd.setDefinicaoDeTarefa(td);
						setDepois.add(dd);
						if (dd.getSeguinteIde() != null) {
							for (WfDefinicaoDeTarefa td2 : pd.getDefinicaoDeTarefa()) {
								if (dd.getSeguinteIde().equals(td2.getIdExterna())) {
									dd.setSeguinte(td2);
									break;
								}
							}
						}
					}
				}
			}
		}

		if (id != null) {
			WfDefinicaoDeProcedimento opd = dao().consultar(id, WfDefinicaoDeProcedimento.class, false);
			opd = dao().consultarAtivoPorIdInicial(WfDefinicaoDeProcedimento.class, opd.getHisIdIni());
			setAntes.add(opd);
			pd.setAno(opd.getAno());
			pd.setNumero(opd.getNumero());
			pd.setHisIdIni(opd.getHisIdIni());
			pd.setOrgaoUsuario(opd.getOrgaoUsuario());
			if (opd.getDefinicaoDeTarefa() != null) {
				for (WfDefinicaoDeTarefa otd : opd.getDefinicaoDeTarefa()) {
					setAntes.add(otd);
					if (otd.getDefinicaoDeVariavel() != null)
						for (WfDefinicaoDeVariavel ovd : otd.getDefinicaoDeVariavel())
							setAntes.add(ovd);
					if (otd.getDefinicaoDeVariavel() != null)
						for (WfDefinicaoDeDesvio odd : otd.getDefinicaoDeDesvio())
							setAntes.add(odd);
				}
			}
		}

		// Utilizaremos o sincronizador para perceber apenas as diferenças entre a
		// definição que está guardada no banco de dados e a nova versão submetida..
		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setDepois);
		sinc.setSetAntigo(setAntes);
		List<Item> list = sinc.getEncaixe();
		sinc.ordenarOperacoes();

		for (Item i : list) {
			Date dt = new Date();
			switch (i.getOperacao()) {
			case alterar:
				dao().gravarComHistorico((HistoricoAuditavel) i.getNovo(), (HistoricoAuditavel) i.getAntigo(), dt,
						getIdentidadeCadastrante());
				break;
			case incluir:
				dao().gravarComHistorico((HistoricoAuditavel) i.getNovo(), getIdentidadeCadastrante());
				break;
			case excluir:
				((HistoricoAuditavel) i.getAntigo()).setHisDtFim(dt);
				dao().gravarComHistorico((HistoricoAuditavel) i.getAntigo(), getIdentidadeCadastrante());
				break;
			}
		}

		if ("Aplicar".equals(param("submit"))) {
			result.redirectTo("editar?id=" + pd.getId());
			return;
		}
		result.use(Results.json()).from("OK");
	}

	@Transacional
	@Get("desativar")
	public void desativar(final Long id) throws Exception {
		ModeloDao.iniciarTransacao();
		assertAcesso(VERIFICADOR_ACESSO);
		if (id == null) {
			throw new AplicacaoException("ID não informada");
		}
		final WfDefinicaoDeProcedimento pd = dao().consultar(id, WfDefinicaoDeProcedimento.class, false);
		dao().excluirComHistorico(pd, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
		ModeloDao.commitTransacao();

		result.redirectTo(WfDiagramaController.class).lista();
	}

	private WfDefinicaoDeProcedimento buscar(final Long id) {
		if (id != null) {
			return dao().consultar(id, WfDefinicaoDeProcedimento.class, false);
		}
		return new WfDefinicaoDeProcedimento();
	}

	private WfDefinicaoDeProcedimento buscarAntiga(final Long idInicial) {
		if (idInicial != null) {
			return dao().consultarAtivoPorIdInicial(WfDefinicaoDeProcedimento.class, idInicial);
		}
		return null;
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		return null;
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
			json.put("errordetails", arr);
		} catch (JSONException e1) {
			throw new RuntimeException(e1);
		}

		String s = json.toString();
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(500);
		response.flushBuffer();
		throw e;
	}

}
