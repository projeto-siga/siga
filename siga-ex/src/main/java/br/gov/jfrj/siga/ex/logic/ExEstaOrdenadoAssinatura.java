package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExEstaOrdenadoAssinatura implements Expression {
	ExDocumento doc;

	public ExEstaOrdenadoAssinatura(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc != null && !doc.getMobilGeral().getMovimentacoesPorTipo(ExTipoDeMovimentacao.ORDEM_ASSINATURA, Boolean.TRUE).isEmpty();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está ordenado as assinaturas", result);
	}

}
