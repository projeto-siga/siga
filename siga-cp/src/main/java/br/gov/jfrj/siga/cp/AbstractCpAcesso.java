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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
public abstract class AbstractCpAcesso implements Serializable {

	public static enum CpTipoAcessoEnum {
		AUTENTICACAO, RENOVACAO_AUTOMATICA, FINALIZACAO
	}

	@Id
	@SequenceGenerator(name = "CP_ACESSO_SEQ", sequenceName = "CORPORATIVO.CP_ACESSO_SEQ")
	@GeneratedValue(generator = "CP_ACESSO_SEQ")
	@Column(name = "ID_ACESSO", unique = true, nullable = false)
	@Desconsiderar
	private Long idCpAcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_IDENTIDADE", nullable = false)
	private CpIdentidade cpIdentidade;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INI", length = 19)
	private Date dtInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_FIM", length = 19)
	private Date dtTermino;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TP_ACESSO")
	private CpTipoAcessoEnum tipo;

	@Column(name = "IP_AUDIT", length = 256)
	private String auditIP;

	public Long getIdCpAcesso() {
		return idCpAcesso;
	}

	public void setIdCpAcesso(Long idCpAcesso) {
		this.idCpAcesso = idCpAcesso;
	}

	public CpIdentidade getCpIdentidade() {
		return cpIdentidade;
	}

	public void setCpIdentidade(CpIdentidade cpIdentidade) {
		this.cpIdentidade = cpIdentidade;
	}

	public Date getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	public Date getDtTermino() {
		return dtTermino;
	}

	public void setDtTermino(Date dtTermino) {
		this.dtTermino = dtTermino;
	}

	public CpTipoAcessoEnum getTipo() {
		return tipo;
	}

	public void setTipo(CpTipoAcessoEnum tipo) {
		this.tipo = tipo;
	}

	public String getAuditIP() {
		return auditIP;
	}

	public void setAuditIP(String auditIP) {
		this.auditIP = auditIP;
	}
}
