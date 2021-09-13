package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorTipoAplicacaoEnum implements IEnumWithId {
	GERAL(1, "Geral"),
	//
	TODAS_AS_VIAS_OU_ULTIMO_VOLUME(2, "Em todas as vias"),
	//
	VIA_ESPECIFICA_OU_ULTIMO_VOLUME(3, "Via específica ou último volume"),;

	private final Integer id;
	private final String descricao;

	private CpMarcadorTipoAplicacaoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoAplicacaoEnum item : values()) {
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