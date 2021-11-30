package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCancelarOuAlterarPrazoDeAssinatura extends CompositeExpressionSupport {

	private ExMovimentacao mov;
	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível cancelar ou alterar uma movimentação de definição de
	 * prazo de assinatura, passada através do parâmetro mov. As regras são as
	 * seguintes:
	 * <ul>
	 * <li>A movimentação não pode estar cancelada</li>
	 * <li>Se o documento estiver assinado, só o subscritor pode cancelar. Caso
	 * contrário, só o cadastrante.</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Cancelar
	 * Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param subscritor
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public ExPodeCancelarOuAlterarPrazoDeAssinatura(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mov = mov;
		this.mob = mov.getExMobil();
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExMovimentacaoEstaCancelada(mov)),

				If.of(

						new ExEstaAssinadoPeloSubscritorComTokenOuSenha(doc),

						new ExESubscritor(doc, titular),

						new ExMovimentacaoECadastrante(mov, titular)),

				Not.of(new ExMovimentacaoEstaAssinada(mov)), new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO).withExTpMov(mov.getIdTpMov()));
	}
}