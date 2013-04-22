package models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLock;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity(name = "Agendamentos")
@Table(name = "Agendamentos")
public class Agendamentos extends GenericModel {
 @Id()
 Date data_ag;
 @Id()
 String hora_ag;
 @Id()
 String cod_local;
 String sigla;
 String periciado;
 String perito_juizo;
 String perito_parte;
 String processo;
 public Agendamentos(Date data_ag, String hora_ag, String cod_local, 
		String sigla, String periciado, String perito_juizo,
		String perito_parte, String processo) {
	super();
	this.data_ag = data_ag;
	this.hora_ag = hora_ag;
	this.cod_local = cod_local;
	this.sigla = sigla;
	this.periciado = periciado;
	this.perito_juizo = perito_juizo;
	this.perito_parte = perito_parte;
	this.processo = processo;
 }

}
