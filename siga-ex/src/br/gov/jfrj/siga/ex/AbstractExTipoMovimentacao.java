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
 * Created Mon Nov 14 13:35:21 GMT-03:00 2005
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Set;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_TIPO_MOVIMENTACAO table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExTipoMovimentacao()}.
 */
public abstract class AbstractExTipoMovimentacao extends Objeto implements Serializable {
	private Set<ExEstadoDoc> deEstadoDocSet;

	/** The value of the simple descrTipoMovimentacao property. */
	private java.lang.String descrTipoMovimentacao;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private java.lang.Long idTpMov;

	private Set<ExEstadoDoc> paraEstadoDocSet;

	/**
	 * Simple constructor of AbstractExTipoMovimentacao instances.
	 */
	public AbstractExTipoMovimentacao() {
	}

	/**
	 * Constructor of AbstractExTipoMovimentacao instances given a simple
	 * primary key.
	 * 
	 * @param idTpMov
	 */
	public AbstractExTipoMovimentacao(final java.lang.Long idTpMov) {
		this.setIdTpMov(idTpMov);
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
		if ((rhs == null) || !(rhs instanceof ExTipoMovimentacao))
			return false;
		final ExTipoMovimentacao that = (ExTipoMovimentacao) rhs;
		// se o codLotacao for nulo e o outro tambem
		if ((this.getIdTpMov() == null ? that.getIdTpMov() == null : this.getIdTpMov().equals(that.getIdTpMov()))) {
			if ((this.getDescrTipoMovimentacao() == null ? that.getDescrTipoMovimentacao() == null : this
					.getDescrTipoMovimentacao().equals(that.getDescrTipoMovimentacao())))
				return true;

		}
		return false;
	}

	public Set<ExEstadoDoc> getDeEstadoDocSet() {
		return deEstadoDocSet;
	}

	/**
	 * Return the value of the DESCR_TIPO_MOVIMENTACAO column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescrTipoMovimentacao() {
		return this.descrTipoMovimentacao;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdTpMov() {
		return idTpMov;
	}

	public Set<ExEstadoDoc> getParaEstadoDocSet() {
		return paraEstadoDocSet;
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
		int idValue = this.getIdTpMov() == null ? 0 : this.getIdTpMov().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescrTipoMovimentacao() == null ? 0 : this.getDescrTipoMovimentacao().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;
	}

	public void setDeEstadoDocSet(final Set<ExEstadoDoc> deEstadoDocSet) {
		this.deEstadoDocSet = deEstadoDocSet;
	}

	/**
	 * Set the value of the DESCR_TIPO_MOVIMENTACAO column.
	 * 
	 * @param descrTipoMovimentacao
	 */
	public void setDescrTipoMovimentacao(final java.lang.String descrTipoMovimentacao) {
		this.descrTipoMovimentacao = descrTipoMovimentacao;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idTpMov
	 */
	public void setIdTpMov(final java.lang.Long idTpMov) {
		this.idTpMov = idTpMov;
	}

	public void setParaEstadoDocSet(final Set<ExEstadoDoc> paraEstadoDocSet) {
		this.paraEstadoDocSet = paraEstadoDocSet;
	}
}
