package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import com.crivano.jlogic.*;

public class ExPodeVisualizarExternamente extends CompositeExpressionSupport {

    private ExMobil mob;
    private ExDocumento doc;
    private DpPessoa titular;
    private DpLotacao lotaTitular;

    public ExPodeVisualizarExternamente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
        this.mob = mob;
        this.doc = mob.doc();
        this.titular = titular;
        this.lotaTitular = lotaTitular;
    }

    /**
     * Retorna se é possível visualizar um documento por um usuário externo
     *
     * @param titular
     * @param lotaTitular
     * @param mob
     * @return
     */
    @Override
    protected Expression create() {
        return And.of(
                new ExEstaFinalizado(doc),

                new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc),

                new ExPodePorConfiguracao(titular, lotaTitular)
                        .withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
                        .withExTpMov(ExTipoDeMovimentacao.VISUALIZACAO_EXTERNA)
                        .withExMod(mob.doc().getExModelo())
        );
    }
}