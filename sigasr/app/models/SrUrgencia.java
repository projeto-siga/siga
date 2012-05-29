package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrUrgencia {

	AGIR_IMEDIATO(5, "Resolver imediatamente", "Imediatamente"), ALGUMA_URGENCIA(4, "Resolver com urgência", "Com urgência"), MAIS_CEDO_POSSIVEL(
			3, "Resolver o mais cedo possível", "O mais cedo possível"), PODE_ESPERAR(2, "Solução pode esperar um pouco", "Pode esperar um pouco"), SEM_PRESSA(
			1, "Resolver sem pressa", "Não tem pressa");

	public static String ENUNCIADO = "O problema deve ser resolvido...";

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
