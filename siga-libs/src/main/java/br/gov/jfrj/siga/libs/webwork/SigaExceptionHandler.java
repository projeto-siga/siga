package br.gov.jfrj.siga.libs.webwork;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.ExceptionHolder;
import com.opensymphony.xwork.interceptor.ExceptionMappingInterceptor;

public class SigaExceptionHandler extends ExceptionMappingInterceptor {
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	protected void publishException(ActionInvocation invocation,
			ExceptionHolder exceptionHolder) {
		if (!(exceptionHolder.getException() instanceof AplicacaoException)) {
			logaExcecao(exceptionHolder.getException());
		}
		super.publishException(invocation, exceptionHolder);
	}

	/**
	 * 
	 * @param request
	 * @return Matrícula do Usuário obtida através do método getName da
	 *         implementação da interface Principal
	 */
	protected String getUserPrincipalName(HttpServletRequest request) {
		return request.getUserPrincipal() != null ? request.getUserPrincipal()
				.getName() : "";
	}

	/**
	 * Extrai o contexto da aplicação a partir da requisição
	 * 
	 * @param request
	 * @return Contexto da aplicação
	 */
	private String getContexto(HttpServletRequest request) {
		String contexto = request.getContextPath();
		if (StringUtils.isNotBlank(contexto)) {
			contexto = contexto.substring(1);
		}
		return contexto;
	}

	protected String getAction(String uri, String contexto) {
		String action = null;
		if (StringUtils.isNotBlank(uri) && StringUtils.isNotBlank(contexto)) {
			action = uri.replaceFirst(contexto, "");
		}
		return StringUtils.isNotBlank(action) ? action.substring(1) : action;
	}

	protected String getHostName() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn(
					"Não foi possível identificar o nome do Host para adicioná-lo ao Log por CSV",
					e);
			e.printStackTrace();
		}
		return hostName;
	}

	/**
	 * Loga como error as exceções que vierem a acontecer durante a execução dos
	 * ThreadFiltesr
	 * 
	 * @param request
	 * @param ex
	 */
	public void logaExcecao(final Exception ex) {
		HttpServletRequest httpRequest = ServletActionContext.getRequest();

		String url = httpRequest.getRequestURL().toString();
		String queryString = httpRequest.getQueryString() != null ? "?"
				+ httpRequest.getQueryString() : "";
		String principalName = httpRequest.getUserPrincipal() != null ? httpRequest
				.getUserPrincipal().getName() : "convidado";

		String mensagemErro = this.montaMensagemErroExcecoes(ex);

		logger.error(mensagemErro + " URL: " + url + queryString + " User: "
				+ principalName, ex);
	}

	protected String montaMensagemErroExcecoes(Exception ex) {
		String mensagemErro = "";
		if (ex != null) {
			mensagemErro += ex.getMessage();
			if (ex instanceof AplicacaoException && ex.getCause() != null) {
				mensagemErro += " Causa: " + ex.getCause().getMessage();
			}
		}

		return mensagemErro;
	}
}
