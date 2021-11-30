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
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeApensar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMobil mobVerif;

	public ExPodeApensar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível apensar este móbil a outro, conforme as regras:
	 * <ul>
	 * <li>Móbil precisa ser via ou volume</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em trânsito <b>(o que é isEmTransito?)</b></li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil não pode estar juntado</li>
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

				Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

				Not.of(new ExEstaEmTramiteParalelo(mob)),

				Not.of(new ExEMobilCancelado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaApensado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaSobrestado(mob)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO)
						.withExMod(mob.doc().getExModelo()));

	}
}