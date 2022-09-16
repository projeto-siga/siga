package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "recibo")
public class MontaReciboPublicacaoCancelamentoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "recibo";
	
	@JsonProperty("id")
	private String idReciboPublicacao;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("retorno")
	private String codRetorno;
	
	@JsonProperty("TextoRecibo")
	private String textoRecibo;
	
	public String getIdReciboPublicacao() {
		return idReciboPublicacao;
	}

	public void setIdReciboPublicacao(String idReciboPublicacao) {
		this.idReciboPublicacao = idReciboPublicacao;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
	}

	public String getCodRetorno() {
		return codRetorno;
	}

	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}

	public String getTextoRecibo() {
		return textoRecibo;
	}

	public void setTextoRecibo(String textoRecibo) {
		this.textoRecibo = textoRecibo;
	}

}
