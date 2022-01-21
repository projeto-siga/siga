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

/*
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.vraptor;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpPerfilJEE;
import br.gov.jfrj.siga.cp.CpTipoGrupo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupoEmail;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupoFabrica;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupoFormula;
import br.gov.jfrj.siga.cp.grupo.TipoConfiguracaoGrupoEnum;
import br.gov.jfrj.siga.cp.model.CpGrupoDeEmailSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpGrupoDaoFiltro;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public abstract class GrupoController<T extends CpGrupo> extends
		GiSelecionavelControllerSupport<T, CpGrupoDaoFiltro> {


	/**
	 * @deprecated CDI eyes only
	 */
	public GrupoController() {
		super();
	}

	@Inject
	public GrupoController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3768576909382652437L;

	// Campos da lista de configurações do grupo
	private ArrayList<ConfiguracaoGrupo> configuracoesGrupo;

	private String conteudoConfiguracaoNova;

	private CpTipoGrupo cpTipoGrupo;

	private String dscCpTipoGrupo;
	private String dscGrupo;

	private DpLotacaoSelecao lotacaoGestoraSel;

	// erro
	private Exception exception;
	private CpGrupoDeEmailSelecao grupoPaiSel;
	// Texto endereco email,
	// etc. , conforme
	// codigoTipoConfiguracao.
	// Campos para uma nova configuração par ao grupo
	private String idConfiguracaoNova;
	// Lista de grupos está em itens da superclasse
	// campos do grupo
	private Long idCpGrupo;
	private Long orgaoUsu;
	private String siglaGrupo;
	private CpTipoDeConfiguracao tipoConfiguracao;
	private List<TipoConfiguracaoGrupoEnum> tiposConfiguracaoGrupoParaTipoDeGrupo;
	// Carga inicial
	private List<CpTipoGrupo> tiposDeGrupo;

	@Override
	protected String aBuscar(String sigla, String postback) throws Exception {
		if (postback == null)
			setOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		return super.aBuscar(sigla, postback);
	}

	/*
	 * Prepara a edição do grupo selecionado na lista
	 */
	protected String aEditar(Long idCpGrupo) throws Exception {
		List<String> idConfiguracao = new ArrayList<String>();
		List<String> codigoTipoConfiguracao = new ArrayList<String>();
		List<String> conteudoConfiguracao = new ArrayList<String>();

		Cp.getInstance().getConf().limparCacheSeNecessario();

		Integer t_idTpGrupo = getIdTipoGrupo();
		setCpTipoGrupo(dao().consultar(t_idTpGrupo, CpTipoGrupo.class, false));

		// idCpGrupo vazio indica inclusão
		if (idCpGrupo == null || idCpGrupo.equals("")) {
			dscGrupo = "";
			siglaGrupo = "";
			tiposConfiguracaoGrupoParaTipoDeGrupo = obterTiposConfiguracaoGrupoParaTipoDeGrupo(getCpTipoGrupo());
			configuracoesGrupo = new ArrayList<ConfiguracaoGrupo>();
			return "edita";
		} else {
			setIdCpGrupo(idCpGrupo);
			CpGrupo grp;
			grp = daoGrupo(idCpGrupo);
			if (grp == null) {
				throw new AplicacaoException(
						"Grupo não encontrado para Id do grupo: " + idCpGrupo
								+ ".");
			}
			if (!grp.isAtivo()) {
				throw new AplicacaoException(
						"Grupo antigo não pode ser editado. Favor atualizar a página da lista de grupos antes de selecionar um grupo para edição.");
			}
			dscGrupo = grp.getDscGrupo();
			siglaGrupo = grp.getSiglaGrupo();
			getGrupoPaiSel().buscarPorObjeto(grp.getCpGrupoPai());

			CpTipoGrupo tpGrp = grp.getCpTipoGrupo();

			tiposConfiguracaoGrupoParaTipoDeGrupo = obterTiposConfiguracaoGrupoParaTipoDeGrupo(tpGrp);
			dscCpTipoGrupo = tpGrp.getDscTpGrupo();
			try {
				configuracoesGrupo = Cp.getInstance().getConf()
						.obterCfgGrupo(dao().consultar(grp.getHisIdIni(),CpGrupo.class,false));
				for (ConfiguracaoGrupo t_cfgConfiguracaoGrupo : configuracoesGrupo) {
					CpConfiguracao t_cpcConfiguracaoCorrente = t_cfgConfiguracaoGrupo
							.getCpConfiguracao();
					Long t_lngIdConfiguracao = t_cpcConfiguracaoCorrente
							.getIdConfiguracao();
					idConfiguracao.add(t_lngIdConfiguracao.toString());
					TipoConfiguracaoGrupoEnum t_tpcTipo = t_cfgConfiguracaoGrupo
							.getTipo();
					codigoTipoConfiguracao.add(String.valueOf(t_tpcTipo
							.getCodigo()));
					String t_strConteudoConfiguracaoCorrente = t_cfgConfiguracaoGrupo
							.getConteudoConfiguracao();
					conteudoConfiguracao.add(t_strConteudoConfiguracaoCorrente);
				}
			} catch (Exception e) {
				throw new AplicacaoException(
						MessageFormat.format(
								"Id do grupo: {0} erro ao obter configurações do grupo.",
								idCpGrupo), 0, e);
			}
		}

		result.include("idConfiguracao", idConfiguracao);
		result.include("codigoTipoConfiguracao", codigoTipoConfiguracao);
		result.include("conteudoConfiguracao", conteudoConfiguracao);

		return "edita";
	}

	/**
	 * Exclui o grupo e as configurações A exclusão no caso é lógica: atribui a
	 * data de fim de vigência das configurações do grupo e também a data de fim
	 * do grupo.
	 * 
	 * @throws AplicacaoException
	 */
	protected String aExcluir(Long idCpGrupo) throws Exception {
		try {
			ModeloDao.iniciarTransacao();
			Date dt = dao().consultarDataEHoraDoServidor();
			CpGrupo grp = daoGrupo(idCpGrupo);
			configuracoesGrupo = Cp.getInstance().getConf().obterCfgGrupo(grp);
			for (ConfiguracaoGrupo t_cfgConfiguracaoGrupo : configuracoesGrupo) {
				CpConfiguracao t_cpcConfiguracao = dao().consultar(
						t_cfgConfiguracaoGrupo.getCpConfiguracao().getIdConfiguracao(), CpConfiguracao.class, false);
				t_cpcConfiguracao = dao().carregar(t_cpcConfiguracao);
				dao().gravarComHistorico(t_cpcConfiguracao,
						getIdentidadeCadastrante());
			}			
			grp = dao().carregar(grp);
			grp.setHisDtFim(dt);
			dao().gravarComHistorico(grp, getIdentidadeCadastrante());
			ModeloDao.commitTransacao();
		} catch (Exception e) {
			ModeloDao.rollbackTransacao();
			throw new AplicacaoException("Erro ao excluir grupo de id: "
					+ idCpGrupo + ".", 0, e);
		}
		return "lista";
	}

	protected CpGrupo daoGrupo(Long id) {
		if (id == null) {
			return null;
		} else {
			return dao().consultar(id, CpGrupo.class, false);
		}
	}

	/**
	 * Grava o grupo e as configurações
	 * 
	 * @param idConfiguracao
	 *            TODO
	 * 
	 * @throws AplicacaoException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	protected Long aGravar(Long idCpGrupo, String siglaGrupo, String dscGrupo,
			CpGrupoDeEmailSelecao grupoPaiSel,
			Integer codigoTipoConfiguracaoNova,
			String conteudoConfiguracaoNova, List<String> idConfiguracao,
			List<String> codigoTipoConfiguracao,
			List<String> conteudoConfiguracao) throws Exception {

		if (siglaGrupo == null) {
			throw new AplicacaoException("A sigla do grupo deve ser definida!");
		}

		idConfiguracao = Optional.fromNullable(idConfiguracao).or(
				new ArrayList<String>());

		codigoTipoConfiguracao = Optional.fromNullable(codigoTipoConfiguracao)
				.or(new ArrayList<String>());

		conteudoConfiguracao = Optional.fromNullable(conteudoConfiguracao).or(
				new ArrayList<String>());

		try {
			CpGrupo grp = null;
			CpGrupo grpNovo = null;
			Date dt = dao().consultarDataEHoraDoServidor();
			CpTipoGrupo tpGrp = obterCpTipoGrupoPorId(getIdTipoGrupo());
			if (tpGrp == null) {
				throw new AplicacaoException(
						"Tipo de grupo nulo para Id do grupo: " + idCpGrupo);
			}

			// Substituir isso por uma fábrica
			//
			if (tpGrp.getIdTpGrupo() == CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO)
				grpNovo = new CpGrupoDeEmail();
			if (tpGrp.getIdTpGrupo() == CpTipoGrupo.TIPO_GRUPO_PERFIL_DE_ACESSO)
				grpNovo = new CpPerfil();
			if (tpGrp.getIdTpGrupo() == CpTipoGrupo.TIPO_GRUPO_PERFIL_JEE)
				grpNovo = new CpPerfilJEE();

			if (idCpGrupo == null) {
				grpNovo.setCpTipoGrupo(tpGrp);
				grpNovo.setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());
			} else {
				grp = (CpGrupo) Objeto.getImplementation(daoGrupo(idCpGrupo));
				PropertyUtils.copyProperties(grpNovo, grp);
				grpNovo.setIdGrupo(null);
				grpNovo.setHisIdIni(grp.getHisIdIni());
			}
			grpNovo.setCpGrupoPai(grupoPaiSel != null ? grupoPaiSel.getObjeto()
					: null);
			grpNovo.setDscGrupo(dscGrupo);
			grpNovo.setSiglaGrupo(siglaGrupo);

			dao().iniciarTransacao();
			grp = (CpGrupo) dao().gravarComHistorico(grpNovo, grp, dt,
					getIdentidadeCadastrante());
			CpGrupo grupoInicial = null;
			idCpGrupo = grp.getIdGrupo();

			// Fabrica
			ConfiguracaoGrupoFabrica fabrica = new ConfiguracaoGrupoFabrica();
			// grava uma nova configuração

			TipoConfiguracaoGrupoEnum tpCfgGrpEnum = TipoConfiguracaoGrupoEnum
					.obterPara(tpGrp, codigoTipoConfiguracaoNova);
			if (tpCfgGrpEnum != null) {
				ConfiguracaoGrupo cfgGrp = fabrica.getInstance(tpCfgGrpEnum);
				if (isConfiguracaoAvancada(cfgGrp)
						&& !podeEditarConfiguracoesAvancadas()) {
					throw new AplicacaoException(
							"Você nâo tem permissão para configurar "
									+ tpCfgGrpEnum.getDescricao()
									+ ". Por favor, entre em contato com o suporte técnico para realizar tal configuração.");
				}

				if (conteudoConfiguracaoNova != null) {
					cfgGrp.setConteudoConfiguracao(conteudoConfiguracaoNova);
				}

				if (conteudoConfiguracaoNova == null
						|| cfgGrp.getConteudoConfiguracao() == null
						|| cfgGrp.getConteudoConfiguracao().equals("")) {
					throw new AplicacaoException(
							"Erro ao gravar nova configuração para o grupo: conteúdo do(a) "
									+ tpCfgGrpEnum.getDescricao()
									+ " inexistente! ");
				}
				CpConfiguracao cfg = new CpConfiguracao();
				cfg.setCpTipoConfiguracao(tipoConfiguracao);
				cfg.setHisDtIni(dt);
				cfgGrp.setCpConfiguracao(cfg);
				grupoInicial = dao().consultar(grp.getHisIdIni(),CpGrupo.class,false);
				cfgGrp.setCpGrupo(grupoInicial);
				cfgGrp.atualizarCpConfiguracao();
				dao().gravarComHistorico(cfg, getIdentidadeCadastrante());
			}

			// processa as configurações existentes
			configuracoesGrupo = Cp.getInstance().getConf().obterCfgGrupo(grp);
			for (int i = 0; i < idConfiguracao.size(); i++) {
				Long idCfg = Long.parseLong(idConfiguracao.get(i));
				for (ConfiguracaoGrupo cfgGrpGravada : configuracoesGrupo) {
					Long idCfgGravada = cfgGrpGravada.getCpConfiguracao()
							.getIdConfiguracao();
					if (idCfgGravada.equals(idCfg)) {
						Integer tpCfg = Integer.parseInt(codigoTipoConfiguracao
								.get(i));
						// Remoção de uma configuração gravada antes
						if (tpCfg.equals(-1)) {
							// exclusão remove apenas logicamente, deixa o
							// registro antigo como log
							if (isConfiguracaoAvancada(cfgGrpGravada)
									&& !podeEditarConfiguracoesAvancadas()) {
								throw new AplicacaoException(
										"Você nâo tem permissão para remover "
												+ cfgGrpGravada.getTipo()
														.getDescricao()
												+ ". Por favor, entre em contato com o suporte técnico para realizar tal configuração.");
							}

							CpConfiguracao t_cpcConfiguracao = cfgGrpGravada.getCpConfiguracao();								
							t_cpcConfiguracao = dao().carregar(t_cpcConfiguracao);
							t_cpcConfiguracao.setHisDtFim(dt);
							dao().gravarComHistorico(t_cpcConfiguracao,getIdentidadeCadastrante());
						} else {
							String cfgConteudo = conteudoConfiguracao.get(i);
							// Nato: o ideal seria se pudéssemos utilizar o
							// método "semelhante" para comparar configurações.
							// No entanto, como as configurações anteriores são
							// lidas do "cache-da-aplicação", e não do
							// Hibernate, fica impossível fazer a comparação
							// automaticamente. Por isso, é necessário esse "if"
							// que só grava alterações se não for do mesmo tipo
							// ou não tiver mesmo conteúdo que a gravada
							if (!tpCfg.equals(cfgGrpGravada.getTipo()
									.getCodigo())
									|| !cfgConteudo.equals(cfgGrpGravada
											.getConteudoConfiguracao())) {
								TipoConfiguracaoGrupoEnum tpCfgGrpNova = TipoConfiguracaoGrupoEnum
										.obterPara(tpGrp, tpCfg);
								ConfiguracaoGrupo cfgGrpNova = fabrica
										.getInstance(tpCfgGrpNova);
								if (isConfiguracaoAvancada(cfgGrpNova)
										&& !podeEditarConfiguracoesAvancadas()) {
									throw new AplicacaoException(
											"Você nâo tem permissão para configurar "
													+ tpCfgGrpNova
															.getDescricao()
													+ ". Por favor, entre em contato com o suporte técnico para realizar tal configuração.");
								}
								if (cfgConteudo == null
										|| cfgConteudo.equals("")) {
									throw new AplicacaoException(
											"Erro ao gravar alteração da configuração para o grupo: conteúdo do(a)"
													+ tpCfgGrpEnum
															.getDescricao()
													+ " inexistente!");
								}
								cfgGrpNova.setConteudoConfiguracao(cfgConteudo);
								CpConfiguracao cfgNova = new CpConfiguracao();
								cfgNova.setCpTipoConfiguracao(tipoConfiguracao);
								cfgGrpNova.setCpConfiguracao(cfgNova);
								cfgGrpNova.setCpGrupo(grp);
								cfgGrpNova.atualizarCpConfiguracao();
								cfgGrpGravada.setCpConfiguracao(dao.carregar(cfgGrpGravada.getCpConfiguracao()));
								dao().gravarComHistorico(cfgNova,
										cfgGrpGravada.getCpConfiguracao(), dt,
										getIdentidadeCadastrante());
							}
						}
					}
				}
			}

			dao().commitTransacao();
			Cp.getInstance().getConf().limparCacheSeNecessario();
			return idCpGrupo;
		} catch (Exception e) {
			throw new AplicacaoException("Id do grupo: " + idCpGrupo
					+ " erro ao gravar grupo e configurações.", 0, e);
		}
	}

	private boolean isConfiguracaoAvancada(ConfiguracaoGrupo cfgGrupo) {
		return cfgGrupo instanceof ConfiguracaoGrupoEmail
				|| cfgGrupo instanceof ConfiguracaoGrupoFormula;
	}

	private boolean podeEditarConfiguracoesAvancadas() throws Exception {
		return Cp
				.getInstance()
				.getComp()
				.getConfiguracaoBL()
				.podeUtilizarServicoPorConfiguracao(getTitular(),
						getLotaTitular(),
						"SIGA;GI;GDISTR;CONF_AVANC:Configuracões Avançadas");
	}

	protected void aGravarGestorGrupo(Long idCpGrupo,
			DpLotacaoSelecao lotacaoGestoraSel) {
		DpLotacao lot = lotacaoGestoraSel.getObjeto();
		if (lot == null) {
			throw new AplicacaoException("A unidade deve ser definida!");
		} else {
			dao().iniciarTransacao();
			CpTipoDeConfiguracao tpConf = dao().consultar(
					CpTipoDeConfiguracao.GERENCIAR_GRUPO,
					CpTipoDeConfiguracao.class, false);
			CpSituacaoDeConfiguracaoEnum situacao = CpSituacaoDeConfiguracaoEnum.PODE;

			CpConfiguracao conf = new CpConfiguracao();
			conf.setLotacao(lot);
			conf.setCpTipoConfiguracao(tpConf);
			conf.setCpSituacaoConfiguracao(situacao);
			conf.setCpGrupo(daoGrupo(idCpGrupo));
			conf.setHisDtIni(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(conf, getIdentidadeCadastrante());
			setIdCpGrupo(idCpGrupo);
			dao().commitTransacao();
		}

	}

	protected void aExcluirGestorGrupo(Long idCpGrupo, Long idConfGestor) {
		dao().iniciarTransacao();
		CpConfiguracao conf = dao().consultar(idConfGestor,
				CpConfiguracao.class, false);
		conf.setHisDtFim(dao().consultarDataEHoraDoServidor());
		dao().gravarComHistorico(conf, getIdentidadeCadastrante());

		setIdCpGrupo(idCpGrupo);
		dao().commitTransacao();
	}

	protected List<CpConfiguracao> getConfGestores(Long idCpGrupo) {
		CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.GERENCIAR_GRUPO;
		CpSituacaoDeConfiguracaoEnum situacao = CpSituacaoDeConfiguracaoEnum.PODE;
		CpGrupo grp = daoGrupo(idCpGrupo);
		if (grp == null) {
			return null;
		} else {
			CpConfiguracao fltConf = new CpConfiguracao();
			fltConf.setCpGrupo(grp);
			fltConf.setCpTipoConfiguracao(tpConf);
			fltConf.setCpSituacaoConfiguracao(situacao);

			List<CpConfiguracao> confs = dao().consultar(fltConf);

			Iterator it = confs.iterator();
			while (it.hasNext()) {
				CpConfiguracao c = (CpConfiguracao) it.next();
				if (c.getHisDtFim() != null
						|| c.getCpGrupo() == null
						|| !c.getCpGrupo().getIdInicial()
								.equals(grp.getIdInicial())) {
					it.remove();
				}
			}
			return confs;
		}
	}

	/*
	 * Prepara a lista de grupos a exibir
	 */
	protected String aListar() throws Exception {

		int offset = 0;
		int itemPagina = 0;
		if (getP().getOffset() != null) {
			offset = getP().getOffset();
		}
		if (getItemPagina() != null) {
			itemPagina = getItemPagina();
		}
		CpGrupoDaoFiltro flt = new CpGrupoDaoFiltro();
		Integer t_idTpGrupo = getIdTipoGrupo();
		setCpTipoGrupo(dao().consultar(t_idTpGrupo, CpTipoGrupo.class, false));
		flt.setIdTpGrupo(t_idTpGrupo);
		int intQtd = dao().consultarQuantidade(flt);
		setTamanho(intQtd);
		List<CpGrupo> itgGrupos = dao().consultarPorFiltro(flt, offset,
				0);

		Iterator<CpGrupo> it = itgGrupos.iterator();

		CpConfiguracaoBL conf = Cp.getInstance().getConf();
		// se não for administrador, exibe apenas os grupos que pode gerir
		if (getIdTipoGrupo() == CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO
				&& !conf.podeUtilizarServicoPorConfiguracao(
						getTitular(),
						getLotaTitular(),
						"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição")) {
			while (it.hasNext()) {
				CpGrupo cpGrp = it.next();
				CpConfiguracaoBL bl = Cp.getInstance().getConf();
				if (!bl.podePorConfiguracao(getTitular(), getLotaTitular(),
						cpGrp, CpTipoDeConfiguracao.GERENCIAR_GRUPO)) {
					it.remove();
				}

			}
		}

		setItens(itgGrupos);
		return "lista";
	}

	public abstract int getIdTipoGrupo();

	@Override
	protected CpGrupoDaoFiltro createDaoFiltro() {
		final CpGrupoDaoFiltro flt = new CpGrupoDaoFiltro();
		flt.setIdTpGrupo(getIdTipoGrupo());
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		return flt;
	}

	/**
	 * @return the configuracoesGrupo
	 */
	protected ArrayList<ConfiguracaoGrupo> getConfiguracoesGrupo() {
		return configuracoesGrupo;
	}

	/**
	 * @return the conteudoConfiguracaoNova
	 */
	protected String getConteudoConfiguracaoNova() {
		return conteudoConfiguracaoNova;
	}

	protected CpTipoGrupo getCpTipoGrupo() {
		return cpTipoGrupo;
	}

	/**
	 * @return the dscCpTipoGrupo
	 */
	protected String getDscCpTipoGrupo() {
		return dscCpTipoGrupo;
	}

	/**
	 * @return the dscGrupo
	 */
	protected String getDscGrupo() {
		return dscGrupo;
	}

	/**
	 * @return the exception
	 */
	protected Exception getException() {
		return exception;
	}

	protected CpGrupoDeEmailSelecao getGrupoPaiSel() {
		return grupoPaiSel;
	}

	/**
	 * @return the idCpGrupoPai
	 */

	/**
	 * @return the idConfiguracaoNova
	 */
	protected String getIdConfiguracaoNova() {
		idConfiguracaoNova = String.valueOf(Long.MAX_VALUE);
		return idConfiguracaoNova;
	}

	/**
	 * @return the idCpGrupo
	 */
	protected Long getIdCpGrupo() {
		return idCpGrupo;
	}

	protected Long getOrgaoUsu() {
		return orgaoUsu;
	}

	/**
	 * @return the siglaGrupo
	 */
	protected String getSiglaGrupo() {
		return siglaGrupo;
	}

	/**
	 * @return the tipoConfiguracao
	 */
	protected CpTipoDeConfiguracao getTipoConfiguracao() {
		return tipoConfiguracao;
	}

	/**
	 * @return the tiposConfiguracaoGrupoParaTipoDeGrupo
	 */
	protected List<TipoConfiguracaoGrupoEnum> getTiposConfiguracaoGrupoParaTipoDeGrupo() {
		return tiposConfiguracaoGrupoParaTipoDeGrupo;
	}

	protected Map<Integer, String> getTiposConfiguracaoGrupoParaTipoDeGrupoMap() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();

		for (TipoConfiguracaoGrupoEnum item : tiposConfiguracaoGrupoParaTipoDeGrupo) {

			map.put(item.getCodigo(), item.getDescricao());
		}
		return map;
	}

	/**
	 * @return the tiposDeGrupo
	 */
	protected List<CpTipoGrupo> getTiposDeGrupo() {
		return tiposDeGrupo;
	}

	/**
	 * Localiza CpTipoGrupo de um determinado Id
	 * 
	 * @param Integer
	 *            p_intIdTipoGrupo - O id do tipo grupo que deseja localizar. @
	 *            return CpTipoGrupo
	 */
	private CpTipoGrupo obterCpTipoGrupoPorId(Integer p_intIdTipoGrupo) {
		return dao().consultar(p_intIdTipoGrupo, new CpTipoGrupo().getClass(),
				false);
	}

	/**
	 * Localiza CpGrupo
	 * 
	 * @return CpGrupo
	 */
	protected CpTipoGrupo getTipoGrupo() {
		return obterCpTipoGrupoPorId(getIdTipoGrupo());
	}

	/**
	 * Obtém a configuracao de grupo para o índice
	 * 
	 * @return ArrayList<TipoConfiguracaoGrupoEnum> - Tipos de configuração
	 *         possiveis para o grupo
	 */
	private ArrayList<TipoConfiguracaoGrupoEnum> obterTiposConfiguracaoGrupoParaTipoDeGrupo(
			CpTipoGrupo p_ctgTipoGrupo) throws AplicacaoException {
		try {
			return TipoConfiguracaoGrupoEnum
					.valoresParaTipoDeGrupo(p_ctgTipoGrupo);
		} catch (Exception e) {
			throw new AplicacaoException("Id do grupo: " + idCpGrupo
					+ " erro ao obterTiposConfiguracaoGrupoParaTipoDeGrupo.",
					0, e);
		}
	}

	/**
	 * Localiza todos os CpTipoGrupo
	 * 
	 */
	@SuppressWarnings("unchecked")
	private List<CpTipoGrupo> obterTiposGrupo() {
		return (List<CpTipoGrupo>) dao().listarTiposGrupo();
	}

	/**
	 * Prepara as listas de relação
	 * 
	 */
	protected void prepare() {
		lotacaoGestoraSel = new DpLotacaoSelecao();
		grupoPaiSel = new CpGrupoDeEmailSelecao();
		tiposDeGrupo = obterTiposGrupo();
		tipoConfiguracao = CpTipoDeConfiguracao.PERTENCER;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Selecionavel selecionarPorNome(final CpGrupoDaoFiltro flt) {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		try {
			final List l = dao().consultarPorFiltro(flt);
			if (l != null)
				if (l.size() == 1)
					return (DpLotacao) l.get(0);
			return null;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * @param configuracoesGrupo
	 *            the configuracoesGrupo to set
	 */
	protected void setConfiguracoesGrupo(
			ArrayList<ConfiguracaoGrupo> configuracoesGrupo) {
		this.configuracoesGrupo = configuracoesGrupo;
	}

	/**
	 * @param conteudoConfiguracaoNova
	 *            the conteudoConfiguracaoNova to set
	 */
	protected void setConteudoConfiguracaoNova(String conteudoConfiguracaoNova) {
		this.conteudoConfiguracaoNova = conteudoConfiguracaoNova;
	}

	protected void setCpTipoGrupo(CpTipoGrupo cpTipoGrupo) {
		this.cpTipoGrupo = cpTipoGrupo;
	}

	/**
	 * @param dscCpTipoGrupo
	 *            the dscCpTipoGrupo to set
	 */
	protected void setDscCpTipoGrupo(String dscCpTipoGrupo) {
		this.dscCpTipoGrupo = dscCpTipoGrupo;
	}

	/**
	 * @param dscGrupo
	 *            the dscGrupo to set
	 */
	protected void setDscGrupo(String dscGrupo) {
		this.dscGrupo = dscGrupo;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	protected void setException(Exception exception) {
		this.exception = exception;
	}

	protected void setGrupoPaiSel(CpGrupoDeEmailSelecao grupoPaiSel) {
		this.grupoPaiSel = grupoPaiSel;
	}

	/**
	 * @param idConfiguracaoNova
	 *            the idConfiguracaoNova to set
	 */
	protected void setIdConfiguracaoNova(String idConfiguracaoNova) {
		this.idConfiguracaoNova = idConfiguracaoNova;
	}

	/**
	 * @param idCpGrupo
	 *            the idCpGrupo to set
	 */
	protected void setIdCpGrupo(Long idCpGrupo) {
		this.idCpGrupo = idCpGrupo;
	}

	protected void setOrgaoUsu(Long orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	/**
	 * @param siglaGrupo
	 *            the siglaGrupo to set
	 */
	protected void setSiglaGrupo(String siglaGrupo) {
		this.siglaGrupo = siglaGrupo;
	}

	/**
	 * @param tipoConfiguracao
	 *            the tipoConfiguracao to set
	 */
	protected void setTipoConfiguracao(CpTipoDeConfiguracao tipoConfiguracao) {
		this.tipoConfiguracao = tipoConfiguracao;
	}

	/**
	 * @param tiposConfiguracaoGrupoParaTipoDeGrupo
	 *            the tiposConfiguracaoGrupoParaTipoDeGrupo to set
	 */
	protected void setTiposConfiguracaoGrupoParaTipoDeGrupo(
			ArrayList<TipoConfiguracaoGrupoEnum> tiposConfiguracaoGrupoParaTipoDeGrupo) {
		this.tiposConfiguracaoGrupoParaTipoDeGrupo = tiposConfiguracaoGrupoParaTipoDeGrupo;
	}

	/**
	 * @param tiposDeGrupo
	 *            the tiposDeGrupo to set
	 */
	protected void setTiposDeGrupo(ArrayList<CpTipoGrupo> tiposDeGrupo) {
		this.tiposDeGrupo = tiposDeGrupo;
	}

	protected void setLotacaoGestoraSel(DpLotacaoSelecao lotacaoGestoraSel) {
		this.lotacaoGestoraSel = lotacaoGestoraSel;
	}

	protected DpLotacaoSelecao getLotacaoGestoraSel() {
		return lotacaoGestoraSel;
	}
}
