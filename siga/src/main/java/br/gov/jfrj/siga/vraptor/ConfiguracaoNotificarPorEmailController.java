package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpConfiguracaoNotificarPorEmail;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class ConfiguracaoNotificarPorEmailController 
	extends SigaSelecionavelControllerSupport<DpConfiguracaoNotificarPorEmail, DaoFiltroSelecionavel> {

	/**
	 * @deprecated CDI eyes only
	 */
	public ConfiguracaoNotificarPorEmailController() {
		super();
	}
	
	@Inject
	public ConfiguracaoNotificarPorEmailController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
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
		result.forwardTo("/WEB-INF/page/usuario/configuracaoNotificacaoPorEmail.jsp"); 
	}
	
	@Transacional
	@Get({ "/app/configuracaoNotificacaoPorEmail/rec_notificacao_por_email_gravar" })
	public void gravar(Integer paramoffset) throws Exception {
		if(paramoffset == null) {
			paramoffset = 0;   
		} 
		DpConfiguracaoNotificarPorEmail configuracaoEmail = new DpConfiguracaoNotificarPorEmail();
		configuracaoEmail.adicionarConfiguracao(getCadastrante(), getLotaCadastrante());
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);
	}
	 
	@Transacional
	@Post({ "/app/notificarPorEmail/rec_notificacao_por_email_atualiza" })
	public void atualiza(final Long codigo) throws Exception {
		CpConfiguracao acao = dao().consultarExistenciaDeAcaoDeNotificacaoPorEmail(codigo, getTitular().getIdPessoa());
		acao.getDpPessoa().setIdPessoa(getTitular().getIdPessoa());
		acao.setIdConfiguracao(codigo);  
		acao.setDpPessoa(getTitular()); 
		boolean ativar = acao.enviarNotificacao();
		
		if (ativar) {
			acao.setReceberEmail(0);
		} else { 
			acao.setReceberEmail(1);
		}  
		
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);
	}
	
}
