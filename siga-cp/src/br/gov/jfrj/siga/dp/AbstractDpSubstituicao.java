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
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractDpSubstituicao implements Serializable {

	private Date dtFimSubst;

	private Date dtIniSubst;

	private Long idSubstituicao;

	private DpLotacao lotaSubstituto;

	private DpLotacao lotaTitular;

	private DpPessoa substituto;

	private DpPessoa titular;
	
	private Date dtFimRegistro;

	private Date dtIniRegistro;

	private Long idRegistroInicial;
	

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpSubstituicao))
			return false;
		final DpSubstituicao that = (DpSubstituicao) rhs;

		if ((this.getIdSubstituicao() == null ? that.getIdSubstituicao() == null
				: this.getIdSubstituicao().equals(that.getIdSubstituicao())))
			return true;
		return false;
	}

	public Date getDtFimSubst() {
		return dtFimSubst;
	}

	public void setDtFimSubst(Date dtFimSubst) {
		this.dtFimSubst = dtFimSubst;
	}

	public Date getDtIniSubst() {
		return dtIniSubst;
	}

	public void setDtIniSubst(Date dtIniSubst) {
		this.dtIniSubst = dtIniSubst;
	}

	public Long getIdSubstituicao() {
		return idSubstituicao;
	}

	public void setIdSubstituicao(Long idSubstituicao) {
		this.idSubstituicao = idSubstituicao;
	}

	public DpLotacao getLotaSubstituto() {
		return lotaSubstituto;
	}

	public void setLotaSubstituto(DpLotacao lotaSubstituto) {
		this.lotaSubstituto = lotaSubstituto;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public DpPessoa getSubstituto() {
		return substituto;
	}

	public void setSubstituto(DpPessoa substituto) {
		this.substituto = substituto;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public Long getIdRegistroInicial() {
		return idRegistroInicial;
	}

	public void setIdRegistroInicial(Long idRegistroInicial) {
		this.idRegistroInicial = idRegistroInicial;
	}

	public Date getDtFimRegistro() {
		return dtFimRegistro;
	}

	public void setDtFimRegistro(Date dtFimRegistro) {
		this.dtFimRegistro = dtFimRegistro;
	}

	public Date getDtIniRegistro() {
		return dtIniRegistro;
	}

	public void setDtIniRegistro(Date dtIniRegistro) {
		this.dtIniRegistro = dtIniRegistro;
	}
}
