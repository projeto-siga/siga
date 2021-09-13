package br.gov.jfrj.siga.tp.vraptor;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.model.Afastamento;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/afastamento/")
public class AfastamentoController extends TpController {
	
	private static final String AFASTAMENTO = "afastamento";
	private static final String MODO = "modo";
	private static final String LABEL_EDITAR = "views.label.editar";
	private static final String LABEL_INCLUIR = "views.label.incluir";

	/**
	 * @deprecated CDI eyes only
	 */
	public AfastamentoController() {
		super();
	}
	
	@Inject
	public AfastamentoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listarPorCondutor/{idCondutor}")
	public void listarPorCondutor(Long idCondutor) throws Exception {
		Condutor condutor = Condutor.AR.findById(idCondutor);
		List<Afastamento> afastamentos = Afastamento.buscarTodosPorCondutor(condutor);
		MenuMontador.instance(result).recuperarMenuCondutores(idCondutor, ItemMenu.AFASTAMENTOS);
		result.include("afastamentos", afastamentos);
		result.include("condutor", condutor);
	}
	
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluir/{idCondutor}")
	public void incluir(Long idCondutor) throws Exception {
		result.forwardTo(AfastamentoController.class).editar(idCondutor, null);
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/editar/{idCondutor}/{id}")
	public void editar(Long idCondutor, Long id) throws Exception {
		Afastamento afastamento;
		if (id == null || id == 0){
			afastamento = new Afastamento();
			Condutor condutor = Condutor.AR.findById(idCondutor);
			afastamento.setCondutor(condutor);	
			result.include(MODO, LABEL_INCLUIR);
		}else{
			result.include(MODO, LABEL_EDITAR);
			afastamento = Afastamento.AR.findById(id);
		}
		result.include(AFASTAMENTO, afastamento);
	}

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/salvar")
	public void salvar(final Afastamento afastamento) throws Exception {
		validator.validate(afastamento);
		result.include(MODO, null == afastamento.getId() ? LABEL_INCLUIR : LABEL_EDITAR);
		
		if (!validator.hasErrors() && (afastamento.getDataHoraInicio() != null ) && (afastamento.getDataHoraFim() != null) 
				&& (!afastamento.getDescricao().equals(""))&& (!afastamento.ordemDeDatasCorreta())) {
			validator.add(new I18nMessage(AFASTAMENTO, "afastamentos.dataHoraInicio.validation"));
		}
		
		if (validator.hasErrors()) {
			List<Condutor> condutores = Condutor.listarTodos(getTitular().getOrgaoUsuario());
			
			result.include(AFASTAMENTO, afastamento);
			result.include("condutores", condutores);
			validator.onErrorUse(Results.page()).of(AfastamentoController.class).editar(afastamento.getCondutor().getId(), afastamento.getId());
		} else {
			afastamento.setCondutor(Condutor.AR.findById(afastamento.getCondutor().getId()));
			List<Missao> missoes = Missao.retornarMissoes("condutor.id",
					afastamento.getCondutor().getId(),
					afastamento.getCondutor().getCpOrgaoUsuario().getId(),
					afastamento.getDataHoraInicio(), afastamento.getDataHoraFim());
			StringBuilder listaMissoes = new StringBuilder();
			String delimitador = "";

			for (Missao item : missoes) {
				listaMissoes.append(delimitador).append(item.getSequence());
				delimitador = ",";
			}

			if (!missoes.isEmpty()) {
				validator.add(new I18nMessage("LinkErroCondutor", listaMissoes.toString()));
				result.include(AFASTAMENTO, afastamento);
				validator.onErrorUse(Results.page()).of(AfastamentoController.class).editar(afastamento.getCondutor().getId(), afastamento.getId());
			} else {
				afastamento.save();
				result.redirectTo(this).listarPorCondutor(afastamento.getCondutor().getId());
			}
		}
	}

    @Transactional
    @RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		Afastamento afastamento = Afastamento.AR.findById(id);
		afastamento.delete();
		result.redirectTo(this).listarPorCondutor(afastamento.getCondutor().getId());
	}

}
