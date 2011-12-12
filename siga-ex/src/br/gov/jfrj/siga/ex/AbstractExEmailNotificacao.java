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
package br.gov.jfrj.siga.ex;
import java.io.Serializable;

import br.gov.jfrj.siga.dp.DpLotacao;

public class AbstractExEmailNotificacao {
	
	/** The composite primary key value. */
	private java.lang.Long idEmailNotificacao;
	
	private DpLotacao dpLotacao;
	
	private String email;
	
	public java.lang.Long getIdEmailNotificacao() {
		return idEmailNotificacao;
	}

	public void setIdEmailNotificacao(java.lang.Long idEmailNotificacao) {
		this.idEmailNotificacao = idEmailNotificacao;
	}

	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}

	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AbstractExEmailNotificacao() {
	}
}
