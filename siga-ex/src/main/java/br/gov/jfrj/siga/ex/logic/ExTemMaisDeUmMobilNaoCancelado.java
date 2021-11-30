package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExTemMaisDeUmMobilNaoCancelado implements Expression {
	ExDocumento doc;
	ExMovimentacao exUltMovNaoCanc;
	ExMovimentacao exUltMov;

	public ExTemMaisDeUmMobilNaoCancelado(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		int c = 0;
		for (ExMobil outroMobil : doc.getExMobilSet()) {
			if (!outroMobil.isGeral() && !outroMobil.isCancelada()) {
				c++;
			}
		}
		return c > 1;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem mais de uma via não cancelada", result);
	}

}
