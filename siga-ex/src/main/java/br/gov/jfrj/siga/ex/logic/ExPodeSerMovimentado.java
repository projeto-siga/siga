package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeSerMovimentado extends CompositeExpressionSupport {

	private ExMobil mob;

	public ExPodeSerMovimentado(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this(mob);

		if (mob != null && mob.doc().isFinalizado() && this.mob.isGeral()) {
			if (this.mob.doc().isProcesso())
				this.mob = this.mob.doc().getUltimoVolume();
			else {
				for (ExMobil m : this.mob.doc().getExMobilSet()) {
					if (!m.isGeral() && m.isAtendente(titular, lotaTitular)) {
						this.mob = m;
						break;
					}
				}
				if (this.mob.isGeral())
					this.mob = this.mob.doc().getPrimeiraVia();
			}
		}
	}

	public ExPodeSerMovimentado(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	protected Expression create() {
		return And.of(
				
				new CpNaoENulo(mob, "via ou volume"),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

				new ExEstaFinalizado(mob.doc()),

				Not.of(new ExEMobilCancelado(mob)),

				Not.of(new ExEstaEmTransitoExterno(mob)));
	}
};