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
package br.gov.jfrj.siga.sinc.lib;

import java.util.Comparator;

import br.gov.jfrj.siga.sinc.lib.Item.Operacao;

public class ItemComparator implements Comparator<Item> {

	public int compare(Item o1, Item o2) {
		int i = Integer.valueOf(o1.getNivelDeDependencia()).compareTo(
				o2.getNivelDeDependencia());
		if (i != 0)
			return i;
		i = o1.getOperacao().compareTo(o2.getOperacao());
		if (i != 0)
			return i;
		return Integer.valueOf(o1.hashCode()).compareTo(o2.hashCode());
	}

}
