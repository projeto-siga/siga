package br.gov.jfrj.siga.gc.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;


public class GcController extends SigaController {

	/**
	 * @deprecated CDI eyes only
	 */
	public GcController() {
		super();
	}
	

	public GcController(HttpServletRequest request, CpDao dao ,Result result,
			SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	public void assertAcesso(String pathServico) throws AplicacaoException {
		so.assertAcesso("GC:Módulo de Gestão de Conhecimento;" + pathServico);
	}

	protected CpDao dao() {
		return CpDao.getInstance();
	}
}
