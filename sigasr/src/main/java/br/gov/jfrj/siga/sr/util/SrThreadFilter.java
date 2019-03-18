package br.gov.jfrj.siga.sr.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class SrThreadFilter extends ThreadFilter {

	public void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {

			throw new ServletException(e);
		} finally {
			CpDao.freeInstance();
			ContextoPersistencia.setEntityManager(null);
		}
	}

	@Override
	protected String getLoggerName() {
		return "br.gov.jfrj.siga.sr";
	}
}
