package br.gov.jfrj.siga.sr.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.sr.dao.SrDao;
import br.gov.jfrj.siga.sr.model.SrTipoAcao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/tipoAcao")
public class TipoAcaoController extends SrController {

	public TipoAcaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, SrDao.getInstance(), so, em, srValidator);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrTipoAcao> tiposAcao = SrTipoAcao.listar(mostrarDesativados);

		result.include("tiposAcao", tiposAcao);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	@Get("/listar")
	public void listar() throws Exception {
		result.redirectTo(TipoAcaoController.class).listar(Boolean.FALSE);
	}

	@Path("/listarDesativados")
	public void listarDesativados() throws Exception {
		result.redirectTo(TipoAcaoController.class).listar(Boolean.TRUE);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar")
	public void editar(Long id) throws Exception {
		SrTipoAcao tipoAcao = new SrTipoAcao();
		if (id != null)
			tipoAcao = SrTipoAcao.AR.findById(id);

		result.include("tipoAcao", tipoAcao);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrTipoAcao tipoAcao) throws Exception {
		validarFormEditar(tipoAcao);
		if(srValidator.hasErrors()) return;
		tipoAcao.salvar();

		result.use(Results.http()).body(tipoAcao.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrTipoAcao tipoAcao = SrTipoAcao.AR.findById(id);
		tipoAcao.finalizar();

		//result.use(Results.json()).from(tipoAcao).serialize();
		result.use(Results.http()).body(tipoAcao.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrTipoAcao tipoAcao = SrTipoAcao.AR.findById(id);
		tipoAcao.salvar();

		result.use(Results.http()).body(tipoAcao.toJson());
	}

	@Path("/selecionar/{sigla}")
	public void selecionar(String sigla) throws Exception {
		SrTipoAcao tipoAcao = new SrTipoAcao().selecionar(sigla);

		result.include("sel", tipoAcao);
	}

	@Path("/buscar")
	public void buscar(String sigla, String nome) {
		List<SrTipoAcao> itens = null;

		SrTipoAcao filtro = null;
		try {
			filtro = new SrTipoAcao();
			if (temSigla(sigla))
				filtro.setSigla(sigla);

			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrTipoAcao>();
		}

		result.include("itens", itens);
		result.include("filtro", filtro);
		result.include("nome", nome);
	}

	private boolean temSigla(String sigla) {
		return null != sigla && !"".equals(sigla.trim());
	}

	private void validarFormEditar(SrTipoAcao acao) {
		if ("".equals(acao.getSiglaTipoAcao())) {
			srValidator.addError("siglaAcao", "C&oacute;digo n&atilde;o informado");
		}
		if ("".equals(acao.getTituloTipoAcao())) {
			srValidator.addError("tituloAcao", "Titulo n&atilde;o informado");
		}
		if (srValidator.hasErrors()) {
			enviarErroValidacao();
		}
	}
}
