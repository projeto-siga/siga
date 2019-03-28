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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "listarOrdemNivel", query = "from ExNivelAcesso as ena order by ena.grauNivelAcesso") })
public abstract class AbstractExNivelAcesso extends Objeto implements
		Serializable {

	/** The composite primary key value. */
	@Id
	@Column(name = "ID_NIVEL_ACESSO", unique = true, nullable = false)
	private Long idNivelAcesso;

	@Column(name = "NM_NIVEL_ACESSO", nullable = false, length = 50)
	private String nmNivelAcesso;

	@Column(name = "DSC_NIVEL_ACESSO", length = 256)
	private java.lang.String dscNivelAcesso;

	@Column(name = "GRAU_NIVEL_ACESSO")
	private Integer grauNivelAcesso;

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
		return result * 37 + idValue;
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
