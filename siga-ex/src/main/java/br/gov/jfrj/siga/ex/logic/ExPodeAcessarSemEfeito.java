package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpEquivale;
import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeAcessarSemEfeito extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * * Retorna se é possível acessar um documento ao qual pertence o móbil passado
	 * por parâmetro. Considera se o documento está sem efeito (não estando sem
	 * efeito, retorna verdadeiro) e se <i>uma das</i> seguintes condições é
	 * satisfeita:
	 * <ul>
	 * <li>Usuário é da lotação cadastrante do documento</li>
	 * <li>Usuário é subscritor do documento</li>
	 * <li>Usuário é titular do documento</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public ExPodeAcessarSemEfeito(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(new CpNaoENulo(mob, mob != null ? mob.getSigla() : "via ou volume"),
				Or.of(Not.of(new ExEstaSemEfeito(mob.doc())),
						new CpEquivale(mob.doc().getLotaCadastrante(), "lotação cadastrante", lotaTitular,
								"lotação titular")),
				new CpEquivale(mob.doc().getSubscritor(), "subscritor", titular, "titular"),
				new CpEquivale(mob.doc().getTitular(), "titular do documento", titular, "titular"));
	}
}