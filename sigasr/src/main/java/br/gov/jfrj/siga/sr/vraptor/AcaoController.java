package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.mvc.Http;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.dao.CpDao;
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

	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		//TODO    Anderson Liberar acesso
		//assertAcesso(ADM_ADMINISTRAR);
		//List<SrAcao> acoes = SrAcao.listar(mostrarDesativados);
		List<SrAcao> acoes = new ArrayList<>();

		result.include(ACOES, acoes);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		assertAcesso(ADM_ADMINISTRAR);
		SrAcao acao = new SrAcao();
		if (id != null)
			acao = SrAcao.AR.findById(id);

		result.include(ACAO, acao);
	}

	private void validarFormEditarAcao(SrAcao acao) {
		if ("".equals(acao.getSiglaAcao()))
			srValidator.addError("siglaAcao", "Código não informado");

		if ("".equals(acao.getTituloAcao()))
			srValidator.addError("tituloAcao", "Titulo não informado");

		if (srValidator.hasErrors())
			enviarErroValidacao();
	}

	/*
	public void listarAcaoDesativados() throws Exception {
		listarAcao(Boolean.TRUE);
	}

	public String gravarAcao(SrAcao acao) throws Exception {
		assertAcesso(ADM_ADMINISTRAR);
		validarFormEditarAcao(acao);
		acao.salvar();

		// Atualiza os conhecimentos relacionados.
		// Edson: deveria ser feito por webservice. Nao estah sendo coberta
		// a atualizacao da classificacao quando ocorre mudanca de posicao na
		// hierarquia, pois isso eh mais complexo de acertar.

		// Karina: Comentado pois precisa ser refatorado devido ao uso do ConexaoHTTP que está deprecated
//		try {
//			HashMap<String, String> atributos = new HashMap<String, String>();
//			for (Http.Header h : request.headers.values())
//				if (!h.name.equals("content-type"))
//					atributos.put(h.name, h.value());
//
//			SrAcao anterior = acao
//					.getHistoricoAcao().get(0);
//			if (anterior != null
//					&& !acao.tituloAcao
//							.equals(anterior.tituloAcao))
//				ConexaoHTTP.get("http://"
//						+ Play.configuration.getProperty("servidor.principal")
//						+ ":8080/sigagc/app/updateTag?before="
//						+ anterior.getTituloSlugify() + "&after="
//						+ acao.getTituloSlugify(), atributos);
//		} catch (Exception e) {
//			Logger.error("Acao " + acao.idAcao
//					+ " salva, mas nao foi possivel atualizar conhecimento");
//			e.printStackTrace();
//		}
		return acao.toJson();
	}

	public String desativarAcao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso(ADM_ADMINISTRAR);
		SrAcao acao = SrAcao.findById(id);
		acao.finalizar();

		return acao.toJson();
	}

	public String reativarAcao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso(ADM_ADMINISTRAR);
		SrAcao acao = SrAcao.findById(id);
		acao.salvar();
		return acao.toJson();
	}

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
}
