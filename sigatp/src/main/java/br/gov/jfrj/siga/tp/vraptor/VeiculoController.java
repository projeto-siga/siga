package br.gov.jfrj.siga.tp.vraptor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.model.CategoriaCNH;
import br.gov.jfrj.siga.tp.model.Grupo;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.LotacaoVeiculo;
import br.gov.jfrj.siga.tp.model.TipoDeCombustivel;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.util.Combo;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.util.Situacao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/veiculos/")
public class VeiculoController extends TpController {

	private static final String TIPO_CADASTRO = "tipoCadastro";
	private static final String LABEL_EDITAR = "views.cadastro.editar";
	private static final String LABEL_INCLUIR = "views.cadastro.incluir";

	/**
	 * @deprecated CDI eyes only
	 */
	public VeiculoController() {
		super();
	}
	
	@Inject
	public VeiculoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar")
	public void listar() throws Exception {
		CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();
		result.include("veiculos", Veiculo.listarTodos(cpOrgaoUsuario));
	}

	@Transactional
	@RoleAdmin
	@RoleAdminFrota
	@Path("/salvar")
	public void salvar(final Veiculo veiculo, DpLotacaoSelecao lotacaoAtualSel) throws Exception {
		DpLotacao lotacaoAtual = obterLotacaoAtual(lotacaoAtualSel);
		validarAntesDeSalvar(veiculo, lotacaoAtual);
		redirecionarSeErroAoSalvar(veiculo, lotacaoAtual);
		veiculo.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
		veiculo.save();

		if (lotacaoDoVeiculoMudou(veiculo, lotacaoAtual)) {
			LotacaoVeiculo.atualizarDataFimLotacaoAnterior(veiculo);
		}
		if (veiculoNaoTemLotacaoCadastrada(veiculo) || lotacaoDoVeiculoMudou(veiculo, lotacaoAtual)) {
			LotacaoVeiculo novalotacao = new LotacaoVeiculo(null, veiculo, lotacaoAtual, Calendar.getInstance(), null, veiculo.getOdometroEmKmAtual());
			novalotacao.save();
		}
		result.redirectTo(this).listar();
	}

	@RoleAdmin
	@RoleAdminFrota
	@Path("/incluir")
	public void incluir() throws Exception {
		result.forwardTo(this).editar(null);
	}

