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
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

import com.opensymphony.xwork.Action;

/**
 * Classe reponsável pelas actions relativas à caixa de entrada.
 * 
 * @author kpf
 * 
 */
public class WfInboxAction extends WfSigaActionSupport {

	protected List<ProcessDefinition> processDefinitions;

	protected SortedSet<TaskInstance> taskInstances;

	protected TaskInstance taskInstance;

	protected Long pdId;

	protected Long tiId;

	protected Map<String, String> mapDescricao = new TreeMap<String, String>();

	/**
	 * Retorna a sigla do primeiro ator que faça parte do pool de atores na task
	 * instance.
	 * 
	 * @param ti
	 * @return
	 */
	public String getPooledActors(TaskInstance ti) {
		if (ti.getActorId() != null)
			return ti.getActorId();
		if (ti.getPooledActors().size() == 0)
			return "";
		String s = ((PooledActor) ti.getPooledActors().toArray()[0])
				.getActorId();
		if (s.startsWith(getLotaTitular().getOrgaoUsuario().getSigla()))
			s = s.substring(getLotaTitular().getOrgaoUsuario().getSigla()
					.length());
		return s;
	}

	public String getDescricao(String sigla) {
		if (mapDescricao.containsKey(sigla)) {
			return mapDescricao.get(sigla);
		}
		DpPessoa pes = daoPes(sigla);
		if (pes != null) {
			mapDescricao.put(sigla, pes.getDescricao());
			return pes.getDescricao();
		}
		DpLotacao lot = daoLot(getLotaTitular().getOrgaoUsuario().getSigla()
				+ sigla);
		if (lot != null) {
			mapDescricao.put(sigla, lot.getDescricao());
			return lot.getDescricao();
		}
		return "";
	}

	/**
	 * Retorna a sigla do ator a quem está atribuida a tarefa, ou a sigla da
	 * lotação se a tarefa estiver no pool. Se a tarefa estiver com o titular,
	 * retorna "(minha)"
	 * 
	 * 
	 * @param ti
	 * @return
	 */
	public String getAtendente(TaskInstance ti) {
		if (ti.getActorId() != null) {
			if (ti.getActorId().equals(getTitular().getSigla()))
				return "(minha)";
			else
				return ti.getActorId();
		}
		if (ti.getPooledActors().size() == 0)
			return "";
		String s = ((PooledActor) ti.getPooledActors().toArray()[0])
				.getActorId();
		if (s.startsWith(getLotaTitular().getOrgaoUsuario().getSigla()))
			s = s.substring(getLotaTitular().getOrgaoUsuario().getSigla()
					.length());
		return s;
	}

	/**
	 * Coloca as tarefas que estão para o usuário no atributo "taskInstances".
	 * Além disso, coloca os procedimentos que podem ser iniciados pelo usuário
	 * do sistema no atributo "processDefinitions".Este método é executando ao
	 * ser chamada a action "inbox.action".
	 * 
	 * @return
	 * @throws Exception
	 */
	public String loadInbox() throws Exception {
		GraphSession graph = WfContextBuilder.getJbpmContext()
				.getGraphSession();
		taskInstances = Wf.getInstance().getBL().getTaskList(getCadastrante(),
				getTitular(), getLotaTitular());
		// processDefinitions = graph.findLatestProcessDefinitions();
		processDefinitions = new ArrayList<ProcessDefinition>();
		for (ProcessDefinition pd : (Collection<ProcessDefinition>) graph
				.findLatestProcessDefinitions()) {
			// if (Wf.getInstance().getComp().podeInstanciarProcedimento(
			// getTitular(), getLotaTitular(), pd.getName()))
			processDefinitions.add(pd);
		}

		return Action.SUCCESS;
	}

	/**
	 * Prove uma url de teste para a inbox do workflow.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String test() throws Exception {
		DpPessoa pes = daoPes(param("matricula"));
		setTitular(pes);
		setLotaTitular(pes.getLotacao());
		return loadInbox();
	}

	/**
	 * Cria uma instância de processo baseando-se na definição de processo
	 * escolhida pelo usuário.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initializeProcess() throws Exception {
		if (pdId == null)
			throw new RuntimeException();

		GraphSession graph = WfContextBuilder.getJbpmContext()
				.getGraphSession();
		ProcessInstance pi = Wf.getInstance().getBL().createProcessInstance(
				pdId, getCadastrante(), getCadastrante().getLotacao(),
				getTitular(), getLotaTitular(), null, null, true);
		for (TaskInstance ti : ((List<TaskInstance>) WfContextBuilder
				.getJbpmContext().getJbpmContext().getTaskList(
						getTitular().getSigla()))) {
			if (ti.getProcessInstance().getId() == pi.getId()) {
				tiId = ti.getId();
				return "task";
			}
		}

		return Action.SUCCESS;
	}

	/**
	 * Retorna as definições de processos.
	 * 
	 * @return
	 */
	public List<ProcessDefinition> getProcessDefinitions() {
		return processDefinitions;
	}

	/**
	 * Define as definições de processos.
	 * 
	 * @param processDefinitions
	 */
	public void setProcesssDefinitions(
			List<ProcessDefinition> processDefinitions) {
		this.processDefinitions = processDefinitions;
	}

	/**
	 * Retorna o ID da definição de processo.
	 * 
	 * @return
	 */
	public Long getPdId() {
		return pdId;
	}

	/**
	 * Define o ID da definição de processo.
	 * 
	 * @param pdId
	 */
	public void setPdId(Long pdId) {
		this.pdId = pdId;
	}

	/**
	 * Retorna a instância de tarefa.
	 * 
	 * @return
	 */
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	/**
	 * Define a instância de tarefa.
	 * 
	 * @param taskInstance
	 */
	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	/**
	 * Retorna o ID da instância de tarefa.
	 * 
	 * @return
	 */
	public Long getTiId() {
		return tiId;
	}

	/**
	 * Define o ID da instância de tarefa.
	 * 
	 * @param tiId
	 */
	public void setTiId(Long tiId) {
		this.tiId = tiId;
	}

	/**
	 * Retorna o conjunto de instâncias de tarefas.
	 * 
	 * @return
	 */
	public SortedSet<TaskInstance> getTaskInstances() {
		return taskInstances;
	}

	/**
	 * Define o conjunto de instâncias de tarefas.
	 * 
	 * @param taskInstances
	 */
	public void setTaskInstances(SortedSet<TaskInstance> taskInstances) {
		this.taskInstances = taskInstances;
	}

	/**
	 * Define a lista de difinições de processos.
	 * 
	 * @param processDefinitions
	 */
	public void setProcessDefinitions(List<ProcessDefinition> processDefinitions) {
		this.processDefinitions = processDefinitions;
	}

}
