package br.gov.jfrj.siga.tp.model;

public enum ItemMenu {
	DADOSCADASTRAIS("DADOSCADASTRAIS"), PLANTOES("PLANTOES"), AFASTAMENTOS("AFASTAMENTOS"), ESCALASDETRABALHO("ESCALASDETRABALHO"), AGENDA("AGENDA"), INFRACOES("INFRACOES"), AVARIAS("AVARIAS"), RELATORIOSDIARIOS("RELATORIOSDIARIOS"), ABASTECIMENTOS("ABASTECIMENTOS"), LOTACOES("LOTACOES");
	
	private String descricao;
	
	ItemMenu(String descricao){
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
	
	public boolean equals(ItemMenu diferenteEstado) {
		return this.descricao.equals(diferenteEstado.getDescricao());
	}
	
	/**
	 * Sobrecarga criada para auxiliar o desenvolvimento 
	 * na camada de visao. 
	 * 
	 * @param diferenteEstado String de descricao do estado que se quer verificar.
	 * @return se a string passada identifica o estado
	 */
	public boolean equals(String diferenteEstado) {
		return this.descricao.equals(diferenteEstado);
	}
	
}
