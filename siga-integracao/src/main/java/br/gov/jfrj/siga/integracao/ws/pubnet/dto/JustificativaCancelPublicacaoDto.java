package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "justificativa")
public class JustificativasCancelamentoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "justificativa";
	
	@JsonProperty("id")
	private String idJustificativa;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("justificativa_identificador")
	private Integer justificativaId;
	
	@JsonProperty("justificativa_descricao")
	private String justificativaDescr;

	public String getIdJustificativa() {
		return idJustificativa;
	}

	public void setIdJustificativa(String idJustificativa) {
		this.idJustificativa = idJustificativa;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
	}

	public Integer getJustificativaId() {
		return justificativaId;
	}

	public void setJustificativaId(Integer justificativaId) {
		this.justificativaId = justificativaId;
	}

	public String getJustificativaDescr() {
		return justificativaDescr;
	}

	public void setJustificativaDescr(String justificativaDescr) {
		this.justificativaDescr = justificativaDescr;
	}

}
