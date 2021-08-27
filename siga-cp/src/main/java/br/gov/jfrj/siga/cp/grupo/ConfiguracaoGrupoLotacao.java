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
package br.gov.jfrj.siga.cp.grupo;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
/**
 *  Configuração de grupo para uma lotação
 */
public class ConfiguracaoGrupoLotacao extends ConfiguracaoGrupo {
	private DpLotacao dpLotacao;

	/**
	 * @return the lotacao
	 */
	
	
	/**
	 * @return the dpLotacao
	 */
	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}
	/**
	 * @param dpLotacao the dpLotacao to set
	 */
	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}
	@Override
	public void atualizarDeCpConfiguracao() {
		setCpGrupo(getCpConfiguracao().getCpGrupo());
		setDpLotacao(getCpConfiguracao().getLotacao());
		
	}
	@Override
	public void atualizarCpConfiguracao() {
		getCpConfiguracao().setCpGrupo(getCpGrupo());
		getCpConfiguracao().setLotacao(getDpLotacao());
	}
	public TipoConfiguracaoGrupoEnum obterTipoPadrao() {
		return TipoConfiguracaoGrupoEnum.LOTACAO;
	}
	
	public void setConteudoConfiguracao(String p_strConteudo) {
		/*DpLotacao t_dplLotacaoExemplo = new DpLotacao();
		t_dplLotacaoExemplo.setSiglaLotacao(p_strConteudo.toUpperCase());
		DpLotacao t_lotLotacao =  CpDao.getInstance().consultarPorSigla(t_dplLotacaoExemplo);
		setDpLotacao(t_lotLotacao); */
		Long t_lngId = Long.parseLong(p_strConteudo);
		DpLotacao t_lotLotacao =  CpDao.getInstance().consultar( t_lngId,(new DpLotacao()).getClass(),false).getLotacaoInicial();
		setDpLotacao(t_lotLotacao);
		conteudoConfiguracao = p_strConteudo;
	}
	public String getConteudoConfiguracao() {
		if (getDpLotacao() == null) {
			conteudoConfiguracao = new String();
		} else {
			//conteudoConfiguracao =  getDpLotacao().getSiglaLotacao();
			conteudoConfiguracao =  String.valueOf( getDpLotacao().getIdLotacao());
		}
		return conteudoConfiguracao;
	}
	public void setSiglaConteudoConfiguracao(String p_strSigla) {
		siglaConteudoConfiguracao = p_strSigla;
		if (getDpLotacao() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao =  getDpLotacao().getSiglaLotacao();
		}
	}
	public String getSiglaConteudoConfiguracao() {
		if (getDpLotacao() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao =  getDpLotacao().getSiglaLotacao();
		}
		return siglaConteudoConfiguracao;
	}
	public void setIdConteudoConfiguracao(Long p_lngId) {
		idConteudoConfiguracao = p_lngId;
		if (getDpLotacao() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao = getDpLotacao().getIdLotacao();
		}
	}
	public Long getIdConteudoConfiguracao() {
		if (getDpLotacao() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao = getDpLotacao().getIdLotacao();
		}
		return idConteudoConfiguracao;
	}
	public void setDescricaoConteudoConfiguracao(String p_strDescricao) {
		descricaoConteudoConfiguracao = p_strDescricao;
		if (getDpLotacao() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao =  getDpLotacao().getDescricao();
		} 
	}
	public String getDescricaoConteudoConfiguracao() {
		if (getDpLotacao() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao =  getDpLotacao().getDescricao();
		} 
		return descricaoConteudoConfiguracao;
	}
}
