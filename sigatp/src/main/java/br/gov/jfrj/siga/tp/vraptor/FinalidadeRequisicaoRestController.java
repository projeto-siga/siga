package br.gov.jfrj.siga.tp.vraptor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.exceptions.RestControllerException;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.rest.FinalidadeRequisicaoRest;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/finalidadeRequisicaoRest")
public class FinalidadeRequisicaoRestController extends TpController {

    private static final String FUNCAO_NAO_IMPLEMENTADA = "Funcao nao implementada";

	/**
	 * @deprecated CDI eyes only
	 */
	public FinalidadeRequisicaoRestController() {
		super();
	}
	
	@Inject
    public FinalidadeRequisicaoRestController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/ver/{id}")
    public void ver(Long id) throws RestControllerException {
        try {
            ObjectMapper oM = new ObjectMapper();
            ObjectWriter oW = oM.writer().withDefaultPrettyPrinter();
            String json;

            FinalidadeRequisicaoRest fin = FinalidadeRequisicaoRest.buscarFinalidade(id);
            if (fin != null)
                json = oW.writeValueAsString(fin);
            else {
                FinalidadeRequisicaoRest[] finalidades = FinalidadeRequisicaoRest.buscarFinalidades();
                json = oW.writeValueAsString(finalidades);
            }

            result.use(Results.http()).body(json);
        } catch (Exception e) {
            throw new RestControllerException(e);
        }
    }

    public void incluir() {
        result.use(Results.http()).body(FUNCAO_NAO_IMPLEMENTADA);
    }

    public void alterar() {
        result.use(Results.http()).body(FUNCAO_NAO_IMPLEMENTADA);
    }

    public void excluir() {
        result.use(Results.http()).body(FUNCAO_NAO_IMPLEMENTADA);
    }
}
