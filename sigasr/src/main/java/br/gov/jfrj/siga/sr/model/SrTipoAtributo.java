package br.gov.jfrj.siga.sr.model;

public enum SrTipoAtributo {

    TEXTO(1, "Texto"), DATA(2, "Data"), NUM_INTEIRO(3, "Número Inteiro"), NUM_DECIMAL(4, "Número com duas casas decimais"), HORA(5, "Hora"), VL_PRE_DEFINIDO(6, "Valores pré-definidos"), TEXT_AREA(7,
            "TextArea");

    private int idTipoAtributo;

    private String descrTipoAtributo;

    private SrTipoAtributo(int idTipoAtributo, String descrTipoAtributo) {
        this.setIdTipoAtributo(idTipoAtributo);
        this.setDescrTipoAtributo(descrTipoAtributo);
    }

    public int getIdTipoAtributo() {
        return idTipoAtributo;
    }

    public void setIdTipoAtributo(int idTipoAtributo) {
        this.idTipoAtributo = idTipoAtributo;
    }

    public String getDescrTipoAtributo() {
        return descrTipoAtributo;
    }

    public void setDescrTipoAtributo(String descrTipoAtributo) {
        this.descrTipoAtributo = descrTipoAtributo;
    }
}
