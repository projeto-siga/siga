package br.gov.jfrj.siga.uteis;

public enum SiglaDocumentoType {

	//RTP("RTP"),MTP("MTP"),STP("STP");
	RTP("R"),MTP("M"),STP("S");
	
	private String descricao;
	
	SiglaDocumentoType(String descricao){
		this.setDescricao(descricao);
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
}
