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

import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class DpPessoaDaoFiltro extends DaoFiltroSelecionavel {
	private DpLotacao lotacao;
	private DpCargo cargo;
	private DpFuncaoConfianca funcaoConfianca;

	private String nome;
	private Long idOrgaoUsu;
	private Long cpf;
	private Long id;
	
	private boolean buscarFechadas;
	private String situacaoFuncionalPessoa;
	
	public String getSituacaoFuncionalPessoa() {
		return situacaoFuncionalPessoa;
	}

	public void setSituacaoFuncionalPessoa(String situacaoFuncionalPessoa) {
		this.situacaoFuncionalPessoa = situacaoFuncionalPessoa;
	}

	public boolean isBuscarFechadas() {
		return buscarFechadas;
	}

	public void setBuscarFechadas(boolean buscarFechadas) {
		this.buscarFechadas = buscarFechadas;
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

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(final DpLotacao lotacao) {
		this.lotacao = lotacao;
	}
	
	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
	
	public DpCargo getCargo() {
		return cargo;
	}

	public void setCargo(DpCargo cargo) {
		this.cargo = cargo;
	}

	public DpFuncaoConfianca getFuncaoConfianca() {
		return funcaoConfianca;
	}

	public void setFuncaoConfianca(DpFuncaoConfianca funcaoConfianca) {
		this.funcaoConfianca = funcaoConfianca;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
