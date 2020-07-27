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
package br.gov.jfrj.siga.acesso;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class ConheceUsuarioSupport implements ConheceUsuario {
	private CpIdentidade identidadeCadastrante;
	private DpPessoa cadastrante;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	public void setCadastrante(DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public CpIdentidade getIdentidadeCadastrante() {
		return identidadeCadastrante;
	}

	public void setIdentidadeCadastrante(CpIdentidade identidadeCadastrante) {
		this.identidadeCadastrante = identidadeCadastrante;
	}

}
