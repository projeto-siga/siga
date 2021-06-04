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
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.model.AutoDeInfracao;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.Penalidade;
import br.gov.jfrj.siga.tp.model.TipoDeNotificacao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import javax.transaction.Transactional;

@Controller
@Path("/app/autoDeInfracao")
public class AutoDeInfracaoController extends TpController{

	/**
	 * @deprecated CDI eyes only
	 */
	public AutoDeInfracaoController() {
		super();
	}
	
	@Inject	
	public AutoDeInfracaoController(HttpServletRequest request, Result result,
			 Locale localization,  Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(),  validator, so, em);
	}

	@Path("/listarPorVeiculo/{idVeiculo}")
	public void listarPorVeiculo(Long idVeiculo) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
		List<AutoDeInfracao> autosDeInfracao = AutoDeInfracao
				.buscarAutosDeInfracaoPorVeiculo(veiculo);
		MenuMontador.instance(result).recuperarMenuVeiculos(idVeiculo,
				ItemMenu.INFRACOES);

		result.include("autosDeInfracao", autosDeInfracao);
		result.include("veiculo", veiculo);
	}

	@Path("/listarPorCondutor/{idCondutor}")
	public void listarPorCondutor(Long idCondutor) throws Exception {
		Condutor condutor = Condutor.AR.findById(idCondutor);
		List<AutoDeInfracao> autosDeInfracao = AutoDeInfracao
				.buscarAutosDeInfracaoPorCondutor(condutor);
		MenuMontador.instance(result).recuperarMenuCondutores(idCondutor,
				ItemMenu.INFRACOES);
		result.include("autosDeInfracao", autosDeInfracao);
		result.include("condutor", condutor);
	}

	@Path("/listar")
	public void listar() {
		List<AutoDeInfracao> autosDeInfracao = AutoDeInfracao.listarOrdenado();
		result.include("autosDeInfracao", autosDeInfracao);
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluir/{notificacao}")
	public void incluir(String notificacao) throws Exception {
		result.forwardTo(this).editar(0L, notificacao);
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/editar/{id}")
	public void editar(Long id, String notificacao) throws Exception {
		AutoDeInfracao autoDeInfracao;
		TipoDeNotificacao tipoNotificacao;
		renderVeiculosCondutoresEPenalidades();

		if(id >0) {
			autoDeInfracao = AutoDeInfracao.AR.findById(id);
			tipoNotificacao = autoDeInfracao.getPenalidade() == null ? TipoDeNotificacao.AUTUACAO : TipoDeNotificacao.PENALIDADE;
		} else {
			autoDeInfracao = new AutoDeInfracao();
			tipoNotificacao = TipoDeNotificacao.valueOf(notificacao);
		}

		result.include("autoDeInfracao", autoDeInfracao);
		result.include("tipoNotificacao", tipoNotificacao);
	}

	@Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/salvar")
	public void salvar(@Valid AutoDeInfracao autoDeInfracao) throws Exception {
		TipoDeNotificacao tipoNotificacao = autoDeInfracao.getPenalidade() == null ? TipoDeNotificacao.AUTUACAO : TipoDeNotificacao.PENALIDADE;

 		error(autoDeInfracao.getDataDePagamento() != null && autoDeInfracao.dataPosteriorDataCorrente(autoDeInfracao.getDataDePagamento())
 				, "dataPagamento", "autosDeInfracao.dataDePagamento.validation");

		if (validator.hasErrors()) {
			renderVeiculosCondutoresEPenalidades();

			result.include("autoDeInfracao", autoDeInfracao);
			result.include("tipoNotificacao", tipoNotificacao);

			if(autoDeInfracao.getId()  > 0)
				validator.onErrorUse(Results.page()).of(AutoDeInfracaoController.class).editar(autoDeInfracao.getId(), tipoNotificacao.toString());
			else
				validator.onErrorUse(Results.page()).of(AutoDeInfracaoController.class).editar(null, null);

		} else {
			autoDeInfracao.save();
			result.redirectTo(this).listar();
		}
	}

	@Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		AutoDeInfracao autoDeInfracao = AutoDeInfracao.AR.findById(id);
		autoDeInfracao.delete();

		result.redirectTo(this).listar();
	}

	// Incluido apos OSI17(1. grupo) antes de enviar para a OSI (2.grupo de Migracao) - Joao Luis
	// Incluido metodos abaixo e alterado em metodos acima para chamar o metodo renderVeiculosCondutoresEPenalidades

	private void renderVeiculosCondutoresEPenalidades() throws Exception {
		List<Veiculo> veiculos = Veiculo.listarTodos(getTitular().getOrgaoUsuario());
		List<Condutor> condutores = Condutor.listarTodos(getTitular().getOrgaoUsuario());
    	List<Penalidade> penalidades = Penalidade.listarTodos();
    	result.include("veiculos", veiculos);
    	result.include("condutores", condutores);
    	result.include("penalidades", penalidades);
    }
}