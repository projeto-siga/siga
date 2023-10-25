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

public class CpGrupoDaoFiltro extends DaoFiltroSelecionavel {
	private Integer idTpGrupo;
	private Long idOrgaoUsu;
	private String nome;
	
	public CpGrupoDaoFiltro() {}
	
	public CpGrupoDaoFiltro(String nome, Long idOrgaoUsu) {
		this.nome = nome;
		this.idOrgaoUsu = idOrgaoUsu;
	}
	
	
	/**
	 * @return the idTpGrupo
	 */
	public Integer getIdTpGrupo() {
		return idTpGrupo;
	}

	/**
	 * @param idTpGrupo the idTpGrupo to set
	 */
	public void setIdTpGrupo(Integer idTpGrupo) {
		this.idTpGrupo = idTpGrupo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}
	

}
