package br.gov.jfrj.siga.tp.vraptor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.model.Parametro;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/parametro")
public class ParametroController extends TpController {

	/**
	 * @deprecated CDI eyes only
	 */
	public ParametroController() {
		super();
	}
	
	@Inject
	public ParametroController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/listar")
    public void listar() {
        List<Parametro> parametros = Parametro.listarTodos();
        result.include("parametros", parametros);
    }

    @RoleAdmin
    @Path("/editar/{id}")
    public void editar(Long id) {
        Parametro parametro = Parametro.buscar(id);
        carregarDadosPerifericos();
        result.include("parametro", parametro);
    }

    @Transactional
    @RoleAdmin
    @Path("/excluir/{id}")
    public void excluir(Long id) {
        Parametro parametro = Parametro.buscar(id);
        parametro.delete();
        result.redirectTo(ParametroController.class).listar();
    }

    @RoleAdmin
    public void incluir() {
        Parametro parametro = new Parametro();
        carregarDadosPerifericos();
        result.include("parametro", parametro);
    }

    @Transactional
    @RoleAdmin
    public void salvar(@Valid Parametro parametro) {
        validaCamposNulos(parametro);
        if (validator.hasErrors()) {
            carregarDadosPerifericos();
            validator.onErrorUse(Results.page()).of(ParametroController.class).editar(parametro.getId());
            return;
        }
        parametro.save();
        result.redirectTo(ParametroController.class).listar();
    }

    private void validaCamposNulos(Parametro parametro) {
        if (parametro.getCpComplexo().getIdComplexo() == null)
            parametro.setCpComplexo(null);
        if (parametro.getCpOrgaoUsuario().getIdOrgaoUsu() == null)
            parametro.setCpOrgaoUsuario(null);
        if (parametro.getDpPessoa().getId() == null)
            parametro.setDpPessoa(null);
        if (parametro.getDpLotacao().getId() == null)
            parametro.setDpLotacao(null);
    }

    @SuppressWarnings("unchecked")
    private void carregarDadosPerifericos() {
        List<CpOrgaoUsuario> cpOrgaoUsuarios = CpOrgaoUsuario.AR.findAll();
        List<CpComplexo> cpComplexos = CpComplexo.AR.findAll();
        result.include("cpOrgaoUsuarios", cpOrgaoUsuarios);
        result.include("cpComplexos", cpComplexos);
    }
    
    public static String formatarDataParametroParaOracle(String stringCron) {
        String stringData = Parametro.buscarConfigSistemaEmVigor(stringCron);
//        String[] data = stringData.split("/");
//        Calendar cal  = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]));
//        cal.set(Calendar.MONTH, Integer.parseInt(data[1]) - 1);
//        cal.set(Calendar.YEAR, Integer.parseInt(data[2]));
//        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy");
//        String dataInicioPesquisa = formatar.format(cal.getTime());
        return "to_date('" + stringData + "', 'DD/MM/YYYY')";
        //return "to_char('" + dataInicioPesquisa + "', 'DD/MM/YYYY')";
    }

}
