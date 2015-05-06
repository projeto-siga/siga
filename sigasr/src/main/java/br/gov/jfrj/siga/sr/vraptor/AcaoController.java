package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/acao")
public class AcaoController extends SrController {

	public AcaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, dao, so, em, srValidator);
	}

	private static final String ACAO = "acao";
	private static final String ACOES = "acoes";

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrAcao> acoes = SrAcao.listar(mostrarDesativados);

		result.include(ACOES, acoes);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	public void listarDesativados() throws Exception {
		listar(Boolean.TRUE);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar")
	public void editar(Long id) throws Exception {
		SrAcao acao = new SrAcao();
		if (id != null)
			acao = SrAcao.AR.findById(id);

		result.include(ACAO, acao);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrAcao acao) throws Exception {
		validarFormEditarAcao(acao);
		acao.salvar();

		result.use(Results.http()).body(acao.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrAcao acao = SrAcao.AR.findById(id);
		acao.finalizar();

		result.use(Results.http()).body(acao.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrAcao acao = SrAcao.AR.findById(id);
		acao.salvar();

		result.use(Results.http()).body(acao.toJson());
	}

	/*
	public void selecionarAcao(String sigla, SrSolicitacao sol)
			throws Exception {

		SrAcao sel = new SrAcao().selecionar(sigla, sol.getAcoesDisponiveis());
		render("@selecionar", sel);
	}

	public void buscarAcao(String sigla, String nome, SrAcao filtro,
			SrSolicitacao sol) {
		List<SrAcao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrAcao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(sol != null
					&& (sol.solicitante != null || sol.local != null) ? sol
					.getAcoesDisponiveis() : null);
		} catch (Exception e) {
			itens = new ArrayList<SrAcao>();
		}

		render(itens, filtro, nome, sol);
	}
	*/

	private void validarFormEditarAcao(SrAcao acao) {
		if ("".equals(acao.getSiglaAcao()))
			srValidator.addError("siglaAcao", "Código não informado");

		if ("".equals(acao.getTituloAcao()))
			srValidator.addError("tituloAcao", "Titulo não informado");

		if (srValidator.hasErrors())
			enviarErroValidacao();
	}
}
