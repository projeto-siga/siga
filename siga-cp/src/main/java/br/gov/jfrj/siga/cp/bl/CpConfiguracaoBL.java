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
package br.gov.jfrj.siga.cp.bl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.mvel2.MVEL;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoServico;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupoFabrica;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpGrupoDaoFiltro;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

/**
 * @author eeh
 * 
 */
public class CpConfiguracaoBL {

	public static final long ID_USUARIO_ROOT = 1L;
	public static final long MATRICULA_USUARIO_ROOT = 99999L;
	public static final long CPF_ROOT = 11111111111L;
	public static final long ID_ORGAO_ROOT = 999999999L;
	public static final String SIGLA_ORGAO_ROOT = "ZZ";

	private final static org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(CpConfiguracaoBL.class);

	protected Date dtUltimaAtualizacaoCache = null;
	protected boolean cacheInicializado = false;

	protected Comparator<CpConfiguracaoCache> comparator = null;

	protected HashMap<ITipoDeConfiguracao, TreeSet<CpConfiguracaoCache>> hashListas = new HashMap<>();

	public static int PESSOA = 1;

	public static int LOTACAO = 2;

	public static int FUNCAO = 3;

	public static int ORGAO = 4;

	public static int CARGO = 5;

	public static int SERVICO = 6;

	public static int IDENTIDADE = 7;

	public static int TIPO_LOTACAO = 8;

	public static int GRUPO = 9;

	public static int COMPLEXO = 10;

	public static int PESSOA_OBJETO = 11;

	public static int LOTACAO_OBJETO = 12;

	public static int FUNCAO_OBJETO = 13;

	public static int ORGAO_OBJETO = 14;

	public static int CARGO_OBJETO = 15;

	public static int COMPLEXO_OBJETO = 16;

