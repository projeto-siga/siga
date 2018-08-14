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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractExTipoMobil extends Objeto implements
		Serializable {

	@Id
	@SequenceGenerator(sequenceName = "EX_TIPO_MOBIL_SEQ", name = "EX_TIPO_MOBIL_SEQ")
	@GeneratedValue(generator = "EX_TIPO_MOBIL_SEQ")
	@Column(name = "ID_TIPO_MOBIL", unique = true, nullable = false)
	private java.lang.Long idTipoMobil;

	@Column(name = "desc_tipo_mobil", length = 20)
	private java.lang.String descTipoMobil;

	public java.lang.Long getIdTipoMobil() {
		return idTipoMobil;
	}

	public void setIdTipoMobil(java.lang.Long idTipoMobil) {
		this.idTipoMobil = idTipoMobil;
	}

	public java.lang.String getDescTipoMobil() {
		return descTipoMobil;
	}

	public void setDescTipoMobil(java.lang.String descTipoMobil) {
		this.descTipoMobil = descTipoMobil;
	}

}
