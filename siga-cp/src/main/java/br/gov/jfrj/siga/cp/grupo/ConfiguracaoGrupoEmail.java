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
/**
 *  Configuração de grupo para um email
 */

public class ConfiguracaoGrupoEmail extends ConfiguracaoGrupo {
	private String  nmEmail;
	
	
	/**
	 * @return the nmEmail
	 */
	public String getNmEmail() {
		return nmEmail;
	}

	/**
	 * @param nmEmail the nmEmail to set
	 */
	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}

	@Override
	public void atualizarCpConfiguracao() {
		getCpConfiguracao().setCpGrupo(getCpGrupo());
		getCpConfiguracao().setNmEmail(getNmEmail());
	}

	@Override
	public void atualizarDeCpConfiguracao() {
		setCpGrupo(getCpConfiguracao().getCpGrupo());
		setNmEmail(getCpConfiguracao().getNmEmail());
	}

	@Override
	public TipoConfiguracaoGrupoEnum obterTipoPadrao() {
		return TipoConfiguracaoGrupoEnum.EMAIL;
	}
	public void setConteudoConfiguracao(String p_strConteudo) {
		conteudoConfiguracao = p_strConteudo;
		setNmEmail(p_strConteudo);
	}
	public String getConteudoConfiguracao() {
		conteudoConfiguracao = getNmEmail();
		return conteudoConfiguracao;
	}
	public void setSiglaConteudoConfiguracao(String p_strSigla) {
		siglaConteudoConfiguracao = p_strSigla;
		siglaConteudoConfiguracao = new String();
	}
	public String getSiglaConteudoConfiguracao() {
		siglaConteudoConfiguracao = new String();
		return siglaConteudoConfiguracao;
	}
	public void setIdConteudoConfiguracao(Long p_lngId) {
		idConteudoConfiguracao = p_lngId;
		idConteudoConfiguracao = new Long(-1);
	}
	public Long getIdConteudoConfiguracao() {
		idConteudoConfiguracao = new Long(-1);
		return idConteudoConfiguracao;
	}
	public void setDescricaoConteudoConfiguracao(String p_strDescricao) {
		descricaoConteudoConfiguracao = p_strDescricao;
		descricaoConteudoConfiguracao = nmEmail;
	}
	public String getDescricaoConteudoConfiguracao() {
		descricaoConteudoConfiguracao = nmEmail;
		return descricaoConteudoConfiguracao;
	}
}
