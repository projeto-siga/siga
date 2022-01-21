package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExESubscritorDeDespacho implements Expression {

	private ExDocumento doc;
	private DpPessoa titular;

	public ExESubscritorDeDespacho(ExDocumento doc, DpPessoa titular) {
		this.doc = doc;
		this.titular = titular;
	}

	@Override
	public boolean eval() {
		return doc.getSubscritorDespacho().contains(titular);
	}

	@Override
	public String explain(boolean result) {
		return titular.getSiglaCompleta() + (result ? "" : JLogic.NOT) + " é subscritor de despacho";
	}
};
