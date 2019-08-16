package br.gov.jfrj.siga.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "selected" })
public class ListaHierarquicaItem {
	public int level;
	public String text;
	public String searchText;
	public String keywords;
	public Long value;
	public boolean group;
	public boolean selected;

	public ListaHierarquicaItem() {
	}
	
	public ListaHierarquicaItem(int level, String text, String searchText, Long value, boolean group,
			boolean selected) {
		this.level = level;
		this.text = text;
		this.searchText = searchText;
		this.value = value;
		this.group = group;
		this.selected = selected;
	}
	
	public ListaHierarquicaItem(int level, String text, String searchText, String keywords, Long value, boolean group,
			boolean selected) {
		this.level = level;
		this.text = text;
		this.searchText = searchText;
		this.keywords = keywords;
		this.value = value;
		this.group = group;
		this.selected = selected;
	}

	public int getLevel() {
		return level;
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
}
