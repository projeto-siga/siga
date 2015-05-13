package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.dao.SrDao;
import br.gov.jfrj.siga.sr.model.SrDisponibilidade;
import br.gov.jfrj.siga.sr.model.SrTipoDisponibilidade;
import br.gov.jfrj.siga.sr.model.vo.PaginaItemConfiguracao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/disponibilidade")
public class DisponibilidadeController extends SrController {

	public DisponibilidadeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, SrDao.getInstance(), so, em, srValidator);
	}

	@Path("/listar")
	public void listar() {
		@SuppressWarnings("unchecked")
		List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();

		result.include("tipoDisponibilidades", SrTipoDisponibilidade.values());
		result.include("orgaos", orgaos);
	}

	@Path("/listarPagina")
	public void listarPagina(PaginaItemConfiguracao pagina) {

		@SuppressWarnings("unchecked")
		List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();

		result.use(Results.http()).body(pagina.atualizar(orgaos).toJson());
	}

	@Path("/gravar")
	public String gravar(SrDisponibilidade disponibilidade, PaginaItemConfiguracao pagina) throws Exception {
		disponibilidade.salvar(pagina);
		return disponibilidade.toJsonObject().toString();
	}
}
