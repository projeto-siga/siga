package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "permissao")
public class PermissaoPublicanteDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String NOME_NODE_JSON = "permissao";

	@JsonProperty("id")
	private String idPermissao;
	
	@JsonProperty("rowOrder")
	private Integer rowOrder;
	
	@JsonProperty("anunciante_identificador")
	private Long anuncianteId;
	
	@JsonProperty("anunciante_razaosocial")
	private String anuncianteRazaoSocial;
	
	@JsonProperty("retranca_codigo")
	private String retrancaCodigo;
	
	@JsonProperty("retranca_descricao")
	private String retrancaDescricao;
	
	@JsonProperty("tipomateria_identificador")
	private Long tipoMateriaIdr;
	
	@JsonProperty("tipomateria_ordem")
	private Long tipoMateriaOrdem;
	
	@JsonProperty("tipomateria_codigo")
	private String tipoMateriaCodigo;
	
	@JsonProperty("tipomateria_descricao")
	private String tipoMateriaDescr;
	
	@JsonProperty("caderno_identificador")
	private Integer cadernoIdentificador;


	
	public PermissaoPublicanteDto(String idPermissao, Integer rowOrder, Long anuncianteId, String anuncianteRazaoSocial, String retrancaCodigo, String retrancaDescricao,
			Long tipoMateriaIdr, Long tipoMateriaOrdem, String tipoMateriaCodigo, String tipoMateriaDescr, Integer cadernoIdentificador) {
		this.idPermissao = idPermissao;
		this.rowOrder = rowOrder;
		this.anuncianteId = anuncianteId;
		this.anuncianteRazaoSocial = anuncianteRazaoSocial;
		this.retrancaCodigo = retrancaCodigo;
		this.retrancaDescricao = retrancaDescricao;
		this.tipoMateriaIdr = tipoMateriaIdr;
		this.tipoMateriaOrdem = tipoMateriaOrdem;
		this.tipoMateriaCodigo = tipoMateriaCodigo;
		this.tipoMateriaDescr = tipoMateriaDescr;
		this.cadernoIdentificador = cadernoIdentificador;
	}

//	@Override
//	public String toString() {
//		return "PermissaoPublicanteDto [idPermissao=" + idPermissao + ", rowOrder=" + rowOrder + ", anuncianteId="
//				+ anuncianteId + "]";
//	}
//	
//	publi String toString() {
//	        return "[idPermissao: " + idPermissao + " rowOrder:" + "rowOrder" + " anuncianteId:" + anuncianteId + " anuncianteRazaoSocial:" + anuncianteRazaoSocial + " retrancaCodigo:" + retrancaCodigo 
//	        		+ " retrancaDescricao:" + retrancaDescricao + " tipoMateriaIdr:" + tipoMateriaIdr + " tipoMateriaOrdem:" + tipoMateriaOrdem + " tipoMateriaCodigo:" + tipoMateriaCodigo 
//	        		+ " tipoMateriaDescr:" + tipoMateriaDescr + " cadernoIdentificador:" + cadernoIdentificador + 
//	        		"]";
//	    }


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

	public Long getAnuncianteId() {
		return anuncianteId;
	}

	public void setAnuncianteId(Long anuncianteId) {
		this.anuncianteId = anuncianteId;
	}

	public String getAnuncianteRazaoSocial() {
		return anuncianteRazaoSocial;
	}

	public void setAnuncianteRazaoSocial(String anuncianteRazaoSocial) {
		this.anuncianteRazaoSocial = anuncianteRazaoSocial;
	}

	public String getRetrancaCodigo() {
		return retrancaCodigo;
	}

	public void setRetrancaCodigo(String retrancaCodigo) {
		this.retrancaCodigo = retrancaCodigo;
	}

	public String getRetrancaDescricao() {
		return retrancaDescricao;
	}

	public void setRetrancaDescricao(String retrancaDescricao) {
		this.retrancaDescricao = retrancaDescricao;
	}

	public Long getTipoMateriaIdr() {
		return tipoMateriaIdr;
	}

	public void setTipoMateriaIdr(Long tipoMateriaIdr) {
		this.tipoMateriaIdr = tipoMateriaIdr;
	}

	public Long getTipoMateriaOrdem() {
		return tipoMateriaOrdem;
	}

	public void setTipoMateriaOrdem(Long tipoMateriaOrdem) {
		this.tipoMateriaOrdem = tipoMateriaOrdem;
	}

	public String getTipoMateriaCodigo() {
		return tipoMateriaCodigo;
	}

	public void setTipoMateriaCodigo(String tipoMateriaCodigo) {
		this.tipoMateriaCodigo = tipoMateriaCodigo;
	}

	public String getTipoMateriaDescr() {
		return tipoMateriaDescr;
	}

	public void setTipoMateriaDescr(String tipoMateriaDescr) {
		this.tipoMateriaDescr = tipoMateriaDescr;
	}

	public Integer getCadernoIdentificador() {
		return cadernoIdentificador;
	}

	public void setCadernoIdentificador(Integer cadernoIdentificador) {
		this.cadernoIdentificador = cadernoIdentificador;
	}



}

