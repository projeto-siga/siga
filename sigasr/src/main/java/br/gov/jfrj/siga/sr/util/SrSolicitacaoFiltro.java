package br.gov.jfrj.siga.sr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcaoSelecao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAcordoSelecao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracaoSelecao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class SrSolicitacaoFiltro extends SrSolicitacao {

    private static final long serialVersionUID = 1L;
    public static final Long QUALQUER_LISTA_OU_NENHUMA = -1L;
    public static final Long NENHUMA_LISTA = 0L;
    private static final String AND = " AND ";

    private boolean pesquisar = false;

    private String dtIni;

    private String dtFim;

    private CpMarcador situacao;

    private DpPessoa atendente;

    private SrAcordo acordo;

    private DpLotacao lotaAtendente;

    private Long idListaPrioridade;

    private boolean naoDesignados;

    private boolean naoSatisfatorios;

    private boolean apenasFechados;

    private Long idNovoAtributo;

    private DpPessoaSelecao atendenteSel;
    private DpLotacaoSelecao lotaAtendenteSel;

    private DpPessoaSelecao cadastranteSel;
    private DpLotacaoSelecao lotaCadastranteSel;

    private DpPessoaSelecao solicitanteSel;
    private DpLotacaoSelecao lotaSolicitanteSel;

    private SrAcaoSelecao acaoSel;
    private SrAcordoSelecao acordoSel;
    private SrItemConfiguracaoSelecao itemConfiguracaoSel;

    public SrSolicitacaoFiltro() {
        super();
    }

    public void carregarSelecao() {
        if (atendenteSel != null) {
            this.setAtendente(atendenteSel.buscarObjeto());
            atendenteSel.carregarDadosParaView(this.getAtendente());
        }

        if (lotaAtendenteSel != null) {
            this.setLotaAtendente(lotaAtendenteSel.buscarObjeto());
            lotaAtendenteSel.carregarDadosParaView(this.getLotaAtendente());
        }

        if (cadastranteSel != null) {
            this.setCadastrante(cadastranteSel.buscarObjeto());
            cadastranteSel.carregarDadosParaView(this.getCadastrante());
        }

        if (lotaCadastranteSel != null) {
            this.setLotaCadastrante(lotaCadastranteSel.buscarObjeto());
            this.lotaCadastranteSel.carregarDadosParaView(this.getLotaCadastrante());
        }

        if (solicitanteSel != null) {
            this.setSolicitante(solicitanteSel.buscarObjeto());
            solicitanteSel.carregarDadosParaView(this.getSolicitante());
        }

        if (lotaSolicitanteSel != null) {
            this.setLotaSolicitante(lotaSolicitanteSel.buscarObjeto());
            lotaSolicitanteSel.carregarDadosParaView(this.getLotaSolicitante());
        }

        if (acaoSel != null) {
            this.setAcao(acaoSel.getObjeto());
            acaoSel.carregarDadosParaView(this.getAcao());
        }

        if (acordoSel != null) {
            this.setAcordo(acordoSel.buscarObjeto());
            acordoSel.carregarDadosParaView(this.getAcordo());
        }

        if (situacao != null && situacao.getIdMarcador() != null) {
            this.setSituacao(CpMarcador.AR.findById(situacao.getIdMarcador()));
        }

        if (itemConfiguracaoSel != null) {
            this.setItemConfiguracao(getItemConfiguracaoSel().buscarObjeto());
            itemConfiguracaoSel.carregarDadosParaView(this.getItemConfiguracao());
        }
    }

    @SuppressWarnings("unchecked")
    public List<SrSolicitacao> buscar() throws Exception {
        // Edson: foi necessario separar em subquery porque o Oracle nao aceita
        // distinct em coluna CLOB em query contendo join
        String query = montarBusca("from SrSolicitacao sol where idSolicitacao in " + "(select distinct sol.idSolicitacao from SrSolicitacao sol ");

        List<SrSolicitacao> lista = ContextoPersistencia.em().createQuery(query).getResultList();

        List<SrSolicitacao> listaFinal = new ArrayList<SrSolicitacao>();

        if (isNaoSatisfatorios()) {
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
        return ContextoPersistencia.em().createQuery(query).setMaxResults(10).getResultList();
    }

    private String montarBusca(String queryString) throws Exception {

        StringBuilder query = new StringBuilder(queryString);

        if (Filtros.deveAdicionar(getAcordo()))
            query.append(" inner join sol.acordos acordo where acordo.hisIdIni = " + getAcordo().getHisIdIni() + " and ");
        else
            query.append(" where ");

        query.append(" sol.hisDtFim is null ");

        if (Filtros.deveAdicionar(getCadastrante()))
            query.append(" and sol.cadastrante.idPessoaIni = " + getCadastrante().getIdInicial());
        if (Filtros.deveAdicionar(getLotaTitular()))
            query.append(" and sol.lotaTitular.idLotacaoIni = " + getLotaTitular().getIdInicial());
        if (Filtros.deveAdicionar(getSolicitante()))
            query.append(" and sol.solicitante.idPessoaIni = " + getSolicitante().getIdInicial());
        if (Filtros.deveAdicionar(getLotaSolicitante()))
            query.append(" and sol.lotaSolicitante.idLotacaoIni = " + getLotaSolicitante().getIdInicial());
        if (Filtros.deveAdicionar(getItemConfiguracao()))
            query.append(" and sol.itemConfiguracao.itemInicial.idItemConfiguracao = " + getItemConfiguracao().getItemInicial().getIdItemConfiguracao());
        if (Filtros.deveAdicionar(getAcao()))
            query.append(" and sol.acao.acaoInicial.idAcao = " + getAcao().getAcaoInicial().getIdAcao());
        if (getPrioridade() != null && getPrioridade().getIdPrioridade() > 0L)
            query.append(" and sol.prioridade <= " + getPrioridade().ordinal());

        if (getIdListaPrioridade() != null && !getIdListaPrioridade().equals(QUALQUER_LISTA_OU_NENHUMA)) {
            if (getIdListaPrioridade().equals(NENHUMA_LISTA)) {
                query.append(" and not exists (from SrPrioridadeSolicitacao prio where prio.solicitacao.solicitacaoInicial = sol.solicitacaoInicial) ");
            } else {
                SrLista lista = SrLista.AR.findById(getIdListaPrioridade());
                query.append(" and exists (from SrPrioridadeSolicitacao prio where prio.solicitacao.solicitacaoInicial.idSolicitacao = sol.solicitacaoInicial.idSolicitacao ");
                query.append(" and prio.lista.listaInicial.idLista = " + lista.getListaInicial().getId() + ") ");
            }
        }

        if (getDescrSolicitacao() != null && !"".equals(getDescrSolicitacao().trim())) {
            for (String s : getDescrSolicitacao().split(" ")) {
                query.append(" and ( lower(sol.descrSolicitacao) like '%" + s.toLowerCase() + "%' ");
                query.append(" or sol in (select mov.solicitacao from SrMovimentacao mov");
                query.append(" where lower(mov.descrMovimentacao) like '%" + s.toLowerCase() + "%' )) ");
            }
        }

        final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

        if (getDtIni() != null)
            try {
                query.append(" and sol.dtReg >= to_date('" + dfHibernate.format(dfUsuario.parse(getDtIni())) + "', 'yyyy-MM-dd') ");
            } catch (ParseException e) {
                //
            }

        if (getDtFim() != null)
            try {
                query.append(" and sol.dtReg <= to_date('" + dfHibernate.format(dfUsuario.parse(getDtFim())) + " 23:59', 'yyyy-MM-dd HH24:mi') ");
            } catch (ParseException e) {
                //
            }

        StringBuilder subquery = new StringBuilder();

        if (getSituacao() != null && getSituacao().getIdMarcador() != null && getSituacao().getIdMarcador() > 0)
            subquery.append(" and situacao.cpMarcador.idMarcador = " + getSituacao().getIdMarcador());
        if (Filtros.deveAdicionar(getAtendente()))
            subquery.append("and situacao.dpPessoaIni.idPessoa = " + getAtendente().getIdInicial());
        else if (Filtros.deveAdicionar(getLotaAtendente())) {
            if (isNaoDesignados())
                subquery.append("and situacao.dpLotacaoIni.idLotacao = " + getLotaAtendente().getIdInicial() + " and situacao.dpPessoaIni is null");
            else
                subquery.append("and situacao.dpLotacaoIni.idLotacao = " + getLotaAtendente().getIdInicial());
        }

        if (subquery.length() > 0) {
            subquery.insert(0, " and exists (from SrMarca situacao where situacao.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
            subquery.append(" )");
            query.append(subquery);
        }

        montarQueryAtributos(query);

        if (isApenasFechados()) {
            query.append(" and not exists (from SrMovimentacao where tipoMov in (7,8) and solicitacao = sol.hisIdIni)");
        }

        return query.append(") order by sol.idSolicitacao desc").toString();
    }

    private void montarQueryAtributos(StringBuilder query) {
        Boolean existeFiltroPreenchido = Boolean.FALSE; // Indica se foi preenchido algum dos atributos informados na requisicao

        StringBuilder subqueryAtributo = new StringBuilder();
        if (meuAtributoSolicitacaoSet != null && !meuAtributoSolicitacaoSet.isEmpty()) {
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
        List<SrAtributo> arrayList = new ArrayList<SrAtributo>(atributosDisponiveis);
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

    public boolean isPesquisar() {
        return pesquisar;
    }

    public void setPesquisar(boolean pesquisar) {
        this.pesquisar = pesquisar;
    }

    public String getDtIni() {
        return dtIni;
    }

    public void setDtIni(String dtIni) {
        this.dtIni = dtIni;
    }

    public String getDtFim() {
        return dtFim;
    }

    public void setDtFim(String dtFim) {
        this.dtFim = dtFim;
    }

    public CpMarcador getSituacao() {
        return situacao;
    }

    public void setSituacao(CpMarcador situacao) {
        this.situacao = situacao;
    }

    @Override
    public DpPessoa getAtendente() {
        return atendente;
    }

    public void setAtendente(DpPessoa atendente) {
        this.atendente = atendente;
    }

    public SrAcordo getAcordo() {
        return acordo;
    }

    public void setAcordo(SrAcordo acordo) {
        this.acordo = acordo;
    }

    @Override
    public DpLotacao getLotaAtendente() {
        return lotaAtendente;
    }

    public void setLotaAtendente(DpLotacao lotaAtendente) {
        this.lotaAtendente = lotaAtendente;
    }

    public Long getIdListaPrioridade() {
        return idListaPrioridade;
    }

    public void setIdListaPrioridade(Long idListaPrioridade) {
        this.idListaPrioridade = idListaPrioridade;
    }

    public boolean isNaoDesignados() {
        return naoDesignados;
    }

    public void setNaoDesignados(boolean naoDesignados) {
        this.naoDesignados = naoDesignados;
    }

    public boolean isNaoSatisfatorios() {
        return naoSatisfatorios;
    }

    public void setNaoSatisfatorios(boolean naoSatisfatorios) {
        this.naoSatisfatorios = naoSatisfatorios;
    }

    public boolean isApenasFechados() {
        return apenasFechados;
    }

    public void setApenasFechados(boolean apenasFechados) {
        this.apenasFechados = apenasFechados;
    }

    public Long getIdNovoAtributo() {
        return idNovoAtributo;
    }

    public void setIdNovoAtributo(Long idNovoAtributo) {
        this.idNovoAtributo = idNovoAtributo;
    }

    public DpPessoaSelecao getAtendenteSel() {
        return atendenteSel;
    }

    public void setAtendenteSel(DpPessoaSelecao atendenteSel) {
        this.atendenteSel = atendenteSel;
    }

    public DpLotacaoSelecao getLotaAtendenteSel() {
        return lotaAtendenteSel;
    }

    public void setLotaAtendenteSel(DpLotacaoSelecao lotaAtendenteSel) {
        this.lotaAtendenteSel = lotaAtendenteSel;
    }

    public DpPessoaSelecao getCadastranteSel() {
        return cadastranteSel;
    }

    public void setCadastranteSel(DpPessoaSelecao cadastranteSel) {
        this.cadastranteSel = cadastranteSel;
    }

    public DpLotacaoSelecao getLotaCadastranteSel() {
        return lotaCadastranteSel;
    }

    public void setLotaCadastranteSel(DpLotacaoSelecao lotacadastranteSel) {
        this.lotaCadastranteSel = lotacadastranteSel;
    }

    public DpPessoaSelecao getSolicitanteSel() {
        return solicitanteSel;
    }

    public void setSolicitanteSel(DpPessoaSelecao solicitanteSel) {
        this.solicitanteSel = solicitanteSel;
    }

    public DpLotacaoSelecao getLotaSolicitanteSel() {
        return lotaSolicitanteSel;
    }

    public void setLotaSolicitanteSel(DpLotacaoSelecao lotaSolicitanteSel) {
        this.lotaSolicitanteSel = lotaSolicitanteSel;
    }

    public SrAcaoSelecao getAcaoSel() {
        return acaoSel;
    }

    public void setAcaoSel(SrAcaoSelecao acaoSel) {
        this.acaoSel = acaoSel;
    }

    public SrAcordoSelecao getAcordoSel() {
        return acordoSel;
    }

    public void setAcordoSel(SrAcordoSelecao acordoSel) {
        this.acordoSel = acordoSel;
    }

    public SrItemConfiguracaoSelecao getItemConfiguracaoSel() {
        return itemConfiguracaoSel;
    }

    public void setItemConfiguracaoSel(SrItemConfiguracaoSelecao itemConfiguracaoSel) {
        this.itemConfiguracaoSel = itemConfiguracaoSel;
    }

}
