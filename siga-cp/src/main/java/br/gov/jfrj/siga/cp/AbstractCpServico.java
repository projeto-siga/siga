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

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarPorSiglaCpServico", query = "select org from CpServico org "
				+ "where upper(org.siglaServico) like upper(:siglaServico) "
				+ "and ((:idServicoPai = 0L and org.cpServicoPai is null) or org.cpServicoPai.idServico = :idServicoPai)"),
		@NamedQuery(name = "consultarPorSiglaStringCpServico", query = "select org from CpServico org "
				+ "where upper(org.siglaServico) like upper(:siglaServico)") })
public abstract class AbstractCpServico extends Objeto implements Serializable {

	@Id
	@SequenceGenerator(name = "CP_SERVICO_SEQ", sequenceName = "CORPORATIVO.CP_SERVICO_SEQ")
	@GeneratedValue(generator = "CP_SERVICO_SEQ")
	@Column(name = "ID_SERVICO", unique = true, nullable = false)
	private Long idServico;

	@Column(name = "SIGLA_SERVICO", nullable = false, length = 35)
	private String siglaServico;

	@Column(name = "DESC_SERVICO", nullable = false, length = 200)
	private String dscServico;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SERVICO_PAI")
	private CpServico cpServicoPai;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_SERVICO", nullable = false)
	private CpTipoServico cpTipoServico;

	@Column(name = "LABEL_SERVICO", length = 35)
	private String labelServico;

	/**
	 * @return the cpServicoPai
	 */
	public CpServico getCpServicoPai() {
		return cpServicoPai;
	}

	/**
	 * @param cpServicoPai
	 *            the cpServicoPai to set
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
	 * @param cpTipoServico
	 *            the cpTipoServico to set
	 */
	public void setCpTipoServico(CpTipoServico cpTipoServico) {
		this.cpTipoServico = cpTipoServico;
	}

	public String getLabelServico() {
		return labelServico;
	}

	public void setLabelServico(String labelServico) {
		this.labelServico = labelServico;
	}
}
