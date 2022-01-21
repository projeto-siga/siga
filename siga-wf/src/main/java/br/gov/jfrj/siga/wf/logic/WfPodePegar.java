package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfPodePegar extends CompositeExpressionSupport {

	private WfProcedimento pi;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodePegar(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		this.pi = pi;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(new WfEstaResponsavel(pi, titular, lotaTitular),
				Not.of(new WfEstaPessoalmenteResponsavel(pi, titular)));
	}
};