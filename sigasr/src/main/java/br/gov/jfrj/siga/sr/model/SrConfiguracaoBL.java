package br.gov.jfrj.siga.sr.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;

public class SrConfiguracaoBL extends CpConfiguracaoBL {

	public static int ITEM_CONFIGURACAO = 31;

	public static int ACAO = 32;

	public static int LISTA_PRIORIDADE = 33;

	public static int TIPO_ATRIBUTO = 34;

	public static int ATENDENTE = 35;

	public static int PRIORIDADE = 36;

	public static SrConfiguracaoBL get() {
		return (SrConfiguracaoBL) Sr.getInstance().getConf();
	}

	public SrConfiguracaoBL() {
		super();
		setComparator(new SrConfiguracaoComparator());
	}

	@Override
	public void deduzFiltro(CpConfiguracao cpConfiguracao) {
		super.deduzFiltro(cpConfiguracao);
		if (cpConfiguracao instanceof SrConfiguracao) {
			SrConfiguracao srConf = (SrConfiguracao) cpConfiguracao;
			if (srConf.getItemConfiguracaoFiltro() != null)
				srConf.setItemConfiguracaoFiltro(srConf.getItemConfiguracaoFiltro().getAtual());
			if (srConf.getAcaoFiltro() != null)
				srConf.setAcaoFiltro(srConf.getAcaoFiltro().getAtual());
			if (srConf.getListaPrioridade() != null)
				srConf.setListaPrioridade(srConf.getListaPrioridade().getListaAtual());
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

			if (!atributosDesconsiderados.contains(ACAO)
					&& conf.getAcoesSet() != null && conf.getAcoesSet().size() > 0) {
				boolean acaoAtende = false;
				for (SrAcao item : conf.getAcoesSet()) {
					if (filtro.getAcaoFiltro() != null
							&& item.getAtual().isPaiDeOuIgualA(
									filtro.getAcaoFiltro())) {
						acaoAtende = true;
						break;
					}
				}
				if (!acaoAtende)
					return false;
			}

			if (!atributosDesconsiderados.contains(ITEM_CONFIGURACAO)
					&& conf.getItemConfiguracaoSet() != null
					&& conf.getItemConfiguracaoSet().size() > 0) {
				boolean itemAtende = false;
				for (SrItemConfiguracao item : conf.getItemConfiguracaoSet()) {
					if (filtro.getItemConfiguracaoFiltro() != null
							&& item.getAtual().isPaiDeOuIgualA(
									filtro.getItemConfiguracaoFiltro())){
						itemAtende = true;
						break;
					}
				}
				if (!itemAtende)
					return false;
			}

			if (!atributosDesconsiderados.contains(LISTA_PRIORIDADE)
					&& conf.getListaPrioridade() != null
					&& (filtro.getListaPrioridade() == null || (filtro.getListaPrioridade() != null && !conf.getListaPrioridade().getListaAtual().equivale(filtro.getListaPrioridade()))))
				return false;

			if (!atributosDesconsiderados.contains(TIPO_ATRIBUTO)
					&& conf.getAtributo() != null
					&& (filtro.getAtributo() == null || (filtro.getAtributo() != null && !conf.getAtributo()
							.getAtual().equivale(filtro.getAtributo()))))
				return false;

			if (!atributosDesconsiderados.contains(ATENDENTE)
					&& conf.getAtendente() != null
					&& (filtro.getAtendente() == null || (filtro.getAtendente() != null && !conf.getAtendente()
							.getLotacaoAtual().equivale(filtro.getAtendente()))))
				return false;

			if (!atributosDesconsiderados.contains(PRIORIDADE)
					&& conf.getPrioridade() != null
					&& (filtro.getPrioridade() == null || (filtro.getPrioridade() != null && !conf.getPrioridade()
							.equals(filtro.getPrioridade()))))
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

		SortedSet<CpPerfil> perfis = null;
		if (confFiltro.isBuscarPorPerfis()) {
			perfis = consultarPerfisPorPessoaELotacao(
					confFiltro.getDpPessoa(),
					confFiltro.getLotacao(), null);
		}

		List<SrConfiguracao> listaFinal = new ArrayList<SrConfiguracao>();

		if (confFiltro.getCpTipoConfiguracao() != null) {
			TreeSet<CpConfiguracao> lista = getListaPorTipo(confFiltro
					.getCpTipoConfiguracao().getIdTpConfiguracao());

			if (lista != null) {
				for (CpConfiguracao cpConfiguracao : lista) {
					if (cpConfiguracao.getHisDtFim() == null
							&& atendeExigencias(confFiltro, atributosDesconsiderados,
									(SrConfiguracao) cpConfiguracao, perfis)) {
						listaFinal.add((SrConfiguracao) cpConfiguracao);
					}
				}
			}
		}

		return listaFinal;
	}

	@SuppressWarnings("unused")
	@Override
	protected void evitarLazy(List<CpConfiguracao> provResults) {
		super.evitarLazy(provResults);

		for (CpConfiguracao conf : provResults) {
			if (!(conf instanceof SrConfiguracao))
				continue;

			SrConfiguracao srConf = (SrConfiguracao) conf;

			if (srConf.getAtendente() != null){
				srConf.getAtendente().getLotacaoAtual();
				if (srConf.getAtendente().getOrgaoUsuario() != null)
					srConf.getAtendente().getOrgaoUsuario().getSiglaOrgaoUsu();
			}

			if (srConf.getItemConfiguracaoSet() != null)
				for (SrItemConfiguracao i : srConf.getItemConfiguracaoSet()) {
					i.getAtual();

					if(i.gestorSet != null) {
						i.gestorSet.size();
					}

					if(i.fatorMultiplicacaoSet != null) {
						i.fatorMultiplicacaoSet.size();
					}

					for (SrItemConfiguracao hist : i.meuItemHistoricoSet) {
						hist.getAtual();
					}
				}

			if (srConf.getAcoesSet() != null)
				for (SrAcao i : srConf.getAcoesSet())
					i.getAtual();

			if (srConf.getAtributo() != null) {
				srConf.getAtributo().getHisIdIni();

				for (SrAtributo att : srConf.getAtributo().getMeuAtributoHistoricoSet()) {
					att.getAtual();
				}
			}

			if (srConf.getListaPrioridade() != null) {
				srConf.getListaPrioridade().getHisIdIni();

				if(srConf.getListaPrioridade().meuListaHistoricoSet != null)
					srConf.getListaPrioridade().meuListaHistoricoSet.size();

				if(srConf.getListaPrioridade().meuPrioridadeSolicitacaoSet != null)
					srConf.getListaPrioridade().meuPrioridadeSolicitacaoSet.size();
			}

			if (srConf.getPesquisaSatisfacao() != null)
				srConf.getPesquisaSatisfacao().getHisIdIni();

			if (srConf.getAcordo() != null)
				srConf.getAcordo().getAcordoAtual();

			if (srConf.getTipoPermissaoSet() != null) {
				for (SrTipoPermissaoLista perm : srConf.getTipoPermissaoSet()){
					//
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
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
