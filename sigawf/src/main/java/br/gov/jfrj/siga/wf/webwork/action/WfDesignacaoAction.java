/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.wf.webwork.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.Task;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.wf.Designacao;
import br.gov.jfrj.siga.wf.WfConfiguracao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

public class WfDesignacaoAction extends WfSigaActionSupport {

	private static int TIPO_RESP_INDEFINIDO = 0;
	private static int TIPO_RESP_MATRICULA = 1;
	private static int TIPO_RESP_LOTACAO = 2;
	private static int TIPO_RESP_LOTA_SUP = 3;
	private static int TIPO_RESP_SUP_HIER = 4;
	private static int TIPO_RESP_EXPRESSAO = 5;

	private List<Designacao> listaDesignacao = new ArrayList<Designacao>();
	private List<Designacao> listaDesignacaoRaia = new ArrayList<Designacao>();
	private List<Designacao> listaDesignacaoTarefa = new ArrayList<Designacao>();
	private List<TipoResponsavel> listaTipoResponsavel = new ArrayList<TipoResponsavel>();
	private Map<Integer, TipoResponsavel> mapaTipoResponsavel = new HashMap<Integer, TipoResponsavel>();

	private Set<String> raiaProcessada = new HashSet();

	private String orgao;
	private String procedimento;
	// private String procedimentoGravado;

	private ProcessDefinition pd;

	/**
	 * Inicializa os tipos de responsáveis e suas respectivas expressões, quando
	 * houver.
	 */
	public WfDesignacaoAction() {
		TipoResponsavel tpIndefinido = new TipoResponsavel(
				TIPO_RESP_INDEFINIDO, "[Indefinido]", "");
		TipoResponsavel tpMatricula = new TipoResponsavel(TIPO_RESP_MATRICULA,
				"Matrícula", "matricula");
		TipoResponsavel tpLotacao = new TipoResponsavel(TIPO_RESP_LOTACAO,
				"Lotação", "lotacao");
		TipoResponsavel tpLotaSuperior = new TipoResponsavel(
				TIPO_RESP_LOTA_SUP, "Lotação Superior",
				"previous --> group() --> superior_group");
		TipoResponsavel tpSupHier = new TipoResponsavel(TIPO_RESP_SUP_HIER,
				"Superior Hierárquico", "previous --> chief");
		TipoResponsavel tpExpressao = new TipoResponsavel(TIPO_RESP_EXPRESSAO,
				"Expressão", "");

		listaTipoResponsavel.add(tpIndefinido);
		listaTipoResponsavel.add(tpMatricula);
		listaTipoResponsavel.add(tpLotacao);
		listaTipoResponsavel.add(tpLotaSuperior);
		listaTipoResponsavel.add(tpSupHier);
		listaTipoResponsavel.add(tpExpressao);

		for (TipoResponsavel tr : listaTipoResponsavel) {
			mapaTipoResponsavel.put(tr.id, tr);
		}
	}

	/**
	 * O relatório ainda não está ativo.
	 */
	public void aGerarRelatorioDesignacao() {

	}

