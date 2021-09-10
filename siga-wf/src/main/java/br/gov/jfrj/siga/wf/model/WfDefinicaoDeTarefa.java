package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.TaskDefinition;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.util.NaoSerializar;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_def_tarefa")
public class WfDefinicaoDeTarefa extends HistoricoAuditavelSuporte
		implements TaskDefinition<WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio>,
		Sincronizavel, Comparable<Sincronizavel> {
	@Id
	@GeneratedValue
	@Column(name = "DEFT_ID", unique = true, nullable = false)
	@Desconsiderar
	private java.lang.Long id;

	@Column(name = "DEFT_NM", length = 256)
	private java.lang.String nome;

	@Column(name = "DEFT_TX_ASSUNTO", length = 256)
	private java.lang.String assunto;

	@Column(name = "DEFT_TX_CONTEUDO", length = 2048)
	private java.lang.String conteudo;

	@NaoSerializar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFP_ID")
	private WfDefinicaoDeProcedimento definicaoDeProcedimento;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEFT_TP_TAREFA")
	private WfTipoDeTarefa tipoDeTarefa;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEFT_TP_RESPONSAVEL")
	private WfTipoDeResponsavel tipoDeResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFR_ID")
	private WfDefinicaoDeResponsavel definicaoDeResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID")
	private DpPessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID")
	private DpLotacao lotacao;

	@NaoSerializar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_SEGUINTE")
	private WfDefinicaoDeTarefa seguinte;

	@Transient
	private String seguinteIde;

	@Transient
	private Long pessoaId;

	@Transient
	private Long lotacaoId;

	@Transient
	private Long definicaoDeResponsavelId;

	@Column(name = "DEFT_FG_ULTIMO")
	private boolean ultimo;

	@Column(name = "DEFT_NR_ORDEM")
	private int ordem;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeTarefa")
	@OrderBy("ordem ASC")
	@Desconsiderar
	private List<WfDefinicaoDeVariavel> definicaoDeVariavel = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeTarefa")
	@OrderBy("ordem ASC")
	@Desconsiderar
	private List<WfDefinicaoDeDesvio> definicaoDeDesvio = new ArrayList<>();

	@Column(name = "DEFT_ID_REF")
	private java.lang.Long refId;

	@Column(name = "DEFT_SG_REF", length = 32)
	private java.lang.String refSigla;

	@Column(name = "DEFT_DS_REF", length = 256)
	private java.lang.String refDescr;

	@Transient
	private java.lang.String hisIde;

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam de
	// HistoricoSuporte.
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

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.lang.String getAssunto() {
		return assunto;
	}

	public void setAssunto(java.lang.String assunto) {
		this.assunto = assunto;
	}

	public java.lang.String getConteudo() {
		return conteudo;
	}

	public void setConteudo(java.lang.String conteudo) {
		this.conteudo = conteudo;
	}

	public WfDefinicaoDeProcedimento getDefinicaoDeProcedimento() {
		return definicaoDeProcedimento;
	}

	public void setDefinicaoDeProcedimento(WfDefinicaoDeProcedimento definicaoDeProcedimento) {
		this.definicaoDeProcedimento = definicaoDeProcedimento;
	}

	public WfTipoDeTarefa getTipoDeTarefa() {
		return tipoDeTarefa;
	}

	public void setTipoDeTarefa(WfTipoDeTarefa tipoDeTarefa) {
		this.tipoDeTarefa = tipoDeTarefa;
	}

	public WfTipoDeResponsavel getTipoDeResponsavel() {
		return tipoDeResponsavel;
	}

	public void setTipoDeResponsavel(WfTipoDeResponsavel tipoDeResponsavel) {
		this.tipoDeResponsavel = tipoDeResponsavel;
	}

	public WfDefinicaoDeTarefa getSeguinte() {
		return seguinte;
	}

	public void setSeguinte(WfDefinicaoDeTarefa seguinte) {
		this.seguinte = seguinte;
	}

	public boolean isUltimo() {
		return ultimo;
	}

	public void setUltimo(boolean ultimo) {
		this.ultimo = ultimo;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public List<WfDefinicaoDeVariavel> getDefinicaoDeVariavel() {
		return definicaoDeVariavel;
	}

	public void setDefinicaoDeVariavel(List<WfDefinicaoDeVariavel> definicaoDeVariavel) {
		this.definicaoDeVariavel = definicaoDeVariavel;
	}

	public List<WfDefinicaoDeDesvio> getDefinicaoDeDesvio() {
		return definicaoDeDesvio;
	}

	public void setDefinicaoDeDesvio(List<WfDefinicaoDeDesvio> definicaoDeDesvio) {
		this.definicaoDeDesvio = definicaoDeDesvio;
	}

	@Override
	public String getIdentifier() {
		return Long.toString(getId());
	}

	@Override
	public WfTipoDeTarefa getKind() {
		return getTipoDeTarefa() != null ? getTipoDeTarefa() : WfTipoDeTarefa.FORMULARIO;
	}

	@Override
	public String getTitle() {
		return getNome();
	}

	@Override
	public String getAfter() {
		if (isUltimo())
			return null;
		if (getSeguinte() != null)
			return getSeguinte().getIdentifier();
		if (getDefinicaoDeProcedimento().getDefinicaoDeTarefa().size() > getOrdem() + 1)
			return getDefinicaoDeProcedimento().getDefinicaoDeTarefa().get(getOrdem() + 1).getIdentifier();
		return null;
	}

	@Override
	public WfTipoDeResponsavel getResponsibleKind() {
		return getTipoDeResponsavel();
	}

	@Override
	public List<WfDefinicaoDeVariavel> getVariable() {
		return getDefinicaoDeVariavel();
	}

	@Override
	public List<WfDefinicaoDeDesvio> getDetour() {
		return getDefinicaoDeDesvio();
	}

	@Override
	public String getSubject() {
		return getAssunto();
	}

	@Override
	public String getText() {
		return getConteudo();
	}

	public String getSeguinteIde() {
		return seguinteIde;
	}

	public void setSeguinteIde(String seguinteIde) {
		this.seguinteIde = seguinteIde;
	}

	@Override
	public String getIdExterna() {
		return this.getHisIde();
	}

	@Override
	public void setIdExterna(String idExterna) {
		this.setHisIde(idExterna);
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
	public String getDescricaoExterna() {
		return getNome();
	}

	@Override
	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	public java.lang.String getHisIde() {
		return hisIde;
	}

	public void setHisIde(java.lang.String hisIde) {
		this.hisIde = hisIde;
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return this.getIdExterna().compareTo(o.getIdExterna());
	}

	@PostLoad
	public void postLoad() {
		this.setHisIde(Long.toString(this.getIdInicial()));
		if (this.getSeguinte() != null)
			this.setSeguinteIde(Long.toString(this.getSeguinte().getIdInicial()));
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

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	public Long getLotacaoId() {
		return lotacaoId;
	}

	public void setLotacaoId(Long lotacaoId) {
		this.lotacaoId = lotacaoId;
	}

	public Long getDefinicaoDeResponsavelId() {
		return definicaoDeResponsavelId;
	}

	public void setDefinicaoDeResponsavelId(Long definicaoDeResponsavelId) {
		this.definicaoDeResponsavelId = definicaoDeResponsavelId;
	}

	public java.lang.Long getRefId() {
		return refId;
	}

	public void setRefId(java.lang.Long refId) {
		this.refId = refId;
	}

	public java.lang.String getRefSigla() {
		return refSigla;
	}

	public void setRefSigla(java.lang.String refSigla) {
		this.refSigla = refSigla;
	}

	public java.lang.String getRefDescr() {
		return refDescr;
	}

	public void setRefDescr(java.lang.String refDescr) {
		this.refDescr = refDescr;
	}

	@Override
	public String getResponsibleDescription() {
		if (tipoDeResponsavel == null)
			return null;

		switch (tipoDeResponsavel) {
		case LOTACAO:
			if (lotacao != null)
				return lotacao.getSigla();
		case PESSOA:
			if (pessoa != null)
				return pessoa.getSigla();
		case RESPONSAVEL:
			if (definicaoDeResponsavel != null)
				return definicaoDeResponsavel.getNome();
		}

		String s = tipoDeResponsavel.getDescr();
		s = s.replace("Principal: ", "").replace("Lotação ", "Lota. ");
		return s;
	}

}
