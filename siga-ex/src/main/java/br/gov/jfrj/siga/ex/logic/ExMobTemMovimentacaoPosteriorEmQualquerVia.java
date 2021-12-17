package br.gov.jfrj.siga.ex.logic;

import java.util.Date;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExMobTemMovimentacaoPosteriorEmQualquerVia implements Expression {
	ExMobil mob;
	private ExMovimentacao exUltMov;
	private ExMovimentacao exUltMovNaoCanc;

	public ExMobTemMovimentacaoPosteriorEmQualquerVia(ExMobil mob) {
		this.mob = mob;
		this.exUltMov = mob.getUltimaMovimentacao();
		this.exUltMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
	}

	// Antes de deixar cancelar a assinatura, vê antes se houve
	// movimentações posteriores em qualquer via
	@Override
	public boolean eval() {
		if (exUltMovNaoCanc.getExTipoMovimentacao()
				 == ExTipoDeMovimentacao.REGISTRO_ASSINATURA_DOCUMENTO) {
			Date dt = exUltMovNaoCanc.getDtIniMov();
			for (ExMobil m : mob.doc().getExMobilSet()) {
				ExMovimentacao move = m.getUltimaMovimentacaoNaoCancelada();
				if (move != null && move.getDtIniMov().before(dt)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem movimentação posterior à última em qualquer via", result);
	}

}
