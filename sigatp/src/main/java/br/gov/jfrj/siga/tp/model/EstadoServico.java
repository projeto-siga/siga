package br.gov.jfrj.siga.tp.model;

public enum EstadoServico {
	AGENDADO("AGENDADO"), INICIADO("INICIADO"), REALIZADO("REALIZADO"), CANCELADO("CANCELADO");
	
	private String descricao;
	
	EstadoServico(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	public boolean equals(EstadoServico outroEstado) {
		return this.descricao.equals(outroEstado.getDescricao());
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
	
	public static EstadoServico[] getValuesComboIniciarServico() {
		EstadoServico[] retorno = new EstadoServico[3];
		
		retorno[0] = EstadoServico.AGENDADO;
		retorno[1] = EstadoServico.INICIADO;
		retorno[2] = EstadoServico.CANCELADO;
		
		return retorno;
	}

	public static EstadoServico[] getValuesComboFinalizarServico() {
		EstadoServico[] retorno = new EstadoServico[3];
		
		retorno[0] = EstadoServico.INICIADO;
		retorno[1] = EstadoServico.REALIZADO;
		retorno[2] = EstadoServico.CANCELADO;
		
		return retorno;
	}
}