	public Comparator<CpConfiguracaoCache> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<CpConfiguracaoCache> comparator) {
		this.comparator = comparator;
	}

	public CpDao dao() {
		return CpDao.getInstance();
	}

	public CpConfiguracao createNewConfiguracao() {
		return new CpConfiguracao();
	}

	public void reiniciarCache() {
		cacheInicializado = false;
		dtUltimaAtualizacaoCache = null;
		hashListas = new HashMap<>();
		inicializarCacheSeNecessario();
	}
	
	public synchronized void inicializarCacheSeNecessario() {
		if (cacheInicializado)
			return;
		long inicio = System.currentTimeMillis();
		List<CpConfiguracaoCache> results = dao().consultarCacheDeConfiguracoesAtivas();

		long inicioLazy = System.currentTimeMillis();
//		evitarLazy(results);
		long fimLazy = System.currentTimeMillis();

		hashListas.clear();
		for (CpConfiguracaoCache a : results) {
			atualizarDataDeAtualizacaoDoCache(a);
			// Verifica se existe o tipo da configuracao
			if (a.cpTipoConfiguracao == null)
				continue;
			ITipoDeConfiguracao idTpConfiguracao = a.cpTipoConfiguracao;
			TreeSet<CpConfiguracaoCache> tree = hashListas.get(idTpConfiguracao);
			if (tree == null) {
				tree = new TreeSet<CpConfiguracaoCache>(comparator);
				hashListas.put(idTpConfiguracao, tree);
			}
			tree.add(a);
		}
		if (hashListas.size() == 0 && results.size() > 0)
			throw new RuntimeException("Ocorreu um erro na inicialização do cache.");
		cacheInicializado = true;

		long fim = System.currentTimeMillis();

		Logger.getLogger("siga.conf.cache")
				.info("Cache de configurações inicializado via " + this.getClass().getSimpleName() + " em "
						+ (fim - inicio) + "ms, select: " + (inicioLazy - inicio) + "ms, lazy: "
						+ (fimLazy - inicioLazy) + "ms, tree: " + (fim - fimLazy) + "ms");

	}

	public HashMap<ITipoDeConfiguracao, TreeSet<CpConfiguracaoCache>> getHashListas() {
		if (!cacheInicializado) {
			inicializarCacheSeNecessario();
		}

		return hashListas;
	}

	public TreeSet<CpConfiguracaoCache> getListaPorTipo(ITipoDeConfiguracao idTipoConfig) {
		return getHashListas().get(idTipoConfig);
	}

	private void atualizarCache() {
		if (!cacheInicializado) {
			inicializarCacheSeNecessario();
			return;
		}
		Date dt = CpDao.getInstance().consultarDataUltimaAtualizacao();

		if (dt != null && (dtUltimaAtualizacaoCache == null || dt.after(dtUltimaAtualizacaoCache))) {
			procederAtualizacaoDeCache(dt);
		}
	}

	private synchronized void procederAtualizacaoDeCache(Date dt) {
		if (dtUltimaAtualizacaoCache != null && !dt.after(dtUltimaAtualizacaoCache))
			return;

		// sfCpDao.evict(CpConfiguracao.class);

		List<CpConfiguracaoCache> alteracoes = dao().consultarConfiguracoesDesde(dtUltimaAtualizacaoCache);

		Logger.getLogger("siga.conf.cache").fine("Numero de alteracoes no cache: " + alteracoes.size());
		if (alteracoes.size() > 0) {
			// evitarLazy(alteracoes);

			for (CpConfiguracaoCache cpConfiguracao : alteracoes) {
				atualizarDataDeAtualizacaoDoCache(cpConfiguracao);
				ITipoDeConfiguracao idTpConf = cpConfiguracao.cpTipoConfiguracao;
				TreeSet<CpConfiguracaoCache> tree = hashListas.get(idTpConf);
				if (tree == null) {
					tree = new TreeSet<CpConfiguracaoCache>(comparator);
					hashListas.put(idTpConf, tree);
				}
				if (cpConfiguracao.ativaNaData(dt)) {
					int i = tree.size();
					removeById(tree, cpConfiguracao.idConfiguracao);
					tree.add(cpConfiguracao);
					if (tree.size() != i + 1)
						Logger.getLogger("siga.conf.cache")
								.fine("Configuração atualizada: " + cpConfiguracao.toString());
					else
						Logger.getLogger("siga.conf.cache")
								.fine("Configuração adicionada: " + cpConfiguracao.toString());
				} else {
					int i = tree.size();
					removeById(tree, cpConfiguracao.idConfiguracao);
					if (tree.size() != i - 1)
						Logger.getLogger("siga.conf.cache")
								.fine("Configuração previamente removida: " + cpConfiguracao.toString());
					else
						Logger.getLogger("siga.conf.cache").fine("Configuração removida: " + cpConfiguracao.toString());
				}
			}
		}

		dtUltimaAtualizacaoCache = dt;
	}

	public void atualizarDataDeAtualizacaoDoCache(CpConfiguracaoCache cpConfiguracao) {
		if (cpConfiguracao.hisDtIni != null && (dtUltimaAtualizacaoCache == null || cpConfiguracao.hisDtIni.after(dtUltimaAtualizacaoCache)))
			dtUltimaAtualizacaoCache = cpConfiguracao.hisDtIni;
		if (cpConfiguracao.hisDtFim != null && cpConfiguracao.hisDtFim.after(dtUltimaAtualizacaoCache))
			dtUltimaAtualizacaoCache = cpConfiguracao.hisDtFim;
	}

	private void removeById(TreeSet<CpConfiguracaoCache> tree, long id) {
		List<CpConfiguracaoCache> found = new ArrayList<>();
		for (CpConfiguracaoCache cfg : tree)
			if (cfg.idConfiguracao == id)
				found.add(cfg);
		for (CpConfiguracaoCache cfg : found)
			tree.remove(cfg);
	}

	/**
	 * Varre as entidades definidas na configuração para evitar que o hibernate
	 * guarde versões lazy delas.
	 * 
	 * @param listaCfg - lista de configurações que podem ter objetos lazy
	 */
	protected void evitarLazy(List<CpConfiguracao> provResults) {
		for (CpConfiguracao cfg : provResults) {
			if (cfg.getCpSituacaoConfiguracao() != null) {
				cfg.getCpSituacaoConfiguracao().getDescr();
			}
			if (cfg.getOrgaoUsuario() != null)
				cfg.getOrgaoUsuario().getDescricao();
			if (cfg.getComplexo() != null)
				cfg.getComplexo().getNomeComplexo();
			if (cfg.getLotacao() != null) {
				cfg.getLotacao().getSigla();
				cfg.getLotacao().getOrgaoUsuario().getSigla();
			}
			if (cfg.getCargo() != null)
				cfg.getCargo().getDescricao();
			if (cfg.getFuncaoConfianca() != null) {
				cfg.getFuncaoConfianca().getDescricao();
				cfg.setFuncaoConfianca((DpFuncaoConfianca) Hibernate.unproxy(cfg.getFuncaoConfianca()));
			}
			if (cfg.getDpPessoa() != null) {
				cfg.getDpPessoa().getDescricao();
				cfg.getDpPessoa().getOrgaoUsuario().getSigla();
				// cfg.getDpPessoa().getPessoaAtual().getDescricao();
			}
			if (cfg.getCpTipoConfiguracao() != null)
				cfg.getCpTipoConfiguracao().getDescr();
			if (cfg.getCpServico() != null)
				cfg.getCpServico().getDescricao();
			if (cfg.getCpIdentidade() != null)
				cfg.getCpIdentidade().getNmLoginIdentidade();
			if (cfg.getCpGrupo() != null && cfg.getCpGrupo().getId() != null)
				cfg.getCpGrupo().getNivel();
			if (cfg.getCpTipoLotacao() != null)
				cfg.getCpTipoLotacao().getDscTpLotacao();
			if (cfg.getHisIdcIni() != null)
				cfg.getHisIdcIni().getDpPessoa().getDescricao();
			if (cfg.getHisIdcFim() != null)
				cfg.getHisIdcFim().getDpPessoa().getDescricao();

			if (cfg.getOrgaoObjeto() != null)
				cfg.getOrgaoObjeto().getDescricao();
			if (cfg.getComplexoObjeto() != null)
				cfg.getComplexoObjeto().getNomeComplexo();
			if (cfg.getLotacaoObjeto() != null) {
				cfg.getLotacaoObjeto().getSigla();
				cfg.getLotacaoObjeto().getOrgaoUsuario().getSigla();
				cfg.getLotacaoObjeto().getDpPessoaLotadosSet().size();
			}
			if (cfg.getCargoObjeto() != null)
				cfg.getCargoObjeto().getDescricao();
			if (cfg.getFuncaoConfiancaObjeto() != null)
				cfg.getFuncaoConfiancaObjeto().getDescricao();
			if (cfg.getPessoaObjeto() != null) {
				cfg.getPessoaObjeto().getDescricao();
				cfg.getPessoaObjeto().getOrgaoUsuario().getSigla();
				// cfg.getDpPessoa().getPessoaAtual().getDescricao();
			}
		}
	}

	/**
	 * Limpa o cache do hibernate. Como as configurações são mantidas em cache por
	 * motivo de performance, as alterações precisam ser atualizadas para que possam
	 * valer imediatamente.
	 * 
	 * @throws Exception
	 */
	public void limparCacheSeNecessario() throws Exception {
		atualizarCache();
	}

	// public void limparCache(CpTipoConfiguracao cpTipoConfig) throws Exception
	// {
	//
	// atualizarCache(cpTipoConfig!=null?cpTipoConfig.getId():null);
	//
	// }

	/**
	 * 
	 * Obtém a configuração a partir de um filtro, como uma consulta comum a uma
	 * entidade. O parâmetro atributoDesconsideradoFiltro deve-se ao seguinte: para
	 * se escolher a configuração a ser retornada do bando, são consideradas na base
	 * as configurações que não possuam algum campo preenchido que nulo no filtro, a
	 * não ser que esse atributo tenha sido passado através desse parãmetro. Se
	 * nenhuma lista de configurações for informada, busca todas as configurações
	 * para o TipoDeConfiguracao constante no filtro.
	 * 
	 * @param cpConfiguracaoFiltro
	 * @param atributoDesconsideradoFiltro
	 * @param lista
	 * @return
	 * @throws Exception
	 */
	public CpConfiguracaoCache buscaConfiguracao(CpConfiguracao cpConfiguracaoFiltro,
			int atributoDesconsideradoFiltro[], Date dtEvn) {
		deduzFiltro(cpConfiguracaoFiltro);

		Set<Integer> atributosDesconsiderados = new LinkedHashSet<Integer>();
		for (int i = 0; i < atributoDesconsideradoFiltro.length; i++) {
			atributosDesconsiderados.add(atributoDesconsideradoFiltro[i]);
		}

		SortedSet<CpPerfil> perfis = null;
		if (cpConfiguracaoFiltro.isBuscarPorPerfis()
				|| (cpConfiguracaoFiltro.getCpTipoConfiguracao() == CpTipoDeConfiguracao.UTILIZAR_SERVICO)) {
			perfis = consultarPerfisPorPessoaELotacao(cpConfiguracaoFiltro.getDpPessoa(),
					cpConfiguracaoFiltro.getLotacao(), dtEvn);

			// Quando o filtro especifica um perfil, ou seja, estamos tentando
			// avaliar as permissões de um determinado perfil, ele e todos os
			// seus pais devem ser inseridos na lista de perfis
			if (cpConfiguracaoFiltro.getCpGrupo() != null) {
				perfis = new TreeSet<CpPerfil>();
				Object g = cpConfiguracaoFiltro.getCpGrupo();
				while (true) {
					if (g instanceof HibernateProxy) {
						g = ((HibernateProxy) g).getHibernateLazyInitializer().getImplementation();
					}
					if (g instanceof CpPerfil) {
						perfis.add((CpPerfil) g);
						g = ((CpGrupo) g).getCpGrupoPai();
						if (g == null)
							break;
					} else
						break;
				}
				if (perfis.size() == 0)
					perfis = null;
			}
		}

		TreeSet<CpConfiguracaoCache> lista = null;
		if (cpConfiguracaoFiltro.getCpTipoConfiguracao() != null)
			lista = getListaPorTipo(cpConfiguracaoFiltro.getCpTipoConfiguracao());
		if (lista == null)
			return null;

		for (CpConfiguracaoCache cpConfiguracao : lista) {
			if ((!cpConfiguracao.ativaNaData(dtEvn)) || (cpConfiguracao.situacao != null
					&& cpConfiguracao.situacao == CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR)
					|| !atendeExigencias(cpConfiguracaoFiltro.converterParaCache(), atributosDesconsiderados, cpConfiguracao, perfis))
				continue;
			return cpConfiguracao;
		}
		return null;
	}

	public SortedSet<CpPerfil> consultarPerfisPorPessoaELotacao(DpPessoa pessoa, DpLotacao lotacao, Date dtEvn) {
		if (pessoa == null && lotacao == null)
			return null;

		if (pessoa != null) {
			Object p = pessoa;
			if (p instanceof HibernateProxy) {
				pessoa = (DpPessoa) ((HibernateProxy) p).getHibernateLazyInitializer().getImplementation();
			}
		}

		if (lotacao != null) {
			Object l = lotacao;
			if (l instanceof HibernateProxy) {
				lotacao = (DpLotacao) ((HibernateProxy) l).getHibernateLazyInitializer().getImplementation();
			}
		}

		TreeSet<CpConfiguracaoCache> lista = getListaPorTipo(CpTipoDeConfiguracao.PERTENCER);

		SortedSet<CpPerfil> perfis = new TreeSet<CpPerfil>();
		if (lista != null && pessoa != null) {
			for (CpConfiguracaoCache cfg : lista) {
				if (cfg.cpGrupo == 0 || cfg.cpPerfil == null || !cfg.ativaNaData(dtEvn))
					continue;

				Object g = dao().consultar(cfg.cpGrupo,CpPerfil.class,false);

				if (cfg.dpPessoa != 0 && cfg.dpPessoa != pessoa.getIdInicial())
					continue;

				if (cfg.cargo != 0 && cfg.cargo != pessoa.getCargo().getIdInicial())
					continue;

				if (cfg.funcaoConfianca != 0 && (pessoa.getFuncaoConfianca() != null && cfg.funcaoConfianca != pessoa.getFuncaoConfianca().getIdInicial()))
					continue;

				if (cfg.lotacao != 0 && cfg.lotacao != lotacao.getIdInicial())
					continue;

				if (cfg.orgaoUsuario != 0 && cfg.lotacao != lotacao.getIdInicial())
					continue;

				if (g instanceof CpPerfil && cfg.dscFormula != null) {
					Map<String, DpPessoa> pessoaMap = new HashMap<String, DpPessoa>();
					pessoaMap.put("pessoa", pessoa);
					if (!(Boolean) MVEL.eval(cfg.dscFormula, pessoaMap)) {
						continue;
					}
				}

				do {
					perfis.add(cfg.cpPerfil);
					g = cfg.cpPerfil.getCpGrupoPai();
					if (g instanceof HibernateProxy) {
						g = (CpPerfil) ((HibernateProxy) g).getHibernateLazyInitializer().getImplementation();
					}
				} while (g != null);
			}
		}
		return perfis;
	}

	/**
	 * 
	 * Obtém a situação a partir de um filtro, como uma consulta comum a uma
	 * entidade. O parâmetro atributoDesconsideradoFiltro deve-se ao seguinte: para
	 * se escolher a situação a ser retornada, são consideradas na base as
	 * configurações que não possuam algum campo preenchido que nulo no filtro, a
	 * não ser que esse atributo tenha sido passado através desse parãmetro. Caso
	 * nenhuma configuração seja selecionada, a situação default do tipo de
	 * configuração será retornada.
	 * 
	 * @param cpConfiguracaoFiltro
	 * @param atributoDesconsideradoFiltro
	 * @return
	 * @throws Exception
	 */
	public CpSituacaoDeConfiguracaoEnum buscaSituacao(CpConfiguracao cpConfiguracaoFiltro, int atributoDesconsideradoFiltro[],
			TreeSet<CpConfiguracao> lista) throws Exception {
		CpConfiguracaoCache cfg = buscaConfiguracao(cpConfiguracaoFiltro, atributoDesconsideradoFiltro, null);
		if (cfg != null) {
			return cfg.situacao;
		}
		
		return cpConfiguracaoFiltro.getCpTipoConfiguracao().getSituacaoDefault();
	}

	/**
	 * @param cfgFiltro
	 * @param atributosDesconsiderados
	 * @param cfg
	 * @param perfis
	 */
	public boolean atendeExigencias(CpConfiguracaoCache cfgFiltro, Set<Integer> atributosDesconsiderados,
			CpConfiguracaoCache cfg, SortedSet<CpPerfil> perfis) {
		if (cfgFiltro == null)
			cfgFiltro = new CpConfiguracaoCache();

		if (desigual(cfg.cpServico, cfgFiltro.cpServico, atributosDesconsiderados, SERVICO))
			return false;

		if (cfg.cpGrupo != 0 && (cfgFiltro.cpGrupo != 0 && cfg.cpGrupo != cfgFiltro.cpGrupo
				|| ((cfgFiltro.cpGrupo == 0) && !atributosDesconsiderados.contains(GRUPO))
						&& (perfis == null || !perfisContemGrupo(cfg, perfis))))
			return false;

		if (desigual(cfg.cpIdentidade, cfgFiltro.cpIdentidade, atributosDesconsiderados, IDENTIDADE))
			return false;

		if (desigual(cfg.dpPessoa, cfgFiltro.dpPessoa, atributosDesconsiderados, PESSOA))
			return false;

		if (desigual(cfg.lotacao, cfgFiltro.lotacao, atributosDesconsiderados, LOTACAO))
			return false;

		if (desigual(cfg.complexo, cfgFiltro.complexo, atributosDesconsiderados, COMPLEXO))
			return false;

		if (desigual(cfg.funcaoConfianca, cfgFiltro.funcaoConfianca, atributosDesconsiderados, FUNCAO))
			return false;

		if (desigual(cfg.orgaoUsuario, cfgFiltro.orgaoUsuario, atributosDesconsiderados, ORGAO))
			return false;

		if (desigual(cfg.cargo, cfgFiltro.cargo, atributosDesconsiderados, CARGO))
			return false;

		if (desigual(cfg.cpTipoLotacao, cfgFiltro.cpTipoLotacao, atributosDesconsiderados, TIPO_LOTACAO))
			return false;

		if (desigual(cfg.pessoaObjeto, cfgFiltro.pessoaObjeto, atributosDesconsiderados, PESSOA_OBJETO))
			return false;

		if (desigual(cfg.lotacaoObjeto, cfgFiltro.lotacaoObjeto, atributosDesconsiderados, LOTACAO_OBJETO))
			return false;

		if (desigual(cfg.complexoObjeto, cfgFiltro.complexoObjeto, atributosDesconsiderados, COMPLEXO_OBJETO))
			return false;

		if (desigual(cfg.funcaoConfiancaObjeto, cfgFiltro.funcaoConfiancaObjeto, atributosDesconsiderados,
				FUNCAO_OBJETO))
			return false;

		if (desigual(cfg.orgaoObjeto, cfgFiltro.orgaoObjeto, atributosDesconsiderados, ORGAO_OBJETO))
			return false;

		if (desigual(cfg.cargoObjeto, cfgFiltro.cargoObjeto, atributosDesconsiderados, CARGO_OBJETO))
			return false;

		return true;
	}

	protected boolean desigual(long cfg, long filtro, Set<Integer> atributosDesconsiderados, int atributo) {
		return cfg != 0
				&& ((filtro != 0 && cfg != filtro) || ((filtro == 0) && !atributosDesconsiderados.contains(atributo)));
	}

	/**
	 * Verifica se a configuracao refere-se a um perfil ao qual a pessoa/lotacao
	 * pertence
	 * 
	 * @param cfg    - A configuração a ser verificada
	 * @param perfis - os perfis da pessoa/lotacao
	 * @return
	 */
	private boolean perfisContemGrupo(CpConfiguracaoCache cfg, SortedSet<CpPerfil> perfis) {
		for (CpPerfil cpPerfil : perfis) {
			if (cpPerfil.getIdInicial() == cfg.cpGrupo) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * Método com implementação completa, chamado pelas outras sobrecargas
	 * 
	 * @param cpTpDoc
	 * @param cpFormaDoc
	 * @param cpMod
	 * @param cpClassificacao
	 * @param cpVia
	 * @param cpTpMov
	 * @param cargo
	 * @param cpOrgaoUsu
	 * @param dpFuncaoConfianca
	 * @param dpLotacao
	 * @param dpPessoa
	 * @param nivelAcesso
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(CpOrgaoUsuario cpOrgaoUsu, DpLotacao dpLotacao, DpCargo cargo,
			DpFuncaoConfianca dpFuncaoConfianca, DpPessoa dpPessoa, CpServico cpServico, CpIdentidade cpIdentidade,
			CpGrupo cpGrupo, CpTipoLotacao cpTpLotacao, ITipoDeConfiguracao idTpConf) throws Exception {
		try {
			CpConfiguracao cfgFiltro = createNewConfiguracao();

			cfgFiltro.setCargo(cargo);
			cfgFiltro.setOrgaoUsuario(cpOrgaoUsu);
			cfgFiltro.setFuncaoConfianca(dpFuncaoConfianca);
			cfgFiltro.setLotacao(dpLotacao);
			cfgFiltro.setDpPessoa(dpPessoa);
			cfgFiltro.setCpServico(cpServico);
			cfgFiltro.setCpIdentidade(cpIdentidade);
			cfgFiltro.setCpTipoLotacao(dpLotacao != null ? dpLotacao.getCpTipoLotacao() : null);
			cfgFiltro.setCpGrupo(cpGrupo);
			cfgFiltro.setCpTipoLotacao(cpTpLotacao);

			cfgFiltro.setCpTipoConfiguracao(idTpConf);

			CpConfiguracaoCache cfg = (CpConfiguracaoCache) buscaConfiguracao(cfgFiltro, new int[] { 0 }, null);

			return situacaoPermissiva(cfgFiltro, cfg);
		} catch (Exception ex) {
			log.error(ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	public static boolean situacaoPermissiva(CpConfiguracao cfgFiltro, CpConfiguracaoCache cfg) {
		CpSituacaoDeConfiguracaoEnum situacao;

		if (cfg != null) {
			situacao = cfg.situacao;
		} else {
			situacao = cfgFiltro.getCpTipoConfiguracao().getSituacaoDefault();
		}

		if (situacao != null && (situacao == CpSituacaoDeConfiguracaoEnum.PODE
				|| situacao == CpSituacaoDeConfiguracaoEnum.DEFAULT
				|| situacao == CpSituacaoDeConfiguracaoEnum.OBRIGATORIO)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Usado para se verificar se uma pessoa pode realizar uma determinada operação
	 * no documento
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, dpPessoa, null, null, null, null, idTpConf);

	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao, CpServico cpServico, ITipoDeConfiguracao idTpConf)
			throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, dpPessoa, cpServico, null, null, null, idTpConf);

	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, null, dpPessoa, null, null, null, null, idTpConf);
	}

	public boolean podePorConfiguracao(DpLotacao dpLotacao, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, null, null, null, null, null, idTpConf);
	}

	public boolean podePorConfiguracao(CpIdentidade cpIdentidade, ITipoDeConfiguracao idTpConf) throws Exception {
		return podePorConfiguracao(null, null, null, null, null, null, cpIdentidade, null, null, idTpConf);
	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao, CpGrupo cpGrupo, ITipoDeConfiguracao idTpConf)
			throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, dpPessoa, null, null, cpGrupo, null, idTpConf);
	}

	/**
	 * Infere configurações óbvias. Por exemplo, se for informada a pessoa, a
	 * lotação, órgão etc. já serão preenchidos automaticamente.
	 * 
	 * @param cpConfiguracao
	 */
	public void deduzFiltro(CpConfiguracao cpConfiguracao) {

		if (cpConfiguracao == null)
			return;

		if (cpConfiguracao.getCpIdentidade() != null) {
			if (cpConfiguracao.getDpPessoa() == null)
				cpConfiguracao.setDpPessoa(cpConfiguracao.getCpIdentidade().getDpPessoa());
		}

		if (cpConfiguracao.getDpPessoa() != null) {
			if (cpConfiguracao.getLotacao() == null)
				cpConfiguracao.setLotacao(cpConfiguracao.getDpPessoa().getLotacao());
			if (cpConfiguracao.getCargo() == null)
				cpConfiguracao.setCargo(cpConfiguracao.getDpPessoa().getCargo());
			if (cpConfiguracao.getFuncaoConfianca() == null)
				cpConfiguracao.setFuncaoConfianca(cpConfiguracao.getDpPessoa().getFuncaoConfianca());
		}

		if (cpConfiguracao.getLotacao() != null)
			if (cpConfiguracao.getOrgaoUsuario() == null) {
				cpConfiguracao.setOrgaoUsuario(cpConfiguracao.getLotacao().getOrgaoUsuario());
				cpConfiguracao.setCpTipoLotacao(cpConfiguracao.getLotacao().getCpTipoLotacao());
			}

		if (cpConfiguracao.getPessoaObjeto() != null) {
			if (cpConfiguracao.getLotacaoObjeto() == null)
				cpConfiguracao.setLotacaoObjeto(cpConfiguracao.getPessoaObjeto().getLotacao());
			if (cpConfiguracao.getCargoObjeto() == null)
				cpConfiguracao.setCargoObjeto(cpConfiguracao.getPessoaObjeto().getCargo());
			if (cpConfiguracao.getFuncaoConfiancaObjeto() == null)
				cpConfiguracao.setFuncaoConfiancaObjeto(cpConfiguracao.getPessoaObjeto().getFuncaoConfianca());
		}

		if (cpConfiguracao.getLotacaoObjeto() != null)
			if (cpConfiguracao.getOrgaoObjeto() == null) {
				cpConfiguracao.setOrgaoObjeto(cpConfiguracao.getLotacaoObjeto().getOrgaoUsuario());
			}

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("static-access")
	public Boolean podeUtilizarServicoPorConfiguracao(DpPessoa titular, DpLotacao lotaTitular, String servicoPath) {
		try {
			if (titular == null || lotaTitular == null)
				return false;

			CpServico srv = null;
			CpServico srvPai = null;
			CpServico srvRecuperado = null;

			srvRecuperado = dao().consultarCpServicoPorChave(servicoPath);
			if (srvRecuperado == null) {
				// Constroi uma linha completa, tipo full path
				for (String s : servicoPath.split(";")) {
					String[] asParts = s.split(":"); // Separa a sigla da
														// descrição
					String sSigla = asParts[0];
					srv = new CpServico();
					srv.setSiglaServico(srvPai != null ? srvPai.getSigla() + "-" + sSigla : sSigla);
					srv.setCpServicoPai(srvPai);
					srvRecuperado = dao().consultarPorSigla(srv);
					if (srvRecuperado == null) {
						CpTipoServico tpsrv = dao().consultar(CpTipoServico.TIPO_CONFIG_SISTEMA, CpTipoServico.class,
								false);
						String sDesc = (asParts.length > 1 ? asParts[1] : "");
						srv.setDscServico(sDesc);
						srv.setCpServicoPai(srvPai);
						srv.setCpTipoServico(tpsrv);
						ContextoPersistencia.begin();
						dao().acrescentarServico(srv);
						ContextoPersistencia.commit();
					}
					srvPai = srvRecuperado;
				}
			}
			return Cp.getInstance().getConf().podePorConfiguracao(titular, lotaTitular, srvRecuperado,
					CpTipoDeConfiguracao.UTILIZAR_SERVICO);
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível calcular acesso ao serviço " + servicoPath, e);
		}
	}

	/**
	 * Localiza as ConfiguracaoGrupoEmail pertencentes a um determinado grupo
	 * 
	 * @param CpGrupo p_grpGrupo - O grupo que deseja localizar.
	 * 
	 * @throws Exception
	 */
	public ArrayList<ConfiguracaoGrupo> obterCfgGrupo(CpGrupo grp) throws Exception {

		ArrayList<ConfiguracaoGrupo> aCfgGrp = new ArrayList<ConfiguracaoGrupo>();
		ConfiguracaoGrupoFabrica fabrica = new ConfiguracaoGrupoFabrica();
		try {
			TreeSet<CpConfiguracaoCache> l = Cp.getInstance().getConf()
					.getListaPorTipo(CpTipoDeConfiguracao.PERTENCER);
			if (l != null) {
				for (CpConfiguracaoCache cfg : l) {
					if (cfg.cpGrupo == 0 || cfg.cpGrupo != grp.getIdInicial() || cfg.hisDtFim != null)
						continue;
					CpConfiguracao c = dao().consultar(cfg.idConfiguracao, CpConfiguracao.class, false);
					ConfiguracaoGrupo cfgGrp = fabrica.getInstance(c);
					aCfgGrp.add(cfgGrp);
				}
			}
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo configurações", 0, e);
		}
		return aCfgGrp;
	}

	/**
	 * Retorna as pessoas que podem acessar o grupos de segurança da lotação
	 * 
	 * @param lot
	 * @return
	 */
	public Set<DpPessoa> getPessoasGrupoSegManual(DpLotacao lot) {

		Set<DpPessoa> resultado = new HashSet<DpPessoa>();
		try {
			limparCacheSeNecessario();
			Set<CpConfiguracaoCache> configs = Cp.getInstance().getConf()
					.getListaPorTipo(CpTipoDeConfiguracao.UTILIZAR_SERVICO_OUTRA_LOTACAO);
			for (CpConfiguracaoCache c : configs) {
				DpPessoa pesAtual = CpDao.getInstance().consultarPorIdInicial(c.dpPessoa);
				if (c.dpPessoa == pesAtual.getIdInicial()) {
					if (c.hisDtFim == null && pesAtual.getDataFim() == null && c.lotacao == lot.getIdInicial()) {
						resultado.add(pesAtual);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resultado;

	}

	public Set<DpLotacao> getLotacoesGrupoSegManual(DpPessoa pes) {
		Set<DpLotacao> resultado = new HashSet<DpLotacao>();
		try {
			limparCacheSeNecessario();
			Set<CpConfiguracaoCache> configs = Cp.getInstance().getConf()
					.getListaPorTipo(CpTipoDeConfiguracao.UTILIZAR_SERVICO_OUTRA_LOTACAO);
			for (CpConfiguracaoCache c : configs) {
				DpLotacao lotacaoAtual = CpDao.getInstance().consultarPorIdInicial(DpLotacao.class, c.lotacao);
				System.out.println("Lotação atual : " + lotacaoAtual);
				if (c.hisDtFim == null && lotacaoAtual.getDataFim() == null && c.dpPessoa == pes.getIdInicial()) {
					resultado.add(lotacaoAtual);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return resultado;

	}

	public void excluirPessoaExtra(DpPessoa pes, DpLotacao lot, ITipoDeConfiguracao tpConf,
			CpIdentidade identidadeCadastrante) {
		ModeloDao.iniciarTransacao();
		try {
			Set<CpConfiguracaoCache> configs = getListaPorTipo(tpConf);
			for (CpConfiguracaoCache c : configs) {
				if (c.hisDtFim == null && c.dpPessoa == pes.getIdInicial() && c.lotacao == lot.getIdInicial()) {
					CpConfiguracao cfg = dao().consultar(c.idConfiguracao, CpConfiguracao.class, false);
					cfg.setHisDtFim(dao().consultarDataEHoraDoServidor());
					dao().gravarComHistorico(cfg, identidadeCadastrante);
				}
			}
			ModeloDao.commitTransacao();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean podeGerirAlgumGrupo(DpPessoa titular, DpLotacao lotaTitular, Long idCpTipoGrupo) throws Exception {
		return dao().getGruposGeridos(titular, lotaTitular, idCpTipoGrupo).size() > 0;
	}

	public boolean podeGerirGrupo(DpPessoa titular, DpLotacao lotaTitular, Long idCpGrupo, Long idCpTipoGrupo) {

		try {
			CpGrupoDaoFiltro flt = new CpGrupoDaoFiltro();
			CpGrupo cpGrp = CpDao.getInstance().consultar(idCpGrupo, CpGrupo.class, false);
			flt.setIdTpGrupo(idCpTipoGrupo.intValue());
			CpConfiguracaoBL bl = Cp.getInstance().getConf();

			return bl.podePorConfiguracao(titular, lotaTitular, cpGrp, CpTipoDeConfiguracao.GERENCIAR_GRUPO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 
	 * Retorna uma lista de (ex)configurações vigentes de acordo com um certo tipo
	 * 
	 * @param CpConfiguracao
	 * 
	 */
	public List<CpConfiguracao> buscarConfiguracoesVigentes(final CpConfiguracao exemplo) {
		Date hoje = new Date();
		List<CpConfiguracao> todasConfig = CpDao.getInstance().consultar(exemplo);
		List<CpConfiguracao> configVigentes = new ArrayList<CpConfiguracao>();

		for (CpConfiguracao cfg : todasConfig) {
			if (!cfg.ativaNaData(hoje))
				continue;
			configVigentes.add(cfg);
		}
		return (configVigentes);
	}
}
