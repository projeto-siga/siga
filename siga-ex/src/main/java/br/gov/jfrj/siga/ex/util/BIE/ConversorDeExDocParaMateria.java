package br.gov.jfrj.siga.ex.util.BIE;

import java.util.List;

import br.gov.jfrj.siga.ex.ExDocumento;

public abstract class ConversorDeExDocParaMateria {
	
	private TipoMateria tipoMateria;
	
	public ConversorDeExDocParaMateria(){
		
	}
	 
	public abstract boolean canHandle(ExDocumento doc);
	
	public abstract List<Materia> converter(ExDocumento doc);

	public TipoMateria getTipoMateria() {
		return tipoMateria;
	}

	public void setTipoMateria(TipoMateria tipoMateria) {
		this.tipoMateria = tipoMateria;
	}
}
