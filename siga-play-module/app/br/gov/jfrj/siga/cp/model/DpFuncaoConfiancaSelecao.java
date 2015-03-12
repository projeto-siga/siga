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
package br.gov.jfrj.siga.cp.model;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class DpFuncaoConfiancaSelecao extends Selecao<DpFuncaoConfianca> {

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		final DpFuncaoConfianca oExemplo = new DpFuncaoConfianca();
		oExemplo.setSigla(getSigla());

		final DpFuncaoConfianca o = CpDao.getInstance().consultarPorSigla(
				oExemplo);

		if (o == null) {
			apagar();
			return false;
		}

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		final DpFuncaoConfianca o = CpDao.getInstance().consultar(
				getId(), DpFuncaoConfianca.class, false);
		if (o == null)
			return false;

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public String getAcaoBusca() {
		return "/funcao";
	}

	@Override
	public DpFuncaoConfianca buscarObjeto() throws AplicacaoException {
		if (getId() == null)
			return null;

		final DpFuncaoConfianca o = CpDao.getInstance().consultar(
				getId(), DpFuncaoConfianca.class, false);

		return o;
	}
}
