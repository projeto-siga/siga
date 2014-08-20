package models;

public enum SrFormaAtendimentoAcao {

	PRESENCIAL(0, "Presencial"), REMOTO(1, "Remoto");
	
	public long idFormaAtendimentoAcao;
	public String descFormaAtendimentoAcao;
	
	private SrFormaAtendimentoAcao(int id, String descricao) {
		this.idFormaAtendimentoAcao = id;
		this.descFormaAtendimentoAcao = descricao;
	}
}
