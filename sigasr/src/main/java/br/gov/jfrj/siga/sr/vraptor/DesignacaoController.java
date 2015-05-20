package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/designacao")
public class DesignacaoController extends SrController {

	public DesignacaoController(HttpServletRequest request, Result result,
			SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) {
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes(
				mostrarDesativados, null);
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find(
				"hisDtFim is null").fetch();

		result.include("modoExibicao", "designacao");
		result.include("mostrarDesativados", mostrarDesativados);
		result.include("designacoes", designacoes);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);

		result.include("dpPessoaSel", new DpPessoaSelecao());
		result.include("atendenteSel", new DpLotacaoSelecao());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("funcaoConfiancaSel", new DpFuncaoConfiancaSelecao());
		result.include("cargoSel", new DpCargoSelecao());
		result.include("cpGrupoSel", new CpPerfilSelecao());
	}

	@Path("/listar")
	public void listar() {
		result.redirectTo(DesignacaoController.class).listar(Boolean.FALSE);
	}

	@Path("/listarDesativados")
	public void listarDesativados() {
		result.redirectTo(DesignacaoController.class).listar(Boolean.TRUE);
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.findById(id);
		designacao.finalizar();

		result.use(Results.http()).body(designacao.toJson());
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.em().find(
				SrConfiguracao.class, id);
		designacao.salvar();

		result.use(Results.http()).body(designacao.toJson());
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrConfiguracao designacao, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet) throws Exception {
		designacao.setAcoesSet(acoesSet);
		designacao.setItemConfiguracaoSet(itemConfiguracaoSet);
		validarFormEditarDesignacao(designacao);

		if (srValidator.hasErrors())
			return;

		verificaObjetosNulos(designacao);
		designacao.salvarComoDesignacao();

		result.use(Results.http()).body(designacao.toJson());
	}

	private void validarFormEditarDesignacao(SrConfiguracao designacao) {
		if (designacao.getDescrConfiguracao() == null
				|| designacao.getDescrConfiguracao().isEmpty())
			srValidator.addError("designacao.descrConfiguracao",
					"Descrição não informada");

		if (srValidator.hasErrors())
			enviarErroValidacao();
	}

	private void verificaObjetosNulos(SrConfiguracao designacao) {
		if (designacao.getCargo().getId() == null)
			designacao.setCargo(null);

		if (designacao.getCpGrupo().getId() == null)
			designacao.setCpGrupo(null);

		if (designacao.getFuncaoConfianca().getIdFuncao() == null)
			designacao.setFuncaoConfianca(null);

		if (designacao.getLotacao().getId() == null)
			designacao.setLotacao(null);

		if (designacao.getAtendente().getId() == null)
			designacao.setAtendente(null);

		if (designacao.getOrgaoUsuario().getIdOrgaoUsu() == null)
			designacao.setOrgaoUsuario(null);

		if (designacao.getDpPessoa().getId() == null)
			designacao.setDpPessoa(null);
		
		if (designacao.getComplexo().getIdComplexo() == null)
			designacao.setComplexo(null);
	}

}
