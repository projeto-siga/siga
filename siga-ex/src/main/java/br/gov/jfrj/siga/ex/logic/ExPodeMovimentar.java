package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeMovimentar extends CompositeExpressionSupport {

	private ExMobil mob;
	private ITipoDeMovimentacao tpMov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeMovimentar(ExMobil mob, ITipoDeMovimentacao tpMov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;

		if (this.mob != null && this.mob.isGeral()) {
			if (this.mob.doc().isProcesso())
				this.mob = this.mob.doc().getUltimoVolume();
			else {
				for (ExMobil m : this.mob.doc().getExMobilSet()) {
					if (!m.isGeral() && m.isAtendente(titular, lotaTitular)) {
						this.mob = m;
						break;
					}
				}
			}
		}

		this.tpMov = tpMov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	public ExPodeMovimentar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this(mob, null, titular, lotaTitular);
	}

	@Override
	protected Expression create() {
		if (tpMov != null)
			return And.of(

					new CpNaoENulo(mob, "móbile"),

					new ExEstaResponsavel(mob, titular, lotaTitular),

					new ExPodeMovimentarPorConfiguracao(tpMov, titular, lotaTitular));

		return And.of(

				new CpNaoENulo(mob, "móbile"),

				new ExPodeSerMovimentado(mob, titular, lotaTitular),

				new ExEstaResponsavel(mob, titular, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR));
	}
};