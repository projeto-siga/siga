package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpTipoMarcadorEnum implements IEnumWithId {
	TIPO_MARCADOR_SISTEMA(1, "Sistema"),
	//
	TIPO_MARCADOR_GERAL(2, "Geral"),
	//
//	TIPO_MARCADOR_LOTACAO_E_SUBLOTACOES(3, "Lotação e Sublotações"),
	//
	TIPO_MARCADOR_LOTACAO(4, "Lotação");
	//
//	TIPO_MARCADOR_PESSOA(5, "Pessoa"),
	//
//	TIPO_MARCADOR_TAXONOMIA_ADMINISTRADA(6, "Taxonomia Administrada");

	private final Integer id;
	private final String descricao;

	private CpTipoMarcadorEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpTipoMarcadorEnum item : values()) {
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