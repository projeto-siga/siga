package br.gov.jfrj.siga.arquivo;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

public class SigaAuthInterceptor extends HandlerInterceptorAdapter {
	static long DEFAULT_TTL_TOKEN = 3600; // default 1 hora	
	@Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {
        if ("OPTIONS".equals(requestServlet.getMethod())) 
        	return true;

        String token = getCookieValue(requestServlet, "siga-jwt-auth");
        
        if (token == null) {
        	responseServlet.sendError(HttpStatus.SC_FORBIDDEN, "Usuário não autorizado");
        	return false;
        }
        
		try {
	        validarTokenSiga(token);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | JWTVerifyException  e) {
        	responseServlet.sendError(HttpStatus.SC_FORBIDDEN, "Usuário não autorizado. " + e.getMessage());
        	return false;
		}
        return true;
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
    	Cookie[] cookies = req.getCookies();
    	if (cookies != null)
	    	return Arrays.stream(cookies)
	                .filter(c -> c.getName().equals(cookieName))
	                .findFirst()
	                .map(Cookie::getValue)
	                .orElse(null);
    	return null;
    }    
    
	private Map<String, Object> validarTokenSiga(String token) throws Exception {
		if (token == null) {
			throw new AuthenticationException("Token inválido");
		}
		
		final JWTVerifier verifier;
		verifier = new JWTVerifier(System.getProperty("siga.jwt.secret"), System.getProperty("siga.jwt.cookie.domain"));
		return verifier.verify(token);
	}
}