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
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;

import br.gov.jfrj.siga.model.Selecionavel;

public class DpLotacaoAction extends
		SigaSelecionavelActionSupport<DpLotacao, DpLotacaoDaoFiltro> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3768576909382652437L;

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
	public DpLotacaoDaoFiltro createDaoFiltro() {
		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		/*
		 * if (param("postback")==null)
		 * flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		 * else flt.setIdOrgaoUsu(paramInteger("orgaoUsu"));
		 */
		flt.setIdOrgaoUsu(paramLong("orgaoUsu"));
		if (flt.getIdOrgaoUsu() == null)
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario()
					.getIdOrgaoUsu());
		String buscarFechadas = param("buscarFechadas");
		flt.setBuscarFechadas(buscarFechadas != null ? Boolean
				.valueOf(buscarFechadas) : false);

		return flt;
	}

	@Override
	public Selecionavel selecionarPorNome(final DpLotacaoDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpLotacao) l.get(0);
		return null;
	}

}
