package br.gov.jfrj.siga.idp.jwt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.picketlink.common.util.Base64;

import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

/**
 * Servlet para troca de tokens JWT. Ainda não foi utilizado o swagger, pois as
 * bibliotecas do siga são incompatíveis com a versão atual.
 * 
 * @author kpf
 *
 */
public class SigaJwtProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SigaJwtBL instance;

	public SigaJwtProviderServlet() throws SigaJwtProviderException {
		super();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Headers",
				"Authorization,Content-Type,Jwt-Options");
	}

	/**
	 * O HEADER Jwt-Options pode conter perm - OPCIONAL. REGEX com permissões do
	 * siga-gi requeridas ttl - OPCIONAL. Tempo de vida do token em
	 * milissegundos mod - OBRIGATÓRIO. Módulo para o qual o token deve ser
	 * emitido. Essa informação é utilizada para saber a senha a ser utilizada
	 * para gerar a assinatura do token. {"perm":'.*',ttl:'xxx',"mod":"siga-wf"}
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String modulo = extrairModulo(request);
		SigaJwtBL jwtBL = inicializarJwtBL(modulo);

		String result = null;
		if (request.getPathInfo().endsWith("/login")) {
			String b64 = extrairAuthorization(request);
			String[] auth = new String(Base64.decode(b64)).split(":");

			String[] usuarioELotacao = auth[0].split("@");
			String usuario = usuarioELotacao[0];
			String lotacao = usuarioELotacao.length > 1 ? usuarioELotacao[1]
					: null;

			String senha = auth[1];
			String permissoes = null;
			Integer ttl = null;

			permissoes = extrairPermissoes(request);
			ttl = extrairTTL(request);

			String body = request.getReader().readLine();
			try {
				result = jwtBL.login(usuario, lotacao, senha, body, permissoes,
						ttl);
				result = "{token:\"" + result + "\"}";
			} catch (RuntimeException e) {
				response.setStatus(401);
				response.getWriter().write(
						"{token:\"" + e.getLocalizedMessage() + "\"}");
				return;
			}

		} else if (request.getPathInfo().endsWith("/validar")) {
			String token = extrairAuthorization(request);
			try {
				result = jwtBL.validar(token);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		// response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonResult = new JSONObject(result);
		out.println(jsonResult);

	}

	private String extrairAuthorization(HttpServletRequest request) {
		return request.getHeader("Authorization").replaceAll(".* ", "").trim();
	}

	private SigaJwtBL inicializarJwtBL(String modulo) throws IOException,
			ServletException {
		SigaJwtBL jwtBL = null;

		try {
			jwtBL = SigaJwtBL.getInstance(modulo);
		} catch (SigaJwtProviderException e) {
			throw new ServletException("Erro ao iniciar o provider", e);
		}

		return jwtBL;
	}

	private String extrairModulo(HttpServletRequest request)
			throws IOException, ServletException {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			String modulo = new JSONObject(opcoes).optString("mod");
			if (modulo == null || modulo.length() == 0) {
				throw new ServletException(
						"O parâmetro mod deve ser informado no HEADER Jwt-Options do request. Ex: {\"mod\":\"siga-wf\"}");
			}
			return modulo;
		}
		return null;
	}

	private Integer extrairTTL(HttpServletRequest request) throws IOException {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			Integer ttl = new JSONObject(opcoes).optInt("ttl");
			ttl = ttl > 0 ? ttl : null;
			return ttl;
		}

		return null;
	}

	private String extrairPermissoes(HttpServletRequest request)
			throws IOException {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			return new JSONObject(opcoes).optString("perm");
		}
		return null;
	}

}
