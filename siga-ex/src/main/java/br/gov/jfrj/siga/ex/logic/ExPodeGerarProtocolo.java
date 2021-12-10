package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeGerarProtocolo extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeGerarProtocolo(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExTemMobilPai(doc)),

				Not.of(new ExEstaCancelado(doc)),

				Not.of(new ExEstaArquivadoDoc(doc)),

				new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.GERAR_PROTOCOLO, titular,
						lotaTitular));

	}
}