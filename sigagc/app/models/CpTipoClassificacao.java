package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "CP_TIPO_CLASSIFICACAO")
public class CpTipoClassificacao extends GenericModel {
	@Id
	@GeneratedValue
	public long id_tipo_classificacao;

	@Column(nullable = false)
	public String desc_tipo_classificacao;
}
