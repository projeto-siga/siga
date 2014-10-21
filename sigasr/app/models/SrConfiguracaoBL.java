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

	public static int ITEM_CONFIGURACAO = 31;

	public static int ACAO = 32;

	public static int LISTA_PRIORIDADE = 33;

	public static int TIPO_ATRIBUTO = 34;

	public static SrConfiguracaoBL get() {
		return (SrConfiguracaoBL) Sr.getInstance().getConf();
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
		if (cpConfiguracao instanceof SrConfiguracao) {
			SrConfiguracao srConf = (SrConfiguracao) cpConfiguracao;
			if (srConf.itemConfiguracao != null)
				srConf.itemConfiguracao = srConf.itemConfiguracao.getAtual();
			if (srConf.acao != null)
				srConf.acao = srConf.acao.getAtual();
			if (srConf.listaPrioridade != null)
				srConf.listaPrioridade = srConf.listaPrioridade.getListaAtual();
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

			if (filtro.subTipoConfig == SrSubTipoConfiguracao.DESIGNACAO_EQUIPE_QUALIDADE
					&& conf.equipeQualidade == null)
				return false;

			if (!atributosDesconsiderados.contains(ACAO)
					&& conf.acao != null
					&& (filtro.acao == null || (filtro.acao != null && !conf.acao
							.getAtual().isPaiDeOuIgualA(filtro.acao))))
				return false;

			if (!atributosDesconsiderados.contains(ITEM_CONFIGURACAO)
					&& conf.itemConfiguracao != null
					&& (filtro.itemConfiguracao == null || (filtro.itemConfiguracao != null && !conf.itemConfiguracao
							.getAtual()
							.isPaiDeOuIgualA(filtro.itemConfiguracao))))
				return false;

			if (!atributosDesconsiderados.contains(LISTA_PRIORIDADE)
					&& conf.listaPrioridade != null
					&& (filtro.listaPrioridade == null || (filtro.listaPrioridade != null && !conf.listaPrioridade
							.getListaAtual().equivale(filtro.listaPrioridade))))
				return false;

			if (!atributosDesconsiderados.contains(TIPO_ATRIBUTO)
					&& conf.tipoAtributo != null
					&& (filtro.tipoAtributo == null || (filtro.tipoAtributo != null && !conf.tipoAtributo
							.getAtual().equivale(filtro.tipoAtributo))))
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
	protected void evitarLazy(List<CpConfiguracao> provResults) {
		super.evitarLazy(provResults);

		for (CpConfiguracao conf : provResults) {
			if (!(conf instanceof SrConfiguracao))
				continue;
			SrConfiguracao srConf = (SrConfiguracao) conf;
			if (srConf.preAtendente != null)
				srConf.preAtendente.getLotacaoAtual();
			if (srConf.atendente != null)
				srConf.atendente.getLotacaoAtual();
			if (srConf.posAtendente != null)
				srConf.posAtendente.getLotacaoAtual();

			if (srConf.itemConfiguracao != null) {
				SrItemConfiguracao atual = srConf.itemConfiguracao.getAtual();
				if (atual != null)
					atual.getItemETodosDescendentes();

				// Edson: varrer os pais é necessário porque o
				// getItensDisponiveis() precisa dessa
				// informação
				SrItemConfiguracao itemPai = atual.pai;
				while (itemPai != null) {
					itemPai.getDescricao();
					itemPai = itemPai.pai;
				}
			}

			if (srConf.acao != null) {
				SrAcao atual = srConf.acao.getAtual();
				if (atual != null)
					atual.getAcaoETodasDescendentes();
			}
			if (srConf.tipoAtributo != null)
				srConf.tipoAtributo.getAtual();
			if (srConf.listaPrioridade != null)
				srConf.listaPrioridade.getListaAtual();

		}
	}

	public void atualizarConfiguracoesDoCache(List<SrConfiguracao> configs) {
		List<SrConfiguracao> evitarLazy = new ArrayList<SrConfiguracao>();
		for (SrConfiguracao conf : configs) {
			hashListas.get(conf.getCpTipoConfiguracao().getIdTpConfiguracao())
					.remove(conf);
			SrConfiguracao newConf = SrConfiguracao.findById(conf
					.getIdConfiguracao());
			hashListas.get(
					newConf.getCpTipoConfiguracao().getIdTpConfiguracao()).add(
					newConf);
			evitarLazy.add(newConf);
		}
		evitarLazy((List<CpConfiguracao>) (List<?>) evitarLazy);
	}

}
