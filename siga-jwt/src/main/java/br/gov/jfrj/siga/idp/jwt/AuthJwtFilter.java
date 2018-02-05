package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;


public class AuthJwtFilter implements Filter {
	
	private FilterConfig filterConfig;


	private DecodedJWT validarToken(String token) throws IllegalArgumentException, UnsupportedEncodingException{
		if(token==null){
			throw new RuntimeException("Token inválido");
		}
    	String password = AuthUtils.getInstance().getModuloPassword(filterConfig.getInitParameter("nome-modulo"));
    	Algorithm algorithm = Algorithm.HMAC256(password);

		JWTVerifier verificador = JWT.require(algorithm)
					.withIssuer(AuthUtils.getInstance().getProviderIssuer())
				.build();
		DecodedJWT jwt = verificador.verify(token);
		return jwt;
	}
	
	private String extrairAuthorization(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if(auth != null){
			return request.getHeader("Authorization").replaceAll(".* ", "").trim();
		}
		return null;
	}


	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = extrairAuthorization((HttpServletRequest)request);
		DecodedJWT decodedToken = validarToken(token);
		Claim claim = decodedToken.getClaim("perm");
		AuthUtils.getInstance().setPermissoes(claim.asList(java.lang.String.class)); 
		chain.doFilter(request, response);
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}

}
