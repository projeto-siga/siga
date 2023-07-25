package br.gov.jfrj.siga.ex.xjus;

import javax.persistence.EntityManager;

import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;

public class XjusRecordAPIContext extends ApiContextSupport
        implements br.jus.trf2.xjus.record.api.XjusRecordAPIContext {
    public XjusRecordAPIContext() {
        super();
    }

    public void atualizarCacheDeConfiguracoes() throws Exception {
        Ex.getInstance().getConf().limparCacheSeNecessario();
    }

    public CpDao inicializarDao() {
        return ExDao.getInstance();
    }

    public EntityManager criarEntityManager() {
        return ExStarter.emf.createEntityManager();
    }

}
