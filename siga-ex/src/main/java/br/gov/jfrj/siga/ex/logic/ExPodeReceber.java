package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeReceber extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeReceber(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível receber o móbil. conforme as regras a seguir:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil tem de estar em trânsito</li>
	 * <li>Lotação do usuário tem de ser a do atendente definido na última
	 * movimentação</li>
	 * <li>Se o móbil for eletrônico, não pode estar marcado como Despacho pendente
	 * de assinatura, ou seja, móbil em que tenha havido despacho ou despacho com
	 * transferência não pode ser recebido antes de assinado o despacho</li>
	 * </ul>
	 * <b>Obs.: Teoricamente, qualquer pessoa pode receber móbil transferido para
	 * órgão externo</b>
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

				If.of(

						new ExEEletronico(mob.doc()),

						new ExPodeReceberEletronico(mob, titular, lotaTitular),

						And.of(

								Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

								Not.of(new ExEMobilCancelado(mob)),

								Not.of(new ExEstaApensadoAVolumeDoMesmoProcesso(mob)),

								Not.of(new ExEstaSobrestado(mob)),

								Or.of(

										And.of(

												new ExEstaEmTransitoExterno(mob),

												new ExEstaResponsavel(mob, titular, lotaTitular)),

										new ExEstaPendenteDeRecebimento(mob, titular, lotaTitular)))),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.RECEBIMENTO).withExMod(mob.doc().getExModelo()));
	}
}