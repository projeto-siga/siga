package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorIconesEnum {
	ICONE_ETIQUETA(0, "Etiqueta", "fas fa-tag"), 
	ICONE_BOMBA(1, "Bomba", "fas fa-bomb"), 
	ICONE_SEGURANCA(2, "Segurança", "fas fa-user-shield"), 
	ICONE_EXCLAMACAO(3, "Aviso Exclamação", "fas fa-exclamation-triangle"), 
	ICONE_CAVEIRA(4, "Caveira", "fas fa-skull-crossbones"), 
	ICONE_ESPIAO(5, "Espião", "fas fa-user-secret"), 
	ICONE_GRADUACAO(6, "Graduação", "fas fa-user-graduate"),
	ICONE_PESSOA(7, "Pessoa", "fas fa-user"),
	ICONE_PROIBIDO(8, "Proibido", "fas fa-ban"),
	ICONE_SMILE(9, "Smile", "far fa-smile");

	private final Integer id;
	private final String descricao;
	private final String codigoFontAwesome;

	private CpMarcadorIconesEnum(Integer id, String descricao, String codigoFontAwesome) {
		this.id = id;
		this.descricao = descricao;
		this.codigoFontAwesome = codigoFontAwesome;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorIconesEnum item : values()) {
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