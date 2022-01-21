package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeVincular extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeVincular(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível fazer vinculação deste mobil a outro, conforme as
	 * seguintes regras para <i>este</i> móbil:
	 * <ul>
	 * <li>Precisa ser via ou volume (não pode ser geral)</li>
	 * <li>Não pode estar em trânsito</li>
	 * <li>Não pode estar juntado.</li>
	 * <li><i>podeMovimentar()</i> precisa retornar verdadeiro para ele</li>
	 * </ul>
	 * Não é levada em conta, aqui, a situação do mobil ao qual se pertende
	 * vincular.
	 * 
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

				Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaJuntado(mob)));
	}
}