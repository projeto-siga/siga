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
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeRegistrarAssinatura extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeRegistrarAssinatura(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível registrar assinatura manual de documento que contém o
	 * móbil passado por parâmetro. As regras são as seguintes:
	 * <ul>
	 * <li>Móbil tem de ser geral</li>
	 * <li>Não pode ser móbil de processo interno importado</li>
	 * <li>Não pode ser móbil de documento externo</li>
	 * <li>Documento não pode estar cancelado</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro ou usuário tem de ser o
	 * próprio subscritor ou titular do documento</li>
	 * <li>Documento não pode ser eletrônico</li>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode ter sido eliminado</li>
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

				new ExEstaFinalizado(mob.doc()),

				Not.of(new ExEEletronico(mob.doc())),

				Not.of(new ExEMobilGeral(mob)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				NAnd.of(

						new ExEProcesso(mob.doc()),

						new ExEInternoFolhaDeRosto(mob.doc())),

				Not.of(new ExEInternoFolhaDeRosto(mob.doc())),

				Not.of(new ExEstaCancelado(mob.doc())),

				Or.of(

						new ExESubscritor(mob.doc(), titular),

						new ExETitular(mob.doc(), titular)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.REGISTRO_ASSINATURA_DOCUMENTO)
						.withExMod(mob.doc().getExModelo()));
	}
}