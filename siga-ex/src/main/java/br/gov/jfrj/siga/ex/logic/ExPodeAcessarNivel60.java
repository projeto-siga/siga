package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpEquivale;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeAcessarNivel60 extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar o documento que contém o móbil passado por
	 * parâmetro. Não é checado o nível de acesso. Presume-se que o documento seja
	 * limitado entre lotações. <i>Uma das</i> seguintes condições tem de ser
	 * satisfeita:
	 * <ul>
	 * <li>Usuário é da lotação cadastrante do documento</li>
	 * <li>Usuário é o subscritor do documento</li>
	 * <li>Usuário é o titular do documento</li>
	 * <li>Usuário pertence à subsecretaria da lotação destinatária do documento
	 * </li>
	 * <li>Usuário é da lotação destinatária do documento</li>
	 * <li><i>jaPassouPor()</i> é verdadeiro para esses parâmetros</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public ExPodeAcessarNivel60(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return Or.of(
				new CpEquivale(mob.doc().getLotaCadastrante(), "lotação cadastrante", lotaTitular, "lotação titular"),
				new CpEquivale(mob.doc().getSubscritor(), "subscritor", titular, "titular"),
				new CpEquivale(mob.doc().getTitular(), "titular do documento", titular, "titular"),
				new CpEquivale(mob.doc().getLotaDestinatario(), "lotação destinatária", lotaTitular, "lotação titular"),
				new ExJaEsteveNaLotacao(mob.doc(), lotaTitular));
	}
}