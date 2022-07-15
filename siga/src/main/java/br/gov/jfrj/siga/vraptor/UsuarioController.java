package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import com.crivano.swaggerservlet.SwaggerException;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Try.Success;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.base.util.CPFUtils;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.util.MatriculaUtils;
import br.gov.jfrj.siga.cp.util.SigaUtil;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpPessoaTrocaEmailDTO;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapViaWebService;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.integracao.ldap.IntegracaoLdap;

@Controller
public class UsuarioController extends SigaController {

	private static final Logger LOG = Logger.getLogger(UsuarioAction.class);

	/**
	 * @deprecated CDI eyes only
	 */
	public UsuarioController() {
		super();
	}

	@Inject
	public UsuarioController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}

	@Get({ "/app/usuario/trocar_senha", "/public/app/usuario/trocar_senha" })
	public void trocaSenha() {
		result.include("baseTeste", Prop.getBool("/siga.base.teste"));
	}

	@Transacional
	@Post({ "/app/usuario/trocar_senha_gravar", "/public/app/usuario/trocar_senha_gravar" })
	public void gravarTrocaSenha(UsuarioAction usuario) throws Exception {
		String senhaAtual = usuario.getSenhaAtual();
		String senhaNova = usuario.getSenhaNova();
		String senhaConfirma = usuario.getSenhaConfirma();
		String nomeUsuario = usuario.getNomeUsuario().toUpperCase();		

		if (Prop.isGovSP()) {
			List<CpIdentidade> lista1 = new ArrayList<CpIdentidade>();
			CpIdentidade i = null;
			nomeUsuario = nomeUsuario.replace(".", "").replace("-", "");
			if (!nomeUsuario.matches("[0-9]*")) {
				i = CpDao.getInstance().consultaIdentidadeCadastrante(nomeUsuario, Boolean.TRUE);
			}
			lista1 = CpDao.getInstance()
					.consultaIdentidadesPorCpf(i == null ? nomeUsuario : i.getDpPessoa().getCpfPessoa().toString());

			Cp.getInstance().getBL().trocarSenhaDeIdentidadeGovSp(senhaAtual, senhaNova, senhaConfirma, nomeUsuario,
					getIdentidadeCadastrante(), lista1);

		} else {
			CpIdentidade idNova = Cp.getInstance().getBL().trocarSenhaDeIdentidade(senhaAtual, senhaNova, senhaConfirma,
					nomeUsuario, getIdentidadeCadastrante());
			if ("on".equals(usuario.getTrocarSenhaRede())) {
				try {
					IntegracaoLdapViaWebService.getInstancia().trocarSenha(nomeUsuario, senhaNova);
				} catch (Exception e) {
					LOG.error("Não foi possível alterar a senha de rede de " + nomeUsuario  + ". "
							+ "Tente novamente em alguns instantes", e);
					result.include("mensagem", "Senha do siga alterada com sucesso. Não foi possível alterar a senha de rede e do email. "
							+ "Tente novamente em alguns instantes ou repita a operação desmarcando a caixa \"Trocar também a senha...\"");
					result.include("volta", "troca");
					result.include("titulo", "Troca de Senha");
					result.redirectTo(UsuarioController.class).trocaSenha();
					return;
					
				}
			}

		}			

		result.include("mensagem", "A senha foi alterada com sucesso." + 
				(("on".equals(usuario.getTrocarSenhaRede())) ?  " OBS: As senhas de rede e e-mail também foram alteradas." : ""));
		result.include("volta", "troca");
		result.include("titulo", "Troca de Senha");
		result.redirectTo(UsuarioController.class).trocaSenha();
	}

	/*
	 * Alterar email do usuuário Referente ao Cartão 859
	 */
	@Transacional
	@Get({ "/app/usuario/trocar_email", "/public/app/usuario/trocar_email" })
	public void trocaEmail(UsuarioEmailAction usuario) {
		List<DpPessoaTrocaEmailDTO> lstDto = new ArrayList<DpPessoaTrocaEmailDTO>(
				dao().listarTrocaEmailCPF(so.getCadastrante().getCpfPessoa()));
		if(lstDto.size()>1)
			result.include("variosPerfis",true);
		else
			result.include("variosPerfis",false);
		result.include("usuarios", lstDto);
		result.include("matricula", so.getCadastrante().getSigla());
		result.include("email", so.getCadastrante().getEmailPessoaAtual());
		result.include("baseTeste", Prop.getBool("/siga.base.teste"));
	}	

	@Transacional
	@Post({ "/app/usuario/trocar_email_gravar", "/public/app/usuario/trocar_email_gravar" })
	public void gravarTrocaEmail(UsuarioEmailAction usuario) throws Exception {		
		String emailAtual = so.getCadastrante().getEmailPessoaAtual();
		String emailNovo = usuario.getEmailNovo();
		String emailConfirma = usuario.getEmailConfirma();
		String nomeUsuario = so.getCadastrante().getCpfFormatado().toUpperCase();
		/*
		 * Validacoes
		 */
		
		if (emailAtual == null || emailNovo.trim().length() == 0) {
			throw new AplicacaoException("Email atual não confere");
		}

		if (!validarEmail(emailNovo) || !validarEmail(emailConfirma))
			throw new AplicacaoException("Favor, inserir um email válido");
		
		if(dao().consultarQtdePorEmailIgualCpfDiferente(emailNovo, so.getCadastrante().getCpfPessoa(), so.getCadastrante().getIdPessoaIni())>0)
			throw new AplicacaoException("Existem outros usuários utilizando esse endereço de email. Favor, inserir um email diferente");
		
		if (emailNovo.equals(emailConfirma)) {
			if(usuario.getTeste() != null && usuario.getTeste().equals("TRUE")) {
				List<DpPessoa> lst = new ArrayList<DpPessoa>(dao().listarPorCpf(so.getCadastrante().getCpfPessoa()));
				for (DpPessoa p : lst) {
					try {
						Correio.enviar(p.getEmailPessoaAtual(), "Troca de Email",
								"O Administrador do sistema removeu este endereço de email do seguinte usuário "
										+ "\n" + "\n - Nome: " + p.getNomePessoa() + "\n - Matricula: "
										+ p.getSigla() + "\n - Novo email: " + emailNovo
										+ "\n\n Em caso de dúvidas, favor entrar em contato com o administrador "
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda.");
						Correio.enviar(emailNovo, "Troca de Email",
								"O Administrador do sistema inseriu este endereço de email como seguinte usuário "
										+ "\n" + "\n - Nome: " + p.getNomePessoa() + "\n - Matricula: "
										+ p.getSigla() + "\n - Novo email: " + emailNovo
										+ "\n\n Em caso de dúvidas, favor entrar em contato com o administrador "
										+ "\n\n Atenção: esta é uma "
										+ "mensagem automática. Por favor, não responda.");

					} catch (Exception e) {
						System.out.println(
								"Erro: Não foi possível enviar e-mail para o usuário informando que o administrador do sistema alterou sua senha."
										+ "\n" + "\n - Nome: " + p.getNomePessoa() + "\n - Matricula: "
										+ p.getSigla());
					}
					try {
						dao().iniciarTransacao();
						p.setEmailPessoa(emailNovo);
						dao().gravar(p);
						dao().commitTransacao();

					} catch (final Exception e) {
						dao().rollbackTransacao();
						throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
					}
				}
				
			} else {
				DpPessoa pessoa = so.getCadastrante();
				try {
					Correio.enviar(pessoa.getEmailPessoaAtual(), "Troca de Email",
							"O Administrador do sistema removeu este endereço de email do seguinte usuário "
									+ "\n" + "\n - Nome: " + pessoa.getNomePessoa() + "\n - Matricula: "
									+ pessoa.getSigla() + "\n - Novo email: " + emailNovo
									+ "\n\n Em caso de dúvidas, favor entrar em contato com o administrador "
									+ "\n\n Atenção: esta é uma "
									+ "mensagem automática. Por favor, não responda.");
					Correio.enviar(emailNovo, "Troca de Email",
							"O Administrador do sistema inseriu este endereço de email como seguinte usuário "
									+ "\n" + "\n - Nome: " + pessoa.getNomePessoa() + "\n - Matricula: "
									+ pessoa.getSigla() + "\n - Novo email: " + emailNovo
									+ "\n\n Em caso de dúvidas, favor entrar em contato com o administrador "
									+ "\n\n Atenção: esta é uma "
									+ "mensagem automática. Por favor, não responda.");

				} catch (Exception e) {
					System.out.println(
							"Erro: Não foi possível enviar e-mail para o usuário informando que o administrador do sistema alterou sua senha."
									+ "\n" + "\n - Nome: " + pessoa.getNomePessoa() + "\n - Matricula: "
									+ pessoa.getSigla());
				}
				
				try {
					dao().iniciarTransacao();
					pessoa.setEmailPessoa(emailNovo);
					dao().gravar(pessoa);
					dao().commitTransacao();

				} catch (final Exception e) {
					dao().rollbackTransacao();
					throw new AplicacaoException("Ocorreu um erro durante a gravação", 0, e);
				}
			}
		} else {
			throw new AplicacaoException("O novo email é diferente do email de confirmação");
		}
		
		/*
		 * Final das validacoes
		 */
		
		result.include("mensagem", "Email(s) alterado(s) com sucesso.");
		result.include("volta", "troca");
		result.include("titulo", "Troca de Email");							
		result.redirectTo(UsuarioController.class).trocaEmail(usuario);
	}

	/*
	 * Fim da alteracao Cartao 859
	 */

	@Get({ "/app/usuario/incluir_usuario", "/public/app/usuario/incluir_usuario" })
	public void incluirUsuario() {
		if (!SigaMessages.isSigaSP()) {
			result.include("baseTeste", Prop.getBool("/siga.base.teste"));
			result.include("titulo", SigaMessages.getMessage("usuario.novo"));
			result.include("proxima_acao", "incluir_usuario_gravar");
			result.forwardTo("/WEB-INF/page/usuario/esqueciSenha.jsp");
		} else {
			result.include("mensagem", "Não é possível entrar nesta tela neste ambiente.");
			result.redirectTo("/");
		}

	}

	@Transacional
	@Post({ "/app/usuario/incluir_usuario_gravar", "/public/app/usuario/incluir_usuario_gravar" })
	public void gravarIncluirUsuario(UsuarioAction usuario) throws Exception {
		String msgComplemento = "";
		String[] senhaGerada = new String[1];
		boolean isIntegradoAoAD = isIntegradoAD(usuario.getMatricula());
		CpIdentidade idNova = null;
		switch (usuario.getMetodo()) {
		case 1:

			idNova = Cp.getInstance().getBL().criarIdentidade(usuario.getMatricula(), usuario.getCpf(),
					getIdentidadeCadastrante(), usuario.getSenhaNova(), senhaGerada, isIntegradoAoAD);
			if (isIntegradoAoAD) {
				try {
					IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNova, usuario.getSenhaNova());
				} catch (Exception e) {
					LOG.error("Não foi possível definir a sua senha de rede e e-mail. "
							+ "Tente novamente em alguns instantes", e);
				}
			}
			break;
		case 2:
			if (!Cp.getInstance().getBL().podeAlterarSenha(usuario.getAuxiliar1(), usuario.getCpf1(),
					usuario.getSenha1(), usuario.getAuxiliar2(), usuario.getCpf2(), usuario.getSenha2(),
					usuario.getMatricula(), usuario.getCpf(), usuario.getSenhaNova())) {
				String mensagem = "Não foi possível alterar a senha!<br/>"
						+ "1) As pessoas informadas não podem ser as mesmas;<br/>"
						+ "2) Verifique se as matrículas e senhas foram informadas corretamente;<br/>"
						+ "3) Verifique se as pessoas são da mesma lotação ou da lotação imediatamente superior em relação à matrícula que terá a senha alterada;<br/>";
				result.include("mensagem", mensagem);
				result.redirectTo("/app/usuario/incluir_usuario");
			} else {
				idNova = Cp.getInstance().getBL().criarIdentidade(usuario.getMatricula(), usuario.getCpf(),
						getIdentidadeCadastrante(), usuario.getSenhaNova(), senhaGerada, isIntegradoAoAD);
			}
			break;
		default:
			result.include("mensagem", "Método inválido!");
			result.redirectTo("/app/usuario/incluir_usuario");
		}
		
		if (isIntegradoAoAD){
				msgComplemento = "<br/> Atenção: Sua senha de rede e e-mail foi definida com sucesso.";
		}else{
			msgComplemento = "<br/> " + SigaMessages.getMessage("usuario.primeiroacesso.sucessocomplemento");
		}

		result.include("mensagem", "Usuário cadastrado com sucesso." + msgComplemento);
		result.include("titulo", SigaMessages.getMessage("usuario.novo"));
		result.include("volta", "incluir");
		result.use(Results.page()).forwardTo("/WEB-INF/page/usuario/esqueciSenha.jsp");
	}

	@Get({ "/app/usuario/integracao_ldap", "/public/app/usuario/integracao_ldap" })
	public void isIntegradoLdap(String matricula) throws AplicacaoException {
		try {
			String retorno = isIntegradoAD(matricula) ? "" : "0";
			result.use(Results.http()).body(retorno);
		} catch (Exception e) {
			result.use(Results.http()).body(e.getMessage());
		}
	}

	private boolean isIntegradoAD(String matricula) throws AplicacaoException {
		boolean result = false;

		if (matricula == null || matricula.length() < 2) {
			LOG.warn("A matrícula informada é nula ou inválida");
			throw new AplicacaoException("A matrícula informada é nula ou inválida.");
		}

		String sesbPessoa = MatriculaUtils.getSiglaDoOrgaoDaMatricula(matricula);

		if (sesbPessoa != null) {
			result = IntegracaoLdap.getInstancia().integrarComLdap(sesbPessoa);
		}

		return result;
	}

	@Get({ "/app/usuario/check_email_valido", "/public/app/usuario/check_email_valido" })
	public void checkEmailValido(String matricula) throws AplicacaoException {
		try {
			if (isEmailValido(matricula)) {
				result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
			} else {
				result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
			}

		} catch (Exception e) {
			result.include("ajaxMsgErro", e.getMessage());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_msg_erro.jsp");
		}
	}

	private boolean isEmailValido(String matricula) {

		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();

		if (matricula == null || matricula.length() < 2) {
			LOG.warn("A matrícula informada é nula ou inválida");
			throw new AplicacaoException("A matrícula informada é nula ou inválida.");
		}

		orgaoFlt.setSiglaOrgaoUsu(MatriculaUtils.getSiglaDoOrgaoDaMatricula(matricula));
		CpOrgaoUsuario orgaoUsu = dao.consultarPorSigla(orgaoFlt);

		if (orgaoUsu == null) {
			throw new AplicacaoException("O órgão informado é nulo ou inválido.");
		}

		List<DpPessoa> lstPessoa = null;
		try {
			lstPessoa = dao.consultarPorMatriculaEOrgao(MatriculaUtils.getParteNumericaDaMatricula(matricula),
					orgaoUsu.getId(), false, false);
		} catch (Exception e) {
			throw new AplicacaoException("Formato de matrícula inválida.", 9, e);
		}

		if (lstPessoa.size() == 0){
			throw new AplicacaoException(SigaMessages.getMessage("usuario.erro.naocadastrado"));
		}

		if (lstPessoa != null && lstPessoa.size() == 1) {
			DpPessoa p = lstPessoa.get(0);
			if (p.getEmailPessoaAtual() != null && p.getEmailPessoaAtual().trim().length() > 0) {
				return true;
			} else {
				throw new AplicacaoException("Você ainda não possui um e-mail válido. Tente mais tarde.");
			}
		}

		return false;
	}

	/*
	 * Validador de endereço de email Referente ao cartão 859
	 */
	public static boolean validarEmail(String email) {
		Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");   
	    Matcher matcher = pattern.matcher(email);   
	    return matcher.find();   
	}
	
	/* Reset Senha */
	@Get
	@Path({"/app/usuario/senha/reset", "/public/app/usuario/senha/reset" })
	public void resetSenha() throws Exception {	
		String recaptchaSiteKey = getRecaptchaSiteKey();
		String recaptchaSitePassword = getRecaptchaSitePassword();
		result.include("recaptchaSiteKey", recaptchaSiteKey);
		
		result.include("baseTeste", Prop.getBool("/siga.base.teste"));
	}
	
	@Post
	@Transacional
	@Path({"/app/usuario/senha/gerar-token-reset", "/public/app/usuario/senha/gerar-token-reset" })
	public void gerarTokenReset() throws Exception {

		try { 
			String emailOculto = request.getParameter("emailOculto");
			String jwt = request.getParameter("jwt");
			String strCpf = request.getParameter("cpf");
	
			if (strCpf != null) {
				CPFUtils.efetuaValidacaoSimples(request.getParameter("cpf"));
			} else
				throw new RuntimeException("Usuário não localizado. Verifique os dados informados.");
			
			if (emailOculto == null && "".equals(emailOculto)) {
				throw new RuntimeException("Usuário não localizado. Verifique os dados informados.");
			}
			
			/* --- Verifica Token gerado na pesquisa de acesso: buscarEmailUsuarioPorCpf */
			SigaUtil.verifyGetJwtToken(jwt).get("sub").toString();
			final String TIPO_JWT = "RESET-SENHA";
			if (!TIPO_JWT.equals(SigaUtil.verifyGetJwtToken(jwt).get("tipo").toString()) || !strCpf.equals(SigaUtil.verifyGetJwtToken(jwt).get("sub").toString())) {
				throw new RuntimeException("Não é possível gerar código para redefinir a Senha. Token inválido.");
			}
			/* End */
	
			
			long cpf = Long.valueOf(request.getParameter("cpf"));
	
			DpPessoaDaoFiltro dpPessoa = new DpPessoaDaoFiltro();
			dpPessoa.setBuscarFechadas(false);
			dpPessoa.setCpf(cpf);	
			dpPessoa.setNome("");
	
			List<DpPessoa> usuarios = dao().consultarPorFiltro(dpPessoa);
			boolean emailLocalizado = false;
			if (!usuarios.isEmpty()) {
				for(DpPessoa usuario : usuarios) {
					if (emailOculto.equals(usuario.getEmailPessoaAtualParcialmenteOculto())) {
						boolean autenticaPeloBanco = Cp.getInstance().getBL().buscarModoAutenticacao(usuario.getOrgaoUsuario().getSiglaOrgaoUsu()).equals(GiService._MODO_AUTENTICACAO_BANCO);
						//Autenticação não for via banco
						if (!autenticaPeloBanco)
							throw new RuntimeException("O usuário deve modificar sua senha usando a interface do Windows "
														+ "(acionando as teclas Ctrl, Alt e Del / Delete, opção 'Alterar uma senha')"
														+ ", ou entrando em contato com a Central de Atendimento.");

						CpToken token = Cp.getInstance().getBL().gerarTokenResetSenha(cpf);
						Cp.getInstance().getBL().enviarEmailTokenResetSenha(usuario, "Código para redefinição de SENHA ",token.getToken());
						emailLocalizado = true;
						
						HashMap<String, Object> json = new HashMap<>();
						json.put("ldapEnable", Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(usuario,usuario.getLotacao(),"SIGA;GI;INT_LDAP"));
						result.use(Results.json()).withoutRoot().from(json).serialize();
						
						break;
					}
			    }
			} 
			
			if (!emailLocalizado) {
				throw new RuntimeException("Usuário não localizado. Verifique os dados informados.");
			}

		} catch (RuntimeException ex) {
			result.use(Results.http()).sendError(400, ex.getMessage());
		} catch (Exception ex) {
			result.use(Results.http()).sendError(500, ex.getMessage());
		}
		

		
	}
	
	@Transacional
	@Post({ "/app/usuario/senha/reset", "/public/app/usuario/senha/reset" })
	public void gravarNovaSenha() throws Exception {
	
		String strCpf = request.getParameter("cpf");
		String token = request.getParameter("token");
		String senhaNova = request.getParameter("senhaNova");
		String senhaConfirma = request.getParameter("senhaConfirma");
		String jwt = request.getParameter("jwt");
		String emailOculto = request.getParameter("emailOculto");
		String trocarSenhaRede = request.getParameter("trocarSenhaRede");
		
		try {
			/* --- Valida JWT gerado na pesquisa de acesso: buscarEmailUsuarioPorCpf */
			SigaUtil.verifyGetJwtToken(jwt).get("sub").toString();
			final String TIPO_JWT = "RESET-SENHA";
			if (!TIPO_JWT.equals(SigaUtil.verifyGetJwtToken(jwt).get("tipo").toString()) || !strCpf.equals(SigaUtil.verifyGetJwtToken(jwt).get("sub").toString())) {
				throw new RuntimeException("Não é possível gerar código para redefinir a Senha. Token inválido.");
			}
			/* End */
			
			if (!senhaNova.equals(senhaConfirma)) {
				throw new RuntimeException("Repetição da nova senha não confere, favor redigitar.");
			}
			
			long cpf = Long.valueOf(request.getParameter("cpf"));
			
			//Prosseguir com redefinição se JWT é válido e Token enviado para email é válido
			if (Cp.getInstance().getBL().isTokenValido(CpToken.TOKEN_SENHA, cpf, token)) {
				
				//Obter Todas as identidade para o CPF e redefinir a senha
				List<CpIdentidade> listaIdentidadesCpf = CpDao.getInstance().consultaIdentidadesPorCpf(strCpf);
				
				Cp.getInstance().getBL().redefinirSenha(token, senhaNova, senhaConfirma, strCpf, listaIdentidadesCpf);

				Cp.getInstance().getBL().invalidarTokenUtilizado(CpToken.TOKEN_SENHA, token);
				
				//Redefinir senha de rede de todas as matrículas envolvidas
				if (!listaIdentidadesCpf.isEmpty()) {
					for(CpIdentidade usuario : listaIdentidadesCpf) {
						if ("true".equals(trocarSenhaRede) && Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(usuario.getPessoaAtual(),usuario.getPessoaAtual().getLotacao(),"SIGA;GI;INT_LDAP")) {
							String nomeUsuario = usuario.getPessoaAtual().getSiglaCompleta();	
							try {
								IntegracaoLdapViaWebService.getInstancia().trocarSenha(nomeUsuario, senhaNova);
							} catch (Exception e) {
								LOG.error("Não foi possível alterar a senha de rede de " + nomeUsuario  + ". "
										+ "Tente novamente em alguns instantes", e);
								throw new RegraNegocioException("Senha do siga alterada com sucesso. Não foi possível alterar a senha de rede e do email. "
										+ "Tente novamente em alguns instantes ou repita a operação desmarcando a caixa \"Trocar também a senha...\"");
							}
						}
					}
				}
				
				//Obter email usado e enviar notificação
				if (!listaIdentidadesCpf.isEmpty()) {
					for(CpIdentidade usuario : listaIdentidadesCpf) {
						if (emailOculto.equals(usuario.getDpPessoa().getEmailPessoaAtualParcialmenteOculto())) {
							Cp.getInstance().getBL().enviarEmailDefinicaoSenha(usuario.getDpPessoa(), "Redefinição de SENHA","Você redefiniu sua Senha.");
							break;
						}
				    }
				} 
				
			} else {
				throw new RegraNegocioException("Token para redefinição de Senha inválido ou expirado.");
			}
			
			Cp.getInstance().getBL().consisteFormatoSenha(senhaNova);
					

			result.use(Results.status()).noContent();
		} catch (RuntimeException ex) {
			result.use(Results.http()).sendError(400, ex.getMessage());
		} catch (Exception ex) {
			result.use(Results.http()).sendError(500, ex.getMessage());
		}
		
		
	}
	
	private static String getRecaptchaSiteKey() {
		String pwd = null;
		try {
			pwd = Prop.get("/siga.recaptcha.key");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.recaptcha.key");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.recaptcha.key",
					0, e);
		}
	}
	
	private static String getRecaptchaSitePassword() {
		String pwd = null;
		try {
			pwd = Prop.get("/siga.recaptcha.pwd");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.recaptcha.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.recaptcha.pwd",
					0, e);
		}
	}
}
