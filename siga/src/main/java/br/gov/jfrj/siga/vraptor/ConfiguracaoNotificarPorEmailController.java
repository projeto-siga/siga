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
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpAcoesDeNotificarPorEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.DpConfiguracaoNotificarPorEmail;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class ConfiguracaoNotificarPorEmailController 
	extends SigaSelecionavelControllerSupport<DpConfiguracaoNotificarPorEmail, DaoFiltroSelecionavel> {

	private static final String SIGA_CEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email";
	private static final String SIGA_CEMAIL_ALTEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;" +CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getSigla()+ ":"+CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getDescricao();
	private static final String SIGA_CEMAIL_ALTSENHA = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getSigla()+":"+CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getDescricao();
	private static final String SIGA_CEMAIL_CADUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getSigla()+":"+CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getDescricao();
	private static final String SIGA_CEMAIL_CONSSIG = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getSigla()+":"+CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getDescricao();
	private static final String SIGA_CEMAIL_DOCMARC = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getSigla()+":"+CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getDescricao();
	private static final String SIGA_CEMAIL_DOCTUN = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getSigla()+":"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getDescricao();
	private static final String SIGA_CEMAIL_DOCTUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getSigla()+":"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getDescricao();
	private static final String SIGA_CEMAIL_ESQSENHA = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getSigla()+":"+CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getDescricao();
	private static final String SIGA_CEMAIL_RESPASSI = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getSigla()+":"+CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getDescricao();
	private static final String SIGA_CEMAIL_SUB = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getSigla()+":"+CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getDescricao();
	
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
		boolean ativar = acao.enviarNotificao();
		
		if (ativar) {
			acao.setReceberEmail(0);
		} else { 
			acao.setReceberEmail(1);
		}  
		
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);
	}
	
}
