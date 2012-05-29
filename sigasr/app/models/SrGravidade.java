package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrGravidade {

	EXTREMAMENTE_GRAVE(5, "Extremamente grave", "Extremamente graves"), MUITO_GRAVE(4, "Muito grave", "Muito graves"), GRAVE(
			3, "Grave", "Graves"), POUCO_GRAVE(2, "Pouco grave", "Pouco graves"), SEM_RGAVIDADE(1,
			"Sem gravidade");
	
	public int nivelGravidade;

	public String descrGravidade;

	public String respostaEnunciado;

	SrGravidade(int nivel, String descricao) {
		this(nivel, descricao, descricao);
	}

	private SrGravidade(int nivel, String descrGravidade,
			String respostaEnunciado) {
		this.nivelGravidade = nivel;
		this.descrGravidade = descrGravidade;
		this.respostaEnunciado = respostaEnunciado;
	}

}
