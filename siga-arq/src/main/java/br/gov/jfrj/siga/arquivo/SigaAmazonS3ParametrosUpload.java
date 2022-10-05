package br.gov.jfrj.siga.arquivo;

public class SigaAmazonS3ParametrosUpload {
	private String arquivoNome;
	private String arquivoNomeS3;
	private String uploadId;
	private String tamanho;	
	private String hash;
	private String token;	

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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
