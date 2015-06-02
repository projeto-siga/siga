package br.gov.jfrj.siga.sr.model.vo;

import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrPrioridadeSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.util.SigaVraptorUtil;
import edu.emory.mathcs.backport.java.util.Collections;

public class SrSolicitacaoVO {

    private String botaoRemover;
    private String botaoPriorizar;

    private Long idSolicitacao;
    private String codigo = "";
    private String codigoFormatado = "";
    private String descricao = "";
    private String teorFormatado = "";

    private String solicitanteFormatado = "";
    private String dtUltimaMovimentacaoFormatada = "";
    private String ultimaMovimentacaoformatada = "";
    private String prioridadeFormatada = "";
    private String botaoExpandir = "+";
    private String marcadoresEmHtmlDetalhes = "";
    private String lotaAtendenteFormatada = "";
    private String prioridadeListaFormatada = "";
    private String botaoRemoverPriorizar = "";
    private String dtUltimaMovimentacaoString = "";
    private String nomeSolicitante = "";
    private String descricaoSolicitante = "";
    private String dtRegString = "";
    private SelecionavelVO lotaSolicitante;
    private SelecionavelVO lotaAtendente;
    private String ultimaMovimentacao = "";
    private String marcadoresEmHtml = "";

    private SrItemConfiguracaoVO itemConfiguracao;
    private SrPrioridadeSolicitacaoVO prioridadeSolicitacaoVO;

    public SrSolicitacaoVO(SrSolicitacao sol) throws Exception {
        this.setIdSolicitacao(sol.getId());
        this.setCodigo(sol.getCodigo());
        this.setDescricao(sol.getDescricao());
        this.setItemConfiguracao(sol.getItemAtual() != null ? sol.getItemAtual().toVO() : null);
        this.setNomeSolicitante(sol.getSolicitante() != null ? sol.getSolicitante().getNomeAbreviado() : "");
        this.setDescricaoSolicitante(sol.getSolicitante() != null ? sol.getSolicitante().getDescricaoIniciaisMaiusculas() : "");
        this.setLotaSolicitante(sol.getLotaSolicitante() != null ? SelecionavelVO.createFrom(sol.getLotaSolicitante()) : null);
        this.setDtRegString(sol.getSolicitacaoInicial().getDtRegString());
        this.setLotaAtendente(sol.getLotaAtendente() != null ? SelecionavelVO.createFrom(sol.getLotaAtendente()) : null);
        this.setUltimaMovimentacao(sol.getUltimaMovimentacaoQuePossuaDescricao() != null ? sol.getUltimaMovimentacaoQuePossuaDescricao().getDescrMovimentacao() : "");
        this.setDtUltimaMovimentacaoString(sol.getUltimaMovimentacao() != null ? sol.getUltimaMovimentacao().getDtIniMovDDMMYYYYHHMM() : "");

        this.setTeorFormatado(getTeorFormatado(sol.getItemAtual(), this.getDescricao()));
        this.setSolicitanteFormatado(getSolicitanteFormatado(this.getNomeSolicitante(), this.getDescricaoSolicitante(), this.getLotaSolicitante()));
        this.setDtUltimaMovimentacaoFormatada(getDtUltimaMovimentacaoFormatado(this.getDtUltimaMovimentacaoString()));
        this.setUltimaMovimentacaoformatada(SigaVraptorUtil.selecionado(this.getUltimaMovimentacao(), this.getUltimaMovimentacao()));

        this.setLotaAtendenteFormatada(this.getLotaAtendente() != null ? getLotacaoFormatada(this.getLotaAtendente()) : "");
        this.setPrioridadeFormatada(sol.getPrioridade() != null ? SigaVraptorUtil.selecionado(sol.getPrioridade().getDescPrioridade(), sol.getPrioridade().getDescPrioridade()) : "");
    }

