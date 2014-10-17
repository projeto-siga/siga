package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLock;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

/**
 * @author Herval, Ruben e Edson da Rocha
 */
@Entity(name = "Locais")
@Table(name = "Locais", schema = "SIGAPMP")
public class Locais extends GenericModel {
	@Id
	@Column(name="cod_local", length=3, nullable=false)
	public String cod_local;
	@Column(name="local", length=30, nullable=true)
	public String local;
	@ManyToOne
	@JoinColumn(name = "cod_forum", nullable=true) //fk, e, tem que atribuir programaticamente como objeto.
	public Foruns forumFk; // Isso e´ coluna, mas,  tem que atribuir como objeto.
	@Column(name="dias", length=40, nullable=true)
	public String dias;
	@Column(name="hora_ini", length=8, nullable=true)
	public String hora_ini;
	@Column(name="hora_fim", length=8, nullable=true)
	public String hora_fim;
	@Column(name="intervalo_atendimento", length=10, nullable=true)
	public int intervalo_atendimento;
	@Column(name="exibir", length=2, nullable=false)
	public int exibir;
	@Column(name="endereco", length=100, nullable = true)
	public String endereco;
	@Column(name="ordem_apresentacao", length=2, nullable=false )
	public int ordem_apresentacao;
	
}