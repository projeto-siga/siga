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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_TIPO_DOCUMENTO table. You can
 * customize the behavior of this class by editing the class, {@link
 * ExTipoDocumento()}.
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractExTipoDocumento extends Objeto implements
		Serializable {
	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_TIPO_DOCUMENTO_SEQ", name = "EX_TIPO_DOCUMENTO_SEQ")
	@GeneratedValue(generator = "EX_TIPO_DOCUMENTO_SEQ")
	@Column(name = "ID_TP_DOC", unique = true, nullable = false)
	private java.lang.Long idTpDoc;

	/** The value of the simple descrTipoDocumento property. */
	@Column(name = "descr_tipo_documento", nullable = false, length = 256)
	private java.lang.String descrTipoDocumento;

	@ManyToMany(mappedBy = "exTipoDocumentoSet")
	// @JoinTable(name = "EX_TP_FORMA_DOC", joinColumns = { @JoinColumn(name =
	// "ID_TP_DOC") }, inverseJoinColumns = { @JoinColumn(name = "ID_FORMA_DOC")
	// })
	// @OrderBy(value = "DESCR_FORMA_DOC")
	private Set<ExFormaDocumento> ExFormaDocumentoSet;

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
		if ((this.getIdTpDoc() == null ? that.getIdTpDoc() == null : this
				.getIdTpDoc().equals(that.getIdTpDoc()))) {
			if ((this.getDescrTipoDocumento() == null ? that
					.getDescrTipoDocumento() == null : this
					.getDescrTipoDocumento().equals(
							that.getDescrTipoDocumento())))
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
		int idValue = this.getIdTpDoc() == null ? 0 : this.getIdTpDoc()
				.hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescrTipoDocumento() == null ? 0 : this
				.getDescrTipoDocumento().hashCode();
		return result * 37 + idValue;
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
		this.idTpDoc = idTpDoc;
	}

}
