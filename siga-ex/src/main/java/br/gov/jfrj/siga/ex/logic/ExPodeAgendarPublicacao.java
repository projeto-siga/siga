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

public class ExPodeAgendarPublicacao extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível agendar publicação direta, de acordo com as seguintes
	 * regras:
	 * <ul>
	 * <li>Documento tem de estar fechado</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Não pode haver agendamento de publicação direta em aberto</li>
	 * <li>Não pode haver agendamento de publicação indireta em aberto</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode estar eliminado</li>
	 * <li>Nada é dito a respeito do Boletim Interno</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeAgendarPublicacao(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				new ExEstaFinalizado(doc),

				Not.of(new ExEstaPendenteDeAssinatura(doc)),

				Not.of(new ExEstaAgendadaPublicacaoNoDiario(doc)),

				Not.of(new ExEstaSemEfeito(doc)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Or.of(

						new ExMobPodeAtenderPedidoPublicacao(mob, titular, lotaTitular),

						And.of(

								Not.of(new ExEstaSolicitadaPublicacaoNoDiario(doc)),

								new ExPodeMovimentar(mob, titular, lotaTitular),

								new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
										.withExFormaDoc(mob.doc().getExFormaDocumento())
										.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
										.withExTpMov(ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO))));
	}
}