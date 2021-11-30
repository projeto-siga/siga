package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeSerSubscritorDeModelo extends CompositeExpressionSupport {

	private ExModelo mod;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeSerSubscritorDeModelo(ExModelo mod, DpPessoa titular, DpLotacao lotaTitular) {
		this.mod = mod;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível ser subscritor de um documento.
	 */
	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mod).withExFormaDoc(mod.getExFormaDocumento())
				.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
				.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO);
	}
}
