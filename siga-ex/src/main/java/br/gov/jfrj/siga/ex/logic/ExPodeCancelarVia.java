package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.logic.CpPodeNunca;
import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeCancelarVia extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMovimentacao exUltMovNaoCanc;
	private ExMovimentacao exUltMov;

	public ExPodeCancelarVia(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		exUltMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
		exUltMov = mob.getUltimaMovimentacao();
	}

	/**
	 * Retorna se é possível cancelar a via mob, conforme estabelecido a seguir:
	 * <ul>
	 * <li>Móbil tem de ser via</li>
	 * <li>Documento que contém a via não pode estar assinado</li>
	 * <li>Via não pode estar cancelada</li>
	 * <li>Última movimentação não cancelada da via tem de ser a sua criação</li>
	 * <li>Não pode haver movimentações canceladas posteriores à criação</li>
	 * <li>Com relação à movimentação de criação (última movimentação não
	 * cancelada), o usuário tem de ser 1) da lotação atendente da movimentação, 2)
	 * o subscritor da movimentação, 3) o titular da movimentação ou 4) da lotação
	 * cadastrante da movimentação</li>
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
		if (exUltMovNaoCanc == null || exUltMov == null) 
			return new CpPodeNunca();
			
		return And.of(

				new ExEMobilVia(mob),

				Or.of(

						new ExEstaPendenteDeAssinatura(mob.doc()),

						new ExEGovSP()),

				NAnd.of(

						new ExEGovSP(),

						Not.of(new ExTemMovimentacaoNaoCanceladaDoTipo(mob.doc(),
								ExTipoDeMovimentacao.ASSINATURA_COM_SENHA)),

						Or.of(

								new ExTemMovimentacaoNaoCanceladaDoTipo(mob.doc(),
										ExTipoDeMovimentacao.TRANSFERENCIA),

								new ExTemMovimentacaoNaoCanceladaDoTipo(mob.doc(),
										ExTipoDeMovimentacao.JUNTADA),

								new ExTemDocumentosFilhos(mob),

								new ExTemJuntados(mob))),

				Not.of(new ExEMobilCancelado(mob)),

				new ExMovimentacaoEDoTipo(exUltMovNaoCanc, ExTipoDeMovimentacao.CRIACAO),

				new CpIgual(exUltMovNaoCanc.getIdMov(), "última movimentação não cancelada", 
							exUltMov.getIdMov(), "última movimentação"),

				new ExEstaResponsavel(mob, titular, lotaTitular),

				// Não é possível cancelar a última via de um documento pois estava gerando
				// erros nas marcas do mobil geral.
				new ExTemMaisDeUmMobilNaoCancelado(mob.doc()),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.CANCELAR_VIA));

	}

}
