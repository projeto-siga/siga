package br.gov.jfrj.siga.ex.logic;

import java.util.Set;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeCancelarCiencia extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMovimentacao penultMovNaoCancelada;
	private ExMovimentacao ultMov;
	private ExMovimentacao movCiencia;

	public ExPodeCancelarCiencia(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		this.penultMovNaoCancelada = mob.getPenultimaMovimentacaoNaoCancelada();
		this.ultMov = mob.getUltimaMovimentacao();

		Set<ExMovimentacao> setMovCiente = mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA);
		if (setMovCiente != null)
			for (ExMovimentacao mov : setMovCiente)
				if (mov.getCadastrante() != null && mov.getCadastrante().equivale(titular)) {
					this.movCiencia = mov;
					break;
				}
	}

	/**
	 * Retorna se é possível cancelar uma ciência de documento
	 * <ul>
	 * <li>Precisa ser via ou volume</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Não pode cancelar ciência se a última mov não for Ciência, Definir
	 * Marcação ou Definir Perfil
	 * <li>Última mov de ciência não pode ter sido cancelada</li>
	 * <li>Somente o usuário que criou a ciência pode desfazer a mesma</li>
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

				Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEMobilCancelado(mob)),

				new CpNaoENulo(penultMovNaoCancelada, "penúltima movimentação não cancelada"),

				new CpNaoENulo(ultMov, "última movimentação"),

				Or.of(

						new ExMovimentacaoEDoTipo(penultMovNaoCancelada, ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA),

						new ExMovimentacaoEDoTipo(penultMovNaoCancelada, ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO),

						new ExMovimentacaoEDoTipo(penultMovNaoCancelada,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)),

				new CpNaoENulo(movCiencia, "movimentação de ciência"),

				Not.of(new ExMovimentacaoEstaCancelada(movCiencia)),

				new ExMobEstaCiente(mob, titular),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
						titular, lotaTitular));
	}
}
