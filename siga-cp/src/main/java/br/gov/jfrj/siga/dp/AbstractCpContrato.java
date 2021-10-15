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
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.model.Objeto;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractCpContrato extends Objeto implements
		Serializable {

//	@Id
//	@SequenceGenerator(name = "CP_CONTRATO_SEQ", sequenceName = "CORPORATIVO.CP_CONTRATO_SEQ")
//	@GeneratedValue(generator = "generator")
//	@Column(name = "ID_CONTRATO", unique = true, nullable = false)
//	@Desconsiderar
//	private Long idContrato;
//	
	@Id
	@Column(name = "ID_ORGAO_USU", unique = true, nullable = false)
	private Long idOrgaoUsu;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_CONTRATO", length = 19)
	private java.util.Date dtContrato;

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public Date getDtContrato() {
		return dtContrato;
	}

	public void setDtContrato(final java.util.Date dtContrato) {
		this.dtContrato = dtContrato;
	}
}