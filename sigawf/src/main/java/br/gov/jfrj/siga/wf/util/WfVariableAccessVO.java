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

import java.lang.reflect.InvocationTargetException;

import org.jbpm.context.def.VariableAccess;

/**
 * Classe que repesenta um View Object (Objeto Visão, ou seja, objeto que será
 * usado na exibição da interface do usuário) de uma variável presente em uma
 * tarefa.
 * 
 * @author tah
 * 
 */
public class WfVariableAccessVO extends VariableAccess {
	private String respEX;
	private String respWF;

	public WfVariableAccessVO(VariableAccess va) throws IllegalAccessException,
			InvocationTargetException {
		this.access = va.getAccess();
		this.mappedName = va.getMappedName();
		this.variableName = va.getVariableName();
	}

	public boolean isAviso() {
		return respEX != null || respWF != null;
	}

	public String getRespEX() {
		return respEX;
	}

	public void setRespEX(String respEX) {
		this.respEX = respEX;
	}

	public String getRespWF() {
		return respWF;
	}

	public void setRespWF(String respWF) {
		this.respWF = respWF;
	}

}
