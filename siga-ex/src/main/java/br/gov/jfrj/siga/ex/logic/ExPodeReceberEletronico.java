package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeReceberEletronico extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeReceberEletronico(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível receber o móbil eletronicamente, de acordo com as
	 * regras a seguir, <b>que deveriam ser parecidas com as de podeReceber(), para
	 * não haver incoerência</b>:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume</li>
	 * <li>A última movimentação não cancelada do móbil não pode ser transferência
	 * externa <b>(regra falha, pois pode ser feita anotação)</b></li> e não pode
	 * ser Recebimento <b>(corrige recebimentos duplicados)</b></li>
	 * <li>Móbil não pode estar marcado como "Despacho pendente de assinatura", ou
	 * seja, tendo havido despacho ou despacho com transferência, este precisa ter
	 * sido assinado para haver transferência</li>
	 * <li>Se houver pessoa atendente definida na última movimentação não cancelada,
	 * o usuário tem de ser essa pessoa</li>
	 * <li>Não havendo pessoa atendente definida na última movimentação, apenas
	 * lotação atendente, a lotação do usuário tem de ser essa</li>
	 * <li>Documento tem de ser eletrônico <b>(melhor se fosse verificado no
	 * início)</b></li>
	 * <li>Móbil tem de estar em trãnsito <b>(melhor se fosse verificado no
	 * início)</b></li>
	 * <li>Não pode haver configuração impeditiva para recebimento (não para
	 * recebimento eletrônico)</li>
	 * </ul>
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {

		return And.of(

				new ExEEletronico(mob.doc()),
				
				Or.of(
						
						new ExEMobilVia(mob), 
						
						new ExEMobilVolume(mob)),

				Not.of(new ExTemDespachosNaoAssinados(mob)),
				
				Or.of(
						
						And.of(
						
								new ExEstaEmTransitoExterno(mob), 
						
								new ExEstaResponsavel(mob, titular, lotaTitular)),
						
						new ExEstaPendenteDeRecebimento(mob, titular, lotaTitular)));
	}
}