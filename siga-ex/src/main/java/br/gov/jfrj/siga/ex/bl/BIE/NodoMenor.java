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

import br.gov.jfrj.siga.ex.ExDocumento;

public class NodoMenor extends Nodo {
	private List<ExDocumento> exDocumentoSet;
	private Comparator<ExDocumento> comparator;

	public NodoMenor(String descr, Comparator<ExDocumento> comparator) {
		super(descr);
		setComparator(comparator);
		exDocumentoSet = new ArrayList<ExDocumento>();
	}

	public void ordena() {
		Collections.sort(getExDocumentoSet(), getComparator());
	}

	public boolean isVazio() {
		return (exDocumentoSet.size() == 0);
	}

	public Comparator<ExDocumento> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<ExDocumento> comparator) {
		this.comparator = comparator;
	}

	public List<ExDocumento> getExDocumentoSet() {
		return exDocumentoSet;
	}

	public void setExDocumentoSet(List<ExDocumento> exDocumentoSet) {
		this.exDocumentoSet = exDocumentoSet;
	}

};
