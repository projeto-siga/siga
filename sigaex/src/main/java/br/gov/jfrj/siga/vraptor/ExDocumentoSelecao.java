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
package br.gov.jfrj.siga.vraptor;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.Selecao;
import br.gov.jfrj.siga.model.Selecionavel;

public class ExDocumentoSelecao extends Selecao {

	@Override
	public String getAcaoBusca() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Selecionavel buscarObjeto() throws AplicacaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		// TODO Auto-generated method stub
		return false;
	}
}