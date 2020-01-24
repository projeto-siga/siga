package br.gov.jfrj.siga.wf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_DEF_RESPONSAVEL", catalog = "WF")
public class WfDefinicaoDeResponsavel {
	@Id
	@Column(name = "DEFR_ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "DEFR_NM", length = 256)
	private String nome;

	@Column(name = "DEFR_DS", length = 256)
	private String descr;

	@Column(name = "DEFR_TP")
	WfTipoDeResponsavel tipo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public WfTipoDeResponsavel getTipo() {
		return tipo;
	}

	public void setTipo(WfTipoDeResponsavel tipo) {
		this.tipo = tipo;
	}
}
