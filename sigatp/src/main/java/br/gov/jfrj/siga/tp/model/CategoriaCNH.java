package br.gov.jfrj.siga.tp.model;

public enum CategoriaCNH {
	
	A("A"), B("B"), AB("AB"), C("C"), D("D"), E("E"), AC("AC"), AD("AD"), AE("AE");
	
	private String categoria;
	
	CategoriaCNH(String categoria){
		this.setCategoria(categoria);
	}

	private void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getDescricao() {
		return this.categoria;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
