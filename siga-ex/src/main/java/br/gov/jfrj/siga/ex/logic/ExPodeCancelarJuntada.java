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

public class ExPodeCancelarJuntada extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExMobil mobPai;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeCancelarJuntada(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;

		mobPai = null;
		if (!mob.isJuntadoExterno()) {
			mobPai = mob.getExMobilPai();
			if (mobPai != null && mobPai.isApensado())
				mobPai = mobPai.getGrandeMestre();
		}

		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível desentranhar móbil de outro. Regras:
	 * <ul>
	 * <li>Móbil tem de ser via</li>
	 * <li>Móbil tem de estar juntado externo ou interno (verifica-se juntada
	 * interna pela existência de móbil pai)</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>A não ser que o móbil esteja juntado externo, <i>podeMovimentar()</i>
	 * tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Móbil tem de estar juntado. <b>Obs.: essa checagem não torna
	 * desnecessários os processamentos acima?</b></li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração:
	 * Cancelar Juntada</li>
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

				new ExEMobilVia(mob),
				
				new ExEstaJuntado(mob),
							
				Not.of(new ExEstaArquivado(mobPai)),  
				
				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),
				
				Not.of(new ExEMobilCancelado(mob)),
				
				Or.of(new ExEstaJuntadoExterno(mob), new ExPodeMovimentar(mobPai, titular, lotaTitular)),
				
				new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA, titular, lotaTitular));
	}
}
