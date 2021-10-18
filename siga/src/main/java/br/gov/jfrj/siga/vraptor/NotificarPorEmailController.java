package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpNotificarPorEmail;
import br.gov.jfrj.siga.dp.NotificarPorEmail;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpNotificarPorEmailDaoFiltro;

@Controller
public class NotificarPorEmailController extends SigaSelecionavelControllerSupport<DpNotificarPorEmail, DpNotificarPorEmailDaoFiltro>{

	/**
	 * @deprecated CDI eyes only
	 */
	public NotificarPorEmailController() {
		super();
	}
	
	@Inject
	public NotificarPorEmailController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	protected boolean temPermissaoParaExportarDados() {
		return Boolean.valueOf(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getTitular().getLotacao(),"SIGA;GI;CAD_CARGO;EXP_DADOS"));
	}
	
	@Override
	protected DpNotificarPorEmailDaoFiltro createDaoFiltro() {
		// TODO Auto-generated method stub
		return null; 
	}
	
	//@Transacional
	@Get({ "/app/notificarPorEmail/rec_notificacao_por_email", "/public/app/page/usuario/rec_notificacao_por_email" })
	public void lista(Integer paramoffset) throws Exception {	
		if(paramoffset == null) {
			paramoffset = 0;
		}
		setItens(CpDao.getInstance().consultarNotificaocaoEmail(paramoffset, 15));
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidadeNotificacaoPorEmail()); 
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.forwardTo("/WEB-INF/page/usuario/notificarPorEmail.jsp"); 
	}
	
	@Transacional
	@Post({ "/app/notificarPorEmail/rec_notificacao_por_email2" })
	public void notificar(final Long id) throws Exception {
		DpNotificarPorEmail email = dao().consultar(id, DpNotificarPorEmail.class, false);
		DpNotificarPorEmail emailNovo = new DpNotificarPorEmail();
		
		email.setId(id);
		
		int configuravel = email.getConfiguravel();
		int naoConfiguravel = email.getNaoConfiguravel();
		
		if (configuravel == 1 && naoConfiguravel == 0) {
			email.setConfiguravel(0);
			email.setNaoConfiguravel(1);
		} else {
			email.setConfiguravel(1);
			email.setNaoConfiguravel(0);
		}
		
		if (configuravel == 0 && naoConfiguravel == 1) {
			email.setConfiguravel(1);
			email.setNaoConfiguravel(0);
		} else {
			email.setConfiguravel(0);
			email.setNaoConfiguravel(1);
		}
		
		result.redirectTo(NotificarPorEmailController.class).lista(1);
	}
	
	
}

