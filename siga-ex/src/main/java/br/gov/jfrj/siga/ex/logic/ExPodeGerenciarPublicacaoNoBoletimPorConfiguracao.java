
package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeGerenciarPublicacaoNoBoletimPorConfiguracao extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeGerenciarPublicacaoNoBoletimPorConfiguracao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível, com base em configuração, utilizar rotina para
	 * redefinição de permissões de publicação no Boletim. Não é utilizado o
	 * parâmetro mob.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return And.of(

				new CpNaoENulo(lotaTitular, "lotação titular"),

				new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.GERENCIAR_PUBLICACAO_BOLETIM));
	}
};
