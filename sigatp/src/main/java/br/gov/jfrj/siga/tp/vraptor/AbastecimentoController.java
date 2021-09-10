package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.google.common.collect.Lists;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.annotation.LogMotivo;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminGabinete;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAgente;
import br.gov.jfrj.siga.tp.auth.annotation.RoleGabinete;
import br.gov.jfrj.siga.tp.model.Abastecimento;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.Fornecedor;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.TipoDeCombustivel;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/abastecimento/")
public class AbastecimentoController extends TpController {

    private static final String ABASTECIMENTO = "abastecimento";
    private static final String TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO = "tiposCombustivelParaAbastecimento";
    private static final String CONDUTORES = "condutores";
    private static final String VEICULOS = "veiculos";
    private static final String FORNECEDORES = "fornecedores";

    @Inject
    private AutorizacaoGI autorizacaoGI;

	/**
	 * @deprecated CDI eyes only
	 */
	public AbastecimentoController() {
		super();
	}
	
    @Inject
	public AbastecimentoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/listar")
    public void listar() throws Exception {
        List<Abastecimento> abastecimentos = Lists.newArrayList();
        TipoDeCombustivel combustivel = TipoDeCombustivel.ALCOOL_ADITIVADO;
        TipoDeCombustivel tipoDeCombustivel = TipoDeCombustivel.ALCOOL;

        if (autorizacaoGI.ehGabinete()) {
            Condutor condutor = Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario());
            abastecimentos = Abastecimento.listarAbastecimentosDoCondutor(condutor);
        } else if (autorizacaoGI.ehAdminGabinete())
            abastecimentos = Abastecimento.listarParaAdminGabinete(getTitular());
        else if (autorizacaoGI.ehAgente())
            abastecimentos = Abastecimento.listarParaAgente(getTitular());
        else
            abastecimentos = Abastecimento.listarTodos(getTitular());

		MenuMontador.instance(result).recuperarMenuAbastecimentos();
        Condutor condutor = new Condutor();
        Veiculo veiculo = new Veiculo();
        
