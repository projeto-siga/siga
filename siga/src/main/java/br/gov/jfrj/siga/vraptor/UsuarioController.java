package br.gov.jfrj.siga.vraptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.integracao.ldap.IntegracaoLdap;

@Resource
public class UsuarioController extends SigaController {

	private static final Logger LOG = Logger.getLogger(UsuarioAction.class);

	public UsuarioController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}
	
	@Get("/app/trocar_senha")
	public void trocaSenha() {
		result.include("baseTeste", Boolean.valueOf(System.getProperty("isBaseTest").trim()));
	}

	@Post("/app/trocar_senha_gravar")
	public void gravarTrocaSenha(UsuarioAction usuario) throws Exception {
		String msgAD = "";
		String senhaAtual = usuario.getSenhaAtual();
		String senhaNova = usuario.getSenhaNova();
		String senhaConfirma = usuario.getSenhaConfirma();
		String nomeUsuario = usuario.getNomeUsuario().toUpperCase();
		
		CpIdentidade idNova = Cp.getInstance().getBL().trocarSenhaDeIdentidade(
				senhaAtual, senhaNova, senhaConfirma, nomeUsuario,
				getIdentidadeCadastrante());
		
		boolean senhaTrocadaAD = false;
		
		if ("on".equals(usuario.getTrocarSenhaRede())) {
			senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNova,senhaNova);	
		}

		if (isIntegradoAD(nomeUsuario) && senhaTrocadaAD){
			msgAD = "<br/><br/><br/>OBS: A senha de rede e e-mail também foi alterada.";
		}
		
		if (isIntegradoAD(nomeUsuario) && !senhaTrocadaAD){
			msgAD = "<br/><br/><br/>ATENÇÃO: A senha de rede e e-mail NÃO foi alterada embora o seu órgão esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
		}
		
		result.include("mensagem", "A senha foi alterada com sucesso" + msgAD);	
		result.include("volta", "troca");
		result.include("titulo", "Troca de Senha");
		result.redirectTo("/principal.action");
	}

	@Get("/app/incluir_usuario")
	public void incluirUsuario() {
		result.include("baseTeste", Boolean.valueOf(System.getProperty("isBaseTest").trim()));
		result.include("titulo", "Novo Usuário");
		result.include("proxima_acao", "incluir_usuario_gravar");
		result.redirectTo("esqueciSenha.jsp");
	}
	
	@Get("/app/esqueci_senha")
	public void esqueciSenha() {
		result.include("baseTeste", Boolean.valueOf(System.getProperty("isBaseTest").trim()));
		result.include("titulo", "Esqueci Minha Senha");
		result.include("proxima_acao", "esqueci_senha_gravar");
	}
	
	@Post("/app/esqueci_senha_gravar")
	public void gravarEsqueciSenha(UsuarioAction usuario) throws Exception {
		String msgAD = "";
		boolean senhaTrocadaAD = false;
		
		switch (usuario.getMetodo()) {
		case 1:
//			verificarMetodoIntegracaoAD(usuario.getMatricula());
			String[] senhaGerada = new String[1];
			Cp.getInstance().getBL().alterarSenhaDeIdentidade(usuario.getMatricula(),
					usuario.getCpf(), getIdentidadeCadastrante(),senhaGerada);
			break;
		case 2:
			if (!Cp.getInstance().getBL().podeAlterarSenha(usuario.getAuxiliar1(), usuario.getCpf1(),
					usuario.getSenha1(), usuario.getAuxiliar2(), usuario.getCpf2(), usuario.getSenha2(),
					usuario.getMatricula(), usuario.getCpf(), usuario.getSenhaNova())){
				String mensagem = "Não foi possível alterar a senha!<br/>" +
						"1) As pessoas informadas não podem ser as mesmas;<br/>" +
						"2) Verifique se as matrículas e senhas foram informadas corretamente;<br/>" +
						"3) Verifique se as pessoas são da mesma lotação ou da lotação imediatamente superior em relação à matrícula que terá a senha alterada;<br/>";
				result.include("mensagem", mensagem);
				result.redirectTo("/app/esqueci_senha");
				return;
			}
		
			CpIdentidade idAux1 = dao.consultaIdentidadeCadastrante(usuario.getAuxiliar1(), true);
			Cp.getInstance().getBL().definirSenhaDeIdentidade(usuario.getSenhaNova(), usuario.getSenhaConfirma(),
							usuario.getMatricula(), usuario.getAuxiliar1(), usuario.getAuxiliar2(), idAux1);
//			senhaTrocadaAD = IntegracaoLdap.getInstancia().atualizarSenhaLdap(idNovaDefinida,senhaNova);
			break;

		default:
			result.include("mensagem", "Método inválido!");
			result.redirectTo("/app/esqueci_senha");
			return;
		}

		if (isIntegradoAD(usuario.getMatricula()) && senhaTrocadaAD){
			msgAD = "<br/><br/><br/>OBS: A senha de rede e e-mail também foi alterada.";
		}
		
		if (isIntegradoAD(usuario.getMatricula()) && !senhaTrocadaAD){
			msgAD = "<br/><br/><br/>ATENÇÃO: A senha de rede e e-mail NÃO foi alterada embora o seu órgão esteja configurado para integrar as senhas do SIGA, rede e e-mail.";
		}
		
		result.include("mensagem", "A Senha foi alterada com sucesso e foi enviada para seu email" + msgAD);
		result.include("volta", "esqueci");
		result.include("titulo", "Esqueci Minha Senha");
		result.redirectTo("/app/esqueci_senha");
	}

	
	@Get("/app/integracao_ldap")
	public boolean isIntegradoLdap(String matricula) throws AplicacaoException {
		return isIntegradoAD(matricula);
	}

	private boolean isIntegradoAD(String matricula) throws AplicacaoException {
		boolean result = false;
		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();
		
		if (matricula == null || matricula.length() < 2) {
			LOG.warn( "A matrícula informada é nula ou inválida" );
			throw new AplicacaoException( "A matrícula informada é nula ou inválida." );
		}
		
		orgaoFlt.setSiglaOrgaoUsu(matricula.substring(0, 2));		
		CpOrgaoUsuario orgaoUsu = dao.consultarPorSigla(orgaoFlt);
		
		if (orgaoUsu != null) {
			result = IntegracaoLdap.getInstancia().integrarComLdap(orgaoUsu);
		}
		
		return result;
	}
	
	@Get("/app/check_email_valido")
	public void checkEmailValido(String matricula) throws AplicacaoException {
		try{
			isEmailValido(matricula);
			result.use(Results.http()).body("0");
		}catch(Exception e){
			result.use(Results.http()).body(e.getMessage());
		}
	}
	
	private void isEmailValido(String matricula) {
		
		CpOrgaoUsuario orgaoFlt = new CpOrgaoUsuario();
		
		if ( matricula == null || matricula.length() < 2 ) {
			LOG.warn( "A matrícula informada é nula ou inválida" );
			throw new AplicacaoException( "A matrícula informada é nula ou inválida." );
		}
		
		orgaoFlt.setSiglaOrgaoUsu(matricula.substring(0, 2));		
		CpOrgaoUsuario orgaoUsu = dao.consultarPorSigla(orgaoFlt);
		
		if (orgaoUsu == null){
			throw new AplicacaoException("O órgão informado é nulo ou inválido." );
		}

		List<DpPessoa> lstPessoa = null;
		try{
			lstPessoa = dao.consultarPorMatriculaEOrgao(Long.valueOf(matricula.substring(2)), orgaoUsu.getId(), false, false);
		}catch(Exception e){
			throw new AplicacaoException("Formato de matrícula inválida." );
		}

		if (lstPessoa.size() == 0){
			throw new AplicacaoException("O usuário não está cadastrado no banco de dados." );
		}
		
		if (lstPessoa != null && lstPessoa.size() == 1) {
			DpPessoa p = lstPessoa.get(0);
			if (p.getEmailPessoaAtual() != null && p.getEmailPessoaAtual().trim().length() > 0){
				return;
			}else{
				throw new AplicacaoException("Você ainda não possui um e-mail válido. Tente mais tarde." );
			}
		}
		
		return;
	}
	
}
