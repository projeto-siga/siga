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
package br.gov.jfrj.siga.ex.bl.BIE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NodoMaior extends Nodo {
	public List<NodoMenor> nodos;
	private Comparator<Nodo> comparator;

	public NodoMaior(String descr) {
		this(descr, null);
	}

	public NodoMaior(String descr, Comparator<Nodo> comparator) {
		super(descr);
		nodos = new ArrayList<NodoMenor>();
		this.comparator = comparator;
	}

	public boolean contemNodo(String descr) {
		for (NodoMenor nm : nodos)
			if (nm.getDescr().equals(descr))
				return true;
		return false;
	}

	public boolean isVazio() {
		if (nodos.size() == 0)
			return true;
		for (NodoMenor n : nodos)
			if (!n.isVazio())
				return false;
		return true;
	}

	public void ordena() {
		if (comparator != null)
			Collections.sort(nodos, comparator);
	}

	public int indiceDe(String descr) {
		for (int k = 0; k < nodos.size(); k++)
			if (nodos.get(k).getDescr().equals(descr))
				return k;
		return -1;
	}

	public List<NodoMenor> getNodos() {
		return nodos;
	}

	public void setNodos(List<NodoMenor> nodos) {
		this.nodos = nodos;
	}
};
