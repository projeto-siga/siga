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
package br.gov.jfrj.siga.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.hibernate.Query;
import org.jbpm.JbpmContext;
import org.jbpm.context.def.VariableAccess;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.bl.WfBL;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.service.WfService;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;
import br.gov.jfrj.siga.wf.webwork.action.WfTaskAction;

/**
 * Classe que representa o webservice do workflow. O SIGA-DOC faz a chamada
 * remota dos métodos dessa classe para atualizar o workflow automaticamente,
 * baseando-se nas operações sobre documentos.
 * 
 * @author kpf
 * 
 */
@WebService(endpointInterface = "br.gov.jfrj.siga.wf.service.WfService")
public class WfServiceImpl implements WfService {

	private boolean hideStackTrace = false;

	public boolean isHideStackTrace() {
		return hideStackTrace;
	}

	public void setHideStackTrace(boolean hideStackTrace) {
		this.hideStackTrace = hideStackTrace;
	}

	/**
	 * Atualiza o workflow de um documento. Este método pesquisa todas as
	 * variáveis que começam com "doc_" em todas as instâncias de tarefas.
	 * Quando encontra a variável, passa a sigla do documento para o execution
	 * execution context e executa a ação da tarefa.
	 */
	public Boolean atualizarWorkflowsDeDocumento(String codigoDocumento)
			throws Exception {
		try {
			Boolean b = false;
			GraphSession graph = WfContextBuilder.getJbpmContext()
					.getGraphSession();
			JbpmContext ctx = WfContextBuilder.getJbpmContext()
					.getJbpmContext();
			// List<TaskInstance> tis = ctx.getTaskList();

			Query q = WfDao
					.getInstance()
					.getSessao()
					.createQuery(
							"select ti from org.jbpm.taskmgmt.exe.TaskInstance as ti"
									+ " where ti.isSuspended != true and ti.isOpen = true");
			List<TaskInstance> tis = q.list();

			for (TaskInstance ti : tis) {
				if (ti.getTask().getTaskController() != null) {
					List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
							.getTask().getTaskController()
							.getVariableAccesses();
					for (VariableAccess variable : variableAccesses) {
						if (variable.getMappedName() != null
								&& variable.getMappedName().startsWith("doc_")
								&& variable.isReadable()
								&& !variable.isWritable()) {
							String mapping = variable.getMappedName();
							String value = (String) ti.getContextInstance()
									.getVariable(mapping);
							if (value != null
									&& value.startsWith(codigoDocumento)) {
								ExecutionContext ec = new ExecutionContext(ti
										.getToken());
								ec.setTaskInstance(ti);
								ec.setTask(ti.getTask());
								ti.getTask().fireEvent("context-change", ec);
								
								executarAcoes(codigoDocumento, ti);
								
								b = true;
							}
						}
					}
				}
			}
			return b;
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	/**
	 * Executa ações na tarefa baseando-se nos serviços externos associados à tarefa.
	 * 
	 * @param codigoDocumento
	 * @param ti
	 * @throws Exception
	 */
	private void executarAcoes(String codigoDocumento, TaskInstance ti)
			throws Exception {
		ExService exSvc = Service.getExService();
		WfBL bl = Wf.getInstance().getBL();
		WfDao dao = WfDao.getInstance();
		
		if (exSvc.isSemEfeito(codigoDocumento)){
			bl.encerrarProcessInstance(ti.getProcessInstance().getId(), dao.consultarDataEHoraDoServidor());
		}
	}

	/**
	 * Inicia um novo procedimento.
	 */
	public Boolean criarInstanciaDeProcesso(String nomeProcesso,
			String siglaCadastrante, String siglaTitular,
			ArrayList<String> keys, ArrayList<String> values) throws Exception {

		try {
			if (nomeProcesso == null)
				throw new RuntimeException();
			GraphSession graph = WfContextBuilder.getJbpmContext()
					.getGraphSession();
			ProcessDefinition pd = graph
					.findLatestProcessDefinition(nomeProcesso);
			if (pd == null)
				throw new RuntimeException(
						"Não foi encontrado um ProcessDefinition com o nome '"
								+ nomeProcesso + "'");
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			PessoaLotacaoParser titularParser = new PessoaLotacaoParser(
					siglaTitular);

			ProcessInstance pi = Wf.getInstance().getBL()
					.createProcessInstance(pd.getId(),
							cadastranteParser.getPessoa(),
							cadastranteParser.getLotacao(),
							titularParser.getPessoa(),
							titularParser.getLotacao(), keys, values, false);

			WfTaskAction.transferirDocumentosVinculados(pi, siglaTitular);
			return true;
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

}
