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
 * Created Mon Nov 14 13:33:06 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

/**
 * A class that represents a row in the EX_TEMPORALIDADE table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExTemporalidade()}.
 */
public abstract class AbstractExTemporalidade implements Serializable {
	/** The value of the simple descTemporalidade property. */
	private java.lang.String descTemporalidade;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private java.lang.Short idTemporalidade;

	/** The value of the simple permanenciaArquivo property. */
	private java.lang.Integer permanenciaArquivo;

	/**
	 * Simple constructor of AbstractExTemporalidade instances.
	 */
	public AbstractExTemporalidade() {
	}

	/**
	 * Constructor of AbstractExTemporalidade instances given a simple primary
	 * key.
	 * 
	 * @param idTemporalidade
	 */
	public AbstractExTemporalidade(final java.lang.Short idTemporalidade) {
		this.setIdTemporalidade(idTemporalidade);
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
		if ((rhs == null) || !(rhs instanceof ExTemporalidade))
			return false;
		final ExTemporalidade that = (ExTemporalidade) rhs;
		if ((this.getIdTemporalidade() == null ? that.getIdTemporalidade() == null : this.getIdTemporalidade().equals(
				that.getIdTemporalidade()))) {
			if ((this.getDescTemporalidade() == null ? that.getDescTemporalidade() == null : this
					.getDescTemporalidade().equals(that.getDescTemporalidade())))
				return true;
		}
		return false;
	}

	/**
	 * Return the value of the DESC_TEMPORALIDADE column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescTemporalidade() {
		return this.descTemporalidade;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Short
	 */
	public java.lang.Short getIdTemporalidade() {
		return idTemporalidade;
	}

	/**
	 * Return the value of the PERMANENCIA_ARQUIVO column.
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getPermanenciaArquivo() {
		return this.permanenciaArquivo;
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdTemporalidade() == null ? 0 : this.getIdTemporalidade().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescTemporalidade() == null ? 0 : this.getDescTemporalidade().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;
	}

	/**
	 * Set the value of the DESC_TEMPORALIDADE column.
	 * 
	 * @param descTemporalidade
	 */
	public void setDescTemporalidade(final java.lang.String descTemporalidade) {
		this.descTemporalidade = descTemporalidade;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idTemporalidade
	 */
	public void setIdTemporalidade(final java.lang.Short idTemporalidade) {
		this.hashValue = 0;
		this.idTemporalidade = idTemporalidade;
	}

	/**
	 * Set the value of the PERMANENCIA_ARQUIVO column.
	 * 
	 * @param permanenciaArquivo
	 */
	public void setPermanenciaArquivo(final java.lang.Integer permanenciaArquivo) {
		this.permanenciaArquivo = permanenciaArquivo;
	}
}
