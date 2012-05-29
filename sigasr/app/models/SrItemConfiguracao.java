package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_ITEM_CONFIGURACAO")
public class SrItemConfiguracao extends SrCodigoHierarquicoSuporte {

	@Id
	@GeneratedValue
	@Column(name = "ID_ITEM_CONFIGURACAO")
	public int idItemConfiguracao;

	@Column(name = "SIGLA_ITEM_CONFIGURACAO")
	public String siglaItemConfiguracao;

	@Column(name = "DESCR_ITEM_CONFIGURACAO")
	public String descrItemConfiguracao;

	public SrItemConfiguracao() {
		this(null, null);
	}

	public SrItemConfiguracao(String descricao) {
		this(descricao, null);
	}

	public SrItemConfiguracao(String sigla, String descricao) {
		super("00.00.00.00", "siglaItemConfiguracao", "descrItemConfiguracao");
		this.descrItemConfiguracao = descricao;
		this.siglaItemConfiguracao = sigla;
	}

	@Override
	public Integer getId() {
		return idItemConfiguracao;
	}

	@Override
	public String getSigla() {
		return siglaItemConfiguracao;
	}

	@Override
	public String getDescricao() {
		return descrItemConfiguracao;
	}

	@Override
	protected void setSiglaInterno(String sigla) {
		this.siglaItemConfiguracao = sigla;
	}

	@Override
	protected void setDescricaoInterno(String descr) {
		this.descrItemConfiguracao = descr;
	}

}
