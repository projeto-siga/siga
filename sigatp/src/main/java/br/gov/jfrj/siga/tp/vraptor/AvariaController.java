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
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.model.Avaria;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/avaria")
public class AvariaController extends TpController {
	
	private static final String MODO = "modo";
	private static final String LABEL_EDITAR = "views.label.editar";
	private static final String LABEL_INCLUIR = "views.label.incluir";

	/**
	 * @deprecated CDI eyes only
	 */
	public AvariaController() {
		super();
	}
	
	@Inject	
	public AvariaController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so, EntityManager em) throws Exception {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar")
	public void listar() {
		List<Avaria> avarias = Avaria.listarTodos();
		result.include("avarias", avarias);
	}
	
	@Path("/listarPorVeiculo/{idVeiculo}")
	public void listarPorVeiculo(Long idVeiculo) throws Exception {
		montarListaDeAvariasPorVeiculo(idVeiculo);
	}

	protected void montarListaDeAvariasPorVeiculo(Long idVeiculo) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
		result.include("veiculo", veiculo);
		result.include("avarias", Avaria.buscarTodasPorVeiculo(veiculo));
		MenuMontador.instance(result).recuperarMenuVeiculos(idVeiculo, ItemMenu.AVARIAS);
	}

	@RoleAdmin
	@RoleAdminFrota
	@Path("/incluir/{idVeiculo}")
	public void incluir(Long idVeiculo) throws Exception {
		result.forwardTo(this).editar(idVeiculo, null, false);
	}
	
	@RoleAdmin
	@RoleAdminFrota
	@Path("/incluir")
	public void incluir() throws Exception {
		result.forwardTo(this).editar(null, null, false);
	}

	@RoleAdmin
	@RoleAdminFrota
	@Path("/editar/{idVeiculo}/{id}/{fixarVeiculo}")
	public void editar(final Long idVeiculo, final Long id, final Boolean fixarVeiculo) throws Exception {
		Avaria avaria = new Avaria();
		Veiculo veiculo = new Veiculo();
		
		if(null != id) {
			avaria = Avaria.AR.findById(id);
			if (fixarVeiculo)
				veiculo = avaria.getVeiculo();
			
			result.include("veiculo", veiculo);
			result.include("fixarVeiculo", fixarVeiculo);
			
		} else {
			if (idVeiculo != null) {
				avaria.setVeiculo(Veiculo.AR.findById(idVeiculo));
				result.include("fixarVeiculo", true);
				MenuMontador.instance(result).recuperarMenuVeiculos(avaria.getVeiculo().getId(), ItemMenu.AVARIAS);
			} else
				result.include("fixarVeiculo", false);
		}
		
		List<Veiculo> veiculos = Veiculo.listarTodos(getTitular().getOrgaoUsuario());
		
		resultIncluirOuEditar(avaria);
		result.include("avaria", avaria);
		result.include("veiculos", veiculos);
	}

    @Transactional
	@RoleAdmin
	@RoleAdminFrota
	@Path("/salvar")
	public void salvar(@Valid Avaria avaria, boolean fixarVeiculo) throws Exception {
		
		if (validator.hasErrors()) {
			MenuMontador.instance(result).recuperarMenuVeiculos(avaria.getVeiculo().getId(), ItemMenu.AVARIAS);
			List<Veiculo> veiculos = Veiculo.listarTodos(getTitular().getOrgaoUsuario());
			Veiculo veiculo = Veiculo.AR.findById(avaria.getVeiculo().getId());

			resultIncluirOuEditar(avaria);
			
			result.include("avaria", avaria);
			result.include("veiculos", veiculos);
			result.include("veiculo", veiculo);
			result.include("fixarVeiculo", fixarVeiculo);
			
			validator.onErrorUse(Results.page()).of(AvariaController.class).editar(veiculo.getId(), avaria.getId(), true);
		}

		if (avaria.getPodeCircular().equals(PerguntaSimNao.NAO)) {
			avaria.setVeiculo(Veiculo.AR.findById(avaria.getVeiculo().getId()));
			List<Missao> missoes = Missao.retornarMissoes("veiculo.id", avaria.getVeiculo().getId(), avaria.getVeiculo().getCpOrgaoUsuario().getId(), avaria.getDataDeRegistro(), avaria.getDataDeSolucao());
			StringBuilder listaMissoes = new StringBuilder();
			String delimitador = "";

			for (Missao item : missoes) {
				listaMissoes.append(delimitador).append(item.getSequence());
				delimitador = ",";
			}
			
			error(!missoes.isEmpty(), "LinkErroVeiculo", listaMissoes.toString());
		}

		if (validator.hasErrors()) {
			result.include("avaria", avaria);
			resultIncluirOuEditar(avaria);
			validator.onErrorUse(Results.page()).of(AvariaController.class).editar(avaria.getVeiculo().getId(), avaria.getId(), true);
		} else {
			avaria.save();
			if (fixarVeiculo)
				result.redirectTo(this).listarPorVeiculo(avaria.getVeiculo().getId());
			else
				result.redirectTo(this).listar();
		}
	}

	private void resultIncluirOuEditar(Avaria avaria) {
		if(avaria.getId() == 0)
			result.include(MODO, LABEL_INCLUIR);
		else
			result.include(MODO, LABEL_EDITAR);
	}

    @Transactional
	@RoleAdmin
	@RoleAdminFrota
	@Path("/excluir/{id}/{fixarVeiculo}")
	public void excluir(Long id, boolean fixarVeiculo) throws Exception {
		Avaria avaria;
		avaria = Avaria.AR.findById(id);
		Veiculo veiculo = avaria.getVeiculo();
		avaria.delete();
		if (fixarVeiculo) {
			result.redirectTo(this).listarPorVeiculo(veiculo.getId());
		} else {
			result.redirectTo(this).listar();
		}
	}
}