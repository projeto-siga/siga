package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPergunta;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrTipoPergunta;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("app/pesquisaSatisfacao")
public class PesquisaSatisfacaoController extends SrController {

	private static final String PESQUISA = "pesquisa";

	/**
	 * @deprecated CDI eyes only
	 */
	public PesquisaSatisfacaoController() {
		super();
	}
	
	@Inject
	public PesquisaSatisfacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar")
	public void listar(boolean mostrarDesativados) {

		List<SrPesquisa> pesquisas = SrPesquisa.listar(mostrarDesativados);
		List<SrTipoPergunta> tipos = SrTipoPergunta.buscarTodos();
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
		List<CpComplexo> locais = CpComplexo.AR.findAll();

		result.include("pesquisas", pesquisas);
		result.include("tipos", tipos);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("mostrarDesativados", mostrarDesativados);

		result.include("pessoa", new DpPessoaSelecao());
		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);

		result.include("itemConfiguracao", new SelecionavelVO(null,null));
		result.include("acao", new SelecionavelVO(null,null));
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id) throws Exception {
		SrPesquisa pesq = SrPesquisa.AR.findById(id);
		pesq.finalizar();

		result.use(Results.http()).body(pesq.toJson());

	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id) throws Exception {
		SrPesquisa pesq = SrPesquisa.AR.findById(id);
		pesq.salvarComHistorico();

		result.use(Results.http()).body(pesq.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar")
	public void editar(Long id) throws Exception {
		SrPesquisa pesquisa = new SrPesquisa();
		if (id != null)
			pesquisa = SrPesquisa.AR.findById(id);

		result.include(PESQUISA, pesquisa);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravarPesquisa(SrPesquisa pesquisa, List<SrPergunta> perguntaSet) throws Exception {
		pesquisa.setPerguntaSet(perguntaSet != null ? perguntaSet : new ArrayList<SrPergunta>());
		pesquisa.salvarComHistorico();

		result.use(Results.http()).body(pesquisa.atualizarTiposPerguntas().toJson());
	}

	@Path("/associacoes")
	public void buscarAssociacaoPesquisa(Long idPesquisa) throws Exception {
		SrPesquisa pesq = SrPesquisa.AR.findById(idPesquisa);

		if (pesq != null) {
			result.use(Results.http()).body(pesq.toJson(true));
		}
		result.use(Results.http()).body("");
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/pesquisas")
	public void listarAssociacaoPesquisa(Long idPesquisa, boolean exibirInativos) throws Exception {
		SrPesquisa pesquisa = new SrPesquisa();
    	if (idPesquisa != null)
    		pesquisa = SrPesquisa.AR.findById(idPesquisa);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesPesquisa(pesquisa, exibirInativos);

        result.use(Results.http()).body(SrConfiguracao.convertToJSon(associacoes));
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativarAssociacaoEdicao")
	public void desativarAssociacaoEdicao(Long idAssociacao) throws Exception {
		SrConfiguracao associacao = SrConfiguracao.AR.em().find(SrConfiguracao.class, idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativarAssociacaoPesquisaEdicao")
	public void desativarAssociacaoPesquisaEdicao(Long idAssociacao) throws Exception {
		SrConfiguracao associacao = SrConfiguracao.AR.em().find(SrConfiguracao.class, idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}

}
