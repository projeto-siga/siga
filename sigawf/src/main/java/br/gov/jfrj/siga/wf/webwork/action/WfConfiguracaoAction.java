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
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.SessionFactory;
import org.jbpm.graph.def.ProcessDefinition;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.wf.Permissao;
import br.gov.jfrj.siga.wf.WfConfiguracao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Classe que representa as ações de configuração do workflow.
 * 
 * @author kpf
 * 
 */
public class WfConfiguracaoAction extends WfSigaActionSupport {

	private static int TIPO_RESP_INDEFINIDO = 0;
	private static int TIPO_RESP_MATRICULA = 1;
	private static int TIPO_RESP_LOTACAO = 2;

	private List<Permissao> listaPermissao = new ArrayList<Permissao>();
	private List<TipoResponsavel> listaTipoResponsavel = new ArrayList<TipoResponsavel>();

	private String orgao;
	private String procedimento;

	private ProcessDefinition pd;

	/**
	 * Inicializa os tipos de responsáveis e suas respectivas expressões, quando
	 * houver.
	 */
	public WfConfiguracaoAction() {

		TipoResponsavel tpIndefinido = new TipoResponsavel(
				TIPO_RESP_INDEFINIDO, "[Indefinido]", "");
		TipoResponsavel tpMatricula = new TipoResponsavel(TIPO_RESP_MATRICULA,
				"Matrícula", "matricula");
		TipoResponsavel tpLotacao = new TipoResponsavel(TIPO_RESP_LOTACAO,
				"Lotação", "lotacao");

		listaTipoResponsavel.add(tpIndefinido);
		listaTipoResponsavel.add(tpMatricula);
		listaTipoResponsavel.add(tpLotacao);
	}

	/**
	 * Grava a configuração da permissão. Primeiro, processa-se as alteraões nas
	 * permissões existentes e depois processa-se as novas permissões. As
	 * permissões indefinidas são excluídas da lista de permissões.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String aGravarConfiguracao() throws Exception {

		pd = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getGraphSession().findLatestProcessDefinition(procedimento);
		Date horaDoDB = dao().consultarDataEHoraDoServidor();

		if (pd != null) {

			// processa permissões existentes
			listaPermissao = getPermissoes(pd);
			for (Permissao perm : listaPermissao) {
				WfConfiguracao cfg = prepararConfiguracao();

				processarPermissao(perm, cfg, horaDoDB);

			}

			// processa nova permissão
			WfConfiguracao cfg = prepararConfiguracao();
			Permissao novaPermissao = new Permissao();
			novaPermissao.setId(this.getIdNovaPermissao());
			novaPermissao.setProcedimento(procedimento);

			processarPermissao(novaPermissao, cfg, horaDoDB);

			if (novaPermissao.getPessoa() != null
					|| novaPermissao.getLotacao() != null) {
				listaPermissao.add(novaPermissao);
			}

			// remove permissões inválidas
			ArrayList<Permissao> listaAux = new ArrayList<Permissao>();
			for (Permissao perm : listaPermissao) {
				if (perm.getPessoa() != null || perm.getLotacao() != null) {
					listaAux.add(perm);
				}
			}

			listaPermissao = listaAux;

			limparCache();
		}

		return "SUCESS";
	}

	/**
	 * Seleciona um procedimento que terá suas permissões configuradas.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String aConfigurar() throws Exception {
		assertAcesso("CONFIGURAR:Configurar iniciadores");

		limparCache();
		pd = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getGraphSession().findLatestProcessDefinition(procedimento);

		if (pd != null) {
			listaPermissao.addAll(getPermissoes(pd));
		}

		return "SUCESS";

	}

	/**
	 * Este método pega os parâmetros do request, identifica qual foi a
	 * matrícula da pessoa selecionada (selecao.tag) e busca o objeto DpPessoa
	 * no banco de dados.
	 * 
	 * @param id
	 * @return Retorna um objeto DpPessoa baseado no id da permissão.
	 */
	private DpPessoa extrairPessoa(long id) {
		String keyMatriculaId = "matricula_" + id + "_pessoaSel.id";
		String keyMatriculaSigla = "matricula_" + id + "_pessoaSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		Map parametros = this.getRequest().getParameterMap();
		DpPessoa pessoa = null;

		if (parametros.containsKey(keyMatriculaId)
				&& parametros.containsKey(keyMatriculaSigla)) {
			responsavelId = ((String[]) parametros.get(keyMatriculaId))[0];
			responsavelSigla = ((String[]) parametros.get(keyMatriculaSigla))[0];
			if (!responsavelId.equals("") && !responsavelSigla.equals("")) {
				pessoa = daoPes(new Long(responsavelId));
			}
		}

		return pessoa;
	}

