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
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.rh;

/**
 * Representaçaõ da tabela rh_contratados
 * 
 * @author SEANS
 * 
 */
public class Contratado extends Pessoa {
	private String email;

	private String identificacao;

	private Lotacao lotacaoAtual;

	private Integer matricula;

	private String siglaSecaoSubSecao;

	@Override
	public boolean equals(final Object obj) {

		if ((obj == null) || !(obj instanceof Contratado))
			return false;
		final Contratado fun = (Contratado) obj;
		/*
		 * o funcionamento do operador ternário condição ? valorVerdadeiro :
		 * valorFalso
		 */
		if ((this.siglaSecaoSubSecao == null ? fun.siglaSecaoSubSecao == null : this.siglaSecaoSubSecao
				.equals(fun.siglaSecaoSubSecao))) {
			if ((this.matricula == null ? fun.matricula == null : this.matricula.equals(fun.matricula)))
				return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.jfrj.siga.Funcionario#getCodFuncionario()
	 */
	public Integer getCodFuncionario() {
		return this.getMatricula();
	}

	/**
	 * @return Retorna o atributo email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return Retorna o atributo identificacao.
	 */
	public String getIdentificacao() {
		return identificacao;
	}

	/**
	 * @return Retorna o atributo lotacaoAtual.
	 */
	@Override
	public Lotacao getLotacaoAtual() {
		return lotacaoAtual;
	}

	/**
	 * @return Retorna o atributo matricula.
	 */
	public Integer getMatricula() {
		return matricula;
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
	 * @return Retorna o atributo siglaSecaoSubSecao.
	 */
	public String getSiglaSecaoSubSecao() {
		return siglaSecaoSubSecao;
	}

	@Override
	public int hashCode() {

		final int inicial = 47;
		int codigo = inicial; // Semente
		codigo = codigo * inicial + (this.siglaSecaoSubSecao != null ? 0 : this.siglaSecaoSubSecao.hashCode());
		codigo = codigo * inicial + (this.matricula != null ? 0 : this.matricula.hashCode());

		return codigo;

	}

	/**
	 * @param email
	 *            Atribui a email o valor.
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @param identificacao
	 *            Atribui a identificacao o valor.
	 */
	public void setIdentificacao(final String identificacao) {
		this.identificacao = identificacao;
	}

	/**
	 * @param lotacaoAtual
	 *            Atribui a lotacaoAtual o valor.
	 */
	public void setLotacaoAtual(final Lotacao lotacaoAtual) {
		this.lotacaoAtual = lotacaoAtual;
	}

	/**
	 * @param matricula
	 *            Atribui a matricula o valor.
	 */
	public void setMatricula(final Integer matricula) {
		this.matricula = matricula;
	}

	/**
	 * @param siglaSecaoSubSecao
	 *            Atribui a siglaSecaoSubSecao o valor.
	 */
	public void setSiglaSecaoSubSecao(final String siglaSecaoSubSecao) {
		this.siglaSecaoSubSecao = siglaSecaoSubSecao;
	}

	@Override
	public String getCargoDescricao() {
		return "Contratado";
	}

	@Override
	public String getIdentificador() {
		return siglaSecaoSubSecao + matricula;
	}

}
