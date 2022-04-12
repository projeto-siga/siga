package br.gov.jfrj.siga.integracao.api.v1;

import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

import javax.persistence.EntityManager;

public class IntegracaoApiV1Context extends ApiContextSupport {

    private static final String INTGR_MODULO_DE_WORKFLOW = "INTGR:Módulo de Integração;";

    @Override
    public void atualizarCacheDeConfiguracoes() throws Exception {
        Ex.getInstance().getConf().limparCacheSeNecessario();
    }

    @Override
    public CpDao inicializarDao() {
        return ExDao.getInstance();
    }

    @Override
    public EntityManager criarEntityManager() {
        EntityManager em = this.inicializarDao().em();

        if(em == null || em.isOpen()){
            em = ExStarter.emf.createEntityManager();
        }

        return em;
    }
}
