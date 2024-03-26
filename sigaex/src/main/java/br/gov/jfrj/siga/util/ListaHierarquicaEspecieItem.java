package br.gov.jfrj.siga.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "selected" })
public class ListaHierarquicaEspecieItem {
	public int level;
	public String idInicial;
	public String text;
	public String searchText;
	public String keywords;
	public Long value;
	public boolean group;
	public boolean selected;
	public String especie;

	public ListaHierarquicaEspecieItem() {
	}
	
	public ListaHierarquicaEspecieItem(int level, String idInicial, String text, String searchText, Long value, boolean group,
			boolean selected, String especie) {
		this.level = level;
		this.idInicial = idInicial;
		this.text = text;
		this.searchText = searchText;
		this.value = value;
		this.group = group;
		this.selected = selected;
		this.especie = especie;
	}
	
	public ListaHierarquicaEspecieItem(int level, String idInicial, String text, String searchText, String keywords, Long value, boolean group,
			boolean selected, String especie) {
		this.level = level;
		this.idInicial = idInicial;
		this.text = text;
		this.searchText = searchText;
		this.keywords = keywords;
		this.value = value;
		this.group = group;
		this.selected = selected;
		this.especie = especie;
	}

	public int getLevel() {
		return level;
	}

	public String getIdInicial() {
		return idInicial;
	}

	public String getText() {
		return text;
	}

	public String getSearchText() {
		return searchText;
	}

	public Long getValue() {
		return value;
	}

	public boolean getGroup() {
		return group;
	}

	public boolean getSelected() {
		return selected;
	}
	
	public String getKeywords() {
		return keywords;
	}
	
	public String getEspecie() {
		return especie;
	}
}
