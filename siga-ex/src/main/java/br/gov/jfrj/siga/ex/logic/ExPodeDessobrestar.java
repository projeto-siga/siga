package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeDessobrestar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeDessobrestar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível desobrestar um móbil, segundo as seguintes regras:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume. Não pode ser geral</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Móbil tem de estar sobrestado</li>
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

				new ExEstaSobrestado(mob),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaApensadoAVolumeDoMesmoProcesso(mob)),

				new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.DESOBRESTAR, titular,
						lotaTitular));
	}
}