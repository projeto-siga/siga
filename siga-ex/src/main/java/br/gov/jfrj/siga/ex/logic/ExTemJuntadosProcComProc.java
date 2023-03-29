package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemJuntadosProcComProc implements Expression {
	ExDocumento doc;

	public ExTemJuntadosProcComProc(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.temJuntadosProcComProc();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem processos juntados", result);
	}

}
