package br.gov.jfrj.siga.cp.model.enm;

public enum EntidadeEnum {
    GI_PESSOA(ModuloEnum.GI),
    GI_LOTACAO(ModuloEnum.GI),
    EX_DOCUMENTO(ModuloEnum.EX),
    WF_PROCEDIMENTO(ModuloEnum.WF),
    WF_DIAGRAMA(ModuloEnum.WF),
    SR_SOLICITACAO(ModuloEnum.SR),
    GC_INFORMACAO(ModuloEnum.CG);

    private ModuloEnum modulo;

    EntidadeEnum(ModuloEnum modulo) {
        this.modulo = modulo;
    }

    public ModuloEnum getModulo() {
        return modulo;
    }
}
