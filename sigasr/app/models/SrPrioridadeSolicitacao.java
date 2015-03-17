package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_PRIORIDADE_SOLICITACAO", schema = "SIGASR")
public class SrPrioridadeSolicitacao extends Objeto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_PRIORIDADE_SOLICITACAO_SEQ", name = "srPrioridadeSolicitacaoSeq")
	@GeneratedValue(generator = "srPrioridadeSolicitacaoSeq")
	@Column(name = "ID_PRIORIDADE_SOLICITACAO")
	public Long idPrioridadeSolicitacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	public SrLista lista;	

	@ManyToOne
	@JoinColumn(name="ID_SOLICITACAO")
	public SrSolicitacao solicitacao;	

	@Column(name = "NUM_POSICAO")
	public Long numPosicao;
	
	@Enumerated(EnumType.ORDINAL)
	public SrPrioridade prioridade;
	
	@Column(name = "NAO_REPOSICIONAR_AUTOMATICO")
	@Type(type = "yes_no")
	public Boolean naoReposicionarAutomatico;	

	public SrPrioridadeSolicitacao(SrLista lista, SrSolicitacao solicitacao, SrPrioridade prioridade, boolean naoReposicionarAutomatico) {
		this.lista = lista;
		this.solicitacao = solicitacao;
		this.prioridade = prioridade;
		this.naoReposicionarAutomatico = naoReposicionarAutomatico;
	}	

	public Long getId() {
		return idPrioridadeSolicitacao;
	}

	public void setId(Long id) {
		this.idPrioridadeSolicitacao = id;
	}	
	
	public SrLista getLista() {
		return lista;
	}

	public void setLista(SrLista lista) {
		this.lista = lista;
	}

	public Long getIdPrioridadeSolicitacao() {
		return idPrioridadeSolicitacao;
	}

	public void setIdPrioridadeSolicitacao(Long idPrioridadeSolicitacao) {
		this.idPrioridadeSolicitacao = idPrioridadeSolicitacao;
	}

	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public SrSolicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SrSolicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Long getNumPosicao() {
		return numPosicao;
	}

	public void setNumPosicao(Long numPosicao) {
		this.numPosicao = numPosicao;
	}

	public Boolean getNaoReposicionarAutomatico() {
		return naoReposicionarAutomatico;
	}

	public void setNaoReposicionarAutomatico(Boolean naoReposicionarAutomatico) {
		this.naoReposicionarAutomatico = naoReposicionarAutomatico;
	}

	public void incrementarPosicao() {
		this.numPosicao++; 
	}
}