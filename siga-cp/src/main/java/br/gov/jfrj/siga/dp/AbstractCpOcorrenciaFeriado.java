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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.model.Objeto;

//Essa anotação é necessária por causa do mappedBy em CpFeriado que aponta pra cá
@MappedSuperclass
public abstract class AbstractCpOcorrenciaFeriado extends Objeto implements
		Serializable {

	@Id
	@SequenceGenerator(sequenceName = "CORPORATIVO.CP_OCORRENCIA_FERIADO_SEQ", name = "CORPORATIVO.CP_OCORRENCIA_FERIADO_SEQ")
	@GeneratedValue(generator = "CORPORATIVO.CP_OCORRENCIA_FERIADO_SEQ")
	@Column(name = "ID_OCORRENCIA", unique = true, nullable = false)
	private Long idOcorrencia;

	@Column(name = "DT_INI_FERIADO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtIniFeriado;

	@Column(name = "DT_FIM_FERIADO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtFimFeriado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FERIADO")
	private CpFeriado cpFeriado;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cpOcorrenciaFeriado")
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

	/**
	 * Retorna a data de início formato dd/mm/aa , por exemplo, 01/02/10.
	 * 
	 * @return Data no formato dd/mm/aa, por exemplo, 01/02/10.
	 * 
	 */
	public String getDtRegIniDDMMYYYY() {
		if (getDtIniFeriado() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtIniFeriado());
		}
		return "";
	}

	public String getDtRegFimDDMMYYYY() {
		if (getDtFimFeriado() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtFimFeriado());
		}
		return "";
	}

}
