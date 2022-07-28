package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeCancelarAnexo extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExMovimentacao mov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeCancelarAnexo(ExMobil mob, ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível cancelar uma movimentação mov, de anexação de arquivo.
	 * Regras:
	 * <ul>
	 * <li>Anexação não pode estar cancelada</li>
	 * <li>Não pode mais ser possível <i>excluir</i> a anexação</li>
	 * <li>Se o documento for físico, anexação não pode ter sido feita antes da
	 * finalização</li>
	 * <li>Se o documento for digital, anexação não pode ter sido feita antes da
	 * assinatura</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante da movimentação</li>
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

				Not.of(new ExMovimentacaoEstaCancelada(mov)),

				Not.of(new ExPodeExcluirAnexo(mob, mov, titular, lotaTitular)),

				new ExMovimentacaoELotaCadastrante(mov, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO)
						.withExTpMov(ExTipoDeMovimentacao.ANEXACAO));
	}
}
