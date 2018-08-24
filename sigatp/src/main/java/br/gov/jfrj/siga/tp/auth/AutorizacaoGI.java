package br.gov.jfrj.siga.tp.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

import com.google.common.base.Optional;

/**
 * Classe que contem os dados de autorizacao do usuario. Agrupa em um mapa o nome da permissao e um boleano indicando se o usuario a possui ou nao.
 *
 * @author db1
 *
 */
@RequestScoped
@Component
public class AutorizacaoGI {

	public static final String CP_COMPLEXO_ADMINISTRADOR = "cpComplexoAdministrador";
	private SigaObjects so;
	private Map<String, Boolean> statusPermissoes = new HashMap<String, Boolean>();

	public AutorizacaoGI(SigaObjects so) {
		this.so = so;
		this.statusPermissoes = new HashMap<String, Boolean>();
	}

	/**
	 * Recupera na configuracao do GI o complexo do perfil AdministradorPorComplexo para usuario logado verificando orgao e Lotacao e o
	 * tipo de configuracao "Utilizar Complexo"
	 *
	 * @return
	 * @throws Exception
	 */
	public CpComplexo recuperarComplexoAdministrador() throws Exception {
		String SERVICO_COMPLEXO_ADMINISTRADOR = "SIGA-TP-ADMMISSAOCOMPLEXO";
		CpServico cpServico = TpDao.find(CpServico.class, "siglaServico", SERVICO_COMPLEXO_ADMINISTRADOR).first();
		CpSituacaoConfiguracao cpSituacaoConfiguracaoPode = TpDao.findById(CpSituacaoConfiguracao.class, 1L);
		List<CpConfiguracao> configuracoes = null;
		CpComplexo cpComplexo = null;

		// and dtHistDtFim IS NOT NULL
		Object[] parametros = { so.getTitular().getIdPessoaIni(), cpSituacaoConfiguracaoPode.getIdSitConfiguracao(), cpServico };
		configuracoes = TpDao.find(CpConfiguracao.class, "(dpPessoa.idPessoaIni = ? and cpSituacaoConfiguracao.idSitConfiguracao = ? and cpServico = ? and hisIdcFim is null )", parametros).fetch();

		if (configuracoes != null)
			cpComplexo = configuracoes.get(0).getComplexo();
		return cpComplexo;
	}

	public AutorizacaoGI incluir(String nomePermissao) {
		this.statusPermissoes.put(nomePermissao, statusAutorizacao(nomePermissao));
		return this;
	}

	public Boolean getStatusPermissao(String nomePermissao) {
		return Optional.fromNullable(statusPermissoes.get(nomePermissao)).or(Boolean.FALSE);
	}

	public boolean statusAutorizacao(String permissao) {
		try {
			so.assertAcesso("TP:Modulo de Transportes;" + permissao);
			return Boolean.TRUE;
		} catch (Exception e) {
		}
		return Boolean.FALSE;
	}

	public AutorizacaoGI incluirAdministrarMissaoComplexo(Result result) {
		try {
			CpComplexo cpComplexo = recuperarComplexoAdministrador();
			Boolean encontrouComplexo = cpComplexo != null;
			if (encontrouComplexo) {
				result.include(CP_COMPLEXO_ADMINISTRADOR, cpComplexo);
			}
			this.statusPermissoes.put(Autorizacoes.EXIBIR_MENU_ADMMISSAO_ADMINISTRAR_MISSAO_COMPLEXO, encontrouComplexo);
		} catch (Exception e) {
			this.statusPermissoes.put(Autorizacoes.EXIBIR_MENU_ADMMISSAO_ADMINISTRAR_MISSAO_COMPLEXO, Boolean.FALSE);
		}
		return this;
	}

	public Boolean deveExibirMenuAdmissaoComplexo() {
		return getStatusPermissao(Autorizacoes.EXIBIR_MENU_ADMMISSAO_ADMINISTRAR_MISSAO_COMPLEXO);
	}

