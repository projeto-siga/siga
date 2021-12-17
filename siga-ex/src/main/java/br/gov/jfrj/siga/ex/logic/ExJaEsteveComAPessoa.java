package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExJaEsteveComAPessoa implements Expression {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExJaEsteveComAPessoa(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		for (ExMobil m : doc.getExMobilSet()) {
			for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
				if ((mov.getLotaResp() != null && mov.getLotaResp().equivale(lotaTitular) && mov.getResp() == null)
						|| (mov.getResp() != null && mov.getResp().equivale(titular)))
					return true;
				if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.REDEFINICAO_NIVEL_ACESSO
						&& ((mov.getLotaCadastrante() != null && mov.getLotaCadastrante().equivale(lotaTitular)
								&& mov.getCadastrante() == null)
								|| (mov.getCadastrante() != null && mov.getCadastrante().equivale(titular))))
					return true;
			}
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return doc.getCodigo() + (result ? "" : JLogic.NOT) + " esteve com a pessoa "
				+ (titular != null ? titular.getSiglaCompleta() : "");
	}
};
