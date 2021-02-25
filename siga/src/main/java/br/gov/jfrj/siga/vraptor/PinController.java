package br.gov.jfrj.siga.vraptor;



import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.bl.Cp;
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
		
		if (!Cp.getInstance().getComp().podeSegundoFatorPin( getCadastrante(), getLotaCadastrante())) {
			throw new AplicacaoException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
		}
		result.include("baseTeste", Prop.getBool("/siga.base.teste"));
	}
	
	@Get
	@Path("/troca")
	public void troca() throws Exception {	
		if (!Cp.getInstance().getComp().podeSegundoFatorPin( getCadastrante(), getLotaCadastrante())) {
			throw new AplicacaoException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
		}
		result.include("baseTeste", Prop.getBool("/siga.base.teste"));
	}
	
	@Get
	@Path("/reset")
	public void reset() throws Exception {	
		if (!Cp.getInstance().getComp().podeSegundoFatorPin( getCadastrante(), getLotaCadastrante())) {
			throw new AplicacaoException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
		}
		result.include("baseTeste", Prop.getBool("/siga.base.teste"));
	}
			

}
