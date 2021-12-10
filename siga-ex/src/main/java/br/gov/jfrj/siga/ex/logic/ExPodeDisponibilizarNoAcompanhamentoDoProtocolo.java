package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeDisponibilizarNoAcompanhamentoDoProtocolo extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeDisponibilizarNoAcompanhamentoDoProtocolo(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular).withExMod(doc.getExModelo())
				.withExFormaDoc(doc.getExFormaDocumento()).withExTpDoc(doc.getExTipoDocumento())
				.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
				.withExTpMov(ExTipoDeMovimentacao.EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO);
	}
};
