package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.acesso.ConfiguracaoAcesso;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpConfiguracaoNotificarEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpConfiguracaoNotificarEmail;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class ConfiguracaoNotificarPorEmailController extends SigaSelecionavelControllerSupport<DpConfiguracaoNotificarEmail, DaoFiltroSelecionavel>{

	private static final String SIGA_CEMAIL_DOCMARC = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCMARC:Notificações de marcadores destinados a minha unidade";
	private static final String SIGA_CEMAIL_DOCTUN = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCTUN:Documento foi tramitado para a minha unidade";
	private static final String SIGA_CEMAIL_DOCTUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCTUSU:Tramitar para o meu usuário";
	private static final String SIGA_CEMAIL_CONSIG = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;CONSIG:Fui incluído como consignatário de um documento";
	private static final String SIGA_CEMAIL_RESPASSI = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;RESPASSI:Fui incluído como responsável pela assinatura";
	private Long idServico;
	private Long idSituacao;
	private CpTipoDeConfiguracao cpTipoConfiguracaoAConfigurar;
	private List<CpServico> cpServicosDisponiveis;
	
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
		
		setItens(CpDao.getInstance().consultarConfiguracaoNotificarEmailPorUsuario(paramoffset, 15, getTitular().getIdPessoa()));
		
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidadeDeAcoesParaNotificacoesPorEmail()); 
		setItemPagina(15); 
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.include("idServico", this.idServico);
		result.include("idSituacao", this.idSituacao);	 
		result.forwardTo("/WEB-INF/page/usuario/configuracaoNotificarEmail.jsp"); 
	}
	
	@Transacional
	@Post({"app/notificarPorEmail/gravar"})
	public void gravar(Long idServico, Integer idSituacao, DpPessoa pessoa, String servicoPai) throws Exception {
		CpPerfil perfil = null;  
		DpLotacao lotacao = null;
		CpOrgaoUsuario orgao = null;
		CpConfiguracao cpConfiguracao = new CpConfiguracao();
		CpServico servico = dao().consultar(idServico, CpServico.class, false);
		if (pessoa == null && pessoa.getIdPessoa() == null) 
			throw new AplicacaoException(
					"Não foi informada pessoa.");  
		
		CpSituacaoDeConfiguracaoEnum situacao = CpSituacaoDeConfiguracaoEnum.getById(idSituacao);
		CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.UTILIZAR_SERVICO;
		Cp.getInstance().getBL().configurarAcesso(perfil, orgao, lotacao,pessoa, servico, situacao,tpConf, getIdentidadeCadastrante());
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);
	}	
	
	@Transacional
	@Get({ "/app/notificarPorEmail/rec_notificacao_por_email_gravar" })
	public void inicial(Integer paramoffset) throws Exception {
		CpServico servico = null;
		if (!Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_CONSIG)) {
			servico = new CpServico();
			servico = dao().consultarPorSigla(CpConfiguracaoNotificarEmail.CONSIG.getSigla());
			Cp.getInstance().getConf().adicionarServicoEmConfiguracao(servico, getTitular());
		}
		if (!Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_DOCMARC)) {
			servico = new CpServico();
			servico = dao().consultarPorSigla(CpConfiguracaoNotificarEmail.DOCMARC.getSigla());
			Cp.getInstance().getConf().adicionarServicoEmConfiguracao(servico, getTitular());
		}
		if (!Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_DOCTUN)) {
			servico = new CpServico();
			servico = dao().consultarPorSigla(CpConfiguracaoNotificarEmail.DOCTUN.getSigla());
			Cp.getInstance().getConf().adicionarServicoEmConfiguracao(servico, getTitular());
		}
		if (!Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_DOCTUSU)) {
			servico = new CpServico();
			servico = dao().consultarPorSigla(CpConfiguracaoNotificarEmail.DOCTUSU.getSigla());
			Cp.getInstance().getConf().adicionarServicoEmConfiguracao(servico, getTitular());
		}
		if (!Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_RESPASSI)) {
			servico = new CpServico();
			servico = dao().consultarPorSigla(CpConfiguracaoNotificarEmail.RESPASS.getSigla());
			Cp.getInstance().getConf().adicionarServicoEmConfiguracao(servico, getTitular());
		}
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);   
	}
	  
}
