package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpEquivale;
import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeAcessarNivel20 extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar o documento que contém o móbil passado por
	 * parâmetro. Não é checado o nível de acesso. Presume-se que o documento seja
	 * limitado ao órgão. <i>Uma das</i> seguintes condições tem de ser satisfeita:
	 * <ul>
	 * <li>Órgão a que pertence a lotação passada por parâmetro tem de ser o órgão
	 * da lotação cadastrante do documento</li>
	 * <li>Usuário é o subscritor do documento, não importando de que órgão seja
	 * </li>
	 * <li>Usuário é o titular do documento, não importando de que órgão seja</li>
	 * <li>Usuário é o destinatario do documento ou da lotação deste, não importando
	 * de que órgão seja</li>
	 * <li><i>jaEsteveNoOrgao()</i> é verdadeiro para esses parâmetros</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public ExPodeAcessarNivel20(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return Or.of(
				new CpIgual(lotaTitular.getOrgaoUsuario(), "órgão da lotação titular",
						mob.doc().getLotaCadastrante().getOrgaoUsuario(), "órtão da lotação cadastrante"),
				new CpEquivale(mob.doc().getSubscritor(), "subscritor", titular, "titular"),
				new CpEquivale(mob.doc().getTitular(), "titular do documento", titular, "titular"),
				new CpEquivale(mob.doc().getDestinatario(), "destinatário", titular, "titular"),
				new CpEquivale(mob.doc().getLotaDestinatario(), "lotação destinatária", titular, "lotação titular"),
				new ExJaEsteveNoOrgao(mob.doc(), titular, lotaTitular));
	}
}