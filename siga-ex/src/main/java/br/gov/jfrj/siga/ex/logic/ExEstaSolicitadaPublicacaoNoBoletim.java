package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaSolicitadaPublicacaoNoBoletim implements Expression {
	ExDocumento doc;

	public ExEstaSolicitadaPublicacaoNoBoletim(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isPublicacaoBoletimSolicitada();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está agendada a publicação no Boletim", result);
	}

}
