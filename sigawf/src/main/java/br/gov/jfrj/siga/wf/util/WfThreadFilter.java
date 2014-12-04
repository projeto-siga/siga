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
		WfInterceptor i = new WfInterceptor();
		try {
			Wf.setInstance(null);
			i.antes();
			chain.doFilter(request, response);
			i.depois();
		} catch (Exception e) {
			i.excecao();
			throw new ServletException(e);
		} finally {
			i.finalmente();
		}
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}