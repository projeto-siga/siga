package br.gov.jfrj.siga.wf.vraptor;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfUtil;

public class WfController extends SigaController {

	static final String ACESSO_WF = "WF:Módulo de Workflow";
	protected static final String ACESSO_EXCLUIR_INSTANCIA_DE_PROCESSO = "EXCLUIR:Excluir instancia de processo";
	protected static final String ACESSO_ENCERRAR_INSTANCIA_DE_PROCESSO = "ENCERRAR:Encerrar instancia de processo";
	protected static final String ACESSO_MOVER_TOKEN = "MOVER_TOKEN:Mover token";
	protected static final String ACESSO_CONFIGURAR_INICIADORES = "CONFIGURAR:Configurar iniciadores";
	protected static final String ACESSO_DESIGNAR_TAREFAS = "DESIGNAR:Designar tarefas";
	protected static final String ACESSO_RELATORIOS = "REL:Relatórios";

	protected WfUtil util;

	/**
	 * @deprecated CDI eyes only
	 */
	public WfController() {
		super();
	}

	@Inject
	public WfController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, (CpDao) dao, so, null);
		this.util = util;
	}

	public void assertAcesso(String pathServico) throws AplicacaoException {
		so.assertAcesso(ACESSO_WF + ";" + pathServico);
	}

	protected WfDao dao() {
		return WfDao.getInstance();
	}

	/**
	 * Como na página pesquisarDesignação.jsp os componentes de seleção dos atores
	 * são dinâmicos, é necessária a extração dos dados diretamente dos parâmetros
	 * do request. O prefixo "matricula_" é difinido na página
	 * pesquisaDesignacao.jsp e os sufixos "_pessoaSel.id" e "_pessoaSel.sigla" são
	 * definidos na TAG selecao.tag
	 * 
	 * @param id da tarefa
	 * @return Um objeto DpPessoa do ator selecionado na página.
	 */
	protected DpPessoa extrairAtor(long id) {
		String keyMatriculaId = "matricula_" + id + "_pessoaSel.id";
		String keyMatriculaSigla = "matricula_" + id + "_pessoaSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		DpPessoa ator = null;
		Map<?, ?> parametros = this.getRequest().getParameterMap();
		if (parametros.containsKey(keyMatriculaId) && parametros.containsKey(keyMatriculaSigla)) {
			responsavelId = ((String[]) parametros.get(keyMatriculaId))[0];
			responsavelSigla = ((String[]) parametros.get(keyMatriculaSigla))[0];
			if (!responsavelId.equals("") && !responsavelSigla.equals("")) {
				ator = daoPes(new Long(responsavelId));
			}
		}

		return ator;
	}

	/**
	 * Como na página pesquisarDesignação.jsp os componentes de seleção das lotações
	 * são dinâmicas, é necessária a extração dos dados diretamente dos parâmetros
	 * do request. O prefixo "lotacao_" é difinido na página pesquisaDesignacao.jsp
	 * e os sufixos "_lotacaoSel.id" e "_lotacaoSel.sigla" são definidos na TAG
	 * selecao.tag
	 * 
	 * @param id da tarefa
	 * @return Um objeto DpPessoa do ator selecionado na página
	 */
	protected DpLotacao extrairLotaAtor(long id) {
		String keyLotacaoId = "lotacao_" + id + "_lotacaoSel.id";
		String keyLotacaoSigla = "lotacao_" + id + "_lotacaoSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		Map<?, ?> parametros = this.getRequest().getParameterMap();
		DpLotacao lotaAtor = null;

		if (parametros.containsKey(keyLotacaoId) && parametros.containsKey(keyLotacaoSigla)) {
			responsavelId = ((String[]) parametros.get(keyLotacaoId))[0];
			responsavelSigla = ((String[]) parametros.get(keyLotacaoSigla))[0];
			if (!responsavelId.equals("") && !responsavelSigla.equals("")) {
				lotaAtor = daoLot(new Long(responsavelId));
			}
		}
		return lotaAtor;
	}
	
}
