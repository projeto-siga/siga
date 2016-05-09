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

	public void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			
			throw new ServletException(e);
		} finally {
			ContextoPersistencia.setEntityManager(null);
			HibernateUtil.fechaSessaoSeEstiverAberta();
			CpDao.freeInstance();
		}
	}

	@Override
	protected String getLoggerName() {
		return "br.gov.jfrj.siga.sr";
	}
}
