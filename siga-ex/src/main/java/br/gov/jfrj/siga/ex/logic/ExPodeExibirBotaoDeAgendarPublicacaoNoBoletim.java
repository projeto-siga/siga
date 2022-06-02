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

public class ExPodeExibirBotaoDeAgendarPublicacaoNoBoletim extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeExibirBotaoDeAgendarPublicacaoNoBoletim(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível exibir a opção para agendar publicação no Boletim.
	 * Seguem as regras:
	 * <ul>
	 * <li>Móbil não pode ser geral</li>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Documento tem de ser do tipo interno produzido</li>
	 * <li><i>podeGerenciarPublicacaoBoletimPorConfiguracao()</i> ou
	 * <i>podeMovimentar()</i>tem de ser verdadeiro para o usuário</li>
	 * <li>Documento não pode já ter sido publicado em boletim</li>
	 * <li>Publicação no boletim não pode ter sido já agendada para o documento</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Documento não pode ter sido eliminado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * 
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

				new ExEMobilGeral(mob),

				new ExEstaFinalizado(mob.doc()),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEInternoFolhaDeRosto(mob.doc())),

				Or.of(

						And.of(

								new ExPodeMovimentar(mob, titular, lotaTitular),

								new ExPodePorConfiguracao(titular, lotaTitular)
										.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
										.withExTpMov(ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM)
										.withExMod(mob.doc().getExModelo())),

						new ExPodeGerenciarPublicacaoNoBoletimPorConfiguracao(mob, titular, lotaTitular)),

				Not.of(new ExEstaPublicadoNoBoletim(mob.doc())),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExEstaSolicitadaPublicacaoNoBoletim(mob.doc())),

				Not.of(new ExEstaArquivado(mob)));
	}
}