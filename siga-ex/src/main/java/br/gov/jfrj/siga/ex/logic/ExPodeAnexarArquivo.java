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

public class ExPodeAnexarArquivo extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAnexarArquivo(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível anexar arquivo a um móbil. As condições são as
	 * seguintes:
	 * <ul>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar arquivado</li>
	 * <li>Volume não pode estar encerrado</li>
	 * <li>Móbil tem de estar finalizado</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil / usuário</li>
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

				If.of(

						new ExEstaFinalizado(mob.doc()),

						And.of(

								Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

								Or.of(

										Not.of(new ExEMobilGeral(mob)),

										And.of(new ExEExterno(mob.doc()), Not.of(new ExJaTransferido(mob.doc())))),

								Not.of(new ExEstaJuntado(mob)),

								Not.of(new ExEstaArquivado(mob)),

								Not.of(new ExEstaEncerrado(mob)),

								Not.of(new ExEstaSobrestado(mob)),

								Not.of(new ExEstaSemEfeito(mob.doc())),

								new ExPodeMovimentar(mob, titular, lotaTitular)),

						And.of(new ExEMobilGeral(mob), Not.of(new ExEProcesso(mob.doc())))),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
					.withExTpMov(ExTipoDeMovimentacao.ANEXACAO).withExMod(mob.doc().getExModelo()));
	}
}