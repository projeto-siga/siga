package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "hash")
public class TokenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "hash";
	
	@JsonProperty("id")
	private String idJustificativa;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("token")
	private String token;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
