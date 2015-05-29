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
package br.gov.jfrj.siga.cd;

/**
 * Uma classe que encapsula pares de objetos. Inspirado no template pair<>
 * da STL.
 * 
 * @param <T>
 *            O primeiro tipo.
 * @param <U>
 *            O segundo tipo.
 */
public class Pair<T, U> {
	/** O primeiro objeto. */
	public T first;

	/** O segundo objeto. */
	public U second;

	/** Construtor */
	public Pair(T t, U u) {
		first = t;
		second = u;
	}
}

