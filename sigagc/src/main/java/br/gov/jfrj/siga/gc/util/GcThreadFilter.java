package br.gov.jfrj.siga.gc.util;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class GcThreadFilter extends ThreadFilter {

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		EntityManager em = Persistence.createEntityManagerFactory("default")
				.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		em.getTransaction().begin();

		try {
			chain.doFilter(request, response);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new ServletException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
		}
	}
}