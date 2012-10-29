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
@Table(name = "CP_TIPO_MOVIMENTACAO")
public class GcTipoMovimentacao extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_TIPO_MOVIMENTACAO")
	public long id;

	@Column(name = "NOME_TIPO_MOVIMENTACAO", nullable = false)
	public String nome;
}
