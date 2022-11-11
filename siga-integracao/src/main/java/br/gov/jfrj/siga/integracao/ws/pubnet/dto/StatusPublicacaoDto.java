package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "statusPublicacao")
public class StatusPublicacaoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "statusPublicacao";
	
	@JsonProperty("id")
	private String idListaStatusPub;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("statuspublicacao_identificador")
	private Integer statusPublicacaoId;
	
	@JsonProperty("statuspublicacao_descricao")
	private String statusPublicacaoDescr;

	public String getIdListaStatusPub() {
		return idListaStatusPub;
	}

	public void setIdListaStatusPub(String idListaStatusPub) {
		this.idListaStatusPub = idListaStatusPub;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
	}

	public Integer getStatusPublicacaoId() {
		return statusPublicacaoId;
	}

	public void setStatusPublicacaoId(Integer statusPublicacaoId) {
		this.statusPublicacaoId = statusPublicacaoId;
	}

	public String getStatusPublicacaoDescr() {
		return statusPublicacaoDescr;
	}

	public void setStatusPublicacaoDescr(String statusPublicacaoDescr) {
		this.statusPublicacaoDescr = statusPublicacaoDescr;
	}

}
