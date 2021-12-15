package br.gov.jfrj.siga.vraptor;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpAcoesDeNotificarPorEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpNotificarPorEmail;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpNotificarPorEmailDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class NotificarPorEmailController extends SigaSelecionavelControllerSupport<DpNotificarPorEmail, DaoFiltroSelecionavel>{

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
	protected DaoFiltroSelecionavel createDaoFiltro() {
		return null;
	}
	
	@Transacional
	@Get({ "/app/notificarPorEmail/rec_notificacao_por_email", "/public/app/page/usuario/rec_notificacao_por_email" })
	public void lista(Integer paramoffset) throws Exception {	
		if(paramoffset == null) {
			paramoffset = 0;
		} 
		setItens(CpDao.getInstance().consultarAcoesParaNotificacoesPorEmail(paramoffset, 15, getTitular().getIdPessoa()));
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidadeDeAcoesParaNotificacoesPorEmail()); 
		setItemPagina(15); 
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.forwardTo("/WEB-INF/page/usuario/notificarPorEmail.jsp"); 
	}
	
	@Transacional
	@Get({ "/app/notificarPorEmail/rec_notificacao_por_email_gravar" })
	public void gravar(Integer paramoffset) throws Exception {
		if(paramoffset == null) {
			paramoffset = 0;
		}
		
		/*
		 * Verifica se o usuário possui as 11 ações necessárias. Caso não possua será verificado para adicionar as que faltam.
		 */
		
		List<CpConfiguracao> acoes = dao().consultarAcoesParaNotificacoesPorEmail(paramoffset, 15, getTitular().getIdPessoa());
		if (acoes.size() < 11) {  
			DpNotificarPorEmail notificarPorEmail = new DpNotificarPorEmail();
			notificarPorEmail.verificandoAusenciaDeAcoesParaUsuario(getTitular());
		}
		result.redirectTo(NotificarPorEmailController.class).lista(0);
	}
	
	@Transacional
	@Post({ "/app/notificarPorEmail/rec_notificacao_por_email_atualiza" })
	public void atualiza(final Long codigo) throws Exception {
		CpConfiguracao acao = dao().consultarExistenciaDeAcaoDeNotificacaoPorEmail(codigo, getTitular().getIdPessoa());
		acao.getDpPessoa().setIdPessoa(getTitular().getIdPessoa());
		acao.setIdConfiguracao(codigo);  
		acao.setDpPessoa(getTitular()); 
		boolean ativar = acao.isVerificaSeEstaAtivadoOuDesativadoNotificacaoPorEmail();
		
		if (ativar) {
			acao.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.NAO_PODE);
		} else { 
			acao.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.PODE);
		}  
		
		result.redirectTo(NotificarPorEmailController.class).lista(0);
	}

}

