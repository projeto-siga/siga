package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCancelarMovimentacaoPorConfiguracao implements Expression {

	private ITipoDeMovimentacao tpMov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeCancelarMovimentacaoPorConfiguracao(ITipoDeMovimentacao tpMov, DpPessoa titular,
			DpLotacao lotaTitular) {
		this.tpMov = tpMov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return Ex.getInstance().getConf().podePorConfiguracao(titular, lotaTitular, tpMov,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("pode cancelar movimentação por configuração", result);
	}
};
