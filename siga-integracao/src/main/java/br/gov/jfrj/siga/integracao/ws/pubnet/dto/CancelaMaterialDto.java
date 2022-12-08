package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "cancelamento")
public class CancelaMaterialDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "cancelamento";

	@JsonProperty("id")
	private String idPermissao;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("retorno")
	private String codRetorno;
	
	@JsonProperty("publicacao_identificador")
	private Long publicacaoId;
	
	@JsonProperty("ComprovanteCancelamento")
	private Long comprovanteCancelamento;
	
	@JsonProperty("statusPublicacao identificador")
	private Integer statusPublicacaoId;
	
	@JsonProperty("recibo")
	private String recibo;
	
	@JsonProperty("nome_arquivo")
	private String nomeArquivo;

	public String getIdPermissao() {
		return idPermissao;
	}

	public void setIdPermissao(String idPermissao) {
		this.idPermissao = idPermissao;
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

	public Long getPublicacaoId() {
		return publicacaoId;
	}

	public void setPublicacaoId(Long publicacaoId) {
		this.publicacaoId = publicacaoId;
	}

	public Long getComprovanteCancelamento() {
		return comprovanteCancelamento;
	}

	public void setComprovanteCancelamento(Long comprovanteCancelamento) {
		this.comprovanteCancelamento = comprovanteCancelamento;
	}

	public Integer getStatusPublicacaoId() {
		return statusPublicacaoId;
	}

	public void setStatusPublicacaoId(Integer statusPublicacaoId) {
		this.statusPublicacaoId = statusPublicacaoId;
	}

	public String getRecibo() {
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
