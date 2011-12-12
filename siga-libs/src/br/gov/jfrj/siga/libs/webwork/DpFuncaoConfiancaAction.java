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
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.libs.webwork;

import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.dao.DpFuncaoConfiancaDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

public class DpFuncaoConfiancaAction
		extends
		SigaSelecionavelActionSupport<DpFuncaoConfianca, DpFuncaoConfiancaDaoFiltro> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098577621835510117L;

	private Long orgaoUsu;

	public String aBuscar() throws Exception {
		if (param("postback") == null)
			setOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		return super.aBuscar();
	}

	public Long getOrgaoUsu() {
		return orgaoUsu;
	}

	public void setOrgaoUsu(Long orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	@Override
	public DpFuncaoConfiancaDaoFiltro createDaoFiltro() {
		final DpFuncaoConfiancaDaoFiltro flt = new DpFuncaoConfiancaDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		flt.setIdOrgaoUsu(paramLong("orgaoUsu"));
		if (flt.getIdOrgaoUsu() == null)
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario()
					.getIdOrgaoUsu());
		return flt;
	}

	@Override
	public Selecionavel selecionarPorNome(final DpFuncaoConfiancaDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpFuncaoConfianca) l.get(0);
		return null;
	}
}
