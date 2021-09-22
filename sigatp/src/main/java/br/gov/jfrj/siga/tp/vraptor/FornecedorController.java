package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
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
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminGabinete;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleGabinete;
import br.gov.jfrj.siga.tp.model.Fornecedor;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Uf;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/fornecedor")
public class FornecedorController extends TpController {
	
	private static final String MODO = "modo";
	private static final String LABEL_EDITAR = "views.label.editar";
	private static final String LABEL_INCLUIR = "views.label.incluir";

	/**
	 * @deprecated CDI eyes only
	 */
	public FornecedorController() {
		super();
	}
	
	@Inject
	public FornecedorController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar")
	public void listar() {
		result.include("fornecedores", getFornecedores());
	}

	@RoleAdmin
	@RoleAdminFrota
	@RoleAdminMissao	
	@RoleAdminGabinete
	@RoleGabinete
	@Path("/incluir")
	public void incluir() throws Exception {
		result.forwardTo(this).editar(null);
	}

	@RoleAdmin
	@RoleAdminFrota
	@RoleAdminMissao
	@RoleAdminGabinete
	@RoleGabinete
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		Fornecedor fornecedor = new Fornecedor();
		
		if(null == id)
			result.include(MODO, LABEL_INCLUIR);
		else {
			fornecedor = Fornecedor.AR.findById(id);
			result.include(MODO, LABEL_EDITAR);
		}
		
		result.include("fornecedor", fornecedor);
		result.include("listaUF", Uf.listarTodos());
	}
	
	@RoleAdmin
	@RoleAdminFrota
	@RoleAdminMissao
	@RoleAdminGabinete
	@RoleGabinete
    @Transactional
	@Path("/salvar")
	public void salvar(@Valid Fornecedor fornecedor) throws Exception {
		if (validator.hasErrors()) {
			
			result.include("listaUF", Uf.listarTodos());
			result.include("fornecedor", fornecedor);
			
			if(fornecedor.getId() == 0){
				result.include(MODO, LABEL_INCLUIR);
				validator.onErrorUse(Results.page()).of(FornecedorController.class).editar(null);
			} else {
				result.include(MODO, LABEL_EDITAR);
				validator.onErrorUse(Results.page()).of(FornecedorController.class).editar(fornecedor.getId());
			}
		} else {
			fornecedor.save();
			result.redirectTo(this).listar();
		}
	}

    @Transactional
	@RoleAdmin
	@RoleAdminFrota
	@RoleAdminMissao
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		Fornecedor fornecedor = Fornecedor.AR.findById(id);
		fornecedor.delete();
		result.redirectTo(this).listar();
	}

	private List<Fornecedor> getFornecedores() {
		try {
			return Fornecedor.listarTodos();
		} catch (Exception ignore) {
			return new ArrayList<Fornecedor>();
		}
	}
}
