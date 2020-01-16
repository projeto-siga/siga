package br.gov.jfrj.siga.wf;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.crivano.jflow.model.TaskDefinitionDetour;

public class WfDefinicaoDeDesvio implements TaskDefinitionDetour {
	@Id
	@Column(name = "DESV_ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "DESV_NM", length = 256)
	private java.lang.String nome;

	@Column(name = "DESV_TX_CONDICAO", length = 256)
	private java.lang.String condicao;

	@Column(name = "DESV_NR_ORDEM")
	private int ordem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID")
	private WfDefinicaoDeTarefa definicaoDeTarefa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_SEGUINTE")
	private WfDefinicaoDeTarefa seguinte;

	@Column(name = "DESV_FG_ULTIMO")
	private boolean ultimo;

	@Override
	public String getTitle() {
		return nome;
	}

	@Override
	public String getTaskIdentifier() {
		return seguinte.getIdentifier();
	}

	@Override
	public String getCondition() {
		return condicao;
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

	public java.lang.String getCondicao() {
		return condicao;
	}

	public void setCondicao(java.lang.String condicao) {
		this.condicao = condicao;
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

	public WfDefinicaoDeTarefa getSeguinte() {
		return seguinte;
	}

	public void setSeguinte(WfDefinicaoDeTarefa seguinte) {
		this.seguinte = seguinte;
	}

	public boolean getUltimo() {
		return ultimo;
	}

	public void setUltimo(boolean ultimo) {
		this.ultimo = ultimo;
	}
}