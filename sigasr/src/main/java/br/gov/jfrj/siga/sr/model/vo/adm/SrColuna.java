package br.gov.jfrj.siga.sr.model.vo.adm;

public class SrColuna {
	
	private String label;
	
	private String propriedade;

	public SrColuna(String label, String propriedade) {
		super();
		this.label = label;
		this.propriedade = propriedade;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}
	
	

}
