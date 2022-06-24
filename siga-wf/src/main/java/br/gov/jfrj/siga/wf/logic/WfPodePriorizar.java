package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfPodePriorizar extends CompositeExpressionSupport {

	private WfProcedimento pi;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodePriorizar(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		this.pi = pi;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return Or.of(

				new WfEstaResponsavel(pi, titular, lotaTitular),

				new WfPodeEditarDiagrama(pi.getDefinicaoDeProcedimento(), titular, lotaTitular));

	}

	public static void afirmar(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		WfPodePriorizar teste = new WfPodePriorizar(pi, titular, lotaTitular);
		if (!teste.eval())
			throw new AplicacaoException(
					"Não é possível priorizar porque " + AcaoVO.Helper.produzirExplicacao(teste, false));
	}

};