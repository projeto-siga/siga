package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeSerTransferido extends CompositeExpressionSupport {

	private ExMobil mob;

	public ExPodeSerTransferido(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	protected Expression create() {

		return And.of(

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Or.of(new ExEMobilVia(mob), new ExEMobilVolume(mob)),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaApensadoAVolumeDoMesmoProcesso(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaSobrestado(mob)),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExPodeSerMovimentado(mob),

				Or.of(

						Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

						new ExEExterno(mob.doc()),

						And.of(

								new ExEProcesso(mob.doc()),

								new ExEInternoFolhaDeRosto(mob.doc()))));
	}
}