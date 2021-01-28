package br.gov.jfrj.siga.sr.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
public class TestesController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public TestesController() {
		super();
	}
	
	@Inject
	public TestesController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) throws Throwable {
		super(request, result, dao, so, em, srValidator);
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@Get("/public/app/testes/gadgetTest")
	public void test(final String matricula) throws Exception {
		if (matricula == null) {
			result.use(Results.http())
					.body("ERRO: É necessário especificar o parâmetro 'matricula'.")
					.setStatusCode(400);
			return;
		}

		final DpPessoa pes = daoPes(matricula);

		if (pes == null) {
			result.use(Results.http())
					.body("ERRO: Não foi localizada a pessoa referenciada pelo parâmetro 'matricula'.")
					.setStatusCode(400);
			return;
		}

		setTitular(pes);
		setLotaTitular(pes.getLotacao());
		result.redirectTo(SolicitacaoController.class).gadget();
	}
}
