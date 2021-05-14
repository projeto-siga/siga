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

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractCpModelo extends HistoricoAuditavelSuporte {

	/** The primary key value. */

	@Id
	@SequenceGenerator(name = "CP_MODELO_SEQ", sequenceName = "CORPORATIVO.CP_MODELO_SEQ")
	@GeneratedValue(generator = "CP_MODELO_SEQ")
	@Column(name = "ID_MODELO", unique = true, nullable = false)
	@Desconsiderar
	private java.lang.Long idMod;

	/** The value of the simple conteudoBlobMod property. */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTEUDO_BLOB_MOD")
	private byte[] conteudoBlobMod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario cpOrgaoUsuario;

	/**
	 * Return the value of the primary key column.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdMod() {
		return idMod;
	}

	/**
	 * Return the value of the CONTEUDO_BLOB_MOD column.
	 * 
	 * @return java.lang.String
	 */
	public byte[] getConteudoBlobMod() {
		return this.conteudoBlobMod;
	}

	/**
	 * /** Set the simple primary key value that identifies this object.
	 * 
	 * @param idMod
	 */
	public void setIdMod(final java.lang.Long idMod) {
		this.idMod = idMod;
	}

	/**
	 * Set the value of the CONTEUDO_BLOB_MOD column.
	 * 
	 * @param conteudoBlobMod
	 */
	public void setConteudoBlobMod(byte[] conteudoBlobMod) {
		this.conteudoBlobMod = conteudoBlobMod;
	}

	public CpOrgaoUsuario getCpOrgaoUsuario() {
		return cpOrgaoUsuario;
	}

	public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.cpOrgaoUsuario = cpOrgaoUsuario;
	}

}
