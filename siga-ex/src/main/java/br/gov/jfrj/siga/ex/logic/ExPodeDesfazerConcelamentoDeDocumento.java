package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpEGovSP;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeDesfazerConcelamentoDeDocumento extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeDesfazerConcelamentoDeDocumento(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				new ExEEletronico(mob.doc()),

				new ExEstaCancelado(mob.doc()),

				Or.of(

						new ExECadastrante(mob.doc(), titular, lotaTitular),

						And.of(

								Not.of(new ExEExternoCapturado(mob.doc())),

								new ExESubscritor(mob.doc(), titular, lotaTitular))

				));
	}
};
