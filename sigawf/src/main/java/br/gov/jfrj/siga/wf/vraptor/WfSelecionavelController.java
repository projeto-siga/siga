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
package br.gov.jfrj.siga.wf.vraptor;

import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.SigaSelecionavelControllerSupport;
import br.gov.jfrj.siga.wf.dao.WfDao;

public abstract class WfSelecionavelController<T extends Selecionavel, DaoFiltroT extends DaoFiltroSelecionavel>
		extends SigaSelecionavelControllerSupport<T, DaoFiltroT> {

	private static ResourceBundle bundle;

	/**
	 * @deprecated CDI eyes only
	 */
	public WfSelecionavelController() {
		super();
	}

	public WfSelecionavelController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so,
			EntityManager em) {
		super(request, result, dao, so, em);
	}

	public void assertAcesso(String pathServico) throws AplicacaoException {
		so.assertAcesso(WfController.ACESSO_WF + ";" + pathServico);
	}

	protected WfDao dao() {
		return WfDao.getInstance();
	}
}
