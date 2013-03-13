package models.siga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;

import play.db.jpa.JPA;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupoFabrica;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

/**
 * @author eeh
 * 
 */
public class PlayConfiguracaoBL {

	private Date dtUltimaAtualizacao = null;

	private Comparator<PlayConfiguracao> comparator = null;

	public HashMap<Long, TreeSet<PlayConfiguracao>> hashListas = new HashMap<Long, TreeSet<PlayConfiguracao>>();

	public static int PESSOA = 1;

	public static int LOTACAO = 2;

	public static int FUNCAO = 3;

	public static int ORGAO = 4;

	public static int CARGO = 5;

	public static int SERVICO = 6;

	public static int IDENTIDADE = 7;

	public CpDao dao() {
		return CpDao.getInstance();
	}

	public Comparator<PlayConfiguracao> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<PlayConfiguracao> comparator) {
		this.comparator = comparator;
	}

	public PlayConfiguracao createNewConfiguracao() {
		return new PlayConfiguracao();
	}

	public HashMap<Long, TreeSet<PlayConfiguracao>> getHashListas() {
		return hashListas;
	}

	public TreeSet<PlayConfiguracao> getListaPorTipo(Long idTipoConfig)
			throws Exception {
		synchronized (this) {
			if (!getHashListas().containsKey(idTipoConfig)) {
				TreeSet<PlayConfiguracao> tree = new TreeSet<PlayConfiguracao>(
						comparator);
				PlayConfiguracao cpConfiguracao = createNewConfiguracao();

				cpConfiguracao.setCpTipoConfiguracao(JPA.em().find(
						CpTipoConfiguracao.class, idTipoConfig));
				List<PlayConfiguracao> provResults = (List<PlayConfiguracao>) JPA
						.em()
						.createQuery(
								"from PlayConfiguracao where cpTipoConfiguracao.idTpConfiguracao = "
										+ idTipoConfig).getResultList();

				// Varre algumas entidades para evitar que o hibernate guarde
				// versoes lazy delas
				for (PlayConfiguracao cfg : provResults) {
					if (cfg.getCpSituacaoConfiguracao() != null)
						cfg.getCpSituacaoConfiguracao().getDscSitConfiguracao();
					if (cfg.getOrgaoUsuario() != null)
						cfg.getOrgaoUsuario().getDescricao();
					if (cfg.getLotacao() != null)
						cfg.getLotacao().getSigla();
					if (cfg.getCargo() != null)
						cfg.getCargo().getDescricao();
					if (cfg.getFuncaoConfianca() != null)
						cfg.getFuncaoConfianca().getDescricao();
					if (cfg.getDpPessoa() != null)
						cfg.getDpPessoa().getDescricao();
					if (cfg.getCpTipoConfiguracao() != null)
						cfg.getCpTipoConfiguracao().getDscTpConfiguracao();
					if (cfg.getCpServico() != null)
						cfg.getCpServico().getDescricao();
					if (cfg.getCpIdentidade() != null)
						cfg.getCpIdentidade().getNmLoginIdentidade();
					if (cfg.getCpGrupo() != null)
						cfg.getCpGrupo().getDescricao();
					if (cfg.getCpTipoLotacao() != null)
						cfg.getCpTipoLotacao().getDscTpLotacao();
				}

				if (provResults != null)
					tree.addAll(provResults);
				getHashListas().put(idTipoConfig, tree);
			}
			return getHashListas().get(idTipoConfig);
		}
	}

	public void evictListaPorTipo(CpTipoConfiguracao cpTipoConfig)
			throws Exception {
		synchronized (this) {
			if (cpTipoConfig == null) {
				getHashListas().clear();
				return;
			}
			Long id = cpTipoConfig.getIdTpConfiguracao();
			if (getHashListas().containsKey(id)) {
				getHashListas().remove(id);
			}
			return;
		}
	}

	public void limparCacheSeNecessario() throws Exception {
		Date dt = CpDao.getInstance().consultarDataUltimaAtualizacao();
		if (dtUltimaAtualizacao == null || dt.after(dtUltimaAtualizacao)) {
			limparCache(null);
			dtUltimaAtualizacao = dt;
		}
	}

	/**
	 * Limpa o cache do hibernate. Como as configurações são mantidas em cache
	 * por motivo de performance, as alterações precisam ser atualizadas para
	 * que possam valer imediatamente.
	 * 
	 * @throws Exception
	 */
	public void limparCache(CpTipoConfiguracao cpTipoConfig) throws Exception {

		SessionFactory sfCpDao = CpDao.getInstance().getSessao()
				.getSessionFactory();

		evictListaPorTipo(cpTipoConfig);

		sfCpDao.evict(PlayConfiguracao.class);
		sfCpDao.evict(DpLotacao.class);

		sfCpDao.evictQueries("query.PlayConfiguracao");
		sfCpDao.evictQueries("query.DpLotacao");

		return;

	}

	/**
	 * 
	 * Obtém a configuração a partir de um filtro, como uma consulta comum a uma
	 * entidade. O parâmetro atributoDesconsideradoFiltro deve-se ao seguinte:
	 * para se escolher a configuração a ser retornada do bando, são
	 * consideradas na base as configurações que não possuam algum campo
	 * preenchido que nulo no filtro, a não ser que esse atributo tenha sido
	 * passado através desse parãmetro. Se nenhuma lista de configurações for
	 * informada, busca todas as configurações para o TipoDeConfiguracao
	 * constante no filtro.
	 * 
	 * @param cpConfiguracaoFiltro
	 * @param atributoDesconsideradoFiltro
	 * @param lista
	 * @return
	 * @throws Exception
	 */
	public PlayConfiguracao buscaConfiguracao(
			PlayConfiguracao cpConfiguracaoFiltro,
			int atributoDesconsideradoFiltro[], Date dtEvn) throws Exception {
		deduzFiltro(cpConfiguracaoFiltro);

		Set<Integer> atributosDesconsiderados = new LinkedHashSet<Integer>();
		for (int i = 0; i < atributoDesconsideradoFiltro.length; i++) {
			atributosDesconsiderados.add(atributoDesconsideradoFiltro[i]);
		}

		SortedSet<CpPerfil> perfis = null;
		if (cpConfiguracaoFiltro.getCpTipoConfiguracao() != null
				&& cpConfiguracaoFiltro
						.getCpTipoConfiguracao()
						.getIdTpConfiguracao()
						.equals(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO)) {
			perfis = consultarPerfisPorPessoaELotacao(
					cpConfiguracaoFiltro.getDpPessoa(),
					cpConfiguracaoFiltro.getLotacao(), dtEvn);

			// Quando o filtro especifica um perfil, ou seja, estamos tentando
			// avaliar as permissões de um determinado perfil, ele e todos os
			// seus pais devem ser inseridos na lista de perfis
			if (cpConfiguracaoFiltro.getCpGrupo() != null) {
				perfis = new TreeSet<CpPerfil>();
				Object g = cpConfiguracaoFiltro.getCpGrupo();
				while (true) {
					if (g instanceof HibernateProxy) {
						g = ((HibernateProxy) g).getHibernateLazyInitializer()
								.getImplementation();
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

		TreeSet<PlayConfiguracao> lista = null;
		// try {
		lista = getListaPorTipo(cpConfiguracaoFiltro.getCpTipoConfiguracao()
				.getIdTpConfiguracao());
		// } catch (Exception e) {
		// System.out.println(e.getStackTrace());
		// }

		for (PlayConfiguracao cpConfiguracao : lista) {
			if ((!cpConfiguracao.ativaNaData(dtEvn))
					|| (cpConfiguracao.getCpSituacaoConfiguracao() != null && cpConfiguracao
							.getCpSituacaoConfiguracao()
							.getIdSitConfiguracao()
							.equals(CpSituacaoConfiguracao.SITUACAO_IGNORAR_CONFIGURACAO_ANTERIOR))
					|| !atendeExigencias(cpConfiguracaoFiltro,
							atributosDesconsiderados, cpConfiguracao, perfis))
				continue;
			return cpConfiguracao;
		}
		return null;
	}

	private SortedSet<CpPerfil> consultarPerfisPorPessoaELotacao(
			DpPessoa pessoa, DpLotacao lotacao, Date dtEvn) throws Exception {
		if (pessoa == null && lotacao == null)
			return null;
		TreeSet<PlayConfiguracao> lista = getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_PERTENCER);

		SortedSet<CpPerfil> perfis = new TreeSet<CpPerfil>();
		if (pessoa != null) {
			for (PlayConfiguracao cfg : lista) {
				if (cfg.getCpGrupo() == null)
					continue;
				Object g = cfg.getCpGrupo();
				if (g instanceof HibernateProxy) {
					g = ((HibernateProxy) g).getHibernateLazyInitializer()
							.getImplementation();
				}
				if (!cfg.ativaNaData(dtEvn) || !(g instanceof CpPerfil))
					continue;
				if (cfg.getDpPessoa() != null
						&& !cfg.getDpPessoa().equivale(pessoa))
					continue;

				if (cfg.getCargo() != null
						&& !cfg.getCargo().equivale(pessoa.getCargo()))
					continue;

				if (cfg.getFuncaoConfianca() != null
						&& !cfg.getFuncaoConfianca().equivale(
								pessoa.getFuncaoConfianca()))
					continue;

				if (cfg.getLotacao() != null
						&& !cfg.getLotacao().equivale(lotacao))
					continue;

				if (cfg.getOrgaoUsuario() != null
						&& !cfg.getLotacao().getOrgaoUsuario()
								.equivale(lotacao.getOrgaoUsuario()))
					continue;

				do {
					perfis.add((CpPerfil) g);
					g = ((CpPerfil) g).getCpGrupoPai();
					if (g instanceof HibernateProxy) {
						g = ((HibernateProxy) g).getHibernateLazyInitializer()
								.getImplementation();
					}
				} while (g != null);
			}
		}
		return perfis;
	}

	/**
	 * 
	 * Obtém a situação a partir de um filtro, como uma consulta comum a uma
	 * entidade. O parâmetro atributoDesconsideradoFiltro deve-se ao seguinte:
	 * para se escolher a situação a ser retornada, são consideradas na base as
	 * configurações que não possuam algum campo preenchido que nulo no filtro,
	 * a não ser que esse atributo tenha sido passado através desse parãmetro.
	 * Caso nenhuma configuração seja selecionada, a situação default do tipo de
	 * configuração será retornada.
	 * 
	 * @param cpConfiguracaoFiltro
	 * @param atributoDesconsideradoFiltro
	 * @return
	 * @throws Exception
	 */
	public CpSituacaoConfiguracao buscaSituacao(
			PlayConfiguracao cpConfiguracaoFiltro,
			int atributoDesconsideradoFiltro[], TreeSet<PlayConfiguracao> lista)
			throws Exception {
		PlayConfiguracao cfg = buscaConfiguracao(cpConfiguracaoFiltro,
				atributoDesconsideradoFiltro, null);
		if (cfg != null) {
			return cfg.getCpSituacaoConfiguracao();
		}
		return cpConfiguracaoFiltro.getCpTipoConfiguracao()
				.getSituacaoDefault();
	}

	/**
	 * @param cfgFiltro
	 * @param atributosDesconsiderados
	 * @param cfg
	 * @param perfis
	 */
	public boolean atendeExigencias(PlayConfiguracao cfgFiltro,
			Set<Integer> atributosDesconsiderados, PlayConfiguracao cfg,
			SortedSet<CpPerfil> perfis) {
		if (cfg.getCpServico() != null
				&& ((cfgFiltro.getCpServico() != null
						&& !cfgFiltro.getCpServico().equivale(
								cfg.getCpServico()) || ((cfgFiltro
						.getCpServico() == null) && !atributosDesconsiderados
						.contains(SERVICO)))))
			return false;

		if (cfg.getCpGrupo() != null
				&& (perfis == null || !perfis.contains(cfg.getCpGrupo())))
			return false;

		if (cfg.getCpIdentidade() != null
				&& ((cfgFiltro.getCpIdentidade() != null
						&& !cfg.getCpIdentidade().equivale(
								cfgFiltro.getCpIdentidade()) || ((cfgFiltro
						.getCpIdentidade() == null) && !atributosDesconsiderados
						.contains(IDENTIDADE)))))
			return false;

		if (cfg.getDpPessoa() != null
				&& ((cfgFiltro.getDpPessoa() != null
						&& !cfg.getDpPessoa().equivale(cfgFiltro.getDpPessoa()) || ((cfgFiltro
						.getDpPessoa() == null) && !atributosDesconsiderados
						.contains(PESSOA)))))
			return false;

		if (cfg.getLotacao() != null
				&& ((cfgFiltro.getLotacao() != null
						&& !cfg.getLotacao().equivale(cfgFiltro.getLotacao()) || ((cfgFiltro
						.getLotacao() == null) && !atributosDesconsiderados
						.contains(LOTACAO)))))
			return false;

		if (cfg.getFuncaoConfianca() != null
				&& ((cfgFiltro.getFuncaoConfianca() != null && !cfg
						.getFuncaoConfianca().getIdFuncao()
						.equals(cfgFiltro.getFuncaoConfianca().getIdFuncao())) || ((cfgFiltro
						.getFuncaoConfianca() == null) && !atributosDesconsiderados
						.contains(FUNCAO))))
			return false;

		if (cfg.getOrgaoUsuario() != null
				&& ((cfgFiltro.getOrgaoUsuario() != null && !cfg
						.getOrgaoUsuario().getIdOrgaoUsu()
						.equals(cfgFiltro.getOrgaoUsuario().getIdOrgaoUsu())) || ((cfgFiltro
						.getOrgaoUsuario() == null) && !atributosDesconsiderados
						.contains(ORGAO))))
			return false;

		if (cfg.getCargo() != null
				&& ((cfgFiltro.getCargo() != null && !cfg.getCargo()
						.getIdCargo().equals(cfgFiltro.getCargo().getIdCargo())) || ((cfgFiltro
						.getCargo() == null) && !atributosDesconsiderados
						.contains(CARGO))))
			return false;
		return true;
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
	public boolean podePorConfiguracao(CpOrgaoUsuario cpOrgaoUsu,
			DpLotacao dpLotacao, DpCargo cargo,
			DpFuncaoConfianca dpFuncaoConfianca, DpPessoa dpPessoa,
			CpServico cpServico, CpIdentidade cpIdentidade, long idTpConf)
			throws Exception {

		PlayConfiguracao cfgFiltro = createNewConfiguracao();

		cfgFiltro.setCargo(cargo);
		cfgFiltro.setOrgaoUsuario(cpOrgaoUsu);
		cfgFiltro.setFuncaoConfianca(dpFuncaoConfianca);
		cfgFiltro.setLotacao(dpLotacao);
		cfgFiltro.setDpPessoa(dpPessoa);
		cfgFiltro.setCpServico(cpServico);
		cfgFiltro.setCpIdentidade(cpIdentidade);

		cfgFiltro.setCpTipoConfiguracao(CpDao.getInstance().consultar(idTpConf,
				CpTipoConfiguracao.class, false));

		PlayConfiguracao cfg = (PlayConfiguracao) buscaConfiguracao(cfgFiltro,
				new int[] { 0 }, null);

		CpSituacaoConfiguracao situacao;
		if (cfg != null) {
			situacao = cfg.getCpSituacaoConfiguracao();
		} else {
			situacao = cfgFiltro.getCpTipoConfiguracao().getSituacaoDefault();
		}

		if (situacao != null
				&& situacao.getIdSitConfiguracao() == CpSituacaoConfiguracao.SITUACAO_PODE)
			return true;
		return false;
	}

	/**
	 * 
	 * Usado para se verificar se uma pessoa pode realizar uma determinada
	 * operação no documento
	 * 
	 * @param dpPessoa
	 * @param dpLotacao
	 * @param idTpConf
	 * @throws Exception
	 */
	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			long idTpConf) throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, dpPessoa, null,
				null, idTpConf);

	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, DpLotacao dpLotacao,
			CpServico cpServico, long idTpConf) throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, dpPessoa,
				cpServico, null, idTpConf);

	}

	public boolean podePorConfiguracao(DpPessoa dpPessoa, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, null, null, null, dpPessoa, null,
				null, idTpConf);
	}

	public boolean podePorConfiguracao(DpLotacao dpLotacao, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, dpLotacao, null, null, null, null,
				null, idTpConf);
	}

	public boolean podePorConfiguracao(CpIdentidade cpIdentidade, long idTpConf)
			throws Exception {
		return podePorConfiguracao(null, null, null, null, null, null,
				cpIdentidade, idTpConf);
	}

	/**
	 * Infere configurações óbvias. Por exemplo, se for informada a pessoa, a
	 * lotação, órgão etc. já serão preenchidos automaticamente.
	 * 
	 * @param cpConfiguracao
	 */
	public void deduzFiltro(PlayConfiguracao cpConfiguracao) {

		if (cpConfiguracao == null)
			return;

		if (cpConfiguracao.getCpIdentidade() != null) {
			if (cpConfiguracao.getDpPessoa() == null)
				cpConfiguracao.setDpPessoa(cpConfiguracao.getCpIdentidade()
						.getDpPessoa());
		}

		if (cpConfiguracao.getDpPessoa() != null) {
			if (cpConfiguracao.getLotacao() == null)
				cpConfiguracao.setLotacao(cpConfiguracao.getDpPessoa()
						.getLotacao());
			if (cpConfiguracao.getCargo() == null)
				cpConfiguracao
						.setCargo(cpConfiguracao.getDpPessoa().getCargo());
			if (cpConfiguracao.getFuncaoConfianca() == null)
				cpConfiguracao.setFuncaoConfianca(cpConfiguracao.getDpPessoa()
						.getFuncaoConfianca());
		}

		if (cpConfiguracao.getLotacao() != null)
			if (cpConfiguracao.getOrgaoUsuario() == null)
				cpConfiguracao.setOrgaoUsuario(cpConfiguracao.getLotacao()
						.getOrgaoUsuario());
	}

	@SuppressWarnings("static-access")
	public Boolean podeUtilizarServicoPorConfiguracao(DpPessoa titular,
			DpLotacao lotaTitular, String servicoPath) throws Exception {
		if (titular == null || lotaTitular == null)
			return false;

		CpServico srv = null;
		CpServico srvPai = null;
		CpServico srvRecuperado = null;

		// Constroi uma linha completa, tipo full path
		for (String s : servicoPath.split(";")) {
			String[] asParts = s.split(":"); // Separa a sigla da descrição
			String sSigla = asParts[0];
			srv = new CpServico();
			srv.setSiglaServico(srvPai != null ? srvPai.getSigla() + "-"
					+ sSigla : sSigla);
			srv.setCpServicoPai(srvPai);
			srvRecuperado = dao().consultarPorSigla(srv);
			if (srvRecuperado == null) {
				CpTipoServico tpsrv = dao().consultar(
						CpTipoServico.TIPO_CONFIG_SISTEMA, CpTipoServico.class,
						false);
				String sDesc = (asParts.length > 1 ? asParts[1] : "");
				srv.setDscServico(sDesc);
				srv.setCpTipoServico(tpsrv);
				dao().iniciarTransacao();
				srvRecuperado = dao().gravar(srv);
				dao().commitTransacao();
			}
			srvPai = srvRecuperado;
		}
		return Cp
				.getInstance()
				.getConf()
				.podePorConfiguracao(titular, lotaTitular, srvRecuperado,
						CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
	}

}
