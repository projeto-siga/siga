package br.gov.jfrj.siga.sr.model;

public enum SrMeioComunicacao {

    TELEFONE(1, "Telefone"), 
    EMAIL(2, "Email"), 
    CONTATO_DIRETO(3, "Contato Direto"), 
    PANDION(4, "Mensageiro (Pandion, Lync, etc)"), 
    CHAT(5, "Chat"), 
    OFICIO(6, "Documento (Ofício, Memorando, etc)");

    private int idTipoContato;

    private String descrMeioComunicacao;

    private SrMeioComunicacao(int idTipoContato, String descrTipoContato) {
        this.setIdTipoContato(idTipoContato);
        this.setDescrMeioComunicacao(descrTipoContato);
    }

    public int getIdTipoContato() {
        return idTipoContato;
    }

    public void setIdTipoContato(int idTipoContato) {
        this.idTipoContato = idTipoContato;
    }

    public String getDescrMeioComunicacao() {
        return descrMeioComunicacao;
    }

    public void setDescrMeioComunicacao(String descrMeioComunicacao) {
        this.descrMeioComunicacao = descrMeioComunicacao;
    }

}
