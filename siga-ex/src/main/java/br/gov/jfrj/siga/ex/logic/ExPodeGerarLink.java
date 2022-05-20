package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

public class ExPodeGerarLink extends CompositeExpressionSupport {

    private final ExMobil mob;
    private final DpPessoa titular;
    private final DpLotacao lotaTitular;

    public ExPodeGerarLink(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
        super();
        this.mob = mob;
        this.titular = titular;
        this.lotaTitular = lotaTitular;
    }

    @Override
    protected Expression create() {
        return And.of(
                new ExEProcesso(mob.doc()),

                new ExEstaFinalizado(mob.doc()),

                Not.of(new ExEstaSemEfeito(mob.doc())),

                Not.of(new ExEstaEliminado(mob)),

                Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

                new ExEstaMarcadoComMarcador(mob, ExDao.getInstance().consultar(CpMarcadorEnum.COVID_19.getId(), CpMarcador.class, false)),

                new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
                        .withExTpMov(ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO)
        );
    }
}
