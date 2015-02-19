package br.gov.jfrj.siga.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class PrincipalController extends SigaController {
	
	public PrincipalController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/principal")
	public void principal() {
	}
	
	@Get("app/pagina_vazia")
	public void paginaVazia() {
	}
	
	@Get("app/usuario_autenticado")
	public void usuarioAutenticado() {
	}
}