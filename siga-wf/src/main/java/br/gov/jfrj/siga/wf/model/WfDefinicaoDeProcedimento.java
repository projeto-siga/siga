package br.gov.jfrj.siga.wf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessDefinition;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_def_procedimento")
public class WfDefinicaoDeProcedimento extends HistoricoAuditavelSuporte implements Serializable,
		ProcessDefinition<WfDefinicaoDeTarefa>, Selecionavel, Sincronizavel, Comparable<Sincronizavel> {
	@Id
	@GeneratedValue
	@Column(name = "DEFP_ID", unique = true, nullable = false)
	@Desconsiderar
	private java.lang.Long id;

	@NotNull
	@Column(name = "DEFP_NM", length = 256)
	private java.lang.String nome;

	@NotNull
	@Column(name = "DEFP_DS", length = 256)
	private java.lang.String descr;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeProcedimento")
	@OrderBy("ordem ASC")
	@Desconsiderar
	private List<WfDefinicaoDeTarefa> definicaoDeTarefa = new ArrayList<>();

	@Transient
	private java.lang.String hisIde;
	
	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		super.setHisAtivo(hisAtivo);
		this.hisAtivo = getHisAtivo();
	}

	@Override
	public List<WfDefinicaoDeTarefa> getTaskDefinition() {
		return getDefinicaoDeTarefa();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	@Override
	public String getSigla() {
		return Long.toString(id);
	}

	@Override
	public void setSigla(String sigla) {
		id = Long.parseLong(sigla);
	}

	@Override
	public String getDescricao() {
		return nome;
	}

	public List<WfDefinicaoDeTarefa> getDefinicaoDeTarefa() {
		return definicaoDeTarefa;
	}

	public void setDefinicaoDeTarefa(List<WfDefinicaoDeTarefa> definicaoDeTarefa) {
		this.definicaoDeTarefa = definicaoDeTarefa;
	}

	public java.lang.String getHisIde() {
		return hisIde;
	}

	public void setHisIde(java.lang.String hisIde) {
		this.hisIde = hisIde;
	}

	@Override
	public String getIdExterna() {
		return getHisIde() != null ? getHisIde() : "nova definição de procedimento";
	}

	@Override
	public void setIdExterna(String idExterna) {
		setHisIde(idExterna);
	}

	@Override
	public void setIdInicial(Long idInicial) {
		this.setHisIdIni(idInicial);
	}

	@Override
	public Date getDataInicio() {
		return getHisDtIni();
	}

	@Override
	public void setDataInicio(Date dataInicio) {
		setHisDtIni(dataInicio);
	}

	@Override
	public Date getDataFim() {
		return getHisDtFim();
	}

	@Override
	public void setDataFim(Date dataFim) {
		setHisDtFim(dataFim);
	}

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	@Override
	public int getNivelDeDependencia() {
		return 0;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	@Override
	public String getDescricaoExterna() {
		return getNome();
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return this.getIdExterna().compareTo(o.getIdExterna());
	}

	public java.lang.String getDescr() {
		return descr;
	}

	public void setDescr(java.lang.String descr) {
		this.descr = descr;
	}
}
