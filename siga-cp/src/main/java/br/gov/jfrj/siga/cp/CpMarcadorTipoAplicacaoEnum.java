package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorTipoAplicacaoEnum {
	GERAL(0, "Geral"),
	EM_TODAS_VIAS(1, "Em todas as vias"),
	VIA_ESPECIFICA_OU_ULTIMO_VOLUME(2, "Via específica ou último volume");

	private final Integer id;
	private final String descricao;
	
	private CpMarcadorTipoAplicacaoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() { 
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoAplicacaoEnum item : values()){
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