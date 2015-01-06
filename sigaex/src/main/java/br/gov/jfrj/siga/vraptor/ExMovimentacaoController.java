package br.gov.jfrj.siga.vraptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class ExMovimentacaoController extends SigaController {

	public ExMovimentacaoController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

}
