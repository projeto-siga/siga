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

import java.util.List;

import org.jbpm.JbpmConfiguration;
import org.jbpm.graph.def.ProcessDefinition;

/**
 * Classe utilitária utilizada para testar o contexto do JBPM.
 * 
 * @author kpf
 * 
 */
public class WfContextTest {

	private static final String PROCEDIMENTO_A_REMOVER = "testeNovo";

	/**
	 * Remove um procedimento com todas as suas versões. CUIDADO AO USAR ESTE
	 * MÉTODO!.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		JbpmConfiguration conf = WfContextBuilder.getConfiguration();
		WfJbpmContext ctx = WfContextBuilder.getJbpmContext();

		// Rotina para remover procedimentos. Utilizar com muito cuidado!
		System.out.println("Removendo procedimento: " + PROCEDIMENTO_A_REMOVER);
		for (ProcessDefinition pd : (List<ProcessDefinition>) ctx
				.getGraphSession().findAllProcessDefinitionVersions(
						PROCEDIMENTO_A_REMOVER)) {
			if (pd.getVersion() != 19 && pd.getVersion() != 16) {
				System.out.println("Removendo versão: " + pd.getVersion());
				ctx.getGraphSession().deleteProcessDefinition(pd);
			}
		}
		System.out.println("Removido: " + PROCEDIMENTO_A_REMOVER);
		ctx.getJbpmContext().close();
	}
}
