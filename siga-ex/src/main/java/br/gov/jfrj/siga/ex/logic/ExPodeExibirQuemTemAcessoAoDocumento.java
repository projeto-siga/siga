package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeExibirQuemTemAcessoAoDocumento implements Expression {
	ExModelo mod;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível exibir quem tem acesso a um documento limitado.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mod
	 * @return
	 */
	public ExPodeExibirQuemTemAcessoAoDocumento(ExModelo mod, DpPessoa titular, DpLotacao lotaTitular) {
		this.mod = mod;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return Ex.getInstance().getComp().getConf().podePorConfiguracao(null, null, null, null,
				mod.getExFormaDocumento(), mod, null, null, null, titular.getCargo(), titular.getOrgaoUsuario(),
				titular.getFuncaoConfianca(), lotaTitular, titular, null, null,
				ExTipoDeConfiguracao.EXIBIR_QUEM_TEM_ACESSO_DOCUMENTO_LIMITADO, null, lotaTitular, null, null, null,
				null);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("pode exibir quem está com o documento", result);
	}

}
