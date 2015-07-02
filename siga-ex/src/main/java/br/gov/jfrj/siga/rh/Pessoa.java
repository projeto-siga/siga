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
 * Criado em  18/10/2005
 *
 * o change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.rh;

/**
 * 
 * @author SEANS Representação parcial da tabela RH_PESSOAL ou da tabela
 *         RH_CONTRATADOS
 */
public abstract class Pessoa {

	private Integer cpf;

	private String identidade;

	private String nome;

	private String sexo;

	private Integer telefone1;

	@Override
	public boolean equals(final Object obj) {

		if ((obj == null) || !(obj instanceof Pessoa))
			return false;
		final Pessoa coisa = (Pessoa) obj;
		/*
		 * o funcionamento do operador ternário condição ? valorVerdadeiro :
		 * valorFalso
		 */
		if ((this.cpf == null ? coisa.cpf == null : this.cpf.equals(coisa.cpf))) {
			if ((this.nome == null ? coisa.nome == null : this.nome.equals(coisa.nome)))
				return true;
		}
		return false;
	}

	public abstract String getCargoDescricao();

	/**
	 * @return Retorna o atributo cpf.
	 */
	public Integer getCpf() {
		return cpf;
	}

	/**
	 * @return Retorna o atributo identidade.
	 */
	public String getIdentidade() {
		return identidade;
	}

	public abstract String getIdentificador();

	public abstract Lotacao getLotacaoAtual();

	/**
	 * @return Retorna o atributo nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Retorna o atributo sexo.
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @return Retorna o atributo telefone1.
	 */
	public Integer getTelefone1() {
		return telefone1;
	}

	@Override
	public int hashCode() {
		final int inicial = 17;
		int codigo = inicial; // Semente
		if (this.cpf != null) {
			codigo = codigo * inicial + this.cpf.hashCode();
		}
		if (this.nome != null) {
			codigo = codigo * inicial + this.nome.hashCode();
		}
		return codigo;
	}

	/**
	 * @param cpf
	 *            Atribui a cpf o valor.
	 */
	public void setCpf(final Integer cpf) {
		this.cpf = cpf;
	}

	/**
	 * @param identidade
	 *            Atribui a identidade o valor.
	 */
	public void setIdentidade(final String identidade) {
		this.identidade = identidade;
	}

	/**
	 * @param nome
	 *            Atribui a nome o valor.
	 */
	public void setNome(final String nome) {
		this.nome = nome;
	}

	/**
	 * @param sexo
	 *            Atribui a sexo o valor.
	 */
	public void setSexo(final String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @param telefone1
	 *            Atribui a telefone1 o valor.
	 */
	public void setTelefone1(final Integer telefone1) {
		this.telefone1 = telefone1;
	}

}
