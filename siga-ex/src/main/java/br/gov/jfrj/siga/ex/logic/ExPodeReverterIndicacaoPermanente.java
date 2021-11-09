package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeReverterIndicacaoPermanente extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeReverterIndicacaoPermanente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível reverter a indicação de um móbil para guarda
	 * permanente. Têm de ser satisfeitas as seguintes condições:
	 * <ul>
	 * <li>Móbil tem de estar indicado para guarda permanente</li>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode ter sido arquivado permanentemente nem eliminado</li>
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

				new ExEstaIndicadoParaGuardaPermanente(mob),

				Or.of(

						new ExEMobilVia(mob),

						new ExEMobilGeralDeProcesso(mob)),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaArquivadoPermanente(mob)),

				Not.of(new ExEMobilCancelado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentarPorConfiguracao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_REVERSAO_INDICACAO_GUARDA_PERMANENTE, titular,
						lotaTitular));
	}
}