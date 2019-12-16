package br.gov.jfrj.siga.jee;

import java.io.IOException;
import java.util.Enumeration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.jee.ResponseHeaderFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class WebServiceFilter implements Filter {

	private static final Logger log = Logger
			.getLogger(ResponseHeaderFilter.class);
	private EntityManagerFactory factory;

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		EntityManager em = null;
		try {
			em = factory.createEntityManager();
			ContextoPersistencia.setEntityManager(em);
			em.getTransaction().begin();

			chain.doFilter(req, res);

			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}

		} catch (Throwable ex) {
			log.error(ex.getMessage(), ex);
			ex.printStackTrace();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public void init(FilterConfig filterConfig) {
		factory = Persistence.createEntityManagerFactory("default");
	}

	public void destroy() {
		if (factory.isOpen()) {
			factory.close();
		}
	}
}
