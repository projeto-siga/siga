package br.gov.jfrj.siga.sr.api.v1;

import java.io.Closeable;
import java.io.IOException;

import javax.persistence.EntityManager;

import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.sr.util.SrStarter;


public class ApiContext implements Closeable {
	EntityManager em;
	boolean transacional;
	long inicio = System.currentTimeMillis();

	public ApiContext(boolean transacional) {
		this.transacional = transacional;
		em = SrStarter.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ModeloDao.freeInstance();
		CpDao.getInstance();
		try {
			Cp.getInstance().getConf().limparCacheSeNecessario();
		} catch (Exception e1) {
			throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
		}
		if (this.transacional)
			em.getTransaction().begin();
	}

	public void rollback(Exception e) {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		if (!RequestLoggerFilter.isAplicacaoException(e)) {
			RequestLoggerFilter.logException(null, inicio, e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			if (this.transacional)
				em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
		}
	}
}