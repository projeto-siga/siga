package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.sr.dao.SrDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/equipe")
public class EquipeController extends SrController{

	public EquipeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, SrDao.getInstance(), so, em);
	}
	
	@Path("/listar")
	public void listar() {
		
	}

}
