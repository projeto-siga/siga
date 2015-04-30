package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/acao")
public class AcaoController extends SrController {

	private static final String ACAO = "acao";
	private static final String ACOES = "acoes";

	public AcaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrAcao> acoes = SrAcao.listar(mostrarDesativados);

		result.include(ACOES, acoes);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		SrAcao acao = new SrAcao();
		if (id != null)
			acao = SrAcao.AR.findById(id);

		result.include(ACAO, acao);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	public String gravarAcao(SrAcao acao) throws Exception {
		validarFormEditarAcao(acao);
		acao.salvar();

		return acao.toJson();
	}

	public void listarDesativados() throws Exception {
		listar(Boolean.TRUE);
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

	public String desativarAcao(Long id, boolean mostrarDesativados) throws Exception {
		//assertAcesso(ADM_ADMINISTRAR);
		SrAcao acao = SrAcao.findById(id);
		acao.finalizar();

		return acao.toJson();
	}

	public String reativarAcao(Long id, boolean mostrarDesativados) throws Exception {
		//assertAcesso(ADM_ADMINISTRAR);
		SrAcao acao = SrAcao.findById(id);
		acao.salvar();
		return acao.toJson();
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
