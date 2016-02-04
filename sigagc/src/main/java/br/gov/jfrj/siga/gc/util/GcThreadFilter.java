package br.gov.jfrj.siga.gc.util;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class GcThreadFilter extends ThreadFilter {

	private static final Logger log = Logger.getLogger("br.gov.jfrj.siga.gc");
	
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
			
			StringBuffer caminho = new StringBuffer();
			String parametros = "";
			if (request instanceof HttpServletRequest){
				HttpServletRequest httpReq = (HttpServletRequest)request;
				caminho = httpReq.getRequestURL();
				parametros = httpReq.getQueryString()==null?"":"?" + httpReq.getQueryString();
				caminho.append(parametros);
			}
			log.info("Ocorreu um erro durante a execução da operação: "+ e.getMessage() 
					+ "\ncaminho: " + caminho.toString());
			
			throw new ServletException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
		}
	}
}