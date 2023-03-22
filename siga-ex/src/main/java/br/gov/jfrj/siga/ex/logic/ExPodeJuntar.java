package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeJuntar extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento docPai;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeJuntar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	public ExPodeJuntar(ExDocumento docPai, ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.docPai = docPai;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível junta este móbil a outro. Seguem as regras:
	 * <ul>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Volume não pode estar encerrado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil não pode estar juntado <b>(mas pode ser juntado estando
	 * apensado?)</b></li>
	 * <li>Móbil não pode estar em algum arquivo</li>
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

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Not.of(new ExEMobilCancelado(mob)),

				Not.of(new ExEstaEncerrado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Or.of(

						And.of(

								new ExTemMobilPai(mob.doc()),

								new ExESubscritorOuCossignatario(mob.doc(), titular)),

						And.of(

								new CpNaoENulo(docPai, "documento onde foi autuado"),

								new ExEMobilAutuado(docPai, mob)),

						new ExPodeMovimentar(mob, titular, lotaTitular)),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaApensado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaSobrestado(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(new ExEstaEmTramiteParalelo(mob)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.JUNTADA).withExMod(mob.doc().getExModelo()));

	}
}