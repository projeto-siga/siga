package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExDeveAutenticarComSenha extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExDeveAutenticarComSenha(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/*
	 * Retorna se é possível autenticar um documento com senha:
	 */
	@Override
	protected Expression create() {
		return new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
				.withExFormaDoc(mob.doc().getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
				.withExTpMov(ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA).withAceitarPode(false);
	}
}