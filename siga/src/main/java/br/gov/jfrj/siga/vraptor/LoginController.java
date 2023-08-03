package br.gov.jfrj.siga.vraptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.SigaVersion;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.auth.AutenticadorFabrica;
import br.gov.jfrj.siga.cp.auth.ValidadorDeSenhaFabrica;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapViaWebService;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.model.enm.NivelDaConta;
import br.gov.sp.prodesp.siga.servlet.CallBackServlet;

@Controller
public class LoginController extends SigaController {
	private static final Logger log = Logger.getLogger("LoginController]");
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
	public void login(String cont, String mensagem) throws IOException {
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Calendar c = Calendar.getInstance();
		
		if (mensagem != null)
		    result.include("loginMensagem", mensagem);
		result.include("fAviso", "21-11-2019".equals(df.format(c.getTime())));
		result.include("avisoMensagem", "Prezado usuário, o sistema SP Sem Papel passa por instabilidade e a equipe técnica está trabalhando para solucionar o mais rápido possível, assim que restabelecido essa mensagem sairá do ar.");
		result.include("versao", SigaVersion.SIGA_VERSION);
		result.include("cont", cont);
	}

	@Post("public/app/login")
	@Transacional
	public void auth(String username, String password, String cont) throws IOException {
		StringBuffer mensagem = new StringBuffer();
		
		try {
			if (loginSenhaVazios(username, password)) {
				
				mensagem.append(SigaMessages.getMessage("usuario.informarlogin"));
				throw new RuntimeException(mensagem.toString());
			}
		
			GiService giService = Service.getGiService();
			String usuarioLogado = giService.login(username, password);

			if (Pattern.matches("\\d+", username) && username.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(username, Boolean.TRUE);
			}
			if (usuarioLogado == null || usuarioLogado.trim().length() == 0) {
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
				result.forwardTo(this).login(cont, null);				
			} else {
				gravaCookieComToken(username, cont);
				result.include("isPinNotDefined", true);
			}
		} catch (Exception e) {
			if (mensagem.length() == 0)
				result.include("loginMensagem", SigaMessages.getMessage("usuario.falhaautenticacao")); 
			else
				result.include("loginMensagem", e.getMessage());
			result.forwardTo(this).login(cont, null);
		}
	}

	@Get("public/app/logout")
	public void logout() throws Exception {
		/*
		 * Interrompe a sessão local com SSO
		 */
		request.getSession().setAttribute(CallBackServlet.PUBLIC_CPF_USER_SSO, null);
				
		request.getSession(false);

		AutenticadorFabrica.getInstance().removerCookie(getRequest(), getResponse());
		
		result.redirectTo("/");					
		
	}		

