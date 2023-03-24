package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeCancelarRestringirAcesso extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMovimentacao mov;

	/**
	 * Retorna se é possível cancelar uma movimentação de vinculação de perfil,
	 * passada através do parâmetro mov. As regras são as seguintes:
	 * <ul>
	 * <li>Vinculação de perfil não pode estar cancelada</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Cancelar
	 * Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public ExPodeCancelarRestringirAcesso(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mov = mov;
		this.mob = mov.getExMobil();
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(
				Not.of(new ExMovimentacaoEstaCancelada(mov)),
				
				new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO)
						.withExTpMov(ExTipoDeMovimentacao.RESTRINGIR_ACESSO),
				
				Or.of(
						And.of(
								Not.of(new ExEstaFinalizado(doc)),
								
								new ExECadastrante(doc, lotaTitular)),
						
						new ExPodeMovimentar(mob.getDoc().getMobilDefaultParaReceberJuntada(), titular, lotaTitular)),			
				
				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)));



	}
}