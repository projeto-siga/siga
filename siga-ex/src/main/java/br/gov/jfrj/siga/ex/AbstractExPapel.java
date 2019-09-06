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
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractExPapel extends Objeto implements Serializable {

	@Id
	@Column(name = "ID_PAPEL", unique = true, nullable = false)
	private java.lang.Long idPapel;

	@Column(name = "DESC_PAPEL", length = 20)
	private java.lang.String descPapel;

	public java.lang.Long getIdPapel() {
		return idPapel;
	}

	public void setIdPapel(java.lang.Long idPapel) {
		this.idPapel = idPapel;
	}

	public java.lang.String getDescPapel() {
		return descPapel;
	}

	public void setDescPapel(java.lang.String descPapel) {
		this.descPapel = descPapel;
	}

}
