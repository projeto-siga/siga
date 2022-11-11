package br.gov.jfrj.siga.arquivo;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptions;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptionsBuilder;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProvider;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

public class SigaAuthInterceptor extends HandlerInterceptorAdapter {
	public static final String SIGA_JWT_AUTH_COOKIE_NAME = "siga-jwt-auth";
	public static final String SIGA_JWT_AUTH_COOKIE_DOMAIN = null;

	public static final int TIME_TO_EXPIRE_IN_S = 60 * 60 * 8; // 8h é o tempo
	// de duração
	static long DEFAULT_TTL_TOKEN = 3600; // default 1 hora	
	@Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {
        if ("OPTIONS".equals(requestServlet.getMethod()) 
        		|| "/siga-arq/api/v1/test".equals(requestServlet.getRequestURI())) 
        	return true;

        String token = getCookieValido(requestServlet);
        
        if (token == null) {
        	responseServlet.sendError(HttpStatus.SC_FORBIDDEN, "Usuário não autorizado");
        	return false;
        }
        
        return true;
    }

	public static String getCookieValido(HttpServletRequest request) throws Exception {
		Cookie[] cookies = request.getCookies();
		ArrayList<String> tokens = new ArrayList<String>();

		if (cookies != null) {
			// Percorre lista cookie e extrai tokens
			for (Cookie c : cookies) {
				if (getNameCookie().equals(c.getName())) {
					tokens.add(c.getValue());
				}
			}
			if (!tokens.isEmpty()) {
				// tenta localizar algum token válido
				for (String t : tokens) {
					try {
						getProvider().validarToken(t);
						return t; // Se houver algum Token Válido Retorna para Rotina Principal
					} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | JWTVerifyException  e) {
						// Passa para Próximo Token.
					} catch (Exception e) {
						throw e;
					}
				}
			}
		}
		return null; // Se não há Tokens
	}

	public static SigaJwtProvider getProvider() throws SigaJwtProviderException {
		String password = Prop.get("/siga.jwt.secret");
		SigaJwtOptions options = new SigaJwtOptionsBuilder().setPassword(password).setModulo(null)
				.setTTL(TIME_TO_EXPIRE_IN_S).build();
		SigaJwtProvider provider = SigaJwtProvider.getInstance(options);
		return provider;
	}

	private static String getNameCookie() {
		
		if (SIGA_JWT_AUTH_COOKIE_NAME.equals(Prop.get("/siga.jwt.cookie.name")))
			return SIGA_JWT_AUTH_COOKIE_NAME;
		else
			//Add prefixo + name cookie para desambiguacao
			return SIGA_JWT_AUTH_COOKIE_NAME + "-" + Prop.get("/siga.jwt.cookie.name");
	}
	
}