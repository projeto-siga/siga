package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.dp.DpLotacao;

public class CpELotacaoAtiva implements Expression {

	private DpLotacao lota;

	public CpELotacaoAtiva(DpLotacao lota) {
		this.lota = lota;
	}

	@Override
	public boolean eval() {
		return !lota.isFechada();
	}

	@Override
	public String explain(boolean result) {
		return "a " + SigaMessages.getMessage("usuario.lotacao").toLowerCase() + " " 
			+ lota.getSiglaCompleta() + (result ? "" : JLogic.NOT) + " está ativa";
	}
};
