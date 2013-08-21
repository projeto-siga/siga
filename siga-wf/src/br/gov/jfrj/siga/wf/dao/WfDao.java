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
/*
 * Criado em  01/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.wf.dao;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import org.hibernate.Query;
import org.hibernate.cfg.AnnotationConfiguration;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.WfConfiguracao;
import br.gov.jfrj.siga.wf.WfConhecimento;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Classe que representa o DAO do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfDao extends CpDao {

	public static final String CACHE_WF = "wf";

	/**
	 * Retorna uma instância do DAO.
	 * 
	 * @return
	 */
	public static WfDao getInstance() {
		return ModeloDao.getInstance(WfDao.class);
	}

	/**
	 * Pesquisa as configurações que são semelhantes ao exemplo
	 * 
	 * @param exemplo
	 *            Uma configuração de exemplo para a pesquisa.
	 * @return Lista de configurações encontradas.
	 */
	public List<WfConfiguracao> consultar(final WfConfiguracao exemplo) {
		Query query = getSessao().getNamedQuery("consultarWfConfiguracoes");

		query.setLong("idTpConfiguracao", exemplo.getCpTipoConfiguracao()
				.getIdTpConfiguracao());

		query.setCacheable(true);
		query.setCacheRegion(CACHE_QUERY_CONFIGURACAO);
		return query.list();
	}

	public List<TaskInstance> consultarTarefasAtivasPorDocumento(String siglaDoc) {
		Query query = getSessao().getNamedQuery(
				"consultarTarefasAtivasPorDocumento");
		query.setParameter("siglaDoc", siglaDoc + "%");
		return query.list();
	}

	public WfConhecimento consultarConhecimento(String procedimento,
			String tarefa) {
		Query query = getSessao().getNamedQuery("consultarConhecimento");
		query.setParameter("procedimento", procedimento);
		query.setParameter("tarefa", tarefa);
		List<WfConhecimento> l = query.list();
		if (l.size() == 0)
			return null;
		return l.get(0);

	}
	
	 public List<ProcessInstance> consultarInstanciasDoProcessInstance(Long id) {
			return WfContextBuilder
					.getJbpmContext().getGraphSession().findProcessInstances(
							id);
	}


	static public AnnotationConfiguration criarHibernateCfg(String datasource)
			throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(datasource);
		WfDao.configurarHibernate(cfg);
		return cfg;
	}

	static public AnnotationConfiguration criarHibernateCfg(
			String connectionUrl, String username, String password)
			throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(connectionUrl,
				username, password);
		WfDao.configurarHibernate(cfg);
		return cfg;
	}

	static private void configurarHibernate(AnnotationConfiguration cfg)
			throws Exception {
		cfg.addClass(br.gov.jfrj.siga.wf.WfConfiguracao.class);
		cfg.addClass(br.gov.jfrj.siga.wf.WfConhecimento.class);

		cfg.addResource("org/jbpm/db/hibernate.queries.hbm.xml");
		cfg.addResource("org/jbpm/db/hibernate.types.hbm.xml");
		cfg.addResource("hibernate.extra.hbm.xml");

		cfg.addClass(org.jbpm.graph.action.MailAction.class);
		cfg.addClass(org.jbpm.graph.def.ProcessDefinition.class);
		cfg.addClass(org.jbpm.graph.def.Node.class);
		cfg.addClass(org.jbpm.graph.def.Transition.class);
		cfg.addClass(org.jbpm.graph.def.Event.class);
		cfg.addClass(org.jbpm.graph.def.Action.class);
		cfg.addClass(org.jbpm.graph.def.SuperState.class);
		cfg.addClass(org.jbpm.graph.def.ExceptionHandler.class);
		cfg.addClass(org.jbpm.instantiation.Delegation.class);
		cfg.addClass(org.jbpm.graph.action.Script.class);
		cfg.addClass(org.jbpm.graph.node.StartState.class);
		cfg.addClass(org.jbpm.graph.node.EndState.class);
		cfg.addClass(org.jbpm.graph.node.ProcessState.class);
		cfg.addClass(org.jbpm.graph.node.Decision.class);
		cfg.addClass(org.jbpm.graph.node.Fork.class);
		cfg.addClass(org.jbpm.graph.node.Join.class);
		cfg.addClass(org.jbpm.graph.node.MailNode.class);
		cfg.addClass(org.jbpm.graph.node.State.class);
		cfg.addClass(org.jbpm.graph.node.TaskNode.class);

		cfg.addClass(org.jbpm.context.def.ContextDefinition.class);
		cfg.addClass(org.jbpm.context.def.VariableAccess.class);
		cfg.addClass(org.jbpm.bytes.ByteArray.class);

		cfg.addClass(org.jbpm.module.def.ModuleDefinition.class);
		cfg.addClass(org.jbpm.file.def.FileDefinition.class);

		cfg.addClass(org.jbpm.taskmgmt.def.TaskMgmtDefinition.class);
		cfg.addClass(org.jbpm.taskmgmt.def.Swimlane.class);
		cfg.addClass(org.jbpm.taskmgmt.def.Task.class);

		cfg.addClass(org.jbpm.taskmgmt.def.TaskController.class);

		cfg.addClass(org.jbpm.scheduler.def.CreateTimerAction.class);

		cfg.addClass(org.jbpm.scheduler.def.CancelTimerAction.class);
		cfg.addClass(org.jbpm.graph.exe.Comment.class);
		cfg.addClass(org.jbpm.graph.exe.ProcessInstance.class);
		cfg.addClass(org.jbpm.graph.exe.Token.class);
		cfg.addClass(org.jbpm.graph.exe.RuntimeAction.class);
		cfg.addClass(org.jbpm.module.exe.ModuleInstance.class);

		cfg.addClass(org.jbpm.context.exe.ContextInstance.class);

		cfg.addClass(org.jbpm.context.exe.TokenVariableMap.class);

		cfg.addClass(org.jbpm.context.exe.VariableInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.ByteArrayInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.DateInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.DoubleInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.HibernateLongInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.HibernateStringInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.LongInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.NullInstance.class);

		cfg.addClass(org.jbpm.context.exe.variableinstance.StringInstance.class);
		cfg.addClass(org.jbpm.job.Job.class);
		cfg.addClass(org.jbpm.job.Timer.class);
		cfg.addClass(org.jbpm.job.ExecuteNodeJob.class);
		cfg.addClass(org.jbpm.job.ExecuteActionJob.class);
		cfg.addClass(org.jbpm.job.CleanUpProcessJob.class);

		cfg.addClass(org.jbpm.taskmgmt.exe.TaskMgmtInstance.class);
		cfg.addClass(org.jbpm.taskmgmt.exe.TaskInstance.class);
		cfg.addClass(org.jbpm.taskmgmt.exe.PooledActor.class);

		cfg.addClass(org.jbpm.taskmgmt.exe.SwimlaneInstance.class);
		cfg.addClass(org.jbpm.logging.log.ProcessLog.class);
		cfg.addClass(org.jbpm.logging.log.MessageLog.class);
		cfg.addClass(org.jbpm.logging.log.CompositeLog.class);
		cfg.addClass(org.jbpm.graph.log.ActionLog.class);
		cfg.addClass(org.jbpm.graph.log.NodeLog.class);

		cfg.addClass(org.jbpm.graph.log.ProcessInstanceCreateLog.class);

		cfg.addClass(org.jbpm.graph.log.ProcessInstanceEndLog.class);
		cfg.addClass(org.jbpm.graph.log.ProcessStateLog.class);
		cfg.addClass(org.jbpm.graph.log.SignalLog.class);
		cfg.addClass(org.jbpm.graph.log.TokenCreateLog.class);
		cfg.addClass(org.jbpm.graph.log.TokenEndLog.class);
		cfg.addClass(org.jbpm.graph.log.TransitionLog.class);
		cfg.addClass(org.jbpm.context.log.VariableLog.class);

		cfg.addClass(org.jbpm.context.log.VariableCreateLog.class);

		cfg.addClass(org.jbpm.context.log.VariableDeleteLog.class);

		cfg.addClass(org.jbpm.context.log.VariableUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.ByteArrayUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.DateUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.DoubleUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.HibernateLongUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.HibernateStringUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.LongUpdateLog.class);

		cfg.addClass(org.jbpm.context.log.variableinstance.StringUpdateLog.class);
		cfg.addClass(org.jbpm.taskmgmt.log.TaskLog.class);
		cfg.addClass(org.jbpm.taskmgmt.log.TaskCreateLog.class);
		cfg.addClass(org.jbpm.taskmgmt.log.TaskAssignLog.class);
		cfg.addClass(org.jbpm.taskmgmt.log.TaskEndLog.class);
		cfg.addClass(org.jbpm.taskmgmt.log.SwimlaneLog.class);

		cfg.addClass(org.jbpm.taskmgmt.log.SwimlaneCreateLog.class);

		cfg.addClass(org.jbpm.taskmgmt.log.SwimlaneAssignLog.class);

		cfg.addClass(br.gov.jfrj.siga.wf.util.WfTaskInstance.class);

		cfg.setCacheConcurrencyStrategy("org.jbpm.context.def.VariableAccess",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.file.def.FileDefinition.processFiles",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.action.Script.variableAccesses",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.graph.def.Action",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.graph.def.Event",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Event.actions", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.graph.def.ExceptionHandler",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.ExceptionHandler.actions",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.graph.def.Node",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Node.events", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Node.exceptionHandlers",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Node.leavingTransitions",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Node.arrivingTransitions",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.graph.def.ProcessDefinition",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.ProcessDefinition.events",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.ProcessDefinition.exceptionHandlers",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.ProcessDefinition.nodes",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.ProcessDefinition.actions",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.ProcessDefinition.definitions",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.SuperState.nodes", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.graph.def.Transition",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Transition.events", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.def.Transition.exceptionHandlers",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.node.Decision.decisionConditions",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.node.ProcessState.variableAccesses",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.graph.node.TaskNode.tasks", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.instantiation.Delegation",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.module.def.ModuleDefinition",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.taskmgmt.def.Swimlane.tasks", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.taskmgmt.def.TaskController",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCacheConcurrencyStrategy("org.jbpm.taskmgmt.def.Task",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.taskmgmt.def.TaskController.variableAccesses",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.taskmgmt.def.Task.events", "nonstrict-read-write",
				CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.taskmgmt.def.Task.exceptionHandlers",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.taskmgmt.def.TaskMgmtDefinition.swimlanes",
				"nonstrict-read-write", CACHE_WF);
		cfg.setCollectionCacheConcurrencyStrategy(
				"org.jbpm.taskmgmt.def.TaskMgmtDefinition.tasks",
				"nonstrict-read-write", CACHE_WF);

		CacheManager manager = CacheManager.getInstance();
		Cache cache;
		CacheConfiguration config;

		if (!manager.cacheExists(CACHE_WF)) {
			manager.addCache(CACHE_WF);
			cache = manager.getCache(CACHE_WF);
			config = cache.getCacheConfiguration();
			config.setTimeToIdleSeconds(3600);
			config.setTimeToLiveSeconds(36000);
			config.setMaxElementsInMemory(10000);
			config.setMaxElementsOnDisk(1000000);
		}

		// ModeloDao.configurarHibernateParaDebug(cfg);
	}
}
