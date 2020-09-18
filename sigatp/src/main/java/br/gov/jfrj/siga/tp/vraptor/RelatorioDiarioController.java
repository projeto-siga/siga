package br.gov.jfrj.siga.tp.vraptor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.RelatorioDiario;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/relatorioDiario/")
public class RelatorioDiarioController extends TpController {
	
	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioDiarioController() {
		super();
	}
	
	@Inject
	public RelatorioDiarioController(HttpServletRequest request, Result result, Locale localization, 
			 Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar/{idVeiculo}")
	public void listarPorVeiculo(Long idVeiculo) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
		List<RelatorioDiario> relatoriosDiarios = RelatorioDiario.buscarTodosPorVeiculo(veiculo);
		MenuMontador.instance(result).recuperarMenuVeiculos(veiculo.getId(), ItemMenu.RELATORIOSDIARIOS);
		result.include("relatoriosDiarios", relatoriosDiarios);
		result.include("veiculo", veiculo);
		result.include("idVeiculo", idVeiculo);
	}
	
	@RoleAdmin
	@RoleAdminFrota
	@Path("/incluir/{idVeiculo}")
	public void incluir(Long idVeiculo) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
		RelatorioDiario relatorioDiario = new RelatorioDiario();
		relatorioDiario.setVeiculo(veiculo);
		result.include("relatorioDiario", relatorioDiario);
		result.forwardTo(this).editar(null);
	}
	
	@RoleAdmin
	@RoleAdminFrota
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		result.include("tipoCadastro", id != null?MessagesBundle.getMessage("views.relatorioDiario.editar")
				 								 :MessagesBundle.getMessage("views.relatorioDiario.incluir"));		
		if (id!=null){
			RelatorioDiario relatorioDiario = RelatorioDiario.AR.findById(id);
			result.include("relatorioDiario", relatorioDiario);
		}
	}
	
	@RoleAdmin
	@RoleAdminFrota
	@Path("/salvar")
	public void salvar(@Valid RelatorioDiario relatorioDiario) throws Exception {
		redirecionarSeErroAoSalvar(relatorioDiario);
		
		relatorioDiario.save();
		result.redirectTo(this).listarPorVeiculo(relatorioDiario.getVeiculo().getId());
	}
	
	@RoleAdmin
	@RoleAdminFrota
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		RelatorioDiario relatorioDiario = RelatorioDiario.AR.findById(id);
		relatorioDiario.delete();
		result.redirectTo(this).listarPorVeiculo(relatorioDiario.getVeiculo().getId());
	}
	
	private void redirecionarSeErroAoSalvar(RelatorioDiario relatorioDiario) throws Exception {
		if (validator.hasErrors()) {
			List<Veiculo> veiculos = Veiculo.listarTodos(getTitular().getOrgaoUsuario());
			result.include("relatorioDiario", relatorioDiario).include("veiculo", veiculos);
			
			if (relatorioDiario.getId().compareTo(0L) > 0) {
				validator.onErrorUse(Results.page()).of(RelatorioDiarioController.class).editar(relatorioDiario.getId());
			}
			else {
				validator.onErrorUse(Results.page()).of(RelatorioDiarioController.class).editar(relatorioDiario.getId());
			}
		}
	}
}