	/**
	 * Grava a configuração da designação. Inicialmente processa-se as raias e
	 * depois as tarefas, desativa-se as configurações antigas e retorna à
	 * página pesquisarDesignação.jsp com os dados gravados.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String aGravarDesignacao() throws Exception {

		selecionarApenasUmOrgao();
		pd = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getGraphSession().findLatestProcessDefinition(procedimento);
		Date horaDoDB = dao().consultarDataEHoraDoServidor();
		Set<Long> raiasGravadas = new HashSet<Long>();

		listaDesignacaoRaia = getDesignacaoSwimlanes(pd);
		for (Designacao d : listaDesignacaoRaia) {
			d.setAtor(null);
			d.setLotaAtor(null);
			d.setExpressao(null);
			d.setTipoResponsavel(null);
		}
		listaDesignacaoTarefa = getDesignacaoTarefas(pd);
		for (Designacao d : listaDesignacaoTarefa) {
			d.setAtor(null);
			d.setLotaAtor(null);
			d.setExpressao(null);
			d.setTipoResponsavel(null);
		}

		if (pd != null) {
			for (Object o : pd.getTaskMgmtDefinition().getTasks().values()) {
				Task t = (Task) o;

				WfConfiguracao cfg = prepararConfiguracao();

				if (t.getSwimlane() != null) { // se task está em uma raia
					listaDesignacaoRaia = processarRaia(t.getSwimlane(), cfg,
							horaDoDB, raiasGravadas);
				} else {
					listaDesignacaoTarefa = processarTarefa(t, cfg, horaDoDB);
				}

			}

			limparCache();
		}

		// this.procedimentoGravado = procedimento;
		return "SUCESS";
	}

	/**
	 * Lê as definições gravadas no banco e exibe na página.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String aPesquisarDesignacao() throws Exception {
		assertAcesso("DESIGNAR:Designar tarefas");

		selecionarApenasUmOrgao();

		limparCache();
		pd = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getGraphSession().findLatestProcessDefinition(procedimento);

		if (pd != null) {
			listaDesignacaoRaia.addAll(getDesignacaoSwimlanes(pd));
			listaDesignacaoTarefa.addAll(getDesignacaoTarefas(pd));
		}

		return "SUCESS";

	}

	/**
	 * Este código foi criadoporque a caixa de seleção na linha 82 do arquivo
	 * pesquisarDesignacao.jsp está considerando múltiplas seleções embora
	 * esteja definido multiple="false". Segundo o site
	 * http://jira.opensymphony.com/browse/WW-1188 isso é um bug.
	 * */
	private void selecionarApenasUmOrgao() {
		if (orgao != null) {
			String[] lista = orgao.split(",");
			if (lista.length > 0) {
				orgao = lista[lista.length - 1].trim();
			}
		}
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
	private DpPessoa extrairAtor(long id) {
		String keyMatriculaId = "matricula_" + id + "_pessoaSel.id";
		String keyMatriculaSigla = "matricula_" + id + "_pessoaSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		Map parametros = this.getRequest().getParameterMap();
		DpPessoa ator = null;

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
	 * Informa se a raia já foi exibida na página. Uilizada pela página
	 * pesquisarDesignação.jsp.
	 * 
	 * @param nome
	 * @return
	 */
	public boolean isRaiaProcessada(String nome) {
		if (raiaProcessada.contains(nome)) {
			return true;
		} else {
			raiaProcessada.add(nome);
		}
		return false;
	}

	/**
	 * Como na página pesquisarDesignação.jsp os componentes de seleção das
	 * expressões são dinâmicas, é necessária a extração dos dados diretamente
	 * dos parâmetros do request. O prefixo "tipoResponsavel_" é definido na
	 * página pesquisarDesignacao.jsp
	 * 
	 * @param id
	 *            da tarefa
	 * @return Um objeto String com a expressão que equivale ao item
	 *         selecionado.
	 */
	private String extrairExpressao(long id) {

		String keyExpressao = "tipoResponsavel_" + id;
		String expressaoId = "expressao_" + id;
		Map parametros = this.getRequest().getParameterMap();
		String expressao = null;

		if (!this.getRequest().getParameterMap().containsKey(keyExpressao))
			return null;

		String responsavel = ((String[]) parametros.get(keyExpressao))[0];
		if (responsavel == null || responsavel.equals(""))
			return null;

		int idResp = new Integer(responsavel).intValue();

		if (idResp != TIPO_RESP_LOTA_SUP && idResp != TIPO_RESP_SUP_HIER
				&& idResp != TIPO_RESP_EXPRESSAO)
			return null;

		for (TipoResponsavel tr : listaTipoResponsavel) {
			if (tr.getId() == idResp) {
				expressao = tr.getValor();
				if (expressao == null || expressao.length() == 0) {
					expressao = ((String[]) parametros.get(expressaoId))[0];
				}
			}
		}
		return expressao;
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
	private DpLotacao extrairLotaAtor(long id) {
		String keyLotacaoId = "lotacao_" + id + "_lotacaoSel.id";
		String keyLotacaoSigla = "lotacao_" + id + "_lotacaoSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		Map parametros = this.getRequest().getParameterMap();
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
	 * Busca um configuração do tipo TIPO_CONFIG_DESIGNAR_TAREFA no banco de
	 * dados, caso exista.
	 * 
	 * @param nome
	 *            - Nome da raia ou tarefa.
	 * @param isRaia
	 *            - Determina se o parâmetro anterior é uma raia ou tarefa.
	 * @return
	 * @throws Exception
	 */
	private WfConfiguracao getConfiguracaoExistente(String nome, boolean isRaia)
			throws Exception {
		WfConfiguracao fltConfigExistente = new WfConfiguracao();

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA,
				CpTipoConfiguracao.class, false);
		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(orgao);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) WfDao.getInstance()
				.consultarPorSigla(fltOrgao);

		fltConfigExistente.setCpTipoConfiguracao(tipoConfig);
		fltConfigExistente.setOrgaoUsuario(orgaoUsuario);
		fltConfigExistente.setProcedimento(procedimento);

		if (isRaia) {
			fltConfigExistente.setRaia(nome);
		} else {
			fltConfigExistente.setTarefa(nome);
		}

		CpConfiguracao cfgExistente = Wf.getInstance().getConf()
				.buscaConfiguracao(fltConfigExistente, new int[] { 0 }, null);

		return (WfConfiguracao) cfgExistente;
	}

	/**
	 * Monta a lista de designações que se referem às raias.
	 * 
	 * @param pd
	 *            - Objeto ProcessDefinition
	 * @return - Lista de designações
	 * @throws Exception
	 */
	private List<Designacao> getDesignacaoSwimlanes(ProcessDefinition pd)
			throws Exception {

		WfConfiguracao cfgFiltro = new WfConfiguracao();

		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(orgao);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) WfDao.getInstance()
				.consultarPorSigla(fltOrgao);

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA,
				CpTipoConfiguracao.class, false);

		cfgFiltro.setCpTipoConfiguracao(tipoConfig);
		cfgFiltro.setOrgaoUsuario(orgaoUsuario);
		cfgFiltro.setProcedimento(procedimento);

		List<Designacao> resultado = new ArrayList<Designacao>();

		for (Object s : pd.getTaskMgmtDefinition().getSwimlanes().values()) {
			Swimlane raia = (Swimlane) s;

			for (Object t : raia.getTasks()) {
				Task tarefa = (Task) t;
				Designacao d = new Designacao();
				d.setProcedimento(procedimento);
				d.setId(tarefa.getId());
				d.setNomeDoNo(tarefa.getTaskNode() == null ? "" : tarefa
						.getTaskNode().getName());
				d.setTarefa(tarefa.getName());
				d.setRaia(raia.getName());
				cfgFiltro.setRaia(raia.getName());

				WfConfiguracao cfg = (WfConfiguracao) Wf.getInstance()
						.getConf()
						.buscaConfiguracao(cfgFiltro, new int[] { 0 }, null);

				if (cfg != null) {
					d.setAtor(cfg.getAtor());
					d.setLotaAtor(cfg.getLotaAtor());

					if (cfg.getExpressao() != null) {
						d.setExpressao(cfg.getExpressao());
						for (TipoResponsavel tr : listaTipoResponsavel) {
							if (tr.getValor().equals(d.getExpressao())) {
								d.setTipoResponsavel(tr.getId());
							}
						}
						if (d.getTipoResponsavel() == null) {
							d.setTipoResponsavel(TIPO_RESP_EXPRESSAO);
							d.setExpressao(cfg.getExpressao());
						}
					}
				}

				if (d.getAtor() != null) {
					d.setTipoResponsavel(TIPO_RESP_MATRICULA);
				}
				if (d.getLotaAtor() != null) {
					d.setTipoResponsavel(TIPO_RESP_LOTACAO);
				}

				resultado.add(d);
			}

		}

		return resultado;
	}

