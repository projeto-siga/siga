package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.BuscaDocumentoBuilder;

@Controller
public class ExIntegracaoController extends ExController {

	@Inject
	public ExIntegracaoController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em, Validator validator) {

		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	private final static Logger log = Logger.getLogger(ExIntegracaoController.class);

	/**
	 * @deprecated CDI eyes only
	 */
	public ExIntegracaoController() {
		super();
	}


	@Get("/app/expediente/integracao/integracaows")
	public void integracaows(final String sigla) {
		ExDocumento doc = buscarDocumento(sigla);

		result.include("sigla", sigla);
		result.include("mob", doc.getMobilGeral());
	}

	@Transacional
	@Post("/app/expediente/integracao/integrar_gravar")
	public void integrarGravar(final String usuario, final String senha, final String sigla) {
		ExDocumento doc = buscarDocumento(sigla);

		Ex.getInstance().getBL().gravarSiafem(usuario, senha, doc, getCadastrante(), getLotaTitular());

		result.include("origemRedirectIntegrarGravar", true);
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	private ExDocumento buscarDocumento(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia().setSigla(sigla);

		if (builder.getMob() != null)
			verificaNivelAcesso(builder.getMob());

		return builder.buscarDocumento(dao());
	}

}
