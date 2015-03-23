package br.gov.jfrj.siga.wf.vraptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

public class WfController extends SigaController {

	protected WfUtil util;

	public WfController(HttpServletRequest request, Result result, WfDao dao,
			SigaObjects so, WfUtil util) {
		super(request, result, (CpDao) dao, so, null);
		this.util = util;

		result.include("processDefinitions", getProcessDefinitions());
	}

	private List<ProcessDefinition> getProcessDefinitions() {
		GraphSession graph = WfContextBuilder.getJbpmContext()
				.getGraphSession();
		// processDefinitions = graph.findLatestProcessDefinitions();
		List<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();
		for (ProcessDefinition pd : (Collection<ProcessDefinition>) graph
				.findLatestProcessDefinitions()) {
			// if (Wf.getInstance().getComp().podeInstanciarProcedimento(
			// getTitular(), getLotaTitular(), pd.getName()))
			processDefinitions.add(pd);
		}
		return processDefinitions;
	}

	public void assertAcesso(String pathServico) throws AplicacaoException {
		so.assertAcesso("WF:Módulo de Workflow;" + pathServico);
	}

	protected WfDao dao() {
		return WfDao.getInstance();
	}

	/**
	 * Como na página pesquisarDesignação.jsp os componentes de seleção dos
	 * atores são dinâmicos, é necessária a extração dos dados diretamente dos
	 * parâmetros do request. O prefixo "matricula_" é difinido na página
	 * pesquisaDesignacao.jsp e os sufixos "_pessoaSel.id" e "_pessoaSel.sigla"
	 * são definidos na TAG selecao.tag
	 * 
	 * @param id
	 *            da tarefa
	 * @return Um objeto DpPessoa do ator selecionado na página.
	 */
	protected DpPessoa extrairAtor(long id) {
		String keyMatriculaId = "matricula_" + id + "_pessoaSel.id";
		String keyMatriculaSigla = "matricula_" + id + "_pessoaSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		DpPessoa ator = null;
		Map<?, ?> parametros = this.getRequest().getParameterMap();
		if (parametros.containsKey(keyMatriculaId)
				&& parametros.containsKey(keyMatriculaSigla)) {
			responsavelId = ((String[]) parametros.get(keyMatriculaId))[0];
			responsavelSigla = ((String[]) parametros.get(keyMatriculaSigla))[0];
			if (!responsavelId.equals("") && !responsavelSigla.equals("")) {
				ator = daoPes(new Long(responsavelId));
			}
		}

		return ator;
	}

	/**
	 * Como na página pesquisarDesignação.jsp os componentes de seleção das
	 * lotações são dinâmicas, é necessária a extração dos dados diretamente dos
	 * parâmetros do request. O prefixo "lotacao_" é difinido na página
	 * pesquisaDesignacao.jsp e os sufixos "_lotacaoSel.id" e
	 * "_lotacaoSel.sigla" são definidos na TAG selecao.tag
	 * 
	 * @param id
	 *            da tarefa
	 * @return Um objeto DpPessoa do ator selecionado na página
	 */
	protected DpLotacao extrairLotaAtor(long id) {
		String keyLotacaoId = "lotacao_" + id + "_lotacaoSel.id";
		String keyLotacaoSigla = "lotacao_" + id + "_lotacaoSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		Map<?, ?> parametros = this.getRequest().getParameterMap();
		DpLotacao lotaAtor = null;

		if (parametros.containsKey(keyLotacaoId)
				&& parametros.containsKey(keyLotacaoSigla)) {
			responsavelId = ((String[]) parametros.get(keyLotacaoId))[0];
			responsavelSigla = ((String[]) parametros.get(keyLotacaoSigla))[0];
			if (!responsavelId.equals("") && !responsavelSigla.equals("")) {
				lotaAtor = daoLot(new Long(responsavelId));
			}
		}
		return lotaAtor;
	}
	
	/**
	 * Retorna a lista de procedimentos que podem ter designações definidas.
	 * Este método é usado pela página pesquisarDesignação.jsp
	 * 
	 * @return Lista de definições de processo
	 */
	protected List<ProcessDefinition> getListaProcedimento() {
		@SuppressWarnings("unchecked")
		List<ProcessDefinition> lista = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findLatestProcessDefinitions();
		// Markenson: O código abaixo foi inserido para evitar a carga de
		// definicões de processos defeituosos
		// Esse problema foi detectado quando o Orlando fez o deploy de um
		// processo sem definir o nome
		// O tratamento de deploys deve fazer essa verificação
		List<ProcessDefinition> resultado = new ArrayList<ProcessDefinition>();
		for (ProcessDefinition p : lista) {
			if (p.getName() != null) {
				resultado.add(p);
			}
		}

		return resultado;
	}

}
