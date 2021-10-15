package br.gov.jfrj.siga.wf.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.vraptor.SigaIdDescr;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.relatorio.RelEstatisticaProcedimento;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDoc;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDocDetalhado;
import br.gov.jfrj.siga.wf.util.WfUtil;

@Controller
public class WfRelatorioController extends WfController {
	private static final Long REL_ESTATISTICAS_GERAIS = 1L;
	private static final Long REL_TEMPO_DE_DOCUMENTOS = 2L;
	private static final Long REL_TEMPO_DE_DOCUMENTOS_DETALHADO = 3L;
	private static final String VERIFICADOR_ACESSO = "REL:Relatórios";

	/**
	 * @deprecated CDI eyes only
	 */
	public WfRelatorioController() {
		super();
	}

	@Inject
	public WfRelatorioController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	@Get
	@Path("/app/rel/listar-para-gerar-relatorios")
	public void listarParaGerarRelatorios() throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		List<WfDefinicaoDeProcedimento> modelos = dao().listarAtivos(WfDefinicaoDeProcedimento.class, "nome");
		result.include("itens", modelos);
	}

	@Get
	@Path("/app/rel/medir/{sigla}")
	public void medir(String sigla) throws Exception {
		assertAcesso(ACESSO_RELATORIOS);
		WfDefinicaoDeProcedimento pd = dao().consultarPorSigla(sigla, WfDefinicaoDeProcedimento.class, null);

		result.include("orgao", getLotaTitular().getOrgaoUsuario().getNmOrgaoUsu());
		result.include("procedimento", pd);
		result.include("pdId", pd.getId());
		result.include("lstGruposIni", pd.getDefinicaoDeTarefa());
		result.include("lstGruposFim", pd.getDefinicaoDeTarefa());
		result.include("minMediaTruncada", Prop.get("rel.estatisticas.gerais.media.truncada.min").replace(".", ","));
		result.include("maxMediaTruncada", Prop.get("rel.estatisticas.gerais.media.truncada.max").replace(".", ","));
		result.include("listaTipoRelatorio", getListaTipoRelatorio());
	}

	private List<SigaIdDescr> getListaTipoRelatorio() {
		ArrayList<SigaIdDescr> lista = new ArrayList<SigaIdDescr>();

		lista.add(new SigaIdDescr(1, "Estatísticas gerais"));
		lista.add(new SigaIdDescr(2, "Tempo de documentos"));
		lista.add(new SigaIdDescr(3, "Tempo de documentos detalhado"));
		return lista;
	}

	@Get
	@Path("/app/rel/relatorio")
	public Download relatorio(Long pdId, Integer grpIni, Integer grpFim, String incluirAbertos) throws Exception {
		WfDefinicaoDeProcedimento pd = WfDefinicaoDeProcedimento.AR.findById(pdId);
		Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();

		for (Object o : getRequest().getParameterMap().keySet()) {
			String k = (String) o;
			String v = (String) getRequest().getParameter(k);
			parametrosRelatorio.put(k, v);
		}
		parametrosRelatorio.put("secaoUsuario", getLotaTitular().getOrgaoUsuario().getNmOrgaoUsu());
		parametrosRelatorio.put("nomeProcedimento", pd.getNome());
		parametrosRelatorio.put("incluirAbertos", incluirAbertos == null ? false : incluirAbertos);
		parametrosRelatorio.put("inicioGrupo",
				(grpIni == null || grpIni < 0) ? null : pd.getDefinicaoDeTarefa().get(grpIni).getNome());
		parametrosRelatorio.put("fimGrupo",
				(grpFim == null || grpFim < 0) ? null : pd.getDefinicaoDeTarefa().get(grpFim).getNome());

		addParametrosPersonalizadosOrgao(parametrosRelatorio);

		byte[] buff = null;
		if (getRelatorioEscolhido().equals(REL_ESTATISTICAS_GERAIS)) {
			buff = gerarRelEstGeral(parametrosRelatorio);
		}
		if (getRelatorioEscolhido().equals(REL_TEMPO_DE_DOCUMENTOS)) {
			buff = gerarRelTempoDoc(parametrosRelatorio);
		}
		if (getRelatorioEscolhido().equals(REL_TEMPO_DE_DOCUMENTOS_DETALHADO)) {
			buff = gerarRelTempoDocDetalhado(parametrosRelatorio);
		}

		return new ByteArrayDownload(buff, "application/pdf", "relatorio.pdf", false);
	}

	private void addParametrosPersonalizadosOrgao(final Map<String, Object> parameters) {
		parameters.put("titulo", Prop.get("/siga.relat.titulo"));
		parameters.put("subtitulo", Prop.get("/siga.relat.subtitulo"));
		parameters.put("brasao", Prop.get("/siga.relat.brasao"));
	}

	private Long getRelatorioEscolhido() {
		return Long.valueOf(getRequest().getParameter("selecaoRelatorio"));
	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método é
	 * executado quando a action "emiteRelEstatisticaProcedimento.action" é chamada.
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private byte[] gerarRelTempoDocDetalhado(Map<String, Object> parametrosRelatorio) throws Exception {
		try {
			RelTempoDocDetalhado rel = new RelTempoDocDetalhado(parametrosRelatorio);
			rel.gerar();

			return rel.getRelatorioPDF();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método é
	 * executado quando a action "emiteRelEstatisticaProcedimento.action" é chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	private byte[] gerarRelEstGeral(Map<String, Object> parametrosRelatorio) throws Exception {
		try {

			RelEstatisticaProcedimento rel = new RelEstatisticaProcedimento(parametrosRelatorio);
			rel.gerar();

			return rel.getRelatorioPDF();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método é
	 * executado quando a action "emiteRelEstatisticaProcedimento.action" é chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	private byte[] gerarRelTempoDoc(Map<String, Object> parametrosRelatorio) throws Exception {
		try {
			RelTempoDoc rel = new RelTempoDoc(parametrosRelatorio);
			rel.gerar();

			return rel.getRelatorioPDF();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

}
