package br.gov.jfrj.siga.tp.interceptor;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.ApplicationLogicException;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.jpa.JPATransactionInterceptor;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaObjects;

/**
 * Interceptor que trata das excecoes ocorridas no sistema. Lanca a excecao e executa a substituicao do bundle quando necessario.
 * 
 * @author db1
 *
 */
@RequestScoped
@Intercepts(after = JPATransactionInterceptor.class)
public class ExceptionHandlerInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerInterceptor.class);
	private SigaObjects so;
	private Localization localization;
	private HttpServletRequest request;

	public ExceptionHandlerInterceptor(Localization localization, SigaObjects so, HttpServletRequest request) {
		this.so = so;
		this.request = request;
		this.localization = localization;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		try {
			stack.next(method, resourceInstance);
		} catch (InterceptionException ie) {
			tratarExcecoes(encontrarCausa(ie));
		} catch (Exception e) {
			tratarExcecoes(e);
		}
	}

	private void tratarExcecoes(Throwable e) {
		log(e);
		redirecionarParaErro(e);
	}

	private Throwable encontrarCausa(InterceptionException ie) {
		Throwable cause = ie.getCause();
		if (cause instanceof ApplicationLogicException) {
			Throwable applicationExceptionCause = cause.getCause();
			return applicationExceptionCause != null ? applicationExceptionCause : cause;
		}
		return cause;
	}

	private void redirecionarParaErro(Throwable e) {
		request.setAttribute("exception", traduzir(e));
		throw new InterceptionException(e);
	}

	private Throwable traduzir(Throwable e) {
		if (e.getMessage() != null) {
			String message = localization.getMessage(e.getMessage());
			if (message.startsWith("???")) {
				return e;
			}
			return criarExcecaoComMesmoStackTrace(e, message);
		}
		return e;
	}

	private Throwable criarExcecaoComMesmoStackTrace(Throwable e, String message) {
		Exception translatedException = new Exception(message);
		translatedException.setStackTrace(e.getStackTrace());
		return translatedException;
	}

	private void log(Throwable e) {
		DpPessoa cadastrante = so.getCadastrante();
		DpLotacao lotaTitular = so.getLotaTitular();

		e.printStackTrace();

		if (so.getCadastrante() != null) {
			LOGGER.error(MessageFormat.format("Erro Siga-TP; Pessoa: {0}; Lotacao: {1}", cadastrante.getSigla(), lotaTitular.getSigla()), e);
		}
		LOGGER.error(e.getMessage(), e);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}
}