    public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista, SrPrioridadeSolicitacao prioridadeSolicitacao, boolean podeRemover, boolean podePriorizar) throws Exception {
        this(sol);

        this.setPrioridadeSolicitacaoVO(prioridadeSolicitacao.toVO());
        this.setPrioridadeListaFormatada(SigaVraptorUtil.tagA(String.valueOf(prioridadeSolicitacao.numPosicao)));
        this.setPrioridadeFormatada(prioridadeSolicitacao.prioridade != null ? SigaVraptorUtil.selecionado(prioridadeSolicitacao.prioridade.getDescPrioridade(),
                prioridadeSolicitacao.prioridade.getDescPrioridade()) : "");

        if (podeRemover)
            this.botaoRemover = SigaVraptorUtil.botaoRemoverSolicitacao(this.getIdSolicitacao(), lista.getIdLista());

        if (podePriorizar)
            this.botaoPriorizar = SigaVraptorUtil.botaoPriorizarSolicitacao();

        if (podeRemover || podePriorizar)
            this.setBotaoRemoverPriorizar(this.botaoRemover + this.botaoPriorizar);
    }

    public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista, SrPrioridadeSolicitacao prioridadeSolicitacao, String nome, boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante,
            boolean podeRemover, boolean podePriorizar) throws Exception {
        this(sol, lista, prioridadeSolicitacao, podeRemover, podePriorizar);
        this.setCodigoFormatado(getCodigoFormatado(sol.getId(), sol.getCodigo(), nome, isPopup));
        this.setMarcadoresEmHtml(sol.getMarcadoresEmHtml(cadastrante, lotaTitular));
        this.setMarcadoresEmHtmlDetalhes(getMarcadoresEmHTMLDetalhes(this.marcadoresEmHtml, this.getDtUltimaMovimentacaoString()));
    }

    public SrSolicitacaoVO(SrSolicitacao sol, String nome, boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
        this(sol);
        this.setCodigoFormatado(getCodigoFormatado(sol.getId(), sol.getCodigo(), nome, isPopup));
        this.setMarcadoresEmHtml(sol.getMarcadoresEmHtml(cadastrante, lotaTitular));
        this.setMarcadoresEmHtmlDetalhes(getMarcadoresEmHTMLDetalhes(this.marcadoresEmHtml, this.getDtUltimaMovimentacaoString()));
    }

    private String getCodigoFormatado(Long id, String codigo, String nome, boolean isPopup) {
        StringBuilder sb = new StringBuilder();

        if (isPopup) {
            sb.append("<a href=\"javascript:opener.retorna_solicitacao");
            sb.append(nome + "('");
            sb.append(getIdSolicitacao() + "','" + codigo);
            sb.append("}'," + codigo + "');window.close()\">");
            sb.append(codigo + "</a>");
        } else {
            // TODO Alterar este link após migração para vRaptor
            sb.append("<a href=\"/sigasr/app/solicitacao/exibir/");
            sb.append(id + "\">");
            sb.append(codigo + "</a>");
        }

        return sb.toString();
    }

    private String getTeorFormatado(SrItemConfiguracao itemConfiguracao, String descricao) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>");
        sb.append(SigaVraptorUtil.descricaoItem(itemConfiguracao));
        sb.append(":</b>&nbsp;");
        sb.append(SigaVraptorUtil.selecionado(descricao, descricao));

        return sb.toString();
    }

    private String getSolicitanteFormatado(String nomeSolicitante, String descricaoSolicitante, SelecionavelVO lotaSolicitante) {
        StringBuilder sb = new StringBuilder();
        sb.append(SigaVraptorUtil.selecionado(nomeSolicitante, descricaoSolicitante));
        sb.append("&nbsp;");

        if (lotaSolicitante != null)
            sb.append(SigaVraptorUtil.selecionado(lotaSolicitante.getSigla(), lotaSolicitante.getDescricao()));

        return sb.toString();
    }

    private String getLotacaoFormatada(SelecionavelVO lotacao) {
        return new String("<b>" + lotacao.getSigla() + "</b>");
    }

    private String getDtUltimaMovimentacaoFormatado(String dtUltimaMovimentacao) {
        return new String("<b>" + SigaVraptorUtil.selecionado(dtUltimaMovimentacao, dtUltimaMovimentacao) + "</b>");
    }

    private String getMarcadoresEmHTMLDetalhes(String marcadoresEmHTML, String data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>");
        sb.append(marcadoresEmHTML);
        sb.append(" desde</b> ");
        sb.append(data);

        return sb.toString();
    }

    public void setMarcadoresEmHtml(String valor) {
        if (valor != null)
            this.marcadoresEmHtml = valor;
        else
            this.marcadoresEmHtml = "";
    }

    public static void ordenarPorPrioridade(List<SrSolicitacaoVO> lista) {
        SrSolicitacaoVOComparator comparator = new SrSolicitacaoVOComparator();
        Collections.sort(lista, comparator);
    }

    public Long getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(Long idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoFormatado() {
        return codigoFormatado;
    }

    public void setCodigoFormatado(String codigoFormatado) {
        this.codigoFormatado = codigoFormatado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTeorFormatado() {
        return teorFormatado;
    }

    public void setTeorFormatado(String teorFormatado) {
        this.teorFormatado = teorFormatado;
    }

    public String getSolicitanteFormatado() {
        return solicitanteFormatado;
    }

    public void setSolicitanteFormatado(String solicitanteFormatado) {
        this.solicitanteFormatado = solicitanteFormatado;
    }

    public String getDtUltimaMovimentacaoFormatada() {
        return dtUltimaMovimentacaoFormatada;
    }

    public void setDtUltimaMovimentacaoFormatada(String dtUltimaMovimentacaoFormatada) {
        this.dtUltimaMovimentacaoFormatada = dtUltimaMovimentacaoFormatada;
    }

    public String getUltimaMovimentacaoformatada() {
        return ultimaMovimentacaoformatada;
    }

    public void setUltimaMovimentacaoformatada(String ultimaMovimentacaoformatada) {
        this.ultimaMovimentacaoformatada = ultimaMovimentacaoformatada;
    }

    public String getPrioridadeFormatada() {
        return prioridadeFormatada;
    }

    public void setPrioridadeFormatada(String prioridadeFormatada) {
        this.prioridadeFormatada = prioridadeFormatada;
    }

    public String getBotaoExpandir() {
        return botaoExpandir;
    }

    public void setBotaoExpandir(String botaoExpandir) {
        this.botaoExpandir = botaoExpandir;
    }

    public String getMarcadoresEmHtmlDetalhes() {
        return marcadoresEmHtmlDetalhes;
    }

    public void setMarcadoresEmHtmlDetalhes(String marcadoresEmHtmlDetalhes) {
        this.marcadoresEmHtmlDetalhes = marcadoresEmHtmlDetalhes;
    }

    public String getLotaAtendenteFormatada() {
        return lotaAtendenteFormatada;
    }

    public void setLotaAtendenteFormatada(String lotaAtendenteFormatada) {
        this.lotaAtendenteFormatada = lotaAtendenteFormatada;
    }

    public String getPrioridadeListaFormatada() {
        return prioridadeListaFormatada;
    }

    public void setPrioridadeListaFormatada(String prioridadeListaFormatada) {
        this.prioridadeListaFormatada = prioridadeListaFormatada;
    }

    public String getBotaoRemoverPriorizar() {
        return botaoRemoverPriorizar;
    }

    public void setBotaoRemoverPriorizar(String botaoRemoverPriorizar) {
        this.botaoRemoverPriorizar = botaoRemoverPriorizar;
    }

    public String getDtUltimaMovimentacaoString() {
        return dtUltimaMovimentacaoString;
    }

    public void setDtUltimaMovimentacaoString(String dtUltimaMovimentacaoString) {
        this.dtUltimaMovimentacaoString = dtUltimaMovimentacaoString;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public String getDescricaoSolicitante() {
        return descricaoSolicitante;
    }

    public void setDescricaoSolicitante(String descricaoSolicitante) {
        this.descricaoSolicitante = descricaoSolicitante;
    }

    public String getDtRegString() {
        return dtRegString;
    }

    public void setDtRegString(String dtRegString) {
        this.dtRegString = dtRegString;
    }

    public SelecionavelVO getLotaSolicitante() {
        return lotaSolicitante;
    }

    public void setLotaSolicitante(SelecionavelVO lotaSolicitante) {
        this.lotaSolicitante = lotaSolicitante;
    }

    public SelecionavelVO getLotaAtendente() {
        return lotaAtendente;
    }

    public void setLotaAtendente(SelecionavelVO lotaAtendente) {
        this.lotaAtendente = lotaAtendente;
    }

    public String getUltimaMovimentacao() {
        return ultimaMovimentacao;
    }

    public void setUltimaMovimentacao(String ultimaMovimentacao) {
        this.ultimaMovimentacao = ultimaMovimentacao;
    }

    public SrItemConfiguracaoVO getItemConfiguracao() {
        return itemConfiguracao;
    }

    public void setItemConfiguracao(SrItemConfiguracaoVO itemConfiguracao) {
        this.itemConfiguracao = itemConfiguracao;
    }

    public SrPrioridadeSolicitacaoVO getPrioridadeSolicitacaoVO() {
        return prioridadeSolicitacaoVO;
    }

    public void setPrioridadeSolicitacaoVO(SrPrioridadeSolicitacaoVO prioridadeSolicitacaoVO) {
        this.prioridadeSolicitacaoVO = prioridadeSolicitacaoVO;
    }
}