	@RoleAdmin
	@RoleAdminFrota
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		Veiculo veiculo = obterVeiculoParaEdicao(id);
		preencherResultComDadosPadrao(veiculo);
		result.include("veiculo", veiculo);
		result.include("mostrarCampoOdometro", Boolean.FALSE);

	}

	@Transactional
	@RoleAdmin
	@RoleAdminFrota
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(id);
		veiculo.delete();
		result.redirectTo(this).listar();
	}

	@Path("/ler/{id}")
	public void ler(Long id) throws Exception {
		Veiculo veiculo = obterVeiculoParaEdicao(id);
		veiculo.configurarOdometroParaMudancaDeLotacao();
		preencherResultComDadosPadrao(veiculo);
		result.include("esconderBotoes", true);
		result.include("veiculo", veiculo);
	}

	@Path("/{idVeiculo}/avarias")
	public void listarAvarias(Long idVeiculo) throws Exception {
		result.redirectTo(AvariaController.class).listarPorVeiculo(idVeiculo);
	}

	@Path("/buscarPeloId/{id}")
	public void buscarPeloId(Long id) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(id);
		veiculo.configurarOdometroParaMudancaDeLotacao();
		MenuMontador.instance(result).recuperarMenuVeiculos(id, ItemMenu.DADOSCADASTRAIS);
		result.include("veiculo", veiculo);
		result.include("esconderBotoes", true);
		result.redirectTo(this).ler(id);
	}

	private List<DpLotacao> buscarDpLotacoes() {
        HashMap<String, Object> parametros = new HashMap<String, Object>();
		CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();
        parametros.put("orgaoUsuario",  cpOrgaoUsuario);
		List<DpLotacao> dpLotacoes = DpLotacao.AR.find("orgaoUsuario = :orgaoUsuario and DATA_FIM_LOT is null order by NOME_LOTACAO", parametros).fetch();
		return dpLotacoes;
	}

	private boolean veiculoNaoTemLotacaoCadastrada(Veiculo veiculo) {
		return veiculo.getLotacoes() == null || veiculo.getLotacoes().isEmpty();
	}

	private boolean lotacaoDoVeiculoMudou(Veiculo veiculo, DpLotacao lotacaoAtual) {
		if (veiculo.getLotacoes() == null) {
			return true;
		}
		return (!veiculo.getLotacoes().isEmpty()) && (!veiculo.getLotacoes().get(0).getLotacao().equivale(lotacaoAtual));
	}

	private void validarAntesDeSalvar(Veiculo veiculo, DpLotacao lotacaoAtual) {				 
		error(veiculo.getLotacaoAtualSel() == null, "lotacaoAtual", "veiculo.lotacaoAtual.nula.validation"); 
		
		if (veiculoNaoTemLotacaoCadastrada(veiculo) || lotacaoDoVeiculoMudou(veiculo, lotacaoAtual)) {
			final Double odometroAnterior = veiculo.getUltimoOdometroDeLotacao();
			final Double odometroEmKmAtual = Optional.fromNullable(veiculo.getOdometroEmKmAtual()).or(0D);

			error(odometroAnterior > odometroEmKmAtual, "odometroEmKmAtual", "veiculo.odometroEmKmAtual.maiorAnterior.validation");
			error(odometroEmKmAtual.equals(new Double(0)), "odometroEmKmAtual", "veiculo.odometroEmKmAtual.zero.validation");
		}
		validator.validate(veiculo);
	}

	private boolean deveMostrarCampoOdometro(Veiculo veiculo, DpLotacao lotacaoAtual) {
		return veiculoNaoTemLotacaoCadastrada(veiculo) || veiculo.getLotacoes().isEmpty() || (!veiculo.getLotacoes().get(0).getLotacao().equivale(lotacaoAtual));
	}

	private void redirecionarSeErroAoSalvar(Veiculo veiculo, DpLotacao lotacaoAtual) throws Exception {
		if (validator.hasErrors()) {
			preencherResultComDadosPadrao(veiculo);
			result.include("veiculo", veiculo);
			result.include("mostrarCampoOdometro", deveMostrarCampoOdometro(veiculo, lotacaoAtual));

			if (veiculo.ehNovo()) {
				validator.onErrorUse(Results.page()).of(VeiculoController.class).editar(null);
			} else {
				validator.onErrorUse(Results.page()).of(VeiculoController.class).editar(veiculo.getId());
			}
		}
	}

	private void preencherResultComDadosPadrao(Veiculo veiculo) throws Exception {
		Combo.montar(result, Combo.Cor, Combo.Fornecedor);

		labelIncluirOuEditar(veiculo.getId());
		result.include(Combo.Grupo.getDescricao(), Grupo.listarTodos());
		result.include("dpLotacoes", buscarDpLotacoes());
		result.include("situacoes", Situacao.values());
		result.include("respostasSimNao", PerguntaSimNao.values());
		result.include("lotacaoAtualSel", veiculo.getLotacaoAtualSel());
		result.include("tiposDeCombustivel", TipoDeCombustivel.values());
		result.include("categoriasCNH", CategoriaCNH.values());

		MenuMontador.instance(result).recuperarMenuVeiculos(veiculo.getId(), ItemMenu.DADOSCADASTRAIS);
	}

	private Veiculo obterVeiculoParaEdicao(Long id) throws Exception {
		if (id != null) {
			return Veiculo.AR.findById(id);
		}
		return new Veiculo();
	}

	private DpLotacao obterLotacaoAtual(DpLotacaoSelecao lotacaoAtualSel) throws Exception {
		if (lotacaoAtualSel.getId() != null) {
			return DpLotacao.AR.findById(lotacaoAtualSel.getId());
		}
		return null;
	}

	private void labelIncluirOuEditar(Long id) {
		result.include(TIPO_CADASTRO, (id == null || id == 0) ? LABEL_INCLUIR : LABEL_EDITAR);
	}

}
