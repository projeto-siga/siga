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
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Objeto;

public class AbstractExEmailNotificacao extends Objeto  {
	
	/** The composite primary key value. */
	private java.lang.Long idEmailNotificacao;

	private DpLotacao dpLotacao;

	private DpPessoa dpPessoa;

	private String email;

	private DpLotacao lotacaoEmail;

	private DpPessoa pessoaEmail;

	public java.lang.Long getIdEmailNotificacao() {
		return idEmailNotificacao;
	}

	public void setIdEmailNotificacao(java.lang.Long idEmailNotificacao) {
		this.idEmailNotificacao = idEmailNotificacao;
	}
	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}
	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}
	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}	
	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}
	public DpLotacao getLotacaoEmail() {
		return lotacaoEmail;
	}
	public void setLotacaoEmail(DpLotacao lotacaoEmail) {
		this.lotacaoEmail = lotacaoEmail;
	}
	public String getEmail() {
		return email;
	}
	public DpPessoa getPessoaEmail() {
		return pessoaEmail;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPessoaEmail(DpPessoa pessoaEmail) {
		this.pessoaEmail = pessoaEmail;
	}
	public AbstractExEmailNotificacao() {
	}
}
