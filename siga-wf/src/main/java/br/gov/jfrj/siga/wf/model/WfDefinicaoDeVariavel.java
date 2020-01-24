package br.gov.jfrj.siga.wf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.TaskDefinitionVariable;
import com.crivano.jflow.model.enm.VariableEditingKind;
import com.crivano.jflow.model.enm.VariableKind;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_DEF_VARIAVEL", catalog = "WF")
public class WfDefinicaoDeVariavel implements TaskDefinitionVariable {
	@Id
	@Column(name = "DEFV_ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFT_ID")
	private WfDefinicaoDeTarefa definicaoDeTarefa;

	@Column(name = "DEFV_CD", length = 32)
	private String identificador;

	@Column(name = "DEFV_NM", length = 256)
	private String nome;

	@Column(name = "DEFV_TP")
	VariableKind tipo;

	@Column(name = "DEFV_TP_ACESSO")
	VariableEditingKind acesso;

	public VariableEditingKind getAcesso() {
		return acesso;
	}

	@Override
	public VariableEditingKind getEditingKind() {
		return acesso;
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
		return tipo;
	}

	public String getNome() {
		return nome;
	}

	public VariableKind getTipo() {
		return tipo;
	}

	@Override
	public String getTitle() {
		return nome;
	}

	public void setAcesso(VariableEditingKind acesso) {
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

	public void setTipo(VariableKind tipo) {
		this.tipo = tipo;
	}

	public boolean isRequired() {
		// TODO Esse método deve ser implementado
		return false;
	}
}
