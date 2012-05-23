package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrGravidade {

	EXTREMAMENTE_GRAVE(5,
			"Extremamente graves"), MUITO_GRAVE(
			4, "Muito graves"), GRAVE(3, "Graves"), POUCO_GRAVE(2,
			"Pouco graves"), SEM_RGAVIDADE(1, "Sem gravidade");

	public int idGravidade;

	public String descrGravidade;

	SrGravidade(int id, String descricao) {
		this.idGravidade = id;
		this.descrGravidade = descricao;
	}

}
