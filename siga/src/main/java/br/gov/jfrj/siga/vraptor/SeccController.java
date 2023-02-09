package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class SeccController extends SigaController {

	/**
	 * @deprecated CDI eyes only
	 */
	public SeccController() {
		super();
	}

	@Inject
	public SeccController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	@Get("app/secc/acesso")
	public void pesquisa(String q) throws Exception {
		result.redirectTo(Prop.get("/secc.ui.url"));
	}
	
}
