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

import java.io.Serializable;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.model.Objeto;

public abstract class AbstractExBoletimDoc extends Objeto implements Serializable {
	
	/** The composite primary key value. */
	private java.lang.Long idBoletimDoc;
	
	private ExDocumento exDocumento;
	
	private ExDocumento boletim;
	
	public AbstractExBoletimDoc() {
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

	public java.lang.Long getIdBoletimDoc() {
		return idBoletimDoc;
	}

	public void setIdBoletimDoc(java.lang.Long idBoletimDoc) {
		this.idBoletimDoc = idBoletimDoc;
	}

}
