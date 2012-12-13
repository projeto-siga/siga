package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSubTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;

public class SrConfiguracaoBL extends CpConfiguracaoBL {

	private static SrConfiguracaoBL instancia = new SrConfiguracaoBL();

	public static SrConfiguracaoBL get() {
		return instancia;
	}

	public SrConfiguracaoBL() {
		super();
		setComparator(new SrConfiguracaoComparator());
	}

	public SrConfiguracao buscarConfiguracao(SrConfiguracao conf)
			throws Exception {
		return (SrConfiguracao) buscaConfiguracao(conf, new int[] { 0 }, null);
	}

	@Override
	public void deduzFiltro(CpConfiguracao cpConfiguracao) {
		super.deduzFiltro(cpConfiguracao);
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

			if (conf.servico != null
					&& (filtro.servico == null || (filtro.servico != null && !conf.servico
							.isPaiDeOuIgualA(filtro.servico))))
				return false;

			if (conf.itemConfiguracao != null
					&& (filtro.itemConfiguracao == null || (filtro.itemConfiguracao != null && !conf.itemConfiguracao
							.isPaiDeOuIgualA(filtro.itemConfiguracao))))
				return false;

		}
		return true;
	}

	public List<SrConfiguracao> listarConfiguracoesAtivasPorFiltro(
			SrConfiguracao confFiltro) throws Exception {
		List<SrConfiguracao> listaFinal = new ArrayList<SrConfiguracao>();
		TreeSet<CpConfiguracao> lista = getListaPorTipo(confFiltro
				.getCpTipoConfiguracao().getIdTpConfiguracao());
		for (CpConfiguracao cpConfiguracao : lista) {
			if (cpConfiguracao.getHisDtFim() == null
					&& atendeExigencias(confFiltro, null,
							(SrConfiguracao) cpConfiguracao, null))
				listaFinal.add((SrConfiguracao) cpConfiguracao);
		}
		return listaFinal;
	}

}
