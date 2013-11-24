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
 */

package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import br.gov.jfrj.siga.model.Objeto;

public abstract class AbstractExNivelAcesso extends Objeto implements Serializable {

	private java.lang.String dscNivelAcesso;

	private String nmNivelAcesso;

	private Integer grauNivelAcesso;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private Long idNivelAcesso;

	public AbstractExNivelAcesso() {
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof ExNivelAcesso))
			return false;
		final AbstractExNivelAcesso that = (AbstractExNivelAcesso) rhs;
		if ((this.getIdNivelAcesso() == null ? that.getIdNivelAcesso() == null
				: this.getIdNivelAcesso().equals(that.getIdNivelAcesso()))) {
			if ((this.getDscNivelAcesso() == null ? that.getDscNivelAcesso() == null
					: this.getDscNivelAcesso().equals(that.getDscNivelAcesso())))
				return true;
		}
		return false;

	}

	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdNivelAcesso() == null ? 0 : this
				.getIdNivelAcesso().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDscNivelAcesso() == null ? 0 : this
				.getDscNivelAcesso().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;
	}

	public java.lang.String getDscNivelAcesso() {
		return dscNivelAcesso;
	}

	public void setDscNivelAcesso(java.lang.String dscNivelAcesso) {
		this.dscNivelAcesso = dscNivelAcesso;
	}

	public String getNmNivelAcesso() {
		return nmNivelAcesso;
	}

	public void setNmNivelAcesso(String nmNivelAcesso) {
		this.nmNivelAcesso = nmNivelAcesso;
	}

	public Integer getGrauNivelAcesso() {
		return grauNivelAcesso;
	}

	public void setGrauNivelAcesso(Integer grauNivelAcesso) {
		this.grauNivelAcesso = grauNivelAcesso;
	}

	public Long getIdNivelAcesso() {
		return idNivelAcesso;
	}

	public void setIdNivelAcesso(Long idNivelAcesso) {
		this.idNivelAcesso = idNivelAcesso;
	}
}
