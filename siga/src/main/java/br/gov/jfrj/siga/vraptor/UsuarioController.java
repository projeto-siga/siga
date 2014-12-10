package br.gov.jfrj.siga.vraptor;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
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
	
}
