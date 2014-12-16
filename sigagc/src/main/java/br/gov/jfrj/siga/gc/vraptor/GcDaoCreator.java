package br.gov.jfrj.siga.gc.vraptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.GcDao;

@Component
@RequestScoped
public class GcDaoCreator implements ComponentFactory<GcDao> {

	private EntityManager em;

	public GcDaoCreator(EntityManager em) {
		this.em = em;
	}

	@PostConstruct
	public void create() {
	}

	public GcDao getInstance() {
		CpDao.getInstance((Session) em.getDelegate(), ((Session) em
				.getDelegate()).getSessionFactory().openStatelessSession());
		return (GcDao) CpDao.getInstance();
	}

	@PreDestroy
	public void destroy() {
	}

}