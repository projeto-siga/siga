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
package br.gov.jfrj.siga.ex.BIE;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarDocsDisponiveisParaInclusaoEmBoletim", query = "select bol.exDocumento from ExBoletimDoc bol where bol.boletim is null and bol.exDocumento.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"),
		@NamedQuery(name = "consultarDocsInclusosNoBoletim", query = "select bol.exDocumento from ExBoletimDoc bol where bol.boletim.idDoc = :idDoc"),
		@NamedQuery(name = "consultarBoletimEmQueODocumentoEstaIncluso", query = "from ExBoletimDoc bol where bol.exDocumento.idDoc = :idDoc"),
		@NamedQuery(name = "consultarBoletim", query = "from ExBoletimDoc bol where bol.boletim.idDoc = :idDoc") })
public abstract class AbstractExBoletimDoc extends Objeto implements
		Serializable {

	/** The composite primary key value. */
	@Id
	@GeneratedValue
	@Column(name = "ID_BOLETIM_DOC", unique = true, nullable = false)
	private java.lang.Long idBoletimDoc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOC", unique = true)
	private ExDocumento exDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BOLETIM")
	private ExDocumento boletim;

	public AbstractExBoletimDoc() {
	}

	public java.lang.Long getIdBoletimDoc() {
		return idBoletimDoc;
	}

	public void setIdBoletimDoc(java.lang.Long idBoletimDoc) {
		this.idBoletimDoc = idBoletimDoc;
	}

	public ExDocumento getExDocumento() {
		return exDocumento;
	}

	public void setExDocumento(ExDocumento exDocumento) {
		this.exDocumento = exDocumento;
	}

	public ExDocumento getBoletim() {
		return boletim;
	}

	public void setBoletim(ExDocumento boletim) {
		this.boletim = boletim;
	}

}
