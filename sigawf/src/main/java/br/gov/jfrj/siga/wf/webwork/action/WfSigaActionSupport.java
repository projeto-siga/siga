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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

public class WfSigaActionSupport extends SigaActionSupport {

	public boolean matchSwimlane(String swimlane) {
		return ((swimlane.equals(getTitular().getSigla())) || (swimlane
				.charAt(0) == '@' && swimlane.equals("@"
				+ getLotaTitular().getOrgaoUsuario().getSigla()
				+ getLotaTitular().getSigla())));
	}

	public void assertAcesso(String pathServico) throws AplicacaoException,
			Exception {
		super.assertAcesso("WF:Módulo de Workflow;" + pathServico);
	}

	public WfDao dao() {
		return WfDao.getInstance();
	}

	/**
	 * Retorna as definições de processos.
	 * 
	 * @return
	 */
	public List<ProcessDefinition> getProcessDefinitions() {
		List<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();
		GraphSession graph = WfContextBuilder.getJbpmContext()
				.getGraphSession();

		for (ProcessDefinition pd : (Collection<ProcessDefinition>) graph
				.findLatestProcessDefinitions()) {
			// if (Wf.getInstance().getComp().podeInstanciarProcedimento(
			// getTitular(), getLotaTitular(), pd.getName()))
			processDefinitions.add(pd);
		}
		return processDefinitions;
	}

}
