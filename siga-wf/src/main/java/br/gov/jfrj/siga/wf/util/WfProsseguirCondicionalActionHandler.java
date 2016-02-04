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

import java.util.Collection;
import java.util.List;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class WfProsseguirCondicionalActionHandler implements ActionHandler {

	/**
	 * Faz um 'sinal' na transição de saída que estiver habilitada. Caso não
	 * exista nenhuma condição de saída com 'condition' == true, permanece na
	 * mesma tarefa.
	 * 
	 * @param executionContext
	 * @throws Exception
	 */
	public void execute(ExecutionContext executionContext) throws Exception {
		Token to = executionContext.getToken();
		for (TaskInstance ti : (Collection<TaskInstance>) (executionContext
				.getTaskMgmtInstance().getUnfinishedTasks(to))) {
			for (Transition t : (List<Transition>) ti.getAvailableTransitions()) {
				if (t.getCondition() != null) {
					if (to.isLocked())
						to.unlock("token[" + to.getId() + "]");
					ti.end(t);
				}
			}
		}
	}

}
