package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeReceberPorConfiguracao extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeReceberPorConfiguracao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se a lotação ou pessoa tem permissão para receber documento
	 * 
	 * @param pessoa
	 * @param lotacao
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {

		return new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
				.withExTpMov(ExTipoDeMovimentacao.RECEBIMENTO).withExMod(mob.doc().getExModelo());

	}
}