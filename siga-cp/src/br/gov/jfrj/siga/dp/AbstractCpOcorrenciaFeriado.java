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
import java.util.Date;

public abstract class AbstractCpOcorrenciaFeriado implements Serializable {

	private Long idOcorrencia;

	private Date dtIniFeriado;
	
	private Date dtFimFeriado;
	
	private CpFeriado cpFeriado;
	
	private java.util.Set<CpAplicacaoFeriado> cpAplicacaoFeriadoSet;

	public java.util.Set<CpAplicacaoFeriado> getCpAplicacaoFeriadoSet() {
		return cpAplicacaoFeriadoSet;
	}

	public void setCpAplicacaoFeriadoSet(
			java.util.Set<CpAplicacaoFeriado> cpAplicacaoFeriadoSet) {
		this.cpAplicacaoFeriadoSet = cpAplicacaoFeriadoSet;
	}

	public Long getIdOcorrencia() {
		return idOcorrencia;
	}

	public void setIdOcorrencia(Long idOcorrencia) {
		this.idOcorrencia = idOcorrencia;
	}

	public Date getDtIniFeriado() {
		return dtIniFeriado;
	}

	public void setDtIniFeriado(Date dtIniFeriado) {
		this.dtIniFeriado = dtIniFeriado;
	}

	public Date getDtFimFeriado() {
		return dtFimFeriado;
	}

	public void setDtFimFeriado(Date dtFimFeriado) {
		this.dtFimFeriado = dtFimFeriado;
	}

	public CpFeriado getCpFeriado() {
		return cpFeriado;
	}

	public void setCpFeriado(CpFeriado cpFeriado) {
		this.cpFeriado = cpFeriado;
	}

}
