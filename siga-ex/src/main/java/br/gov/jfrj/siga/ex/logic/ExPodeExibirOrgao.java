package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeExibirOrgao extends CompositeExpressionSupport{

	private DpPessoa titular;
	private DpLotacao lotaTitular; 
	
	public ExPodeExibirOrgao(DpPessoa titular, DpLotacao lotaTitular) {
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular)
				.withIdTpConf(ExTipoDeConfiguracao.RESTRINGIR_VINCULACAO_DO_ORGAO_NO_CAMPO_BUSCAR).withPessoaObjeto(titular);
	}
	
}