	/**
	 * Monta a lista de designações que se referem às tarefas.
	 * 
	 * @param pd
	 *            - Objeto ProcessDefinition
	 * @return - Lista de designações
	 * @throws Exception
	 */
	private List<Designacao> getDesignacaoTarefas(ProcessDefinition pd)
			throws Exception {

		WfConfiguracao cfgFiltro = new WfConfiguracao();

		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(orgao);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) WfDao.getInstance()
				.consultarPorSigla(fltOrgao);

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA,
				CpTipoConfiguracao.class, false);

		cfgFiltro.setCpTipoConfiguracao(tipoConfig);
		cfgFiltro.setOrgaoUsuario(orgaoUsuario);
		cfgFiltro.setProcedimento(procedimento);

		List<Designacao> resultado = new ArrayList<Designacao>();
		for (Object o : pd.getTaskMgmtDefinition().getTasks().values()) {
			Task t = (Task) o;
			if (t.getSwimlane() == null) {
				Designacao d = new Designacao();
				d.setProcedimento(procedimento);
				d.setId(t.getId());
				d.setNomeDoNo(t.getTaskNode() == null ? "" : t.getTaskNode()
						.getName());
				d.setTarefa(t.getName());
				cfgFiltro.setTarefa(d.getTarefa());

				WfConfiguracao cfg = (WfConfiguracao) Wf.getInstance()
						.getConf()
						.buscaConfiguracao(cfgFiltro, new int[] { 0 }, null);

				if (cfg != null) {
					d.setAtor(cfg.getAtor());
					d.setLotaAtor(cfg.getLotaAtor());

					if (cfg.getExpressao() != null) {
						d.setExpressao(cfg.getExpressao());
						for (TipoResponsavel tr : listaTipoResponsavel) {
							if (tr.getValor().equals(d.getExpressao())) {
								d.setTipoResponsavel(tr.getId());
							}
						}
						if (d.getTipoResponsavel() == null) {
							d.setTipoResponsavel(TIPO_RESP_EXPRESSAO);
							d.setExpressao(cfg.getExpressao());
						}
					}
				}
				if (d.getAtor() != null) {
					d.setTipoResponsavel(TIPO_RESP_MATRICULA);
				}
				if (d.getLotaAtor() != null) {
					d.setTipoResponsavel(TIPO_RESP_LOTACAO);
				}

				resultado.add(d);
			}
		}

		return resultado;

	}

	/**
	 * Retorna a lista de designações atribuídas às raias. Este método é usado
	 * pela página pesquisarDesignação.jsp
	 * 
	 * @return Lista de designações
	 */
	public List<Designacao> getListaDesignacaoRaia() {
		return listaDesignacaoRaia;
	}

	/**
	 * Retorna a lista de designações atribuídas às tarefas. Este método é usado
	 * pela página pesquisarDesignação.jsp
	 * 
	 * @return Lista de designações
	 */
	public List<Designacao> getListaDesignacaoTarefa() {
		return listaDesignacaoTarefa;
	}

	/**
	 * Retorna a lista de órgãos que podem ter designações definidas. Este
	 * método é usado pela página pesquisarDesignação.jsp
	 * 
	 * @return Lista de órgãos
	 */
	public List<CpOrgaoUsuario> getListaOrgao() {
		return dao().listarOrgaosUsuarios();
	}

	/**
	 * Retorna a lista de procedimentos que podem ter designações definidas.
	 * Este método é usado pela página pesquisarDesignação.jsp
	 * 
	 * @return Lista de definições de processo
	 */
	public List<ProcessDefinition> getListaProcedimento() {
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

	/**
	 * Retorna a lista de tipos de responsáveis que podem ter designações
	 * definidas. Este método é usado pela página pesquisarDesignação.jsp
	 * 
	 * @return Lista de designações
	 */
	public List<TipoResponsavel> getListaTipoResponsavel() {
		return listaTipoResponsavel;
	}

	/**
	 * Retorna o órgão selecionado.
	 * 
	 * @return Nome do órgão selecionado
	 */
	public String getOrgao() {
		return orgao;
	}

	/**
	 * Retorna o procedimento selecionado.
	 * 
	 * @return Nome do órgão selecionado
	 */
	public String getProcedimento() {
		return procedimento;
	}

	/**
	 * Grava a nova configuração no banco de dados.
	 * 
	 * @param cfg
	 *            - Configuração a ser gravada
	 * @throws Exception
	 */
	private void gravarNovaConfig(WfConfiguracao cfg) throws Exception {
		WfDao.getInstance().gravarComHistorico(cfg, getIdentidadeCadastrante());
	}

	/**
	 * Desativa uma configuração existente.
	 * 
	 * @param cfgExistente
	 *            - objecto da configuração existente
	 * @param dataFim
	 *            - data em que a configuração será desativada
	 * @throws AplicacaoException
	 */
	private void invalidarConfiguracao(WfConfiguracao cfgExistente, Date dataFim)
			throws AplicacaoException {
		if (cfgExistente != null && cfgExistente.getHisDtFim() == null) {
			cfgExistente.setHisDtFim(dataFim);
			WfDao.getInstance().gravarComHistorico(cfgExistente,
					getIdentidadeCadastrante());
		}
	}

	/**
	 * Limpa o cache do hibernate. Como as configurações são armazenadas em
	 * cache por serem pouco modificadas, faz-se necessário limpar o cache
	 * quando as configurações são alteradas ou incluídas, senão as
	 * configurações somente serão válidas quando o session factory for
	 * reinicializado.
	 * 
	 * @throws Exception
	 */
	public void limparCache() throws Exception {

		SessionFactory sfWfDao = WfDao.getInstance().getSessao()
				.getSessionFactory();
		SessionFactory sfCpDao = CpDao.getInstance().getSessao()
				.getSessionFactory();

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA,
				CpTipoConfiguracao.class, false);

		Wf.getInstance().getConf().limparCacheSeNecessario();

		sfWfDao.evict(CpConfiguracao.class);
		sfWfDao.evict(WfConfiguracao.class);
		sfCpDao.evict(DpLotacao.class);

		sfWfDao.evictQueries(CpDao.CACHE_QUERY_CONFIGURACAO);

		return;

	}

	/**
	 * Prepara um objeto para receber uma nova configuração.
	 * 
	 * @return Objeto apto a receber uma configuração.
	 */
	private WfConfiguracao prepararConfiguracao() {
		WfConfiguracao cfg = new WfConfiguracao();
		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA,
				CpTipoConfiguracao.class, false);
		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(orgao);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) WfDao.getInstance()
				.consultarPorSigla(fltOrgao);

		cfg.setCpTipoConfiguracao(tipoConfig);
		cfg.setOrgaoUsuario(orgaoUsuario);
		cfg.setProcedimento(procedimento);
		return cfg;
	}

	/**
	 * Processa as designações definidas para raia extraindo as informações do
	 * request.
	 * 
	 * @param raia
	 *            - Raia a ser processada
	 * @param cfg
	 *            - Configuração pré-processada que receberá as informações de
	 *            designação.
	 * @param horaDoBD
	 *            - Hora que será utilizada em todas as operações do banco de
	 *            dados fazendo com que todas as alterações tenham o mesmo
	 *            horário.
	 * @param raiasGravadas
	 * @return
	 * @throws Exception
	 */
	private List<Designacao> processarRaia(Swimlane raia, WfConfiguracao cfg,
			Date horaDoBD, Set<Long> raiasGravadas) throws Exception {

		if (!raiasGravadas.contains(raia.getId())) { // se a swimlane não foi
			// gravada ainda

			for (Object o : raia.getTasks()) {
				Task tarefaRaia = (Task) o;

				DpLotacao lotaAtor = null;
				DpPessoa ator = null;
				String expressao = null;

				lotaAtor = extrairLotaAtor(tarefaRaia.getId());
				ator = extrairAtor(tarefaRaia.getId());
				expressao = extrairExpressao(tarefaRaia.getId());

				if (ator != null || lotaAtor != null || expressao != null) {// se
					// configuração
					// definida
					cfg.setAtor(ator);
					cfg.setLotaAtor(lotaAtor);
					cfg.setExpressao(expressao);

					cfg.setRaia(raia.getName());
					cfg.setHisDtIni(horaDoBD);

					WfConfiguracao cfgExistente = getConfiguracaoExistente(
							raia.getName(), true);
					invalidarConfiguracao(cfgExistente, horaDoBD);

					gravarNovaConfig(cfg);

					// atualiza os dados da visão
					for (Designacao d : listaDesignacaoRaia) {
						if (d.getRaia().equals(
								tarefaRaia.getSwimlane().getName())) {
							d.setAtor(null);
							d.setLotaAtor(null);
							d.setExpressao(null);
							if (ator != null) {
								d.setAtor(ator);
								d.setTipoResponsavel(TIPO_RESP_MATRICULA);
							}

							if (lotaAtor != null) {
								d.setLotaAtor(lotaAtor);
								d.setTipoResponsavel(TIPO_RESP_LOTACAO);
							}

							if (expressao != null) {
								d.setExpressao(expressao);
								for (TipoResponsavel tr : listaTipoResponsavel) {
									if (tr.getValor().equals(expressao)) {
										d.setTipoResponsavel(tr.getId());
									}
								}
							}
						}
					}
					break;

				} else { // configuracao indefinida
					WfConfiguracao cfgExistente = getConfiguracaoExistente(
							raia.getName(), true);
					invalidarConfiguracao(cfgExistente, horaDoBD);
				}

			}

			raiasGravadas.add(raia.getId());

		}

		return listaDesignacaoRaia;
	}

	/**
	 * Processa as designações definidas para a tarefa extraindo as informações
	 * do request.
	 * 
	 * @param t
	 *            - Tarefa a ser processada
	 * @param cfg
	 *            - Configuração pré-processada que receberá as informações de
	 *            designação.
	 * @param horaDoBD
	 *            - Hora que será utilizada em todas as operações do banco de
	 *            dados fazendo com que todas as alterações tenham o mesmo
	 *            horário.
	 * @return
	 * @throws Exception
	 */
	private List<Designacao> processarTarefa(Task t, WfConfiguracao cfg,
			Date horaDoBD) throws Exception {
		DpLotacao lotaAtor = null;
		DpPessoa ator = null;
		String expressao = null;

		lotaAtor = extrairLotaAtor(t.getId());
		ator = extrairAtor(t.getId());
		expressao = extrairExpressao(t.getId());

		if (ator != null || lotaAtor != null || expressao != null) {// se
			// configuração
			// definida
			cfg.setAtor(ator);
			cfg.setLotaAtor(lotaAtor);
			cfg.setExpressao(expressao);

			cfg.setTarefa(t.getName());
			cfg.setHisDtIni(horaDoBD);

			WfConfiguracao cfgExistente = getConfiguracaoExistente(t.getName(),
					false);
			invalidarConfiguracao(cfgExistente, horaDoBD);

			gravarNovaConfig(cfg);
			// atualiza os dados da visão
			for (Designacao d : listaDesignacaoTarefa) {
				if (d.getTarefa().equals(t.getName())) {
					d.setAtor(null);
					d.setLotaAtor(null);
					d.setExpressao(null);
					if (ator != null) {
						d.setAtor(ator);
						d.setTipoResponsavel(TIPO_RESP_MATRICULA);
					}

					if (lotaAtor != null) {
						d.setLotaAtor(lotaAtor);
						d.setTipoResponsavel(TIPO_RESP_LOTACAO);
					}

					if (expressao != null) {
						d.setExpressao(expressao);
						for (TipoResponsavel tr : listaTipoResponsavel) {
							if (tr.getValor().equals(expressao)) {
								d.setTipoResponsavel(tr.getId());
							}
						}
					}

				}
			}

		} else { // configuracao indefinida
			WfConfiguracao cfgExistente = getConfiguracaoExistente(t.getName(),
					false);
			invalidarConfiguracao(cfgExistente, horaDoBD);
		}
		return listaDesignacaoTarefa;

	}

	/**
	 * Define a lista de designações.
	 * 
	 * @param listaDesignacao
	 */
	public void setListaDesignacao(List<Designacao> listaDesignacao) {
		this.listaDesignacao = listaDesignacao;
	}

	/**
	 * Define a lista de designações para um determinada raia.
	 * 
	 * @param listaDesignacaoRaia
	 */
	public void setListaDesignacaoRaia(List<Designacao> listaDesignacaoRaia) {
		this.listaDesignacaoRaia = listaDesignacaoRaia;
	}

	/**
	 * Define a lista de designações para um determinada tarefa.
	 * 
	 * @param listaDesignacaoTarefa
	 */
	public void setListaDesignacaoTarefa(List<Designacao> listaDesignacaoTarefa) {
		this.listaDesignacaoTarefa = listaDesignacaoTarefa;
	}

	/**
	 * Define o órgão.
	 * 
	 * @param orgao
	 */
	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	/**
	 * Define o procedimento.
	 * 
	 * @param procedimento
	 */
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	/**
	 * Classe que representa o tipo do responsável designado.
	 * 
	 * @author kpf
	 * 
	 */
	class TipoResponsavel {
		int id;
		/**
		 * Texto amigável que representa o tipo de responsável.
		 */
		String texto;

		/**
		 * Texto da expressão ou identificador do tipo de responsável.
		 */
		String valor;

		/**
		 * Construtor da classe TipoResponsável.
		 * 
		 * @param id
		 * @param texto
		 * @param valor
		 */
		public TipoResponsavel(int id, String texto, String valor) {
			this.setId(id);
			this.setTexto(texto);
			this.setValor(valor);
		}

		/**
		 * Retorna o id do tipo de responsável.
		 * 
		 * @return
		 */
		public int getId() {
			return id;
		}

		/**
		 * Retorna o texto amigável do Tipo de responsável
		 * 
		 * @return
		 */
		public String getTexto() {
			return texto;
		}

		/**
		 * Retorna o valor do tipo de responsável, por exemplo, a expressão
		 * associada ao tipo de responsável.
		 * 
		 * @return
		 */
		public String getValor() {
			return valor;
		}

		/**
		 * Define o id do tipo de responsável.
		 * 
		 * @param id
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Define o texto amigável do tipo de responsável.
		 * 
		 * @param texto
		 */
		public void setTexto(String texto) {
			this.texto = texto;
		}

		/**
		 * Define o valor (por exemplo, a expressão) do tipo de responsável.
		 * 
		 * @param valor
		 */
		public void setValor(String valor) {
			this.valor = valor;
		}

		/**
		 * Retorna o texto amigável do tipo de responsável.
		 */
		public String toString() {
			return this.getTexto();
		}
	}
}
