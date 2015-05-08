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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbpm.context.def.VariableAccess;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.node.EndState;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.jpdl.el.impl.JbpmExpressionEvaluator;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.WfConhecimento;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfAssignmentHandler;
import br.gov.jfrj.siga.wf.util.WfWikiParser;

/**
 * Classe que repesenta um View Object (Objeto Visão, ou seja, objeto que será
 * usado na exibição da interface do usuário) de uma tarefa.
 * 
 * @author kpf
 * 
 */
public class WfTaskVO {
	public static final String DISABLE_DOC_FORWARD = "disable_doc_forward";

	protected TaskInstance taskInstance;

	protected List<VariableAccess> variableList = new ArrayList<VariableAccess>();

	protected Set<WfTransitionVO> transitions = new TreeSet<WfTransitionVO>();

	private String descricao = null;

	private boolean conhecimentoEditavel = false;

	private String conhecimento = null;

	private String msgAviso = "";

	private List<String> tags;

	private String ancora;

	/**
	 * Construtor privado, impedindo a instanciação sem informar a tarefa.
	 */
	private WfTaskVO() {
	};

	/**
	 * Contrutor padrão (default)
	 * 
	 * @param taskInstance
	 */
	public WfTaskVO(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	public WfTaskVO(TaskInstance taskInstance, DpPessoa titular,
			DpLotacao lotaTitular) throws IllegalAccessException,
			InvocationTargetException, Exception, AplicacaoException {
		this(taskInstance, null, titular, lotaTitular);
	}

	public WfTaskVO(TaskInstance taskInstance, String siglaDoc,
			DpPessoa titular, DpLotacao lotaTitular)
			throws IllegalAccessException, InvocationTargetException,
			Exception, AplicacaoException {
		this.taskInstance = taskInstance;

		WfConhecimento c = WfDao.getInstance().consultarConhecimento(
				taskInstance.getProcessInstance().getProcessDefinition()
						.getName(), taskInstance.getName());
		if (c != null) {
			this.setDescricao(WfWikiParser.renderXHTML(c.getDescricao()));
			this.setConhecimento(c.getDescricao());
		} else {
			if (taskInstance.getDescription() != null) {
				this.setDescricao(WfWikiParser.renderXHTML(taskInstance
						.getDescription()));
				this.setConhecimento(taskInstance.getDescription());
			}
		}

		ExService service = Service.getExService();

		String siglaTitular = titular.getSigla() + "@"
				+ lotaTitular.getSiglaCompleta();

		String respWF = taskInstance.getActorId();
		if (respWF == null)
			for (PooledActor lot : (Collection<PooledActor>) taskInstance
					.getPooledActors()) {
				respWF = "@" + lot.getActorId();
				break;
			}

		// O ideal é que seja desconsiderada apenas a variavel cujo valor seja
		// igual a siglaDoc, e não todas que começam com doc_
		if (taskInstance.getTask().getTaskController() != null) {
			List<VariableAccess> variableAccesses = (List<VariableAccess>) taskInstance
					.getTask().getTaskController().getVariableAccesses();

			for (VariableAccess va : variableAccesses) {
				String access = va.getAccess().toString();
				if (access.contains("value=")) {
					Pattern pattern = Pattern.compile("value\\=(\\{[^}]+\\})");
					Matcher matcher = pattern.matcher(access);
					if (matcher.find()) {
						String expression = matcher.group(1);
						Object resultado = JbpmExpressionEvaluator.evaluate(
								"#" + expression,
								new ExecutionContext(taskInstance.getToken()));
						taskInstance.getContextInstance().setVariableLocally(
								va.getMappedName(), resultado,
								taskInstance.getToken());
					}
				}

				WfVariableAccessVO wfva = new WfVariableAccessVO(va);
				if (wfva.getMappedName().startsWith("doc_")) {
					String documento = (String) taskInstance
							.getContextInstance().getVariable(
									wfva.getMappedName());
					if (documento != null) {
						if (siglaDoc == null || !documento.startsWith(siglaDoc)) {
							this.variableList.add(wfva);
						} else {
							if (wfva != null)
								this.variableList.add(wfva);
							setMsgAviso("");
							if (!access.contains(DISABLE_DOC_FORWARD)) {
								// if (!service.isAtendente(value, siglaTitular)) {
								String respEX = service.getAtendente(documento,
										siglaTitular);
								DpLotacao lotEX = new PessoaLotacaoParser(respEX)
										.getLotacaoOuLotacaoPrincipalDaPessoa();
								// if (lotEX != null &&
								// !lotaTitular.equivale(lotEX))
								// wfva.setRespEX(lotEX.getSigla());
	
								DpLotacao lotWF = new PessoaLotacaoParser(respWF)
										.getLotacaoOuLotacaoPrincipalDaPessoa();
								// if (lotWF != null &&
								// !lotaTitular.equivale(lotWF))
								// wfva.setRespWF(lotWF.getSigla());
	
								// if (wfva.isAviso())
								// }
	
								boolean podeMovimentar = service.podeMovimentar(
										documento, siglaTitular);
								boolean estaComTarefa = titular
										.equivale(new PessoaLotacaoParser(respWF)
												.getPessoa());
								respEX = service.getAtendente(documento,
										siglaTitular);
								lotEX = new PessoaLotacaoParser(respEX)
										.getLotacaoOuLotacaoPrincipalDaPessoa();
	
								if (podeMovimentar && !estaComTarefa) {
									if (lotWF == null || !lotWF.equivale(lotEX)) {
										setMsgAviso("Esta tarefa só poderá prosseguir quando o documento "
												+ documento
												+ " for transferido para "
												+ (lotWF == null ? "Nula" : lotWF.getSigla()) + ".");
									}
								}
								if (!podeMovimentar && estaComTarefa) {
									setMsgAviso("Esta tarefa só poderá prosseguir quando o documento "
											+ documento
											+ ", que está com "
											+ lotEX.getSigla() + ", for devolvido.");
								}
								if (!podeMovimentar && !estaComTarefa) {
									if (lotWF != null && !lotWF.equivale(lotEX)) {
										setMsgAviso("Esta tarefa só poderá prosseguir quando o documento "
												+ documento
												+ ", que está com "
												+ lotEX.getSigla()
												+ ", for transferido para "
												+ lotWF.getSigla() + ".");
									}
								}
	
							}
						}
					} else {
						this.variableList.add(wfva);
					}
				} else {
					this.variableList.add(wfva);
				}
			}
		}
		if (taskInstance.getAvailableTransitions() != null) {
			WfAssignmentHandler ah = new WfAssignmentHandler();
			Set<String> set = new TreeSet<String>();

			for (Transition transition : (Collection<Transition>) taskInstance
					.getAvailableTransitions()) {

				set.clear();
				Node nextNode = transition.getTo();
				while (nextNode != null) {
					nextNode = (Node) Objeto.getImplementation(nextNode);
					if (nextNode instanceof TaskNode
							|| nextNode instanceof EndState)
						break;
					if (nextNode.getLeavingTransitions() != null
							&& nextNode.getLeavingTransitions().size() == 1) {
						nextNode = ((Transition) nextNode
								.getLeavingTransitions().get(0)).getTo();
					} else {
						nextNode = null;
						break;
					}
				}
				if (nextNode != null) {
					if (nextNode instanceof EndState) {
						set.add("FIM");
					} else if (nextNode instanceof TaskNode) {
						TaskNode taskNode = (TaskNode) nextNode;
						if (taskNode.getTasks() != null) {
							Set<Task> tasks = (Set<Task>) taskNode.getTasks();
							for (Task t : tasks) {
								ExecutionContext ctx = new ExecutionContext(
										taskInstance.getToken());
								ctx.setTaskInstance(taskInstance);
								if (t.getSwimlane() != null) {
									{
										SwimlaneInstance si = taskInstance
												.getTaskMgmtInstance()
												.getSwimlaneInstance(
														t.getSwimlane()
																.getName());
										if (si != null) {
											if (si.getActorId() != null)
												set.add(si.getActorId());
											if (si.getPooledActors() != null) {
												for (Object s : si
														.getPooledActors())
													set.add(((PooledActor) s)
															.getActorId());
											}
										}
									}
									if (set.size() == 0) {
										SwimlaneInstance si = new SwimlaneInstance(
												t.getSwimlane());
										si.setTaskMgmtInstance(taskInstance
												.getTaskMgmtInstance());
										try {
											ah.assign(si, ctx);
											if (si.getActorId() != null)
												set.add(si.getActorId());
											if (si.getPooledActors() != null) {
												for (Object s : si
														.getPooledActors())
													set.add(((PooledActor) s)
															.getActorId());
											}
										} catch (AplicacaoException e) {

										}
									}
								} else {
									TaskInstance ti = new TaskInstance();
									ti.setTask(t);
									ti.setTaskMgmtInstance(taskInstance
											.getTaskMgmtInstance());
									try {
										ah.assign(ti, ctx);
										if (ti.getActorId() != null)
											set.add(ti.getActorId());
										if (ti.getPooledActors() != null) {
											for (Object s : ti
													.getPooledActors())
												set.add(((PooledActor) s)
														.getActorId());
										}
									} catch (AplicacaoException e) {

									}
								}
							}
						}
					}
				}
				// Será que já temos em "set" uma lista das pessoas responsáveis
				// pelo próximo passo?
				// nextNode deve pular para o próximo se não for um tasknode e
				// se só tiver uma saida

				//System.out.println(set);

				this.transitions.add(new WfTransitionVO(transition, set));
			}
		}

		tags = new ArrayList<String>();
		if (taskInstance.getProcessInstance().getProcessDefinition().getName() != null)
			tags.add("@"
					+ Texto.slugify(taskInstance.getProcessInstance()
							.getProcessDefinition().getName(), true, true));
		if (taskInstance.getName() != null)
			tags.add("@" + Texto.slugify(taskInstance.getName(), true, true));

		if (taskInstance.getProcessInstance().getProcessDefinition().getName() != null
				&& taskInstance.getName() != null)
			ancora = "^wf:"
					+ Texto.slugify(taskInstance.getProcessInstance()
							.getProcessDefinition().getName()
							+ "-" + taskInstance.getName(), true, true);
	}

	/**
	 * Retorna o objeto TaskInstance.
	 * 
	 * @return
	 */
	public TaskInstance getTaskInstance() {
		return taskInstance;
	}

	/**
	 * Define o objeto TaskInstance.
	 * 
	 * @param taskInstance
	 */
	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	/**
	 * Retorna a lista de variáveis.
	 * 
	 * @return
	 */
	public List<VariableAccess> getVariableList() {
		return variableList;
	}

	/**
	 * Define a lista de variáveis.
	 * 
	 * @param variables
	 */
	public void setVariableList(List<VariableAccess> variables) {
		this.variableList = variables;
	}

	/**
	 * Retorna as transições.
	 * 
	 * @return
	 */
	public Set<WfTransitionVO> getTransitions() {
		return transitions;
	}

	/**
	 * Define as transições.
	 * 
	 * @param transitions
	 */
	public void setTransitions(Set<WfTransitionVO> transitions) {
		this.transitions = transitions;
	}

	/**
	 * Retorna a descrição da tarefa.
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isConhecimentoEditavel() {
		return this.conhecimentoEditavel;
	}

	public void setConhecimentoEditavel(boolean conhecimentoEditavel) {
		this.conhecimentoEditavel = conhecimentoEditavel;
	}

	public String getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(String conhecimento) {
		this.conhecimento = conhecimento;
	}

	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}

	public String getMsgAviso() {
		return msgAviso;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getAncora() {
		return ancora;
	}

	public void setAncora(String ancora) {
		this.ancora = ancora;
	}
	
	public String getCadastrante() {
		return (String) getTaskInstance().getVariable("wf_cadastrante");
	}
	public String getLotaCadastrante() {
		return (String) getTaskInstance().getVariable("wf_lota_cadastrante");
	}
	public String getTitular() {
		return (String) getTaskInstance().getVariable("wf_titular");
	}
	public String getLotaTitular() {
		return (String) getTaskInstance().getVariable("wf_lota_titular");
	}
}
