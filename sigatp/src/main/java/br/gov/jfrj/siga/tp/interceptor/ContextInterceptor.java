package br.gov.jfrj.siga.tp.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.model.TpDao;


/**
 * Interceptor que inicia a instancia do DAO a ser utilizado pelo sistema. O DAO
 * deve ser utilizado quando se deseja realizar operacoes quando nao se pode
 * utilizar o {@link ActiveRecord}.
 * 
 * @author db1.
 *
 */
@RequestScoped
@Intercepts(before = {JPATransactionInterceptor.class })
public class ContextInterceptor   {


	private EntityManager em;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ContextInterceptor() {
		super();
		em = null;
	}
	
	@Inject
	public ContextInterceptor(EntityManager em) {
		this.em = em;
	}

	
	@AroundCall
	public void intercept(SimpleInterceptorStack stack)  {
		try {
			ContextoPersistencia.setEntityManager(this.em);
			//	MessagesBundle.set()
			TpDao.freeInstance();
			TpDao.getInstance();
			stack.next();
		} catch (Exception e) {
			throw new InterceptionException(e);
		} finally {
		//	MessagesBundle.remove();
		}
	}

	@Accepts
	public boolean accepts() {
		return Boolean.TRUE;
	}

}