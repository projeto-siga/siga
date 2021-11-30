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
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeRetirarDeEditalDeEliminacao extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExDocumento edital;

	public ExPodeRetirarDeEditalDeEliminacao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		ExMovimentacao movInclusao = mob.getUltimaMovimentacaoNaoCancelada(
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO);

		if (movInclusao != null)
			this.edital = movInclusao.getExMobilRef().getExDocumento();
	}

	/**
	 * Retorna se é possível retirar um móbil de edital de eliminação. Têm de ser
	 * satisfeitas as seguintes condições:
	 * <ul>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Móbil tem de estar em edital de eliminação</li>
	 * <li>Edital contendo o móbil precisa estar assinado</li>
	 * <li>Pessoa a fazer a retirada tem de ser o subscritor ou titular do
	 * edital</li>
	 * <li>Não pode haver configuração impeditiva</li>
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

				Not.of(new ExEstaEliminado(mob)),

				new ExEstaEmEditalDeEliminacao(mob),

				Not.of(new ExEstaPendenteDeAssinatura(edital)),

				If.of(new ExTemSubscritor(edital),

						new ExESubscritor(edital, titular),

						new ExECadastrante(edital, lotaTitular)));
	}
}