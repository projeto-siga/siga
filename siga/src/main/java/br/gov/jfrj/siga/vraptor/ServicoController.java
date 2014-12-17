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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.SituacaoFuncionalEnum;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.libs.rpc.FaultMethodResponseRPC;
import br.gov.jfrj.siga.libs.rpc.SimpleMethodResponseRPC;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.Preparable;

@Resource
public class ServicoController 	extends SigaController {
	public ServicoController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}
	// preparação do ambiente
	private CpTipoConfiguracao cpTipoConfiguracaoUtilizador;
	private CpTipoConfiguracao cpTipoConfiguracaoAConfigurar;
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
	private String idSituacaoConfiguracao;
	private Long idTipoConfiguracao;
	
	// gravação - retorno
	private String respostaXMLStringRPC;
	private String resultadoRetornoAjax;
	private String mensagemRetornoAjax;
	private String idPessoaRetornoAjax;
	private String idServicoRetornoAjax;
	private String idSituacaoRetornoAjax;
	//
	

	
	@Get("/app/servico-acesso")
	public void edita() {
		setDpPessoasDaLotacao(new ArrayList<DpPessoa>());
		setCpConfiguracoesAdotadas(new ArrayList<CpConfiguracao>());
		setCpTipoConfiguracaoUtilizador(obterCpTipoConfiguracaoUtilizador());
		setCpTipoConfiguracaoAConfigurar(obterCpTipoConfiguracaoAConfigurar(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO));
		setCpServicosDisponiveis(  obterServicosDaLotacaoEfetiva());
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		
		result.include("cpServicosDisponiveis", cpServicosDisponiveis);
		result.include("idTipoConfiguracaoUtilizarServico", CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
		result.include("IdTipoConfiguracaoUtilizarServicoOutraLotacao", CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
		result.include("dpPessoasDaLotacao", dpPessoasDaLotacao);
		result.include("cpConfiguracoesAdotadas", cpConfiguracoesAdotadas);
		result.include("cpTipoConfiguracaoAConfigurar", cpTipoConfiguracaoAConfigurar);
	}
	
	
	/**
	*@param dpPessoasDaLotacao the dpPessoasDaLotacao to set
	*/
	public void setDpPessoasDaLotacao(List<DpPessoa> dpPessoasDaLotacao) {
		this.dpPessoasDaLotacao = dpPessoasDaLotacao;
	}
	
	/**
	 * @param cpConfiguracoesAdotadas the cpConfiguracoesAdotadas to set
	 */
	public void setCpConfiguracoesAdotadas(
			List<CpConfiguracao> cpConfiguracoesAdotadas) {
		this.cpConfiguracoesAdotadas = cpConfiguracoesAdotadas;
	}
	
	/**
	 *  Retorna o tipo de configuração que o utilizador da interface  
	 *  tem permissão
	 */
	private CpTipoConfiguracao obterCpTipoConfiguracaoUtilizador() {
		CpTipoConfiguracao t_tcfTipo = dao.consultar(
				CpTipoConfiguracao.TIPO_CONFIG_HABILITAR_SERVICO_DE_DIRETORIO
				//CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO
				, CpTipoConfiguracao.class
				, false);
		
		return t_tcfTipo;
	}
	
	/**
	* @param cpTipoConfiguracaoUtilizador the cpTipoConfiguracaoUtilizador to set
	*/
	public void setCpTipoConfiguracaoUtilizador(
			CpTipoConfiguracao cpTipoConfiguracaoUtilizador) {
		this.cpTipoConfiguracaoUtilizador = cpTipoConfiguracaoUtilizador;
	}
	
	/**
	*  Retorna o tipo de configuração a Configurar  
	*  
	*/
	private CpTipoConfiguracao obterCpTipoConfiguracaoAConfigurar(Long idTipoConfiguracao) {
		CpTipoConfiguracao t_tcfTipo = dao.consultar(
				idTipoConfiguracao
				, CpTipoConfiguracao.class
				, false);
		return t_tcfTipo;
	}
	
	/**
	* @param cpTipoConfiguracaoAConfigurar the cpTipoConfiguracaoAConfigurar to set
	*/
	public void setCpTipoConfiguracaoAConfigurar(
			CpTipoConfiguracao cpTipoConfiguracaoAConfigurar) {
		this.cpTipoConfiguracaoAConfigurar = cpTipoConfiguracaoAConfigurar;
	}
	
	/**
	*  Obtém, os servicos da lotação efetiva
	*/
	@SuppressWarnings("unchecked")
	private ArrayList<CpServico> obterServicosDaLotacaoEfetiva() {
		CpTipoLotacao t_ctlTipoLotacao = obterTipoDeLotacaoEfetiva();
		final Query query = dao.getSessao().getNamedQuery("consultarCpConfiguracoesPorTipoLotacao");
		query.setLong("idTpLotacao", t_ctlTipoLotacao.getIdTpLotacao());
		ArrayList<CpConfiguracao> t_arlConfigServicos = (ArrayList<CpConfiguracao>) query.list();
		ArrayList<CpServico> t_arlServicos = new ArrayList<CpServico>();
		for (CpConfiguracao t_cfgConfiguracao : t_arlConfigServicos) {
			t_arlServicos.add(t_cfgConfiguracao.getCpServico());
		}
		return t_arlServicos;
	}
	
	/**
	* @param cpServicosDisponiveis the cpServicosDisponiveis to set
	*/
	public void setCpServicosDisponiveis(List<CpServico> cpServicosDisponiveis) {
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
	

	@Post("/app/servico-acesso/inserirPessoaExtra")
	public void aInserirPessoaExtra() throws AplicacaoException{
		DpPessoa pes = dao.consultar(paramLong("pessoaExtra_pessoaSel.id"), DpPessoa.class,false);
		if (pes.getLotacao().equivale(obterLotacaoEfetiva())){
			throw new AplicacaoException("A pessoa selecionada deve ser de outra lotação!");
		}
		
		
		/*
		 * MELHORAR: Permite a inclusão apenas de pessoas ativas. 
		 * Isso deve ser melhorado, pois ainda não existe uma referência nem mapeamento no hibernate
		 *  para a descrição da situação funciona da pessoa. 
		 * */
		if(!pes.getSituacaoFuncionalPessoa().equals("1")){
			if (pes.getSituacaoFuncionalPessoa().equals("2")){
				throw new AplicacaoException("Não é possível inserir uma pessoa que está CEDIDA!<br/>Por favor, abra um chamado para o suporte técnico.");
			}else{
				throw new AplicacaoException("A pessoa não está com situação funcional ATIVA! Situação atual: " + pes.getSituacaoFuncionalPessoa() + "<br/>Por favor, abra um chamado para o suporte técnico.");	
			}
			
		}
		
		
		CpTipoConfiguracao tpConf =  obterCpTipoConfiguracaoAConfigurar(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
		Cp.getInstance().getBL().configurarAcesso(null, pes.getOrgaoUsuario(), obterLotacaoEfetiva(), pes, null, null, tpConf, getIdentidadeCadastrante());
	}
	
}