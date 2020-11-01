package br.gov.jfrj.siga.sr.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.InterceptorMethodParametersResolver;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.Sr;
//@RequestScoped
//@Intercepts(before = { 	InterceptorMethodParametersResolver.class, JPATransactionInterceptor.class })
public class ContextInterceptor  {
	private EntityManager em;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ContextInterceptor() {
		super();
	}
	
//	@Inject
	public ContextInterceptor(EntityManager em, Result result)  {
		this.em = em;
	}

//	@Accepts
	public boolean accepts(ControllerMethod method) {
		return true;
	}

//	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws Exception  {
		ContextoPersistencia.setEntityManager(em);
		CpDao.freeInstance();
		CpDao.getInstance();
		Sr.getInstance().getConf().limparCacheSeNecessario();
		stack.next();
	}

}