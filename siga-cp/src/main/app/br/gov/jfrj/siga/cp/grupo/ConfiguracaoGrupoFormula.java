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
 *  Configuração de grupo para uma fórmula 
 */
public class ConfiguracaoGrupoFormula extends ConfiguracaoGrupo {
	private String dscFormula;
	
	/**
	 * @return the dscFormula
	 */
	public String getDscFormula() {
		return dscFormula;
	}

	/**
	 * @param dscFormula the dscFormula to set
	 */
	public void setDscFormula(String dscFormula) {
		this.dscFormula = dscFormula;
	}

	@Override
	public void atualizarCpConfiguracao() {
		getCpConfiguracao().setCpGrupo(getCpGrupo());
		getCpConfiguracao().setDscFormula(getDscFormula());
	}

	@Override
	public void atualizarDeCpConfiguracao() {
		setCpGrupo(getCpConfiguracao().getCpGrupo());
		setDscFormula(getCpConfiguracao().getDscFormula());
	}

	@Override
	public TipoConfiguracaoGrupoEnum obterTipoPadrao() {
		return TipoConfiguracaoGrupoEnum.FORMULA;
	}
	public void setConteudoConfiguracao(String p_strConteudo) {
		setDscFormula(p_strConteudo);
	}
	public String getConteudoConfiguracao() {
		return getDscFormula();
	}
	public void setSiglaConteudoConfiguracao(String p_strSigla) {
		siglaConteudoConfiguracao =  new String();
	}
	public String getSiglaConteudoConfiguracao() {
		siglaConteudoConfiguracao =  new String();
		return siglaConteudoConfiguracao;
	}
	public void setIdConteudoConfiguracao(Long p_lngId) {
		idConteudoConfiguracao = p_lngId;
		idConteudoConfiguracao =  new Long(-1);
	}
	public Long getIdConteudoConfiguracao() {
		idConteudoConfiguracao =  new Long(-1);
		return idConteudoConfiguracao;
	}
	public void setDescricaoConteudoConfiguracao(String p_strDescricao) {
		descricaoConteudoConfiguracao = p_strDescricao;
		descricaoConteudoConfiguracao = dscFormula;
	}
	public String getDescricaoConteudoConfiguracao() {
		descricaoConteudoConfiguracao = dscFormula;
		return descricaoConteudoConfiguracao;
	}
}
