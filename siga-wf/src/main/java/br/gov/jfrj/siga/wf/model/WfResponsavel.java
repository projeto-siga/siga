package br.gov.jfrj.siga.wf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessDefinition;
import com.crivano.jflow.model.Responsible;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_responsavel")
public class WfResponsavel extends HistoricoAuditavelSuporte
		implements Responsible, Serializable, Sincronizavel, Comparable<Sincronizavel> {

	@Id
	@GeneratedValue
	@Column(name = "RESP_ID", unique = true, nullable = false)
	@Desconsiderar
	private java.lang.Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFR_ID")
	private WfDefinicaoDeResponsavel definicaoDeResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID")
	private DpPessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID")
	private DpLotacao lotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGU_ID")
	private CpOrgaoUsuario orgaoUsuario;

	@Transient
	private java.lang.String hisIde;

	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	@Desconsiderar
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

	public WfResponsavel() {
		super();
	}

	public WfResponsavel(DpPessoa pessoa, DpLotacao lotacao) {
		super();
		this.pessoa = pessoa;
		this.lotacao = lotacao;
	}

	@Override
	public String getInitials() {
		if (pessoa != null)
			return pessoa.getSigla();
		if (lotacao != null)
			return lotacao.getSiglaCompleta();
		return null;
	}

	public String getCodigo() {
		if (pessoa != null)
			return pessoa.getSiglaCompleta();
		if (lotacao != null)
			return "@" + lotacao.getSiglaCompleta();
		return null;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public WfDefinicaoDeResponsavel getDefinicaoDeResponsavel() {
		return definicaoDeResponsavel;
	}

	public void setDefinicaoDeResponsavel(WfDefinicaoDeResponsavel definicaoDeResponsavel) {
		this.definicaoDeResponsavel = definicaoDeResponsavel;
	}

	public DpPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(DpPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	public java.lang.String getHisIde() {
		return hisIde;
	}

	public void setHisIde(java.lang.String hisIde) {
		this.hisIde = hisIde;
	}

	@Override
	public String getIdExterna() {
		return getHisIde() != null ? getHisIde() : "novo responsável";
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
		return getCodigo();
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return this.getIdExterna().compareTo(o.getIdExterna());
	}

	@PostLoad
	public void postLoad() {
		this.setHisIde(criarIdExterna());
	}

	public String criarIdExterna() {
		return Long.toString(this.getDefinicaoDeResponsavel().getIdInicial()) + "-"
				+ Long.toString(this.getOrgaoUsuario().getId());
	}

	public String getTooltip() {
		if (pessoa != null)
			return pessoa.getNomePessoa();
		if (lotacao != null)
			return lotacao.getNomeLotacao();
		return null;
	}

}
