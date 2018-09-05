package br.gov.jfrj.siga.tp.vraptor;

import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/agenda")
public class AgendaController extends TpController {

    public static final Long NOVOVEICULO = null;
    public static final String ACTION_EDITAR = "Veiculos/Editar";

    public AgendaController(HttpServletRequest request, Result result, CpDao dao, Validator validator, SigaObjects so, EntityManager em) {
        super(request, result, dao, validator, so, em);

    }

    @Path("/listar")
    public void listar() {
        result.redirectTo(this).emDesenvolvimento();
    }

    @Path("/listarPorCondutor/{idCondutor}")
    public void listarPorCondutor(Long idCondutor) throws ParseException {
        result.forwardTo(RelatorioController.class).listarAgendaPorCondutor(idCondutor, RelatorioController.getToday());
    }

    @Path("/listarPorVeiculo/{idVeiculo}")
    public void listarPorVeiculo(Long idVeiculo) throws ParseException {
        result.forwardTo(RelatorioController.class).listarAgendaPorVeiculo(idVeiculo, RelatorioController.getToday());
    }

    public void emDesenvolvimento() {
        /*
         * Metodo criado apenas para o VRaptor renderizar a pagina
         */
    }

}
