package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.ArrayList;
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
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.TipoAcaoSelecao;
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

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/listar")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrAcao> acoes = SrAcao.listar(mostrarDesativados);

		result.include(ACOES, acoes);
		result.include("tipoAcaoSel", new TipoAcaoSelecao());
		result.include("mostrarDesativados", mostrarDesativados);
	}

	@Path("/listarDesativados")
	public void listarDesativados() throws Exception {
		result.redirectTo(AcaoController.class).listar(Boolean.TRUE);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar")
	public void editar(Long id) throws Exception {
		SrAcao acao = new SrAcao();
		if (id != null)
			acao = SrAcao.AR.findById(id);

		result.include(ACAO, acao);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrAcao acao, TipoAcaoSelecao tipoAcaoSel) throws Exception {
		validarFormEditarAcao(acao);
		acao.setTipoAcao(tipoAcaoSel.buscarObjeto());
		acao.salvar();

		result.use(Results.http()).body(acao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrAcao acao = SrAcao.AR.findById(id);
		acao.finalizar();

		result.use(Results.http()).body(acao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrAcao acao = SrAcao.AR.findById(id);
		acao.salvar();

		result.use(Results.http()).body(acao.toJson());
	}

	@Path("/selecionar")
	public void selecionar(String sigla, SrSolicitacao sol)throws Exception {
		SrAcao acao = new SrAcao().selecionar(sigla, sol.getAcoesDisponiveis());
		result
			.forwardTo(SelecaoController.class)
			.ajaxRetorno(acao);
	}

	@Path("/buscar")
	public void buscar(String sigla, String nome, String siglaAcao, String tituloAcao, SrSolicitacao sol, String propriedade) {
		List<SrAcao> items = null;
		
		SrAcao filtro = new SrAcao();
		filtro.setSiglaAcao(siglaAcao);
		filtro.setTituloAcao(tituloAcao);

		try {
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			
			items = filtro.buscar(sol != null
					&& (sol.getSolicitante() != null || sol.getLocal() != null) ? sol
					.getAcoesDisponiveis() : null);
			
		} catch (Exception e) {
			items = new ArrayList<SrAcao>();
		}

		result.include("items", items);
		result.include("sol", sol);
		result.include("filtro", filtro);
		result.include("nome", nome);
		result.include("propriedade", propriedade);
	}

	private void validarFormEditarAcao(SrAcao acao) {
		if ("".equals(acao.getSiglaAcao()))
			srValidator.addError("siglaAcao", "Código não informado");

		if ("".equals(acao.getTituloAcao()))
			srValidator.addError("tituloAcao", "Titulo não informado");

		if (srValidator.hasErrors())
			enviarErroValidacao();
	}
}
