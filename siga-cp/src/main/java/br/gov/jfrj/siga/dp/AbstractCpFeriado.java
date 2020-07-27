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

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "listarCpFeriadoOrdenadoPorDescricao", query = "from CpFeriado feriado order by feriado.dscFeriado") })
public abstract class AbstractCpFeriado extends Objeto implements Serializable {

	@Id
	@SequenceGenerator(name = "CP_FERIADO_SEQ", sequenceName = "CORPORATIVO"
			+ ".CP_FERIADO_SEQ")
	@GeneratedValue(generator = "CP_FERIADO_SEQ")
	@Column(name = "ID_FERIADO", unique = true, nullable = false)
	private Integer idFeriado;

	@Column(name = "DSC_FERIADO", nullable = false, length = 256)
	private String dscFeriado;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cpFeriado")
	private java.util.Set<CpOcorrenciaFeriado> cpOcorrenciaFeriadoSet;

	public Integer getIdFeriado() {
		return idFeriado;
	}

	public void setIdFeriado(Integer idFeriado) {
		this.idFeriado = idFeriado;
	}

	public String getDscFeriado() {
		return dscFeriado;
	}

	public void setDscFeriado(String dscFeriado) {
		this.dscFeriado = dscFeriado;
	}

	public java.util.Set<CpOcorrenciaFeriado> getCpOcorrenciaFeriadoSet() {
		return cpOcorrenciaFeriadoSet;
	}

	public void setCpOcorrenciaFeriadoSet(
			java.util.Set<CpOcorrenciaFeriado> cpOcorrenciaFeriadoSet) {
		this.cpOcorrenciaFeriadoSet = cpOcorrenciaFeriadoSet;
	}

}
