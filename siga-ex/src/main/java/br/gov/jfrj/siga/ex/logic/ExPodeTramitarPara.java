package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeTramitarPara extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private DpPessoa destinatario;
	private DpLotacao lotaDestinatario;

	public ExPodeTramitarPara(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular, DpPessoa destinatario, DpLotacao lotaDestinatario) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.destinatario = destinatario;
		this.lotaDestinatario = lotaDestinatario;
	}

	@Override
	protected Expression create() {

		return new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
				.withExTpMov(ExTipoDeMovimentacao.TRANSFERENCIA).withExMod(mob.doc().getExModelo())
				.withPessoaObjeto(destinatario).withLotacaoObjeto(lotaDestinatario);

	}
}