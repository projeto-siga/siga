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

public class ExPodeTransferir extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeTransferir(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível a uma lotação, com base em configuração, receber móbil
	 * de documento não assinado. Não é aqui verificado se o móbil está realmente
	 * pendente de assinatura
	 * 
	 * @param pessoa
	 * @param lotacao
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {

		return And.of(

				new ExPodeSerTransferido(mob),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentar(mob, titular, lotaTitular),
				
				Not.of(new ExEstaJuntadoAOutroProcesso(mob)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.TRANSFERENCIA));

	}
}