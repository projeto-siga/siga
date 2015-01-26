package models.vo;

import models.SrAtributoAcordo;
import models.SrOperador;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;

public class SrAtributoAcordoVO {
	
	public Long idAtributoAcordo;
	public SrOperador operador;
	public Integer valor;
	public CpUnidadeMedida unidadeMedida;
	public SrAtributoVO atributo;
	
	public SrAtributoAcordoVO(Long idAtributoAcordo, SrOperador operador, Integer valor, CpUnidadeMedida unidadeMedida, SrAtributoVO atributo) {
		this.idAtributoAcordo = idAtributoAcordo; 
		this.operador = operador;
		this.valor = valor;
		this.unidadeMedida = unidadeMedida;
		this.atributo = atributo;
	}
	
	public static SrAtributoAcordoVO createFrom(SrAtributoAcordo atributoAcordo) {
		return new SrAtributoAcordoVO(atributoAcordo.idAtributoAcordo, atributoAcordo.operador, 
				atributoAcordo.valor, atributoAcordo.unidadeMedida, SrAtributoVO.createFrom(atributoAcordo.atributo));
	}
}
