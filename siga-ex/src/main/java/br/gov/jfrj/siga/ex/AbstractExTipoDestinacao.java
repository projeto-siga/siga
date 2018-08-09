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
 * Created Mon Nov 14 13:33:07 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_TIPO_DESTINACAO table. You can
 * customize the behavior of this class by editing the class, {@link
 * ExTipoDestinacao()}.
 */
@MappedSuperclass
public abstract class AbstractExTipoDestinacao extends Objeto implements
		Serializable {
	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_TIPO_DESTINACAO_SEQ", name = "EX_TIPO_DESTINACAO_SEQ")
	@GeneratedValue(generator = "EX_TIPO_DESTINACAO_SEQ")
	@Column(name = "ID_TP_DESTINACAO", unique = true, nullable = false)
	private java.lang.Long idTpDestinacao;

	/** The value of the simple descrTipoDestinacao property. */
	@Column(name = "DESCR_TIPO_DESTINACAO", nullable = false, length = 256)
	private java.lang.String descrTipoDestinacao;

	/** The value of the exViaSet one-to-many association. */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exTipoDestinacao")
	private Set<ExVia> exViaSet;

	/** The value of the simple facilitadorDest property. */
	@Column(name = "FACILITADOR_DEST", length = 4000)
	private java.lang.String facilitadorDest;

	/**
	 * Simple constructor of AbstractExTipoDestinacao instances.
	 */
	public AbstractExTipoDestinacao() {
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
		if ((rhs == null) || !(rhs instanceof ExTipoDestinacao))
			return false;
		final ExTipoDestinacao that = (ExTipoDestinacao) rhs;
		if ((this.getIdTpDestinacao() == null ? that.getIdTpDestinacao() == null
				: this.getIdTpDestinacao().equals(that.getIdTpDestinacao()))) {
			if ((this.getDescrTipoDestinacao() == null ? that
					.getDescrTipoDestinacao() == null : this
					.getDescrTipoDestinacao().equals(
							that.getDescrTipoDestinacao())))
				return true;

		}
		return false;
	}

	/**
	 * Return the value of the DESCR_TIPO_DESTINACAO column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescrTipoDestinacao() {
		return this.descrTipoDestinacao;
	}

	/**
	 * Return the value of the ID_TP_DESTINACAO collection.
	 * 
	 * @return ExVia
	 */
	public java.util.Set getExViaSet() {
		return this.exViaSet;
	}

	/**
	 * Return the value of the FACILITADOR_DEST column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFacilitadorDest() {
		return this.facilitadorDest;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdTpDestinacao() {
		return idTpDestinacao;
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
		int idValue = this.getIdTpDestinacao() == null ? 0 : this
				.getIdTpDestinacao().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescrTipoDestinacao() == null ? 0 : this
				.getDescrTipoDestinacao().hashCode();
		return result * 37 + idValue;
	}

	/**
	 * Set the value of the DESCR_TIPO_DESTINACAO column.
	 * 
	 * @param descrTipoDestinacao
	 */
	public void setDescrTipoDestinacao(
			final java.lang.String descrTipoDestinacao) {
		this.descrTipoDestinacao = descrTipoDestinacao;
	}

	/**
	 * Set the value of the ID_TP_DESTINACAO collection.
	 * 
	 * @param exViaSet
	 */
	public void setExViaSet(final java.util.Set exViaSet) {
		this.exViaSet = exViaSet;
	}

	/**
	 * Set the value of the FACILITADOR_DEST column.
	 * 
	 * @param facilitadorDest
	 */
	public void setFacilitadorDest(final java.lang.String facilitadorDest) {
		this.facilitadorDest = facilitadorDest;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idTpDestinacao
	 */
	public void setIdTpDestinacao(final java.lang.Long idTpDestinacao) {
		this.idTpDestinacao = idTpDestinacao;
	}
}
