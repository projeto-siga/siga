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

import java.lang.reflect.Method;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

/**
 * Action personalizada que sempre passa o executionContext nas chamadas dos métodos. 
 * @author kpf
 *
 */
public abstract class WfActionHandler implements ActionHandler {

	private String method;

	/**
	 * Passa o executionContext nas chamadas dos métodos
	 */
	public void execute(ExecutionContext executionContext) throws Exception {
		if (method == null)
			throw new Exception("Não foi associado um Action.");
		else {
			Method m = this.getClass()
					.getMethod(method, ExecutionContext.class);
			if (m == null)
				throw new Exception("Action não encontrado.");
			m.invoke(this, executionContext);
		}
	}

	/**
	 * Retorna o método a ser invocado.
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Informa o método a ser invocado.
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

}
