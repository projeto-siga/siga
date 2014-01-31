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
import java.util.Set;

import br.gov.jfrj.siga.model.Objeto;

public abstract class AbstractExTpDocPublicacao extends Objeto implements Serializable {

	private Long idDocPublicacao;

	private String nmDocPublicacao;

	private Set<ExModelo> exModeloSet;
	
	private String carater;
	
	public Long getIdDocPublicacao() {
		return idDocPublicacao;
	}

	public void setIdDocPublicacao(Long idDocPublicacao) {
		this.idDocPublicacao = idDocPublicacao;
	}

	public String getNmDocPublicacao() {
		return nmDocPublicacao;
	}

	public void setNmDocPublicacao(String nmDocPublicacao) {
		this.nmDocPublicacao = nmDocPublicacao;
	}

	public Set<ExModelo> getExModeloSet() {
		return exModeloSet;
	}

	public void setExModeloSet(final Set<ExModelo> exModeloSet) {
		this.exModeloSet = exModeloSet;
	}
	
	public String getCarater() {
		return carater;
	}

	public void setCarater(String carater) {
		this.carater = carater;
	}

}
