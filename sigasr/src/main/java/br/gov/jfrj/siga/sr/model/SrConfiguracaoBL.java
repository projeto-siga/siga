package br.gov.jfrj.siga.sr.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;

public class SrConfiguracaoBL extends CpConfiguracaoBL {

    public static final int ITEM_CONFIGURACAO = 31;

    public static final int ACAO = 32;

    public static final int LISTA_PRIORIDADE = 33;

    public static final int TIPO_ATRIBUTO = 34;

    public static final int ATENDENTE = 35;

    public static final int PRIORIDADE = 36;

    public static SrConfiguracaoBL get() {
        return (SrConfiguracaoBL) Sr.getInstance().getConf();
    }

    public SrConfiguracaoBL() {
        super();
        setComparator(new SrConfiguracaoCacheComparator());
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
    public boolean atendeExigencias(CpConfiguracaoCache cfgFiltro, Set<Integer> atributosDesconsiderados, CpConfiguracaoCache cfg, SortedSet<CpPerfil> perfis) {

        if (!super.atendeExigencias(cfgFiltro, atributosDesconsiderados, cfg, perfis))
            return false;

        if (cfg instanceof SrConfiguracaoCache && cfgFiltro instanceof SrConfiguracaoCache) {
        	SrConfiguracaoCache conf = (SrConfiguracaoCache) cfg;
        	SrConfiguracaoCache filtro = (SrConfiguracaoCache) cfgFiltro;

            if (!atributosDesconsiderados.contains(ACAO) && conf.acoesSet != null && conf.acoesSet.size() > 0) {
                boolean acaoAtende = false;
                for (SrAcao item : conf.acoesSet) {
                    if (filtro.getAcaoFiltro() != null && item.getAtual().isPaiDeOuIgualA(filtro.getAcaoFiltro())) {
                        acaoAtende = true;
                        break;
                    }
                }
                if (!acaoAtende)
                    return false;
            }

            if (!atributosDesconsiderados.contains(ITEM_CONFIGURACAO) && conf.itemConfiguracaoSet != null && conf.itemConfiguracaoSet.size() > 0) {
                boolean itemAtende = false;
                for (SrItemConfiguracao item : conf.itemConfiguracaoSet) {
                    if (filtro.getItemConfiguracaoFiltro() != null && item.getAtual().isPaiDeOuIgualA(filtro.getItemConfiguracaoFiltro())) {
                        itemAtende = true;
                        break;
                    }
                }
                if (!itemAtende)
                    return false;
            }

            if (!atributosDesconsiderados.contains(LISTA_PRIORIDADE) && conf.listaPrioridade != 0
                    && (filtro.listaPrioridade == 0 || (filtro.listaPrioridade != 0 && conf.listaPrioridade != filtro.listaPrioridade)))
                return false;

            if (!atributosDesconsiderados.contains(TIPO_ATRIBUTO) && conf.atributo != 0
                    && (filtro.atributo == 0 || (filtro.atributo != 0 && conf.atributo != filtro.atributo)))
                return false;

            if (!atributosDesconsiderados.contains(ATENDENTE) && conf.atendente != 0
                    && (filtro.atendente == 0 || (filtro.atendente != 0 && conf.atendente != filtro.atendente)))
                return false;

            if (!atributosDesconsiderados.contains(PRIORIDADE) && conf.getPrioridade() != null
                    && (filtro.getPrioridade() == null || (filtro.getPrioridade() != null && !conf.getPrioridade().equals(filtro.getPrioridade()))))
                return false;

        }
        return true;
    }

    public List<SrConfiguracaoCache> listarConfiguracoesAtivasPorFiltro(SrConfiguracao confFiltro, int atributoDesconsideradoFiltro[]) throws Exception {

        deduzFiltro(confFiltro);
        Set<Integer> atributosDesconsiderados = new LinkedHashSet<Integer>();
        for (int i = 0; i < atributoDesconsideradoFiltro.length; i++) {
            atributosDesconsiderados.add(atributoDesconsideradoFiltro[i]);
        }

        SortedSet<CpPerfil> perfis = null;
        if (confFiltro.isBuscarPorPerfis()) {
            perfis = consultarPerfisPorPessoaELotacao(confFiltro.getDpPessoa(), confFiltro.getLotacao(), null);
        }

        List<SrConfiguracaoCache> listaFinal = new ArrayList<>();
        
        try {
        	 if (confFiltro.getCpTipoConfiguracao() != null) {
                 TreeSet<CpConfiguracaoCache> lista = getListaPorTipo(confFiltro.getCpTipoConfiguracao());

                 if (lista != null) {
                	 CpConfiguracaoCache filtro = confFiltro.converterParaCache();
                     for (CpConfiguracaoCache cpConfiguracao : lista) {
						if (cpConfiguracao.hisDtFim == null && atendeExigencias(filtro, atributosDesconsiderados, cpConfiguracao, perfis)) {
                             listaFinal.add((SrConfiguracaoCache) cpConfiguracao);
                         }
                     }
                 }
             }
		} catch (Exception e) {
			throw new Exception(e);
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

            if (srConf.getAtendente() != null) {
                srConf.getAtendente().getLotacaoAtual();
                if (srConf.getAtendente().getOrgaoUsuario() != null)
                    srConf.getAtendente().getOrgaoUsuario().getSiglaOrgaoUsu();
            }

            if (srConf.getItemConfiguracaoSet() != null)
                for (SrItemConfiguracao i : srConf.getItemConfiguracaoSet()) {
                    i.getAtual();

                    if (i.getGestorSet() != null) {
                        i.getGestorSet().size();
                    }

                    if (i.getFatorMultiplicacaoSet() != null) {
                        i.getFatorMultiplicacaoSet().size();
                    }

                    for (SrItemConfiguracao hist : i.getMeuItemHistoricoSet()) {
                        hist.getAtual();
                    }
                }

            if (srConf.getAcoesSet() != null)
                for (SrAcao i : srConf.getAcoesSet())
                    i.getAtual();

            if (srConf.getAtributo() != null) {
            	srConf.getAtributo().getAtual();
            }

            if (srConf.getListaPrioridade() != null) {
            	srConf.getListaPrioridade().getListaAtual();
            }

            if (srConf.getPesquisaSatisfacao() != null)
                srConf.getPesquisaSatisfacao().getHisIdIni();

            if (srConf.getAcordo() != null)
                srConf.getAcordo().getAcordoAtual();

            if (srConf.getTipoPermissaoSet() != null) {
                for (SrTipoPermissaoLista perm : srConf.getTipoPermissaoSet()) {
                    //
                }
            }
        }
    }

//    @SuppressWarnings("unchecked")
//    public void atualizarConfiguracoesDoCache(List<SrConfiguracao> configs) throws Exception {
//        List<SrConfiguracao> evitarLazy = new ArrayList<SrConfiguracao>();
//        for (SrConfiguracao conf : configs) {
//            hashListas.get(conf.getCpTipoConfiguracao().getId()).remove(conf);
//            SrConfiguracao newConf = SrConfiguracao.AR.findById(conf.getIdConfiguracao());
//            hashListas.get(newConf.getCpTipoConfiguracao().getId()).add(newConf);
//            evitarLazy.add(newConf);
//        }
//        evitarLazy((List<CpConfiguracao>) (List<?>) evitarLazy);
//    }
    
}
