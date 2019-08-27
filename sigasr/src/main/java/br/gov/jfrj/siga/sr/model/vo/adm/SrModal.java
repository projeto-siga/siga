package br.gov.jfrj.siga.sr.model.vo.adm;

import java.util.List;

public class SrModal {
	
	private List<SrCampo> campos;
	
	public SrModal addCampo(String label, String propriedade, SrTipoCampo tipo){
		campos.add(new SrCampo(label, propriedade, tipo));
		return this;
	}

}
