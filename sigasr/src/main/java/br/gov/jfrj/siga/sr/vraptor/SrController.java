package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class SrController extends SigaController {

	public SrController(HttpServletRequest request, Result result,
			SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		// result.include("processDefinitions", getProcessDefinitions());
	}

	public void assertAcesso(String pathServico) {
		so.assertAcesso("SR:Módulo de Serviços;" + pathServico);
	}

	protected CpDao dao() {
		return CpDao.getInstance();
	}
}
