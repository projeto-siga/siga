package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/associacao")
public class AssociacaoController extends SrController {

	public AssociacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}
	
	@Path("/desativar")
	public void desativarAssociacao(Long idAssociacao) throws Exception {
//		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = SrConfiguracao.AR.findById(idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}
	
	@Path("/gravar")
	public void gravarAssociacao(SrConfiguracao associacao) throws Exception {
//		assertAcesso("ADM:Administrar");
		associacao.salvarComoAssociacaoAtributo();
//		associacao.AR.refresh();
		result.use(Results.http()).body(associacao.toVO().toJson());
	}
	
	@Path("/gravarComoPesquisa")
	public void gravarAssociacaoPesquisa(SrConfiguracao associacao) throws Exception {
//		assertAcesso("ADM:Administrar");
		associacao.salvarComoAssociacaoPesquisa();
//		associacao.refresh();
		result.use(Results.http()).body(associacao.toVO().toJson());
	}

}
