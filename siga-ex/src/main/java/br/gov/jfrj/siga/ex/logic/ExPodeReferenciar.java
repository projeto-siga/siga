package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeReferenciar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeReferenciar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível vincular este móbil a outro, conforme as regras:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Móbil não pode estar em trãnsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
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

				new ExEMobilGeral(mob),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaCancelado(mob.doc())),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.REFERENCIA)
						.withExMod(mob.doc().getExModelo()));
	}
}