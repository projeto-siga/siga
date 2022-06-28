package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.logic.CpTokenEValido;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

public class ExPodeVisualizarExternamente extends CompositeExpressionSupport {

    private final ExMobil mob;
    private final DpPessoa titular;
    private final DpLotacao lotaTitular;

    private final String cod;

    /**
     * Retorna se é possível visualizar um documento externamente pelo
     * link de autenticidade contido no rodapé do Documento.
     */
    public ExPodeVisualizarExternamente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular, String cod) {
        this.mob = mob;
        this.titular = titular;
        this.lotaTitular = lotaTitular;
        this.cod = cod;
    }

    @Override
    protected Expression create() {

        return Or.of(

                new ExPodeAcessarDocumento(mob, titular, lotaTitular),

                new ExPodePorConfiguracao(titular, lotaTitular)
                        .withIdTpConf(ExTipoDeConfiguracao.VISUALIZACAO_EXTERNA_DOCUMENTOS)
                        .withCpOrgaoUsu(mob.getDoc().getOrgaoUsuario()),

                new CpTokenEValido(cod)
        );
    }

}