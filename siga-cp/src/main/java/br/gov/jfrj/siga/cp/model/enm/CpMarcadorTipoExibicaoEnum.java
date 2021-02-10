package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorTipoExibicaoEnum implements IEnumWithId {
	IMEDIATA(1, "Imediata"),
	//
	DATA_LIMITE(2, "Na data limite"),
	//
	DATA_PLANEJADA(3, "Na data planejada"),
	//
	MENOR_DATA(4, "Na data que vier primeiro");

	private final Integer id;
	private final String descricao;

	private CpMarcadorTipoExibicaoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoExibicaoEnum item : values()) {
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
