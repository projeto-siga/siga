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
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

public class ExMobilSelecao extends Selecao<ExMobil> {

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		final ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		flt.setSigla(getSigla());

		try {
			ExMobil o = ExDao.getInstance().consultarPorSigla(flt);
			if (o == null || o.isEliminado()) {
				apagar();
				return false;
			}
			buscarPorObjeto(o);
		} catch (Exception se) {
		}

		return true;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		final ExMobil o = ExDao.getInstance().consultar(getId(), ExMobil.class,
				false);
		if (o == null || o.isEliminado())
			return false;

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public String getAcaoBusca() {
		return "/expediente";
	}

	@Override
	public ExMobil buscarObjeto() throws AplicacaoException {
		if (getId() == null)
			return null;

		final ExMobil o = ExDao.getInstance().consultar(getId(), ExMobil.class,
				false);

		return o;
	}
	
	@Override
	public String getTamanho() {
		return TAMANHO_GRANDE;
	}
}