package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExEMobilAutuado implements Expression {

	private ExDocumento docPai;
	private ExMobil mob;

	public ExEMobilAutuado(ExDocumento docPai, ExMobil mob) {
		this.docPai = docPai;
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob != null && docPai != null && mob.equals(docPai.getExMobilAutuado());
	}

	@Override
	public String explain(boolean result) {
		// TODO Auto-generated method stub
		return null;
	}

}
