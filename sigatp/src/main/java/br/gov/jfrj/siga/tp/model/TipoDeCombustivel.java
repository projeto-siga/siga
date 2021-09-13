package br.gov.jfrj.siga.tp.model;

import java.util.ArrayList;
import java.util.List;

public enum TipoDeCombustivel {

	GASOLINA(0, "GASOLINA", true),
	ALCOOL(1, "ETANOL", true),
	DIESEL(2, "DIESEL", true), 
	GNV(3, "GNV", true),
	ALCOOL_E_GASOLINA(4, "FLEX", false),
	ALCOOL_E_GNV(5, "ETANOL/GNV", false),
	GASOLINA_E_GNV(6, "GASOLINA/GNV", false),
	DIESEL_E_GNV(7, "DIESEL/GNV", false),
	GASOLINA_E_ALCOOL_E_GNV(8, "GASOLINA/ETANOL/GNV", false),
	GASOLINA_ADITIVADA(9, "GASOLINA ADITIVADA", true),
	ALCOOL_ADITIVADO(10, "ETANOL ADITIVADO", true);
	
	private Integer indice;
	private String descricao;
	private boolean exibirNoAbastecimento;
	
	TipoDeCombustivel(Integer indice, String descricao, boolean exibirNoAbastecimento){
	    this.setIndice(indice);
		this.setDescricao(descricao);
		this.setExibirNoAbastecimento(exibirNoAbastecimento);
	}

	public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	private void setExibirNoAbastecimento(boolean exibirNoAbastecimento) {
		this.exibirNoAbastecimento = exibirNoAbastecimento;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public boolean getExibirNoAbastecimento() {
		return this.exibirNoAbastecimento;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	public static List<TipoDeCombustivel> tiposParaAbastecimento() {
		List<TipoDeCombustivel> retorno = new ArrayList<TipoDeCombustivel>();
		TipoDeCombustivel[] todos = TipoDeCombustivel.values();
		
		for (int i = 0; i < todos.length; i++) {
			TipoDeCombustivel tipo = todos[i];
			if(tipo.getExibirNoAbastecimento()) {
				retorno.add(tipo);
			}
		}
		
		return retorno;
	}
	
	public TipoDeCombustivel[] getValues() {
		return TipoDeCombustivel.values();
	}
	
}
