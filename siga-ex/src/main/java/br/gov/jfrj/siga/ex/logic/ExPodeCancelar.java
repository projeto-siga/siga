package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCancelar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMovimentacao mov;

	public ExPodeCancelar(ExMobil mob, ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível cancelar uma movimentação mov, segundo as regras
	 * abaixo. <b>Método em desuso?</b>
	 * <ul>
	 * <li>Movimentação não pode estar cancelada</li>
	 * <li>Usuário tem de ser da lotação cadastrante da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Cancelar
	 * Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEMobilCancelado(mob)),

				Or.of(

						new ExMovimentacaoEDoTipo(mov,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR),

						new ExMovimentacaoELotaCadastrante(mov, lotaTitular)),

				new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO));
	}
}
