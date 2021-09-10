package br.gov.jfrj.siga.tp.vraptor;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.model.FinalidadeRequisicao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import javax.transaction.Transactional;

@Controller
@Path("/app/finalidade")
public class FinalidadeController extends TpController {
	
	private static final String MODO = "modo";
	private static final String LABEL_EDITAR = "views.label.editar";
	private static final String LABEL_INCLUIR = "views.label.incluir";

	/**
	 * @deprecated CDI eyes only
	 */
	public FinalidadeController() {
		super();
	}
	
	@Inject
	public FinalidadeController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar")
	public void listar(String mensagem) {
    	MenuMontador.instance(result).recuperarMenuFinalidades(true);
    	List<FinalidadeRequisicao> finalidades = FinalidadeRequisicao.listarTodos(getTitular().getOrgaoUsuario());
    	error(null != mensagem, "finalidade", mensagem);
    	
    	result.include("finalidades", finalidades);
    }
	
	public void listar() {
		result.redirectTo(this).listar(null);
	}
	
	@Path("/listarTodas")
	public void listarTodas() {
    	MenuMontador.instance(result).recuperarMenuFinalidades(false);
    	List<FinalidadeRequisicao> finalidades = FinalidadeRequisicao.listarTodos();
   		
    	result.include("finalidades", finalidades);
    }
	
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluir")
	public void incluir() throws Exception {
		result.forwardTo(this).editar(null);
	}
	
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/editar/{id}")
	public void editar(final Long id) throws Exception {
    	FinalidadeRequisicao finalidade = buscar(id);
    	
    	if(isUpdate(finalidade)) {
    		finalidade.checarProprietario(getTitular().getOrgaoUsuario());
    		result.include(MODO, LABEL_EDITAR);
    	} else
    		result.include(MODO, LABEL_INCLUIR);
    	
    	result.include("finalidade", finalidade);
    }
	
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Transactional
	@Path("/salvar")
	public void salvar(FinalidadeRequisicao finalidade) throws Exception {
		
		validator.validate(finalidade);
		
		FinalidadeRequisicao finalidadeBuscada = buscar(finalidade.getId());
		finalidadeBuscada.setDescricao(finalidade.getDescricao());
		
		if(isUpdate(finalidadeBuscada))
			finalidadeBuscada.checarProprietario(getTitular().getOrgaoUsuario());
    	
    	finalidadeBuscada.setCpOrgaoOrigem(getTitular().getOrgaoUsuario());
		
    	if(validator.hasErrors()) {
    		result.include("finalidade", finalidadeBuscada);
    		if(isUpdate(finalidadeBuscada))
    			result.include(MODO, LABEL_EDITAR);
    		else
    			result.include(MODO, LABEL_INCLUIR);
    		
    		validator.onErrorUse(Results.page()).of(FinalidadeController.class).editar(finalidadeBuscada.getId());
		}

	 	finalidadeBuscada.save();
	 	listar();
    }

	private boolean isUpdate(FinalidadeRequisicao finalidade) {
		return null != finalidade.getId() && finalidade.getId() > 0;
	}
	
    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/excluir/{id}")
    public void excluir(final Long id) throws Exception  { 
        FinalidadeRequisicao finalidade = FinalidadeRequisicao.AR.findById(id);
        finalidade.checarProprietario(getTitular().getOrgaoUsuario());
		EntityTransaction tx = FinalidadeRequisicao.AR.em().getTransaction();  
		
		if (! tx.isActive())
			tx.begin();

		try {
		    finalidade.delete();    
			tx.commit();
			
		} catch(PersistenceException ex) {
			tx.rollback();
			if (ex.getCause().getCause().getMessage().contains("o de integridade"))
				result.redirectTo(this).listar("finalidadeRequisicao.vinculada.requisicao");
			else
				result.redirectTo(this).listar(ex.getMessage());
			
		} catch(Exception ex) {
			tx.rollback();
			result.redirectTo(this).listar(ex.getMessage());
		}

		listar();
	}
	
	private FinalidadeRequisicao buscar(final Long id) throws Exception {
		if(null != id && id > 0)
			return FinalidadeRequisicao.AR.findById(id);
		
		return new FinalidadeRequisicao();
	}
}
