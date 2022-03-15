package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExEDocFilho implements Expression {
	ExDocumento docFilho;
	ExMobil mobPai;

	public ExEDocFilho(ExDocumento docFilho, ExMobil mobPai) {
		this.docFilho = docFilho;
		this.mobPai = mobPai;
	}

	@Override
	public boolean eval() {
		return mobPai.equals(docFilho.getExMobilPai());
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é documento filho", result);
	}

}
