package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	@JsonProperty("dataRecebimento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date dataRecebimento;
	
	@JsonProperty("dataprevista")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date dataPrevista;
	
	@JsonProperty("datapublicacao")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date dataPublicacao;
	
	@JsonProperty("comprovanteenvio")
	private Long comprovanteEnvio;
	
	@JsonProperty("nome_arquivo")
	private String nomeArquivo;
	
	@JsonProperty("recibo")
	private String recibo;
	
	@JsonProperty("reciboCancelamento")
	private String reciboCancelamento;
	
	@JsonProperty("dataCancelamento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date dataCancelamento;
	
	@JsonProperty("razaoSocial")
	private String razaoSocial;
	
	private StatusPublicacaoDto statusPublicacaoDto;

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

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Date getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
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

	public String getReciboCancelamento() {
		return reciboCancelamento;
	}

	public void setReciboCancelamento(String reciboCancelamento) {
		this.reciboCancelamento = reciboCancelamento;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public StatusPublicacaoDto getStatusPublicacaoDto() {
		return statusPublicacaoDto;
	}

	public void setStatusPublicacaoDto(StatusPublicacaoDto statusPublicacaoDto) {
		this.statusPublicacaoDto = statusPublicacaoDto;
	}
}