	private static String convertStreamToString(java.io.InputStream is) {
		if (is == null)
			return null;
		try (java.util.Scanner s = new java.util.Scanner(is, "UTF-8")) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
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

			gravaCookieComToken(username, cont);
			
		} catch (Exception e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-warning alert-dismissible");
		}
	}

	private void gravaCookieComToken(String username, String cont) throws Exception {
	    String token = AutenticadorFabrica.getInstance().criarCookie(request, response, username);

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
		    if (!ValidadorDeSenhaFabrica.getInstance().validarSenha(identidade, senhaAtual))
		        usuario.enviarErro("senhaAtual", "Senha atual está incorreta");
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
			String accessToken = (String) request.getSession().getAttribute(CallBackServlet.PUBLIC_ACCESSTOKEN);
			String nome = (String) request.getSession().getAttribute(CallBackServlet.PUBLIC_NOME_USER_SSO);
			String email = (String) request.getSession().getAttribute(CallBackServlet.PUBLIC_EMAIL_USER_SSO);
			boolean criarUsuarioExterno;
			boolean usuarioPermitido = false;

			if(cpf == null){
				result.redirectTo(Contexto.urlBase(request) + "/siga/openIdServlet");	
			}else{
				
				List<CpIdentidade> idsCpf = CpDao.getInstance().consultaIdentidadesCadastrante(cpf, true, false);
				
				
				for (CpIdentidade identCpf : idsCpf) {
					
					usuarioPermitido = true;
					if (identCpf.isBloqueada() || !identCpf.isAtivo()) {
						usuarioPermitido = false;
						break;
					}
				}
				try {
					criarUsuarioExterno = Prop.getBool("/siga.usuario.externo.criar");
				} catch (Exception e) {
					throw new AplicacaoException("Usuário não existe. Falta definição da propriedade siga.usuario.externo.criar.");
				}

				if (!usuarioPermitido && !criarUsuarioExterno)
					throw new AplicacaoException("Usuário não cadastrado ou sem permissão de acesso: " + cpf + ".");
		
		
				
				/******** TRATAR CÓDIGO PARA VERIFICAR O SELO DE CONFIABILIDADE ANTES DE EMITIR TOKEN DO SIGA ********/
				/* Pode registrar no Token do SIGA o Nível e decidir com regras de Negócio o que pode fazer. Ou usar um PODE ou NAO PODE a partir de tal nível */
				
				Boolean atingiuNivelMinimo =  atingiuNivelContaMinimo(cont, cpf, accessToken);
				
				if (!usuarioPermitido && criarUsuarioExterno) {
					try {
					String idOrgaoUsuarioExterno = Prop.get("/siga.usuario.externo.criar.no.id.orgao");
					String idCargoOrgaoExterno = Prop.get("/siga.usuario.externo.criar.no.id.cargo");
					String idLotacaoOrgaoExterno = Prop.get("/siga.usuario.externo.criar.no.id.lotacao");
					new CpBL().criarUsuario(null, null, Long.parseLong(idOrgaoUsuarioExterno),Long.parseLong(idCargoOrgaoExterno), null, Long.parseLong(idLotacaoOrgaoExterno), nome,null, cpf, email, null,
							null, null, null, null, "S");
					} catch (Exception e) {
						throw new AplicacaoException("Falta definição do conjuntos de propriedades siga.usuario.externo ou erro na criação do usuário." + e.getMessage());
					}
				}
				
				if (atingiuNivelMinimo) {
					gravaCookieComToken(cpf, cont);
				}
				
			}
				
			} catch(AplicacaoException a){
				result.include("loginMensagem", a.getMessage());		
				result.forwardTo(this).login(Contexto.urlBase(request) + "/siga/public/app/login", null);
			}catch(Exception e){
				throw new AplicacaoException("Não foi possivel acessar o ." + Prop.get("/siga.integracao.sso.nome") );
		}
	}

	private Boolean atingiuNivelContaMinimo(String cont, String cpf, String accessToken) throws Exception, AplicacaoException {
		Boolean atingiuNivel = true;
		String nivelDaContaMinimoLido = null;
		try {
			nivelDaContaMinimoLido = Prop.get("/siga.integracao.sso.nivelDaContaMinimo");
		} catch (Exception e) {
			throw new AplicacaoException("Falta definição da propriedade siga.integracao.sso.nivelDaContaMinimo.");
		}

		if (NivelDaConta.valueOf(nivelDaContaMinimoLido).ordinal() != NivelDaConta.OPCIONAL.ordinal()) {
			
			atingiuNivel = false;
			
			List<NivelDaContaGovBr> niveisDaContaGovBr = getNiveisDeConta(accessToken, cpf);
			
			for (NivelDaContaGovBr nivelDaContaGovBr :niveisDaContaGovBr) {
				int nivelDaContaGovBrLido = Integer.parseInt(nivelDaContaGovBr.id);
				if(nivelDaContaGovBrLido >= NivelDaConta.valueOf(nivelDaContaMinimoLido).ordinal())
				{
					atingiuNivel = true;
					return atingiuNivel;
				}
			}
			throw new AplicacaoException("Nivel minimo " + nivelDaContaMinimoLido + " exigido para acesso a aplicação. Aumente o seu nivel no portal do GOV.BR.");
			
		}
		return atingiuNivel;
	}
	
	static class NivelDaContaGovBr 	{
		public String id;
		public Date dataAtualizacao;
	}
	
	private List<NivelDaContaGovBr> getNiveisDeConta(String accessToken, String cpf) throws  Exception, AplicacaoException {
		URL url = new URL(Prop.get("/siga.integracao.sso.nivelDaConta.dominio") + "/confiabilidades/v3/contas/" + cpf + "/niveis?response-type=ids");
		log.debug("Invocando url para recuperar niveis da Conta :" + url); 
		log.debug("Bearer " + accessToken);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		int responseCode = conn.getResponseCode();
		log.debug("GET Response Code :: " + responseCode);
		log.debug("GET Response Message :: " + conn.getResponseMessage());
		List<NivelDaContaGovBr> niveisDaContaGovBr = null;
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder retornoIa = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				retornoIa.append(inputLine);
			}
			in.close();
			// print result
			log.info(retornoIa.toString());
			final ObjectMapper objectMapper = new ObjectMapper();
			niveisDaContaGovBr = objectMapper.readValue(retornoIa.toString(), new TypeReference<List<NivelDaContaGovBr>>(){});	
		} else {
			throw new AplicacaoException("Erro na obtenção do Nivel da conta - httpErrocode : " + responseCode );
		}
		
		conn.disconnect();
		return niveisDaContaGovBr;
		
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