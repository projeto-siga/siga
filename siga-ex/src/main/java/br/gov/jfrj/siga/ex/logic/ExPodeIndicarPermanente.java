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

public class ExPodeIndicarPermanente extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeIndicarPermanente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível indicar um móbil para guarda permanente. Têm de ser
	 * satisfeitas as seguintes condições:
	 * <ul>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode ter sido já indicado para guarda permanente</li>
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

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Or.of(new ExEMobilVia(mob), new ExEMobilGeralDeProcesso(mob)),

				Not.of(new ExEMobilCancelado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEstaJuntado(mob)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaIndicadoParaGuardaPermanente(mob)),

				Not.of(new ExEstaArquivadoPermanente(mob)),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INDICACAO_GUARDA_PERMANENTE,
						titular, lotaTitular));
	}
}