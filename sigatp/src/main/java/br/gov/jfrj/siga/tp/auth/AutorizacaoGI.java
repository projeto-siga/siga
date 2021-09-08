package br.gov.jfrj.siga.tp.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.util.TpBL;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

/**
 * Classe que contem os dados de autorizacao do usuario. Agrupa em um mapa o nome da permissao e um boleano indicando se o usuario a possui ou nao.
 *
 * @author db1
 *
 */
@RequestScoped
public class AutorizacaoGI {

	public static final String CP_COMPLEXO_ADMINISTRADOR = "cpComplexoAdministrador";
	private SigaObjects so;
	private Map<String, Boolean> statusPermissoes = new HashMap<String, Boolean>();
	private CpComplexo complexoPadrao;
	private CpComplexo complexoAdministrador;
	private TpBL tpbl;

	/**
	 * @deprecated CDI eyes only
	 */
	public AutorizacaoGI() {
		this.so = null;
		this.statusPermissoes = null;
		
	}
	
	@Inject
	public AutorizacaoGI(SigaObjects so, TpBL tpbl) throws Exception {
		this.so = so;
		this.tpbl = tpbl;
		this.statusPermissoes = new HashMap<String, Boolean>();
        this.setComplexoPadrao(recuperarComplexoPadrao());
        this.setComplexoAdministrador(recuperarComplexoAdministrador());
	}

	/**
	 * Recupera na configuracao do GI o complexo do perfil AdministradorPorComplexo para usuario logado verificando orgao e Lotacao e o
	 * tipo de configuracao "Utilizar Complexo"
	 *
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CpComplexo recuperarComplexoAdministrador() throws Exception {
		String SERVICO_COMPLEXO_ADMINISTRADOR = "SIGA-TP-ADMMISSAOCOMPLEXO";
		List<CpConfiguracao> configuracoes = null;
		
		CpServico cpServico = CpDao.getInstance().consultarPorSiglaCpServico(SERVICO_COMPLEXO_ADMINISTRADOR);
        if (cpServico == null) {
        	return null;
        }
		
        CpComplexo cpComplexo = null;
    	
    	CpConfiguracao cpConf = tpbl.buscaConfiguracaoComplexoAdministrador(so.getTitular(), CpTipoDeConfiguracao.UTILIZAR_SERVICO, cpServico );
        
    	if (cpConf != null) {
    		cpComplexo = cpConf.getComplexo();
    	}

        // Recuperando ComplexoAdministrador para uma lotacao especifica de um orgão
        /*
    	String qrl = 	"SELECT cp FROM CpConfiguracao cp " +  
    	" WHERE  cp.dpPessoa.idPessoaIni = :idPessoaIni"  	+
    	" AND    cp.orgaoUsuario.idOrgaoUsu = :orgaoUsuarioId"		+
    	" AND    cp.cpSituacaoConfiguracao.idSitConfiguracao = :cpSituacaoConfiguracaoId " + 
    	" AND    cp.cpServico.siglaServico = '" + SERVICO_COMPLEXO_ADMINISTRADOR + "'" +
    	" AND    cp.hisIdcFim is null"; 

    	Query qry = RequisicaoTransporte.AR.em().createQuery(qrl);
    	qry.setParameter("idPessoaIni", so.getTitular().getIdPessoaIni());
    	qry.setParameter("orgaoUsuarioId",so.getTitular().getOrgaoUsuario().getId());
    	qry.setParameter("cpSituacaoConfiguracaoId", 1L);
    	configuracoes = (List<CpConfiguracao>) qry.getResultList();
        
        if (configuracoes != null && !configuracoes.isEmpty() && configuracoes.size() > 0) {
            cpComplexo = configuracoes.get(0).getComplexo();
        } */

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

    @SuppressWarnings("unchecked")
	public CpComplexo recuperarComplexoPadrao(DpPessoa dpPessoa) {
        
        CpComplexo cpComplexo = null;
    	
    	CpConfiguracao cpConf = tpbl.buscaConfiguracaoComplexoPadrao(dpPessoa, CpTipoDeConfiguracao.UTILIZAR_COMPLEXO);
        
    	cpComplexo = cpConf.getComplexo();
            	
    /*	long TIPO_CONFIG_COMPLEXO_PADRAO = 400;
        List<CpConfiguracao> configuracoes = null;


        // Recuperando Configuracao Pode para uma lotacao especifica de um orgão
        
    	String qrl = 	"SELECT cp FROM CpConfiguracao cp " +  
    	" WHERE  cp.lotacao.idLotacaoIni = :lotacaoIni"  	+
    	" AND    cp.orgaoUsuario.idOrgaoUsu = :orgaoUsuarioId"		+
    	" AND    cp.cpTipoConfiguracao = " + CpTipoDeConfiguracao.UTILIZAR_COMPLEXO +
    	" AND    cp.cpSituacaoConfiguracao = :cpSituacaoConfiguracaoId" +
    	" AND    cp.hisIdcFim is null";

    	Query qry = RequisicaoTransporte.AR.em().createQuery(qrl);
    	qry.setParameter("lotacaoIni", dpPessoa.getLotacao().getIdLotacaoIni());
    	qry.setParameter("orgaoUsuarioId",dpPessoa.getOrgaoUsuario().getId());
    	qry.setParameter("cpSituacaoConfiguracaoId", CpSituacaoDeConfiguracaoEnum.PODE);
    	configuracoes = (List<CpConfiguracao>) qry.getResultList();
        
        if (configuracoes != null && !configuracoes.isEmpty()) {
            cpComplexo = configuracoes.get(0).getComplexo();
        } else {
            // Recuperando Configuracao default para um orgao especifico
        	
        	qrl = 	"SELECT cp FROM CpConfiguracao cp" +  
        	    	" WHERE  cp.lotacao is null"  	+
        	    	" AND    cp.orgaoUsuario.idOrgaoUsu = :orgaoUsuarioId"		+
        	    	" AND    cp.cpTipoConfiguracao = " + CpTipoDeConfiguracao.UTILIZAR_COMPLEXO +
        	    	" AND    cp.cpSituacaoConfiguracao = :cpSituacaoConfiguracaoId" +
        	    	" AND    cp.hisIdcFim is null";
        	qry = RequisicaoTransporte.AR.em().createQuery(qrl);
        	qry.setParameter("orgaoUsuarioId",dpPessoa.getOrgaoUsuario().getId());
        	qry.setParameter("cpSituacaoConfiguracaoId", CpSituacaoDeConfiguracaoEnum.DEFAULT);
        	configuracoes = (List<CpConfiguracao>) qry.getResultList();
        	
        	if (configuracoes != null && !configuracoes.isEmpty()) {
                cpComplexo = configuracoes.get(0).getComplexo();
            }
        } */
        if (cpComplexo == null) {
            throw new NullPointerException(MessagesBundle.getMessage("cpComplexo.null.exception", ""));
        }

		return cpComplexo;
	}

	public CpComplexo getComplexoPadrao() {
		return complexoPadrao;
	}

	public void setComplexoPadrao(CpComplexo complexoPadrao) {
		this.complexoPadrao = complexoPadrao;
	}

	public CpComplexo getComplexoAdministrador() {
		return complexoAdministrador;
	}

	public void setComplexoAdministrador(CpComplexo complexoAdministrador) {
		this.complexoAdministrador = complexoAdministrador;
	}
	
}