package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeArquivarPermanente extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeArquivarPermanente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível fazer o arquivamento intermediário do móbil, ou seja,
	 * se é possível mostrar o link para movimentação e se, além disso, o móbil
	 * encontra-se na lotação titular ou é digital.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {

		return And.of(

				new ExPodeExibirBotaoDeArquivarPermanente(mob, titular, lotaTitular),

				Or.of(new ExEstaResponsavel(mob, titular, lotaTitular), new ExEEletronico(mob.doc())));
	}
}