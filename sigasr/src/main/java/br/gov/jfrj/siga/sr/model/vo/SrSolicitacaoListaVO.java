package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrPrioridadeSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.util.JsonUtil;
import br.gov.jfrj.siga.sr.util.SigaPlayUtil;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SrSolicitacaoListaVO {

    private static final String ULTIMA_MOVIMENTACAOFORMATADA = "ultimaMovimentacaoformatada";
    private static final String GT_CELULA_NOWRAP_SOLICITACAO_DADOS = "gt-celula-nowrap solicitacao-dados";
    private static Long LARGURA_COLUNA_CODIGO = 130L;
    private static Long LARGURA_COLUNA_REMOVER_PRIORIZAR = 55L;
    private static Long LARGURA_COLUNA_PRIORIDADE = 20L;

    private boolean podeOrdenar;
    private boolean podePriorizar;
    private boolean podeRemover;
    private boolean podeFiltrar;
    private boolean podePaginar;
    private List<SrSolicitacaoVO> itens;
    private List<ColunasVO> colunas;
    private List<ColunasVO> colunasDetalhamento;
    private String colunasTabelaJson;
    private String colunasDetalhamentoJson;

    public SrSolicitacaoListaVO() {
        this.setColunas(new ArrayList<ColunasVO>());
        this.setColunasDetalhamento(new ArrayList<ColunasVO>());
        this.setItens(new ArrayList<SrSolicitacaoVO>());
    }

    public static SrSolicitacaoListaVO fromFiltro(SrSolicitacaoFiltro filtro, boolean telaDeListas, String propriedade, boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
        List<SrSolicitacao> solicitacoes = filtro.buscar();
        SrSolicitacaoListaVO solicitacoesVO = new SrSolicitacaoListaVO();
        SrLista lista = null;

        if (filtro.getIdListaPrioridade() != null)
            lista = SrLista.AR.findById(filtro.getIdListaPrioridade());

        if (telaDeListas && lista != null) {
            solicitacoesVO.setPodePriorizar(lista.podePriorizar(lotaTitular, cadastrante));
            solicitacoesVO.setPodeOrdenar(false);
            solicitacoesVO.setPodeRemover(lista.podeRemover(lotaTitular, cadastrante));
            solicitacoesVO.setPodeFiltrar(false);
            solicitacoesVO.setPodePaginar(false);
        } else {
            solicitacoesVO.setPodeOrdenar(true);
            solicitacoesVO.setPodeFiltrar(true);
            solicitacoesVO.setPodePaginar(true);
        }

        solicitacoesVO.setColunas(solicitacoesVO.gerarColunasSolicitacao(telaDeListas, solicitacoesVO.isPodeRemover(), solicitacoesVO.isPodePriorizar()));
        solicitacoesVO.setColunasDetalhamento(solicitacoesVO.gerarColunasDetalhamentoSolicitacao(telaDeListas));
        solicitacoesVO.setColunasTabelaJson(createColunasTabelaJson(solicitacoesVO.getColunas(), telaDeListas));
        solicitacoesVO.setColunasDetalhamentoJson(createColunasDetalhamentoJson(solicitacoesVO.getColunasDetalhamento()));

        for (SrSolicitacao sol : solicitacoes) {

            if (lista != null) {
                SrPrioridadeSolicitacao prioridade = lista.getSrPrioridadeSolicitacao(sol);

                // só adiciona caso exista o vínculo entre a lista e a Solicitação
                if (prioridade != null)
                    solicitacoesVO.getItens().add(
                            new SrSolicitacaoVO(sol, lista, prioridade, propriedade, isPopup, lotaTitular, cadastrante, solicitacoesVO.isPodeRemover(), solicitacoesVO.isPodePriorizar()));
            } else
                solicitacoesVO.getItens().add(new SrSolicitacaoVO(sol, propriedade, isPopup, lotaTitular, cadastrante));
        }

        if (lista != null)
            SrSolicitacaoVO.ordenarPorPrioridade(solicitacoesVO.getItens());

        return solicitacoesVO;
    }

    public List<ColunasVO> gerarColunasSolicitacao(boolean telaDeListas, boolean podeRemover, boolean podePriorizar) {
        List<ColunasVO> colunasVO = new ArrayList<ColunasVO>();

        if (telaDeListas) {
            colunasVO.add(new ColunasVO("#", "prioridadeListaFormatada", "gt-celula-nowrap solicitacao-dados solicitacao-prioridade numero-solicitacao", LARGURA_COLUNA_PRIORIDADE));
            colunasVO.addAll(getColunasEmComum());
            colunasVO.add(new ColunasVO("Lota\u00e7\u00e3o", "lotaAtendenteFormatada", GT_CELULA_NOWRAP_SOLICITACAO_DADOS));
            colunasVO.add(new ColunasVO("\u00daltima Movimenta\u00e7\u00e3o", ULTIMA_MOVIMENTACAOFORMATADA, GT_CELULA_NOWRAP_SOLICITACAO_DADOS));

            if (podeRemover || podePriorizar)
                colunasVO.add(new ColunasVO("", "botaoRemoverPriorizar", "gt-celula-nowrap solicitacao-dados solicitacao-remover", LARGURA_COLUNA_REMOVER_PRIORIZAR));
        } else {
            colunasVO.add(new ColunasVO(SigaPlayUtil.botaoExpandir(), "botaoExpandir", "hide-sort-arrow bt-expandir-tabela gt-celula-nowrap details-control"));
            colunasVO.addAll(getColunasEmComum());
            colunasVO.add(new ColunasVO("Situa\u00e7\u00e3o", "marcadoresEmHtml", GT_CELULA_NOWRAP_SOLICITACAO_DADOS));
            colunasVO.add(new ColunasVO("\u00daltimo Andamento", ULTIMA_MOVIMENTACAOFORMATADA, GT_CELULA_NOWRAP_SOLICITACAO_DADOS));
            colunasVO.add(new ColunasVO("Prioridade", "prioridadeFormatada", GT_CELULA_NOWRAP_SOLICITACAO_DADOS));
        }

        return colunasVO;
    }

    private List<ColunasVO> getColunasEmComum() {
        List<ColunasVO> colunasVO = new ArrayList<ColunasVO>();
        colunasVO.add(new ColunasVO("C\u00f3digo", "codigoFormatado", "gt-celula-nowrap solicitacao-codigo", LARGURA_COLUNA_CODIGO));
        colunasVO.add(new ColunasVO("Teor", "teorFormatado", GT_CELULA_NOWRAP_SOLICITACAO_DADOS));
        colunasVO.add(new ColunasVO("Solicitante", "solicitanteFormatado", GT_CELULA_NOWRAP_SOLICITACAO_DADOS));
        colunasVO.add(new ColunasVO("Aberto", "dtRegString", GT_CELULA_NOWRAP_SOLICITACAO_DADOS));

        return colunasVO;
    }

    public List<ColunasVO> gerarColunasDetalhamentoSolicitacao(boolean telaDeListas) {
        List<ColunasVO> colunasDetalhamentoSolicitacao = new ArrayList<ColunasVO>();

        colunasDetalhamentoSolicitacao.add(new ColunasVO("Teor", "teorFormatado"));
        colunasDetalhamentoSolicitacao.add(new ColunasVO("Solicitante", "solicitanteFormatado"));
        colunasDetalhamentoSolicitacao.add(new ColunasVO("Prioridade", "prioridadeFormatada"));
        colunasDetalhamentoSolicitacao.add(new ColunasVO("Situa\u00e7\u00e3o", "marcadoresEmHtmlDetalhes"));
        colunasDetalhamentoSolicitacao.add(new ColunasVO("\u00datima Movimenta\u00e7\u00e3o", ULTIMA_MOVIMENTACAOFORMATADA));

        return colunasDetalhamentoSolicitacao;
    }

    public String toJson() {
        return JsonUtil.toJson(this).toString();
    }

    private static String createColunasTabelaJson(List<ColunasVO> colunas, boolean telaDeListas) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        List<ColunasVO> colunasResult = null;
        Gson gson = builder.create();

        // remove a primeira coluna, que será sempre o detalhamento ou posição na lista
        if (!colunas.isEmpty()) {
            if (telaDeListas)
                colunasResult = colunas.subList(1, colunas.size() - 1);
            else
                colunasResult = colunas.subList(1, colunas.size());
        }

        return gson.toJson(colunasResult);
    }

    private static String createColunasDetalhamentoJson(List<ColunasVO> colunasDetalhamento) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        return gson.toJson(colunasDetalhamento);
    }

    public boolean isPodeOrdenar() {
        return podeOrdenar;
    }

    public void setPodeOrdenar(boolean podeOrdenar) {
        this.podeOrdenar = podeOrdenar;
    }

    public boolean isPodePriorizar() {
        return podePriorizar;
    }

    public void setPodePriorizar(boolean podePriorizar) {
        this.podePriorizar = podePriorizar;
    }

    public boolean isPodeRemover() {
        return podeRemover;
    }

    public void setPodeRemover(boolean podeRemover) {
        this.podeRemover = podeRemover;
    }

    public boolean isPodeFiltrar() {
        return podeFiltrar;
    }

    public void setPodeFiltrar(boolean podeFiltrar) {
        this.podeFiltrar = podeFiltrar;
    }

    public boolean isPodePaginar() {
        return podePaginar;
    }

    public void setPodePaginar(boolean podePaginar) {
        this.podePaginar = podePaginar;
    }

    public List<SrSolicitacaoVO> getItens() {
        return itens;
    }

    public void setItens(List<SrSolicitacaoVO> itens) {
        this.itens = itens;
    }

    public List<ColunasVO> getColunas() {
        return colunas;
    }

    public void setColunas(List<ColunasVO> colunas) {
        this.colunas = colunas;
    }

    public List<ColunasVO> getColunasDetalhamento() {
        return colunasDetalhamento;
    }

    public void setColunasDetalhamento(List<ColunasVO> colunasDetalhamento) {
        this.colunasDetalhamento = colunasDetalhamento;
    }

    public String getColunasTabelaJson() {
        return colunasTabelaJson;
    }

    public void setColunasTabelaJson(String colunasTabelaJson) {
        this.colunasTabelaJson = colunasTabelaJson;
    }

    public String getColunasDetalhamentoJson() {
        return colunasDetalhamentoJson;
    }

    public void setColunasDetalhamentoJson(String colunasDetalhamentoJson) {
        this.colunasDetalhamentoJson = colunasDetalhamentoJson;
    }

}
