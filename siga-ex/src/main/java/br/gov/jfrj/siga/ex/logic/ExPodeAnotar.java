package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSuport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeAnotar extends CompositeExpressionSuport {

//	(!mob.isEmTransitoInterno()
//		&& !mob.isEliminado()
//		&& !mob.isGeral())
//	&& getConf().podePorConfiguracao(titular, lotaTitular,
//					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO,
//					CpTipoDeConfiguracao.MOVIMENTAR);

	private ExMobil mob;
	private long idTpMov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAnotar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(Not.of(new ExEstaEmTransitoInterno(mob)), Not.of(new ExEstaEliminado(mob)),
				Not.of(new ExEMobilGeral(mob)), new ExPodeMovimentarPorConfiguracao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO, titular, lotaTitular));
	}
};