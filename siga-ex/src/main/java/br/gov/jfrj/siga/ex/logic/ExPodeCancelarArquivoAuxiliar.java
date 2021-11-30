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
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCancelarArquivoAuxiliar extends CompositeExpressionSupport {

	private ExMovimentacao mov;
	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível cancelar uma movimentação mov, de anexação de arquivo.
	 * Regras:
	 * <ul>
	 * <li>Anexação não pode estar cancelada</li>
	 * <li>Não pode mais ser possível <i>excluir</i> a anexação</li>
	 * <li>Se o documento for físico, anexação não pode ter sido feita antes da
	 * finalização</li>
	 * <li>Se o documento for digital, anexação não pode ter sido feita antes da
	 * assinatura</li>
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
	public ExPodeCancelarArquivoAuxiliar(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
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

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO)
						.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR));
	}
}