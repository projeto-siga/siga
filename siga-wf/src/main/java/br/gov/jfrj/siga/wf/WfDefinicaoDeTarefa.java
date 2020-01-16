package br.gov.jfrj.siga.wf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.crivano.jflow.model.ResponsibleKind;
import com.crivano.jflow.model.TaskDefinition;
import com.crivano.jflow.model.TaskDefinitionDetour;
import com.crivano.jflow.model.TaskDefinitionVariable;
import com.crivano.jflow.model.TaskKind;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

public class WfDefinicaoDeTarefa
		implements TaskDefinition<WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio> {
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
	private WfTipoDeTarefa tipoTarefa;

	@Column(name = "DEFT_TP_RESPONSAVEL")
	private WfTipoDeTarefa tipoResponsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID_SEGUINTE")
	private WfDefinicaoDeTarefa seguinte;

	@Column(name = "DEFT_FG_ULTIMO")
	private boolean ultimo;

	@Column(name = "DEFT_NR_ORDEM")
	private int ordem;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeTarefa")
	@Desconsiderar
	private List<WfVariavel> variaveis = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeTarefa")
	@Desconsiderar
	private List<WfDefinicaoDeDesvio> desvios = new ArrayList<>();

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WfDefinicaoDeVariavel> getVariable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WfDefinicaoDeDesvio> getDetour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
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

	public WfTipoDeTarefa getTipoTarefa() {
		return tipoTarefa;
	}

	public void setTipoTarefa(WfTipoDeTarefa tipoTarefa) {
		this.tipoTarefa = tipoTarefa;
	}

	public WfTipoDeTarefa getTipoResponsavel() {
		return tipoResponsavel;
	}

	public void setTipoResponsavel(WfTipoDeTarefa tipoResponsavel) {
		this.tipoResponsavel = tipoResponsavel;
	}

	public WfDefinicaoDeTarefa getSeguinte() {
		return seguinte;
	}

	public void setSeguinte(WfDefinicaoDeTarefa seguinte) {
		this.seguinte = seguinte;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public List<WfVariavel> getVariaveis() {
		return variaveis;
	}

	public void setVariaveis(List<WfVariavel> variaveis) {
		this.variaveis = variaveis;
	}

	public List<WfDefinicaoDeDesvio> getDesvios() {
		return desvios;
	}

	public void setDesvios(List<WfDefinicaoDeDesvio> desvios) {
		this.desvios = desvios;
	}

	public boolean getUltimo() {
		return ultimo;
	}

	public void setUltimo(boolean ultimo) {
		this.ultimo = ultimo;
	}

}
