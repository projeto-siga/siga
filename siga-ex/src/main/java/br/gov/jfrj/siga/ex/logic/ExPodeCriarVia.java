package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCriarVia extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMovimentacao exUltMovNaoCanc;
	private ExMovimentacao exUltMov;

	public ExPodeCriarVia(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		exUltMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
		exUltMov = mob.getUltimaMovimentacao();
	}

	/**
	 * Retorna se é possível criar via para o documento que contém o móbil passado
	 * por parâmetro, de acordo com as seguintes condições:
	 * <ul>
	 * <li>Documento tem de ser expediente</li>
	 * <li>Documento não pode ter pai, pois não é permitido criar vias em documento
	 * filho</li>
	 * <li>Número da última via não pode ser maior ou igual a 21</li>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Documento não pode ter sido eliminado</li>
	 * <li>Documento tem de possuir alguma via não cancelada</li>
	 * <li>Lotação do titular igual a do cadastrante ou a do subscritor ou
	 * 
	 * o titular ser o próprio subscritor</li>
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

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExEExpediente(mob.doc()),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaPendenteDeColaboracao(mob)),

				NAnd.of(new ExTemMobilPai(mob.doc()), new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExTemNumeroMaximoDeVias(mob.doc())),

				new ExEstaFinalizado(mob.doc()),

				Or.of(

						new ExPodeMovimentar(mob, titular, lotaTitular),

						new ExECadastrante(mob.doc(), lotaTitular),

						new ExESubscritor(mob.doc(), titular, lotaTitular)),

				new ExPodePorConfiguracao(titular, lotaTitular)
					.withExFormaDoc(mob.doc().getExFormaDocumento())
					.withExMod(mob.doc().getExModelo())
					.withIdTpConf(ExTipoDeConfiguracao.CRIAR_VIA));

	}

}
