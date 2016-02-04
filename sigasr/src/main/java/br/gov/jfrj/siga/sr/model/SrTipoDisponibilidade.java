package br.gov.jfrj.siga.sr.model;

public enum SrTipoDisponibilidade {
    DISPONIVEL("Disponível", "/sigasr/images/icons/disponibilidade/disponivel.png"), INDISPONIVEL_PERMITE_SOLICITACOES("Indisponível com possibilidade de abertura de solicitações",
            "/sigasr/images/icons/disponibilidade/indisponivel_permite_solicitacoes.png"), INDISPONIVEL_BLOQUEIO_SOLICITACOES("Indisponível com bloqueio de solicitações",
            "/sigasr/images/icons/disponibilidade/indisponivel_bloqueio_solicitacoes.png"), NAO_UTILIZADO("Não Utilizado", "/sigasr/images/icons/disponibilidade/nao_utilizado.png"), DISPONIVEL_IRREGULAR(
            "Disponível com funcionamento irregular", "/sigasr/images/icons/disponibilidade/disponivel_irregular.png"), DISPONIVEL_AVISO("Disponível com mensagem de aviso",
            "/sigasr/images/icons/disponibilidade/disponivel_aviso.png"), NENHUM("Nenhum", "/sigasr/images/icons/disponibilidade/sem_disponibilidade.png");

    private String descricao;
    private String caminhoIcone;

    private SrTipoDisponibilidade(String descricao, String caminhoIcone) {
        this.setDescricao(descricao);
        this.setCaminhoIcone(caminhoIcone);
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCaminhoIcone() {
        return caminhoIcone;
    }

    public static String iconeVazio() {
        return SrTipoDisponibilidade.NENHUM.getCaminhoIcone();
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCaminhoIcone(String caminhoIcone) {
        this.caminhoIcone = caminhoIcone;
    }
}