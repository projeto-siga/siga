package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaComPrazoDeAssinaturaVencido implements Expression {
	ExDocumento doc;

	public ExEstaComPrazoDeAssinaturaVencido(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isPrazoDeAssinaturaVencido();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está com prazo de assinatura vencido", result);
	}

}
