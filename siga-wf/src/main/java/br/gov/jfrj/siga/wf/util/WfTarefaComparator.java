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
package br.gov.jfrj.siga.wf.util;

import java.util.Comparator;

public class WfTarefaComparator implements Comparator<WfTarefa> {

	/**
	 * Compara dois objetos TaskInstance. Retorna zero (0) se os objetos são
	 * "iguais", retorna (1) se a PRIORIDADE ou ID do primeiro objeto (o1) for maior
	 * doque o segundo objeto (o2). Retorna (-1), caso contrário. ESTE CÓDIGO ESTÁ
	 * DUPLICADO EM WfDocumentoAction.java.
	 */
	public int compare(WfTarefa o1, WfTarefa o2) {
//		if (o1.getId() > o2.getId())
//			return 1;
//		if (o1.getId() < o2.getId())
//			return -1;
		return 0;
	}
}
