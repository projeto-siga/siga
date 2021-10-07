package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSuport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfPodeTerminar extends CompositeExpressionSuport {

	private WfProcedimento pi;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeTerminar(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		this.pi = pi;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(Not.of(new WfEstaTerminado(pi)),
				Or.of(new WfEstaResponsavel(pi, titular, lotaTitular), And.of(new WfEstaSemResponsavel(pi),
						new WfPodeEditarDiagrama(pi.getDefinicaoDeProcedimento(), titular, lotaTitular))));
	}
};