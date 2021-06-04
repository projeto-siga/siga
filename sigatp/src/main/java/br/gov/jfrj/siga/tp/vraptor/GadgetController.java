package br.gov.jfrj.siga.tp.vraptor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app")
public class GadgetController extends TpController {

	
	/**
	 * @deprecated CDI eyes only
	 */
	public GadgetController() {
		super();
	}
	
	@Inject
	public GadgetController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,   EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
 	}


    @Path("/gadget")
    public void gadget() {
         result.redirectTo("/app/application/gadget");
    }
}