package models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;

public class SrConfiguracaoBL extends CpConfiguracaoBL {

	private static SrConfiguracaoBL instancia = new SrConfiguracaoBL();

	public static int ITEM_CONFIGURACAO = 31;

	public static int SERVICO = 32;

	public static SrConfiguracaoBL get() {
		return instancia;
	}

	public SrConfiguracaoBL() {
		super();
		setComparator(new SrConfiguracaoComparator());
	}

	private void evitaSessionClosed(SrConfiguracao conf) {
		if (conf.preAtendente != null)
			conf.preAtendente.getLotacaoAtual();
		if (conf.atendente != null)
			conf.atendente.getLotacaoAtual();
		if (conf.posAtendente != null)
			conf.posAtendente.getLotacaoAtual();
		if (conf.itemConfiguracao != null)
			conf.itemConfiguracao.getAtual();
		if (conf.servico != null)
			conf.servico.getAtual();
		if (conf.tipoAtributo != null)
			conf.tipoAtributo.getAtual();
	}

	public SrConfiguracao buscarConfiguracao(SrConfiguracao conf)
			throws Exception {
		return (SrConfiguracao) buscaConfiguracao(conf, new int[] { 0 }, null);
	}

	@Override
	public void deduzFiltro(CpConfiguracao cpConfiguracao) {
		super.deduzFiltro(cpConfiguracao);
		if (cpConfiguracao instanceof SrConfiguracao) {
			SrConfiguracao srConf = (SrConfiguracao) cpConfiguracao;
			if (srConf.itemConfiguracao != null)
				srConf.itemConfiguracao = srConf.itemConfiguracao.getAtual();
			if (srConf.servico != null)
				srConf.servico = srConf.servico.getAtual();
		}

	}

	@Override
	public boolean atendeExigencias(CpConfiguracao cfgFiltro,
			Set<Integer> atributosDesconsiderados, CpConfiguracao cfg,
			SortedSet<CpPerfil> perfis) {

		if (!super.atendeExigencias(cfgFiltro, atributosDesconsiderados, cfg,
				perfis))
			return false;

		if (cfg instanceof SrConfiguracao
				&& cfgFiltro instanceof SrConfiguracao) {
			SrConfiguracao conf = (SrConfiguracao) cfg;
			SrConfiguracao filtro = (SrConfiguracao) cfgFiltro;

			if (filtro.subTipoConfig == SrSubTipoConfiguracao.DESIGNACAO_PRE_ATENDENTE
					&& conf.preAtendente == null)
				return false;

			if (filtro.subTipoConfig == SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE
					&& conf.atendente == null)
				return false;

			if (filtro.subTipoConfig == SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE
					&& conf.posAtendente == null)
				return false;

			if (!atributosDesconsiderados.contains(SERVICO)
					&& conf.servico != null
					&& (filtro.servico == null || (filtro.servico != null && !conf.servico
							.getAtual().isPaiDeOuIgualA(filtro.servico))))
				return false;

			if (!atributosDesconsiderados.contains(ITEM_CONFIGURACAO)
					&& conf.itemConfiguracao != null
					&& (filtro.itemConfiguracao == null || (filtro.itemConfiguracao != null && !conf.itemConfiguracao
							.getAtual()
							.isPaiDeOuIgualA(filtro.itemConfiguracao))))
				return false;

		}
		return true;
	}

	public List<SrConfiguracao> listarConfiguracoesAtivasPorFiltro(
			SrConfiguracao confFiltro, int atributoDesconsideradoFiltro[])
			throws Exception {

		deduzFiltro(confFiltro);
		Set<Integer> atributosDesconsiderados = new LinkedHashSet<Integer>();
		for (int i = 0; i < atributoDesconsideradoFiltro.length; i++) {
			atributosDesconsiderados.add(atributoDesconsideradoFiltro[i]);
		}

		List<SrConfiguracao> listaFinal = new ArrayList<SrConfiguracao>();
		TreeSet<CpConfiguracao> lista = getListaPorTipo(confFiltro
				.getCpTipoConfiguracao().getIdTpConfiguracao());

		for (CpConfiguracao cpConfiguracao : lista) {
			if (cpConfiguracao.getHisDtFim() == null
					&& atendeExigencias(confFiltro, atributosDesconsiderados,
							(SrConfiguracao) cpConfiguracao, null))
				listaFinal.add((SrConfiguracao) cpConfiguracao);
		}
		return listaFinal;
	}

	@Override
	public TreeSet<CpConfiguracao> getListaPorTipo(Long idTipoConfig)
			throws Exception {
		TreeSet<CpConfiguracao> lista = super.getListaPorTipo(idTipoConfig);
		for (CpConfiguracao conf : lista) {
			if (conf instanceof SrConfiguracao)
				evitaSessionClosed((SrConfiguracao) conf);
		}
		return lista;
	}

}
