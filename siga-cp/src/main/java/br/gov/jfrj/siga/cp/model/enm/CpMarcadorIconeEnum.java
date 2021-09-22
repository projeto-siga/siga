package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorIconeEnum implements IEnumWithId {
	ICONE_INFORMACAO(25, "Informação", "fas fa-info-circle"),
	ICONE_ETIQUETA(1, "Etiqueta", "fas fa-tag"), 
	ICONE_BOMBA(2, "Bomba", "fas fa-bomb"), 
	ICONE_SEGURANCA(3, "Segurança", "fas fa-user-shield"), 
	ICONE_EXCLAMACAO(4, "Aviso Exclamação", "fas fa-exclamation-triangle"), 
	ICONE_CAVEIRA(5, "Caveira", "fas fa-skull-crossbones"), 
	ICONE_ESPIAO(6, "Espião", "fas fa-user-secret"), 
	ICONE_GRADUACAO(7, "Graduação", "fas fa-user-graduate"),
	ICONE_PESSOA(8, "Pessoa", "fas fa-user"),
	ICONE_PROIBIDO(9, "Proibido", "fas fa-ban"),
	ICONE_ACESSIVEL(10, "Acessivel", "fab fa-accessible-icon"),
	ICONE_IDENTIDADE(11, "Identidade", "fas fa-address-card"),
	ICONE_AMBULANCIA(12, "Ambulância", "fas fa-ambulance"),
	ICONE_BALANCA(13, "Balança", "fas fa-balance-scale"),
	ICONE_RAIO(14, "Raio", "fas fa-bolt"),
	ICONE_CALENDARIO(15, "Calendário", "fas fa-calendar"),
	ICONE_ARQUIVO(16, "Arquivo", "fas fa-file"),
	ICONE_PASTA(17, "Pasta", "fas fa-folder"),
	ICONE_CORACAO(18, "Coração", "fas fa-heart"),
	ICONE_ESTRELA(19, "Estrela", "fas fa-star"),
	ICONE_CERTIFICADO(20, "Certificado", "fas fa-certificate"),
	ICONE_SINO(21, "Sino", "fas fa-bell"),
	ICONE_DATACHECK(22, "Data check", "far fa-calendar-check"),
	ICONE_ANTENA(23, "Antena", "fas fa-broadcast-tower"),
	ICONE_LAMPADA(24, "Lâmpada", "fas fa-lightbulb"),
	ICONE_SMILE(26, "Smile", "far fa-smile");

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