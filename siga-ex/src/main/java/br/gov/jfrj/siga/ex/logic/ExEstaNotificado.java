package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaNotificado implements Expression {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExEstaNotificado(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return mob.isNotificado(titular, lotaTitular);
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigo() + (result ? "" : JLogic.NOT) + " tem notificação pendente para "
				+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta();
	}
};
