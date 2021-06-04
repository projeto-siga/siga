package br.gov.jfrj.siga.tp.vraptor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.tp.exceptions.ApplicationControllerException;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
public class PrincipalController extends TpController {

	/**
	 * @deprecated CDI eyes only
	 */
	public PrincipalController() {
		super();
	}
	
	@Inject
	public PrincipalController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Get("/app/principal")
    public void principal() throws ApplicationControllerException {
        // Principal
    	result.redirectTo(ApplicationController.class).index();
    }
}