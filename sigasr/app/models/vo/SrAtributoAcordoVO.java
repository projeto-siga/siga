package models.vo;

import models.SrAtributoAcordo;
import models.SrOperador;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;

public class SrAtributoAcordoVO {
	
	public Long idAtributoAcordo;
	public SrOperador operador;
	public String operadorNome;
	public Long valor;
	public CpUnidadeMedida unidadeMedida;
	public String unidadeMedidaPlural;
	public SrAtributoVO atributo;
	public boolean ativo;
	
	public SrAtributoAcordoVO(SrAtributoAcordo atributoAcordo) {
		this.idAtributoAcordo = atributoAcordo.idAtributoAcordo; 
		this.operador = atributoAcordo.operador;
		this.operadorNome = atributoAcordo.operador != null ? atributoAcordo.operador.nome : "";
		this.valor = atributoAcordo.valor;
		this.unidadeMedida = atributoAcordo.unidadeMedida;
		this.unidadeMedidaPlural = atributoAcordo.unidadeMedida != null ? atributoAcordo.unidadeMedida.getPlural() : "";
		this.atributo = SrAtributoVO.createFrom(atributoAcordo.atributo, false);
		this.ativo = atributoAcordo.isAtivo();
	}
	
	public static SrAtributoAcordoVO createFrom(SrAtributoAcordo atributoAcordo) {
		if (atributoAcordo != null)
			return new SrAtributoAcordoVO(atributoAcordo);
		else
			return null;
	}
}