	public void preencherDadosAutorizacoes(Result result) {
		result.include(Autorizacoes.EXIBIR_MENU_ADMINISTRAR, ehAdministrador());
		result.include(Autorizacoes.EXIBIR_MENU_ADMINISTRAR_FROTA, ehAdministradorFrota());
		result.include(Autorizacoes.EXIBIR_MENU_ADMINISTRAR_MISSAO, ehAdministradorMissao());
		result.include(Autorizacoes.EXIBIR_MENU_APROVADOR, ehAprovador());
		result.include(Autorizacoes.EXIBIR_MENU_GABINETE, ehGabinete());
		result.include(Autorizacoes.EXIBIR_MENU_ADMIN_GABINETE, ehAdminGabinete());
		result.include(Autorizacoes.EXIBIR_MENU_AGENTE, ehAgente());
		result.include(Autorizacoes.EXIBIR_MENU_ADMMISSAO_ADMINISTRAR_MISSAO_COMPLEXO, deveExibirMenuAdmissaoComplexo());
	}

	public Boolean ehAdministrador() {
		return getStatusPermissao(Autorizacoes.ADM_ADMINISTRAR);
	}

	public Boolean ehAdministradorFrota() {
		return getStatusPermissao(Autorizacoes.ADMFROTA_ADMINISTRAR_FROTA);
	}

	public Boolean ehAdministradorMissao() {
		return getStatusPermissao(Autorizacoes.ADMMISSAO_ADMINISTRAR_MISSAO);
	}

	public Boolean ehAdministradorMissaoPorComplexo() {
		return getStatusPermissao(Autorizacoes.EXIBIR_MENU_ADMMISSAO_ADMINISTRAR_MISSAO_COMPLEXO);
	}

	public Boolean ehAprovador() {
		return getStatusPermissao(Autorizacoes.APR_APROVADOR);
	}

	public Boolean ehGabinete() {
		return getStatusPermissao(Autorizacoes.GAB_GABINETE);
	}

	public Boolean ehAdminGabinete() {
		return getStatusPermissao(Autorizacoes.ADMGAB_ADMIN_GABINETE);
	}

	public Boolean ehAgente() {
		return getStatusPermissao(Autorizacoes.AGN_AGENTE);
	}
	
	public CpComplexo recuperarComplexoPadrao() throws Exception {
		return recuperarComplexoPadrao(so.getTitular());
	}

	private CpComplexo recuperarComplexoPadrao(DpPessoa dpPessoa) throws Exception {
		long TIPO_CONFIG_COMPLEXO_PADRAO = 400;
		CpTipoConfiguracao tpConf = CpTipoConfiguracao.AR.findById(TIPO_CONFIG_COMPLEXO_PADRAO);
		CpSituacaoConfiguracao cpSituacaoConfiguracaoPode = CpSituacaoConfiguracao.AR.findById(1L); 
		CpSituacaoConfiguracao cpSituacaoConfiguracaoPadrao = CpSituacaoConfiguracao.AR.findById(5L); 
		List<CpConfiguracao> configuracoes = null;
		CpComplexo cpComplexo = null;

		// Recuperando Configuracao Pode para uma lotacao especifica
		Object[] parametros =  {dpPessoa.getLotacao().getIdLotacaoIni(), cpSituacaoConfiguracaoPode, dpPessoa.getOrgaoUsuario(),tpConf};
		configuracoes = CpConfiguracao.AR.find("((lotacao.idLotacaoIni = ? and cpSituacaoConfiguracao = ?) and orgaoUsuario = ?  and cpTipoConfiguracao = ? and hisIdcFim is null  )", parametros).fetch();
		if (configuracoes != null && configuracoes.size() > 0) {
			cpComplexo = configuracoes.get(0).getComplexo();
		} else {
		
		// Recuperando Configuracao default para um orgao especifico
		Object[] parametros1 =  {cpSituacaoConfiguracaoPadrao, dpPessoa.getOrgaoUsuario(),tpConf};
		configuracoes = CpConfiguracao.AR.find("((cpSituacaoConfiguracao = ?) and orgaoUsuario = ?  and cpTipoConfiguracao = ? and hisIdcFim is null  )", parametros1).fetch();
			if (configuracoes != null && configuracoes.size() > 0) {
				cpComplexo = configuracoes.get(0).getComplexo();
			}
		}
		if (cpComplexo == null) {
            throw new NullPointerException(MessagesBundle.getMessage("cpComplexo.null.exception"));
		}
		
		return cpComplexo;
	}
}