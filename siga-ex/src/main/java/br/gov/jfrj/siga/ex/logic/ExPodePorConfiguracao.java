package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.bl.Ex;

public class ExPodePorConfiguracao implements Expression {

	private ITipoDeConfiguracao idTpConf;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodePorConfiguracao(ITipoDeConfiguracao idTpConf, DpPessoa titular, DpLotacao lotaTitular) {
		this.idTpConf = idTpConf;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return Ex.getInstance().getConf().podePorConfiguracao(titular, lotaTitular, idTpConf);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("pode por configuração", result);
	}
};
