package br.gov.jfrj.siga.sr.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrSelecionavel;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/selecao")
public class SelecaoController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public SelecaoController() {
		super();
	}
	
	@Inject
	public SelecaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	@Path("/ajaxRetorno")
	public void ajaxRetorno(SrSelecionavel selecionavel) {
		result.include("sel", selecionavel);

		if (selecionavel == null) {
			result.forwardTo(this).ajaxVazio();
		}
	}

	@Path("/ajaxVazio")
	public void ajaxVazio() {
	}
}