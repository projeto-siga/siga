package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeArquivarCorrente extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeArquivarCorrente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível fazer arquivamento corrente de um móbil, segundo as
	 * regras a
	 * 
	 * seguir:
	 * <ul>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil tem de ser via ou geral de processo (caso em que se condidera a
	 * situação do último volume)</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar em trânsito</li>
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

				Or.of(new ExEMobilVia(mob), new ExEMobilUltimoVolume(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Or.of(

						Not.of(new ExEEletronico(mob.doc())),

						Not.of(

								Or.of(

										new ExTemAnexosNaoAssinados(mob),

										new ExTemDespachosNaoAssinados(mob),

										new ExTemAnexosNaoAssinados(mob.doc().getMobilGeral()),

										new ExTemDespachosNaoAssinados(mob.doc().getMobilGeral()))
								)

						),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),
				
				new ExPodeMovimentar(mob, titular, lotaTitular),
				
				Not.of(new ExEstaEmTramiteParalelo(mob)), Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaSobrestado(mob)), Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.ARQUIVAMENTO_CORRENTE)
						.withCargo(titular.getCargo()).withDpFuncaoConfianca(titular.getFuncaoConfianca())
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withExMod(mob.doc().getExModelo()));
	}
}