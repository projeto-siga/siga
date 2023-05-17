package br.gov.jfrj.siga.api.v1;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IAutenticarPost;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.auth.AutenticadorFabrica;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.vraptor.Transacional;

@AcessoPublico
@Transacional
public class AutenticarPost implements IAutenticarPost {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		try {
            HttpServletRequest request = ctx.getCtx().getRequest();

			final String authorization = request.getHeader("Authorization");
			if (authorization == null || !authorization.toLowerCase().startsWith("basic"))
				throw new Exception("Header Authorization deve conter uma autorização do tipo Basic");
			String base64Credentials = authorization.substring("Basic".length()).trim();
			byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
			String credentials = new String(credDecoded, StandardCharsets.UTF_8);
			final String[] values = credentials.split(":", 2);

			String username = values[0];
			String password = values[1];

			GiService giService = Service.getGiService();
			String usuarioLogado = giService.login(username, password);

			if (Pattern.matches("\\d+", username) && username.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(username, Boolean.TRUE);
				if (lista.size() > 1) {
					throw new RuntimeException("Pessoa com mais de um usuário, favor efetuar login com a matrícula!");
				}
			}
			if (usuarioLogado == null || usuarioLogado.trim().length() == 0) {
				StringBuffer mensagem = new StringBuffer();
				mensagem.append(SigaMessages.getMessage("usuario.falhaautenticacao"));
				if (giService.buscarModoAutenticacao(username).equals(GiService._MODO_AUTENTICACAO_LDAP)) {
					mensagem.append(" ");
					mensagem.append(SigaMessages.getMessage("usuario.autenticacaovialdap"));
				}

				throw new RuntimeException(mensagem.toString());
			}
			
			String token = AutenticadorFabrica.getInstance().criarCookie(request, ctx.getCtx().getResponse(), username);
			resp.token = token;
		} catch (Exception ex) {
			throw new PresentableUnloggedException("Erro no login: " + ex.getMessage(), ex);
		}
	}

	@Override
	public String getContext() {
		return "autenticar usuário";
	}

//	public static Cookie buildCookie(String tokenNew) {
//		Cookie cookie = new Cookie(AuthJwtFormFilter.SIGA_JWT_AUTH_COOKIE_NAME, tokenNew);
//		cookie.setPath("/");
//		if (AuthJwtFormFilter.SIGA_JWT_AUTH_COOKIE_DOMAIN != null)
//			cookie.setDomain(AuthJwtFormFilter.SIGA_JWT_AUTH_COOKIE_DOMAIN);
//
//		cookie.setMaxAge(AuthJwtFormFilter.TIME_TO_EXPIRE_IN_S);
//
//		// cookie.setSecure(true);
//		return cookie;
//	}

	public static Cookie buildEraseCookie() {
		Cookie cookie = new Cookie(AuthJwtFormFilter.SIGA_JWT_AUTH_COOKIE_NAME, "");
		cookie.setPath("/");
		if (AuthJwtFormFilter.SIGA_JWT_AUTH_COOKIE_DOMAIN != null)
			cookie.setDomain(AuthJwtFormFilter.SIGA_JWT_AUTH_COOKIE_DOMAIN);

		cookie.setMaxAge(0);
		return cookie;
	}

}
