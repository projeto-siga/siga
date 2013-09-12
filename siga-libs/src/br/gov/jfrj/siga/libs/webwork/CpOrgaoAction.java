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

import com.opensymphony.xwork.Action;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

public class CpOrgaoAction extends SigaSelecionavelActionSupport<CpOrgao, CpOrgaoDaoFiltro> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098577621835510117L;	
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public CpOrgao daoOrgao(long id) {
		return dao().consultar(id, CpOrgao.class, false);
	}

	@Override
	public CpOrgaoDaoFiltro createDaoFiltro() {
		final CpOrgaoDaoFiltro flt = new CpOrgaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		return flt;
	}
	
	@Override
	public Selecionavel selecionarPorNome(final CpOrgaoDaoFiltro flt) throws AplicacaoException {
		
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = CpDao.getInstance().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (CpOrgao) l.get(0);
		return null;
	}
	
	public String aListar() throws Exception {
		
		setItens(CpDao.getInstance().listarOrgaos());
		
		return Action.SUCCESS;
	}
	
	public String aExcluir() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		if (getId() != null) {
			try {
				dao().iniciarTransacao();
				CpOrgao orgao = daoOrgao(getId());				
				dao().excluir(orgao);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de Orgão", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		return Action.SUCCESS;
	}
}
