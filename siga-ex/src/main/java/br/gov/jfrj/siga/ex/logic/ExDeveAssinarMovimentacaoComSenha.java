package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExDeveAssinarMovimentacaoComSenha extends CompositeExpressionSupport {

	private ExMovimentacao mov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/*
	 * Retorna se pode autenticar uma movimentação que só foi assinada com senha.
	 * 
	 */
	public ExDeveAssinarMovimentacaoComSenha(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mov.getExMobil().doc().getExModelo())
				.withExFormaDoc(mov.getExMobil().doc().getExFormaDocumento())
				.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
				.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA)
				.withAceitarPode(false);
	}
}