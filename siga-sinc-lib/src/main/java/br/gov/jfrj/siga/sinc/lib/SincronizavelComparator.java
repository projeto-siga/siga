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

public class SincronizavelComparator implements Comparator<Sincronizavel> {

	public int compare(Sincronizavel o1, Sincronizavel o2) {
		if (o1 == o2)
	        return 0;
		int i = o1.getClass().getName().compareTo(o2.getClass().getName());
		if (i != 0)
			return i;
		return o1.getIdExterna().compareTo(o2.getIdExterna());
	}

}
