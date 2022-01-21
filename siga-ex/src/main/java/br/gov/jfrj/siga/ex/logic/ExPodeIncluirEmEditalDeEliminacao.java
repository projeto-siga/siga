package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeIncluirEmEditalDeEliminacao extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMobil mobVerif;

	public ExPodeIncluirEmEditalDeEliminacao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		this.mobVerif = mob;

		if (this.mob.isGeralDeProcesso())
			this.mobVerif = this.mob.doc().getUltimoVolume();

	}

	/**
	 * Retorna se é possível incluir o móbil em edital de eliminação, de acordo com
	 * as condições a seguir:
	 * <ul>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * <li>Móbil tem de estar em arquivo corrente ou intermediário</li>
	 * <li>PCTT tem de prever, para o móbil, destinação final Eliminação</li>
	 * <li>Móbil não pode estar arquivado permanentemente</li>
	 * <li>Documento a que o móbil pertence tem de ser digital ou estar na lotação
	 * titular</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
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

				new CpNaoENulo(mobVerif, "móbil"),

				Or.of(

						new ExEstaArquivadoCorrente(mobVerif),

						new ExEstaArquivadoIntermediario(mobVerif)),

				Not.of(new ExEstaArquivadoPermanente(mobVerif)),

				Not.of(new ExTemTemporalidadeDestinacaoEliminacao(mobVerif)),

				Or.of(

						new ExEstaResponsavel(mobVerif, titular, lotaTitular),

						new ExEEletronico(mob.doc())),

				new ExPodeMovimentarPorConfiguracao(
						ExTipoDeMovimentacao.INCLUSAO_EM_EDITAL_DE_ELIMINACAO, titular, lotaTitular));
	}
}