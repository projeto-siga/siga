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
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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
		DecodedJWT jwt = null;
		try{
			jwt = verificador.verify(token);
		}catch(JWTDecodeException e){
			throw new AuthJwtException("Token inválido.");
		}
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
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		if(req.getPathInfo().equals("/info")){
			chain.doFilter(request, response);
			return;
		}
		try{
			String token = extrairAuthorization(req);
			DecodedJWT decodedToken = validarToken(token);
			Claim claim = decodedToken.getClaim("perm");
			AuthUtils.getInstance().setPermissoes(claim.asList(java.lang.String.class)); 
			chain.doFilter(request, response);
		}catch(TokenExpiredException e){
			informarAutenticacaoInvalida(resp, e);
			return;
		}catch (AuthJwtException e) {
			informarAutenticacaoProibida(resp, e);
			return;
		}catch (Exception e) {
			if(e.getCause() instanceof AuthJwtException){
				informarAutenticacaoProibida(resp, e);
				return;
			}else{
				throw e;
			}
		}
	}

	private void informarAutenticacaoInvalida(HttpServletResponse resp,
			Exception e) throws IOException {
		String mensagem = e.getCause() != null?e.getCause().getLocalizedMessage():e.getLocalizedMessage();
		resp.setStatus(401); //401 Unauthorized - authentication is required and has failed or has not yet been provided.
		resp.getWriter().write(mensagem);
	}

	private void informarAutenticacaoProibida(HttpServletResponse resp, Exception e)
			throws IOException {
		String mensagem = e.getCause() != null?e.getCause().getLocalizedMessage():e.getLocalizedMessage();
		resp.setStatus(403); //403 Forbidden - The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort
		resp.getWriter().write(mensagem);
		return;
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		this.filterConfig = fConfig;
	}

}
