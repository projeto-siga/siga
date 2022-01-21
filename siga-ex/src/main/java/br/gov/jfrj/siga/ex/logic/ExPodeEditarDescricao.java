package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeEditarDescricao extends CompositeExpressionSupport {

	private ExModelo mod;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível editar a descrição de um documento, conforme
	 * configuração específica.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mod
	 * @return
	 */
	public ExPodeEditarDescricao(ExModelo mod, DpPessoa titular, DpLotacao lotaTitular) {
		this.mod = mod;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mod).withExFormaDoc(mod.getExFormaDocumento())
				.withIdTpConf(ExTipoDeConfiguracao.EDITAR_DESCRICAO);
	}
}