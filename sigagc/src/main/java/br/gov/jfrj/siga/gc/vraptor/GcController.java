package br.gov.jfrj.siga.gc.vraptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.GcDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class GcController extends SigaController {

	protected GcUtil util;

	public GcController(HttpServletRequest request, Result result, GcDao dao,
			SigaObjects so, GcUtil util) {
		super(request, result, (CpDao) dao, so);
		this.util = util;

		// result.include("processDefinitions", getProcessDefinitions());
	}

	protected void assertAcesso(String pathServico) throws AplicacaoException,
			Exception {
		so.assertAcesso("GC:Módulo de Gestão do Conhecimento;" + pathServico);
	}

	protected GcDao dao() {
		return GcDao.getInstance();
	}
}
