package br.gov.jfrj.siga.sr.vraptor;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPergunta;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrTipoPergunta;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/pesquisaSatisfacao")
public class PesquisaSatisfacaoController extends SrController {
	
	private static final String PESQUISA = "pesquisa";

	public PesquisaSatisfacaoController(HttpServletRequest request,
			Result result, CpDao dao, SigaObjects so, EntityManager em,
			SrValidator srValidator) {
		super(request, result, dao, so, em, srValidator);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {

		List<SrPesquisa> pesquisas = SrPesquisa.listar(mostrarDesativados);
		List<SrTipoPergunta> tipos = SrTipoPergunta.buscarTodos();
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
		List<CpComplexo> locais = CpComplexo.AR.findAll();

		result.include("pesquisas", pesquisas);
		result.include("tipos", tipos);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	public void listarDesativados() throws Exception {
		listar(Boolean.TRUE);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id) throws Exception {
		SrPesquisa pesq = SrPesquisa.AR.findById(id);
		pesq.finalizar();

		result.use(Results.http()).body(pesq.toJson());
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id) throws Exception {
		SrPesquisa pesq = SrPesquisa.AR.findById(id);
		pesq.salvar();

		result.use(Results.http()).body(pesq.toJson());
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar")
	public void editar(Long id) throws Exception {
		SrPesquisa pesquisa = new SrPesquisa();
		if (id != null)
			pesquisa = SrPesquisa.AR.findById(id);

		result.include(PESQUISA, pesquisa);
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravarPesquisa(SrPesquisa pesquisa, Set<SrPergunta> perguntaSet) throws Exception {
		pesquisa = (SrPesquisa) Objeto.getImplementation(pesquisa);
		pesquisa.setPerguntaSet((pesquisa.getPerguntaSet() != null) ? pesquisa.getPerguntaSet() : new HashSet<SrPergunta>());
		pesquisa.salvar();

		result.use(Results.http()).body(pesquisa.atualizarTiposPerguntas().toJson());
	}
	

	// @AssertAcesso(ADM_ADMINISTRAR)
	public void listarAssociacao(Long idPesquisa) throws Exception {
		SrPesquisa pesquisa = new SrPesquisa();

		if (idPesquisa != null)
			pesquisa = SrPesquisa.AR.findById(idPesquisa);

		List<SrConfiguracao> associacoes = SrConfiguracao
				.listarAssociacoesPesquisa(pesquisa, Boolean.FALSE);

		result.use(Results.http()).body(
				SrConfiguracao.convertToJSon(associacoes));
	}

	@Path("/listarAssociacaoDesativados")
	public void listarAssociacaoDesativados(Long idPesquisa) throws Exception {
		SrPesquisa pesquisa = new SrPesquisa();
		if (idPesquisa != null)
			pesquisa = SrPesquisa.AR.findById(idPesquisa);
		List<SrConfiguracao> associacoes = SrConfiguracao
				.listarAssociacoesPesquisa(pesquisa, Boolean.TRUE);

		result.use(Results.http()).body(
				SrConfiguracao.convertToJSon(associacoes));
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativarAssociacaoEdicao")
	public void desativarAssociacaoEdicao(Long idAssociacao) throws Exception {
		SrConfiguracao associacao = SrConfiguracao.AR.em().find(SrConfiguracao.class, idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}

	// @AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativarAssociacaoPesquisaEdicao")
	public void desativarAssociacaoPesquisaEdicao(Long idAssociacao) throws Exception {
		SrConfiguracao associacao = SrConfiguracao.AR.em().find(SrConfiguracao.class, idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}

	@Path("/buscarAssociacao/{idPesquisa}")
	public void buscarAssociacao(Long idPesquisa) throws Exception {
		SrPesquisa pesq = SrPesquisa.AR.findById(idPesquisa);

		if (pesq != null) {
			result.use(Results.http()).body(pesq.toJson(true));
		}
		result.use(Results.http()).body("");
	}

}
