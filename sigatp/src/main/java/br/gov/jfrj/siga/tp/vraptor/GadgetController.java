package br.gov.jfrj.siga.tp.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app")
public class GadgetController extends TpController {

	
	public GadgetController(HttpServletRequest request, Result result, Validator validator, SigaObjects so, EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
 	}


    @Path("/gadget")
    public void gadget() {
         result.redirectTo("/app/application/gadget");
    }
}