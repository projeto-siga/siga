package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorTipoInteressadoEnum {
	ATENDENTE(0, "Atendente"),
	PESSOA(1, "Pessoa"),
	LOTACAO(2, "Lotacao"),
	PESSOA_OU_LOTACAO(3, "Pessoa ou lotação"),
	LOTACAO_OU_PESSOA(4, "Lotação ou pessoa");

	private final Integer id;
	private final String descricao;
	
	private CpMarcadorTipoInteressadoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() { 
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoInteressadoEnum item : values()){
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
