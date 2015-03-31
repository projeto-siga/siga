package models;


public enum SrTipoMotivoPendencia {

	AGUARDANDO_PRIORIZACAO(1, "Aguardando priorização", false),
	AGUARDANDO_RECURSO_EXTERNO(2, "Aguardando recurso externo", false),
	USUARIO_INDISPONIVEL(3, "Usuário indisponível", false),
	AGUARDANDO_RESPOSTA(4, "Aguardando resposta do usuário", false);

	public int nivelTipoMotivoPendencia;

	public String descrTipoMotivoPendencia;

	public boolean suspendeTempoAtendimento;

	private SrTipoMotivoPendencia(int nivelUrgencia, String descrUrgencia) {
		this(nivelUrgencia, descrUrgencia, false);
	}

	SrTipoMotivoPendencia(int nivel, String descricao, boolean suspende) {
		this.nivelTipoMotivoPendencia = nivel;
		this.descrTipoMotivoPendencia = descricao;
		this.suspendeTempoAtendimento = suspende;
	}

}
