package br.gov.jfrj.siga.integracao.api.v1;

import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeAcessarDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.IntegracaoDao;
import br.gov.jfrj.siga.hibernate.IntegracaoStarter;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerException;

import javax.persistence.EntityManager;

import static java.util.Objects.isNull;

public class IntegracaoApiV1Context extends ApiContextSupport {

    private static final String INTGR_MODULO_DE_INTEGRACAO = "DOC:Módulo de Documentos;";

    public void assertAcesso(String acesso) throws Exception {
        getSigaObjects().assertAcesso(INTGR_MODULO_DE_INTEGRACAO + acesso);
    }

    @Override
    public void atualizarCacheDeConfiguracoes() throws Exception {
        Cp.getInstance().getConf().limparCacheSeNecessario();
    }

    @Override
    public CpDao inicializarDao() {
    	ModeloDao.freeInstance();
        return IntegracaoDao.getInstance();
    }

    @Override
    public EntityManager criarEntityManager() {
        return IntegracaoStarter.emf.createEntityManager();
    }

    ExMobil buscarEValidarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp,
                                String descricaoDocumento) throws Exception {
        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
        filter.setSigla(sigla);
        ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);

        if (isNull(mob)) {
            throw new SwaggerException("Número do " + descricaoDocumento + " não existe", 404, null, req, resp, null);
        }
        if (!Ex.getInstance().getComp().pode(ExPodeAcessarDocumento.class, getTitular(), getLotaTitular(), mob))
            throw new SwaggerException("Acesso ao " + descricaoDocumento + " " + mob.getSigla()
                    + " permitido somente a usuários autorizados. (" + getTitular().getSigla() + "/"
                    + getLotaTitular().getSiglaCompleta() + ")", 403, null, req, resp, null);

        return mob;
    }
}
