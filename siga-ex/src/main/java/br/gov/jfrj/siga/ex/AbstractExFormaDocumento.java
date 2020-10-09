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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Where;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_FORMA_DOCUMENTO table. You can
 * customize the behavior of this class by editing the class, {@link
 * ExFormaDocumento()}.
 */
@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "consultarSiglaForma", query = "from ExFormaDocumento o where o.siglaFormaDoc = :sigla") })
public abstract class AbstractExFormaDocumento extends Objeto implements
		Serializable {
	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_FORMA_DOCUMENTO_SEQ", name = "EX_FORMA_DOCUMENTO_SEQ")
	@GeneratedValue(generator = "EX_FORMA_DOCUMENTO_SEQ")
	@Column(name = "ID_FORMA_DOC", unique = true, nullable = false)
	private Long idFormaDoc;

	/** The value of the simple descrFormaDoc property. */
	@Column(name = "DESCR_FORMA_DOC", nullable = false, length = 64)
	private String descrFormaDoc;

	/** The value of the exModeloSet one-to-many association. */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exFormaDocumento")
	@OrderBy(value="nmMod")
	@Where(clause="HIS_ATIVO = 1")
	private Set<ExModelo> exModeloSet;

	@ManyToMany
	@JoinTable(name = "siga.ex_tp_forma_doc", joinColumns = { @JoinColumn(name = "ID_FORMA_DOC") }, inverseJoinColumns = { @JoinColumn(name = "ID_TP_DOC") })
	@OrderBy(value="idTpDoc")
	private Set<ExTipoDocumento> exTipoDocumentoSet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_FORMA_DOC", nullable = false)
	private ExTipoFormaDoc exTipoFormaDoc;

	@Column(name = "SIGLA_FORMA_DOC", nullable = false, length = 3)
	private String siglaFormaDoc;

	@Column(name = "IS_COMPOSTO", length = 1)
	private Integer isComposto;

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

		if ((this.getIdFormaDoc() == null ? that.getIdFormaDoc() == null : this
				.getIdFormaDoc().equals(that.getIdFormaDoc())))
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
		return exTipoDocumentoSet;
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

	public Integer getIsComposto() {
		return isComposto;
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
		final int idValue = this.getIdFormaDoc() == null ? 0 : this
				.getIdFormaDoc().hashCode();
		return result * 37 + idValue;

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

	public void setExTipoDocumentoSet(
			final Set<ExTipoDocumento> exTipoDocumentoSet) {
		this.exTipoDocumentoSet = exTipoDocumentoSet;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idFormaDoc
	 */
	public void setIdFormaDoc(final Long idFormaDoc) {
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

	public void setIsComposto(final java.lang.Integer isComposto) {
		this.isComposto = isComposto;
	}

}
