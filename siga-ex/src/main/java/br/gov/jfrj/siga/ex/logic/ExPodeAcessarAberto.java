package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeAcessarAberto extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar o documento ao qual pertence o móbil passado
	 * por parâmetro. Considera se o documento ainda não foi assinado (sendo então
	 * considerado aberto; estando já assinado, retorna verdadeiro) e se <i>uma
	 * das</i> seguintes condições é satisfeita:
	 * <ul>
	 * <li>Usuário é da lotação cadastrante do documento</li>
	 * <li>Usuário é subscritor do documento</li>
	 * <li>Usuário é titular do documento</li>
	 * <li><i>podeMovimentar()</i> é verdadeiro para o usuário / móbil</li>
	 * <li>Usuário é um dos cossignatários do documento</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeAcessarAberto(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(new CpNaoENulo(mob, mob != null ? mob.getSigla() : "via ou volume"),
				new ExEstaPendenteDeAssinatura(mob.doc()), new ExEstaResponsavel(mob, titular, lotaTitular));
	}
}