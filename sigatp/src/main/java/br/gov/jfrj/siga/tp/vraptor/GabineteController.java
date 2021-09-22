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
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.exceptions.GabineteControllerException;
import br.gov.jfrj.siga.tp.model.Abastecimento;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.Fornecedor;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.TipoDeCombustivel;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/gabinete")
public class GabineteController extends TpController {

    private static final String TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO = "tiposCombustivelParaAbastecimento";
    private static final String FORNECEDORES = "fornecedores";
    private static final String CONDUTORES = "condutores";
    private static final String VEICULO = "veiculo";
    private static final String VEICULOS = "veiculos";
    private static final String ABASTECIMENTOS = "abastecimentos";
    private static final String ABASTECIMENTO = "abastecimento";

	/**
	 * @deprecated CDI eyes only
	 */
	public GabineteController() {
		super();
	}
	
	@Inject
    public GabineteController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/listar")
    public void listar() {
        List<Abastecimento> abastecimentos = Abastecimento.listarTodos();

        result.include(ABASTECIMENTOS, abastecimentos);
    }

    // Verificar se o MenuMontador e realmente utilizado
    @Path("/listarPorVeiculo/{idVeiculo}")
    public void listarPorVeiculo(Long idVeiculo) throws GabineteControllerException {
        try {
            Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
            List<Abastecimento> abastecimentos = Abastecimento.buscarTodosPorVeiculo(veiculo);
            MenuMontador.instance(result).recuperarMenuVeiculos(idVeiculo, ItemMenu.DADOSCADASTRAIS);

            result.include(ABASTECIMENTOS, abastecimentos);
            result.include(VEICULO, veiculo);
        } catch (Exception e) {
            throw new GabineteControllerException(e);
        }
    }

    @RoleAdmin
    @Path("/incluir")
    public void incluir() throws GabineteControllerException {
        try {
            List<Fornecedor> fornecedores = Fornecedor.listarTodos();
            List<Veiculo> veiculos = Veiculo.listarTodos(getOrgaoUsuario());
            List<Condutor> condutores = Condutor.listarTodos(getOrgaoUsuario());
            Abastecimento abastecimento = new Abastecimento();
            abastecimento.setOrgao(getTitular().getOrgaoUsuario());
            abastecimento.setTitular(getTitular());

            result.include(TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO, TipoDeCombustivel.tiposParaAbastecimento());
            result.include(ABASTECIMENTO, abastecimento);
            result.include(VEICULOS, veiculos);
            result.include(CONDUTORES, condutores);
            result.include(FORNECEDORES, fornecedores);
        } catch (Exception e) {
            throw new GabineteControllerException(e);
        }
    }

    @RoleAdmin
    @Path("/editar/{id}")
    public void editar(Long id) throws GabineteControllerException {
        try {
            List<Fornecedor> fornecedores = Fornecedor.listarTodos();
            List<Veiculo> veiculos = Veiculo.listarTodos(getOrgaoUsuario());
            List<Condutor> condutores = Condutor.listarTodos(getOrgaoUsuario());
            Abastecimento abastecimento = Abastecimento.AR.findById(id);

            result.include(TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO, TipoDeCombustivel.tiposParaAbastecimento());
            result.include(ABASTECIMENTO, abastecimento);
            result.include(VEICULOS, veiculos);
            result.include(CONDUTORES, condutores);
            result.include(FORNECEDORES, fornecedores);
        } catch (Exception e) {
            throw new GabineteControllerException(e);
        }
    }

    @Transactional
    @RoleAdmin
    public void salvar(@Valid Abastecimento abastecimento) throws GabineteControllerException {
        try {
            if (validator.hasErrors()) {
                List<Fornecedor> fornecedores = Fornecedor.listarTodos();
                List<Veiculo> veiculos = Veiculo.listarTodos(getOrgaoUsuario());
                List<Condutor> condutores = Condutor.listarTodos(getOrgaoUsuario());

                result.include(TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO, TipoDeCombustivel.tiposParaAbastecimento());
                result.include(ABASTECIMENTO, abastecimento);
                result.include(FORNECEDORES, fornecedores);
                result.include(CONDUTORES, condutores);
                result.include(VEICULOS, veiculos);

                validator.onErrorUse(Results.page()).of(GabineteController.class).editar(abastecimento.getId());
            } else {
                abastecimento.save();
                result.redirectTo(GabineteController.class).listar();
            }
        } catch (Exception e) {
            throw new GabineteControllerException(e);
        }
    }

    @Transactional
    @RoleAdmin
    @Path("/excluir/{id}")
    public void excluir(Long id) throws GabineteControllerException {
        try {
            Abastecimento abastecimento = Abastecimento.AR.findById(id);
            abastecimento.delete();
            result.redirectTo(GabineteController.class).listar();
        } catch (Exception e) {
            throw new GabineteControllerException(e);
        }
    }

    private CpOrgaoUsuario getOrgaoUsuario() {
        return getTitular().getOrgaoUsuario();
    }
}
