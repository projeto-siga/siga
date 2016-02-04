package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.sr.model.SrOperador;
import br.gov.jfrj.siga.sr.model.SrParametro;
import br.gov.jfrj.siga.sr.model.SrParametroAcordo;

public class SrParametroAcordoVO {

    private Long idParametroAcordo;
    private SrOperador operador;
    private String operadorNome;
    private Long valor;
    private CpUnidadeMedida unidadeMedida;
    private String unidadeMedidaPlural;
    private SrParametro parametro;
    private String parametroNome;
    private boolean ativo;

    public SrParametroAcordoVO(SrParametroAcordo parametroAcordo) throws Exception {
        this.setIdParametroAcordo(parametroAcordo.getIdParametroAcordo());
        this.setOperador(parametroAcordo.getOperador());
        this.setOperadorNome(parametroAcordo.getOperador() != null ? parametroAcordo.getOperador().getNome() : "");
        this.setValor(parametroAcordo.getValor());
        this.setUnidadeMedida(parametroAcordo.getUnidadeMedida());
        this.setUnidadeMedidaPlural(parametroAcordo.getUnidadeMedida() != null ? parametroAcordo.getUnidadeMedida().getPlural() : "");
        this.setParametro(parametroAcordo.getParametro());
        this.setParametroNome(parametroAcordo.getParametro() != null ? parametroAcordo.getParametro().getDescricao() : "");
        this.setAtivo(parametroAcordo.isAtivo());
    }

    public static SrParametroAcordoVO createFrom(SrParametroAcordo atributoAcordo) throws Exception {
        if (atributoAcordo != null)
            return new SrParametroAcordoVO(atributoAcordo);
        else
            return null;
    }

    public Long getIdParametroAcordo() {
        return idParametroAcordo;
    }

    public void setIdParametroAcordo(Long idAtributoAcordo) {
        this.idParametroAcordo = idAtributoAcordo;
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

    public SrParametro getParametro() {
        return parametro;
    }

    public void setParametro(SrParametro atributo) {
        this.parametro = atributo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

	public String getParametroNome() {
		return parametroNome;
	}

	public void setParametroNome(String parametroNome) {
		this.parametroNome = parametroNome;
	}
}
