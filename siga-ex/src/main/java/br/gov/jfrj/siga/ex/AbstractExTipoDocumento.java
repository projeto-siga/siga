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
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Set;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_TIPO_DOCUMENTO table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExTipoDocumento()}.
 */
public abstract class AbstractExTipoDocumento extends Objeto implements Serializable {
	/** The value of the simple descrTipoDocumento property. */
	private java.lang.String descrTipoDocumento;

	private Set<ExFormaDocumento> ExFormaDocumentoSet;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private java.lang.Long idTpDoc;

	/**
	 * Simple constructor of AbstractExTipoDocumento instances.
	 */
	public AbstractExTipoDocumento() {
	}

	/**
	 * Constructor of AbstractExTipoDocumento instances given a simple primary
	 * key.
	 * 
	 * @param idTpDoc
	 */
	public AbstractExTipoDocumento(final java.lang.Long idTpDoc) {
		this.setIdTpDoc(idTpDoc);
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
		if ((rhs == null) || !(rhs instanceof ExTipoDocumento))
			return false;
		final ExTipoDocumento that = (ExTipoDocumento) rhs;
		// se o codLotacao for nulo e o outro tambem
		if ((this.getIdTpDoc() == null ? that.getIdTpDoc() == null : this.getIdTpDoc().equals(that.getIdTpDoc()))) {
			if ((this.getDescrTipoDocumento() == null ? that.getDescrTipoDocumento() == null : this
					.getDescrTipoDocumento().equals(that.getDescrTipoDocumento())))
				return true;

		}
		return false;
	}

	/**
	 * Return the value of the DESCR_TIPO_DOCUMENTO column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescrTipoDocumento() {
		return this.descrTipoDocumento;
	}

	public Set<ExFormaDocumento> getExFormaDocumentoSet() {
		return ExFormaDocumentoSet;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdTpDoc() {
		return idTpDoc;
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
		int idValue = this.getIdTpDoc() == null ? 0 : this.getIdTpDoc().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescrTipoDocumento() == null ? 0 : this.getDescrTipoDocumento().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;
	}
	
	/**
	 * Set the value of the DESCR_TIPO_DOCUMENTO column.
	 * 
	 * @param descrTipoDocumento
	 */
	public void setDescrTipoDocumento(final java.lang.String descrTipoDocumento) {
		this.descrTipoDocumento = descrTipoDocumento;
	}

	public void setExFormaDocumentoSet(final Set exFormaDocumentoSet) {
		ExFormaDocumentoSet = exFormaDocumentoSet;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idTpDoc
	 */
	public void setIdTpDoc(final java.lang.Long idTpDoc) {
		this.hashValue = 0;
		this.idTpDoc = idTpDoc;
	}
	
	
}
