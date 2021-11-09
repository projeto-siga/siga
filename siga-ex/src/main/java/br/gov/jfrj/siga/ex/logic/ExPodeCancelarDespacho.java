package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCancelarDespacho extends CompositeExpressionSupport {

	private ExMovimentacao mov;
	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * <b>(Quando é usado este método?)</b> Retorna se é possível cancelar
	 * movimentação do tipo despacho, representada pelo parâmetro mov. São estas as
	 * regras:
	 * <ul>
	 * <li>Despacho não pode estar cancelado</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante do despacho</li>
	 * <li>Despacho não pode estar assinado</li>
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
	public ExPodeCancelarDespacho(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
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

				new ExMovimentacaoECadastrante(mov, titular, lotaTitular),

				Not.of(new CpIgual(mov, "movimentação", mob.getUltimaMovimentacao(), "última movimentação")),

				Not.of(new ExMovimentacaoEstaAssinada(mov)), new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO).withExTpMov(mov.getIdTpMov()));
	}
}