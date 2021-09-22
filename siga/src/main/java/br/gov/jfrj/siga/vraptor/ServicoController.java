/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

/*
 * Criado em 23/11/2005
 */

package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.SituacaoFuncionalEnum;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.libs.rpc.FaultMethodResponseRPC;
import br.gov.jfrj.siga.libs.rpc.SimpleMethodResponseRPC;
import br.gov.jfrj.siga.vraptor.suporte.ConfiguracaoConfManual;

//MIGRAÇÃO VRAPTOR DA CLASSE WEB-WORK "package br.gov.jfrj.webwork.action.SelfConfigAction"

@Controller
public class ServicoController 	extends SigaController {
	
	
	// preparação do ambiente
	private CpTipoDeConfiguracao cpTipoConfiguracaoUtilizador;
	private CpTipoDeConfiguracao cpTipoConfiguracaoAConfigurar;
	private List<CpServico> cpServicosDisponiveis;
	/*private CpSituacaoConfiguracao cpSituacaoPadrao;
	
	private List<CpSituacaoConfiguracao> cpSituacoesPossiveis;
	*/
	// edição
	private DpLotacao dpLotacaoConsiderada;
	private List<DpPessoa> dpPessoasDaLotacao; 
	private List<CpConfiguracao> cpConfiguracoesAdotadas;
	// gravação - parametros
	private String idPessoaConfiguracao;
	private String idServicoConfiguracao;
	private Integer idSituacaoConfiguracao;
	private Integer idTipoConfiguracao;
	
	// gravação - retorno
	private String respostaXMLStringRPC;
	private String resultadoRetornoAjax;
	private String mensagemRetornoAjax;
	private String idPessoaRetornoAjax;
	private String idServicoRetornoAjax;
	private String idSituacaoRetornoAjax;
	//
	
	

	/**
	 * @deprecated CDI eyes only
	 */
	public ServicoController() {
		super();
	}

