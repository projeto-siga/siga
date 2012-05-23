package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrTendencia {

	PIORA_IMEDIATA(5,
			"Vai piorar imediatamente"), PIORA_CURTO_PRAZO(
			4, "Vai piorar em curto prazo"), PIORA_MEDIO_PRAZO(3,
			"Vai piorar em médio prazo"), PIORA_LONGO_PRAZO(2,
			"Vai piorar a longo prazo"), NAO_PIORA(1,
			"Não vai piorar ou pode ate melhorar");

	public int idTendencia;

	public String descrTendencia;

	SrTendencia(int id, String descricao) {
		this.idTendencia = id;
		this.descrTendencia = descricao;
	}

}
