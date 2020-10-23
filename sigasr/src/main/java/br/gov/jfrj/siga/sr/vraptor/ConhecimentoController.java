package br.gov.jfrj.siga.sr.vraptor;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EDTCONH_CRIAR_CONHECIMENTOS;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;



@Controller
@Path("app/solicitacao/conhecimento")
public class ConhecimentoController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public ConhecimentoController() {
		super();
	}
	
	@Inject
	public ConhecimentoController(HttpServletRequest request, Result result,
			SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	@AssertAcesso(EDTCONH_CRIAR_CONHECIMENTOS)
	@Path("/listar")
	public void listar(boolean ajax, Long idItem, Long idAcao) throws Exception{
		SrItemConfiguracao item = idItem != null ? SrItemConfiguracao.AR.findById(idItem) : new SrItemConfiguracao();
		SrAcao acao = idAcao != null ? SrAcao.AR.findById(idAcao) : new SrAcao();
		result.include("acao", acao);
		result.include("itemConfiguracao", item);
		
		if(ajax) {
			result.include("currentTimeMillis", new Date().getTime());
			result.forwardTo("/WEB-INF/page/conhecimento/listarAjax.jsp");
		}
	}
	
	 public void listarClassificacoes(){
			result.include("acoes", SrAcao.listar(false));
			result.include("itens", SrItemConfiguracao.listar(false));
		}
	
}
