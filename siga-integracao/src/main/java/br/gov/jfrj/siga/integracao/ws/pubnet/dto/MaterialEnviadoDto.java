package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "enviado")
public class MaterialEnviadoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "enviado";
	
	@JsonProperty("id")
	private String idMaterial;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("publicacao_identificador")
	private Long publicacaoId;
	
	@JsonProperty("eventolicitacao_identificador")
	private Long eventoLicitacaoId;
	
	@JsonProperty("statusPublicacao")
	private Integer statusPublicacao;
	
	@JsonProperty("dataprevista")
	private String dataPrevista;
	
	@JsonProperty("datapublicacao")
	private String dataPublicacao;
	
	@JsonProperty("comprovanteenvio")
	private Long comprovanteEnvio;
	
	@JsonProperty("nome_arquivo")
	private String nomeArquivo;
	
	@JsonProperty("recibo")
	private String recibo;

	public String getIdMaterial() {
		return idMaterial;
	}

	public void setIdMaterial(String idMaterial) {
		this.idMaterial = idMaterial;
	}

	public Integer getRowOrder() {
		return rowOrder;
	}

	public void setRowOrder(Integer rowOrder) {
		this.rowOrder = rowOrder;
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

	public Integer getStatusPublicacao() {
		return statusPublicacao;
	}

	public void setStatusPublicacao(Integer statusPublicacao) {
		this.statusPublicacao = statusPublicacao;
	}

	public String getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(String dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
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

	public String getRecibo() {
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}
	
}
