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

public class ExPodeDesarquivarCorrente extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeDesarquivarCorrente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolume();
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

				Or.of(new ExEMobilVia(mob), new ExEMobilGeralDeProcesso(mob)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Or.of(new ExEstaArquivado(mob), new ExEstaArquivadoIntermediario(mob)),

				Not.of(new ExEstaArquivadoPermanente(mob)),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE,
						titular, lotaTitular));
	}
}
