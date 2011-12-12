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

public abstract class AbstractCpPersonalizacao implements Serializable {
	private Long idPersonalizacao;
	private DpPessoa pessoa;
	private DpPessoa pesSubstituindo;
	private DpLotacao lotaSubstituindo;

	// private Usuario usuarioSimulando;

	public Long getIdPersonalizacao() {
		return idPersonalizacao;
	}

	public void setIdPersonalizacao(Long idPersonalizacao) {
		this.idPersonalizacao = idPersonalizacao;
	}

	public DpPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(DpPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DpPessoa getPesSubstituindo() {
		return pesSubstituindo;
	}

	public void setPesSubstituindo(DpPessoa pesSubstituindo) {
		this.pesSubstituindo = pesSubstituindo;
	}

	public DpLotacao getLotaSubstituindo() {
		return lotaSubstituindo;
	}

	public void setLotaSubstituindo(DpLotacao lotaSubstituindo) {
		this.lotaSubstituindo = lotaSubstituindo;
	}
	// public Usuario getUsuarioSimulando() {
	// return usuarioSimulando;
	// }
	// public void setUsuarioSimulando(Usuario usuarioSimulando) {
	// this.usuarioSimulando = usuarioSimulando;
	// }
}
