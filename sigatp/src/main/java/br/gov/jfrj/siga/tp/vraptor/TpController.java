package br.gov.jfrj.siga.tp.vraptor;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class TpController extends SigaController {

    protected Validator validator;

	/**
	 * @deprecated CDI eyes only
	 */
	public TpController() {
		super();
	}
    
    public TpController(HttpServletRequest request, Result result, CpDao dao, Validator validator, SigaObjects so, EntityManager em) {
        super(request, result, dao, so, em);
        this.validator = validator;
        this.result.include("currentTimeMillis", new Date().getTime());
    }

    protected void error(boolean errorCondition, String category, String message) {
        if (errorCondition)	 {
            validator.add(new I18nMessage(category, message));
        }
    }

}