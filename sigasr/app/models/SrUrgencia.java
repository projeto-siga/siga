package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrUrgencia {

	AGIR_IMEDIATO(5, "Imediatamente"), ALGUMA_URGENCIA(4,
			"Com alguma urgência"), MAIS_CEDO_POSSIVEL(3,
			"O mais cedo possível"), PODE_ESPERAR(2, "Pode esperar um pouco"), SEM_PRESSA(
			1, "Não tem pressa");

	public int idUrgencia;

	public String descrUrgencia;

	SrUrgencia(int id, String descricao) {
		this.idUrgencia = id;
		this.descrUrgencia = descricao;
	}

}
