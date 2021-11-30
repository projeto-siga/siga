package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodePublicarPortalDaTransparencia extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodePublicarPortalDaTransparencia(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				new ExEstaFinalizado(mob.doc()),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExTemMovimentacaoNaoCanceladaDoTipo(mob.doc(),
						ExTipoDeMovimentacao.PUBLICACAO_PORTAL_TRANSPARENCIA)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.PUBLICACAO_PORTAL_TRANSPARENCIA));

	}
};
