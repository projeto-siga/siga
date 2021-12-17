package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExMobHaCancelamentoDeMovimentacaoAnterior implements Expression {
	ExMobil mob;
	private ExMovimentacao exUltMov;
	private ExMovimentacao exUltMovNaoCanc;

	public ExMobHaCancelamentoDeMovimentacaoAnterior(ExMobil mob) {
		this.mob = mob;
		this.exUltMov = mob.getUltimaMovimentacao();
		this.exUltMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
	}

	// Não deixa cancelar a mov se há um
	// cancelamento imediatamente anterior, a não ser se este for
	// cancelamento de receb transitório ou de atualização
	@Override
	public boolean eval() {
		return (exUltMovNaoCanc.getIdMov() != exUltMov.getIdMov() && exUltMov.getExMovimentacaoRef() != null
				&& exUltMov.getExMovimentacaoRef().getExTipoMovimentacao()
						 != ExTipoDeMovimentacao.RECEBIMENTO_TRANSITORIO
				&& exUltMov.getExMovimentacaoRef().getExTipoMovimentacao()
						 != ExTipoDeMovimentacao.ATUALIZACAO);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem cancelamento de movimentação anterior", result);
	}

}
