package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeExcluirCossignatario extends CompositeExpressionSupport {

	private ExMovimentacao mov;
	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível excluir uma movimentação de Inclusão de Cossignatário
	 * <ul>
	 * <li>Não pode estar cancelada</li>
	 * <li>Não pode estar assinado</li>
	 * <li>Se o documento for físico, não pode estar finalizado</li>
	 * <li>Não pode estar assinado</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Excluir
	 * Anexo</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public ExPodeExcluirCossignatario(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
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

				If.of(new ExEEletronico(doc),

						Not.of(new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc)),

						Not.of(new ExEstaFinalizado(doc))),

				new ExECadastrante(doc, lotaTitular),

				new ExEstaPendenteDeAssinatura(doc),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.EXCLUIR));
	}
}