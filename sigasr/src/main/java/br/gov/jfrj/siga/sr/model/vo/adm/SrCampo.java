package br.gov.jfrj.siga.sr.model.vo.adm;

public class SrCampo {
	
	public static int CAMPO_TEXTO = 1;
	
	private String label;
	
	private String propriedade;
	
	private SrTipoCampo tipo;

	public SrCampo(String label, String propriedade, SrTipoCampo tipo) {
		super();
		this.label = label;
		this.propriedade = propriedade;
		this.tipo = tipo;
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

	public SrTipoCampo getTipo() {
		return tipo;
	}

	public void setTipo(SrTipoCampo tipo) {
		this.tipo = tipo;
	}

}
