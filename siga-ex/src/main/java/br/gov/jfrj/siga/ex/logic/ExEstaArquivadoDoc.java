package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaArquivadoDoc implements Expression {
	ExDocumento doc;

	public ExEstaArquivadoDoc(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isArquivado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está arquivado", result);
	}

}
