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
package br.gov.jfrj.siga.ex.util;

import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class Feriados {

	private static List<CpFeriado> listaFeriados;

	public static List<CpFeriado> getListaFeriados() {
		if (listaFeriados == null || listaFeriados.size() == 0) {
			CpDao dao = CpDao.getInstance();
			listaFeriados = dao.listarFeriados();
		}
		return listaFeriados;
	}

	public static void setListaFeriados(List<CpFeriado> listaFeriados) {
		Feriados.listaFeriados = listaFeriados;
	}

	public static CpFeriado verificarPorData(Date dt) {

		for (CpFeriado f : getListaFeriados())
			if (f.abrange(dt))
				return f;
		return null;
	}
}
