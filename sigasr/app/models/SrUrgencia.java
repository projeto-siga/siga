package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrUrgencia {

	SEM_PRESSA(1, "Sem urgência.", "Sem pressa"), PODE_ESPERAR(2,
			"Pouco urgente.", "Quando for oportuno"), URGENCIA(
			3, "Urgente.", "Com urgência"), MUITA_URGENCIA(
			4, "Muito urgente", "Com muita urgência"), AGIR_IMEDIATO(5,
			"Extremamente urgente.", "Imediatamente");

	public int nivelUrgencia;

	public String descrUrgencia;

	public String respostaEnunciado;

	private SrUrgencia(int nivelUrgencia, String descrUrgencia) {
		this(nivelUrgencia, descrUrgencia, descrUrgencia);
	}

	SrUrgencia(int nivel, String descricao, String respostaEnunciado) {
		this.nivelUrgencia = nivel;
		this.descrUrgencia = descricao;
		this.respostaEnunciado = respostaEnunciado;
	}

}
