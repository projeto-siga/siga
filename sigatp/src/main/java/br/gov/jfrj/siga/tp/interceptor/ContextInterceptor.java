package br.gov.jfrj.siga.tp.interceptor;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.jpa.extra.ParameterLoaderInterceptor;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.ParameterOptionalLoaderInterceptor;

/**
 * Interceptor que inicia a instancia do DAO a ser utilizado pelo sistema. O DAO deve ser utilizado quando se deseja realizar operacoes quando nao se pode utilizar o {@link ActiveRecord}.
 * 
 * @author db1.
 *
 */
@RequestScoped
@Intercepts(before = { ParameterLoaderInterceptor.class, ParameterOptionalLoaderInterceptor.class })
public class ContextInterceptor implements Interceptor {

	private EntityManager em;
	private Localization localization;

	public ContextInterceptor(Localization localization, EntityManager em) {
		this.em = em;
		this.localization = localization;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		try {
			ContextoPersistencia.setEntityManager(em);
			MessagesBundle.set(localization);
			TpDao.freeInstance();
			TpDao.getInstance((Session) em.getDelegate(), ((Session) em.getDelegate()).getSessionFactory().openStatelessSession());
			stack.next(method, resourceInstance);
		} catch (Exception e) {
			throw new InterceptionException(e);
		} finally {
			MessagesBundle.remove();
		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}
}