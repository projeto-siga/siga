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

import org.jbpm.module.exe.ModuleInstance;
import org.jbpm.taskmgmt.def.TaskMgmtDefinition;

/**
 * Redefine um TaskMgmtDefinition. Atualmente não está com as definições
 * personalizada, ou seja, está com o comportamento padrão do JBPM.
 * 
 * @author kpf
 * 
 */
public class WfTaskMgmtDefinition extends TaskMgmtDefinition {

	/**
	 * Cria uma instância do módulo.
	 */
	@Override
	public ModuleInstance createInstance() {
		// return new TaskMgmtInstance(this);
		// TODO Auto-generated method stub
		return super.createInstance();
	}

}
