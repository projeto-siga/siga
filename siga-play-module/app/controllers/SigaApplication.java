package controllers;

import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;
import org.jboss.security.SecurityContextAssociation;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Scope;
import play.mvc.Http.Request;
import play.mvc.Scope.Params;
import play.mvc.Scope.RenderArgs;
import play.templates.JavaExtensions;
import br.gov.jfrj.siga.acesso.UsuarioAutenticado;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Usuario;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class SigaApplication extends Controller {

	protected static void prepararSessao() throws Exception {
		Session playSession = (Session) JPA.em().getDelegate();
		CpDao.freeInstance();
		HibernateUtil.setSessao(playSession); //Karina: Esta linha deve vir antes da linha de baixo, obrigatoriamente ou o podem ocorrer NPEs
		CpDao.getInstance(playSession);
		Cp.getInstance().getConf().limparCacheSeNecessario();
	}

	protected static void obterCabecalhoEUsuario(String backgroundColor) throws Exception {
		SigaHTTP http = new SigaHTTP();
		try {
			request = (request == null) ? Request.current() : request;
			params = (params == null) ? Params.current() : params;
			renderArgs = RenderArgs.current();
			
			// Obter cabecalho e rodape do Siga
			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
	 			if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());

			String popup = params.get("popup");
			if (popup == null	|| (!popup.equals("true") && !popup.equals("false")))
				popup = "false";

			String url = getBaseSiga()	+ "/pagina_vazia.action?popup=" + popup;
			String paginaVazia = http.get(url, null, Scope.Session.current().getId());
			if (!paginaVazia.contains("/sigaidp")){
				String[] pageText = paginaVazia.split("<!-- insert body -->");
				String[] cabecalho = pageText[0].split("<!-- insert menu -->");

				if (backgroundColor != null)
					cabecalho[0] = cabecalho[0].replace("<html>","<html style=\"background-color: " + backgroundColor	+ " !important;\">");

				String[] cabecalho_pre = cabecalho[0].split("</head>");

				String cabecalhoPreHead = cabecalho_pre[0];
				String cabecalhoPreMenu = "</head>" + cabecalho_pre[1];
				String cabecalhoPos = cabecalho.length > 1 ? cabecalho[1] : null;
				String rodape = pageText[1];

				if (cabecalhoPos == null) {
					cabecalhoPos = cabecalhoPreMenu;
					cabecalhoPreMenu = null; 
				}

				RenderArgs.current().put("_cabecalho_pre_head", cabecalhoPreHead);
				RenderArgs.current().put("_cabecalho_pre_menu", cabecalhoPreMenu);
				RenderArgs.current().put("_cabecalho_pos", cabecalhoPos);
				RenderArgs.current().put("_rodape", rodape);
			}
			
			// Obter usuario logado
			String user = SecurityContextAssociation.getPrincipal().getName();
			Usuario usuario = new Usuario();
			CpDao dao = CpDao.getInstance();
			CpIdentidade id = dao.consultaIdentidadeCadastrante(user, true);
			UsuarioAutenticado.carregarUsuario(id, usuario);
			
			RenderArgs.current().put("cadastrante", usuario.getCadastrante());
			RenderArgs.current().put("lotaCadastrante", usuario.getLotaTitular());
			RenderArgs.current().put("titular", usuario.getTitular());
			RenderArgs.current().put("lotaTitular", usuario.getLotaTitular());
			RenderArgs.current().put("identidadeCadastrante", usuario.getIdentidadeCadastrante());

			RenderArgs.current().put("currentTimeMillis", new Date().getTime());

		} catch (ArrayIndexOutOfBoundsException aioob) {
			// Edson: Quando as informações não puderam ser obtidas do Siga,
			// manda para a página de login. Se não for esse o erro, joga
			// exceção pra cima.
			redirect("/siga/redirect.action?uri=" + JavaExtensions.urlEncode(request.url));
		}

	}

	protected static boolean podeUtilizarServico(String servico)
			throws Exception {
		return Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(titular(),lotaTitular(), servico);
	}

	protected static void assertAcesso(String pathServico) throws Exception {
		String servico = "SIGA:Sistema Integrado de Gestão Administrativa;"
				+ pathServico;
		if (servico.endsWith(";"))
			servico = servico.substring(0, servico.length()-1);
		if (!podeUtilizarServico(servico))
			throw new Exception("Acesso negado. Serviço: '" + servico
					+ "' usuário: " + titular().getSigla() + " lotação: "
					+ lotaTitular().getSiglaCompleta());
	}
	
	protected static void tratarExcecoes(Exception e) {
		// MailUtils.sendErrorMail(e);
		if (cadastrante() != null)
			Logger.error("Erro Siga-SR; Pessoa: " + cadastrante().getSigla()
					+ "; Lotação: " + lotaTitular().getSigla(), e);
		e.printStackTrace();
		error(e.getMessage());
	}

	static DpPessoa cadastrante() {
		return (DpPessoa) RenderArgs.current().get("cadastrante");
	}

	static DpPessoa titular() {
		return (DpPessoa) RenderArgs.current().get("titular");
	}

	static DpLotacao lotaTitular() {
		return (DpLotacao) RenderArgs.current().get("lotaTitular");
	}

	static CpIdentidade idc() {
		return (CpIdentidade) RenderArgs.current().get("identidadeCadastrante");
	}

	static String getBaseSiga() {
		return "http://" + Play.configuration.getProperty("servidor.principal")+ ":8080/siga";
	}
	
	@Catch(value = Throwable.class, priority = 1)
	public static void catchError(Throwable throwable) {
		if (Play.mode.isDev())
			return;
		
		// Flash.current().clear();
		// Flash.current().put("_cabecalho_pre",
		// renderArgs.get("_cabecalho_pre"));
		// Flash.current().put("_cabecalho_pos",
		// renderArgs.get("_cabecalho_pos"));
		// Flash.current().put("_rodape", renderArgs.get("_rodape"));
		while(throwable.getMessage() == null && throwable.getCause() != null)
			throwable = throwable.getCause();
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		throwable.printStackTrace(pw);
		String stackTrace = sw.toString();
		String message = throwable.getMessage();
		if (message == null)
			message = "Nenhuma informação disponível.";
		erro(message, stackTrace);
	}
	
	public static void erro(String message, String stackTrace) {
		render(message, stackTrace);
	}
}