package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.support.ProcessInstanceVariable;

import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_variavel")
public class WfVariavel implements ProcessInstanceVariable, Sincronizavel, Comparable<Sincronizavel> {
	@Id
	@GeneratedValue
	@Column(name = "VARI_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROC_ID")
	private WfProcedimento procedimento;

	@Column(name = "VARI_NM", length = 256)
	private String nome;

	@Column(name = "VARI_TX")
	String string;

	@Column(name = "VARI_DF")
	Date date;

	@Column(name = "VARI_FG")
	Boolean bool;

	@Column(name = "VARI_NR")
	Double number;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getIdentifier() {
		return getNome();
	}

	@Override
	public void setIdentifier(String identifier) {
		this.setNome(identifier);
	}

	@Override
	public String getString() {
		return string;
	}

	@Override
	public void setString(String string) {
		this.string = string;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public Boolean getBool() {
		return bool;
	}

	@Override
	public void setBool(Boolean bool) {
		this.bool = bool;
	}

	@Override
	public Double getNumber() {
		return number;
	}

	@Override
	public void setNumber(Double number) {
		this.number = number;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getIdExterna() {
		return this.nome;
	}

	@Override
	public void setIdExterna(String idExterna) {
		this.nome = idExterna;
	}

	@Override
	public Long getIdInicial() {
		return null;
	}

	@Override
	public void setIdInicial(Long idInicial) {
	}

	@Override
	public Date getDataInicio() {
		return null;
	}

	@Override
	public void setDataInicio(Date dataInicio) {
	}

	@Override
	public Date getDataFim() {
		return null;
	}

	@Override
	public void setDataFim(Date dataFim) {
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
		return this.nome;
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return this.getIdExterna().compareTo(o.getIdExterna());
	}

	public WfProcedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(WfProcedimento procedimento) {
		this.procedimento = procedimento;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getValor() {
		if (string != null)
			return string;
		if (date != null)
			return date;
		if (bool != null)
			return bool;
		if (number != null)
			return number;
		return null;
	}

	public Object getValorAsString() {
		if (string != null)
			return string;
		if (date != null)
			return Data.formatDDMMYY_AS_HHMMSS(date);
		if (bool != null)
			return bool ? "Sim" : "Não";
		if (number != null)
			return number.toString();
		return null;
	}

}
