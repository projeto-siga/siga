package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeNotificar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeNotificar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {

		return And.of(

				new ExPodeAcessarDocumento(mob, titular, lotaTitular),

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaApensadoAVolumeDoMesmoProcesso(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Or.of(

						Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

						new ExEExterno(mob.doc()),

						And.of(

								new ExEProcesso(mob.doc()),

								new ExEInternoFolhaDeRosto(mob.doc()))),

				Not.of(new ExEstaSobrestado(mob)),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO));
	}
}