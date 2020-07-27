package br.gov.jfrj.siga.base.log;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class RequestExceptionLogger {

	private ServletRequest request;
	private Exception exception;
	private String loggerName;
	private long duracao;

	public RequestExceptionLogger(final ServletRequest r, final Exception e, final Long duracaoRequest,
			final String logger) {
		this.request = r;
		this.exception = e;
		this.loggerName = logger;
		this.duracao = duracaoRequest;
	}

	public void logar() {

		if (!((exception instanceof AplicacaoException) || (exception.getCause() instanceof AplicacaoException))) {

			HttpServletRequest httpReq = (HttpServletRequest) request;
			StringBuffer caminho = httpReq.getRequestURL();
			String parametros = httpReq.getQueryString() == null ? "" : "?" + httpReq.getQueryString();
			caminho.append(parametros);

			StringBuffer requestInfo = new StringBuffer();

			requestInfo.append("\n\n ----- Detalhes do erro -----\n");
			requestInfo.append("Ocorreu um erro durante a execução da operação: ");
			requestInfo.append(exception.getMessage());

			requestInfo.append("\nCaminho: ");
			requestInfo.append(caminho);
			requestInfo.append("\n");

			requestInfo.append("Duração (em ms): ");
			requestInfo.append(duracao);
			requestInfo.append("\n");

			requestInfo.append("Método: ");
			requestInfo.append(httpReq.getMethod());
			requestInfo.append("\n");

			requestInfo.append("Session ID: ");
			requestInfo
					.append(httpReq.getRequestedSessionId() == null ? "indefinido" : httpReq.getRequestedSessionId());
			requestInfo.append("\n");

			requestInfo.append("User Principal: ");
			requestInfo.append(ContextoPersistencia.getUserPrincipal() == null ? "indefinido"
					: ContextoPersistencia.getUserPrincipal());
			requestInfo.append("\n");

			requestInfo.append("Thread ID: ");
			requestInfo.append(Thread.currentThread().getId());
			requestInfo.append("\n");

			requestInfo.append("Thread Name: ");
			requestInfo.append(Thread.currentThread().getName());
			requestInfo.append("\n");

			requestInfo.append("Remote Host: ");
			requestInfo.append(httpReq.getRemoteHost());
			requestInfo.append("\n");

			requestInfo.append("Parâmetros: \n");
			Enumeration<String> params = httpReq.getParameterNames();
			while (params.hasMoreElements()) {
				String name = params.nextElement();
				requestInfo.append("\t");
				requestInfo.append(name);
				requestInfo.append(" : ");
				requestInfo.append(httpReq.getParameter(name));
				requestInfo.append("\n");
			}

			requestInfo.append("Atributos: \n");
			Enumeration<String> attrs = httpReq.getAttributeNames();
			while (attrs.hasMoreElements()) {
				String name = attrs.nextElement();
				requestInfo.append("\t");
				requestInfo.append(name);
				requestInfo.append(" : ");
				try {
					requestInfo.append(httpReq.getAttribute(name));
				} catch (Exception e) {
					requestInfo.append("não foi possível determinar: ");
					requestInfo.append(e.getMessage());
				}
				requestInfo.append("\n");
			}

			requestInfo.append("Headers:  \n");
			Enumeration<String> headers = httpReq.getHeaderNames();
			while (headers.hasMoreElements()) {
				String name = headers.nextElement();
				requestInfo.append("\t");
				requestInfo.append(name);
				requestInfo.append(" : ");
				requestInfo.append(httpReq.getHeader(name));
				requestInfo.append("\n");
			}

			requestInfo.append("Stack Trace:  \n\t");
			requestInfo.append(simplificarStackTrace(exception));
			
			requestInfo.append("\n ------ Fim dos detalhes do erro -----\n\n");

			Logger.getLogger(loggerName).error(requestInfo);
		}

	}

	public static String simplificarStackTrace(Throwable t) {
		if (t == null)
			return null;
		while (t.getCause() != null && t != t.getCause()) {
			if (t.getClass().getSimpleName().equals("AplicacaoException"))
				break;
			t = t.getCause();
		}
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		t.printStackTrace(pw);
		String s = sw.toString();
		String[] lines = s.split(System.getProperty("line.separator"));
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("br.com.caelum.vraptor.core.DefaultReflectionProvider.invoke")) {
				for (int j = i - 1; j > 0; j--) {
					if (lines[j].trim().startsWith("at br.") && !lines[j].contains("$Proxy$_$$_WeldClientProxy")) {
						StringBuilder sb = new StringBuilder();
						for (int k = 0; k <= j; k++) {
							sb.append(lines[k]);
							sb.append(System.getProperty("line.separator"));
						}
						s = sb.toString();
						break;
					}
				}
				break;
			}
		}
		return s;
	}

}
