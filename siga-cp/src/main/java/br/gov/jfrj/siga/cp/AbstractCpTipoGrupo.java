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
public class AbstractCpTipoGrupo extends Objeto {

	@Id
	@Column(name = "ID_TP_GRUPO", unique = true, nullable = false)
	private Integer idTpGrupo;

	@Column(name = "DESC_TP_GRUPO", length = 200)
	private String dscTpGrupo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dscTpGrupo == null) ? 0 : dscTpGrupo.hashCode());
		result = prime * result
				+ ((idTpGrupo == null) ? 0 : idTpGrupo.hashCode());
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
		if (!(obj instanceof AbstractCpTipoGrupo)) {
			return false;
		}
		AbstractCpTipoGrupo other = (AbstractCpTipoGrupo) obj;
		if (dscTpGrupo == null) {
			if (other.dscTpGrupo != null) {
				return false;
			}
		} else if (!dscTpGrupo.equals(other.dscTpGrupo)) {
			return false;
		}
		if (idTpGrupo == null) {
			if (other.idTpGrupo != null) {
				return false;
			}
		} else if (!idTpGrupo.equals(other.idTpGrupo)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the idTpGrupo
	 */
	public Integer getIdTpGrupo() {
		return idTpGrupo;
	}

	/**
	 * @param idTpGrupo
	 *            the idTpGrupo to set
	 */
	public void setIdTpGrupo(Integer idTpGrupo) {
		this.idTpGrupo = idTpGrupo;
	}

	/**
	 * @return the dscTpGrupo
	 */
	public String getDscTpGrupo() {
		return dscTpGrupo;
	}

	/**
	 * @param dscTpGrupo
	 *            the dscTpGrupo to set
	 */
	public void setDscTpGrupo(String dscTpGrupo) {
		this.dscTpGrupo = dscTpGrupo;
	}
}
