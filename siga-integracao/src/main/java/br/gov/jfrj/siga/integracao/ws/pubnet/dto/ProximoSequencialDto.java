package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "sequencial")
public class ProximoSequencialDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "sequencial";
	
	@JsonProperty("id")
	private String idProximoSeq;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("retranca")
	private Integer retrancaCodigo;
	
	@JsonProperty("sequenciaPublicacao")
	private String sequenciaPublicacao;
	
	@JsonProperty("sequenciaLicitacao")
	private String sequenciaLicitacao;

	public String getIdProximoSeq() {
		return idProximoSeq;
	}

	public void setIdProximoSeq(String idProximoSeq) {
		this.idProximoSeq = idProximoSeq;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
	}

	public Integer getRetrancaCodigo() {
		return retrancaCodigo;
	}

	public void setRetrancaCodigo(Integer retrancaCodigo) {
		this.retrancaCodigo = retrancaCodigo;
	}

	public String getSequenciaPublicacao() {
		return sequenciaPublicacao;
	}

	public void setSequenciaPublicacao(String sequenciaPublicacao) {
		this.sequenciaPublicacao = sequenciaPublicacao;
	}

	public String getSequenciaLicitacao() {
		return sequenciaLicitacao;
	}

	public void setSequenciaLicitacao(String sequenciaLicitacao) {
		this.sequenciaLicitacao = sequenciaLicitacao;
	}
	
}
