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
package br.gov.jfrj.webwork.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.SigaSelecionavelActionSupport;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public abstract class ExSelecionavelActionSupport<T extends Selecionavel, DaoFiltroT extends DaoFiltroSelecionavel>
		extends SigaSelecionavelActionSupport<T, DaoFiltroT> {

	public ExDao dao() {
		return ExDao.getInstance();
	}

	public List<CpMarcador> getEstados() throws AplicacaoException {
		return dao().listarMarcadores();
	}

	public Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}

	public List<String> getListaAnos() {
		final ArrayList<String> lst = new ArrayList<String>();
		// map.add("", "[Vazio]");
		final Calendar cal = Calendar.getInstance();
		for (Integer ano = cal.get(Calendar.YEAR); ano >= 1990; ano--)
			lst.add(ano.toString());
		return lst;
	}

	public List<ExTipoDocumento> getTiposDocumento() throws AplicacaoException {
		return dao().listarExTiposDocumento();
	}

	public void assertAcesso(String pathServico) throws AplicacaoException,
			Exception {
		super.assertAcesso("DOC:Módulo de Documentos;" + pathServico);
	}
}
