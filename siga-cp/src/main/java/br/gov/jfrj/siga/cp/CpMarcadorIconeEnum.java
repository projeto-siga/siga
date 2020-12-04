package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorIconeEnum implements IEnumWithId {
	ICONE_ETIQUETA(1, "Etiqueta", "fas fa-tag"), 
	ICONE_BOMBA(2, "Bomba", "fas fa-bomb"), 
	ICONE_SEGURANCA(3, "Segurança", "fas fa-user-shield"), 
	ICONE_EXCLAMACAO(4, "Aviso Exclamação", "fas fa-exclamation-triangle"), 
	ICONE_CAVEIRA(5, "Caveira", "fas fa-skull-crossbones"), 
	ICONE_ESPIAO(6, "Espião", "fas fa-user-secret"), 
	ICONE_GRADUACAO(7, "Graduação", "fas fa-user-graduate"),
	ICONE_PESSOA(8, "Pessoa", "fas fa-user"),
	ICONE_PROIBIDO(9, "Proibido", "fas fa-ban"),
	ICONE_SMILE(10, "Smile", "far fa-smile");

	private final Integer id;
	private final String descricao;
	private final String codigoFontAwesome;

	private CpMarcadorIconeEnum(Integer id, String descricao, String codigoFontAwesome) {
		this.id = id;
		this.descricao = descricao;
		this.codigoFontAwesome = codigoFontAwesome;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorIconeEnum item : values()) {
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

	public String getCodigoFontAwesome() {
		return codigoFontAwesome;
	}
}