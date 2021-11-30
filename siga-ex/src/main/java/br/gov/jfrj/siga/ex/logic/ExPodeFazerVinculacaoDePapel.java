package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeFazerVinculacaoDePapel extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeFazerVinculacaoDePapel(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível vincular perfil ao documento. Basta não estar eliminado
	 * o documento e não haver configuração impeditiva, o que significa que, tendo
	 * acesso a um documento não eliminado, qualquer usuário pode se cadastrar como
	 * interessado.
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

				Not.of(new ExEstaCancelado(mob.doc())),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(new ExEstaEliminado(mob)),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL, titular,
						lotaTitular));
	}
}
