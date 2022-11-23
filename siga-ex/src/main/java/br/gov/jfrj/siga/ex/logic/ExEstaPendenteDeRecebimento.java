package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.ExTramiteBL.Pendencias;

public class ExEstaPendenteDeRecebimento implements Expression {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExEstaPendenteDeRecebimento(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;

		if (this.mob.isGeral()) {
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

		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		// Verifica se existe recebimento pendente para titular ou lotaTitular
		Pendencias p = mob.calcularTramitesPendentes();
		boolean f = false;
		for (ExMovimentacao tramite : p.tramitesPendentes) {
			if (tramite.isRespExato(titular, lotaTitular))
				return true;
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigo() + (result ? "" : JLogic.NOT) + " está pendente de recebimento por "
				+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta();
	}
};
