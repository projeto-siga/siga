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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractExSituacaoConfiguracao extends Objeto implements
		Serializable {

	@Id
	@Column(name = "ID_SIT_CONFIGURACAO", unique = true, nullable = false)
	private Long idSitConfiguracao;

	@Column(name = "DSC_SIT_CONFIGURACAO", length = 256)
	private String dscSitConfiguracao;

	public Long getIdSitConfiguracao() {
		return idSitConfiguracao;
	}

	public void setIdSitConfiguracao(Long idConfiguracao) {
		this.idSitConfiguracao = idConfiguracao;
	}

	public String getDscSitConfiguracao() {
		return dscSitConfiguracao;
	}

	public void setDscSitConfiguracao(String dscSitConfiguracao) {
		this.dscSitConfiguracao = dscSitConfiguracao;
	}

}
