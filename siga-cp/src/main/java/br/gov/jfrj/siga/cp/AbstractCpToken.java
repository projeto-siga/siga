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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;



@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractCpToken implements Serializable {

	@Id
	@SequenceGenerator(name="CP_TOKEN_GENERATOR", sequenceName="CORPORATIVO.CP_TOKEN_SEQ")
	@GeneratedValue(generator="CP_TOKEN_GENERATOR")
	@Column(name="ID_TOKEN")
	private long idToken;

	@Column(name = "ID_REF")
	private java.lang.Long idRef;
	
	@Column(name = "ID_TP_TOKEN")
	private java.lang.Long idTpToken;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_IAT", length = 19)
	private java.util.Date dtIat;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_EXP", length = 19)
	private java.util.Date dtExp;
	
	@Column(length = 256)
	private String token;

	public long getIdToken() {
		return idToken;
	}

	public void setIdToken(long idToken) {
		this.idToken = idToken;
	}

	public java.lang.Long getIdRef() {
		return idRef;
	}

	public void setIdRef(java.lang.Long idRef) {
		this.idRef = idRef;
	}

	public java.lang.Long getIdTpToken() {
		return idTpToken;
	}

	public void setIdTpToken(java.lang.Long idTpToken) {
		this.idTpToken = idTpToken;
	}

	public java.util.Date getDtIat() {
		return dtIat;
	}

	public void setDtIat(java.util.Date dtIat) {
		this.dtIat = dtIat;
	}

	public java.util.Date getDtExp() {
		return dtExp;
	}

	public void setDtExp(java.util.Date dtExp) {
		this.dtExp = dtExp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
