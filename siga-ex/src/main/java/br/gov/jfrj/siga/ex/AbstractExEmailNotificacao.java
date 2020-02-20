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

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarEmailporPessoa", query = "from ExEmailNotificacao e where e.dpPessoa.idPessoa = :idPessoaIni"),
		@NamedQuery(name = "consultarEmailporLotacao", query = "from ExEmailNotificacao e where e.dpLotacao.idLotacao = :idLotacaoIni") })
public class AbstractExEmailNotificacao extends Objeto {

	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_EMAIL_NOTIFICACAO_SEQ", name = "EX_EMAIL_NOTIFICACAO_SEQ")
	@GeneratedValue(generator = "EX_EMAIL_NOTIFICACAO_SEQ")
	@Column(name = "ID_EMAIL_NOTIFICACAO", unique = true, nullable = false)
	private java.lang.Long idEmailNotificacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO", nullable = false)
	private DpLotacao dpLotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA", nullable = false)
	private DpPessoa dpPessoa;

	@Column(name = "EMAIL", length = 60)
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_EMAIL", nullable = false)
	private DpLotacao lotacaoEmail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA_EMAIL", nullable = false)
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
