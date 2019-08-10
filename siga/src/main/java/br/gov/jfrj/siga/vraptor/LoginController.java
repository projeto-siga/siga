package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.AbstractCpAcesso;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;
import br.gov.jfrj.siga.util.SigaJwtBL;

@Resource
public class LoginController extends SigaController {
	HttpServletResponse response;
	private ServletContext context;

	private static String convertStreamToString(java.io.InputStream is) {
		if (is == null)
			return null;
		try (java.util.Scanner s = new java.util.Scanner(is, "UTF-8")) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

	public LoginController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		this.response = response;
		this.context = context;
	}
	
	@Get("public/app/login")
	public void login(String cont) throws IOException {
		Map<String, String> manifest = new HashMap<>();
		try (InputStream is = context.getResourceAsStream("/META-INF/VERSION.MF")) {
			String m = convertStreamToString(is);
			if (m != null) {
				m = m.replaceAll("\r\n", "\n");
				for (String s : m.split("\n")) {
					String a[] = s.split(":", 2);
					if (a.length == 2) {
						manifest.put(a[0].trim(), a[1].trim());
					}
				}
			}
		}

		result.include("versao", manifest.get("Siga-Versao"));
		result.include("cont", cont);
	}

	@Post("public/app/login")
	public void auth(String username, String password, String cont) throws IOException {
		try {
			GiService giService = Service.getGiService();
			String usuarioLogado = giService.login(username, password);
			
			if( Pattern.matches( "\\d+", username) && username.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(username, Boolean.TRUE);
				if(lista.size() > 1) {
					throw new RuntimeException("Pessoa com mais de um usuário, favor efetuar login com a matrícula!");
				}
			}
			if (usuarioLogado == null || usuarioLogado.trim().length() == 0) {
				throw new RuntimeException(SigaMessages.getMessage("usuario.falhaautenticacao"));
			}

			gravaCookieComToken(username, cont);
			
		} catch (Exception e) {
			result.include("loginMensagem", e.getMessage());
			result.forwardTo(this).login(cont);
		}
	}

	@Get("public/app/logout")
	public void logout() {
		this.response.addCookie(AuthJwtFormFilter.buildEraseCookie());
		result.redirectTo("/");
	}

	private String extrairAuthorization(HttpServletRequest request) {
		return request.getHeader("Authorization").replaceAll(".* ", "").trim();
	}

	private SigaJwtBL inicializarJwtBL(String modulo) throws IOException, ServletException {
		SigaJwtBL jwtBL = null;

		try {
			jwtBL = SigaJwtBL.getInstance(modulo);
		} catch (SigaJwtProviderException e) {
			throw new ServletException("Erro ao iniciar o provider", e);
		}

		return jwtBL;
	}

	@Get("app/swapUser")
	public void authSwap(String username, String cont) throws IOException {
		
		try {
			if (!SigaMessages.isSigaSP()) 
				throw new ServletException("Funcionalidade não disponível neste ambiente.");

			CpIdentidade usuarioSwap = CpDao.getInstance().consultaIdentidadeCadastrante(username, true);
			
			if (usuarioSwap == null)
				throw new ServletException("Usuário não permitido para acesso com a chave " + username + ".");
			
			List<CpIdentidade> idsCpf = CpDao.getInstance().consultaIdentidadesCadastrante(so.getIdentidadeCadastrante().getDpPessoa().getCpfPessoa().toString(), true);
			
			boolean usuarioPermitido = false;
			for (CpIdentidade identCpf : idsCpf) {
				if (identCpf.getNmLoginIdentidade().equals(username)) {
					usuarioPermitido = true;
					break;
				}
			}
			if (!usuarioPermitido)
				throw new ServletException("Usuário não permitido para acesso com a chave " + username + ".");
				
			if (!so.getIdentidadeCadastrante().getDscSenhaIdentidade().equals(usuarioSwap.getDscSenhaIdentidade())) 
				throw new ServletException("Senha do usuário atual não confere com a do usuário da lotação.");

			this.response.addCookie(AuthJwtFormFilter.buildEraseCookie());

			gravaCookieComToken(username, cont);
			
		} catch (Exception e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-warning alert-dismissible");
		}
	}

	private void gravaCookieComToken(String username, String cont) throws Exception {
		String modulo = extrairModulo(request);
		SigaJwtBL jwtBL = inicializarJwtBL(modulo);

		String token = jwtBL.criarToken(username, null, null, null);

		Map<String, Object> decodedToken = jwtBL.validarToken(token);
		Cp.getInstance().getBL().logAcesso(AbstractCpAcesso.CpTipoAcessoEnum.AUTENTICACAO,
				(String) decodedToken.get("sub"), (Integer) decodedToken.get("iat"),
				(Integer) decodedToken.get("exp"), HttpRequestUtils.getIpAudit(request));

		response.addCookie(AuthJwtFormFilter.buildCookie(token));

		if (cont != null) {
			if (cont.contains("?"))
				cont += "&";
			else
				cont += "?";
			cont += "exibirAcessoAnterior=true";
			result.redirectTo(cont);
		} else
			result.redirectTo("/");
	}
	
	private String extrairModulo(HttpServletRequest request) throws IOException, ServletException {
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

	private String extrairPermissoes(HttpServletRequest request) throws IOException {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			return new JSONObject(opcoes).optString("perm");
		}
		return null;
	}
}