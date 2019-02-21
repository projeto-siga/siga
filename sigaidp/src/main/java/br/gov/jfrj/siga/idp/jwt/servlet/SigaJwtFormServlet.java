package br.gov.jfrj.siga.idp.jwt.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.cp.AbstractCpAcesso;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

/**
 * Servlet para FORM de autenticação usando JWT.
 * 
 * @author tah
 *
 */
public class SigaJwtFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SigaJwtBL instance;

	public SigaJwtFormServlet() throws SigaJwtProviderException {
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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cont = req.getParameter("cont");
		req.setAttribute("cont", cont);
		req.getRequestDispatcher("/paginas/jwt-login.jsp").forward(req, resp);
	}

	/**
	 * Recebe login e senha e tenta autenticar o usuário
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String senha = request.getParameter("password");
		String usuario = request.getParameter("username");
		String cont = request.getParameter("cont");
		try {

			GiService giService = Service.getGiService();
			String usuarioLogado = giService.login(usuario, senha);
			if (usuarioLogado == null || usuarioLogado.trim().length() == 0) {
				throw new RuntimeException("Falha de autenticação!");
			}

			String modulo = extrairModulo(request);
			SigaJwtBL jwtBL = inicializarJwtBL(modulo);

			String token = jwtBL.criarToken(usuario, null, null, null);
			
			Map<String, Object> decodedToken = jwtBL.validarToken(token);
//			Cp.getInstance().getBL().logAcesso(AbstractCpAcesso.CpTipoAcessoEnum.AUTENTICACAO,
//					(String) decodedToken.get("sub"), (Integer) decodedToken.get("iat"),
//					(Integer) decodedToken.get("exp"), HttpRequestUtils.getIpAudit(request));

			response.addCookie(AuthJwtFormFilter.buildCookie(token));
 
			response.sendRedirect(request.getParameter("cont"));
		} catch (Exception e) {

			request.setAttribute("mensagem", e.getMessage());
			request.getRequestDispatcher("/paginas/jwt-login.jsp").forward(
					request, response);
		}
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
