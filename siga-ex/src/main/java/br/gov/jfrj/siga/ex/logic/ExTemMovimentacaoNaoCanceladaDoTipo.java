package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemMovimentacaoNaoCanceladaDoTipo implements Expression {
	private ExMobil mob;
	private long tipo;

	public ExTemMovimentacaoNaoCanceladaDoTipo(ExDocumento doc, long tipo) {
		this.mob = doc.getMobilGeral();
		this.tipo = tipo;
	}

	public ExTemMovimentacaoNaoCanceladaDoTipo(ExMobil mob, long tipo) {
		this.mob = mob;
		this.tipo = tipo;
	}

	@Override
	public boolean eval() {
		return mob.getMovsNaoCanceladas(tipo).size() > 0;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem movimentação não cancelada do tipo " + tipo, result);
	}

}
