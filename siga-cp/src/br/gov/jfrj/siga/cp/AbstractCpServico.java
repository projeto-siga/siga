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
package br.gov.jfrj.siga.cp;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractCpServico implements Serializable {

	private Long idServico;
	private String siglaServico;
	private String dscServico;
	private CpServico cpServicoPai;
	private CpTipoServico cpTipoServico ;
	/**
	 * @return the cpServicoPai
	 */
	public CpServico getCpServicoPai() {
		return cpServicoPai;
	}

	/**
	 * @param cpServicoPai the cpServicoPai to set
	 */
	public void setCpServicoPai(CpServico cpServicoPai) {
		this.cpServicoPai = cpServicoPai;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public String getSiglaServico() {
		return siglaServico;
	}

	public void setSiglaServico(String siglaServico) {
		this.siglaServico = siglaServico;
	}

	public String getDscServico() {
		return dscServico;
	}

	public void setDscServico(String dscServico) {
		this.dscServico = dscServico;
	}

	/**
	 * @return the cpTipoServico
	 */
	public CpTipoServico getCpTipoServico() {
		return cpTipoServico;
	}

	/**
	 * @param cpTipoServico the cpTipoServico to set
	 */
	public void setCpTipoServico(CpTipoServico cpTipoServico) {
		this.cpTipoServico = cpTipoServico;
	}
	
}
