package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

public class ExPodeVisualizarExternamente extends CompositeExpressionSupport {

    private final ExDocumento doc;

    public ExPodeVisualizarExternamente(ExDocumento doc) {
        this.doc = doc;
    }

    /**
     * Retorna se é possível visualizar um documento por um usuário externo
     */
    @Override
    protected Expression create() {
        return And.of(
                new ExEstaFinalizado(doc),

                new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc),

                new ExTemMovimentacaoNaoCanceladaDoTipo(doc, ExTipoDeMovimentacao.GERAR_PROTOCOLO),

                new ExPodePorConfiguracao(null, null)
                        .withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
                        .withExTpMov(ExTipoDeMovimentacao.VISUALIZACAO_EXTERNA)
        );
    }
}