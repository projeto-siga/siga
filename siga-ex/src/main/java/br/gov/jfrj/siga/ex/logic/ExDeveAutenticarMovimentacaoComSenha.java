package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExDeveAutenticarMovimentacaoComSenha extends CompositeExpressionSupport {

	private ExMovimentacao mov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/*
	 * Retorna se pode autenticar uma movimentação que só foi assinada com senha.
	 * 
	 */
	public ExDeveAutenticarMovimentacaoComSenha(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				// Não é necessário autenticar movimentação de anexação pois o link para
				// assinar/autenticar sempre está disponível.
				new ExMovimentacaoEDoTipo(mov, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mov.getExMobil().doc().getExModelo())
						.withExFormaDoc(mov.getExMobil().doc().getExFormaDocumento())
						.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
						.withAceitarPode(false));
	}

}