package br.gov.jfrj.siga.wf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_def_responsavel")
public class WfDefinicaoDeResponsavel extends HistoricoAuditavelSuporte {
	@Id
	@GeneratedValue
	@Column(name = "DEFR_ID", unique = true, nullable = false)
	@Desconsiderar
	private Long id;

	@Column(name = "DEFR_NM", length = 256)
	private String nome;

	@Column(name = "DEFR_DS", length = 256)
	private String descr;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEFR_TP")
	WfTipoDeResponsavel tipo;

	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
	//
	@Desconsiderar
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		this.hisAtivo = hisAtivo;
	}

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

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}
}
