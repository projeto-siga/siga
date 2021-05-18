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
 * Criado em  18/11/2019
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_contrato")
public class CpContrato extends AbstractCpContrato implements
		Serializable {

	public Long getIdOrgaoUsu() {
		return getIdOrgaoUsu();
	}

	public String getDtContratoDDMMYYYY() {
		if (getDtContrato() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtContrato());
		}
		return "";
	}
}