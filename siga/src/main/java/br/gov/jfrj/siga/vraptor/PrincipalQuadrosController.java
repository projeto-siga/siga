package br.gov.jfrj.siga.vraptor;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class PrincipalQuadrosController extends SigaController {

	private static Map<String, String> modulos;
	private SigaHTTP http;
	
	static {
		PrincipalQuadrosController.modulos = new HashMap<String, String>();
		PrincipalQuadrosController.modulos.put("sigaex", "/sigaex/app/expediente/gadget?idTpFormaDoc=1&apenasQuadro=true");
		PrincipalQuadrosController.modulos.put("processos", "/sigaex/app/expediente/gadget?idTpFormaDoc=2");
		PrincipalQuadrosController.modulos.put("sigawf", "/sigawf/app/inbox");
		PrincipalQuadrosController.modulos.put("sigasr", "/sigasr/app/solicitacao/gadget");
		PrincipalQuadrosController.modulos.put("sigagc", "/sigagc/app/gadget");
	}

	public PrincipalQuadrosController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		this.http = new SigaHTTP();
	}
	
	@Get("app/principalQuadros/carregaModulo")
	public void carregarModulos(String modulo) {
		if (modulo == null) {
			throw new IllegalArgumentException("Modulo nao informado");
		}
		String url = PrincipalQuadrosController.modulos.get(modulo);

		result
			.use(Results.http())
			.body(invocar(url));
	}
	
	private String invocar(String url) {
		String html = http.get(url, getRequest(), null);
		
		if (html.contains("HTTP Status 404")) {
			return "<span style='color:red' class='error'> 404 - Módulo indisponível. </span>";
		} else if (html.contains("HTTP Status 500")) {
			return "<span style='color:red' class='error'> 500 - Módulo indisponível. </span>";
		} else if (html.contains("HTTP Status")) {
			return "<span style='color:red' class='error'> Módulo indisponível. </span>";
		}
		return html;
	}
}