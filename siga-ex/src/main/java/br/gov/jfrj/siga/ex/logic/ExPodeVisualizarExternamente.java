package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import com.crivano.jlogic.*;

public class ExPodeVisualizarExternamente extends CompositeExpressionSupport {

    private ExMobil mob;
    private DpPessoa titular;
    private DpLotacao lotaTitular;

    /**
     * Retorna se é possível visualizar um documento externamente pelo
     * link de autenticidade contido no rodapé do Documento.
     *
     * @param titular
     * @param lotaTitular
     * @param mob
     * @return
     */
    public ExPodeVisualizarExternamente(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
        this.mob = mob;
        this.titular = titular;
        this.lotaTitular = lotaTitular;
    }

    @Override
    protected Expression create() {

        return Or.of(
				
				new ExPodeAcessarDocumento(mob, titular, lotaTitular),
				
				new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.VISUALIZACAO_EXTERNA_DOCUMENTOS)
						.withCpOrgaoUsu(mob.getDoc().getOrgaoUsuario())
		);
    }
}