package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemMovimentacaoNaoCanceladaDoTipo implements Expression {
	private ExMobil mob;
	private ITipoDeMovimentacao tipo;

	public ExTemMovimentacaoNaoCanceladaDoTipo(ExDocumento doc, ITipoDeMovimentacao tipo) {
		this.mob = doc.getMobilGeral();
		this.tipo = tipo;
	}

	public ExTemMovimentacaoNaoCanceladaDoTipo(ExMobil mob, ITipoDeMovimentacao tipo) {
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
