package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExJaEsteveNaLotacao implements Expression {

	private ExDocumento doc;
	private DpLotacao lotaTitular;

	public ExJaEsteveNaLotacao(ExDocumento doc, DpLotacao lotaTitular) {
		this.doc = doc;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		for (ExMobil m : doc.getExMobilSet()) {
			for (ExMovimentacao mov : m.getExMovimentacaoSet())
				if (mov.getLotaResp() != null && mov.getLotaResp().equivale(lotaTitular))
					return true;
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return doc.getCodigo() + (result ? "" : JLogic.NOT) + " esteve na lotação "
				+ (lotaTitular != null ? lotaTitular.getSiglaCompleta() : "");
	}
};
