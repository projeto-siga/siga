package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
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
import br.gov.jfrj.siga.cp.model.enm.CpServicosNotificacaoPorEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class ConfiguracaoNotificarPorEmailController extends SigaSelecionavelControllerSupport<CpServico, DaoFiltroSelecionavel> {

	private static final Boolean UTILIZAVEL = true;

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

	@Get({ "/app/notificarPorEmail/listar", "/public/app/page/usuario/listar" })
	public void listar () throws Exception {	 
		
		assertAcesso("CEMAIL:Módulo de notificação por email");
		
		result.include("ITENS", colecao());
		result.include("SERV_PAI", CpServicosNotificacaoPorEmail.SIGACEMAIL.getSigla());
		result.include("RECB_OBRIGATORIO", null); 
		result.include("PODE", CpSituacaoDeConfiguracaoEnum.PODE.getId());
		result.include("NAO_PODE", CpSituacaoDeConfiguracaoEnum.NAO_PODE.getId()); 
		result.forwardTo("/WEB-INF/page/usuario/configuracaoNotificarEmail.jsp"); 
	}

	public List<CpConfiguracao> colecao () {   
		final List<CpConfiguracao> configuracoes = new ArrayList<>();
		for (CpServicosNotificacaoPorEmail servicoDoUsuario : CpServicosNotificacaoPorEmail.values()) {
			CpServico servico = new CpServico();
			CpConfiguracao configuracao = new CpConfiguracao(); 
			if (servicoDoUsuario.getCondicao().equals(UTILIZAVEL)) { 
				if(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getCadastrante(), getCadastrante().getLotacao(), servicoDoUsuario.getChave())) 
					configuracao.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.PODE);
				else 
					configuracao.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.NAO_PODE);
			} 
			servico.setSigla(servicoDoUsuario.getSigla());
			servico.setDscServico(servicoDoUsuario.getDescricao());
			servico.setLabelServico(servicoDoUsuario.getExplicacao());
			configuracao.setCpServico(servico); 
			configuracoes.add(configuracao); 
		}	
		return configuracoes;
	}

	@SuppressWarnings("all") 
	@Transacional 
	@Post({"/app/notificarPorEmail/editar"})
	public void editar (String siglaServ, Integer idSituacao) throws Exception {
		CpServico servico = null;
		DpPessoa pessoa = null;
		DpLotacao lotacao = null; 
		pessoa = daoPes(getCadastrante().getId()).getPessoaInicial();
		lotacao = daoLot(getLotaCadastrante().getId()).getLotacaoInicial(); 
		servico = dao().consultarCpServicoPorChave(siglaServ);
		CpSituacaoDeConfiguracaoEnum situacao = CpSituacaoDeConfiguracaoEnum.getById(idSituacao);
		CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.UTILIZAR_SERVICO; 
		Cp.getInstance().getBL().configurarAcesso(null, getCadastrante().getOrgaoUsuario(), 
				lotacao, pessoa, servico, situacao, tpConf, getIdentidadeCadastrante());
		result.redirectTo(ConfiguracaoNotificarPorEmailController.class).listar();
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		return null;
	}    
	
}
