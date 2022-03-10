package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfPodeEditarVariaveis extends CompositeExpressionSupport {

	private WfProcedimento pi;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeEditarVariaveis(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		this.pi = pi;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new WfPodeEditarDiagrama(pi.getDefinicaoDeProcedimento(), titular, lotaTitular);
	}
	
	public static void afirmar(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		WfPodeEditarVariaveis teste = new WfPodeEditarVariaveis(pi, titular, lotaTitular);
		if (!teste.eval())
			throw new AplicacaoException(
					"Não é possível editar variáveis porque " + AcaoVO.Helper.produzirExplicacao(teste, false));
	}

};