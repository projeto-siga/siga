package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorTipoJustificativaEnum {
	DESATIVADA(0, "Desativada"),
	OPCIONAL(1, "Opcional"),
	OBRIGATORIA(2, "Obrigatoria");

	private final Integer id;
	private final String descricao;
	
	private CpMarcadorTipoJustificativaEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() { 
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoJustificativaEnum item : values()){
		    list.add(item.descricao);
		}
	    return list;
	}
	public Integer getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
}
