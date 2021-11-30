package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeEncerrarVolume extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeEncerrarVolume(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível encerrar um volume, dadas as seguintes condições:
	 * <ul>
	 * <li>Móbil tem de ser volume</li>
	 * <li>Volume não pode estar encerrado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode estar spbrestado</li>
	 * <li>Volume não pode estar em trânsito</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
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

				new ExEMobilVolume(mob),

				Not.of(new ExEstaEncerrado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEstaEmTramiteParalelo(mob)),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExEstaSobrestado(mob)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME,
						titular, lotaTitular));
	}
}
