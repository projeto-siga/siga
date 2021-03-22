package br.gov.jfrj.siga.base.log;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
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
				if (name.toLowerCase().contains("senha") || name.toLowerCase().contains("password")
					|| name.toLowerCase().contains("pwd"))
					requestInfo.append("[parâmetro suprimido]");
				else {
					String p = httpReq.getParameter(name);
					if (p != null && p.length() > 200)
						p = "[parâmetro encurtado] " + p.substring(0, 200) + "...";
					requestInfo.append(p);
				}
				requestInfo.append("\n");
			}

			requestInfo.append("Atributos: \n");
			Enumeration<String> attrs = httpReq.getAttributeNames();
			while (attrs.hasMoreElements()) {
				String name = attrs.nextElement();
				if (name.startsWith("org.jboss.weld"))
					continue;
				requestInfo.append("\t");
				requestInfo.append(name);
				requestInfo.append(" : ");
				try {
					String a = httpReq.getAttribute(name).toString();
					if (a != null && a.length() > 200)
						a = "[atributo encurtado] " + a.substring(0, 200) + "...";
					requestInfo.append(a);
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
				if ("authorization".equalsIgnoreCase(name))
					continue;
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
	
	private static String[] packages = { "br.gov.jfrj.siga", "com.crivano" };

	public static String simplificarStackTrace(Throwable t) {
		if (t == null)
			return null;
		while ((t.getClass().getSimpleName().equals("ServletException") || t.getClass().getSimpleName().equals("InterceptionException") || t.getClass().getSimpleName().equals("InvocationTargetException") || t.getClass().getSimpleName().equals("ProxyInvocationException")) && t.getCause() != null && t != t.getCause()) {
 			t = t.getCause();
 		}
		String s = simplifyStackTrace(t, packages);
		return s;
	}
		
	public static String simplifyStackTrace(Throwable t, String[] pkgs) {
		if (t == null)
			return null;
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		t.printStackTrace(pw);
		String s = sw.toString();
		final String lineSeparator = System.lineSeparator();
		if (true) {
			StringBuilder sb = new StringBuilder();
			String[] lines = s.split(System.getProperty("line.separator"));
			for (int i = 0; i < lines.length; i++) {
				String l = lines[i];
				boolean isInPackages = false;
				if (pkgs != null) {
					for (String pkg : pkgs) {
						isInPackages |= l.contains(pkg);
					}
				}
				if (!l.startsWith("\t") || (isInPackages && !l.contains("$$_Weld") && !l.contains(".invoke(")
						&& !l.contains(".doFilter("))) {
					sb.append(l);
					sb.append(System.getProperty("line.separator"));
				}
			}
			s = sb.toString();
		}
		return s;
	}

}
