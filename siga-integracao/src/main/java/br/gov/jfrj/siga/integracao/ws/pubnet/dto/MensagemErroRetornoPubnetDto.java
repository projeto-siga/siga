package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MensagemErroRetornoPubnetDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String idInformacoes;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("retorno")
	private String codRetorno;
	
	@JsonProperty("descricao")
	private String descrRetorno;

	public String getCodRetorno() {
		return codRetorno;
	}

	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}

	public String getDescrRetorno() {
		return descrRetorno;
	}

	public void setDescrRetorno(String descrRetorno) {
		this.descrRetorno = descrRetorno;
	}
}
