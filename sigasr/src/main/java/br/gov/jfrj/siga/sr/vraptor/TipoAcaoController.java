package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
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

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/listar")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrTipoAcao> tiposAcao = SrTipoAcao.listar(mostrarDesativados);

		result.include("tiposAcao", tiposAcao);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar")
	public void editar(Long id) throws Exception {
		SrTipoAcao tipoAcao = new SrTipoAcao();
		if (id != null)
			tipoAcao = SrTipoAcao.AR.findById(id);

		result.include("tipoAcao", tipoAcao);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrTipoAcao tipoAcao) throws Exception {
		validarFormEditar(tipoAcao);
		if(srValidator.hasErrors()) return;
		tipoAcao.salvar();

		result.use(Results.http()).body(tipoAcao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrTipoAcao tipoAcao = SrTipoAcao.AR.findById(id);
		tipoAcao.finalizar();

		result.use(Results.http()).body(tipoAcao.toJson());
	}

	@AssertAcesso(ADM_ADMINISTRAR)
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
	public void buscar(SrTipoAcao tipoAcao, String nome, String propriedade) {
		List<SrTipoAcao> itens = null;

		SrTipoAcao filtro = null;
		try {
			filtro = (null != tipoAcao) ? tipoAcao : new SrTipoAcao();
			if (temSigla(tipoAcao.getSiglaTipoAcao()))
				filtro.setSigla(tipoAcao.getSiglaTipoAcao());

			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrTipoAcao>();
		}

		result.include("itens", itens);
		result.include("tipoAcao", filtro);
		result.include("nome", nome);
		result.include("param.propriedade", propriedade);
	}

	private boolean temSigla(String sigla) {
		return null != sigla && !"".equals(sigla.trim());
	}

	private void validarFormEditar(SrTipoAcao acao) {
		if ("".equals(acao.getSiglaTipoAcao())) {
			srValidator.addError("tipoAcao.siglaTipoAcao", "C&oacute;digo n&atilde;o informado");
		}
		if ("".equals(acao.getTituloTipoAcao())) {
			srValidator.addError("tipoAcao.tituloTipoAcao", "Titulo n&atilde;o informado");
		}
		if (srValidator.hasErrors()) {
			enviarErroValidacao();
		}
	}
}