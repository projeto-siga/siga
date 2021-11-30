package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExESubscritorOuCossignatario extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;

	public ExESubscritorOuCossignatario(ExDocumento doc, DpPessoa titular) {
		this.doc = doc;
		this.titular = titular;
	}

	@Override
	protected Expression create() {
		return Or.of(new ExESubscritor(doc, titular), new ExECossignatario(doc, titular));
	}
};