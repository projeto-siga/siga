package br.gov.jfrj.siga.wf.api.v1;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CacheControlFilterCache implements Filter {
	static long expires = java.util.concurrent.TimeUnit.DAYS.toSeconds(30);

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Cache-Control", "public,max-age=" + expires + ",must-revalidate");
		resp.setDateHeader("Expires",
				System.currentTimeMillis() + java.util.concurrent.TimeUnit.SECONDS.toMillis(expires));
		resp.setHeader("Pragma", "");

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}