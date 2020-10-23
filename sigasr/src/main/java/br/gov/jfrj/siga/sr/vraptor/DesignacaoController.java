package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("app/designacao")
public class DesignacaoController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public DesignacaoController() {
		super();
	}
	
	@Inject
	public DesignacaoController(HttpServletRequest request, Result result,
			SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar")
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

		result.include("atendenteSel", new DpLotacaoSelecao());
		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);
	}
	 
	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.findById(id);
		designacao.finalizar();

		result.use(Results.http()).body(designacao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.em().find(
				SrConfiguracao.class, id);
		designacao.salvarComHistorico();

		result.use(Results.http()).body(designacao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrConfiguracao designacao, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet) throws Exception {
		designacao.setAcoesSet(acoesSet);
		designacao.setItemConfiguracaoSet(itemConfiguracaoSet);
		validarFormEditarDesignacao(designacao);

		if (srValidator.hasErrors())
			return;

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
}
