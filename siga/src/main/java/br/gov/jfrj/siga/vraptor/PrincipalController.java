package br.gov.jfrj.siga.vraptor;

import java.io.DataInputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class PrincipalController extends SigaController {

	private static final String OK = "<span style=\"color: green;\">OK</span>";
	private static final String ERRO = "<span style=\"color: red;\">ERRO</span>";
	private static final String SIGA_TESTES_ACTION = "/siga/app/testes";
	
	public PrincipalController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/principal")
	public void principal() {
	}
	
	@Get("app/pagina_vazia")
	public void paginaVazia() {
	}
	
	@Get("app/usuario_autenticado")
	public void usuarioAutenticado() {
	}
	
	@Get("app/testes")
	public void testes() {
		this.testarSiga();
		this.testarOutrosModulos();
	}

	private void testarOutrosModulos() {
		String templateURL = getURL();
		
		for (ConfiguracaoAcessoTeste acessoTeste : getAcessos()) {
			String urlTeste = templateURL.replace(SIGA_TESTES_ACTION, acessoTeste.getUrlTeste());
			super.getRequest().setAttribute(acessoTeste.getAttrURL(), urlTeste);
			super.getRequest().setAttribute(acessoTeste.getAttrNomeTeste(), ERRO);

			if (httpTest(urlTeste, acessoTeste.getMustHave()))
				super.getRequest().setAttribute(acessoTeste.getAttrNomeTeste(), OK);
		}
	}

	private void testarSiga() {
		DpPessoa pes = daoPes(param("matricula"));
		super.getRequest().setAttribute("siga_test_url", "#");
		super.getRequest().setAttribute("siga_test", ERRO);

		if (pes != null)
			super.getRequest().setAttribute("siga_test", OK);
	}

	private String getURL() {
		return MessageFormat.format("{0}?matricula={1}", super.getRequest().getRequestURL().toString(), super.getRequest().getParameter("matricula"));
	}

	private List<ConfiguracaoAcessoTeste> getAcessos() {
		return Arrays.asList(
			new ConfiguracaoAcessoTeste("siga_ex_test_url", "siga_ex_test", "/sigaex/app/testes/gadgetTest", "Atendente"),
			new ConfiguracaoAcessoTeste("siga_wf_test_url", "siga_wf_test", "/sigawf/testes/gadgetTest.action", "Atendente"),
			new ConfiguracaoAcessoTeste("siga_cd_test_url", "siga_cd_test", "/sigacd/testes/testes.action", "OK!")
		);
	}
	
	@SuppressWarnings("deprecation")
	private boolean httpTest(String url, String mustHave) {
		try {
			String thisLine;
			URL u = new URL(url);
			DataInputStream theHTML = new DataInputStream(u.openStream());
			boolean fHas = false;
			while ((thisLine = theHTML.readLine()) != null) {
				if (mustHave != null && thisLine.contains(mustHave))
					fHas = true;
			}
			if (mustHave != null && fHas)
				return fHas;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	class ConfiguracaoAcessoTeste {
		private String attrURL;
		private String attrNomeTeste;
		private String urlTeste;
		private String mustHave;
		
		public ConfiguracaoAcessoTeste(String atributoURL, String atributoNomeTeste, String url, String mustHave) {
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