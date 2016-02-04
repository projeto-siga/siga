package br.gov.jfrj.siga.sr.model;

public enum SrFormaAtendimentoAcao {

	PRESENCIAL(0, "Presencial"), REMOTO(1, "Remoto");
	
	private long idFormaAtendimentoAcao;
	private String descFormaAtendimentoAcao;
	
	private SrFormaAtendimentoAcao(int id, String descricao) {
		this.idFormaAtendimentoAcao = id;
		this.descFormaAtendimentoAcao = descricao;
	}

	public long getIdFormaAtendimentoAcao() {
		return idFormaAtendimentoAcao;
	}

	public void setIdFormaAtendimentoAcao(long idFormaAtendimentoAcao) {
		this.idFormaAtendimentoAcao = idFormaAtendimentoAcao;
	}

	public String getDescFormaAtendimentoAcao() {
		return descFormaAtendimentoAcao;
	}

	public void setDescFormaAtendimentoAcao(String descFormaAtendimentoAcao) {
		this.descFormaAtendimentoAcao = descFormaAtendimentoAcao;
	}
}
