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
 * Criado em  12/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.cp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpTipoConfiguracao extends Objeto implements Serializable {

	@Id
	@Column(name = "ID_TP_CONFIGURACAO", unique = true, nullable = false)
	private Long idTpConfiguracao;
	
	@Column(name = "DSC_TP_CONFIGURACAO")
	private String dscTpConfiguracao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SIT_CONFIGURACAO")
	
	private CpSituacaoConfiguracao situacaoDefault;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cpTipoConfiguracao")
	//private Set<CpSituacaoConfiguracao> cpSituacoesConfiguracaoSet;

	/**
	 * @return the cpSituacoesConfiguracaoSet
	 */
	/*public Set<CpSituacaoConfiguracao> getCpSituacoesConfiguracaoSet() {
		return cpSituacoesConfiguracaoSet;
	}

	/**
	 * @param cpSituacoesConfiguracaoSet
	 *            the cpSituacoesConfiguracaoSet to set
	 */
	/*public void setCpSituacoesConfiguracaoSet(
			Set<CpSituacaoConfiguracao> cpSituacoesConfiguracaoSet) {
		this.cpSituacoesConfiguracaoSet = cpSituacoesConfiguracaoSet;
	}*/

	public Long getIdTpConfiguracao() {
		return idTpConfiguracao;
	}

	public void setIdTpConfiguracao(Long idTipoConfiguracao) {
		this.idTpConfiguracao = idTipoConfiguracao;
	}

	public String getDscTpConfiguracao() {
		return dscTpConfiguracao;
	}

	public void setDscTpConfiguracao(String dscTipoConfiguracao) {
		this.dscTpConfiguracao = dscTipoConfiguracao;
	}

	/**
	 * @return the situacaoDefault
	 */
	public CpSituacaoConfiguracao getSituacaoDefault() {
		return situacaoDefault;
	}

	/**
	 * @param situacaoDefault
	 *            the situacaoDefault to set
	 */
	public void setSituacaoDefault(CpSituacaoConfiguracao situacaoDefault) {
		this.situacaoDefault = situacaoDefault;
	}

}
