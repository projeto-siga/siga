package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorTipoTextoEnum implements IEnumWithId {
	DESATIVADA(1, "Desativada"),
	//
	OPCIONAL(2, "Opcional"),
	//
	OBRIGATORIA(3, "Obrigatoria");

	private final Integer id;
	private final String descricao;

	private CpMarcadorTipoTextoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoTextoEnum item : values()) {
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
