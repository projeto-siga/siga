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
 * Criado em  12/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.model.Objeto;

public abstract class AbstractExLocalidadePublicacao extends Objeto implements Serializable {

	private Integer idLocalidadePublicacao;
	
	private CpLocalidade localidade;
	
	private Integer codLocalidadePublicacao;

	public CpLocalidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(CpLocalidade localidade) {
		this.localidade = localidade;
	}

	public Integer getCodLocalidadePublicacao() {
		return codLocalidadePublicacao;
	}

	public void setCodLocalidadePublicacao(Integer codLocalidadePublicacao) {
		this.codLocalidadePublicacao = codLocalidadePublicacao;
	}

	public Integer getIdLocalidade() {
		return idLocalidadePublicacao;
	}

	public void setIdLocalidade(Integer idLocalidade) {
		this.idLocalidadePublicacao = idLocalidade;
	}

}
