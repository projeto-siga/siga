package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSuport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeMovimentar extends CompositeExpressionSuport {

	private ExMobil mob;
	private long idTpMov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeMovimentar(ExMobil mob, long idTpMov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.idTpMov = idTpMov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(new ExEstaResponsavel(mob, titular, lotaTitular),
				new ExPodeMovimentarPorConfiguracao(idTpMov, titular, lotaTitular));
	}
};