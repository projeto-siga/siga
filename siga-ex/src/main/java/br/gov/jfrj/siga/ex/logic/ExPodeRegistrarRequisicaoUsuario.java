package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import com.crivano.jlogic.*;

public class ExPodeRegistrarRequisicaoUsuario extends CompositeExpressionSupport {

    private final ExMobil mob;
    private final DpPessoa titular;
    private final DpLotacao lotaTitular;

    public ExPodeRegistrarRequisicaoUsuario(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
        this.mob = mob;
        this.titular = titular;
        this.lotaTitular = lotaTitular;
    }

    @Override
    protected Expression create() {

        return And.of(

                Not.of(new ExEstaPendenteDeAssinatura(mob.getDoc())),
                
                new ExPodeAcessarDocumento(mob, titular, lotaTitular),

                new ExPodePorConfiguracao(titular, lotaTitular)
                        .withIdTpConf(ExTipoDeConfiguracao.REGISTRAR_REQUISICOES_USUARIO)
                        .withCpOrgaoUsu(mob.getDoc().getOrgaoUsuario())

        );
    }

}