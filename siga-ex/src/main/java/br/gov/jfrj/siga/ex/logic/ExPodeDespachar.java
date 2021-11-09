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

public class ExPodeDespachar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeDespachar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível fazer despacho no móbil, conforme as regras a seguir:
	 * <ul>
	 * <li>Móbil não pode ter despacho pendente de assinatura</li>
	 * <li>Móbil tem de ser via ou volume. Não pode ser geral</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode estar em edital de eliminação</li>
	 * <li>Móbil tem de estar assinado ou ser externo. <b>Mas documento externo não
	 * é cnsiderado assinado? <i>isAssinado</i> não deveria retornar
	 * verdadeiro?</b></li>
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

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				Not.of(new ExEstaApensadoAVolumeDoMesmoProcesso(mob)),

				Not.of(new ExEstaSobrestado(mob)),

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Or.of(

						Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

						new ExEExterno(mob.doc()),

						And.of(

								new ExEProcesso(mob.doc()),

								new ExEInternoFolhaDeRosto(mob.doc()))),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO, titular,
						lotaTitular));
	}
}