package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorTipoDataPlanejadaEnum {
	TIPO_DATA_PLANEJADA_DESATIVADA(1, "Desativada"),
	TIPO_DATA_PLANEJADA_OPCIONAL(2, "Opcional"),
	TIPO_DATA_PLANEJADA_OBRIGATORIA(3, "Obrigatoria");
	
	private final Integer id;
	private final String descricao;

	private CpMarcadorTipoDataPlanejadaEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoDataPlanejadaEnum item : values()){
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