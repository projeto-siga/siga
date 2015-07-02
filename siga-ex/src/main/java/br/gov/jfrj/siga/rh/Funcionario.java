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
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.rh;

/**
 * Representação da Tabela rh_funcionario
 * 
 * @author SEANS
 * 
 */
public class Funcionario extends Pessoa {

	private Integer codFuncionario;

	private String email;

	private Lotacao lotacaoAtual;

	private String siglaSecaoSubSecao;

	@Override
	public boolean equals(final Object obj) {

		if ((obj == null) || !(obj instanceof Funcionario))
			return false;
		final Funcionario fun = (Funcionario) obj;
		/*
		 * o funcionamento do operador ternário condição ? valorVerdadeiro :
		 * valorFalso
		 */
		if ((this.siglaSecaoSubSecao == null ? fun.siglaSecaoSubSecao == null : this.siglaSecaoSubSecao
				.equals(fun.siglaSecaoSubSecao))) {
			if ((this.codFuncionario == null ? fun.codFuncionario == null : this.codFuncionario
					.equals(fun.codFuncionario)))
				return true;
		}
		return false;

	}

	/**
	 * @return Retorna o atributo codFuncionario.
	 */
	public Integer getCodFuncionario() {
		return codFuncionario;
	}

	/**
	 * @return Retorna o atributo email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return Retorna o atributo lotacaoAtual.
	 */
	@Override
	public Lotacao getLotacaoAtual() {
		return lotacaoAtual;
	}

	/**
	 * Implementação obrigatória
	 * 
	 * @see interface Funcionario
	 */
	public String getNomeLotacao() {
		String s = "Lotação não Informada";
		if (lotacaoAtual != null)
			s = lotacaoAtual.getDescricaoLotacao();
		return s;
	}

	/**
	 * @return Retorna o atributo siglaSecaoSubsecao.
	 */
	public String getSiglaSecaoSubSecao() {
		return siglaSecaoSubSecao;
	}

	@Override
	public int hashCode() {

		final int inicial = 47;
		int codigo = inicial; // Semente
		if (this.siglaSecaoSubSecao != null) {
			codigo = codigo * inicial + this.siglaSecaoSubSecao.hashCode();
		}
		if (this.codFuncionario != null) {
			codigo = codigo * inicial + this.codFuncionario.hashCode();
		}
		return codigo;

	}

	/**
	 * @param codFuncionario
	 *            Atribui a codFuncionario o valor.
	 */
	public void setCodFuncionario(final Integer codFuncionario) {
		this.codFuncionario = codFuncionario;
	}

	/**
	 * @param email
	 *            Atribui a email o valor.
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @param lotacaoAtual
	 *            Atribui a lotacaoAtual o valor.
	 */
	public void setLotacaoAtual(final Lotacao lotacaoAtual) {
		this.lotacaoAtual = lotacaoAtual;
	}

	/**
	 * @param siglaSecaoSubsecao
	 *            Atribui a siglaSecaoSubsecao o valor.
	 */
	public void setSiglaSecaoSubsecao(final String siglaSecaoSubsecao) {
		this.siglaSecaoSubSecao = siglaSecaoSubsecao;
	}

	@Override
	public String getCargoDescricao() {
		return "Funcionario";
	}

	@Override
	public String getIdentificador() {
		return siglaSecaoSubSecao + codFuncionario;
	}
}
