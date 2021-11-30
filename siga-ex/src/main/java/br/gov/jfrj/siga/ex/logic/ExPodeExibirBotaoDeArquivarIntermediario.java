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

public class ExPodeExibirBotaoDeArquivarIntermediario extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeExibirBotaoDeArquivarIntermediario(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível mostrar o link para arquivamento intermediário de um
	 * móbil, de acordo com as condições a seguir:
	 * <ul>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * 
	 * <li>Móbil tem de estar assinado</li>
	 * <li>Móbil tem de estar arquivado corrente</li>
	 * <li>PCTT tem de prever, para o móbil, tempo de permanência no arquivo
	 * intermediário</li>
	 * <li>Móbil não pode estar arquivado intermediário nem permanente</li>
	 * <li>Móbil não pode estar em edital de eliminação</li>
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

				Not.of(new ExEstaSemEfeito(mob.doc())), Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())), Not.of(new ExEstaArquivado(mob)),
				new ExTemTemporalidadeIntermediario(mob), Not.of(new ExEstaArquivadoIntermediario(mob)),
				Not.of(new ExEstaArquivadoPermanente(mob)), Not.of(new ExEstaEmEditalDeEliminacao(mob)),
				Not.of(new ExEstaEmTramiteParalelo(mob)),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO,
						titular, lotaTitular));
	}
}