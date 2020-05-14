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
package br.gov.jfrj.siga.dp.dao;

import br.gov.jfrj.siga.cp.util.MatriculaUtils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class DpLotacaoDaoFiltro extends DaoFiltroSelecionavel {
	private String nome;

	private Long idOrgaoUsu;

	private boolean buscarFechadas;
	
	public DpLotacaoDaoFiltro() {}
	
	public DpLotacaoDaoFiltro(String nome, Long idOrgaoUsu) {
		this.nome = nome;
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public boolean isBuscarFechadas() {
		return buscarFechadas;
	}

	public void setBuscarFechadas(boolean buscarFechadas) {
		this.buscarFechadas = buscarFechadas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public void setSiglaCompleta(String siglaCompleta) {
		if (siglaCompleta == null)
			return;

		String orgaoUsu = MatriculaUtils.getSiglaDoOrgaoDaLotacao(siglaCompleta);
		CpOrgaoUsuario cpOrgaoUsuario = new CpOrgaoUsuario();
		cpOrgaoUsuario.setSigla(orgaoUsu);
		CpOrgaoUsuario orgaoUsuario = CpDao.getInstance().consultarPorSigla(cpOrgaoUsuario);
		
		if(null != orgaoUsuario) {
			idOrgaoUsu = orgaoUsuario.getId();
		}

		setSigla(MatriculaUtils.getSiglaDaLotacao(siglaCompleta));
	}
}
