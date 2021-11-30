package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExETitular implements Expression {

	private ExDocumento doc;
	private DpPessoa titular;

	public ExETitular(ExDocumento doc, DpPessoa titular) {
		this.doc = doc;
		this.titular = titular;
	}

	@Override
	public boolean eval() {
		return Utils.equivale(doc.getTitular(), titular);
	}

	@Override
	public String explain(boolean result) {
		return titular.getSiglaCompleta() + (result ? "" : JLogic.NOT) + " é titular de " + doc.getCodigo();
	}
};
