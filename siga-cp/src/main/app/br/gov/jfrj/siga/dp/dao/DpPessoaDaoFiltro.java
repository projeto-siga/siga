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
	private Long[] idLotacaoSelecao;
	private Long[] idPessoaSelecao;
	private DpCargo cargo;
	private DpFuncaoConfianca funcaoConfianca;

	private String nome;
	private Long idOrgaoUsu;
	private Long cpf;
	private Long id;
	private String email;
	
	
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
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
	public DpLotacao getLotacao() {
		return lotacao;
	}	

	public void setLotacao(final DpLotacao lotacao) {
		this.lotacao = lotacao;
	}
	
	public Long[] getIdLotacaoSelecao() {
		return this.idLotacaoSelecao;
	}
	
	public void setIdLotacaoSelecao(Long[] idLotacaoSelecao) {
		this.idLotacaoSelecao = idLotacaoSelecao;
	}
	
	public Long[] getIdPessoaSelecao() {
		return this.idPessoaSelecao;
	}
	
	public void setIdPessoaSelecao(Long[] idPessoaSelecao) {
		this.idPessoaSelecao = idPessoaSelecao;
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
	
	public void prepararLotacao(String idLotacaoSelecao) {
		if(idLotacaoSelecao != null) {			
			if (idLotacaoSelecao.contains(",")) {									
				String idLotacoes[] = idLotacaoSelecao.split(",");				
				this.idLotacaoSelecao = new Long[idLotacoes.length];
				for (int i = 0; i <= idLotacoes.length -1; i++) {
					this.idLotacaoSelecao[i] = Long.valueOf(idLotacoes[i]);
				}				
			} else {
				DpLotacao lotacao = new DpLotacao();
				lotacao.setId(Long.valueOf(idLotacaoSelecao));
				this.lotacao = lotacao;
			}
		}				
	}
	
	public void prepararPessoa(String idPessoaSelecao) {
		if(idPessoaSelecao != null) {			
			if (idPessoaSelecao.contains(",")) {									
				String idPessoas[] = idPessoaSelecao.split(",");				
				this.idPessoaSelecao = new Long[idPessoas.length];
				for (int i = 0; i <= idPessoas.length -1; i++) {
					this.idPessoaSelecao[i] = Long.valueOf(idPessoas[i]);
				}				
			} else if(idPessoaSelecao.length() > 0) {
				this.idPessoaSelecao = new Long[1];
				this.idPessoaSelecao[0] = Long.valueOf(idPessoaSelecao); 
			}
		}
	}
}
