package br.gov.jfrj.siga.arquivo;

import java.util.List;

import com.amazonaws.services.s3.model.PartETag;

public class SigaAmazonS3ParametrosUpload {
	private String arquivoNome;
	private String arquivoNomeS3;
	private String uploadId;
	private String sequencia;
	private String tamanho;	
	private String hash;
	private PartETag partETag;
	private List<Object> partETags;

	public String getArquivoNome() {
		return arquivoNome;
	}
	public void setArquivoNome(String arquivoNome) {
		this.arquivoNome = arquivoNome;
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public String getSequencia() {
		return sequencia;
	}
	public void setSequencia(String sequencia) {
		this.sequencia = sequencia;
	}
	public PartETag getPartETag() {
		return partETag;
	}
	public void setPartETag(PartETag partETag) {
		this.partETag = partETag;
	}
	public List<Object> getPartETags() {
		return partETags;
	}
	public void setPartETags(List<Object> partETags) {
		this.partETags = partETags;
	}
	public String getArquivoNomeS3() {
		return arquivoNomeS3;
	}
	public void setArquivoNomeS3(String arquivoNomeS3) {
		this.arquivoNomeS3 = arquivoNomeS3;
	}
	public String getTamanho() {
		return tamanho;
	}
	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
}
