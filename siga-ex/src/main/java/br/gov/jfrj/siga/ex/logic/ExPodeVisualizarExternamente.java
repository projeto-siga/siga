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
    private DpPessoa cadastrante;
    private DpLotacao lotaCadastrante;

    public ExPodeVisualizarExternamente(ExMobil mob, DpPessoa cadastrante, DpLotacao lotaCadastrante) {
        this.mob = mob;
        this.doc = mob.doc();
        this.cadastrante = cadastrante;
        this.lotaCadastrante = lotaCadastrante;
    }

    /**
     * Retorna se é possível visualizar um documento por um usuário externo
     *
     * @param cadastrante
     * @param lotaCadastrante
     * @param mob
     * @return
     */
    @Override
    protected Expression create() {
        return And.of(
                new ExEstaFinalizado(doc),

                new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc),

                new ExPodePorConfiguracao(cadastrante, lotaCadastrante)
                        .withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
                        .withExTpMov(ExTipoDeMovimentacao.VISUALIZACAO_EXTERNA)
                        .withCpOrgaoUsu(mob.doc().getOrgaoUsuario())
        );
    }
}