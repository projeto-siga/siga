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


/**
 * Classe que representa uma linha na tabela DP_CARGO. Você pode customizar o
 * comportamento desta classe editando a classe {@link DpCargo}.
 */
public abstract class AbstractCpLocalidade implements Serializable {

	private Long idLocalidade;

	private String nmLocalidade;
	
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
