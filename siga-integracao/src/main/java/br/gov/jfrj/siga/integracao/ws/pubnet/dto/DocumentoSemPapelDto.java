package br.gov.jfrj.siga.integracao.ws.pubnet.dto;

import java.io.Serializable;

public class DocumentoSemPapelDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idMov;
	
	private String codDocSemPapel;
	
	private String siglaDocSemPapel;
	
	private String descrDocSemPapel;

	public Long getIdMov() {
		return idMov;
	}

	public void setIdMov(Long idMov) {
		this.idMov = idMov;
	}

	public String getCodDocSemPapel() {
		return codDocSemPapel;
	}

	public void setCodDocSemPapel(String codDocSemPapel) {
		this.codDocSemPapel = codDocSemPapel;
	}

	public String getSiglaDocSemPapel() {
		return siglaDocSemPapel;
	}

	public void setSiglaDocSemPapel(String siglaDocSemPapel) {
		this.siglaDocSemPapel = siglaDocSemPapel;
	}

	public String getDescrDocSemPapel() {
		return descrDocSemPapel;
	}

	public void setDescrDocSemPapel(String descrDocSemPapel) {
		this.descrDocSemPapel = descrDocSemPapel;
	}
}
