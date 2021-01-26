package br.gov.jfrj.siga.sr.prop;

public class VarredorPropriedades {

	private String[] prefixo;
	private String nome;
	
	private int indiceAtual;
	
	public VarredorPropriedades(String[] prefixo, String nome) {
		this.prefixo = prefixo;
		this.nome = nome;
		this.indiceAtual = 0;
	}

	public boolean isVarreduraCompleta() {
		return indiceAtual > prefixo.length;
	}

	public String getNext() {
		if (isVarreduraCompleta()){
			return null;
		}
		String prop = new String();
		for (int j = indiceAtual; j < prefixo.length; j++) {
			prop += prefixo[j] + ".";
		}
		indiceAtual++;
		return prop + nome;
	}
	
	public void reset(){
		indiceAtual = 0;
	}
	
	public int getIndiceAtual() {
		return indiceAtual;
	}

	
}
