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

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpGrupo;
/**
 *  Uma configuração de grupo é a representação de uma configuração (CpConfiguracao) 
 *  com o objetivo específico de configurar um grupo, seja ele grupo de email
 *  , grupo de acesso a um recurso ou programa, etc.
 */
public abstract class ConfiguracaoGrupo {
	private CpConfiguracao cpConfiguracao ;
	private CpGrupo cpGrupo;
	private TipoConfiguracaoGrupoEnum tipo;
	// apenas para uso em EL 
	protected String conteudoConfiguracao;
	protected String siglaConteudoConfiguracao;
	protected Long idConteudoConfiguracao;
	protected String descricaoConteudoConfiguracao;
	//
	public ConfiguracaoGrupo() {
		tipo = obterTipoPadrao();
	}
	/**
	 * @return the cpConfiguracao
	 */
	public CpConfiguracao getCpConfiguracao() {
		return cpConfiguracao;
	}

	/**
	 * @param cpConfiguracao the cpConfiguracao to set
	 */
	public void setCpConfiguracao(CpConfiguracao cpConfiguracao) {
		this.cpConfiguracao = cpConfiguracao;
	}

	/**
	 * @return the cpGrupo
	 */
	public CpGrupo getCpGrupo() {
		return cpGrupo;
	}

	/**
	 * @param cpGrupo the cpGrupo to set
	 */
	public void setCpGrupo(CpGrupo cpGrupo) {
		this.cpGrupo = cpGrupo;
	}
	/**
	 * atualizar os atributos da configuração (cpConfiguracao) a partir dos atributos da instância
	*/
	public abstract void atualizarCpConfiguracao() ;
	/**
	 * atualizar dos atributos da instância a partir dos atributos da configuração (cpConfiguracao) 
	*/
	public abstract void atualizarDeCpConfiguracao();
	/**
	 * @return the tipo
	 */
	public TipoConfiguracaoGrupoEnum getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoConfiguracaoGrupoEnum tipo) {
		this.tipo = tipo;
	}

	public abstract void setConteudoConfiguracao(String p_strConteudo) ;
	/**
	 * Retorna o conteúdo da configuração 
	 * Dependendo do tipo da configuração, pode ser um id, um e-mail, uma fórmula, etc. 
	 * 
	 * @return 	String o conteúdo da configuração
	 */
	public abstract String getConteudoConfiguracao() ;
	/**
	 * Retorna o conteúdo da configuração 
	 * Dependendo do tipo da configuração, pode ser um id, um e-mail, uma fórmula, etc. 
	 * 
	 * @return 	String o conteúdo da configuração
	 */
	public abstract String getSiglaConteudoConfiguracao() ;
	public abstract void setSiglaConteudoConfiguracao(String p_strConteudo) ;
	/**
	 * Retorna a sigla do conteudo de uma configuração
	 * Dependendo do tipo da configuração, é a sigla da pessoa, da configuração , etc. 
	 * 
	 * @return 	String a sigla do conteúdo da configuracao 
	 */
	public abstract Long getIdConteudoConfiguracao() ;
	public abstract void setIdConteudoConfiguracao(Long p_lngId) ;
	/**
	 * Retorna o id do conteudo de uma configuração
	 * Dependendo do tipo da configuração, é a descrição da pessoa, da configuração , etc. 
	 * 
	 * @return 	String a descrição do conteúdo da configuracao 
	 */
	public abstract String getDescricaoConteudoConfiguracao();
	public abstract void setDescricaoConteudoConfiguracao(String p_strDescricao);
	/**
	 * Retorna o id do conteudo de uma configuração
	 * Dependendo do tipo da configuração, é o id da pessoa, da configuração , etc. 
	 * 
	 * @return 	Long o id do conteúdo da configuracao 
	 */
	public abstract TipoConfiguracaoGrupoEnum obterTipoPadrao();
}
