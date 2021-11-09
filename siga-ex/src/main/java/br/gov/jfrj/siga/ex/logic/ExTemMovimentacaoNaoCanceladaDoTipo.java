package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemMovimentacaoNaoCanceladaDoTipo implements Expression {
	private ExDocumento doc;
	private long tipo;

	public ExTemMovimentacaoNaoCanceladaDoTipo(ExDocumento doc, long tipo) {
		this.doc = doc;
		this.tipo = tipo;
	}

	@Override
	public boolean eval() {
		return doc.getMobilGeral().getMovsNaoCanceladas(tipo).size() > 0;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem movimentação não cancelada do tipo " + tipo, result);
	}

}
