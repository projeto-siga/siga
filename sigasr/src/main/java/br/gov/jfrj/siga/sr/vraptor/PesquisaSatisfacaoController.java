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
import br.gov.jfrj.siga.sr.model.SrPergunta;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrTipoPergunta;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/pesquisaSatisfacao")
public class PesquisaSatisfacaoController extends SrController {

	public PesquisaSatisfacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
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
	@Path("/gravar")
	public void gravar(SrPesquisa srPesquisa, Set<SrPergunta> perguntaSet) throws Exception {
		SrPesquisa pesquisa = (SrPesquisa) Objeto.getImplementation(srPesquisa);
		pesquisa.setPerguntaSet((pesquisa.getPerguntaSet() != null) ? pesquisa
				.getPerguntaSet() : new HashSet<SrPergunta>());
		pesquisa.salvar();

		result.use(Results.http()).body(pesquisa.toJson());
	}
	
	public void listarDesativados() throws Exception {
		listar(Boolean.TRUE);
	}


}
