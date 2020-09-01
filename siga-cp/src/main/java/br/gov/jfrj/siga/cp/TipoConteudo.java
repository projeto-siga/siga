package br.gov.jfrj.siga.cp;

public enum TipoConteudo {
	
	ZIP("application/zip", "zip"),
	TXT("application/txt", "txt"),
	PDF("application/pdf", "pdf");
	
	private TipoConteudo(String mimeType ,String extensao) {
		this.mimeType = mimeType;
		this.extensao = extensao;
	}
	
	private String mimeType;
	private String extensao;
	
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public String getExtensao() {
		return extensao;
	}
	
	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
}
