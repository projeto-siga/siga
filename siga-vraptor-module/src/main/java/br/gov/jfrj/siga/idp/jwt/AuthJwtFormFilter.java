package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.gov.jfrj.siga.cp.util.SigaUtil;
import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.auth.AutenticadorFabrica;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class AuthJwtFormFilter implements Filter {

	public static final String SIGA_JWT_AUTH_COOKIE_NAME = "siga-jwt-auth";
	public static final String SIGA_JWT_AUTH_COOKIE_DOMAIN = null;

	public static final int TIME_TO_EXPIRE_IN_S = 60 * 60 * 8; // 8h é o tempo
																// de duração
	public static final int TIME_TO_RENEW_IN_S = 60 * 60 * 7; // renova
																// automaticamente
																// 7h antes de
																// expirar

	static final String PROVIDER_ISSUER = "sigaidp";
	static long DEFAULT_TTL_TOKEN = 3600; // default 1 hora

	private FilterConfig filterConfig;

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		try {
			if (!req.getRequestURI().equals("/sigaex/autenticar.action")) {
				ContextoPersistencia.setUserPrincipal(AutenticadorFabrica.getInstance().obterPrincipal(req, resp));
			}
			chain.doFilter(request, response);
			/*
			 * Exceções de Sessão o erro não é exposto ao usuário e redireciona para Login
			 */
		} catch (AuthJwtException e) {
			redirecionarParaFormDeLogin(req, resp, e);
			return;
		} catch (SigaJwtProviderException e) {
			redirecionarParaFormDeLogin(req, resp, e);
			return;
		} catch (JWTVerifyException e) {
			if ("jwt expired".equals(e.getMessage()))
				redirecionarParaFormDeLogin(req, resp, e);
			else
				throw new ServletException(e);
		} catch (SigaJwtInvalidException e) {
			redirecionarParaFormDeLogin(req, resp, e);
			return;
		} catch (SignatureException e) {
			redirecionarParaFormDeLogin(req, resp, e);
			return;
		} catch (Exception e) {
			if (e.getCause() instanceof AuthJwtException) {
				redirecionarParaFormDeLogin(req, resp, e);
				return;
			} else {
				/* Erro não gerado pela sessão adiciona na stack */
				throw new ServletException(e);
			}
		} finally {
			ContextoPersistencia.removeUserPrincipal();
		}
	}

	
	private void redirecionarParaFormDeLogin(HttpServletRequest req, HttpServletResponse resp, Exception e)
			throws IOException {
		if (req.getHeader("X-Requested-With") != null) {
			informarAutenticacaoInvalida(resp, e);
			return;
		}
		if (!"GET".equalsIgnoreCase(req.getMethod())) {
			informarAutenticacaoInvalida(resp, e);
			return;
		}

		// Envia Mensagem para Tela de Login
		String mensagem = "";
		if (e.getClass() != SigaJwtInvalidException.class && e.getClass() != JWTExpiredException.class)
			mensagem = URLEncoder.encode(SigaMessages.getMessage("login.erro.jwt"), "UTF-8");
		else 
		    mensagem = e.getLocalizedMessage();
		if (!mensagem.isEmpty())
		    mensagem = "&mensagem=" + URLEncoder.encode(mensagem, "UTF-8");

		String cont = req.getRequestURL() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
		String base = Prop.get("/siga.base.url");
		if (base != null && base.startsWith("https:") && cont.startsWith("http:"))
			cont = "https" + cont.substring(4);

		resp.sendRedirect("/siga/public/app/login?cont=" + URLEncoder.encode(cont, "UTF-8") + mensagem);
	}

	private void informarAutenticacaoInvalida(HttpServletResponse resp, Exception e) throws IOException {
		String mensagem = "Não foi possível autenticar o usuário, se não quiser perder o trabalho, use uma outra janela do navegador para entrar no sistema e fazer um novo login, depois volte nessa página e clique no botão de atualizar: ";
		if (e.getCause() == null)
			mensagem += e.getLocalizedMessage();
		else
			mensagem += e.getCause().getLocalizedMessage();
		resp.setStatus(401); // 401 Unauthorized - authentication is required
								// and has failed or has not yet been provided.
		resp.getWriter().write(mensagem);
	}

	private void informarAutenticacaoProibida(HttpServletResponse resp, Exception e) throws IOException {
		String mensagem = e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage();
		resp.setStatus(403); // 403 Forbidden - The request was valid, but the
								// server is refusing action. The user might not
								// have the necessary permissions for a
								// resource, or may need an account of some sort
		resp.getWriter().write(mensagem);
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}

}
