package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrTendencia {

	NAO_PIORA(1, "Se não resolvido, não vai piorar",
			"Não vai piorar ou pode ate melhorar"), PIORA_LONGO_PRAZO(2,
			"Se não resolvido, vai piorar em longo prazo",
			"Vai piorar em longo prazo"), PIORA_MEDIO_PRAZO(3,
			"Se não resolvido, vai piorar em médio prazo",
			"Vai piorar em médio prazo"), PIORA_CURTO_PRAZO(4,
			"Se não resolvido, vai piorar em curto prazo",
			"Vai piorar em curto prazo"), PIORA_IMEDIATA(5,
			"Se não resolvido, vai piorar imedatamente",
			"Vai piorar imediatamente");
	public static String ENUNCIADO = "Se nada for feito, a situação vai piorar...";

	public int nivelTendencia;

	public String descrTendencia;

	public String respostaEnunciado;

	SrTendencia(int nivel, String descricao) {
		this(nivel, descricao, descricao);
	}

	private SrTendencia(int nivel, String descrTendencia,
			String respostaEnunciado) {
		this.nivelTendencia = nivel;
		this.descrTendencia = descrTendencia;
		this.respostaEnunciado = respostaEnunciado;
	}

}
