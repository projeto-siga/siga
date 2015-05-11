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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.jbpm.context.def.VariableAccess;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.Comment;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.logging.log.ProcessLog;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.wf.WfConhecimento;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfAssignmentHandler;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;
import br.gov.jfrj.siga.wf.util.WfGraphFactory;

import com.opensymphony.xwork.Action;

/**
 * Classe reponsável pelas actions relativas às tarefas.
 * 
 * @author kpf
 * 
 */
public class WfTaskAction extends WfSigaActionSupport {

	public static final long serialVersionUID = 1;

	protected TaskInstance taskInstance;

	protected WfTaskVO task;

	protected Long tiId;

	protected List<ProcessLog> logs;

	protected Integer prioridade;

	protected String comentario;

	protected String justificativa;

	protected String conhecimento;

	protected String[] fieldNames;

	protected String[] fieldValues;

	protected String transitionName;

	protected List<VariableAccess> docVariables;

	// protected List<VariableAccess> variableList;

	protected String sigla;

	protected String result;

	protected DpPessoaSelecao atorSel = new DpPessoaSelecao();
	protected DpLotacaoSelecao lotaAtorSel = new DpLotacaoSelecao();

	protected String dot;

	/**
	 * Retorna o comentário da tarefa.
	 * 
	 * @return
	 */
	public String getComentario() {
		return comentario;
	}

	/**
	 * Define o comentário da tarefa.
	 * 
	 * @param comentario
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	/**
	 * Retorna a lista de logs da tarefa.
	 * 
	 * @return
	 */
	public List<ProcessLog> getLogs() {
		return logs;
	}

	/**
	 * Define a lista de logs da tarefa.
	 * 
	 * @param logs
	 */
	public void setLogs(List<ProcessLog> logs) {
		this.logs = logs;
	}

	/**
	 * Retorna a prioridade da tarefa.
	 * 
	 * @return
	 */
	public Integer getPrioridade() {
		return prioridade;
	}

