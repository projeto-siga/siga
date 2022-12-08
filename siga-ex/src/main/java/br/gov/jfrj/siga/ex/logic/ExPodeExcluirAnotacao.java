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
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeExcluirAnotacao extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExMovimentacao mov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeExcluirAnotacao(ExMobil mob, ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível excluir anotação realizada no móbil, passada pelo
	 * parâmetro mov. As seguintes regras incidem sobre a movimentação a ser
	 * excluída:
	 * <ul>
	 * <li>Não pode estar cancelada</li>
	 * <li>Lotação do usuário tem de ser a do cadastrante ou do subscritor
	 * (responsável) da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Excluir
	 * Anotação</li>
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

				Not.of(new ExMovimentacaoEstaCancelada(mov)),

				Or.of(

						new ExMovimentacaoECadastrante(mov, titular),

						new ExMovimentacaoESubscritor(mov, titular),
						
						new ExMovimentacaoELotaCadastrante(mov, titular.getLotacao()),
						
						new ExMovimentacaoELotaSubscritor(mov, titular.getLotacao()),
				
						new ExMovimentacaoELotaTitular(mov, lotaTitular)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.EXCLUIR_ANOTACAO));
	}
}
