package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeRemeterParaPublicacaoSolicitadaNoDiario extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível fazer o agendamento de publicação solicitada
	 * indiretamente. Basta haver permissão para atender pedido de publicação e
	 * estar com publicação indireta solicitada o documento a que pertence o móbil
	 * passado por parâmetro.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeRemeterParaPublicacaoSolicitadaNoDiario(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				new ExEstaSolicitadaPublicacaoNoDiario(doc),

				new ExMobPodeAtenderPedidoPublicacao(mob, titular, lotaTitular));
	}
}