	@Inject
	public ServicoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

//		result.on(AplicacaoException.class).forwardTo(this).appexception();
//		result.on(Exception.class).forwardTo(this).exception();
//		
//		result.on(AplicacaoException.class).forwardTo(this).appexception();
//		result.on(Exception.class).forwardTo(this).exception();		
	}

	
	@Get("/app/gi/servico/editar")
	public void edita() throws Exception {
		ConfiguracaoConfManual configuracaoConfManual = new ConfiguracaoConfManual(dao, obterLotacaoEfetiva());
		setDpPessoasDaLotacao(new ArrayList<DpPessoa>());
		setCpConfiguracoesAdotadas(new ArrayList<CpConfiguracao>());
		setCpTipoConfiguracaoUtilizador(obterCpTipoConfiguracaoUtilizador());
		setCpTipoConfiguracaoAConfigurar(CpTipoDeConfiguracao.UTILIZAR_SERVICO);
		setCpServicosDisponiveis(  obterServicosDaLotacaoEfetiva());

		if (seUsuarioPodeExecutar()) {
			DpLotacao t_dltLotacao = obterLotacaoEfetiva();
			dpLotacaoConsiderada = t_dltLotacao;
			if (t_dltLotacao != null) {
				// TODO: _LAGS - verificar opção para sublotações
				setDpPessoasDaLotacao(dao().pessoasPorLotacao(t_dltLotacao.getIdLotacao(), false,false,SituacaoFuncionalEnum.ATIVOS_E_CEDIDOS));
				setCpConfiguracoesAdotadas(obterConfiguracoesDasPessoasDaLotacaoConsiderada());
			}
		} else {
			throw new AplicacaoException("Acesso não permitido !");
			
		}	
		
		result.include("configuracaoConfManual", configuracaoConfManual);
		result.include("cpServicosDisponiveis", cpServicosDisponiveis);
		result.include("idTpConfUtilizarSvc", CpTipoDeConfiguracao.UTILIZAR_SERVICO);
		result.include("idTpConfUtilizarSvcOutraLot", CpTipoDeConfiguracao.UTILIZAR_SERVICO_OUTRA_LOTACAO);
		result.include("dpPessoasDaLotacao", dpPessoasDaLotacao);
		result.include("cpConfiguracoesAdotadas", cpConfiguracoesAdotadas);
		result.include("cpTipoConfiguracaoAConfigurar", cpTipoConfiguracaoAConfigurar);
		result.include("dscTpConfiguracao", cpTipoConfiguracaoAConfigurar.getDescr());
		result.include("pessoasGrupoSegManual", Cp.getInstance().getConf().getPessoasGrupoSegManual(obterLotacaoEfetiva()));
	}
	
	/**
	 * Retorna as configurações para as pessoas da lotação considerada   
	 *  
	 */
	private List<CpConfiguracao> obterConfiguracoesDasPessoasDaLotacaoConsiderada() throws AplicacaoException {
		ArrayList<CpConfiguracao> t_arlConfig = new ArrayList<CpConfiguracao>();
		for (DpPessoa t_dppPessoa : dpPessoasDaLotacao ) {
			for (CpServico t_cpsServico : cpServicosDisponiveis) {
				CpConfiguracao t_cfgConfigPessoaLotacao = obterConfiguracao(dpLotacaoConsiderada,
															t_dppPessoa,
															cpTipoConfiguracaoAConfigurar,
															t_cpsServico);
				if (t_cfgConfigPessoaLotacao == null) {
					CpConfiguracao t_cpcConfigNovo = new CpConfiguracao();
					t_cpcConfigNovo.setLotacao(dpLotacaoConsiderada);
					t_cpcConfigNovo.setDpPessoa(t_dppPessoa);
					t_cpcConfigNovo.setCpTipoConfiguracao(cpTipoConfiguracaoAConfigurar);
					t_cpcConfigNovo.setCpServico(t_cpsServico);
					t_cpcConfigNovo.setCpSituacaoConfiguracao(obterSituacaoPadrao(t_cpsServico));
					t_arlConfig.add(t_cpcConfigNovo);
				} else {
					t_arlConfig.add(t_cfgConfigPessoaLotacao);
				}
			}
		}
		return t_arlConfig;
	}
	
	/**
	 *  Retorna a situacao padrão para um dado servico
	 * 
	 */
	private CpSituacaoDeConfiguracaoEnum obterSituacaoPadrao(CpServico p_cpsServico) {
		return p_cpsServico.getCpTipoServico().getSituacaoDefault();
	}
	

	/**
	 *  Retorna se o usuário ou quem ele substitui pode
	 *  pode executar a interface
	 */
	private boolean seUsuarioPodeExecutar() {
		// TODO: _LAGS - obterPessoaEfetiva() e ver se é diretor
		/// ID_TIPO_CONFIGURACAO_PODE_EXECUTAR_SERVICO = new Long(202);
		return true;
	}

	/**
	 * Retorna a configuração para a pessoa, lotação e cpTipoConfiguracaoAConfigurar  
	 *  
	 */
	private CpConfiguracao obterConfiguracao(DpLotacao p_dltLotacao,
											 DpPessoa p_dpsPessoa,
											 CpTipoDeConfiguracao p_ctcTipoConfig,
											 CpServico p_cpsServico
											 ) {
		CpConfiguracao t_cfgConfigExemplo  = new CpConfiguracao();
		t_cfgConfigExemplo.setLotacao(p_dltLotacao);
		t_cfgConfigExemplo.setDpPessoa(p_dpsPessoa);
		t_cfgConfigExemplo.setCpTipoConfiguracao(p_ctcTipoConfig);
		t_cfgConfigExemplo.setCpServico(p_cpsServico);
		
		CpConfiguracao cpConf = null;
		try {
			CpConfiguracaoCache cache = Cp.getInstance().getConf().buscaConfiguracao(
					t_cfgConfigExemplo, new int[0], null);
			cpConf = CpDao.getInstance().consultar(cache.idConfiguracao, CpConfiguracao.class, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cpConf;
		
	}
	
	
	/**
	*@param dpPessoasDaLotacao the dpPessoasDaLotacao to set
	*/
	private void setDpPessoasDaLotacao(List<DpPessoa> dpPessoasDaLotacao) {
		this.dpPessoasDaLotacao = dpPessoasDaLotacao;
	}
	
	/**
	 * @param cpConfiguracoesAdotadas the cpConfiguracoesAdotadas to set
	 */
	private void setCpConfiguracoesAdotadas(
			List<CpConfiguracao> cpConfiguracoesAdotadas) {
		this.cpConfiguracoesAdotadas = cpConfiguracoesAdotadas;
	}
	
	/**
	 *  Retorna o tipo de configuração que o utilizador da interface  
	 *  tem permissão
	 */
	private CpTipoDeConfiguracao obterCpTipoConfiguracaoUtilizador() {
		CpTipoDeConfiguracao t_tcfTipo = dao.consultar(
				CpTipoDeConfiguracao.HABILITAR_SERVICO_DE_DIRETORIO
				//CpTipoDeConfiguracao.UTILIZAR_SERVICO
				, CpTipoDeConfiguracao.class
				, false);
		
		return t_tcfTipo;
	}
	
	/**
	* @param cpTipoConfiguracaoUtilizador the cpTipoConfiguracaoUtilizador to set
	*/
	private void setCpTipoConfiguracaoUtilizador(
			CpTipoDeConfiguracao cpTipoConfiguracaoUtilizador) {
		this.cpTipoConfiguracaoUtilizador = cpTipoConfiguracaoUtilizador;
	}
	
	/**
	* @param cpTipoConfiguracaoAConfigurar the cpTipoConfiguracaoAConfigurar to set
	*/
	private void setCpTipoConfiguracaoAConfigurar(
			CpTipoDeConfiguracao cpTipoConfiguracaoAConfigurar) {
		this.cpTipoConfiguracaoAConfigurar = cpTipoConfiguracaoAConfigurar;
	}
	
	/**
	*  Obtém, os servicos da lotação efetiva
	*/
	@SuppressWarnings("unchecked")
	private ArrayList<CpServico> obterServicosDaLotacaoEfetiva() {
		ArrayList<CpServico> t_arlServicos = new ArrayList<CpServico>();
		CpTipoLotacao t_ctlTipoLotacao = obterTipoDeLotacaoEfetiva();
		if (t_ctlTipoLotacao == null)
			return t_arlServicos;
		List<CpConfiguracao> t_arlConfigServicos = dao.consultarCpConfiguracoesPorTipoLotacao(t_ctlTipoLotacao.getIdTpLotacao());
		for (CpConfiguracao t_cfgConfiguracao : t_arlConfigServicos) {
			t_arlServicos.add(t_cfgConfiguracao.getCpServico());
		}
		return t_arlServicos;
	}
	
	/**
	* @param cpServicosDisponiveis the cpServicosDisponiveis to set
	*/
	private void setCpServicosDisponiveis(List<CpServico> cpServicosDisponiveis) {
		this.cpServicosDisponiveis = cpServicosDisponiveis;
	}
	
	/**
	*  Retorna o tipo de lotação do usuário 
	*  ou o tipo de lotação na qual ele substitui alguém
	*/     
	private CpTipoLotacao obterTipoDeLotacaoEfetiva() {
		 /*Código definitivo
		 * return obterLotacaoEfetiva().getCpTipoLotacao();
		 * abaixo codigo temporário : usado nos testes iniciais
		 */
		//return dao.consultar(100L, CpTipoLotacao.class, false);
		return obterLotacaoEfetiva().getCpTipoLotacao();
	}
	
	/**
	 *  Retorna a lotação do usuário ou a lotação na qual ele
	 *  substitui alguém
	 */
	private DpLotacao obterLotacaoEfetiva() {
		if (getLotaTitular() != null)  {
			if (getLotaTitular().getIdLotacao() != getCadastrante().getLotacao().getIdLotacao()) {
				return getLotaTitular();
			}
		}
		if (getCadastrante()  != null) {
			return getCadastrante().getLotacao();
		}
		return null;
	}
	

	@Transacional
	@Post("/app/gi/servico/inserirPessoaExtra")
	public void aInserirPessoaExtra() throws Exception{
		DpPessoa pes = dao.consultar(paramLong("pessoaExtra_pessoaSel.id"), DpPessoa.class,false);
		if (pes.getLotacao().equivale(obterLotacaoEfetiva())){
			throw new AplicacaoException("A pessoa selecionada deve ser de outra lotação!");
		}
		
		
		/*
		 * MELHORAR: Permite a inclusão apenas de pessoas ativas. 
		 * Isso deve ser melhorado, pois ainda não existe uma referência nem mapeamento no hibernate
		 *  para a descrição da situaçao funcional da pessoa. 
		 * */
		if(!pes.getSituacaoFuncionalPessoa().equals("1")){
			if (pes.getSituacaoFuncionalPessoa().equals("2")){
				throw new AplicacaoException("Não é possével inserir uma pessoa que está CEDIDA!<br/>Por favor, abra um chamado para o suporte técnico.");
			}else{
				throw new AplicacaoException("A pessoa não está com situação funcional ATIVA! Situação atual: " + pes.getSituacaoFuncionalPessoa() + "<br/>Por favor, abra um chamado para o suporte técnico.");	
			}
			
		}
		
		
		Cp.getInstance().getBL().configurarAcesso(null, pes.getOrgaoUsuario(), obterLotacaoEfetiva(), pes, null, null, CpTipoDeConfiguracao.UTILIZAR_SERVICO_OUTRA_LOTACAO, getIdentidadeCadastrante());
		result.redirectTo(this).edita();
	}
	
	
	
	@Transacional
	@Get("/app/gi/servico/excluir-pessoa-extra/{id}")
	public void excluirPessoaExtra(Long id) throws Exception{
		DpPessoa pes = dao().consultar(id, DpPessoa.class,false);
		Cp.getInstance().getConf().excluirPessoaExtra(pes, obterLotacaoEfetiva(), CpTipoDeConfiguracao.UTILIZAR_SERVICO_OUTRA_LOTACAO, getIdentidadeCadastrante());
		
		result.redirectTo(this).edita();
	}
	
	@Transacional
	@Get("/app/gi/servico/gravar")
	public void gravar(String idPessoaConfiguracao, 
					String idServicoConfiguracao, 
					Integer idSituacaoConfiguracao, 
					Integer idTipoConfiguracao,
					HttpServletResponse response) throws Exception {
		this.idPessoaConfiguracao = idPessoaConfiguracao;
		this.idServicoConfiguracao = idServicoConfiguracao;
		this.idSituacaoConfiguracao = idSituacaoConfiguracao;
		this.idTipoConfiguracao = idTipoConfiguracao;
		
		if (seUsuarioPodeExecutar()) {
			try {
				DpLotacao t_dplLotacao = obterLotacaoEfetiva();
				Long t_lngIdPessoa = Long.parseLong(idPessoaConfiguracao);
				DpPessoa t_dppPessoa = dao().consultar(t_lngIdPessoa,DpPessoa.class,false);
				Integer t_lngIdServico = Integer.parseInt(idServicoConfiguracao);
                CpServico t_cpsServico = dao().consultar(t_lngIdServico, CpServico.class, false);
                CpSituacaoDeConfiguracaoEnum t_cstSituacao = CpSituacaoDeConfiguracaoEnum.getById(idSituacaoConfiguracao);
                CpConfiguracao t_cfgConfigGravada = Cp.getInstance().getBL().configurarAcesso(null,t_dplLotacao.getOrgaoUsuario()
                											  ,t_dplLotacao
                											  ,t_dppPessoa
                											  ,t_cpsServico
                											  ,t_cstSituacao
                											  ,CpTipoDeConfiguracao.getById(t_lngIdServico)
                											  ,getIdentidadeCadastrante());
				HashMap<String, String> t_hmpRetorno = new HashMap<String, String>();
				t_hmpRetorno.put("idpessoa", /*idPessoaConfiguracao*/ String.valueOf(t_cfgConfigGravada.getDpPessoa().getIdPessoa()));
				t_hmpRetorno.put("idservico", /*idServicoConfiguracao*/String.valueOf(t_cfgConfigGravada.getCpServico().getIdServico()));
				t_hmpRetorno.put("idsituacao", /*idSituacaoConfiguracao*/String.valueOf(t_cfgConfigGravada.getCpSituacaoConfiguracao().getId()) );
				SimpleMethodResponseRPC t_smrResposta = new SimpleMethodResponseRPC();
				t_smrResposta.setMembersFrom(t_hmpRetorno);
				setRespostaXMLStringRPC(t_smrResposta.toXMLString());	
			} catch (Exception e) {
				CpDao.rollbackTransacao();
				FaultMethodResponseRPC t_fmrRetorno = new FaultMethodResponseRPC();
				t_fmrRetorno.set(0, e.getMessage());
				setRespostaXMLStringRPC(t_fmrRetorno.toXMLString());
			}
		} else {
			FaultMethodResponseRPC t_fmrRetorno = new FaultMethodResponseRPC();
			t_fmrRetorno.set(0, "Acesso não permitido !");
			setRespostaXMLStringRPC(t_fmrRetorno.toXMLString());
		}
	    result
	    	.use(Results.http())
	    	.body(getRespostaXMLStringRPC());  
   }


	private String getRespostaXMLStringRPC() {
		return respostaXMLStringRPC;
	}


	private void setRespostaXMLStringRPC(String respostaXMLStringRPC) {
		this.respostaXMLStringRPC = respostaXMLStringRPC;
	}
}