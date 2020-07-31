package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;

public class AuthJwtFilter implements Filter {
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
		return getProvider().validarToken(token);
	}

	public SigaJwtProvider getProvider() throws SigaJwtProviderException {
		String password = Prop.get("/siga.jwt.secret");
		SigaJwtOptions options = new SigaJwtOptionsBuilder()
				.setPassword(password).setModulo(null)
				.setTTL(DEFAULT_TTL_TOKEN).build();
		SigaJwtProvider provider = SigaJwtProvider.getInstance(options);
		return provider;
	}

	private String extrairAuthorization(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (auth != null) {
			return request.getHeader("Authorization").replaceAll(".* ", "")
					.trim();
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
		
		if(req.getPathInfo().equals("/info")){
			chain.doFilter(request, response);
			return;
		}

		try {

			String token = extrairAuthorization(req);
			Map<String, Object> decodedToken = validarToken(token);
			List<String> claim = (List<String>) decodedToken.get("perm");
			AuthUtils.getInstance().setPermissoes(claim);
			
			chain.doFilter(request, response);
		} catch (JWTVerifyException e) {
			informarAutenticacaoInvalida(resp, e);
			return;
		} catch (AuthJwtException e) {
			informarAutenticacaoProibida(resp, e);
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

	private void informarAutenticacaoInvalida(HttpServletResponse resp,
			Exception e) throws IOException {
		String mensagem = e.getCause() != null ? e.getCause()
				.getLocalizedMessage() : e.getLocalizedMessage();
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
