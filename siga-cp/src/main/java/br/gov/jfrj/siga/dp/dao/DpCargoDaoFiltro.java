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

import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class DpCargoDaoFiltro extends DaoFiltroSelecionavel {
	private String nome;
	private Long idOrgaoUsu;
	private Long idCargoIni;
	private boolean buscarInativos;
	
	public DpCargoDaoFiltro() {}
	
	public DpCargoDaoFiltro(String nome, Long idOrgaoUsu) {
		this.nome = nome;
		this.idOrgaoUsu = idOrgaoUsu;
	}
	
	public boolean isBuscarInativos() {
		return buscarInativos;
	}

	public void setBuscarInativos(boolean buscarInativos) {
		this.buscarInativos = buscarInativos;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}	

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Long getIdCargoIni() {
		return this.idCargoIni;
	}

	public void setIdCargoIni(Long idCargoIni) {
		this.idCargoIni = idCargoIni;
	}
}
