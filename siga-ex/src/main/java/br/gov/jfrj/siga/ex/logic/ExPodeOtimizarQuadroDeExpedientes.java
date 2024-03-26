package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeOtimizarQuadroDeExpedientes extends CompositeExpressionSupport {

    private final DpPessoa titular;
    private final DpLotacao lotaTitular;

    public ExPodeOtimizarQuadroDeExpedientes(DpPessoa titular, DpLotacao lotaTitular) {
        this.titular = titular;
        this.lotaTitular = lotaTitular;
    }

    @Override
    protected Expression create() {

        return And.of(

                new ExPodePorConfiguracao(titular, lotaTitular)
                        .withIdTpConf(ExTipoDeConfiguracao.OTIMIZAR_QUERY_QUADRO)

        );
    }

}