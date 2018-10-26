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
package br.gov.jfrj.siga.cp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public class AbstractCpTipoIdentidade extends Objeto {

	@Id
	@Column(name = "ID_TP_IDENTIDADE", unique = true, nullable = false)
	private Integer idCpTpIdentidade;

	@Column(name = "DESC_TP_IDENTIDADE", length = 60)
	private String dscCpTpIdentidade;

	/**
	 * @return the idCpTpIdentidade
	 */
	public Integer getIdCpTpIdentidade() {
		return idCpTpIdentidade;
	}

	/**
	 * @param idCpTpIdentidade
	 *            the idCpTpIdentidade to set
	 */
	public void setIdCpTpIdentidade(Integer idCpTpIdentidade) {
		this.idCpTpIdentidade = idCpTpIdentidade;
	}

	/**
	 * @return the dscTpIdentidade
	 */
	public String getDscCpTpIdentidade() {
		return dscCpTpIdentidade;
	}

	/**
	 * @param dscTpIdentidade
	 *            the dscTpIdentidade to set
	 */
	public void setDscCpTpIdentidade(String dscCpTpIdentidade) {
		this.dscCpTpIdentidade = dscCpTpIdentidade;
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
				+ ((dscCpTpIdentidade == null) ? 0 : dscCpTpIdentidade
						.hashCode());
		result = prime
				* result
				+ ((idCpTpIdentidade == null) ? 0 : idCpTpIdentidade.hashCode());
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
		if (!(obj instanceof AbstractCpTipoIdentidade)) {
			return false;
		}
		AbstractCpTipoIdentidade other = (AbstractCpTipoIdentidade) obj;
		if (dscCpTpIdentidade == null) {
			if (other.dscCpTpIdentidade != null) {
				return false;
			}
		} else if (!dscCpTpIdentidade.equals(other.dscCpTpIdentidade)) {
			return false;
		}
		if (idCpTpIdentidade == null) {
			if (other.idCpTpIdentidade != null) {
				return false;
			}
		} else if (!idCpTpIdentidade.equals(other.idCpTpIdentidade)) {
			return false;
		}
		return true;
	}
}
