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
@Table(name = "CP_TIPO_INFORMACAO")
public class GcTipoInformacao extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_TIPO_INFORMACAO")
	public long id;

	@Column(name = "NOME_TIPO_INFORMACAO", nullable = false)
	public String nome;
}
