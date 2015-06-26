package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.jboss.security.SecurityContextAssociation;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Scope;
import play.mvc.Scope.Params;
import play.mvc.Scope.RenderArgs;
import play.templates.JavaExtensions;
import br.gov.jfrj.siga.acesso.UsuarioAutenticado;
import br.gov.jfrj.siga.base.AplicacaoException;
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
	
			if (play.Play.mode.isDev()) {
				// Obter usuário logado
				Logger.info("Play executando em modo DEV ..");
				url = getBaseSiga().replace(":null", "");
				url = url + "/usuario_autenticado.action?popup=" + popup + atributos;
				String paginaAutenticada = SigaApplication.getUrl(url, atributos,null,null);
				String[] IDs = paginaAutenticada.split(";");

				DpPessoa dpPessoa = JPA.em().find(DpPessoa.class, Long.parseLong(IDs[0]));
				RenderArgs.current().put("cadastrante",dpPessoa);

				if (IDs[1] != null && !IDs[1].equals("")) {
					DpLotacao dpLotacao = JPA.em().find(DpLotacao.class, Long.parseLong(IDs[1]));
					RenderArgs.current().put("lotaCadastrante",dpLotacao);
				}

				if (IDs[2] != null && !IDs[2].equals("")) {
					DpPessoa dpPessoaTitular = JPA.em().find(DpPessoa.class, Long.parseLong(IDs[2]));
					RenderArgs.current().put("titular",dpPessoaTitular);
				}


				if (IDs[3] != null && !IDs[3].equals("")) {
					DpLotacao dpLotacaoTitular = JPA.em().find(DpLotacao.class, Long.parseLong(IDs[3]));
					RenderArgs.current().put("lotaTitular", dpLotacaoTitular);
				}

				if (IDs[4] != null && !IDs[4].equals("")) {
					CpIdentidade identidadeCadastrante = JPA.em().find(CpIdentidade.class, Long.parseLong(IDs[4]));
					RenderArgs.current().put("identidadeCadastrante", identidadeCadastrante);
				}

				renderArgs.put("currentTimeMillis", new Date().getTime());
			} else {
				
			//	Logger.info("Play executando em modo PROD ..");	
			
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
			}

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

	protected static DpPessoa cadastrante() {
		return (DpPessoa) RenderArgs.current().get("cadastrante");
	}

	protected static DpPessoa titular() {
		return (DpPessoa) RenderArgs.current().get("titular");
	}

	protected static DpLotacao lotaTitular() {
		return (DpLotacao) RenderArgs.current().get("lotaTitular");
	}

	protected static CpIdentidade idc() {
		return (CpIdentidade) RenderArgs.current().get("identidadeCadastrante");
	}

	protected static String getBaseSiga() {
		return "http://" + Play.configuration.getProperty("servidor.principal")+ ":"+Play.configuration.getProperty("porta.principal")+"/siga";
//		return Request.current().getBase() + "/siga";
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
	
	private static String getUrl(String URL, HashMap<String, String> header, Integer timeout, String payload)
			throws AplicacaoException {

		try {

			HttpURLConnection conn = (HttpURLConnection) new URL(URL)
					.openConnection();
			
			if (timeout != null) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}
			
			//conn.setInstanceFollowRedirects(true);

			if (header != null) {
				for (String s : header.keySet()) {
						conn.setRequestProperty(s, header.get(s));
				}
			}	

			System.setProperty("http.keepAlive", "false");
			
			if (payload != null) {
				byte ab[] = payload.getBytes("UTF-8");
				conn.setRequestMethod("POST");
				// Send post request
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(ab);
				os.flush();
				os.close();
			}

			//StringWriter writer = new StringWriter();
			//IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			//return writer.toString();
			return IOUtils.toString(conn.getInputStream(), "UTF-8");
			
		} catch (IOException ioe) {
			throw new AplicacaoException("Não foi possível abrir conexão", 1,
					ioe);
		}

	}
}