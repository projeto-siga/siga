package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpEquivaleENaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;

public class ExPodeAcessarNivel30 extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar o documento que contém o móbil passado por
	 * parâmetro. Não é checado o nível de acesso. Presume-se que o documento seja
	 * limitado de pessoa para subsecretaria. <i>Uma das</i> seguintes condições tem
	 * de ser satisfeita:
	 * <ul>
	 * <li>Usuário é o próprio cadastrante do documento</li>
	 * <li>Usuário é o subscritor do documento</li>
	 * <li>Usuário é o titular do documento</li>
	 * <li>Usuário pertence à subsecretaria da lotação destinatária do documento
	 * </li>
	 * <li><i>pessoaJaTeveAcesso()</i> é verdadeiro para esses parâmetros</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public ExPodeAcessarNivel30(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		DpLotacao subLotaTitular = Ex.getInstance().getComp().getSubsecretaria(lotaTitular);
		DpLotacao subLotaDest = Ex.getInstance().getComp().getSubsecretaria(mob.doc().getLotaDestinatario());

		return Or.of(new CpEquivaleENaoENulo(mob.doc().getCadastrante(), "cadastrante", titular, "titular"),
				new CpEquivaleENaoENulo(mob.doc().getSubscritor(), "subscritor", titular, "titular"),
				new CpEquivaleENaoENulo(mob.doc().getTitular(), "titular do documento", titular, "titular"),
				new CpEquivaleENaoENulo(subLotaTitular, "subsecretaria titular", subLotaDest, "subsecretaria destinatária"));
	}
}