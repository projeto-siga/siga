package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.lang3.ArrayUtils;
import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.cp.AbstractCpAcesso;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ITokenCriarPost;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.TokenCriarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.TokenCriarPostResponse;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;

public class TokenCriarPost implements ITokenCriarPost {
	@Override
	public void run(TokenCriarPostRequest req, TokenCriarPostResponse resp) throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		try {
			String usuariosRestritos = Utils.getUsuariosRestritos();
			if (usuariosRestritos != null) {
				if (!ArrayUtils.contains(usuariosRestritos.split(","), req.username))
					throw new PresentableUnloggedException("Usuário não autorizado.");
			}

			String origem = "int";
			DpPessoa pessoa = null;

			if (req.username == null || req.username.isEmpty())
				throw new PresentableUnloggedException("Matrícula não foi informada.");

			if (req.password == null || req.password.isEmpty())
				throw new PresentableUnloggedException("Senha não foi informada.");

			final String hashAtual = GeraMessageDigest.executaHash(req.password.getBytes(), "MD5");

			final CpIdentidade id = ExDao.getInstance().consultaIdentidadeCadastrante(req.username.toUpperCase(), true);

			// se o usuário não existir
			if (id == null)
				throw new PresentableUnloggedException("O usuário não está cadastrado.");

			pessoa = id.getDpPessoa().getPessoaAtual();

			boolean senhaValida = id.getDscSenhaIdentidade().equals(hashAtual);

			if (!senhaValida) {
				throw new PresentableUnloggedException("Senha inválida.");
			}

			String jwt = jwt(origem, req.username.toUpperCase(), Long.toString(pessoa.getCpfPessoa()),
					pessoa.getNomePessoa(), pessoa.getEmailPessoaAtual());
			Map<String, Object> decodedNewToken = verify(jwt);

			Cp.getInstance().getBL().logAcesso(AbstractCpAcesso.CpTipoAcessoEnum.AUTENTICACAO,
					(String) decodedNewToken.get("sub"), (Integer) decodedNewToken.get("iat"),
					(Integer) decodedNewToken.get("exp"),
					HttpRequestUtils.getIpAudit(SwaggerServlet.getHttpServletRequest()));
			ExDao.commitTransacao();

			resp.id_token = jwt;
		} catch (PresentableUnloggedException ex) {
			throw ex;
		} catch (SwaggerAuthorizationException ex) {
			throw new PresentableUnloggedException(ex.getMessage(), ex);
		} catch (Throwable ex) {
			throw new PresentableUnloggedException("Erro no login: " + ex.getMessage(), ex);
		}
	}

	private static Map<String, Object> verify(String jwt) throws SwaggerAuthorizationException {
		final JWTVerifier verifier = new JWTVerifier(Utils.getJwtSecret());
		Map<String, Object> map;
		try {
			map = verifier.verify(jwt);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
				| IOException | JWTVerifyException e) {
			throw new SwaggerAuthorizationException(e);
		}
		return map;
	}

	public static class UsuarioDetalhe {
		Long id;
		Long unidade;
	}

	public static class Usuario {
		String origem;
		String email;
		String nome;
		String usuario;
		String cpf;
		Map<String, UsuarioDetalhe> usuarios;

		boolean isInterno() {
			return origem != null && origem.startsWith("int");
		}
	}

	public static Usuario assertUsuario() throws Exception {
		Map<String, Object> jwt = assertUsuarioAutorizado();
		Usuario u = new Usuario();

		u.origem = (String) jwt.get("origin");
		u.email = (String) jwt.get("email");
		u.nome = (String) jwt.get("name");
		u.cpf = (String) jwt.get("cpf");
		u.usuario = (String) jwt.get("sub");
		String users = (String) jwt.get("users");
		if (users != null && users.length() > 0) {
			u.usuarios = new HashMap<>();
			for (String s : users.split(";")) {
				String[] ss = s.split(",");
				UsuarioDetalhe ud = new UsuarioDetalhe();
				if (!"null".equals(ss[1]))
					ud.id = Long.valueOf(ss[1]);
				if (!"null".equals(ss[2]))
					ud.unidade = Long.valueOf(ss[2]);
				u.usuarios.put(ss[0], ud);
			}
		}
		return u;
	}

	public static Map<String, Object> assertUsuarioAutorizado() throws Exception {
		String authorization = getAuthorizationHeader();
		return verify(authorization);
	}

	private static String getAuthorizationHeader() throws SwaggerAuthorizationException {
		String authorization = ExApiV1Servlet.getHttpServletRequest().getHeader("Authorization");
		if (authorization == null)
			throw new SwaggerAuthorizationException("Authorization header is missing");
		if (authorization.startsWith("Bearer "))
			authorization = authorization.substring(7);
		return authorization;
	}

	public static String assertAuthorization() throws SwaggerAuthorizationException {
		String authorization = getAuthorizationHeader();
		verify(authorization);
		return authorization;
	}

	private static String jwt(String origin, String username, String cpf, String name, String email) {
		final String issuer = Utils.getJwtIssuer();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 18 * 60 * 60L; // token expires in 18 hours

		final JWTSigner signer = new JWTSigner(Utils.getJwtSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if (issuer != null)
			claims.put("iss", issuer);
		claims.put("exp", exp);
		claims.put("iat", iat);

		if (origin != null)
			claims.put("origin", origin);
		claims.put("sub", username);
		claims.put("cpf", cpf);
		claims.put("name", name);
		claims.put("email", email);

		final String jwt = signer.sign(claims);
		return jwt;
	}

	@Override
	public String getContext() {
		return "autenticar usuário";
	}

}
