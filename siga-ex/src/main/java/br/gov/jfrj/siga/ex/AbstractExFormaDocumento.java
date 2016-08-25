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
 * A class that represents a row in the EX_FORMA_DOCUMENTO table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExFormaDocumento()}.
 */
public abstract class AbstractExFormaDocumento extends Objeto implements Serializable {
	/** The value of the simple descrFormaDoc property. */
	private String descrFormaDoc;

	/** The value of the exModeloSet one-to-many association. */
	private Set<ExModelo> exModeloSet;

	private Set ExTipoDocumentoSet;
	
	private ExTipoFormaDoc exTipoFormaDoc;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private Long idFormaDoc;

	private String siglaFormaDoc;

	/**
	 * Simple constructor of AbstractExFormaDocumento instances.
	 */
	public AbstractExFormaDocumento() {
	}

	/**
	 * Constructor of AbstractExFormaDocumento instances given a simple primary
	 * key.
	 * 
	 * @param idFormaDoc
	 */
	public AbstractExFormaDocumento(final java.lang.Long idFormaDoc) {
		this.setIdFormaDoc(idFormaDoc);
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
		if ((rhs == null) || !(rhs instanceof ExFormaDocumento))
			return false;
		final ExFormaDocumento that = (ExFormaDocumento) rhs;

		if ((this.getIdFormaDoc() == null ? that.getIdFormaDoc() == null : this.getIdFormaDoc().equals(
				that.getIdFormaDoc())))
			return true;
		return false;
	}

	/**
	 * Return the value of the DESCR_FORMA_DOC column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescrFormaDoc() {
		return this.descrFormaDoc;
	}

	/**
	 * Return the value of the exModeloSet.
	 * 
	 * @return Set
	 */
	public java.util.Set<ExModelo> getExModeloSet() {
		return this.exModeloSet;
	}

	public Set<ExTipoDocumento> getExTipoDocumentoSet() {
		return ExTipoDocumentoSet;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Short
	 */
	public java.lang.Long getIdFormaDoc() {
		return idFormaDoc;
	}

	public String getSiglaFormaDoc() {
		return siglaFormaDoc;
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
		final int idValue = this.getIdFormaDoc() == null ? 0 : this.getIdFormaDoc().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;

	}

	/**
	 * Set the value of the DESCR_FORMA_DOC column.
	 * 
	 * @param descrFormaDoc
	 */
	public void setDescrFormaDoc(final String descrFormaDoc) {
		this.descrFormaDoc = descrFormaDoc;
	}
	
	/**
	 * Set the value of the ID_FORMA_DOC collection.
	 * 
	 * @param exModeloSet
	 */
	public void setExModeloSet(final Set<ExModelo> exModeloSet) {
		this.exModeloSet = exModeloSet;
	}

	public void setExTipoDocumentoSet(final Set<ExTipoDocumento> exTipoDocumentoSet) {
		ExTipoDocumentoSet = exTipoDocumentoSet;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idFormaDoc
	 */
	public void setIdFormaDoc(final Long idFormaDoc) {
		this.hashValue = 0;
		this.idFormaDoc = idFormaDoc;
	}

	public void setSiglaFormaDoc(final String siglaFormaDoc) {
		this.siglaFormaDoc = siglaFormaDoc;
	}

	public ExTipoFormaDoc getExTipoFormaDoc() {
		return exTipoFormaDoc;
	}

	public void setExTipoFormaDoc(ExTipoFormaDoc exTipoFormaDoc) {
		this.exTipoFormaDoc = exTipoFormaDoc;
	}
}
