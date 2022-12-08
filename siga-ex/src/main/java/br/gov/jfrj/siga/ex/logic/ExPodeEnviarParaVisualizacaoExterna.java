package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import com.crivano.jlogic.*;

public class ExPodeEnviarParaVisualizacaoExterna extends CompositeExpressionSupport {

    private ExMobil mob;
    private DpPessoa titular;
    private DpLotacao lotaTitular;

    /**
     * Retorna se é possível enviar o Documento para visualização externa pelo
     * link de autenticidade contido no rodapé do Documento.
     *
     * @param titular
     * @param lotaTitular
     * @param mob
     * @return
     */
    public ExPodeEnviarParaVisualizacaoExterna(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
        this.mob = mob;
        this.titular = titular;
        this.lotaTitular = lotaTitular;
    }

    @Override
    protected Expression create() {

        return And.of(

                new ExEstaFinalizado(mob.getDoc()),

                new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(mob.getDoc()),

                new ExPodeAcessarDocumento(mob, titular, lotaTitular),

                new ExPodeMovimentarPorConfiguracao(
                        ExTipoDeMovimentacao.ENVIO_PARA_VISUALIZACAO_EXTERNA,
                        titular, lotaTitular
                )
        );
    }
}