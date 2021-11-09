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

public class ExPodeExibirBotaoDeDesarquivarIntermediario extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeExibirBotaoDeDesarquivarIntermediario(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível reabrir um móbil, segundo as seguintes regras:
	 * <ul>
	 * <li>Móbil tem de ser via ou geral de processo.</li>
	 * <li>Móbil tem de estar em arquivo intermediário, não permanente</li>
	 * <li>Móbil não pode estar em edital de eliminação</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
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

				Or.of(new ExEMobilVia(mob), new ExEMobilGeralDeProcesso(mob)),

				new ExEstaArquivadoIntermediario(mob),

				Not.of(new ExEstaArquivadoPermanente(mob)),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO,
						titular, lotaTitular));
	}
}
