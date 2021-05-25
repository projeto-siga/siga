package br.gov.jfrj.siga.api.v1;

import javax.persistence.EntityManager;

import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.util.SigaStarter;

public class SigaApiV1Context extends ApiContextSupport {

	public void atualizarCacheDeConfiguracoes() throws Exception {
		Cp.getInstance().getConf().limparCacheSeNecessario();
	}

	public CpDao inicializarDao() {
		return CpDao.getInstance();
	}

	public EntityManager criarEntityManager() {
		return SigaStarter.emf.createEntityManager();
	}

}
