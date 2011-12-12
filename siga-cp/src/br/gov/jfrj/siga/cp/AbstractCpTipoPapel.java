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
package br.gov.jfrj.siga.cp;

public class AbstractCpTipoPapel {
	private Integer idCpTpPapel;
	private String dscTpPapel;
	/**
	 * @return the idCpTpPapel
	 */
	public Integer getIdCpTpPapel() {
		return idCpTpPapel;
	}
	/**
	 * @param idCpTpPapel the idCpTpPapel to set
	 */
	public void setIdCpTpPapel(Integer idCpTpPapel) {
		this.idCpTpPapel = idCpTpPapel;
	}
	/**
	 * @return the dscTpPapel
	 */
	public String getDscTpPapel() {
		return dscTpPapel;
	}
	/**
	 * @param dscTpPapel the dscTpPapel to set
	 */
	public void setDscTpPapel(String dscTpPapel) {
		this.dscTpPapel = dscTpPapel;
	}
}
