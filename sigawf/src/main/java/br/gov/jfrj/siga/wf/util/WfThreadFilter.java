package br.gov.jfrj.siga.wf.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.vraptor.WfInterceptor;

public class WfThreadFilter extends ThreadFilter {

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		WfExecutionEnvironment ee = new WfExecutionEnvironment();
		try {
			Wf.setInstance(null);
			ee.antes(null);
			chain.doFilter(request, response);
			ee.depois();
		} catch (Exception e) {
			ee.excecao();
			throw new ServletException(e);
		} finally {
			ee.finalmente();
		}
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}