package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExELotacaoCadastranteAtiva implements Expression {

	private ExDocumento doc;

	public ExELotacaoCadastranteAtiva(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return !doc.getLotaCadastrante().isFechada();
	}

	@Override
	public String explain(boolean result) {
		return "a " + SigaMessages.getMessage("usuario.lotacao").toLowerCase() + " " 
			+ doc.getLotaCadastrante().getSiglaCompleta() + (result ? "" : JLogic.NOT) + " está ativa";
	}
};
