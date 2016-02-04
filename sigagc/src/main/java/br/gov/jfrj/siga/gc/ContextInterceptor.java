package br.gov.jfrj.siga.gc;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.jpa.extra.ParameterLoaderInterceptor;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.vraptor.ParameterOptionalLoaderInterceptor;

@RequestScoped
@Intercepts(before = { ParameterLoaderInterceptor.class,
		ParameterOptionalLoaderInterceptor.class })
public class ContextInterceptor implements Interceptor {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();

	private final static ThreadLocal<Result> resultByThread = new ThreadLocal<Result>();

	public ContextInterceptor(EntityManager em, Result result) throws Exception{
		ContextoPersistencia.setEntityManager(em);
 		resultByThread.set(result);
 		CpDao.freeInstance();
 		CpDao.getInstance((Session) em.getDelegate(), ((Session) em
 				.getDelegate()).getSessionFactory().openStatelessSession());
	}

	static public EntityManager em() {
		return emByThread.get();
	}

	static public Result result() {
		return resultByThread.get();
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return false;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		return;
	}

}