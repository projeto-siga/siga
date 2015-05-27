package br.gov.jfrj.siga.sr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class SrSolicitacaoFiltro extends SrSolicitacao {

    private static final long serialVersionUID = 1L;
    public static final Long QUALQUER_LISTA_OU_NENHUMA = -1L;
    public static final Long NENHUMA_LISTA = 0L;
    private static final String AND = " AND ";

    public boolean pesquisar = false;

    public String dtIni;

    public String dtFim;

    public CpMarcador situacao;

    public DpPessoa atendente;

    public SrAcordo acordo;

    public DpLotacao lotaAtendente;

    public Long idListaPrioridade;

    public boolean naoDesignados;

    public boolean naoSatisfatorios;

    public boolean apenasFechados;

    public Long idNovoAtributo;

    @SuppressWarnings("unchecked")
    public List<SrSolicitacao> buscar() throws Exception {
        // Edson: foi necessario separar em subquery porque o Oracle nao aceita
        // distinct em coluna CLOB em query contendo join
        String query = montarBusca("from SrSolicitacao sol where idSolicitacao in " + "(select distinct sol.idSolicitacao from SrSolicitacao sol ");

        List<SrSolicitacao> lista = ContextoPersistencia.em().createQuery(query).getResultList();

        List<SrSolicitacao> listaFinal = new ArrayList<SrSolicitacao>();

        if (naoSatisfatorios) {
            for (SrSolicitacao sol : lista)
                if (!sol.isAcordosSatisfeitos())
                    listaFinal.add(sol);
        } else
            listaFinal.addAll(lista);

        return listaFinal;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> buscarSimplificado() throws Exception {
        String query = montarBusca("select sol.idSolicitacao, sol.descrSolicitacao, sol.codigo, item.tituloItemConfiguracao" + " from SrSolicitacao sol inner join sol.itemConfiguracao as item ");

        List<Object[]> listaRetorno = ContextoPersistencia.em().createQuery(query).setMaxResults(10).getResultList();

        return listaRetorno;
    }

    private String montarBusca(String queryString) throws Exception {

        StringBuffer query = new StringBuffer(queryString);

        if (acordo != null && acordo.getIdAcordo() > 0L)
            query.append(" inner join sol.acordos acordo where acordo.hisIdIni = " + acordo.getHisIdIni() + " and ");
        else
            query.append(" where ");

        query.append(" sol.hisDtFim is null ");

        if (getCadastrante() != null)
            query.append(" and sol.cadastrante.idPessoaIni = " + getCadastrante().getIdInicial());
        if (getLotaTitular() != null)
            query.append(" and sol.lotaTitular.idLotacaoIni = " + getLotaTitular().getIdInicial());
        if (getSolicitante() != null)
            query.append(" and sol.solicitante.idPessoaIni = " + getSolicitante().getIdInicial());
        if (getLotaSolicitante() != null)
            query.append(" and sol.lotaSolicitante.idLotacaoIni = " + getLotaSolicitante().getIdInicial());
        if (getItemConfiguracao() != null && getItemConfiguracao().getIdItemConfiguracao() > 0L)
            query.append(" and sol.itemConfiguracao.itemInicial.idItemConfiguracao = " + getItemConfiguracao().getItemInicial().getIdItemConfiguracao());
        if (getAcao() != null && getAcao().getIdAcao() > 0L)
            query.append(" and sol.acao.acaoInicial.idAcao = " + getAcao().getAcaoInicial().getIdAcao());
        if (getPrioridade() != null && getPrioridade().getIdPrioridade() > 0L)
            query.append(" and sol.prioridade <= " + getPrioridade().ordinal());

        if (idListaPrioridade != null && !idListaPrioridade.equals(QUALQUER_LISTA_OU_NENHUMA)) {
            if (idListaPrioridade.equals(NENHUMA_LISTA)) {
                query.append(" and not exists (from SrPrioridadeSolicitacao prio where prio.solicitacao.solicitacaoInicial = sol.solicitacaoInicial) ");
            } else {
                SrLista lista = SrLista.AR.findById(idListaPrioridade);
                query.append(" and exists (from SrPrioridadeSolicitacao prio where prio.solicitacao.solicitacaoInicial.idSolicitacao = sol.solicitacaoInicial.idSolicitacao ");
                query.append(" and prio.lista.listaInicial.idLista = " + lista.getListaInicial().getId() + ") ");
            }
        }

        if (getDescrSolicitacao() != null && !getDescrSolicitacao().trim().equals("")) {
            for (String s : getDescrSolicitacao().split(" ")) {
                query.append(" and ( lower(sol.descrSolicitacao) like '%" + s.toLowerCase() + "%' ");
                query.append(" or sol in (select mov.solicitacao from SrMovimentacao mov");
                query.append(" where lower(mov.descrMovimentacao) like '%" + s.toLowerCase() + "%' )) ");
            }
        }

        final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

        if (dtIni != null)
            try {
                query.append(" and sol.dtReg >= to_date('" + dfHibernate.format(dfUsuario.parse(dtIni)) + "', 'yyyy-MM-dd') ");
            } catch (ParseException e) {
                //
            }

        if (dtFim != null)
            try {
                query.append(" and sol.dtReg <= to_date('" + dfHibernate.format(dfUsuario.parse(dtFim)) + " 23:59', 'yyyy-MM-dd HH24:mi') ");
            } catch (ParseException e) {
                //
            }

        StringBuffer subquery = new StringBuffer();

        if (situacao != null && situacao.getIdMarcador() != null && situacao.getIdMarcador() > 0)
            subquery.append(" and situacao.cpMarcador.idMarcador = " + situacao.getIdMarcador());
        if (atendente != null)
            subquery.append("and situacao.dpPessoaIni.idPessoa = " + atendente.getIdInicial());
        else if (lotaAtendente != null) {
            if (naoDesignados)
                subquery.append("and situacao.dpLotacaoIni.idLotacao = " + lotaAtendente.getIdInicial() + " and situacao.dpPessoaIni is null");
            else
                subquery.append("and situacao.dpLotacaoIni.idLotacao = " + lotaAtendente.getIdInicial());
        }

        if (subquery.length() > 0) {
            subquery.insert(0, " and exists (from SrMarca situacao where situacao.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
            subquery.append(" )");
            query.append(subquery);
        }

        montarQueryAtributos(query);

        if (apenasFechados) {
            query.append(" and not exists (from SrMovimentacao where tipoMov in (7,8) and solicitacao = sol.hisIdIni)");
        }

        return query.append(") order by sol.idSolicitacao desc").toString();
    }

    private void montarQueryAtributos(StringBuffer query) {
        Boolean existeFiltroPreenchido = Boolean.FALSE; // Indica se foi preenchido algum dos atributos informados na requisicao

        StringBuffer subqueryAtributo = new StringBuffer();
        if (meuAtributoSolicitacaoSet != null && meuAtributoSolicitacaoSet.size() > 0) {
            subqueryAtributo.append(" and (");

            for (SrAtributoSolicitacao att : meuAtributoSolicitacaoSet) {
                if (att.getValorAtributoSolicitacao() != null && !att.getValorAtributoSolicitacao().trim().isEmpty()) {
                    subqueryAtributo.append("(");
                    subqueryAtributo.append(" att.atributo.idAtributo = " + att.getAtributo().getIdAtributo());
                    subqueryAtributo.append(" and att.valorAtributoSolicitacao = '" + att.getValorAtributoSolicitacao() + "' ");

                    subqueryAtributo.append(")");
                    subqueryAtributo.append(AND);

                    existeFiltroPreenchido = Boolean.TRUE;
                }
            }
            subqueryAtributo.setLength(subqueryAtributo.length() - AND.length()); // remove o ultimo AND
            subqueryAtributo.append(" )");
        }
        if (existeFiltroPreenchido) {
            subqueryAtributo.insert(0, " and exists (from SrAtributoSolicitacao att where att.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
            subqueryAtributo.append(" )");
            query.append(subqueryAtributo);
        }
    }

    public List<SrAtributo> getTiposAtributosConsulta() {
        List<SrAtributo> tiposAtributosConsulta = new ArrayList<SrAtributo>();

        if (meuAtributoSolicitacaoSet != null) {
            for (SrAtributoSolicitacao srAtributo : meuAtributoSolicitacaoSet) {
                tiposAtributosConsulta.add(srAtributo.getAtributo());
            }
        }
        return tiposAtributosConsulta;
    }

    public List<SrAtributo> itensDisponiveis(List<SrAtributo> atributosDisponiveis, SrAtributo atributo) {
        ArrayList<SrAtributo> arrayList = new ArrayList<SrAtributo>(atributosDisponiveis);
        arrayList.add(atributo);

        Collections.sort(arrayList, new Comparator<SrAtributo>() {
            @Override
            public int compare(SrAtributo s0, SrAtributo s1) {
                if (s0.getNomeAtributo() == null && s1.getNomeAtributo() == null) {
                    return 0;
                } else if (s0.getNomeAtributo() == null) {
                    return 1;
                } else if (s1.getNomeAtributo() == null) {
                    return -1;
                }
                return s0.getNomeAtributo().compareTo(s1.getNomeAtributo());
            }
        });
        return arrayList;
    }

}
