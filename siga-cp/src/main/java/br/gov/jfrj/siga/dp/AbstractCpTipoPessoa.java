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
package br.gov.jfrj.siga.dp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpTipoPessoa extends Objeto{
	
	@Id
	@Column(name = "ID_TP_PESSOA", unique = true, nullable = false)
	private Integer idTpPessoa;
	
	@Column(name = "DESC_TP_PESSOA", length = 60)
	private String dscTpPessoa;
	/**
	 * @return the idTpPessoa
	 */
	public Integer getIdTpPessoa() {
		return idTpPessoa;
	}
	/**
	 * @param idTpPessoa the idTpPessoa to set
	 */
	public void setIdTpPessoa(Integer idTpPessoa) {
		this.idTpPessoa = idTpPessoa;
	}
	/**
	 * @return the dscTpPessoa
	 */
	public String getDscTpPessoa() {
		return dscTpPessoa;
	}
	/**
	 * @param dscTpPessoa the dscTpPessoa to set
	 */
	public void setDscTpPessoa(String dscTpPessoa) {
		this.dscTpPessoa = dscTpPessoa;
	}
}
