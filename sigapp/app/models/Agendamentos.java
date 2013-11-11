package models;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLock;

import br.gov.jfrj.siga.base.DateUtils;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity(name = "Agendamentos")
@Table(name = "Agendamentos")
public class Agendamentos extends GenericModel {
 @Id()
 @Column(name = "data_ag", nullable=false)
 public Date data_ag;
 @Id()
 @Column(name = "hora_ag", length=4, nullable=false)
 public String hora_ag;
 @Id()
 @ManyToOne
 @JoinColumn(name="cod_local" , nullable = false) //FK
 public Locais localFk;
 @Column(name="matricula" , length=9, nullable=true)
 public String matricula;
 @Column (name="periciado", length=50, nullable=true)
 public String periciado;
 @Column (name = "perito_juizo", length=50, nullable=true)
 public String perito_juizo;
 @Column(name="perito_parte", length=50, nullable=true)
 public String perito_parte;
 @Column(name="processo", length=50, nullable=true)
 public String processo;
 @Column(name="orgao", length=15, nullable=true)
 public String orgao;
public Agendamentos(Date data_ag, String hora_ag, Locais localFk, 
		String matricula, String periciado, String perito_juizo,
		String perito_parte, String processo, String orgao) {
	this.data_ag = data_ag;
	this.hora_ag = hora_ag;
	this.localFk = localFk;
	this.matricula = matricula;
	this.periciado = periciado;
	this.perito_juizo = perito_juizo;
	this.perito_parte = perito_parte;
	this.processo = processo;
	this.orgao = orgao;
 }

}
