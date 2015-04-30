package br.gov.jfrj.siga.sr.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.data.validation.Validation;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrTipoAcao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/tipoAcao")
public class TipoAcaoController extends SrController {

	public TipoAcaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrTipoAcao> tiposAcao = SrTipoAcao.listar(mostrarDesativados);

		result.include("tiposAcao", tiposAcao);
		result.include("mostrarDesativados", mostrarDesativados);
	}

	@Path("/listarDesativados")
	public void listarDesativados() throws Exception {
		listar(Boolean.TRUE);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		SrTipoAcao tipoAcao = new SrTipoAcao();
		if (id != null)
			tipoAcao = SrTipoAcao.findById(id);

		result.include("tipoAcao", tipoAcao);
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar/{tipoAcao}")
	public String gravar(SrTipoAcao tipoAcao) throws Exception {
		validarFormEditar(tipoAcao);
		tipoAcao.salvar();

		return tipoAcao.toJson();
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar/{id}/{mostrarDesativados}")
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrTipoAcao tipoAcao = SrTipoAcao.AR.findById(id);
		tipoAcao.finalizar();
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar/{id}/{mostrarDesativados}")
	public Long reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrTipoAcao tipoAcao = SrTipoAcao.findById(id);
		tipoAcao.salvar();

		return tipoAcao.getId();
	}

	@Path("/selecionar/{sigla}")
	public void selecionar(String sigla) throws Exception {
		SrTipoAcao tipoAcao = new SrTipoAcao().selecionar(sigla);

		result.include("sel", tipoAcao);
	}

	@Path("/buscar")
	public void buscar(String propriedade, String sigla) {
 		List<SrTipoAcao> itens = null;
 		SrTipoAcao srTipoAcao = new SrTipoAcao();

		try {
			if (null != sigla && !"".equals(sigla.trim()))
				srTipoAcao.setSigla(sigla);

			itens = srTipoAcao.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrTipoAcao>();
		}

		result.include("itens", itens);
	}

	private void validarFormEditar(SrTipoAcao acao) {
		if ("".equals(acao.getSiglaTipoAcao())) {
			Validation.addError("siglaAcao", "C&oacute;digo n&atilde;o informado");
		}
		if ("".equals(acao.getTituloTipoAcao())) {
			Validation.addError("tituloAcao", "Titulo n&atilde;o informado");
		}
		if (Validation.hasErrors()) {
			enviarErroValidacao();
		}
	}
}