	/**
	 * Define a prioridade da tarefa.
	 * 
	 * @param prioridade
	 */
	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}

	/**
	 * Retorna o ator selecionado.
	 * 
	 * @return
	 */
	public DpPessoaSelecao getAtorSel() {
		return atorSel;
	}

	/**
	 * Define o ator selecionado.
	 * 
	 * @param atorSel
	 */
	public void setAtorSel(DpPessoaSelecao atorSel) {
		this.atorSel = atorSel;
	}

	public DpLotacaoSelecao getLotaAtorSel() {
		return lotaAtorSel;
	}

	public void setLotaAtorSel(DpLotacaoSelecao lotaAtorSel) {
		this.lotaAtorSel = lotaAtorSel;
	}

	/**
	 * Action que inicializa todos os campos que serão necessários para
	 * finalizar a tarefa especificada pelo Id do ProcessInstance e o Id do
	 * ProcessDefinition.
	 * 
	 * @return Action.SUCCESS
	 */
	public String loadTask() throws Exception {
		loadTaskInstance();
		inicializarTaskVO();
		return Action.SUCCESS;
	}

	/**
	 * Action que inicializa todos os campos que serão necessários para
	 * finalizar a tarefa especificada pelo Id do ProcessoInstance e o Id do
	 * ProcessDefinition.
	 * 
	 * @return Action.SUCCESS
	 */
	public String carregarTarefaEDesignar() throws Exception {
		loadTaskInstance();
		if (taskInstance != null)
			if (taskInstance.getActorId() == null) {
				// taskInstance.setActorId(getTitular().getSiglaCompleta());
				carregarAtorEGrupo();
			}
		inicializarTaskVO();

		ProcessInstance pi = taskInstance.getProcessInstance();
		ProcessDefinition pd = pi.getProcessDefinition();
		byte ab[] = pd.getFileDefinition().getBytes("processdefinition.xml");
		String sXML = new String(ab, "UTF-8");

		WfGraphFactory graph = new WfGraphFactory();

		// Build a delegation map
		Map<String, String> map = new HashMap<String, String>();
		// for (TaskInstance ti : ((Collection<TaskInstance>) pi
		// .getTaskMgmtInstance().getTaskInstances())) {
		// if (ti.getSwimlaneInstance() != null)
		// map.put(ti.getTask().getParent().getName(),
		// designacao(ti.getSwimlaneInstance().getActorId(), ti
		// .getSwimlaneInstance().getPooledActors()));
		// else if (ti.getTask().getSwimlane() == null) {
		// map.put(ti.getTask().getParent().getName(),
		// designacao(ti.getActorId(), ti.getPooledActors()));
		// }
		// }

		WfAssignmentHandler ah = new WfAssignmentHandler();
		ExecutionContext ctx = new ExecutionContext(taskInstance.getToken());
		for (Task t : ((Collection<Task>) pd.getTaskMgmtDefinition().getTasks().values())) {
			String designacao = null;
			TaskNode taskNode = t.getTaskNode();
			if (t.getSwimlane() != null) {
				{
					SwimlaneInstance si;
					si = taskInstance.getTaskMgmtInstance()
							.getSwimlaneInstance(t.getSwimlane().getName());
					if (si != null)
						designacao = designacao(si.getActorId(),
								si.getPooledActors());
					if (designacao != null) {
						if (taskNode != null)
							map.put(taskNode.getName(), designacao);
					} else {
						si = new SwimlaneInstance(t.getSwimlane());
						si.setTaskMgmtInstance(taskInstance
								.getTaskMgmtInstance());
						try {
							ctx.setTaskInstance(taskInstance);
							ah.assign(si, ctx);
							designacao = designacao(si.getActorId(),
									si.getPooledActors());
							map.put(taskNode.getName(), designacao);
						} catch (AplicacaoException e) {

						}
					}
				}
			} else {
				TaskInstance ti = new TaskInstance();
				ti.setTask(t);
				ti.setTaskMgmtInstance(taskInstance.getTaskMgmtInstance());
				try {
					ctx.setTaskInstance(ti);
					ah.assign(ti, ctx);
					designacao = designacao(ti.getActorId(),
							ti.getPooledActors());
					map.put(taskNode.getName(), designacao);
				} catch (AplicacaoException e) {

				}
			}
		}

		// for (Swimlane s : ((Collection<Swimlane>) pd.getTaskMgmtDefinition()
		// .getSwimlanes().values())) {
		// if (s.getTasks() != null)
		// for (Object t : s.getTasks()) {
		// System.out.println(((Task) t).toString());
		// }
		// if (s.getAssignmentDelegation() == null)
		// s.setAssignmentDelegation(d);
		// }
		//
		// for (Task t : ((Collection<Task>)
		// pd.getTaskMgmtDefinition().getTasks()
		// .values())) {
		// if (t.getSwimlane() == null && t.getAssignmentDelegation() == null)
		// t.setAssignmentDelegation(d);
		// }

		dot = graph.gerarDOT(sXML,
				taskInstance.getTask().getParent().getName(), map);

		dot = dot.replace("\n", " ");
		dot = dot.replace("\r", " ");

		return Action.SUCCESS;
	}

	private String designacao(String actorId, Set pooledActors) {
		if (actorId == null && pooledActors == null)
			return null;
		if (pooledActors == null || pooledActors.size() == 0)
			return actorId;
		String pa = "";
		for (Object a : pooledActors) {
			if (pa.length() > 0)
				pa += "/";
			if (a instanceof PooledActor)
				pa += ((PooledActor)a).getActorId();
		}

		if (actorId == null)
			return pa;

		return actorId + " - " + pa;
	}

	/**
	 * Inicializa a variável "task" para que seus atributos possam ser
	 * visualizados pelas actions.
	 * 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws Exception
	 * @throws AplicacaoException
	 */
	private void inicializarTaskVO() throws IllegalAccessException,
			InvocationTargetException, Exception, AplicacaoException {
		task = new WfTaskVO(taskInstance, getTitular(), getLotaTitular());
		task.setConhecimentoEditavel(getTitular().getSigla().equals(
				taskInstance.getActorId()));
	}

	public String designarAtor() throws Exception {
		loadTaskInstance();
		if (taskInstance != null) {
			taskInstance.setActorId(getTitular().getSiglaCompleta(),false);
			carregarAtorEGrupo();
		}
		inicializarTaskVO();

		if (sigla != null) {
			return "doc";
		}
		return "task";
	}

	/**
	 * Carrega as informações relativas à tarefa, tais como, a instância da
	 * tarefa, os logs, a prioridade e suas variáveis.
	 * 
	 * @throws Exception
	 */
	public void loadTaskInstance() throws Exception {
		// GraphSession graph = WfContextBuilder.getJbpmContext()
		// .getGraphSession();
		// ProcessInstance pi = graph.loadProcessInstance(piId); // Carregando o
		// ProcessInstance

		// taskInstance = null;
		// for (TaskInstance ti : pi.getTaskMgmtInstance().getTaskInstances())
		// if (ti.getId() == tiId)
		// taskInstance = ti;

		taskInstance = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getTaskInstance(tiId);
		ProcessInstance pi = taskInstance.getProcessInstance();
		if (taskInstance == null)
			throw new Exception("Atividade indisponível.");

		// WfConhecimento c = dao().consultarConhecimento(
		// taskInstance.getProcessInstance().getProcessDefinition()
		// .getName(), taskInstance.getName());
		// if (c != null) {
		// setDescricao(WfWikiParser.renderXHTML(c.getDescricao()));
		// if (getConhecimento() == null)
		// setConhecimento(c.getDescricao());
		// } else {
		// if (taskInstance.getDescription() != null)
		// setDescricao(WfWikiParser.renderXHTML(taskInstance
		// .getDescription()));
		// if (getConhecimento() == null)
		// setConhecimento(taskInstance.getDescription());
		// }

		logs = (List<ProcessLog>) WfContextBuilder.getJbpmContext()
				.getJbpmContext().getLoggingSession()
				.findLogsByToken(taskInstance.getToken().getId());

		prioridade = taskInstance.getPriority();
		carregarAtorEGrupo();

		// System.out.println(pi.getContextInstance().getVariables());
		// if (taskInstance.getTask().getTaskController() != null) {
		// { // Inicializando a lista de variaveis do Task apartir do
		// // TaskInstance.
		// variableList = new ArrayList<VariableAccess>();
		// List<VariableAccess> vList = taskInstance.getTask()
		// .getTaskController().getVariableAccesses();
		// for (VariableAccess variable : vList) {
		// // Associa cada variavel
		// // com seu valor
		// // especifico.
		// // if (variable.getMappedName().startsWith("doc_")
		// // && !variable.isWritable() && variable.isReadable()) {
		// // this.getDocVariables().add(variable);
		// // } else {
		// variableList.add(variable);
		// // }
		// }
		// }
		//
		// { // Carrega o valor inicial dos fieldVariable se forem para
		// // Leitura
		// // fieldVariable = new String [ variableList.size () ];
		// // Iterator < VariableBean > variableListIterator =
		// // variableList.iterator ();
		// // for ( int n = 0 ; n < fieldVariable.length ; n++ )
		// // { // Caso for leitura, associe a variavel field.
		// // VariableBean variable = variableListIterator.next ();
		// // fieldVariable [ n ] = variable.getValue ();
		// // }
		// }
		// }

		{ // Inicializando a lista transactionList.
			// List < Transition > tList = ( List < Transition > ) ti.getToken
			// ().getNode
			// ().getLeavingTransitions ();
			// for ( Transition tItem : tList )
			// { // Para cada transition, adicione na lista.
			// TransitionBean transition = new TransitionBean ( tItem.getName ()
			// );
			// transition.setId ( tItem.getId () );
			// transitionList.add ( transition );
			// }
		}

	}

	/**
	 * Carrega a pessoa e a lotação designados à terefa.
	 * 
	 * @throws AplicacaoException
	 */
	private void carregarAtorEGrupo() throws AplicacaoException {
		if (taskInstance.getActorId() != null) {
			atorSel.setSigla(taskInstance.getActorId());
			atorSel.buscarPorSigla();
		}
		if (taskInstance.getPooledActors().size() != 0) {
			String actorId = (String) ((PooledActor) (taskInstance
					.getPooledActors().toArray()[0])).getActorId();
			if (actorId != null) {
				lotaAtorSel.setSigla(actorId);
				lotaAtorSel.buscarPorSigla();
			}
		}
	}

	/**
	 * Action que finaliza uma tarefa com as variaveis atribuidas. Os valores
	 * são atribuídos às variáveis da tarefa e depois a tarefa é transferida ao
	 * próximo passo. Se a próxima tarefa for para o mesmo ator, esta é exibida
	 * imediatamente.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public String executeTask() throws Exception {
		String cadastrante = getTitular().getSigla() + "@"
				+ getLotaTitular().getSiglaCompleta();
		loadTaskInstance();
		if (taskInstance == null || taskInstance.getTask() == null)
			throw new AplicacaoException(
					"Não foi possível recuperar a instância da tarefa.");

		if (taskInstance.getActorId() == null) {
			taskInstance.setActorId(getTitular().getSiglaCompleta(),false);
			carregarAtorEGrupo();
		}

		assertPodeTransferirDocumentosVinculados(taskInstance, cadastrante);

		if (taskInstance.getTask().getTaskController() != null) {
			List<VariableAccess> vList = taskInstance.getTask()
					.getTaskController().getVariableAccesses();
			if (fieldNames != null) {
				for (int n = 0, c = 0; n < fieldNames.length; n++) {
					// Associa cada variavel com seu valore especifico
					for (VariableAccess variable : vList) {
						if (variable.getMappedName().equals(fieldNames[n]) && variable.isWritable()) {
							Object value;
							if (variable.getMappedName().startsWith("doc_")) {
								value = param(variable.getMappedName() + "_expedienteSel.sigla");
							} else if (variable.getMappedName().startsWith("pes_")) {
								value = param(variable.getMappedName() + "_pessoaSel.sigla");
							} else if (variable.getMappedName().startsWith("lot_")) {
								value = param(variable.getMappedName() + "_lotacaoSel.sigla");
							} else if (variable.getMappedName().startsWith("dt_")) {
								value = SigaCalendar.converteStringEmData(fieldValues[c]);
								c++;
							} else if (variable.getMappedName().startsWith("sel_")) {
								value = fieldValues[c];
								c++;
							} else {
								value = fieldValues[c];
								c++;
							}
							// Verifica se as variáveis "required" foram
							// preenchidas"
							if (variable.isRequired()
									&& (value == null || (value instanceof String && (((String) value)
											.trim().length() == 0)))) {
								throw new AplicacaoException("O campo "
										+ variable.getVariableName()
										+ " deve ser preenchido");
							}

							taskInstance
									.getToken()
									.getProcessInstance()
									.getContextInstance()
									.setVariable(variable.getMappedName(),
											value);
						}
					}
				}
			}
		}

		// Bloco que transfere a tarefa
		if (transitionName != null && transitionName.contains(" » ")) {
			transitionName = transitionName.substring(0,
					transitionName.indexOf(" » "));
		}
		/*
		 * código inserido para corrigir o caracter &raquo; (») contido no botão
		 * e enviado pelo submit. Este, no caso de autenticação por certificado,
		 * vem em conjunto com o caracter Â. (
		 */
		if (transitionName != null && transitionName.contains(" Â» ")) {
			transitionName = transitionName.substring(0,
					transitionName.indexOf(" Â» "));
		}
		/*
		 * fim do código inserido para corrigir o caracter &raquo;
		 */
		if (transitionName.length() == 0 || transitionName.equals("Prosseguir"))
			transitionName = null;
		// Transition transition = taskInstance.getTask().getTaskNode()
		// .getLeavingTransition(transitionName);
		for (Transition transition : (List<Transition>) (taskInstance
				.getAvailableTransitions())) {
			if ((transition.getName() == null && transitionName == null)
					|| ((transition.getName() != null && transitionName != null) && (transition
							.getName().equals(new String(transitionName
							.getBytes("ISO-8859-1")))))
					|| ((transition.getName() != null && transitionName != null) && (transition
							.getName().equals(new String(transitionName
							.getBytes("UTF-8")))))) {
				taskInstance.end(transition);
				break;
			}
		}

		WfContextBuilder.getJbpmContext().getJbpmContext()
				.save(taskInstance.getToken().getProcessInstance());

		// Service.getExService ().transferir ( "RJ-MEM-2007/00595-A" ,
		// "@RJSCO" , "RJ13635@RJSESAD" );

		transferirDocumentosVinculados(taskInstance.getProcessInstance(),
				cadastrante);

		if (result != null)
			return result;

		// Verificar se um novo task foi criado para o process instance em questão. Se esse
		// task existir e for designado para o mesmo ator, então a próxima
		// página a ser exibida será a página de apresentação do task, e não a
		// página inicial.
		{
			long idpi = taskInstance.getProcessInstance().getId();
			SortedSet<TaskInstance> tasks = Wf
					.getInstance()
					.getBL()
					.getTaskList(getCadastrante(), getTitular(),
							getLotaTitular());
			for (TaskInstance ti : tasks) {
				if (ti.getProcessInstance().getId() == idpi) {
					setTiId(ti.getId());
					return "task";
				}
			}
		}

		return Action.SUCCESS;
	}

	/**
	 * Grava o conhecimento a respeito de como uma tarefa deve ser executada.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public String saveKnowledge() throws Exception {
		loadTaskInstance();
		if (taskInstance == null || taskInstance.getTask() == null)
			throw new AplicacaoException(
					"Não foi possível recuperar a instância da tarefa.");

		if (taskInstance.getActorId() == null) {
			taskInstance.setActorId(getTitular().getSiglaCompleta());
			carregarAtorEGrupo();
		}

		WfConhecimento cAntigo = dao().consultarConhecimento(
				taskInstance.getProcessInstance().getProcessDefinition()
						.getName(), taskInstance.getName());

		WfConhecimento cNovo = new WfConhecimento();
		if (cAntigo != null) {
			PropertyUtils.copyProperties(cNovo, cAntigo);
			cNovo.setId(null);
		} else {
			cNovo.setProcedimento(taskInstance.getProcessInstance()
					.getProcessDefinition().getName());
			cNovo.setTarefa(taskInstance.getName());
		}
		cNovo.setDescricao(getConhecimento());

		dao().iniciarTransacao();
		dao().gravarComHistorico(cNovo, cAntigo, null,
				getIdentidadeCadastrante());
		dao().commitTransacao();

		inicializarTaskVO();
		return Action.SUCCESS;
	}

	public static void transferirDocumentosVinculados(ProcessInstance pi,
			String siglaTitular) throws Exception {
		ExService service = Service.getExService();
		
		ArrayList<Token> tokens = new ArrayList<Token>();
		pi.getRootToken().collectChildrenRecursively(tokens);
		tokens.add(pi.getRootToken());
		
		ArrayList<TaskInstance> tis = new ArrayList<TaskInstance>();
		for (Token t : tokens) {
			tis.addAll((Collection<TaskInstance>) (pi
				.getTaskMgmtInstance().getSignallingTasks(new ExecutionContext(t))));
		}

		for (TaskInstance ti : tis) {
			if (ti.getTask().getTaskController() != null) {
				List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
						.getTask().getTaskController().getVariableAccesses();
				for (VariableAccess variable : variableAccesses) {
					if (variable.getMappedName().startsWith("doc_")
							&& variable.isReadable() && !variable.isWritable() && !variable.getAccess().toString().contains(WfTaskVO.DISABLE_DOC_FORWARD)) {
						String value = (String) ti.getToken()
								.getProcessInstance().getContextInstance()
								.getVariable(variable.getMappedName());
						if (value != null && value.trim().length() == 0)
							value = null;
						String destino = ti.getActorId();
						
						if (destino != null){
							DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
							flt.setSigla(destino);
							DpPessoa ator = (DpPessoa) WfDao.getInstance()
									.consultarPorSigla(flt);
							if (!atorDeveReexecutarTarefa(ator,ti)){
								destino = null;
							}
						}
						
						if (destino == null){
							for (PooledActor lot : (Collection<PooledActor>) ti
									.getPooledActors()) {
								destino = "@" + lot.getActorId();
								break;
							}
						}
						if (value != null && destino != null)
							service.transferir(value, destino, siglaTitular, true);
					}
				}
			}
		}
	}
	
	private static boolean atorDeveReexecutarTarefa(DpPessoa ator, TaskInstance ti) {
		DpLotacaoDaoFiltro lotflt = new DpLotacaoDaoFiltro();
		lotflt.setSiglaCompleta(((PooledActor) ti.getPooledActors().toArray()[0]).getActorId());
		DpLotacao lotacao = (DpLotacao) WfDao.getInstance().consultarPorSigla(lotflt);
		return !ator.isFechada() && ator.getLotacao().equivale(lotacao);
	}


	public static void assertPodeTransferirDocumentosVinculados(
			TaskInstance ti, String siglaTitular) throws Exception {
		ExService service = Service.getExService();

		if (ti.getTask().getTaskController() != null) {
			List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
					.getTask().getTaskController().getVariableAccesses();
			for (VariableAccess variable : variableAccesses) {
				if (variable.getMappedName().startsWith("doc_")
						&& variable.isReadable() && !variable.isWritable() && !variable.getAccess().toString().contains(WfTaskVO.DISABLE_DOC_FORWARD)) {
					String value = (String) ti.getToken().getProcessInstance()
							.getContextInstance()
							.getVariable(variable.getMappedName());
					if (value != null && value.trim().length() != 0)
						if (!service.podeTransferir(value, siglaTitular, true)) {
							throw new AplicacaoException(
									"A tarefa não pode prosseguir porque o documento '"
											+ value
											+ "' não pode ser transferido. Por favor, verifique se o documento está em sua lotação e se está 'Aguardando andamento'.");
						}
				}
			}
		}
	}

	private boolean assertLotacaoAscendenteOuDescendente(DpLotacao lotAtual,
			DpLotacao lotFutura) throws AplicacaoException {
		if (lotAtual.getIdInicial().equals(lotFutura.getIdInicial()))
			return true;

		// Linha ascendente
		DpLotacao lot = lotAtual;
		while (lot.getLotacaoPai() != null) {
			lot = lot.getLotacaoPai();
			if (lot.getIdInicial().equals(lotFutura.getIdInicial()))
				return true;
		}

		// Descendente direta
		lot = lotFutura;
		while (lot.getLotacaoPai() != null) {
			lot = lot.getLotacaoPai();
			if (lot.getIdInicial().equals(lotAtual.getIdInicial()))
				return true;
		}

		throw new AplicacaoException(
				"A designação de '"
						+ lotAtual.getSigla()
						+ "' para '"
						+ lotFutura.getSigla()
						+ "' não é permitida pois só são aceitas lotações ascendentes seguindo a linha do organograma ou descendentes diretas.");
	}

	private String getSiglaTitular() {
		return getTitular().getSigla() + "@"
				+ getLotaTitular().getSiglaCompleta();
	}

	/**
	 * Action que finaliza uma tarefa com as variaveis atribuidas.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public String assignTask() throws Exception {
		taskInstance = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getTaskInstance(tiId);
		ProcessInstance pi = taskInstance.getProcessInstance();
		if (taskInstance == null)
			throw new AplicacaoException("Atividade indisponível.");

		String actorId = null;
		String lotaActorId = null;

		if (atorSel.getId() != null)
			actorId = daoPes(atorSel.getId()).getSigla();
		if (lotaAtorSel.getId() != null)
			lotaActorId = daoLot(lotaAtorSel.getId()).getSiglaCompleta();

		if (actorId == null && lotaActorId == null)
			throw new AplicacaoException(
					"Não é permitido designar a tarefa quando tanto a pessoa quanto a lotação estão em branco.");

		DpLotacao lotAtualAtor = null;
		DpLotacao lotAtualPool = null;
		if (taskInstance.getActorId() != null) {
			DpPessoa pesAtual = new DpPessoa();
			pesAtual.setSigla(taskInstance.getActorId());
			lotAtualAtor = dao().consultarPorSigla(pesAtual).getLotacao();
		}
		if (taskInstance.getPooledActors().size() != 0) {
			DpLotacaoDaoFiltro lotflt = new DpLotacaoDaoFiltro();
			lotflt.setSiglaCompleta(((PooledActor) taskInstance
					.getPooledActors().toArray()[0]).getActorId());
			lotAtualPool = (DpLotacao) dao().consultarPorSigla(lotflt);
		}

		boolean fActorChanged = (actorId != null && taskInstance.getActorId() == null)
				|| (actorId == null && taskInstance.getActorId() != null)
				|| (actorId != null && taskInstance.getActorId() != null && !taskInstance
						.getActorId().equals(actorId));

		boolean fPooledActorsChanged = (lotaActorId != null && taskInstance
				.getPooledActors().size() == 0)
				|| (lotaActorId == null && taskInstance.getPooledActors()
						.size() != 0)
				|| (lotaActorId != null
						&& taskInstance.getPooledActors().size() != 0 && !((PooledActor) taskInstance
						.getPooledActors().toArray()[0]).getActorId().equals(
						lotaActorId));

		if (fActorChanged || fPooledActorsChanged)
			assertPodeTransferirDocumentosVinculados(taskInstance,
					getSiglaTitular());

		if (fActorChanged) {
			if (actorId != null) {
				assertLotacaoAscendenteOuDescendente(
						lotAtualAtor != null ? lotAtualAtor : lotAtualPool,
						daoPes(atorSel.getId()).getLotacao());
			}
			taskInstance.setActorId(actorId);
		}

		if (fPooledActorsChanged) {
			if (lotaActorId == null)
				taskInstance.setPooledActors(new String[] {});
			else {
				assertLotacaoAscendenteOuDescendente(
						lotAtualPool != null ? lotAtualPool : lotAtualAtor,
						daoLot(lotaAtorSel.getId()));
				taskInstance.setPooledActors(new String[] { lotaActorId });
			}
		}
		if (fActorChanged || fPooledActorsChanged)
			transferirDocumentosVinculados(pi, getSiglaTitular());

		taskInstance.setPriority(prioridade);

		if (fActorChanged) {
			Comment c = new Comment(
					getCadastrante().getSigla(),
					"Tarefa designada para "
							+ (taskInstance.getActorId() != null ? "pessoa "
									+ taskInstance.getActorId() + "."
									: " nenhuma pessoa.")
							+ (getJustificativa() != null
									&& getJustificativa().trim().length() > 0 ? " Justificativa: "
									+ getJustificativa() + "."
									: ""));
			taskInstance.addComment(c);
		}

		if (fPooledActorsChanged) {
			Comment c = new Comment(
					getCadastrante().getSigla(),
					"Tarefa designada para "
							+ (taskInstance.getPooledActors().size() != 0 ? "lotação "
									+ ((PooledActor) taskInstance
											.getPooledActors().toArray()[0])
											.getActorId() + "."
									: " nenhuma lotação.")
							+ (getJustificativa() != null
									&& getJustificativa().trim().length() > 0 ? " Justificativa: "
									+ getJustificativa() + "."
									: ""));
			taskInstance.addComment(c);
		}

		inicializarTaskVO();
		return Action.SUCCESS;
	}

	/**
	 * Insere um comentário à tarefa.
	 * 
	 * @return
	 * @throws CsisException
	 */
	public String commentTask() throws Exception {
		taskInstance = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getTaskInstance(tiId);
		if (taskInstance == null)
			throw new Exception("Atividade indisponível.");
		if (comentario == null || comentario.trim() == "")
			return SUCCESS;
		Comment c = new Comment(getCadastrante().getSigla(), getComentario());

		taskInstance.addComment(c);
		inicializarTaskVO();
		return "task";
	}

	/**
	 * Retorna os valores dos campos (variáveis) da tarefa.
	 * 
	 * @return
	 */
	public String[] getFieldValues() {
		return fieldValues;
	}

	/**
	 * Define os valores dos campos (variáveis) da tarefa.
	 * 
	 * @param fieldValues
	 */
	public void setFieldValues(String[] fieldValues) {
		this.fieldValues = fieldValues;
	}

	/**
	 * Retorna o nome da transição.
	 * 
	 * @return
	 */
	public String getTransitionName() {
		return transitionName;
	}

	/**
	 * Define o nome da transição.
	 * 
	 * @param transitionName
	 */
	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	/**
	 * Retorna os nomes dos campos (variáveis) da tarefa.
	 * 
	 * @return
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * Define os nomes dos campos (variáveis) da tarefa.
	 * 
	 * @param fieldNames
	 */
	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	/**
	 * Retorna a instância da tarefa.
	 * 
	 * @return
	 */
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	/**
	 * Retorna o ID da instância da tarefa.
	 * 
	 * @return
	 */
	public Long getTiId() {
		return tiId;
	}

	/**
	 * Define o ID da instância da tarefa.
	 * 
	 * @param tiId
	 */
	public void setTiId(Long tiId) {
		this.tiId = tiId;
	}

	/**
	 * Retorna as variáveis referentes a documentos associados à tarefa.
	 * 
	 * @return
	 */
	public List<VariableAccess> getDocVariables() {
		if (docVariables == null)
			docVariables = new ArrayList<VariableAccess>();
		return docVariables;
	}

	/**
	 * Define as variáveis referentes a documentos associados à tarefa.
	 * 
	 * @param docVariable
	 */
	public void setDocVariables(List<VariableAccess> docVariable) {
		this.docVariables = docVariable;
	}

	/**
	 * Retorna as variáveis associadas à tarefa.
	 * 
	 * @return
	 */
	// public List<VariableAccess> getVariableList() {
	// return variableList;
	// }

	/**
	 * Define as variáveis associadas à tarefa.
	 * 
	 * @param variableList
	 */
	// public void setVariableList(List<VariableAccess> variableList) {
	// this.variableList = variableList;
	// }

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Retorna a sigla
	 * 
	 * @return
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Define a sigla
	 * 
	 * @param sigla
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getConhecimento() {
		// Corrige o proble do encoding ao editar a descrição da Tarefa -
		// 01/08/2012
		/*
		 * try { return new String(conhecimento.getBytes("ISO-8859-1")); } catch
		 * (UnsupportedEncodingException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return conhecimento;
	}

	public void setConhecimento(String conhecimento) {
		this.conhecimento = conhecimento;
	}

	public WfTaskVO getTask() {
		return task;
	}

	public void setTask(WfTaskVO task) {
		this.task = task;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getDot() {
		return dot;
	}

	public void setDot(String dot) {
		this.dot = dot;
	}

}
