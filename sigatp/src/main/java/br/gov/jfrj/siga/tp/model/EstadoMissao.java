package br.gov.jfrj.siga.tp.model;

public enum EstadoMissao {

	PROGRAMADA("PROGRAMADA", 0), INICIADA("INICIADA", 1),FINALIZADA("FINALIZADA", 2), CANCELADA("CANCELADA", 3) ;
	
	private String descricao;
	private Integer ordem;
	
	EstadoMissao(String descricao, int ordem){
		this.setDescricao(descricao);
		this.setOrdem(ordem);
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	public String toString() {
		return this.name();
	}
	
	public int comparar(EstadoMissao outroEstado) {
		return this.ordem.compareTo(outroEstado.getOrdem());
	}
	
	public boolean equals(EstadoMissao outroEstado) {
		return this.descricao.equals(outroEstado.getDescricao());
	}
	
	public EstadoMissao [] getValores() {
		return EstadoMissao.values();
	}
	
	/**
	 * Sobrecarga criada para auxiliar o desenvolvimento 
	 * na camada de visao. 
	 * 
	 * @param outroEstado String de descricao do estado que se quer verificar.
	 * @return se a string passada identifica o estado
	 */
	public boolean equals(String outroEstado) {
		return this.descricao.equals(outroEstado);
	}

	public String primeiraLetra() {
		return this.toString().substring(0, 1);
	}
	
}