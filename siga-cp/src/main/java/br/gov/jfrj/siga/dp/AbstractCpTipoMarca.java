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
 * Criado em  21/12/2005
 *
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpTipoMarca extends Objeto implements
		Serializable {

	@Id
	@Column(name = "ID_TP_MARCA", unique = true, nullable = false)
	private Long idTpMarca;

	@Column(name = "DESCR_TP_MARCA", length = 30)
	private String descrTipoMarca;

	public Long getIdTpMarca() {
		return idTpMarca;
	}

	public void setIdTpMarca(Long idTpMarca) {
		this.idTpMarca = idTpMarca;
	}

	public String getDescrTipoMarca() {
		return descrTipoMarca;
	}

	public void setDescrTipoMarca(String descrTipoMarca) {
		this.descrTipoMarca = descrTipoMarca;
	}
}
