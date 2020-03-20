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
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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

/**
 * Classe que representa uma linha na tabela DP_CARGO. Você pode customizar o
 * comportamento desta classe editando a classe {@link DpCargo}.
 */

@MappedSuperclass
public abstract class AbstractCpLocalidade extends Objeto implements
		Serializable {

	@Id
	@Column(name = "ID_LOCALIDADE", unique = true, nullable = false)
	private Long idLocalidade;

	@Column(name = "NM_LOCALIDADE", nullable = false, length = 256)
	private String nmLocalidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UF", nullable = false)
	private CpUF UF;

	public Long getIdLocalidade() {
		return idLocalidade;
	}

	public void setIdLocalidade(Long idLocalidade) {
		this.idLocalidade = idLocalidade;
	}

	public String getNmLocalidade() {
		return nmLocalidade;
	}

	public void setNmLocalidade(String nmLocalidade) {
		this.nmLocalidade = nmLocalidade;
	}

	public CpUF getUF() {
		return UF;
	}

	public void setUF(CpUF uf) {
		UF = uf;
	}

}
