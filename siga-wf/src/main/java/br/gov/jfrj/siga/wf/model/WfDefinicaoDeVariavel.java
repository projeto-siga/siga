package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.TaskDefinitionVariable;
import com.crivano.jflow.model.enm.VariableEditingKind;
import com.crivano.jflow.model.enm.VariableKind;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeAcessoDeVariavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeVariavel;
import br.gov.jfrj.siga.wf.util.NaoSerializar;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_def_variavel")
public class WfDefinicaoDeVariavel extends HistoricoAuditavelSuporte
		implements TaskDefinitionVariable, Sincronizavel, Comparable<Sincronizavel> {
	@Id
	@GeneratedValue
	@Column(name = "DEFV_ID", unique = true, nullable = false)
	@Desconsiderar
	private Long id;

	@NotNull
	@NaoSerializar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID")
	private WfDefinicaoDeTarefa definicaoDeTarefa;

	@NotNull
	@Column(name = "DEFV_CD", length = 32)
	private String identificador;

	@NotNull
	@Column(name = "DEFV_NM", length = 256)
	private String nome;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "DEFV_TP")
	WfTipoDeVariavel tipo;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "DEFV_TP_ACESSO")
	WfTipoDeAcessoDeVariavel acesso;

	@Column(name = "DEFV_NR_ORDEM")
	private int ordem;

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

	public WfTipoDeAcessoDeVariavel getAcesso() {
		return acesso;
	}

	@Override
	public VariableEditingKind getEditingKind() {
		return acesso.getKind();
	}

	public Long getId() {
		return id;
	}

	public String getIdentificador() {
		return identificador;
	}

	@Override
	public String getIdentifier() {
		return identificador;
	}

	@Override
	public VariableKind getKind() {
		return tipo.getKind();
	}

	public String getNome() {
		return nome;
	}

	public WfTipoDeVariavel getTipo() {
		return tipo;
	}

	@Override
	public String getTitle() {
		return nome;
	}

	public void setAcesso(WfTipoDeAcessoDeVariavel acesso) {
		this.acesso = acesso;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTipo(WfTipoDeVariavel tipo) {
		this.tipo = tipo;
	}

	public boolean isRequired() {
		return this.getAcesso() == WfTipoDeAcessoDeVariavel.READ_WRITE_REQUIRED;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public WfDefinicaoDeTarefa getDefinicaoDeTarefa() {
		return definicaoDeTarefa;
	}

	public void setDefinicaoDeTarefa(WfDefinicaoDeTarefa definicaoDeTarefa) {
		this.definicaoDeTarefa = definicaoDeTarefa;
	}

	@Override
	public String getIdExterna() {
		return this.getDefinicaoDeTarefa().getIdExterna() + "|" + this.getIdentificador();
	}

	@Override
	public void setIdExterna(String idExterna) {
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
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	@Override
	public String getDescricaoExterna() {
		return getNome();
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return (this.getIdExterna()).compareTo(o.getIdExterna());
	}

}
