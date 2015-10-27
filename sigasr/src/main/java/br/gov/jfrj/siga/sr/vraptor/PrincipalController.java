package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
public class PrincipalController  extends SrController{
	public PrincipalController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em,  SrValidator srValidator, Validator validator) {
        super(request, result, dao, so, em, srValidator);
    }
	@Path("/app/principal")
	public void principal() throws Exception {
		//Principal
		result.redirectTo(SolicitacaoController.class).editar(null, null, null, null, null);
	}
}