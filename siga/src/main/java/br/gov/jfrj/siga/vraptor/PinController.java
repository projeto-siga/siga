package br.gov.jfrj.siga.vraptor;



import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.dp.dao.CpDao;


@Controller
@Path("app/pin")
public class PinController extends SigaController {
	
	//private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Configurações";
	
	/**
	 * @deprecated CDI eyes only
	 */
	public PinController() {
		super();
	}
	
	@Inject
	public PinController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}
		
	@Get
	@Path("/cadastro")
	public void cadastro() throws Exception {	


	}
	
	@Get
	@Path("/troca")
	public void troca() throws Exception {	


	}
	
	@Get
	@Path("/reset")
	public void reset() throws Exception {	


	}
			

}
