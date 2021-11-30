package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeReceberDocumentoSemAssinatura extends CompositeExpressionSupport {

	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeReceberDocumentoSemAssinatura(DpPessoa titular, DpLotacao lotaTitular) {
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível a uma lotação, com base em configuração, receber móbil
	 * de documento não assinado. Não é aqui verificado se o móbil está realmente
	 * pendente de assinatura
	 * 
	 * @param pessoa
	 * @param lotacao
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {

		return new ExPodePorConfiguracao(titular, lotaTitular)
				.withIdTpConf(ExTipoDeConfiguracao.RECEBER_DOC_NAO_ASSINADO);

	}
}