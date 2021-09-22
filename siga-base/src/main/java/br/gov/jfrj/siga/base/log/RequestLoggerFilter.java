package br.gov.jfrj.siga.base.log;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.gov.jfrj.siga.base.AplicacaoException;

public class RequestLoggerFilter implements Filter {
	static ThreadLocal<ServletRequest> servletRequest = new ThreadLocal<>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		servletRequest.set(request);
		long inicio = System.currentTimeMillis();
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			if (isAplicacaoException(e))
				throw e;
			logException(request, inicio, e);
			throw e;
		}

	}
	
	public static void logException(ServletRequest request, long inicio, Exception e) {
		if (request == null)
			request = servletRequest.get();
		long duracao = System.currentTimeMillis() - inicio;
		new RequestExceptionLogger(request, e, duracao, getLoggerName()).logar();
	}
	
	public static boolean isAplicacaoException(Throwable e) {		
			while (e != null) {
			if (e instanceof AplicacaoException)
				return true;
			if (e.getCause() == null)
				return false;
			e = e.getCause();
		}
		return false;
	}

	protected static String getLoggerName() {
		return "br.gov.jfrj.siga.request.log";
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	


}
