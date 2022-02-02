package br.gov.jfrj.siga.vraptor;

import java.util.Date;
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
import br.gov.jfrj.siga.cp.model.enm.CpConfiguracaoNotificarEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class ConfiguracaoNotificarPorEmailController extends GiControllerSupport{

	private Integer totalDeServicos = 9;
	private static final String SIGA_CEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email";
	private static final String SIGA_CEMAIL_DOCMARC = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCMARC:Notificações de marcadores destinados a minha unidade";
	private static final String SIGA_CEMAIL_DOCTUN = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCTUN:Documento foi tramitado para a minha unidade";
	private static final String SIGA_CEMAIL_DOCTUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCTUSU:Tramitar para o meu usuário";
	private static final String SIGA_CEMAIL_COSSIG = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;COSSIG:Fui incluído como consignatário de um documento";
	private static final String SIGA_CEMAIL_RESPASSI = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;RESPASSI:Fui incluído como responsável pela assinatura";
	
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
	
	@Transacional
	@Get({ "/app/notificarPorEmail/listar", "/public/app/page/usuario/listar" })
	public void lista(Integer paramoffset) throws Exception {	
		if(paramoffset == null) { 
			paramoffset = 0;
		} 
		result.include("itens", CpDao.getInstance().consultarConfiguracaoNotificarEmailPorUsuario(paramoffset, 15, getTitular().getIdPessoa()));
		result.include("quantidade", CpDao.getInstance().consultarConfiguracaoNotificarEmailPorUsuario(paramoffset, 15, getTitular().getIdPessoa()).size());
		result.forwardTo("/WEB-INF/page/usuario/configuracaoNotificarEmail.jsp"); 
	}
	
	@SuppressWarnings("all")
	@Transacional
	@Post({"/app/notificarPorEmail/editar"})
	public void editar(Long id, Integer idSituacao) throws Exception {
		CpConfiguracao cpConfiguracao = new CpConfiguracao(); 
		cpConfiguracao = dao().consultar(id, CpConfiguracao.class, false);
		CpSituacaoDeConfiguracaoEnum situacao = CpSituacaoDeConfiguracaoEnum.getById(idSituacao);
		CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.UTILIZAR_SERVICO;
		Cp.getInstance().getBL().configurarAcesso(null, getTitular().getOrgaoUsuario(), getLotaTitular(),getTitular(), cpConfiguracao.getCpServico(), situacao,tpConf, getIdentidadeCadastrante());
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);
	}  
	
	@SuppressWarnings("all")
	@Transacional
	@Post({"app/notificarPorEmail/gravar"})  
	public void gravar(CpServico servico) {
		CpConfiguracao config = new CpConfiguracao(); 
		Date dt = dao().consultarDataEHoraDoServidor();
		CpDao.getInstance().iniciarTransacao();
		config.setCpServico(servico);
		config.setDpPessoa(getTitular());
		config.setLotacao(getLotaTitular());
		config.setOrgaoUsuario(getTitular().getOrgaoUsuario());
		config.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.NAO_PODE); 
		config.setCpTipoConfiguracao(CpTipoDeConfiguracao.UTILIZAR_SERVICO);
		config.setHisIdcIni(getIdentidadeCadastrante());
		config.setHisDtIni(dt);
		CpDao.getInstance().gravar(config);
		CpDao.getInstance().commitTransacao();
	}  
	
	public void verificaExistenciaServicos () {
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL);
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_COSSIG);
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_DOCMARC);
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_DOCTUN);
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_DOCTUSU);
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),getLotaTitular(), SIGA_CEMAIL_RESPASSI);
	}
	
	@Transacional
	@Get({ "/app/notificarPorEmail/inicial" })
	public void inicial(Integer paramoffset) throws Exception {
		verificaExistenciaServicos ();
		if(paramoffset == null) { 
			paramoffset = 0;
		}
		List<CpConfiguracao> configuracoes =  
				CpDao.getInstance().consultarConfiguracaoNotificarEmailPorUsuario(paramoffset, 15, getTitular().getIdPessoa());
		if (configuracoes.size() < totalDeServicos) {
			for (CpConfiguracaoNotificarEmail c : CpConfiguracaoNotificarEmail.values()) {
				CpServico servico = new CpServico();
				CpConfiguracao conf = new CpConfiguracao();
				if (c.getSigla() != CpConfiguracaoNotificarEmail.SIGACEMAIL.getSigla() &&
						c.getSigla() != CpConfiguracaoNotificarEmail.EMAILALT.getSigla() &&
						c.getSigla() != CpConfiguracaoNotificarEmail.SUB.getSigla() &&
						c.getSigla() != CpConfiguracaoNotificarEmail.ESQSENHA.getSigla() &&
						c.getSigla() != CpConfiguracaoNotificarEmail.ALTSENHA.getSigla()) {
					servico = dao().consultarPorSigla(c.getSigla());
					conf = dao().consultarConfiguracoesPorServicoEPessoa(servico.getIdServico(), getTitular().getIdPessoa());
					if (conf == null) 
						gravar(servico);
				}
			}
		}
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).lista(0);
	}
	  
}
