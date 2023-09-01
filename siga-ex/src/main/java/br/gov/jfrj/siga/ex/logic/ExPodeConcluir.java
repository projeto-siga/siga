package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
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
        return And.of(

                new ExEEletronico(mob.doc()),

                Or.of(

                        new ExEMobilVia(mob),

                        new ExEMobilUltimoVolume(mob)),

                Or.of(

                        And.of(new ExEstaEmTramiteParalelo(mob), new ExPodeMovimentar(mob, titular, lotaTitular)),

                        new ExEstaNotificado(mob, titular, lotaTitular)),

                Not.of(new ExEstaSemEfeito(mob.doc())),

                Not.of(new ExTemAnexosNaoAssinados(mob)),

                Not.of(new ExTemDespachosNaoAssinados(mob)),

                Not.of(new ExTemAnexosNaoAssinados(mob.doc().getMobilGeral())),

                Not.of(new ExTemDespachosNaoAssinados(mob.doc().getMobilGeral())),

                Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

                Not.of(new ExEstaSobrestado(mob)),

                Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

                Or.of(

                        And.of(

                                Not.of(new ExEstaArquivado(mob)),

                                new ExEstaEmTramiteParalelo(mob),

                                Not.of(new ExEstaJuntado(mob)),

                                new ExPodeMovimentar(mob, titular, lotaTitular)),

                        new ExEstaNotificado(mob, titular, lotaTitular)),

                new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.CONCLUSAO, titular, lotaTitular),

                NAnd.of(

                        new ExEstaAindaComOCadastrante(mob),

                        new ExECadastrante(mob.doc(), null, lotaTitular),

                        new ExECadastrante(mob.doc(), titular, null))
        );
    }
}