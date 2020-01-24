package br.gov.jfrj.siga.wf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.TaskDefinition;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_DEF_TAREFA", catalog = "WF")
public class WfDefinicaoDeTarefa extends HistoricoAuditavelSuporte implements
		TaskDefinition<WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio>, Sincronizavel {
	@Id
	@Column(name = "DEFT_ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "DEFT_NM", length = 256)
	private java.lang.String nome;

	@Column(name = "DEFT_TX_ASSUNTO", length = 256)
	private java.lang.String assunto;

	@Column(name = "DEFT_TX_CONTEUDO", length = 2048)
	private java.lang.String conteudo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFP_ID")
	private WfDefinicaoDeProcedimento definicaoDeProcedimento;

	@Column(name = "DEFT_TP_TAREFA")
	private WfTipoDeTarefa tipoDeTarefa;

	@Column(name = "DEFT_TP_RESPONSAVEL")
	private WfTipoDeResponsavel tipoDeResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFR_ID")
	private WfDefinicaoDeResponsavel definicaoDeResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_SEGUINTE")
	private WfDefinicaoDeTarefa seguinte;

	@Column(name = "DEFT_FG_ULTIMO")
	private boolean ultimo;

	@Column(name = "DEFT_NR_ORDEM")
	private int ordem;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeTarefa")
	@Desconsiderar
	private List<WfDefinicaoDeVariavel> definicaoDeVariavel = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeTarefa")
	@Desconsiderar
	private List<WfDefinicaoDeDesvio> definicaoDeDesvio = new ArrayList<>();

	@Column(name = "HIS_IDE", length = 128)
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WfTipoDeTarefa getKind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAfter() {
		// TODO Auto-generated method stub
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

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getIdExterna() {
		return this.hisIde;
	}

	@Override
	public void setIdExterna(String idExterna) {
		this.hisIde = idExterna;
	}

	@Override
	public void setIdInicial(Long idInicial) {
		setHisIdIni(idInicial);

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
	public String getDescricaoExterna() {
		return getNome();
	}
}
