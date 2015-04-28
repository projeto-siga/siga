package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
public class TesteController extends SigaController{
	
	public TesteController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}

	@Get("/app/teste/leo")
	public void teste() {
		result.use(Results.http()).body("Hello World Cara!");
	}

}
