package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodePedirPublicacao extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível solicitar publicação indireta no DJE, conforme as
	 * regras a seguir:
	 * <ul>
	 * <li>Não pode ser possível agendar publicação direta</li>
	 * <li>Documento tem de estar fechado (verificação desnecessária, visto que
	 * abaixo se checa se está assinado)</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Não pode haver outra solicitação de publicação no DJE em aberto</li>
	 * <li>Não pode pode haver solicitação de publicação no Boletim em aberto</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode estar eliminado</li>
	 * <li>Não pode haver agendamento de publicação direta em aberto <b>(verificação
	 * desnecessária?)</b></li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodePedirPublicacao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExPodeAgendarPublicacao(mob, titular, lotaTitular)),

				new ExEstaFinalizado(doc),

				Or.of(

						new ExPodeMovimentar(mob, titular, lotaTitular),

						new ExMobPodeAtenderPedidoPublicacao(mob, titular, lotaTitular)),

				Not.of(new ExEstaPendenteDeAssinatura(doc)),

				Not.of(new ExEstaSolicitadaPublicacaoNoDiario(doc)),

				Not.of(new ExEstaSolicitadaPublicacaoNoBoletim(doc)),

				Not.of(new ExEstaAgendadaPublicacaoNoDiario(doc)),

				Not.of(new ExEstaSemEfeito(doc)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.PEDIDO_PUBLICACAO));
	}
}