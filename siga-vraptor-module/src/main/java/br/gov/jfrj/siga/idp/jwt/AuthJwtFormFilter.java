package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
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
import javax.servlet.http.HttpSession;

import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
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

	public static Map<String, Object> validarToken(String token)
			throws IllegalArgumentException, SigaJwtInvalidException, SigaJwtProviderException, InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException, SignatureException, IOException, JWTVerifyException {
		if (token == null) {
			throw new SigaJwtInvalidException("Token inválido");
		}
		SigaJwtProvider provider = getProvider();
		return provider.validarToken(token);
	}

	public static String renovarToken(String token)
			throws IllegalArgumentException, SigaJwtProviderException, InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		if (token == null) {
			throw new RuntimeException("Token inválido");
		}
		SigaJwtProvider provider = getProvider();
		return provider.renovarToken(token, TIME_TO_EXPIRE_IN_S);
	}

	public static SigaJwtProvider getProvider() throws SigaJwtProviderException {
		String password = Prop.get("/siga.jwt.secret");
		SigaJwtOptions options = new SigaJwtOptionsBuilder().setPassword(password).setModulo(null)
				.setTTL(TIME_TO_EXPIRE_IN_S).build();
		SigaJwtProvider provider = SigaJwtProvider.getInstance(options);
		return provider;
	}

	public static String extrairAuthorization(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (auth != null) {
			return request.getHeader("Authorization").replaceAll(".* ", "").trim();
		}
		Cookie[] cookies = request.getCookies();
		String token = null;
		ArrayList<String> tokens = new ArrayList<String>();

		if (cookies != null) {
			// Percorre lista cookie e extrai tokens
			for (Cookie c : cookies) {
				if (getNameCookie().equals(c.getName())) {
					tokens.add(c.getValue());
				}
			}
			if (!tokens.isEmpty()) {
				// Se houver apenas 1, retorna para rotina principal validar
				if (tokens.size() == 1) {
					return tokens.get(0);
				} else {
					// Se houver mais de 1, tenta localizar algum token válido
					for (String t : tokens) {
						token = t;
						try {
							validarToken(token);
							return token; // Se houver algum Token Válido Retorna para Rotina Principal
						} catch (Exception e) {
							// Passa para Próximo Token.
						}
					}
					return token; // Se não há nenhum token válido na lista, retorna para rotina explorar o erro
				}
			}
		}
		return null; // Se não há Tokens
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		try {
			if (!req.getRequestURI().equals("/sigaex/autenticar.action")) {
				String token = extrairAuthorization(req);
				Map<String, Object> decodedToken = validarToken(token);
				final long now = System.currentTimeMillis() / 1000L;
				if (((Integer) decodedToken.get("exp")) < (now + TIME_TO_RENEW_IN_S)) {

					String tokenNew = renovarToken(token);
					validarToken(token);
					addCookie(req, resp, buildCookie(tokenNew));
					
//					Cp.getInstance().getBL().logAcesso(AbstractCpAcesso.CpTipoAcessoEnum.RENOVACAO_AUTOMATICA,
//							(String) decodedNewToken.get("sub"), (Integer) decodedNewToken.get("iat"),
//							(Integer) decodedNewToken.get("exp"), HttpRequestUtils.getIpAudit(req));
				}
				ContextoPersistencia.setUserPrincipal((String) decodedToken.get("sub"));
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

	public static Cookie buildCookie(String tokenNew) {
		Cookie cookie = new Cookie(getNameCookie(), tokenNew);
		cookie.setPath("/");

		if (SigaMessages.isSigaSP() && getCookieDomain() != null) {
			cookie.setDomain(getCookieDomain());
		}

		cookie.setMaxAge(TIME_TO_EXPIRE_IN_S);

		return cookie;
	}
	
	private static String removeSpecial(String str) {
		//return str.replaceAll("[a-zA-Z0-9]+", ""); Ajustar REGEX para suportar ., traço e Número
		return str;
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
		response.setHeader("Set-Cookie",
				removeSpecial(cookie.getName()) + "=" + removeSpecial(cookie.getValue()) + "; Path=" + cookie.getPath() + "; Max-Age="
						+ cookie.getMaxAge() + "; Expires=" + new Date(new Date().getTime() + cookie.getMaxAge() * 1000)
						+ "; HttpOnly" + (!Prop.get("/siga.base.url").contains("https") ? "" : "; Secure; SameSite=None"));
	}
	
	public static Cookie buildEraseCookie() {
		Cookie cookie = new Cookie(getNameCookie(), "");
		cookie.setPath("/");
		if (SigaMessages.isSigaSP() && getCookieDomain() != null) {
			cookie.setDomain(getCookieDomain());
		}
		cookie.setMaxAge(0);
		return cookie;
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
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.setAttribute("loginMensagem",
					(e.getClass() != SigaJwtInvalidException.class && e.getClass() != JWTExpiredException.class)
							? SigaMessages.getMessage("login.erro.jwt")
							: "");
		}

		String cont = req.getRequestURL() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
		String base = Prop.get("/siga.base.url");
		if (base != null && base.startsWith("https:") && cont.startsWith("http:"))
			cont = "https" + cont.substring(4);

		resp.sendRedirect("/siga/public/app/login?cont=" + URLEncoder.encode(cont, "UTF-8"));
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

	/**
	 * Este cookie é utilizado na sessão do usuário. Gera o nome do cookie de acordo
	 * com o valor da variavel de name=ambiente que se encontra no standalone.xml do
	 * server. Este metodo é importante para resolver o problema de compatibilidade
	 * de sessao quando se tem mais de uma aplicação aberta no mesmo navegador.
	 * 
	 * @return String
	 */
	private static String getNameCookie() {
		
		if (SIGA_JWT_AUTH_COOKIE_NAME.equals(Prop.get("/siga.jwt.cookie.name")))
			return SIGA_JWT_AUTH_COOKIE_NAME;
		else
			//Add prefixo + name cookie para desambiguacao
			return SIGA_JWT_AUTH_COOKIE_NAME + "-" + Prop.get("/siga.jwt.cookie.name");
	}
	
	private static String getCookieDomain() {
		return Prop.get("/siga.jwt.cookie.domain");
	}

}
