package br.gov.jfrj.siga.tp.util;

public enum Situacao {
	
	Inativo, Ativo;
	
	@Override
	public String toString() {
		return this.name();
	}
    
	public Situacao[] getValues() {
		return Situacao.values();
	}
}
