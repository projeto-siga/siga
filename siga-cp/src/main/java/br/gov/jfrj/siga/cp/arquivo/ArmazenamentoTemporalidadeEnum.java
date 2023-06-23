package br.gov.jfrj.siga.cp.arquivo;

public enum ArmazenamentoTemporalidadeEnum {
    TEMPORARIO("tmp", true),
    MANTER_POR_30_ANOS(null, false);

    public static final String SEPARADOR = "|";
    public static final ArmazenamentoTemporalidadeEnum DEFAULT = localizarPorIdentificador(null);

    String identificador;
    boolean podeApagar;

    ArmazenamentoTemporalidadeEnum(String identificador, boolean podeApagar) {
        this.identificador = identificador;
        this.podeApagar = podeApagar;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public boolean isPodeApagar() {
        return this.podeApagar;
    }

    public static ArmazenamentoTemporalidadeEnum localizarPorIdentificador(String identificador) {
        for (ArmazenamentoTemporalidadeEnum tempo : values())
            if ((identificador == null && tempo.identificador == null)
                    || (identificador != null && identificador.equals(tempo.identificador)))
                return tempo;
        return null;
    }

}
