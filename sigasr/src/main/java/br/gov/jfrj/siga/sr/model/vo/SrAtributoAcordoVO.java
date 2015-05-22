package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.sr.model.SrAtributoAcordo;
import br.gov.jfrj.siga.sr.model.SrOperador;

public class SrAtributoAcordoVO {

    private Long idAtributoAcordo;
    private SrOperador operador;
    private String operadorNome;
    private Long valor;
    private CpUnidadeMedida unidadeMedida;
    private String unidadeMedidaPlural;
    private SrAtributoVO atributo;
    private boolean ativo;

    public SrAtributoAcordoVO(SrAtributoAcordo atributoAcordo) throws Exception {
        this.setIdAtributoAcordo(atributoAcordo.getIdAtributoAcordo());
        this.setOperador(atributoAcordo.getOperador());
        this.setOperadorNome(atributoAcordo.getOperador() != null ? atributoAcordo.getOperador().getNome() : "");
        this.setValor(atributoAcordo.getValor());
        this.setUnidadeMedida(atributoAcordo.getUnidadeMedida());
        this.setUnidadeMedidaPlural(atributoAcordo.getUnidadeMedida() != null ? atributoAcordo.getUnidadeMedida().getPlural() : "");
        this.setAtributo(SrAtributoVO.createFrom(atributoAcordo.getAtributo(), false));
        this.setAtivo(atributoAcordo.isAtivo());
    }

    public static SrAtributoAcordoVO createFrom(SrAtributoAcordo atributoAcordo) throws Exception {
        if (atributoAcordo != null)
            return new SrAtributoAcordoVO(atributoAcordo);
        else
            return null;
    }

    public Long getIdAtributoAcordo() {
        return idAtributoAcordo;
    }

    public void setIdAtributoAcordo(Long idAtributoAcordo) {
        this.idAtributoAcordo = idAtributoAcordo;
    }

    public SrOperador getOperador() {
        return operador;
    }

    public void setOperador(SrOperador operador) {
        this.operador = operador;
    }

    public String getOperadorNome() {
        return operadorNome;
    }

    public void setOperadorNome(String operadorNome) {
        this.operadorNome = operadorNome;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public CpUnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(CpUnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getUnidadeMedidaPlural() {
        return unidadeMedidaPlural;
    }

    public void setUnidadeMedidaPlural(String unidadeMedidaPlural) {
        this.unidadeMedidaPlural = unidadeMedidaPlural;
    }

    public SrAtributoVO getAtributo() {
        return atributo;
    }

    public void setAtributo(SrAtributoVO atributo) {
        this.atributo = atributo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
