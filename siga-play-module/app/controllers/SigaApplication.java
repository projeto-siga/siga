package controllers;

import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http;
import play.templates.JavaExtensions;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class SigaApplication extends Controller {

	protected static void prepararSessao() throws Exception {
		Session playSession = (Session) JPA.em().getDelegate();
		CpDao.freeInstance();
		CpDao.getInstance(playSession, playSession.getSessionFactory().openStatelessSession());
		HibernateUtil.setSessao(playSession);
	}

	protected static void obterCabecalhoEUsuario(String backgroundColor)
			throws Exception {
		try {

			// Obter cabeçalho e rodapé do Siga
			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
				if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());

			String popup = params.get("popup");
			if (popup == null
					|| (!popup.equals("true") && !popup.equals("false")))
				popup = "false";
			String paginaVazia = ConexaoHTTP.get(getBaseSiga()
					+ "/pagina_vazia.action?popup=" + popup, atributos);
			String[] pageText = paginaVazia.split("<!-- insert body -->");
			String[] cabecalho = pageText[0].split("<!-- insert menu -->");

			if (backgroundColor != null)
				cabecalho[0] = cabecalho[0].replace("<html>",
						"<html style=\"background-color: " + backgroundColor
								+ " !important;\">");

			String[] cabecalho_pre = cabecalho[0].split("</head>");

			String cabecalhoPreHead = cabecalho_pre[0];
			String cabecalhoPreMenu = "</head>" + cabecalho_pre[1];
			String cabecalhoPos = cabecalho.length > 1 ? cabecalho[1] : null;
			String rodape = pageText[1];

			if (cabecalhoPos == null) {
				cabecalhoPos = cabecalhoPreMenu;
				cabecalhoPreMenu = null;
			}

			renderArgs.put("_cabecalho_pre_head", cabecalhoPreHead);
			renderArgs.put("_cabecalho_pre_menu", cabecalhoPreMenu);
			renderArgs.put("_cabecalho_pos", cabecalhoPos);
			renderArgs.put("_rodape", rodape);

			// Obter usuário logado
			String[] IDs = ConexaoHTTP.get(
					getBaseSiga() + "/usuario_autenticado.action", atributos)
					.split(";");

			renderArgs.put("cadastrante",
					JPA.em().find(DpPessoa.class, Long.parseLong(IDs[0])));

			if (IDs[1] != null && !IDs[1].equals(""))
				renderArgs.put("lotaCadastrante",
						JPA.em().find(DpLotacao.class, Long.parseLong(IDs[1])));

			if (IDs[2] != null && !IDs[2].equals(""))
				renderArgs.put("titular",
						JPA.em().find(DpPessoa.class, Long.parseLong(IDs[2])));

			if (IDs[3] != null && !IDs[3].equals(""))
				renderArgs.put("lotaTitular",
						JPA.em().find(DpLotacao.class, Long.parseLong(IDs[3])));

			if (IDs[4] != null && !IDs[4].equals("")) {
				CpIdentidade identidadeCadastrante = JPA.em().find(
						CpIdentidade.class, Long.parseLong(IDs[4]));
				renderArgs.put("identidadeCadastrante", identidadeCadastrante);
			}

			renderArgs.put("currentTimeMillis", new Date().getTime());

		} catch (ArrayIndexOutOfBoundsException aioob) {
			// Edson: Quando as informações não puderam ser obtidas do Siga,
			// manda para a página de login. Se não for esse o erro, joga
			// exceção pra cima.
			redirect("/siga/redirect.action?uri=" + JavaExtensions.urlEncode(request.url));
		}

	}

	protected static boolean podeUtilizarServico(String servico)
			throws Exception {
		return Cp
				.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(titular(),
						lotaTitular(), servico);
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
		return (DpPessoa) renderArgs.get("cadastrante");
	}

	static DpPessoa titular() {
		return (DpPessoa) renderArgs.get("titular");
	}

	static DpLotacao lotaTitular() {
		return (DpLotacao) renderArgs.get("lotaTitular");
	}

	static CpIdentidade idc() {
		return (CpIdentidade) renderArgs.get("identidadeCadastrante");
	}

	static String getBaseSiga() {
		return "http://" + Play.configuration.getProperty("servidor.principal")
				+ ":8080/siga";
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
