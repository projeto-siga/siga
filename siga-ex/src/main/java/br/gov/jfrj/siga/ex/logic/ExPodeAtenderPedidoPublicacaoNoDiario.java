package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeAtenderPedidoPublicacaoNoDiario extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível, com base em configuração, utilizar a rotina de
	 * atendimento de pedidos indiretos de publicação no DJE. Não é utilizado o
	 * parãmetro mob.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeAtenderPedidoPublicacaoNoDiario(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
//		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular)
				.withIdTpConf(ExTipoDeConfiguracao.ATENDER_PEDIDO_PUBLICACAO);
	}
}