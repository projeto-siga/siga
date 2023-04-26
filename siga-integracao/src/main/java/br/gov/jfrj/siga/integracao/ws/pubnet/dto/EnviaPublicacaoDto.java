package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "recibo")
public class EnviaPublicacaoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "recibo";
	
	@JsonProperty("id")
	private String idReciboPublicacao;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("retorno")
	private String codRetorno;
	
	@JsonProperty("descricao")
	private String descricao;
	
	@JsonProperty("publicacao_identificador")
	private Long publicacaoId;
	
	@JsonProperty("eventolicitacao_identificador")
	private Long eventoLicitacaoId;
	
	@JsonProperty("statuspublicacao")
	private Integer statusPublicacaoId;
	
	@JsonProperty("recibo")
	private String recibo;
	
	@JsonProperty("comprovanteenvio")
	private Long comprovanteEnvio;
	
	@JsonProperty("nome_arquivo")
	private String nomeArquivo;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getPublicacaoId() {
		return publicacaoId;
	}

	public void setPublicacaoId(Long publicacaoId) {
		this.publicacaoId = publicacaoId;
	}

	public Long getEventoLicitacaoId() {
		return eventoLicitacaoId;
	}

	public void setEventoLicitacaoId(Long eventoLicitacaoId) {
		this.eventoLicitacaoId = eventoLicitacaoId;
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

	public Long getComprovanteEnvio() {
		return comprovanteEnvio;
	}

	public void setComprovanteEnvio(Long comprovanteEnvio) {
		this.comprovanteEnvio = comprovanteEnvio;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
