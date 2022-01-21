package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeAcessarPublico implements Expression {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar documento público. Sempre verdade.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public ExPodeAcessarPublico(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return true;
	}

	@Override
	public String explain(boolean result) {
		return "documento público";
	}
}