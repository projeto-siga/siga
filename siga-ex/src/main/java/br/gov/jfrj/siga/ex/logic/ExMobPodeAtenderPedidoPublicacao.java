package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExMobPodeAtenderPedidoPublicacao extends CompositeExpressionSupport {
	ExMobil mob;
	DpPessoa titular;
	DpLotacao lotaTitular;

	public ExMobPodeAtenderPedidoPublicacao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular)
				.withIdTpConf(ExTipoDeConfiguracao.ATENDER_PEDIDO_PUBLICACAO);
	}
}
