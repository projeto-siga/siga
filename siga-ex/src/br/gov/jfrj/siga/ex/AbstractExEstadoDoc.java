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
import java.util.Set;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_ESTADO_DOC table. You can customize
 * the behavior of this class by editing the class, {@link ExEstadoDoc()}.
 */
public abstract class AbstractExEstadoDoc extends Objeto implements Serializable {
	/** The value of the simple descEstadoDoc property. */
	private java.lang.String descEstadoDoc;

	private Set deTpMovSet;

	private Set exMovimentacaoSet;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private java.lang.Long idEstadoDoc;

	private Set paraTpMovSet;

	/**
	 * Simple constructor of AbstractExEstadoDoc instances.
	 */
	public AbstractExEstadoDoc() {
	}

	/**
	 * Constructor of AbstractExEstadoDoc instances given a simple primary key.
	 * 
	 * @param idEstadoDoc
	 */
	public AbstractExEstadoDoc(final java.lang.Long idEstadoDoc) {
		this.setIdEstadoDoc(idEstadoDoc);
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
		if ((rhs == null) || !(rhs instanceof ExEstadoDoc))
			return false;
		final ExEstadoDoc that = (ExEstadoDoc) rhs;

		if ((this.getIdEstadoDoc() == null ? that.getIdEstadoDoc() == null : this.getIdEstadoDoc().equals(
				that.getIdEstadoDoc())))
			return true;
		return false;

	}

	/**
	 * Return the value of the DESC_ESTADO_DOC column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescEstadoDoc() {
		return this.descEstadoDoc;
	}

	public Set getDeTpMovSet() {
		return deTpMovSet;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdEstadoDoc() {
		return idEstadoDoc;
	}

	public Set getParaTpMovSet() {
		return paraTpMovSet;
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
		final int idValue = this.getIdEstadoDoc() == null ? 0 : this.getIdEstadoDoc().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;
	}

	/**
	 * Set the value of the DESC_ESTADO_DOC column.
	 * 
	 * @param descEstadoDoc
	 */
	public void setDescEstadoDoc(final java.lang.String descEstadoDoc) {
		this.descEstadoDoc = descEstadoDoc;
	}

	public void setDeTpMovSet(final Set deTpMovSet) {
		this.deTpMovSet = deTpMovSet;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idEstadoDoc
	 */
	public void setIdEstadoDoc(final java.lang.Long idEstadoDoc) {
		this.hashValue = 0;
		this.idEstadoDoc = idEstadoDoc;
	}

	public void setParaTpMovSet(final Set paraTpMovSet) {
		this.paraTpMovSet = paraTpMovSet;
	}

	public Set getExMovimentacaoSet() {
		return exMovimentacaoSet;
	}

	public void setExMovimentacaoSet(final Set exMovimentacaoSet) {
		this.exMovimentacaoSet = exMovimentacaoSet;
	}
}
