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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpSituacaoConfiguracao extends Objeto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_SIT_CONFIGURACAO", nullable = false)
	private Long idSitConfiguracao;
	@Column(name = "DSC_SIT_CONFIGURACAO")
	private String dscSitConfiguracao;
	@Column(name = "RESTRITIVIDADE_SIT_CONF")
	private Long restritividadeSitConfiguracao;
	//private Set<CpTipoConfiguracao> cpTiposConfiguracaoSet;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CP_TIPO_SERVICO_SITUACAO", schema = "CORPORATIVO", joinColumns = { @JoinColumn(name = "ID_SIT_CONFIGURACAO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_TP_SERVICO", nullable = false, updatable = false) })
	private Set<CpTipoServico> cpTiposServicoSet;

	public Long getIdSitConfiguracao() {
		return idSitConfiguracao;
	}

	public void setIdSitConfiguracao(Long idConfiguracao) {
		this.idSitConfiguracao = idConfiguracao;
	}

	public String getDscSitConfiguracao() {
		return dscSitConfiguracao;
	}

	public void setDscSitConfiguracao(String dscSitConfiguracao) {
		this.dscSitConfiguracao = dscSitConfiguracao;
	}

	public Long getRestritividadeSitConfiguracao() {
		return restritividadeSitConfiguracao;
	}

	public void setRestritividadeSitConfiguracao(
			Long restritividadeSitConfiguracao) {
		this.restritividadeSitConfiguracao = restritividadeSitConfiguracao;
	}

	/**
	 * @return the cpTiposServicoSet
	 */
	public Set<CpTipoServico> getCpTiposServicoSet() {
		return cpTiposServicoSet;
	}

	/**
	 * @param cpTiposServicoSet
	 *            the cpTiposServicoSet to set
	 */
	public void setCpTiposServicoSet(Set<CpTipoServico> cpTiposServicoSet) {
		this.cpTiposServicoSet = cpTiposServicoSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((getIdSitConfiguracao() == null) ? 0
						: getIdSitConfiguracao().hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractCpSituacaoConfiguracao)) {
			return false;
		}
		AbstractCpSituacaoConfiguracao other = (AbstractCpSituacaoConfiguracao) obj;
		if (getIdSitConfiguracao() == null) {
			if (other.getIdSitConfiguracao() != null) {
				return false;
			}
		} else if (!getIdSitConfiguracao().equals(other.getIdSitConfiguracao())) {
			return false;
		}
		return true;
	}

}
