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
package br.gov.jfrj.siga.cd.old;

/**
 * @author mparaiso
 * 
 * Classe de Exceção disparada quando ocorre algum problema com a validação de
 * uma cadeia de certificação.
 * 
 */
public class ChainValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5349880311187152311L;

	public ChainValidationException(final String msg) {

		super(msg);
	}

	public ChainValidationException(final String msg, final Exception e) {

		super(msg, e);
	}
}
