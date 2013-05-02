package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLock;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

/**
 * @author Herval e Ruben
 */
@Entity(name = "Locais")
@Table(name = "Locais")
public class Locais extends GenericModel {
	@Id
	public String cod_local;
	@Column(name="local")
	public String local;
	//foreign key para tabela Foruns; só no Oracle.
	@Column(name="cod_forum")
	public int cod_forum;
	@Column(name="dias")
	public String dias;
	@Column(name="hora_ini")
	public String hora_ini;
	@Column(name="hora_fim")
	public String hora_fim;
	@Column(name="intervalo_atendimento")
	public int intervalo_atendimento;
	@Column(name="exibir")
	public int exibir;
	@Column(name="endereco")
	public String endereco;
	@Column(name="ordem_apresentacao")
	public int ordem_apresentacao;
	
	public Locais(String cod_local, String local, int cod_forum, String dias, String hora_ini, String hora_fim , int intervalo_atendimento, int exibir, String endereco, int ordem_apresentacao  ) {
		this.cod_local = cod_local;
		this.local = local;
		this.cod_forum = cod_forum;
		this.dias = dias;
		this.hora_ini = hora_ini;
		this.hora_fim = hora_fim;
		this.intervalo_atendimento = 15;
		this.exibir = 1;
		this.endereco = endereco;
		this.ordem_apresentacao = 0;
	}
}