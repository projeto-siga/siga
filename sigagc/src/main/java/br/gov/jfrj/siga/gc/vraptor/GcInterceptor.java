package br.gov.jfrj.siga.gc.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.InterceptorMethodParametersResolver;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

@Intercepts(before = { 	InterceptorMethodParametersResolver.class, JPATransactionInterceptor.class })
public class GcInterceptor  {

	private final EntityManager manager;
	
	private final static ThreadLocal<Result> resultByThread = new ThreadLocal<Result>();
	
	/**
	 * @deprecated CDI eyes only
	 */
	public GcInterceptor() {
		super();
		this.manager = null;
	}
	
	@Inject
	public GcInterceptor(EntityManager manager, Validator validator,
			ServletContext context, HttpServletRequest request,
			HttpServletResponse response,Result result) {
		this.manager = manager;
		resultByThread.set(result);
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack)  {

		ContextoPersistencia.setEntityManager(this.manager);
		ModeloDao.freeInstance();
		CpDao.getInstance();


		stack.next();

	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return true; // Will intercept all requests
	}

	public static Result result() {
		// TODO Auto-generated method stub
		return resultByThread.get();
	}
}