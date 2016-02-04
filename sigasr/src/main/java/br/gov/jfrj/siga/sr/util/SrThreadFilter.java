package br.gov.jfrj.siga.sr.util;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.sr.model.Sr;

public class SrThreadFilter extends ThreadFilter {

	private static final Logger log = Logger.getLogger("br.gov.jfrj.siga.sr");

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			
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
			ContextoPersistencia.setEntityManager(null);
			HibernateUtil.fechaSessaoSeEstiverAberta();
			CpDao.freeInstance();
		}
	}
}
