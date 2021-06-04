package br.gov.jfrj.siga.tp.model;

public enum EstadoRequisicao {

	REJEITADA				(-4, "REJEITADA"), 
	CANCELADA				(-3, "CANCELADA"), 
	ATENDIDA				(-2, "ATENDIDA"),
	ABERTA					(-1, "ABERTA"), 
	
	AUTORIZADA				( 0, "AUTORIZADA"),
	
	PROGRAMADA				( 1, "PROGRAMADA"),
	EMATENDIMENTO			( 2, "EM ATENDIMENTO"),
	NAOATENDIDA				( 3, "NAO ATENDIDA"), 
	ATENDIDAPARCIALMENTE	( 4, "ATENDIDA PARCIALMENTE")
	;
	
	private String descricao;
	private Integer ordem;
	
	EstadoRequisicao(int ordem, String descricao){
		this.ordem = ordem;
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
	
	public int comparar(EstadoRequisicao outroEstado) {
		return this.ordem.compareTo(outroEstado.getOrdem());
	}
	
	public Integer getOrdem() {
		return this.ordem;
	}

	public boolean podeAgendar() {
		boolean retorno = this.comparar(EstadoRequisicao.AUTORIZADA) >= 0;
		return retorno;
		
	}
	
	public static EstadoRequisicao[] valuesComboAtendimentoMissao() {
		EstadoRequisicao[] retorno = new EstadoRequisicao[2];
		
		retorno[0] = EstadoRequisicao.ATENDIDA;
		retorno[1] = EstadoRequisicao.NAOATENDIDA;
		
		return retorno;
	}

	public static EstadoRequisicao copiar(EstadoRequisicao estado) {
		if(estado.equals(EstadoRequisicao.ATENDIDA)) {
			return EstadoRequisicao.ATENDIDA;
		} else if(estado.equals(EstadoRequisicao.NAOATENDIDA)) {
			return EstadoRequisicao.NAOATENDIDA;
		}
		return null;
	}
	
	public String primeiraLetra() {
		return this.toString().substring(0, 1);
	}
	
	public EstadoRequisicao[] getValues() {
		return EstadoRequisicao.values();
	}
	
}