	/**
	 * Este método pega os parâmetros do request, identifica qual foi a sigla da
	 * lotação selecionada (selecao.tag) e busca o objeto DpLotacao no banco de
	 * dados.
	 * 
	 * @param id
	 * @return Retorna um objeto DpLotacao baseado no id da permissão.
	 */
	private DpLotacao extrairLotacao(long id) {
		String keyLotacaoId = "lotacao_" + id + "_lotacaoSel.id";
		String keyLotacaoSigla = "lotacao_" + id + "_lotacaoSel.sigla";
		String responsavelId = null;
		String responsavelSigla = null;
		Map parametros = this.getRequest().getParameterMap();
		DpLotacao lotacao = null;

		if (parametros.containsKey(keyLotacaoId)
				&& parametros.containsKey(keyLotacaoSigla)) {
			responsavelId = ((String[]) parametros.get(keyLotacaoId))[0];
			responsavelSigla = ((String[]) parametros.get(keyLotacaoSigla))[0];
			if (!responsavelId.equals("") && !responsavelSigla.equals("")) {
				lotacao = daoLot(new Long(responsavelId));
			}
		}
		return lotacao;
	}

	/**
	 * Retorna a lista de configurações definidas para uma permissão.
	 * 
	 * @param perm
	 *            -
	 * @return Lista de permissões já gravadas no banco de dados.
	 * @throws Exception
	 */
	private List<WfConfiguracao> getConfiguracaoExistente(Permissao perm)
			throws Exception {
		WfConfiguracao fltConfigExistente = new WfConfiguracao();

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
				CpTipoConfiguracao.class, false);
		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(orgao);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) WfDao.getInstance()
				.consultarPorSigla(fltOrgao);

		fltConfigExistente.setCpTipoConfiguracao(tipoConfig);
		fltConfigExistente.setOrgaoUsuario(orgaoUsuario);
		fltConfigExistente.setProcedimento(procedimento);
		fltConfigExistente.setDpPessoa(perm.getPessoa());
		fltConfigExistente.setLotacao(perm.getLotacao());

		List<WfConfiguracao> cfgExistente = WfDao.getInstance().consultar(
				fltConfigExistente);
		List<WfConfiguracao> resultado = new ArrayList<WfConfiguracao>();
		// Melhorar isso: o filtro por pessoa ou lotação não está funcionando
		for (WfConfiguracao c : cfgExistente) {
			if ((c.getDpPessoa() != null || c.getLotacao() != null)
					&& c.getHisDtFim() == null
					&& c.getIdConfiguracao().equals(perm.getId())) {
				resultado.add(c);
			}
		}

		return resultado;
	}

	/**
	 * Retorna a lista de permissões definidas para uma definição de
	 * procedimento.
	 * 
	 * @param pd
	 *            - Definição de processo
	 * @return - Lista de permissões
	 * @throws Exception
	 */
	private List<Permissao> getPermissoes(ProcessDefinition pd)
			throws Exception {

		CpOrgaoUsuarioDaoFiltro fltOrgao = new CpOrgaoUsuarioDaoFiltro();
		fltOrgao.setSigla(orgao);
		CpOrgaoUsuario orgaoUsuario = (CpOrgaoUsuario) WfDao.getInstance()
				.consultarPorSigla(fltOrgao);

		List<Permissao> resultado = new ArrayList<Permissao>();

		TreeSet<CpConfiguracao> cfg = Wf.getInstance().getConf()
				.getListaPorTipo(
						CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO);
		for (CpConfiguracao c : cfg) {

			if (c instanceof WfConfiguracao) {
				WfConfiguracao wfC = (WfConfiguracao) c;
				if (wfC.getProcedimento() != null
						&& wfC.getProcedimento().equals(procedimento)
						&& wfC.getHisDtFim() == null) {

					Permissao perm = new Permissao();
					perm.setProcedimento(procedimento);
					perm.setId(c.getIdConfiguracao());

					perm.setPessoa(c.getDpPessoa());
					perm.setLotacao(c.getLotacao());

					if (perm.getPessoa() != null) {
						perm.setTipoResponsavel(TIPO_RESP_MATRICULA);
					}
					if (perm.getLotacao() != null) {
						perm.setTipoResponsavel(TIPO_RESP_LOTACAO);
					}

					resultado.add(perm);
				}
			}
		}

		return resultado;

	}

	/**
	 * Retorna a lista de permissões. Este método é utilizado para montar a
	 * view.
	 * 
	 * @return
	 */
	public List<Permissao> getListaPermissao() {
		return listaPermissao;
	}

	/**
	 * Retorna a lista de órgãos. Este método é utilizado para montar a view.
	 * 
	 * @return
	 */
	public List<CpOrgaoUsuario> getListaOrgao() {
		return dao().listarOrgaosUsuarios();
	}

	/**
	 * Retorna a lista dos tipos de responsáveis. Este método é utilizado para
	 * montar a view.
	 * 
	 * @return
	 */
	public List<TipoResponsavel> getListaTipoResponsavel() {
		return listaTipoResponsavel;
	}

	/**
	 * Retorna o órgão selecionado. Este método é utilizado para montar a view.
	 * 
	 * @return
	 */
	public String getOrgao() {
		return orgao;
	}

	/**
	 * Retorna o procedimento selecionado. Este método é utilizado para montar a
	 * view.
	 * 
	 * @return
	 */
	public String getProcedimento() {
		return procedimento;
	}

	/**
	 * Método utilizado para gravar uma nova configuração. ATENÇÃO: ESTE MÉTODO
	 * PROVAVELMENTE PODE SER ELIMINADO EM UM REFACTORING.
	 * 
	 * @param cfg
	 * @throws Exception
	 */
	private void gravarNovaConfig(WfConfiguracao cfg) throws Exception {
		WfDao.getInstance().gravarComHistorico(cfg, getIdentidadeCadastrante());
	}

	/**
	 * Torna uma configuração existente inválida. A invalidação da configuração
	 * normalmente ocorre ao se criar uma nova configuração. A configuração
	 * antiga torna-se inválida mas continua sendo mantida no banco de dados
	 * para fins de histórico.
	 * 
	 * @param cfgExistente
	 *            - Configuração a ser invalidada
	 * @param dataFim
	 *            - Data que define o fim da validade da configuração, ou seja,
	 *            data de invalidação.
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
	 * Limpa o cache do hibernate. Como as configurações são mantidas em cache
	 * por motivo de performance, as alterações precisam ser atualizadas para
	 * que possam valer imediatamente.
	 * 
	 * @throws Exception
	 */
	public void limparCache() throws Exception {

		SessionFactory sfWfDao = WfDao.getInstance().getSessao()
				.getSessionFactory();
		SessionFactory sfCpDao = CpDao.getInstance().getSessao()
				.getSessionFactory();

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
				CpTipoConfiguracao.class, false);

		Wf.getInstance().getConf().limparCacheSeNecessario();

		sfWfDao.evict(CpConfiguracao.class);
		sfWfDao.evict(WfConfiguracao.class);
		sfCpDao.evict(DpLotacao.class);

		sfWfDao.evictQueries(CpDao.CACHE_QUERY_CONFIGURACAO);

		return;

	}

	/**
	 * Inicia um objeto WfConfiguracao de modo que possa receber as
	 * configurações definidas pelo usuário.
	 * 
	 * @return - Configuração pronta para receber as configurações.
	 */
	private WfConfiguracao prepararConfiguracao() {
		WfConfiguracao cfg = new WfConfiguracao();
		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
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
	 * Processa as permissões definidas pelo usuário. São extraídos os dados do
	 * request, definida a data de início da configuração, definidas as
	 * permissões, definida a invalidação da configuração antiga e atualizada a
	 * visão do usuário.
	 * 
	 * @param permissao
	 *            - Permissão a ser processada
	 * @param cfg
	 *            - Configuração preparada para receber as novas configurações
	 * @param horaDoBD
	 *            - Hora do banco de dados. A data/hora de início de vigência
	 *            deve ser a mesma da invalidação da configuração anterior.
	 * @throws Exception
	 */
	private void processarPermissao(Permissao permissao, WfConfiguracao cfg,
			Date horaDoBD) throws Exception {
		DpLotacao lotacao = null;
		DpPessoa pessoa = null;

		lotacao = extrairLotacao(permissao.getId());
		pessoa = extrairPessoa(permissao.getId());

		if (pessoa != null || lotacao != null) {// se
			// configuração
			// definida
			cfg.setDpPessoa(pessoa);
			cfg.setLotacao(lotacao);

			cfg.setHisDtIni(horaDoBD);

			for (WfConfiguracao cfgExistente : getConfiguracaoExistente(permissao)) {
				invalidarConfiguracao(cfgExistente, horaDoBD);
			}

			permissao.setPessoa(pessoa);
			permissao.setLotacao(lotacao);

			CpSituacaoConfiguracao sit = new CpSituacaoConfiguracao();
			sit.setIdSitConfiguracao(CpSituacaoConfiguracao.SITUACAO_PODE);
			cfg.setCpSituacaoConfiguracao(sit);
			gravarNovaConfig(cfg);
			permissao.setId(cfg.getIdConfiguracao()); // deixa de usar a id
			// temporária

			// atualiza os dados da visão
			if (pessoa != null) {
				permissao.setPessoa(pessoa);
				permissao.setTipoResponsavel(TIPO_RESP_MATRICULA);
			}

			if (lotacao != null) {
				permissao.setLotacao(lotacao);
				permissao.setTipoResponsavel(TIPO_RESP_LOTACAO);
			}

		} else {
			for (WfConfiguracao cfgExistente : getConfiguracaoExistente(permissao)) {
				invalidarConfiguracao(cfgExistente, horaDoBD);
			}

			permissao.setPessoa(pessoa);
			permissao.setLotacao(lotacao);
		}

	}

	/**
	 * Define a lista de permissões.
	 * 
	 * @param listaPermissao
	 */
	public void setListaPermissao(List<Permissao> listaPermissao) {
		this.listaPermissao = listaPermissao;
	}

	/**
	 * Define o órgão que terá as permissões configuradas.
	 * 
	 * @param orgao
	 */
	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	/**
	 * Define o procedimento que terá as permissões configuradas.
	 * 
	 * @param procedimento
	 */
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	/**
	 * Retorna um id TEMPORÁRIO para uma nova configuração definida pelo
	 * usuário. Para que não haja conflito de ID's foi escolhido o último valor
	 * do tipo long.
	 * 
	 * @return - ID temporário.
	 */
	public Long getIdNovaPermissao() {
		return Long.MAX_VALUE;
	}

	/**
	 * Classe interna que define o tipo de responsável.
	 * 
	 * @author kpf
	 * 
	 */
	class TipoResponsavel {
		int id;
		String texto;
		String valor;

		/**
		 * Método construtor.
		 * 
		 * @param id
		 *            - Tipo de responsável. Pode ser TIPO_RESP_INDEFINIDO = 0
		 *            (quando for indefinido), TIPO_RESP_MATRICULA = 1 (Quando
		 *            for permissão para uma pessoa) ou TIPO_RESP_LOTACAO = 2
		 *            (quando for permissão para uma lotação);
		 * @param texto
		 *            - Nome amigável do tipo de responsável. Este texto é
		 *            utilizado na view.
		 * @param valor
		 *            - Uma EXPRESSÃO Quando o tipo de responsável for uma
		 *            expressão, "matricula" quando for uma pessoa ou "lotacao"
		 *            quando for uma lotacao.
		 */
		public TipoResponsavel(int id, String texto, String valor) {
			this.setId(id);
			this.setTexto(texto);
			this.setValor(valor);
		}

		/**
		 * Retorna o tipo de responsável.
		 * 
		 * @return
		 */
		public int getId() {
			return id;
		}

		/**
		 * Retorna o nome amigável do tipo de responsável. Utilizado na view.
		 * 
		 * @return
		 */
		public String getTexto() {
			return texto;
		}

		/**
		 * Retorna o valor do responsável. Por exemplo, retorna uma expressão
		 * (por exemplo, previous --> chefe()) quando o responsável for definido
		 * por uma expressão
		 * 
		 * @return
		 */
		public String getValor() {
			return valor;
		}

		/**
		 * Define o id do tipo de responsável. Pode ser TIPO_RESP_INDEFINIDO = 0
		 * (quando for indefinido), TIPO_RESP_MATRICULA = 1 (Quando for
		 * permissão para uma pessoa) ou TIPO_RESP_LOTACAO = 2 (quando for
		 * permissão para uma lotação);
		 * 
		 * @param id
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Define o texto amigável do tipo de responsável. Utilizado na view.
		 * 
		 * @param texto
		 */
		public void setTexto(String texto) {
			this.texto = texto;
		}

		/**
		 * Define o valor do tipo de responsável. Por exemplo, retorna uma
		 * expressão (por exemplo, previous --> chefe()) quando o responsável
		 * for definido por uma expressão
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
