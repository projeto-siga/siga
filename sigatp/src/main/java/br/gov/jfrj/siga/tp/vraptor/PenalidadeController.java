package br.gov.jfrj.siga.tp.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAgente;
import br.gov.jfrj.siga.tp.model.Penalidade;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.CustomJavaExtensions;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@SuppressWarnings("deprecation")
@Resource
@Path("/app/penalidade/")
public class PenalidadeController extends TpController {
	private static final String PENALIDADE_STR = "penalidade";

    public PenalidadeController(HttpServletRequest request, Result result, Validator validator, SigaObjects so, EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar")
	public void listar() {
   		List<Penalidade> penalidades = Penalidade.listarTodos();
   		result.include("penalidades", penalidades);
    }

    @RoleAdmin
    @RoleAdminFrota
	@Path("/editar/{id}")
	public  void editar(Long id) {
		Penalidade penalidade = Penalidade.buscar(id);
		result.include(PENALIDADE_STR, penalidade);
	}

    @RoleAdmin
    @RoleAdminFrota
	@Path("/excluir/{id}")
	public void excluir(Long id) {
        Penalidade penalidade = Penalidade.buscar(id);

		penalidade.delete();
		result.redirectTo(this).listar();
	}

    @RoleAdmin
    @RoleAdminFrota
	@Path("/incluir")
	public void incluir() {
		Penalidade penalidade = new Penalidade();

     	result.include(PENALIDADE_STR,penalidade);
	}

    @RoleAdmin
    @RoleAdminFrota
	@Path("/salvar")
	public void salvar(@Valid Penalidade penalidade) {
    	if(validator.hasErrors()) {
			result.include(PENALIDADE_STR,penalidade);

			if(penalidade.getId() > 0)
			    validator.onErrorUse(Results.page()).of(PenalidadeController.class).editar(penalidade.getId());
			else
			    validator.onErrorUse(Results.page()).of(PenalidadeController.class).incluir();
		}

	 	penalidade.save();

	 	result.redirectTo(this).listar();
    }

	/* Metodo AJAX */
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/listarValorPenalidade")
	public void listarValorPenalidade(Long idPenalidade) throws Exception {
		Penalidade penalidade = Penalidade.AR.findById(idPenalidade);
		String formataMoedaBrasileiraSemSimbolo = CustomJavaExtensions.formataMoedaBrasileiraSemSimbolo(penalidade.getValor());
		result.use(Results.http()).body(formataMoedaBrasileiraSemSimbolo);
	}


	/* Metodo AJAX */
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@RoleAgente
	@Path("/listarClassificacaoPenalidade")
	public void listarClassificacaoPenalidade(Long idPenalidade) throws Exception {
		Penalidade penalidade = Penalidade.AR.findById(idPenalidade);
		result.use(Results.http()).body(penalidade.getClassificacao().getDescricao());
	}
}