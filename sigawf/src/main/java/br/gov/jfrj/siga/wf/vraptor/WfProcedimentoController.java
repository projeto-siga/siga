package br.gov.jfrj.siga.wf.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.GenericoSelecao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.SigaSelecionavelControllerSupport;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfDefinicaoDeProcedimentoDaoFiltro;
import br.gov.jfrj.siga.wf.util.WfProcedimentoDaoFiltro;

@Controller
public class WfProcedimentoController extends WfSelecionavelController<WfProcedimento, WfProcedimentoDaoFiltro> {
	/**
	 * @deprecated CDI eyes only
	 */
	public WfProcedimentoController() {
		super();
	}

	@Inject
	public WfProcedimentoController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so,
			EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Override
	protected WfProcedimentoDaoFiltro createDaoFiltro() {
		WfProcedimentoDaoFiltro flt = new WfProcedimentoDaoFiltro();
		if (flt.ouDefault == null) {
			if (param("matricula") != null) {
				final DpPessoa pes = daoPes(param("matricula"));
				flt.ouDefault = pes.getOrgaoUsuario();
			}
		}
		return flt;
	}

	@Get({ "public/app/procedimento/selecionar", "app/procedimento/selecionar" })
	public void selecionar(final String sigla, final String matricula) throws Exception {
		String resultado = super.aSelecionar(sigla);
		if (getSel() != null && matricula != null) {
			GenericoSelecao sel = new GenericoSelecao();
			sel.setId(getSel().getId());
			sel.setSigla(getSel().getSigla());
			sel.setDescricao("/sigawf/app/procedimento/" + sel.getId());
			setSel(sel);
		}
		if (resultado.equals("ajax_retorno")) {
			result.use(Results.http())
					.body("1;" + getSel().getId() + ";" + getSel().getSigla() + ";" + getSel().getDescricao());
		} else {
			result.use(Results.http()).body("0");
		}
	}

}
