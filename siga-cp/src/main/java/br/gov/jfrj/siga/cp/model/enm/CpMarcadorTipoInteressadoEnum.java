package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorTipoInteressadoEnum implements IEnumWithId {
	ATENDENTE(1, "Atendente"),
	//
	PESSOA(2, SigaMessages.getMessage("usuario.pessoa")),
	//
	LOTACAO(3, SigaMessages.getMessage("usuario.lotacao")),
	//
	PESSOA_OU_LOTACAO(4, SigaMessages.getMessage("usuario.pessoa") + " ou " + SigaMessages.getMessage("usuario.lotacao")),
	//
	LOTACAO_OU_PESSOA(5, SigaMessages.getMessage("usuario.lotacao") + " ou " + SigaMessages.getMessage("usuario.pessoa"));

	private final Integer id;
	private final String descricao;

	private CpMarcadorTipoInteressadoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorTipoInteressadoEnum item : values()) {
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
