package models;

import java.util.ArrayList;
import java.util.Collections;
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
@Table(name = "SR_SERVICO")
public class SrServico extends SrCodigoHierarquicoSuporte {

	@Id
	@GeneratedValue
	@Column(name = "ID_SERVICO")
	public int idServico;

	@Column(name = "SIGLA_SERVICO")
	public String siglaServico;

	@Column(name = "DESCR_SERVICO")
	public String descrServico;

	public SrServico() {
		this(null, null);
	}

	public SrServico(String descricao) {
		this(descricao, null);
	}

	public SrServico(String sigla, String descricao) {
		super("00.00", "siglaServico", "descrServico");
		this.descrServico = descricao;
		this.siglaServico = sigla;
	}

	@Override
	public Integer getId() {
		return this.idServico;
	}

	@Override
	public String getSigla() {
		return this.siglaServico;
	}

	@Override
	public String getDescricao() {
		return descrServico;
	}

	@Override
	protected void setSiglaInterno(String sigla) {
		this.siglaServico = sigla;
	}

	@Override
	protected void setDescricaoInterno(String descr) {
		this.descrServico = descr;
	}

	@Override
	public void setId(Integer id) {
		if (id != null)
		this.idServico = id;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descrServico = descricao;
	}

}
