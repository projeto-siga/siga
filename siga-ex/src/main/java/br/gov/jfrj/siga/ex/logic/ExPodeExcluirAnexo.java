package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeExcluirAnexo extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExMovimentacao mov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeExcluirAnexo(ExMobil mob, ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível excluir uma movimentação de anexação, representada por
	 * mov, conforme as regras a seguir:
	 * <ul>
	 * <li>Anexação não pode estar cancelada</li>
	 * <li>Anexo não pode estar assinado</li>
	 * <li>Se o documento for físico, não pode estar finalizado</li>
	 * <li>Se o documento for eletrônico, não pode estar assinado</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Excluir
	 * Anexo</li>
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

				Not.of(new ExMovimentacaoEstaAssinada(mov)),

				NAnd.of(new ExEstaFinalizado(mob.doc()), new ExEFisico(mob.doc())),

				new ExMovimentacaoELotaCadastrante(mov, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.EXCLUIR_ANEXO));
	}
}
