package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExPapel;

public class ExTemPerfil implements Expression {

	private ExDocumento doc;
	private long papel;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExTemPerfil(ExDocumento doc, long papel, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.papel = papel;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return doc.temPerfil(titular, lotaTitular, papel);
	}

	@Override
	public String explain(boolean result) {
		return (titular != null && lotaTitular != null
				? titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta()
				: titular != null ? titular.getSiglaCompleta()
						: lotaTitular != null ? lotaTitular.getSiglaCompleta() : "")

				+ (result ? "" : JLogic.NOT) + " tem perfil de " + ExPapel.nomeDoPerfil(papel)

				+ " em " + doc.getCodigo();
	}
};
