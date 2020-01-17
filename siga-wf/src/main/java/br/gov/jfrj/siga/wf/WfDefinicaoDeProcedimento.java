package br.gov.jfrj.siga.wf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessDefinition;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@Entity
@BatchSize(size = 500)
@Table(name = "WF_DEFINICAO_PROCEDIMENTO", catalog = "WF")
public class WfDefinicaoDeProcedimento extends HistoricoAuditavelSuporte
		implements Serializable, ProcessDefinition<WfDefinicaoDeTarefa>, Selecionavel {
	@Id
	@Column(name = "DEFP_ID", unique = true, nullable = false)
	private java.lang.Long id;

	@NotNull
	@Column(name = "DEFP_NM", length = 256)
	private java.lang.String nome;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeProcedimento")
	@Desconsiderar
	private List<WfDefinicaoDeTarefa> tarefa = new ArrayList<>();

	@Override
	public List<WfDefinicaoDeTarefa> getTaskDefinition() {
		return (List<WfDefinicaoDeTarefa>) (List) tarefa;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	// TODO: Escrever esse método
	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		return false;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public List<WfDefinicaoDeTarefa> getTarefa() {
		return tarefa;
	}

	public void setTarefa(List<WfDefinicaoDeTarefa> tarefa) {
		this.tarefa = tarefa;
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
}
