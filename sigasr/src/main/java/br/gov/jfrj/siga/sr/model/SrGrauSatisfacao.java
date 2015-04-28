package br.gov.jfrj.siga.sr.model;


public enum SrGrauSatisfacao {

	MUITO_RUIM(0, "Muito ruim"), RUIM(1, "Ruim"), REGULAR(2, "Regular"), BOM(3,
			"Bom"), MUITO_BOM(4, "Muito bom"), EXCELENTE(5, "Excelente");

	public long idGrauSatisfacao;
	public String descrGrauSatisfacao;


	SrGrauSatisfacao(int id, String descricao) {
		this.idGrauSatisfacao = id;
		this.descrGrauSatisfacao = descricao;
	}
	


}
