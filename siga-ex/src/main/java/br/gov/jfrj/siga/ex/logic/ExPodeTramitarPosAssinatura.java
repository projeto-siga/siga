package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeTramitarPosAssinatura extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private DpPessoa destinatario;
	private DpLotacao lotaDestinatario;

	public ExPodeTramitarPosAssinatura(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular, DpPessoa destinatario,
			DpLotacao lotaDestinatario) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.destinatario = destinatario;
		this.lotaDestinatario = lotaDestinatario;
	}

	/**
	 * Retorna se é possível fazer transferência imediatamente antes da tela de
	 * assinatura. As regras são as seguintes para este móbil:
	 * <ul>
	 * <li><i>Destinatario esta definido</i>
	 * <li><i>Destinatario pode receber documento</i>
	 * <li><i>Se temporário, o documento está na mesma lotação do titular</i>
	 * <li><i>Se finalizado, podeMovimentar()</i>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param destinatario
	 * @param lotaDestinatario
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * 
	 */
	@Override
	protected Expression create() {

		return And.of(

				Not.of(new ExEstaOrquestradoPeloWF(mob.doc())),

				NAnd.of(new CpENulo(destinatario, "destinatário"),
						new CpENulo(lotaDestinatario, "lotação destinatária")),

				new ExPodeReceberPorConfiguracao(mob, destinatario, lotaDestinatario),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.TRANSFERENCIA),

				If.of(

						new ExEstaFinalizado(mob.doc()),

						new ExPodeMovimentar(mob, titular, lotaTitular),

						Or.of(

								new ExECadastrante(mob.doc(), titular, lotaTitular),

								new ExESubscritor(mob.doc(), titular, lotaTitular))),

				Or.of(

						Not.of(new ExTemMobilPai(mob.doc())),

						new ExEstaResponsavel(mob.doc().getExMobilPai(), titular, lotaTitular)));
	}
}