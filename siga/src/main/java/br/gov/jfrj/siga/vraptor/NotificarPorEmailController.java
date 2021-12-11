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
			verificandoAusenciaDeAcoesParaUsuario();
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

	private void verificandoAusenciaDeAcoesParaUsuario() {
		CpConfiguracao alterEmail = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong(), getTitular().getIdPessoa());
		if (alterEmail == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao alterSenha = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getIdLong(), getTitular().getIdPessoa());
		if (alterSenha == null) {
			Integer restringir = 0; 
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao cadUsu = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong(), getTitular().getIdPessoa());
		if (cadUsu == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao conssi = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getIdLong(), getTitular().getIdPessoa());
		if (conssi == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao docMarc = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getIdLong(), getTitular().getIdPessoa());
		if (docMarc == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao docTramiUnidade = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getIdLong(), getTitular().getIdPessoa());
		if (docTramiUnidade == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao docTramiUsu = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getIdLong(), getTitular().getIdPessoa());
		if (docTramiUsu == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao esqSenha = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getIdLong(), getTitular().getIdPessoa());
		if (esqSenha == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao respAssi = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getIdLong(), getTitular().getIdPessoa());
		if (respAssi == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getIdLong());
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao sub = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getIdLong(), getTitular().getIdPessoa());
		if (sub == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getIdLong()); 
			adicionarAcao(servico, restringir);
		}
		CpConfiguracao tramiDocMarca = dao().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.TRAMIT_DOC_MARCADOS.getIdLong(), getTitular().getIdPessoa());
		if (tramiDocMarca == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.TRAMIT_DOC_MARCADOS.getIdLong());
			adicionarAcao(servico, restringir);
		}
	} 
	
	public void adicionarAcao (CpServico servico, Integer restringir) {
		CpConfiguracao config = new CpConfiguracao(); 
		dao().iniciarTransacao();
		config.setCpServico(servico);
		config.setHisDtIni(dao().consultarDataEHoraDoServidor());
		config.setDpPessoa(getTitular());
		config.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.NAO_PODE);
		config.setRestringir(restringir);
		dao().gravarComHistorico(config, getIdentidadeCadastrante());
		dao().commitTransacao();
	}
	
}

