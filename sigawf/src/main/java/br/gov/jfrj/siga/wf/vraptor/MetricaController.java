package br.gov.jfrj.siga.wf.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Task;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.gov.jfrj.siga.vraptor.SigaIdDescr;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.SigaWfProperties;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.relatorio.RelEstatisticaProcedimento;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDoc;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDocDetalhado;

@Resource
public class MetricaController extends WfController {
	private static final Long REL_ESTATISTICAS_GERAIS = 1L;
	private static final Long REL_TEMPO_DE_DOCUMENTOS = 2L;
	private static final Long REL_TEMPO_DE_DOCUMENTOS_DETALHADO = 3L;

	public MetricaController(HttpServletRequest request, Result result,
			WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	/**
	 * Este método é executado quando a action "medir.action" é chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	public void medir(Long orgao, String procedimento, Long pdId)
			throws Exception {
		assertAcesso(ACESSO_ANALISAR_METRICAS);

		ProcessDefinition pd = WfDao.getInstance().getProcessDefinition(pdId);

		Set<Task> tarefas = WfDao.getInstance().getTodasAsTarefas(pd.getName());

		result.include("orgao", getLotaTitular().getOrgaoUsuario()
				.getNmOrgaoUsu());

		result.include("procedimento", procedimento);
		result.include("pdId", pdId);
		result.include("lstGruposIni", tarefas);
		result.include("lstGruposFim", tarefas);
		result.include("minMediaTruncada", SigaWfProperties.getRelEstatGeraisMinMediaTrunc().toString()
				.replace(".", ","));
		result.include("maxMediaTruncada", SigaWfProperties.getRelEstatGeraisMaxMediaTrunc().toString()
				.replace(".", ","));
		result.include("listaTipoRelatorio", getListaTipoRelatorio());
	}

	private List<SigaIdDescr> getListaTipoRelatorio() {
		ArrayList<SigaIdDescr> lista = new ArrayList<SigaIdDescr>();
		
		lista.add(new SigaIdDescr(1, "Estatísticas gerais"));
		lista.add(new SigaIdDescr(2, "Tempo de documentos"));
		lista.add(new SigaIdDescr(3, "Tempo de documentos detalhado"));
		return lista;
	}

	public Download relatorio(Long pdId, Long grpIni, Long grpFim,
			String incluirAbertos) throws Exception {
		Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();

		for (Object o : getRequest().getParameterMap().keySet()) {
			String k = (String) o;
			String v = (String) getRequest().getParameter(k);
			parametrosRelatorio.put(k, v);
		}

		parametrosRelatorio.put("secaoUsuario", getLotaTitular()
				.getOrgaoUsuario().getNmOrgaoUsu());
		parametrosRelatorio.put("nomeProcedimento", WfDao.getInstance()
				.getProcessDefinition(pdId).getName());
		parametrosRelatorio.put("incluirAbertos",
				incluirAbertos == null ? false : incluirAbertos);
		parametrosRelatorio.put("inicioGrupo",
				(grpIni == null || grpIni < 0) ? null : getNomeGrupo(grpIni));
		parametrosRelatorio.put("fimGrupo",
				(grpFim == null || grpFim < 0) ? null : getNomeGrupo(grpFim));

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

		return new ByteArrayDownload(buff, "application/pdf", "relatorio.pdf",
				false);
	}

	/**
	 * Método utilizado para
	 * 
	 * @param idGrupo
	 * @return
	 */
	private String getNomeGrupo(Long idGrupo) {
		Task t = WfDao.getInstance().consultar(idGrupo, Task.class, false);
		return t.getName();

	}

	private Long getRelatorioEscolhido() {
		return Long.valueOf(getRequest().getParameter("selecaoRelatorio"));
	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método
	 * é executado quando a action "emiteRelEstatisticaProcedimento.action" é
	 * chamada.
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private byte[] gerarRelTempoDocDetalhado(
			Map<String, Object> parametrosRelatorio) throws Exception {
		try {
			RelTempoDocDetalhado rel = new RelTempoDocDetalhado(
					parametrosRelatorio);
			rel.gerar();

			return rel.getRelatorioPDF();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método
	 * é executado quando a action "emiteRelEstatisticaProcedimento.action" é
	 * chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	private byte[] gerarRelEstGeral(Map<String, Object> parametrosRelatorio)
			throws Exception {
		try {

			RelEstatisticaProcedimento rel = new RelEstatisticaProcedimento(
					parametrosRelatorio);
			rel.gerar();

			return rel.getRelatorioPDF();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método
	 * é executado quando a action "emiteRelEstatisticaProcedimento.action" é
	 * chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	private byte[] gerarRelTempoDoc(Map<String, Object> parametrosRelatorio)
			throws Exception {
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
