package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

public enum SrEstado {

	ANDAMENTO(0,
			"Andamento"), PENDENTE(
			1, "Pendente"), FECHADO(2,
			"Fechado"), CANCELADO(3,
			"Cancelado");
			
	public String descrEstado;
	
	public int idEstado;

	SrEstado(int id, String descrEstado) {
		this.idEstado = id;
		this.descrEstado = descrEstado;
	}
	
}
