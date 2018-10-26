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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpTipoMarcador extends Objeto implements
		Serializable {

	@Id
	@Column(name = "ID_TP_MARCADOR", unique = true, nullable = false)
	private Long idTpMarcador;

	@Column(name = "DESCR_TIPO_MARCADOR", length = 30)
	private String descrTipoMarcador;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cpTipoMarcador")
	private Set<CpMarcador> cpMarcadorSet;

	public Long getIdTpMarcador() {
		return idTpMarcador;
	}

	public void setIdTpMarcador(Long idTpMarcador) {
		this.idTpMarcador = idTpMarcador;
	}

	public String getDescrTipoMarcador() {
		return descrTipoMarcador;
	}

	public void setDescrTipoMarcador(String descrTipoMarcador) {
		this.descrTipoMarcador = descrTipoMarcador;
	}

	public Set<CpMarcador> getCpMarcadorSet() {
		return cpMarcadorSet;
	}

	public void setCpMarcadorSet(Set<CpMarcador> cpMarcadorSet) {
		this.cpMarcadorSet = cpMarcadorSet;
	}

}
