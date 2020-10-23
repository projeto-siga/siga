package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrDisponibilidade;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrTipoDisponibilidade;
import br.gov.jfrj.siga.sr.model.vo.PaginaItemConfiguracao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("app/disponibilidade")
public class DisponibilidadeController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public DisponibilidadeController() {
		super();
	}
	
	@Inject
	public DisponibilidadeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	@Path("/listar")
	public void listar() {
		@SuppressWarnings("unchecked")
		List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();

		result.include("tipoDisponibilidades", SrTipoDisponibilidade.values());
		result.include("orgaos", orgaos);
	}

	@Path("/listarPagina")
	public void listarPagina(PaginaItemConfiguracao pagina) throws Exception {
		@SuppressWarnings("unchecked")
		List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();

		result.use(Results.http()).body(pagina.atualizar(orgaos).toJson());
	}

	@Path("/gravar")
	public void gravar(SrDisponibilidade disponibilidade, PaginaItemConfiguracao pagina, SrItemConfiguracao itemConfiguracao, CpOrgaoUsuario orgao) throws Exception {
		disponibilidade.setItemConfiguracao(itemConfiguracao);
		disponibilidade.setOrgao(orgao);

		disponibilidade.salvar(pagina);
		result.use(Results.http()).body(disponibilidade.toJsonObject().toString());
	}
}
