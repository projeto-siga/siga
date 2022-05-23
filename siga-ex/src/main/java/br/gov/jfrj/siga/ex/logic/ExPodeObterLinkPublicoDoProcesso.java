package br.gov.jfrj.siga.ex.logic;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

public class ExPodeObterLinkPublicoDoProcesso extends CompositeExpressionSupport {

    private final ExMobil mob;

    public ExPodeObterLinkPublicoDoProcesso(ExDocumento doc) {
        super();
        this.mob = doc.getMobilGeral();
    }

    @Override
    protected Expression create() {
        return And.of(

                Not.of(new ExEstaPendenteDeAssinatura(mob.getDoc())),

                new ExEProcesso(mob.getDoc()),

                new ExTemMovimentacaoNaoCanceladaDoTipo(mob, ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO),

                new ExEstaMarcadoComMarcador(mob, ExDao.getInstance().consultar(CpMarcadorEnum.COVID_19.getId(), CpMarcador.class, false)),

                new ExTemVolumeComMovimentacaoNaoCanceladaDoTipo(mob.getDoc(), ExTipoDeMovimentacao.ENVIO_SIAFEM)
        );
    }
}