        result.include("condutor", condutor);
        result.include("veiculo", veiculo);
        result.include("combustivel", combustivel);
        result.include("tipoDeCombustivel", tipoDeCombustivel);		
        result.include("abastecimentos", abastecimentos);
        montarCombos();
    }

    @Path({"/listarPorVeiculo/{idVeiculo}", "/listarPorVeiculo"})
    public void listarPorVeiculo(Long idVeiculo) throws Exception {
        Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
        List<Abastecimento> abastecimentos = Abastecimento.buscarTodosPorVeiculo(veiculo);
        MenuMontador.instance(result).recuperarMenuVeiculos(idVeiculo, ItemMenu.ABASTECIMENTOS);
        result.include("abastecimentos", abastecimentos);
        result.include("veiculo", veiculo);
    }
    
    @RoleAgente
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path({"/listarAvancado"})
	public void listarAvancado(Condutor condutorEscalado, Veiculo veiculoEscalado, TipoDeCombustivel tipoDeCombustivel, Fornecedor fornecedor) throws Exception {
		condutorEscalado = (condutorEscalado != null && condutorEscalado.getId() != 0) ? Condutor.AR.findById(condutorEscalado.getId()) : null;
		veiculoEscalado = (veiculoEscalado != null && veiculoEscalado.getId() != 0) ? Veiculo.AR.findById(veiculoEscalado.getId()) : null;
		fornecedor = (fornecedor != null && fornecedor.getId() != 0) ? Fornecedor.AR.findById(fornecedor.getId()) : null;
        TipoDeCombustivel combustivel = TipoDeCombustivel.ALCOOL_ADITIVADO;

		List<Abastecimento> abastecimentos = Abastecimento.buscarAbastecimentosAvancado(condutorEscalado, veiculoEscalado, tipoDeCombustivel, fornecedor); 
        MenuMontador.instance(result).RecuperarMenuAbastecimentosAvancado();
        result.include("abastecimentos", abastecimentos);
        result.include("veiculoEscalado", veiculoEscalado);		
        result.include("condutorEscalado", condutorEscalado);		
        result.include("fornecedor", fornecedor);		
        result.include("tipoDeCombustivel", tipoDeCombustivel);		
        result.include("combustivel", combustivel);
        montarCombos();
		result.use(Results.page()).of(AbastecimentoController.class).listar();
	}
	
	protected void montarCombos() throws Exception {
	 	result.include("condutoresEscalados", listarCondutores());
	 	result.include("veiculosEscalados", listarVeiculos());
	 	result.include("fornecedores", listarTodos());
	}

    @RoleAdmin
    @RoleGabinete
    @RoleAdminFrota
    @RoleAdminMissao
    @RoleAdminGabinete
    @RoleAdminMissaoComplexo
    @Path("/incluir")
    public void incluir() throws Exception {
        List<Fornecedor> fornecedores = listarTodos();
        List<Veiculo> veiculos = listarVeiculos();
        List<Condutor> condutores = listarCondutores();

        result.include(ABASTECIMENTO, getAbastecimento());
        result.include(VEICULOS, veiculos);
        result.include(CONDUTORES, condutores);
        result.include(FORNECEDORES, fornecedores);
        result.include(TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO, TipoDeCombustivel.tiposParaAbastecimento());
    }

    private Object getAbastecimento() {
        return null != getRequest().getAttribute(ABASTECIMENTO) ? getRequest().getAttribute(ABASTECIMENTO) : new Abastecimento();
    }

    @Transactional
    @RoleAdmin
    @RoleAdminGabinete
    @RoleAdminMissao
    @RoleAdminMissaoComplexo
    @RoleAdminFrota
    @RoleGabinete
    @Path("/salvar")
    public void salvar(final @Valid Abastecimento abastecimento) throws Exception {
        if (!abastecimento.getId().equals(0L))
            verificarAcesso(abastecimento);
        else
            abastecimento.setOrgao(getTitular().getOrgaoUsuario());

        error(abastecimento.getOdometroEmKm().equals(0.0), "odometroEmKm", "abastecimento.odometroEmKm.validation");
        error(abastecimento.getFornecedor().getId().equals(0L), "fornecedor", "abastecimento.fornecedor.validation");

        if (validator.hasErrors()) {
            List<Fornecedor> fornecedores = Fornecedor.listarTodos();
            List<Veiculo> veiculos = listarVeiculos();
            List<Condutor> condutores = listarCondutores();

            result.include(FORNECEDORES, fornecedores);
            result.include(VEICULOS, veiculos);
            result.include(CONDUTORES, condutores);
            result.include(ABASTECIMENTO, abastecimento);
            validator.onErrorUse(Results.logic()).forwardTo(AbastecimentoController.class).incluir();
        } else {
            abastecimento.setTitular(getTitular());
            abastecimento.setSolicitante(getCadastrante());

            abastecimento.save();

            result.redirectTo(this).listar();
        }
    }

    @RoleAdmin
    @RoleGabinete
    @RoleAdminFrota
    @RoleAdminMissao
    @RoleAdminGabinete
    @RoleAdminMissaoComplexo
    @Path("/editar/{id}")
    public void editar(Long id) throws Exception {
        Abastecimento abastecimento = Abastecimento.AR.findById(id);
        verificarAcesso(abastecimento);

        List<Fornecedor> fornecedores = Fornecedor.listarTodos();
        List<Veiculo> veiculos = listarVeiculos();
        List<Condutor> condutores = listarCondutores();

        result.include(ABASTECIMENTO, abastecimento);
        result.include(VEICULOS, veiculos);
        result.include(CONDUTORES, condutores);
        result.include(FORNECEDORES, fornecedores);
        result.include(TIPOS_COMBUSTIVEL_PARA_ABASTECIMENTO, TipoDeCombustivel.tiposParaAbastecimento());
    }

    @Transactional
    @LogMotivo
    @RoleAdmin
    @RoleAdminGabinete
    @RoleAdminMissaoComplexo
    @RoleAdminFrota
    @RoleGabinete
    @Path("/excluir/{id}")
    public void excluir(Long id) throws Exception {
        Abastecimento abastecimento = Abastecimento.AR.findById(id);
        verificarAcesso(abastecimento);
        abastecimento.delete();
        result.redirectTo(this).listar();
    }

    @SuppressWarnings("unchecked")
    private List<Fornecedor> listarTodos() {
        List<Fornecedor> fornecedores = Fornecedor.AR.findAll();
        Collections.sort(fornecedores);
        return fornecedores;
    }

    private List<Veiculo> listarVeiculos() throws Exception {
        if (!autorizacaoGI.ehAdministrador())
            return Veiculo.listarFiltradoPor(getTitular().getOrgaoUsuario(), getTitular().getLotacao());
        else
            return Veiculo.listarTodos(getTitular().getOrgaoUsuario());
    }

    private List<Condutor> listarCondutores() throws Exception {
        if (autorizacaoGI.ehGabinete()) {
            List<Condutor> retorno = new ArrayList<Condutor>();
            retorno.add(Condutor.recuperarLogado(getTitular(), getTitular().getOrgaoUsuario()));
            return retorno;
        }

        if (!autorizacaoGI.ehAdministrador())
            return Condutor.listarFiltradoPor(getTitular().getOrgaoUsuario(), getTitular().getLotacao());
        else
            return Condutor.listarTodos(getTitular().getOrgaoUsuario());
    }

    private void verificarAcesso(Abastecimento abastecimento) throws Exception {
        if (autorizacaoGI.ehAdminGabinete() || autorizacaoGI.ehGabinete()) {
            if (!(autorizacaoGI.ehAdminGabinete() && getTitular().getLotacao().equivale(abastecimento.getTitular().getLotacao())) && !abastecimento.getTitular().equivale(getTitular())) {
                throw new Exception(MessagesBundle.getMessage("abastecimentos.verificarAcesso.exception"));
            }
        } else if (!((autorizacaoGI.ehAdministrador() || autorizacaoGI.ehAdministradorFrota() || autorizacaoGI.ehAdministradorMissao() || autorizacaoGI.ehAdministradorMissaoPorComplexo()) && getTitular()
                .getLotacao().equivale(abastecimento.getTitular().getLotacao())) && !abastecimento.getTitular().equivale(getTitular())) {

            throw new Exception(MessagesBundle.getMessage("abastecimentos.verificarAcesso.exception"));
        }
    }
}