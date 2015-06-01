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
package br.gov.jfrj.siga.wf.bl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.instantiation.Delegation;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Classe que representa a lógica do negócio do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfBL extends CpBL {
	public static final String WF_CADASTRANTE = "wf_cadastrante";
	public static final String WF_LOTA_CADASTRANTE = "wf_lota_cadastrante";
	public static final String WF_TITULAR = "wf_titular";
	public static final String WF_LOTA_TITULAR = "wf_lota_titular";
	private static TaskInstanceComparator tic = new TaskInstanceComparator();

	/**
	 * Cria uma instância de processo. Ao final da criação, define as seguintes
	 * variáveis na instância do processo: WF_CADASTRANTE - Pessoa responsável
	 * (que responde) pelas ações realizadas no sistema no momento da criação da
	 * instância do processo WF_LOTA_CADASTRANTE - Lotação da pessoa que está
	 * operando o sistema no momento da criação da instância do processo
	 * WF_TITULAR - Pessoa responsável (que responde) pelas ações realizadas no
	 * sistema no momento da criação da instância do processo WF_LOTA_TITULAR -
	 * Lotação da pessoa responsável (que responde) pelas ações realizadas no
	 * sistema no momento da criação da instância do processo
	 * 
	 * Essas variáveis são do banco de dados corporativo.
	 * 
	 * @param pdId
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param titular
	 * @param lotaTitular
	 * @param keys
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public ProcessInstance createProcessInstance(long pdId,
			DpPessoa cadastrante, DpLotacao lotaCadastrante, DpPessoa titular,
			DpLotacao lotaTitular, ArrayList<String> keys,
			ArrayList<String> values, boolean fCreateStartTask)
			throws Exception {
		GraphSession graph = WfContextBuilder.getJbpmContext()
				.getGraphSession();
		ProcessDefinition pd = graph.loadProcessDefinition(pdId);
		// ProcessInstance processInstance = pd.createProcessInstance();
		// ExecutionContext ec = new ExecutionContext(processInstance
		// .getRootToken());
		// // processInstance.getRootToken().getNode().leave(ec);
		// processInstance.getRootToken().signal();
		// WfContextBuilder.getJbpmContext().getJbpmContext()
		// .save(processInstance);
		// return processInstance;

		final ProcessInstance pi = pd.createProcessInstance();
		// Signal the root token based on the following criteria:
		// 1. If there is no start task, and
		// 2. If the root token is still at the start state, and
		// 3. If the start state has a default leaving transition, then
		// signal the token along the default transition.
		// context.addSuccessMessage("Started process");

		pi.getContextInstance().setVariable(WF_CADASTRANTE,
				cadastrante.getSigla());
		pi.getContextInstance().setVariable(WF_LOTA_CADASTRANTE,
				lotaCadastrante.getSiglaCompleta());
		pi.getContextInstance().setVariable(WF_TITULAR, titular.getSigla());
		pi.getContextInstance().setVariable(WF_LOTA_TITULAR,
				lotaTitular.getSiglaCompleta());

		if (keys != null && values != null) {
			for (int n = 0; n < keys.size(); n++) {
				pi.getContextInstance().setVariable(keys.get(n), values.get(n));
			}
		}

		final TaskMgmtInstance taskMgmtInstance = pi.getTaskMgmtInstance();
		TaskInstance startTaskInstance = null;
		if (fCreateStartTask)
			startTaskInstance = taskMgmtInstance.createStartTaskInstance();

		/*
		 * next piece causes NPE. and i don't think it is needed to signal a new
		 * process automatically. that can be done in the console itself as
		 * well. TODO it would be nice if the console automatically navigated to
		 * the screen where you can see the root token and actually give the
		 * signal
		 */

		if (startTaskInstance == null) {
			// There is no start task
			final Node initialNode = pd.getStartState();
			final Token rootToken = pi.getRootToken();
			final Node rootTokenNode = rootToken.getNode();
			if (initialNode.getId() == rootTokenNode.getId()) {

				Task startTaskDefinition = initialNode.getProcessDefinition()
						.getTaskMgmtDefinition().getStartTask();

				if (startTaskDefinition != null) {
					SwimlaneInstance si = pi
							.getTaskMgmtInstance()
							.createSwimlaneInstance(
									startTaskDefinition.getSwimlane().getName());
					si.setActorId(titular.getSigla());
					String[] actorsId = new String[1];
					actorsId[0] = lotaTitular.getSiglaCompleta();
					si.setPooledActors(actorsId);
					pi.getTaskMgmtInstance().addSwimlaneInstance(si);
				}
				// The root token is still at the start node
				final Transition defaultLeavingTransition = initialNode
						.getDefaultLeavingTransition();
				if (defaultLeavingTransition != null) {
					// There's a default transition
					rootToken.signal(defaultLeavingTransition);
					// context.addSuccessMessage("Signalled root token");
				}
			} 
		} else {
			startTaskInstance.assign(new ExecutionContext(startTaskInstance.getToken()));
		}

		// context.selectOutcome("started");
		if (pi.hasEnded()) {
			// context.selectOutcome("finished");
			// context.addSuccessMessage("Process completed");
		}

		// if (instanceExpression != null) {
		// try {
		// instanceExpression.setValue(elContext, instance);
		// } catch (ELException ex) {
		// context.setError("Error setting value of "
		// + instanceExpression.getExpressionString(), ex);
		// return;
		// }
		// }

		// Nothing else saves the process, so we must
		WfContextBuilder.getJbpmContext().getJbpmContext().save(pi);
		return pi;
	}

	/**
	 * Retorna o conjunto de tarefas que estão na responsabilidade do usuário.
	 * 
	 * @throws AplicacaoException
	 */
	public SortedSet<TaskInstance> getTaskList(DpPessoa cadastrante,
			DpPessoa titular, DpLotacao lotaTitular) throws AplicacaoException {
		SortedSet<TaskInstance> tasks = new TreeSet<TaskInstance>(tic);
		boolean fActorIdNaLista = false;

		List<String> actorIds = new ArrayList<String>(2);
		actorIds.add(lotaTitular.getSiglaCompleta());
		actorIds.add(titular.getSigla());

		// Acrescenta os tasks do titular
		// tasks.addAll(WfContextBuilder.getJbpmContext().getJbpmContext()
		// .getTaskList(titular.getSigla()));

		// Acrescenta os tasks das pessoas da lotação. Se o usuário estiver
		// substituindo uma lotação, seu actorId deve ser adicionado à lista
		// posteriormente.
		List<DpPessoa> pessoas = dao().pessoasPorLotacao(lotaTitular.getId(),
				false,true);
		if (pessoas != null) {
			List<String> aActorIds = new ArrayList<String>(pessoas.size());
			int i = 0;
			for (DpPessoa pes : pessoas) {
				aActorIds.add(pes.getSigla());
				if (pes.getSigla().equals(titular.getSigla()))
					fActorIdNaLista = true;
				i++;
			}
			if (!fActorIdNaLista)
				aActorIds.add(titular.getSigla());

			tasks.addAll(WfContextBuilder.getJbpmContext().getJbpmContext()
					.getTaskMgmtSession().findTaskInstances(aActorIds));
		}

		tasks.addAll(getGroupTaskList(actorIds));
		return tasks;
	}

	/*
	 * Esse método é necessário pois o método getGroupTaskList de JbpmContext
	 * gera uma exceção sempre que nenhum task é encontrado.
	 */
	private static List<TaskInstance> getGroupTaskList(List<String> actorIds) {
		// Acrescenta os tasks que não tem actorId, mas que possuem a
		// lotaTitular ou o titular no seu pool de atores
		List taskInstanceIds = WfDao.getInstance().getSessao().getNamedQuery(
				"TaskMgmtSession.findPooledTaskInstancesByActorIds")
				.setParameterList("actorIds", actorIds).list();
		List<TaskInstance> taskInstances = null;
		if (taskInstanceIds != null && taskInstanceIds.size() > 0) {
			taskInstances = WfDao.getInstance().getSessao().getNamedQuery(
					"TaskMgmtSession.findTaskInstancesByIds").setParameterList(
					"taskInstanceIds", taskInstanceIds).list();
			return taskInstances;
		}
		return new ArrayList<TaskInstance>();
	}

	public SortedSet<TaskInstance> getTaskList(String siglaDoc) {
		SortedSet<TaskInstance> tasks = new TreeSet<TaskInstance>(tic);
		tasks.addAll(WfDao.getInstance().consultarTarefasAtivasPorDocumento(
				siglaDoc));
		return tasks;
	}

	public static Boolean podePegarTarefa(DpPessoa cadastrante,
			DpPessoa titular, DpLotacao lotaCadastrante, DpLotacao lotaTitular,
			TaskInstance ti) {
		Set<TaskInstance> tasks = new TreeSet<TaskInstance>(tic);
		List<String> actorIds = new ArrayList<String>();
		actorIds.add(lotaTitular == null ? lotaCadastrante.getSiglaCompleta()
				: lotaTitular.getSiglaCompleta());

		tasks.addAll(getGroupTaskList(actorIds));

		for (TaskInstance t : tasks) {
			if (t.getId() == ti.getId()) {
				return true;
			}
		}

		
		if (ti.getActorId() != null){
			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(ti.getActorId());
			DpPessoa actor = (DpPessoa) WfDao.getInstance().consultarPorSigla(flt);
			if (cadastrante.getLotacao().equivale(actor.getLotacao()) ||
					(lotaTitular != null && lotaTitular.equivale(actor.getLotacao()))){
				return true;
			}
		}
		
		return false;
	}

	private WfDao dao() {
		return (WfDao) getComp().getConfiguracaoBL().dao();
	}

	public Comparator<? super TaskInstance> getTaskInstanceComparator() {
		return tic;
	}

	/**
	 * Finaliza todas as Processes Instances que estiverem abertas.
	 * @throws AplicacaoException 
	 */
	public void encerrarTodasProcessesInstances(String nomeProcesso) throws AplicacaoException {
		List<ProcessDefinition> pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findAllProcessDefinitionVersions(nomeProcesso);

		for (ProcessDefinition p : pd) {
			List<ProcessInstance> instanciasProcesso = WfDao.getInstance().consultarInstanciasDoProcessInstance(p.getId());

			for (ProcessInstance pi : instanciasProcesso) {
				encerrarProcessInstance(pi.getId(), new Date());
			}

		}
		WfContextBuilder.closeContext();

	}

	/**
	 * Encerra uma process instance com uma data de fim específica.
	 * 
	 * @param idProcessInstance
	 * @param dataFim
	 * @throws AplicacaoException
	 */
	public void encerrarProcessInstance(Long idProcessInstance, Date dataFim)
			throws AplicacaoException {
		Calendar cDtFim = Calendar.getInstance();
		cDtFim.setTime(dataFim);
		ProcessInstance pi = WfContextBuilder.getJbpmContext()
				.getGraphSession().getProcessInstance(idProcessInstance);

		Calendar cPiStart = Calendar.getInstance();
		cPiStart.setTime(pi.getStart());
		pi.end();

		if (cDtFim != null && (cDtFim.after(cPiStart))) {
			pi.setEnd(cDtFim.getTime());
		} else {
			throw new AplicacaoException(
					"A data de fim é anterior à data de criação do Process Instance ID: "
							+ pi.getId() + " START: " + pi.getStart());
		}

		for (TaskInstance ti : (Collection<TaskInstance>) (pi
				.getTaskMgmtInstance().getUnfinishedTasks(pi.getRootToken()))) {
			Calendar cTiCreate = Calendar.getInstance();
			cTiCreate.setTime(ti.getCreate());
			ti.end();
			if (cDtFim != null && (cDtFim.after(cTiCreate))) {
				ti.setEnd(cDtFim.getTime());
			} else {
				throw new AplicacaoException(
						"A data de fim é anterior à data de criação da Task Instance ID:"
								+ ti.getId() + " CREATE: " + ti.getCreate());
			}
		}

	}

	public void excluirProcessInstance(Long idProcessInstance) {
		ProcessInstance pi = WfContextBuilder.getJbpmContext()
				.getGraphSession().getProcessInstance(idProcessInstance);
		WfContextBuilder.getJbpmContext().getGraphSession()
				.deleteProcessInstance(pi.getId());
	}
	
	/**
	 * Exclui do banco de dados todas as instâncias do processo. CUIDADO: As
	 * exclusões de instâncias são realizadas em TODAS AS VERSÕES do Process
	 * Definition.
	 * 
	 * @param nomeProcesso -
	 *            Nome do processo que terá suas instâncias excluídas.
	 */
	public void excluirTodasProcessInstances(String nomeProcesso) {
		List<ProcessDefinition> pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findAllProcessDefinitionVersions(nomeProcesso);

		for (ProcessDefinition p : pd) {
			List<ProcessInstance> instanciasProcesso =  WfDao.getInstance().consultarInstanciasDoProcessInstance(p.getId());

			for (ProcessInstance pi : instanciasProcesso) {
				WfContextBuilder.getJbpmContext().getGraphSession()
						.deleteProcessInstance(pi.getId());
			}

		}
		WfContextBuilder.closeContext();
	}
	
	/**
	 * Exclui a última versão da definição do processo (Process Definition) do
	 * banco de dados. CUIDADO: todas as instâncias associadas a ela também
	 * serão excluídas.
	 * 
	 * @param nomeProcesso -
	 *            Nome do processo.
	 */
	private static void excluirUltimaVersaoProcessDefinition(String nomeProcesso) {
		ProcessDefinition pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findLatestProcessDefinition(nomeProcesso);

		WfContextBuilder.getJbpmContext().getGraphSession()
				.deleteProcessDefinition(pd.getId());

		WfContextBuilder.closeContext();

	}
	
	
	/**
	 * Exclui TODAS as definições de processo (Process definition) do banco de
	 * dados. O process definition deixará de existir, bem como suas instâncias
	 * de processo.
	 * 
	 * @param nomeProcesso
	 */
	private static void excluirTodosProcessDefinition(String nomeProcesso) {
		List<ProcessDefinition> pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findAllProcessDefinitionVersions(nomeProcesso);

		for (ProcessDefinition p : pd) {
			WfContextBuilder.getJbpmContext().getGraphSession()
					.deleteProcessDefinition(p.getId());
		}
		WfContextBuilder.closeContext();

	}

	/**
	 * Encerra o processo da tarefa especificada.
	 * @param idTI
	 * @param dtFim
	 * @throws AplicacaoException
	 */
	public void encerrarProcessInstanceDaTarefa(Long idTI, Date dtFim) throws AplicacaoException {
		TaskInstance ti = WfContextBuilder.getJbpmContext().getJbpmContext().getTaskInstance(idTI);

		if (ti==null){
			throw new AplicacaoException("Tarefa não encontrada!");
		}
		if (dtFim==null){
			throw new AplicacaoException("Data inválida!");
		}
		
		encerrarProcessInstance(ti.getProcessInstance().getId(), dtFim);
	}
	
	public String gravarProcessDefinition(String xml) {
		JbpmContext jbpmContext = WfContextBuilder.getJbpmContext().getJbpmContext();
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlString(xml);
		jbpmContext.deployProcessDefinition(processDefinition);
		long id = processDefinition.getId();

		String sReturn = "Definição de Procedimento: " + processDefinition.getName()
				+ " atualizada com sucesso.";

		// ProcessDefinition pi = jbpmContext.getGraphSession()
		// .loadProcessDefinition(id);

		Delegation d = new Delegation(
				"br.gov.jfrj.siga.wf.util.WfAssignmentHandler");

		for (Swimlane s : ((Collection<Swimlane>) processDefinition
				.getTaskMgmtDefinition().getSwimlanes().values())) {
			if (s.getTasks() != null)
				for (Object t : s.getTasks()) {
					System.out.println(((Task) t).toString());
				}
			if (s.getAssignmentDelegation() == null)
				s.setAssignmentDelegation(d);
		}

		for (Task t : ((Collection<Task>) processDefinition
				.getTaskMgmtDefinition().getTasks().values())) {
			if (t.getSwimlane() == null
					&& t.getAssignmentDelegation() == null)
				t.setAssignmentDelegation(d);
		}
		return sReturn; 
	}

	public String lerProcessDefinition(String nome) {
		JbpmContext jbpmContext = WfContextBuilder.getJbpmContext().getJbpmContext();
		ProcessDefinition pd = jbpmContext.getGraphSession().findLatestProcessDefinition(nome);

		byte ab[] = pd.getFileDefinition().getBytes("processdefinition.xml");
		String sXML;
		try {
			sXML = new String(ab, "UTF-8");
			return sXML; 
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		
	}

}
