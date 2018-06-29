package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.model.ContextoPersistencia;

import com.auth0.jwt.JWTVerifyException;

public class AuthJwtFormFilter implements Filter {

	public static final String SIGA_JWT_AUTH_COOKIE_NAME = "siga-jwt-auth";
	private static final int TIME_TO_EXPIRE_IN_S = 60 * 60 * 2; // 2h
	private static final int TIME_TO_RENEW_IN_S = 60 * 60; // 1h

	static final String PROVIDER_ISSUER = "sigaidp";
	static long DEFAULT_TTL_TOKEN = 3600; // default 1 hora

	private FilterConfig filterConfig;

	private Map<String, Object> validarToken(String token)
			throws IllegalArgumentException, SigaJwtInvalidException,
			SigaJwtProviderException, InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		if (token == null) {
			throw new SigaJwtInvalidException("Token inválido");
		}
		SigaJwtProvider provider = getProvider();
		return provider.validarToken(token);
	}

	private String renovarToken(String token) throws IllegalArgumentException,
			SigaJwtProviderException, InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		if (token == null) {
			throw new RuntimeException("Token inválido");
		}
		SigaJwtProvider provider = getProvider();
		return provider.renovarToken(token, TIME_TO_EXPIRE_IN_S);
	}

	public SigaJwtProvider getProvider() throws SigaJwtProviderException {
		String password = System.getProperty("idp.jwt.modulo.pwd.sigaidp");
		SigaJwtOptions options = new SigaJwtOptionsBuilder()
				.setPassword(password).setModulo(null)
				.setTTL(TIME_TO_EXPIRE_IN_S).build();
		SigaJwtProvider provider = SigaJwtProvider.getInstance(options);
		return provider;
	}

	private String extrairAuthorization(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (auth != null) {
			return request.getHeader("Authorization").replaceAll(".* ", "")
					.trim();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (SIGA_JWT_AUTH_COOKIE_NAME.equals(c.getName()))
					return c.getValue();
			}
		}
		return null;
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		try {
			String token = extrairAuthorization(req);
			Map<String, Object> decodedToken = validarToken(token);
			final long now = System.currentTimeMillis() / 1000L;
			if ((Integer) decodedToken.get("exp") < now + TIME_TO_RENEW_IN_S) {
				// Seria bom incluir o attributo HttpOnly
				String tokenNew = renovarToken(token);
				Cookie cookie = buildCookie(tokenNew);
				resp.addCookie(cookie);
			}
			ContextoPersistencia.setUserPrincipal((String) decodedToken
					.get("sub"));
			chain.doFilter(request, response);
		} catch (AuthJwtException e) {
			informarAutenticacaoProibida(resp, e);
			return;
		} catch (SigaJwtProviderException e) {
			informarAutenticacaoProibida(resp, e);
			return;
		} catch (JWTVerifyException e) {
			if ("jwt expired".equals(e.getMessage()))
				redirecionarParaFormDeLogin(req, resp, e);
			else
				throw new RuntimeException(e);
		} catch (SigaJwtInvalidException e) {
			redirecionarParaFormDeLogin(req, resp, e);
			return;
		} catch (Exception e) {
			if (e.getCause() instanceof AuthJwtException) {
				informarAutenticacaoProibida(resp, e);
				return;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	public static Cookie buildCookie(String tokenNew) {
		Cookie cookie = new Cookie(SIGA_JWT_AUTH_COOKIE_NAME, tokenNew);
		cookie.setPath("/");
		// cookie.setSecure(true);
		return cookie;
	}

	public static Cookie buildEraseCookie() {
		Cookie cookie = new Cookie(SIGA_JWT_AUTH_COOKIE_NAME, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		return cookie;
	}

	private void redirecionarParaFormDeLogin(HttpServletRequest req,
			HttpServletResponse resp, Exception e) throws IOException {
		if (req.getHeader("X-Requested-With") != null) {
			informarAutenticacaoInvalida(resp, e);
			return;
		}
		if (!"GET".equalsIgnoreCase(req.getMethod())) {
			informarAutenticacaoInvalida(resp, e);
			return;
		}
		resp.sendRedirect("/sigaidp/jwt/login?cont=" + req.getRequestURI());
	}

	private void informarAutenticacaoInvalida(HttpServletResponse resp,
			Exception e) throws IOException {
		String mensagem = "Não foi possível autenticar o usuário, se não quiser perder o trabalho, use uma outra janela do navegador para entrar no sistema e fazer um novo login, depois volte nessa página e clique no botão de atualizar: ";
		if (e.getCause() == null)
			mensagem += e.getLocalizedMessage();
		else
			mensagem += e.getCause().getLocalizedMessage();
		resp.setStatus(401); // 401 Unauthorized - authentication is required
								// and has failed or has not yet been provided.
		resp.getWriter().write(mensagem);
	}

	private void informarAutenticacaoProibida(HttpServletResponse resp,
			Exception e) throws IOException {
		String mensagem = e.getCause() != null ? e.getCause()
				.getLocalizedMessage() : e.getLocalizedMessage();
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
