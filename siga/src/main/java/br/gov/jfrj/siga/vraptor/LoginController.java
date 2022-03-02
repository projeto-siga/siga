package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.AbstractCpAcesso;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapViaWebService;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.idp.jwt.SigaJwtBL;
import br.gov.sp.prodesp.siga.servlet.CallBackServlet;

@Controller
public class LoginController extends SigaController {
	HttpServletResponse response;
	private ServletContext context;

	/**
	 * @deprecated CDI eyes only
	 */
	public LoginController() {
		super();
	}

	@Inject
	public LoginController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		this.response = response;
		this.context = context;
	}

	@Transacional
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
		
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Calendar c = Calendar.getInstance();
		
		result.include("fAviso", "21-11-2019".equals(df.format(c.getTime())));
		result.include("avisoMensagem", "Prezado usuário, o sistema SP Sem Papel passa por instabilidade e a equipe técnica está trabalhando para solucionar o mais rápido possível, assim que restabelecido essa mensagem sairá do ar.");
		result.include("versao", manifest.get("Siga-Versao"));
		result.include("cont", cont);
	}

	@Post("public/app/login")
	@Transacional
	public void auth(String username, String password, String cont) throws IOException {
		try {
			
			if (loginSenhaVazios(username, password)) {
				StringBuffer mensagem = new StringBuffer();
				mensagem.append(SigaMessages.getMessage("usuario.informarlogin"));
				throw new RuntimeException(mensagem.toString());
			}

			
			GiService giService = Service.getGiService();
			String usuarioLogado = giService.login(username, password);

			if (Pattern.matches("\\d+", username) && username.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(username, Boolean.TRUE);
				/* if (lista.size() > 1) {
					throw new RuntimeException("Pessoa com mais de um usuário, favor efetuar login com a matrícula!");
				}*/
			}
			if (usuarioLogado == null || usuarioLogado.trim().length() == 0) {
				StringBuffer mensagem = new StringBuffer();
				mensagem.append(SigaMessages.getMessage("usuario.falhaautenticacao"));
				if(giService.buscarModoAutenticacao(username).equals(GiService._MODO_AUTENTICACAO_LDAP)) {
					mensagem.append(" ");
					mensagem.append(SigaMessages.getMessage("usuario.autenticacaovialdap"));
				}
				
				throw new RuntimeException(mensagem.toString());
			}					
													
			if (isSenhaUsuarioExpirada(usuarioLogado)) {
				result.include("isSenhaUsuarioExpirada", true);
				result.include("loginUsuario", username);
				result.forwardTo(this).login(cont);				
			} else {
				gravaCookieComToken(username, cont);
				result.include("isPinNotDefined", true);
			}
					
			
		} catch (Exception e) {
			result.include("loginMensagem", e.getMessage()); // aqui adicionar tente com a senha de rede windows 
			result.forwardTo(this).login(cont);
		}
	}

	@Get("public/app/logout")
	public void logout() {
		/*
		 * Interrompe a sessão local com SSO
		 */
		request.getSession().setAttribute(CallBackServlet.PUBLIC_CPF_USER_SSO, null);
				
		request.getSession(false);
		this.response.addCookie(AuthJwtFormFilter.buildEraseCookie());
		
		
		result.redirectTo("/");					
		
	}		

	private static String convertStreamToString(java.io.InputStream is) {
		if (is == null)
			return null;
		try (java.util.Scanner s = new java.util.Scanner(is, "UTF-8")) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

	private String extrairAuthorization(HttpServletRequest request) {
		return request.getHeader("Authorization").replaceAll(".* ", "").trim();
	}

	@Get("app/swapUser")
	@Transacional
	public void authSwap(String username, String cont) throws IOException {
		
		try {

			CpIdentidade usuarioSwap = CpDao.getInstance().consultaIdentidadeCadastrante(username, true);
			
			if (usuarioSwap == null)
				throw new ServletException("Usuário não permitido para acesso com a chave " + username + ".");
			
			List<CpIdentidade> idsCpf = CpDao.getInstance().consultaIdentidadesCadastrante(so.getIdentidadeCadastrante().getDpPessoa().getPessoaAtual().getCpfPessoa().toString(), true);
			
			boolean usuarioPermitido = false;
			for (CpIdentidade identCpf : idsCpf) {
				if (identCpf.getNmLoginIdentidade().equals(username)) {
					usuarioPermitido = true;
					break;
				}
			}
			if (!usuarioPermitido)
				throw new ServletException("Usuário não permitido para acesso com a chave " + username + ".");
				
			if (Prop.isGovSP() && !so.getIdentidadeCadastrante().getDscSenhaIdentidade().equals(usuarioSwap.getDscSenhaIdentidade())) 
				throw new ServletException("Senha do usuário atual não confere com a do usuário da lotação.");

			this.response.addCookie(AuthJwtFormFilter.buildEraseCookie());

			gravaCookieComToken(username, cont);
			
		} catch (Exception e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-warning alert-dismissible");
		}
	}

	private void gravaCookieComToken(String username, String cont) throws Exception {
		String modulo = SigaJwtBL.extrairModulo(request);
		SigaJwtBL jwtBL = SigaJwtBL.inicializarJwtBL(modulo);

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
			if (!SigaMessages.isSigaSP())
				cont += "exibirAcessoAnterior=true";
			result.redirectTo(cont);
		} else
			result.redirectTo("/");
	}
	
	private Integer extrairTTL(HttpServletRequest request) throws Exception {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			Integer ttl = new JSONObject(opcoes).optInt("ttl");
			ttl = ttl > 0 ? ttl : null;
			return ttl;
		}

		return null;
	}

	private String extrairPermissoes(HttpServletRequest request) throws Exception {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			return new JSONObject(opcoes).optString("perm");
		}
		return null;
	}	
	
	@Consumes("application/json")
	@Post("public/app/login/novaSenha")
	public void trocarSenhaUsuario(UsuarioAction usuario) throws Exception {
		String cpf = usuario.getCpf();
		String senhaAtual = usuario.getSenhaAtual();
		String senhaNova = usuario.getSenhaNova();
		String senhaConfirma = usuario.getSenhaConfirma();
		String nomeUsuario = usuario.getNomeUsuario().toUpperCase();		
		CpIdentidade identidade = CpDao.getInstance().consultaIdentidadeCadastrante(nomeUsuario, true);
		
		if (identidade == null) {
			throw new RuntimeException("Usuário não encontrado");
		}
						
		if (!StringUtils.isEmpty(cpf)) {
			if (!Long.valueOf(cpf).equals(identidade.getPessoaAtual().getCpfPessoa())) {
				usuario.enviarErro("CPF", "Seu usuário não está vinculado a este CPF");
			}					
		} else {
			usuario.enviarErro("CPF", "Favor informar o CPF");
		}								

		if (!StringUtils.isEmpty(senhaAtual)) {
			final String hashAtual = GeraMessageDigest.executaHash(senhaAtual.getBytes(), "MD5");
			if (!hashAtual.equals(identidade.getDscSenhaIdentidade())) {
				usuario.enviarErro("senhaAtual", "Senha atual está incorreta");
			}			
		} else {
			usuario.enviarErro("senhaAtual", "Favor informar a senha atual");			
		}
		
		if (StringUtils.isEmpty(senhaNova)) {
			usuario.enviarErro("senhaNova", "Favor informar a nova senha");
		}
		
		if (StringUtils.isEmpty(senhaConfirma)) {
			usuario.enviarErro("senhaConfirma", "Favor confirmar a nova senha");
		}		
		
		if (!StringUtils.isEmpty(senhaNova) && !StringUtils.isEmpty(senhaConfirma)) {
			if (!senhaNova.equals(senhaConfirma)) {
				usuario.enviarErro("senhaConfirma", "Senhas não conferem");
			}
			if (!StringUtils.isEmpty(senhaAtual) && senhaNova.equals(senhaAtual)) {
				usuario.enviarErro("senhaNova", "Nova senha deve ser diferente da atual");
			}
		}				
		
		if (!usuario.temErros()) {			
			if (SigaMessages.isSigaSP()) {
				Cp.getInstance().getBL().trocarSenhaDeIdentidadeGovSp(senhaAtual, senhaNova, senhaConfirma, nomeUsuario, 
						identidade, Arrays.asList(identidade));							
			} else {
				if ("on".equals(usuario.getTrocarSenhaRede())) {
					try {
						IntegracaoLdapViaWebService.getInstancia().trocarSenha(nomeUsuario, senhaNova);
					} catch (Exception e) {
						usuario.enviarErro("trocarSenhaRede", "Não foi possível trocar a senha do computador, da rede e do e-mail." 
								+ " Tente novamente em alguns instantes ou repita a operação desmarcando a caixa");												
					}
				}
				
				if (!usuario.temErros()) {
					Cp.getInstance().getBL().trocarSenhaDeIdentidade(senhaAtual, senhaNova, senhaConfirma,
							nomeUsuario, identidade);				
				}
			}						
		}
		
		result.use(Results.json()).from(usuario.semExibirSenhas()).include("erros").serialize();
	}
	
	/**
	 * 1- Verifica se o CPF esta na session.
	 *  
	 * Redireciona o fluxo para o SERVLET openIdServlet
	 * ou
	 * continua com a autenticação
	 * 
	 */
	@Get("public/app/loginSSO")
	@Transacional
	public void loginSSO(String cont) throws AplicacaoException, IOException {
		try {
			
			String cpf = (String) request.getSession().getAttribute(CallBackServlet.PUBLIC_CPF_USER_SSO);

			if(cpf == null){
				result.redirectTo(Contexto.urlBase(request) + "/siga/openIdServlet");	
			}else{
				
				List<CpIdentidade> idsCpf = CpDao.getInstance().consultaIdentidadesCadastrante(cpf, true);
				
				boolean usuarioPermitido = false;
				for (CpIdentidade identCpf : idsCpf) {
					
					usuarioPermitido = true;
					if (identCpf.isBloqueada() || !identCpf.isAtivo()) {
						usuarioPermitido = false;
						break;
					}
				}
				if (!usuarioPermitido)
					throw new ServletException("Usuário não cadastrado ou sem permissão de acesso: " + cpf + ".");
				gravaCookieComToken(cpf, cont);
			}
				
			} catch(AplicacaoException a){
				result.include("loginMensagem", a.getMessage());		
				result.forwardTo(this).login(Contexto.urlBase(request) + "/siga/public/app/login");
			}catch(Exception e){
				throw new AplicacaoException("Não foi possivel acessar o LoginSP." );
		}
	}
	
	private boolean isSenhaUsuarioExpirada(String jsonUsuarioLogado) {		
		try {
			return Boolean.valueOf(new JSONObject(jsonUsuarioLogado).getJSONObject("identidade").getBoolean("isSenhaUsuarioExpirada"));
		} catch (JSONException e) {
			Logger.getLogger(LoginController.class).warn("Não foi possível identificar se a senha do usuário estava expirada ao efetuar login!");
			return false;
		}
	}
	
	private boolean loginSenhaVazios(String username, String password) {
		return ((username == null || username.trim().isEmpty()) || (password == null || password.trim().isEmpty()));
	}

}