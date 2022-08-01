package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeConcluir extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeConcluir(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(new ExEEletronico(mob.doc()), Or.of(new ExEMobilVia(mob), new ExEMobilUltimoVolume(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(Or.of(new ExTemAnexosNaoAssinados(mob), new ExTemDespachosNaoAssinados(mob),
						new ExTemAnexosNaoAssinados(mob.doc().getMobilGeral()),
						new ExTemDespachosNaoAssinados(mob.doc().getMobilGeral()))),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),
				Or.of(And.of(new ExEstaEmTramiteParalelo(mob), new ExPodeMovimentar(mob, titular, lotaTitular)),
						new ExEstaNotificado(mob, titular, lotaTitular)),

				Not.of(new ExEstaArquivado(mob)), Not.of(new ExEstaSobrestado(mob)), Not.of(new ExEstaJuntado(mob)),
				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.COPIA, titular, lotaTitular),

				Or.of(Not.of(new ExEstaAindaComOCadastrante(mob)),
						new ExEstaPendenteDeRecebimento(mob, titular, lotaTitular)),
				new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.RECEBIMENTO, titular, lotaTitular));
	}
}