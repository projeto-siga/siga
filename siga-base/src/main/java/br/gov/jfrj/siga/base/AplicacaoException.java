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
 *
 * 
 */
package br.gov.jfrj.siga.base;


/**
 * @author SEANS Classe padrão para tratamenteo de erros nos Sistemas
 * 
 */
public class AplicacaoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3346798706685517274L;
	private int codigoErro;

	/**
	 * Construtor padrão para a Classe
	 * 
	 */
	public AplicacaoException() {
		this(null, 0, null);

	}

	/**
	 * Construtor da Classe que atribui uma mensagem
	 * 
	 * @param message -
	 *            Descrição do motivo da exceção
	 */
	public AplicacaoException(final String message) {
		this(message, 0);

	}

	/**
	 * 
	 * Construtor da Classe que atribui uma mensagem e código de Identificação
	 * 
	 * @param message -
	 *            Descrição do motivo da exceção
	 * @param codigo -
	 *            Código de identificação da exceção para
	 */
	public AplicacaoException(final String message, final int codigo) {
		this(message, codigo, null);
	}

	/**
	 * 
	 * Construtor da Classe que atribui uma mensagem, um código de Identificação
	 * e uma causa para a exceção
	 * 
	 * @param message -
	 *            Descrição do motivo da exceção
	 * @param codigo -
	 *            Código de identificação da exceção para
	 * @param causa -
	 *            Objeto da classe Throwable que gerou esta exceção
	 */
	public AplicacaoException(final String message, final int codigo, final Throwable causa) {
		super(message, causa);
		this.codigoErro = codigo;
	}

	/**
	 * @return Retorna o atributo codigo.
	 */
	public int getCodigoErro() {
		return codigoErro;
	}
}
