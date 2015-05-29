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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpMarcador extends Objeto implements Serializable {

	@Id
	@Column(name = "ID_MARCADOR", nullable = false)
	private Long idMarcador;

	@Column(name = "DESCR_MARCADOR")
	private String descrMarcador;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_MARCADOR", nullable = false)
	private CpTipoMarcador cpTipoMarcador;

	public Long getIdMarcador() {
		return idMarcador;
	}

	public void setIdMarcador(Long idMarcador) {
		this.idMarcador = idMarcador;
	}

	public String getDescrMarcador() {
		return descrMarcador;
	}

	public void setDescrMarcador(String descrMarcador) {
		this.descrMarcador = descrMarcador;
	}

	public CpTipoMarcador getCpTipoMarcador() {
		return cpTipoMarcador;
	}

	public void setCpTipoMarcador(CpTipoMarcador cpTipoMarcador) {
		this.cpTipoMarcador = cpTipoMarcador;
	}

}
