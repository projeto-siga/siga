package models.vo;

import models.SrObjetivoAtributo;

public class SrObjetivoAtributoVO extends AbstractSelecionavel {
	
	public SrObjetivoAtributoVO(Long id, String descricao) {
		super(id, descricao);
	}
	
	public static SrObjetivoAtributoVO createFrom(SrObjetivoAtributo objetivoAtributo) {
		if (objetivoAtributo != null)
			return new SrObjetivoAtributoVO(objetivoAtributo.idObjetivo, objetivoAtributo.descrObjetivo);
		else
			return null;
	}

}
