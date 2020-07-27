package br.gov.jfrj.siga.vraptor;

import java.io.DataInputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.GenericoSelecao;

@Controller
public class TestesController extends SigaController {

	private static final String OK = "<span style=\"color: green;\">OK</span>";
	private static final String ERRO = "<span style=\"color: red;\">ERRO</span>";
	private static final String SIGA_TESTES_ACTION = "/siga/public/app/testes/testes";


	/**
	 * @deprecated CDI eyes only
	 */
	public TestesController() {
		super();
	}

	@Inject
	public TestesController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("/public/app/testes/testes")
	public void testes() {
		String matricula = param("matricula");
		if (matricula == null) {
			throw new AplicacaoException(
					"Par&acirc;metro&nbsp;'matricula'&nbsp;n&atilde;o&nbsp;informado");
		}
		this.testarSiga(matricula);
		this.testarOutrosModulos();
	}

	@Get("/public/app/testes/selecionar")
	public void selecionar(String matricula, String sigla, GenericoSelecao sel)
			throws Exception {
		try {
			DpPessoa pes = getTitular();
			DpLotacao lot = getLotaTitular();
			String testes = "";
			String incluirMatricula = "";
			if (matricula != null) {
				pes = daoPes(param("matricula"));
				lot = pes.getLotacao();
				testes = "/testes";
				incluirMatricula = "&matricula=" + matricula;
			}

			String urlBase = Contexto.urlBase(request);
			String URLSelecionar = "";
			String uRLExibir = "";

			List<String> orgaos = new ArrayList<String>();
			String copiaSigla = sigla.toUpperCase();
			for (CpOrgaoUsuario o : dao().consultaCpOrgaoUsuario()) {
				orgaos.add(o.getSiglaOrgaoUsu());
				orgaos.add(o.getAcronimoOrgaoUsu());
			}
			for (String s : orgaos)
				if (copiaSigla.startsWith(s)) {
					copiaSigla = copiaSigla.substring(s.length());
					break;
				}
			if (copiaSigla.startsWith("-"))
				copiaSigla = copiaSigla.substring(1);

			// alterada a condicao que verifica se eh uma solicitacao do siga-sr
			// dessa forma a regex verifica se a sigla comeca com SR ou sr e
			// termina com numeros
			// necessario para nao dar conflito caso exista uma lotacao que
			// inicie com SR
			if (copiaSigla.startsWith("SR")) {
				// if (copiaSigla.matches("^[SR|sr].*[0-9]+$")) {
				if (Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(pes, lot, "SIGA;SR"))
					URLSelecionar = urlBase + "/sigasr" + testes
							+ "/solicitacao/selecionar?sigla=" + sigla
							+ incluirMatricula;
			} else if (copiaSigla.startsWith("MTP")
					|| copiaSigla.startsWith("RTP")
					|| copiaSigla.startsWith("STP")) {
				if (Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(pes, lot, "SIGA;TP")) {
					URLSelecionar = urlBase + "/sigatp"
							+ "/selecionar.action?sigla=" + sigla
							+ incluirMatricula;
				}
			} else
				URLSelecionar = urlBase + "/sigaex"
						+ (testes.length() > 0 ? testes : "/app/expediente")
						+ "/selecionar?sigla=" + sigla + incluirMatricula;

			SigaHTTP http = new SigaHTTP();
			String[] response = http.get(URLSelecionar).split(";");

			if (response.length == 1 && Integer.valueOf(response[0]) == 0) {
				// verificar se apos retirada dos prefixos referente
				// ao orgao (sigla_orgao_usu = RJ ou acronimo_orgao_usu = JFRJ)
				// e não achar resultado com as opcoes anteriores
				// a string copiaSigla somente possui números
				if (copiaSigla.matches("(^[0-9]+$)")) {
					URLSelecionar = urlBase + "/siga"
							+ (testes.length() > 0 ? testes : "/pessoa")
							+ "/selecionar.action?sigla=" + sigla
							+ incluirMatricula;
				}
				// encontrar lotacoes
				else {
					URLSelecionar = urlBase + "/siga"
							+ (testes.length() > 0 ? testes : "/lotacao")
							+ "/selecionar.action?sigla=" + sigla
							+ incluirMatricula;
				}
				response = http.get(URLSelecionar).split(";");

				if (copiaSigla.matches("(^[0-9]+$)"))
					uRLExibir = "/siga/pessoa/exibir.action?sigla="
							+ response[2];
				else
					uRLExibir = "/siga/lotacao/exibir.action?sigla="
							+ response[2];
			} else {
				if (copiaSigla.startsWith("SR"))
					uRLExibir = "/sigasr/app/solicitacao/exibir/" + response[2];
				else if (copiaSigla.startsWith("MTP")
						|| copiaSigla.startsWith("STP")
						|| copiaSigla.startsWith("RTP"))

					uRLExibir = "/sigatp/exibir.action?sigla=" + response[2];
				else
					uRLExibir = "/sigaex/app/expediente/doc/exibir?sigla="
							+ response[2];
			}
			sel.setId(Long.valueOf(response[1]));
			sel.setSigla(response[2]);
			sel.setDescricao(uRLExibir);

			result.include("sel", sel);
			result.use(Results.page()).forwardTo(
					"/WEB-INF/jsp/ajax_retorno.jsp");

		} catch (Exception e) {
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

	private void testarOutrosModulos() {
		String templateURL = getURL();

		for (ConfiguracaoAcessoTeste acessoTeste : getAcessos()) {
			String urlTeste = templateURL.replace(SIGA_TESTES_ACTION,
					acessoTeste.getUrlTeste());
			super.getRequest().setAttribute(acessoTeste.getAttrURL(), urlTeste);
			super.getRequest().setAttribute(acessoTeste.getAttrNomeTeste(),
					ERRO);

			if (httpTest(urlTeste, acessoTeste.getMustHave()))
				super.getRequest().setAttribute(acessoTeste.getAttrNomeTeste(),
						OK);
		}
	}

	private void testarSiga(String matricula) {
		DpPessoa pes = daoPes(matricula);
		super.getRequest().setAttribute("siga_test_url", "#");
		super.getRequest().setAttribute("siga_test", ERRO);

		if (pes != null)
			super.getRequest().setAttribute("siga_test", OK);
	}

	private String getURL() {
		return MessageFormat.format("{0}?matricula={1}", super.getRequest()
				.getRequestURL().toString(),
				super.getRequest().getParameter("matricula"));
	}

	private List<ConfiguracaoAcessoTeste> getAcessos() {
		return Arrays.asList(new ConfiguracaoAcessoTeste("siga_ex_test_url",
				"siga_ex_test", "/sigaex/public/app/testes/gadgetTest",
				"Atendente"), new ConfiguracaoAcessoTeste("siga_sr_test_url",
				"siga_sr_test", "/sigasr/public/app/testes/gadgetTest",
				"Atendente"), new ConfiguracaoAcessoTeste("siga_gc_test_url",
				"siga_gc_test", "/sigagc/public/app/testes/gadgetTest",
				"Atendente"), new ConfiguracaoAcessoTeste("siga_wf_test_url",
				"siga_wf_test", "/sigawf/public/app/testes/gadgetTest",
				"Atendente"));
	}

	@SuppressWarnings("deprecation")
	private boolean httpTest(String url, String mustHave) {
		try {
			String thisLine;
			URL u = new URL(url);
			try (DataInputStream theHTML = new DataInputStream(u.openStream())) {
				boolean fHas = false;
				while ((thisLine = theHTML.readLine()) != null) {
					if (mustHave != null && thisLine.contains(mustHave))
						fHas = true;
				}
				if (mustHave != null && fHas)
					return fHas;
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	class ConfiguracaoAcessoTeste {
		private String attrURL;
		private String attrNomeTeste;
		private String urlTeste;
		private String mustHave;

		public ConfiguracaoAcessoTeste(String atributoURL,
				String atributoNomeTeste, String url, String mustHave) {
			super();
			this.attrURL = atributoURL;
			this.attrNomeTeste = atributoNomeTeste;
			this.urlTeste = url;
			this.mustHave = mustHave;
		}

		public String getAttrURL() {
			return attrURL;
		}

		public String getAttrNomeTeste() {
			return attrNomeTeste;
		}

		public String getUrlTeste() {
			return urlTeste;
		}

		public String getMustHave() {
			return mustHave;
		}
	}
}
