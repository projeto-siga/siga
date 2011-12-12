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
package br.gov.jfrj.siga.wf.webwork.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

import org.jbpm.graph.def.Transition;

/**
 * Classe que repesenta um View Object (Objeto Visão, ou seja, objeto que será
 * usado na exibição da interface do usuário) de uma variável presente em uma
 * tarefa.
 * 
 * @author tah
 * 
 */
public class WfTransitionVO implements Comparable {
	private String name;
	private String resp;

	public WfTransitionVO(Transition t, Set<String> lResp)
			throws IllegalAccessException, InvocationTargetException {
		this.name = t.getName();
		this.resp = "";
		for (String s : lResp) {
			if (this.resp.length() > 0)
				this.resp += ", ";
			this.resp += s;
		}
		if (this.resp.length() > 0)
			this.resp = " &raquo; " + this.resp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResp() {
		return resp;
	}

	public void setResp(String resp) {
		this.resp = resp;
	}

	public int compareTo(Object o) {
		WfTransitionVO other = (WfTransitionVO) o;
		if (this.getName() == null) {
			return 1;
		}
		if (other.getName() == null) {
			return -1;
		}
		int i = this.getName().compareToIgnoreCase(other.getName());
		if (i != 0)
			return i;
		return ((Integer) this.hashCode()).compareTo(other.hashCode());
	}
}
