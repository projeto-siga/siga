package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "SR_OBJETIVO_ATRIBUTO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SrObjetivoAtributo extends GenericModel {

	private static final long serialVersionUID = 1L;

	final static public long OBJETIVO_SOLICITACAO = 1;

	final static public long OBJETIVO_ACORDO = 2;

	final static public long OBJETIVO_INDICADOR = 3;
	
	@Id
	@Column(name = "ID_OBJETIVO")
	public long idObjetivo;

	@Column(name = "DESCR_OBJETIVO", nullable = false)
	public String descrObjetivo;
	
	public Long getId() {
		return this.idObjetivo;
	}
	
	public String getDescricao() {
		return this.descrObjetivo;
	}

}
