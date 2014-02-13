package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "GC_TIPO_INFORMACAO", schema = "SIGAGC")
public class GcTipoInformacao extends GenericModel {
	final static public long TIPO_INFORMACAO_REGISTRO_DE_CONHECIMENTO = 1;

	@Id
	@Column(name = "ID_TIPO_INFORMACAO")
	public long id;

	@Column(name = "NOME_TIPO_INFORMACAO", nullable = false)
	public String nome;

	public GcTipoInformacao(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public GcTipoInformacao() {
		super();
	}
}
