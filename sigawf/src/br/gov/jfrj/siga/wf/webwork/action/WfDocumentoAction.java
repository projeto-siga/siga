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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import org.jbpm.context.def.VariableAccess;
import org.jbpm.db.GraphSession;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

import com.opensymphony.xwork.Action;

/**
 * Classe reponsável pelas actions relativas à documentos.
 * 
 * @author kpf
 * 
 */
public class WfDocumentoAction extends WfSigaActionSupport {

	protected String sigla;

	protected Integer maxvia;

	protected Map<String, List<WfTaskVO>> mobilMap = new TreeMap<String, List<WfTaskVO>>();

	protected SortedSet<TaskInstance> taskInstances;

	/**
	 * Classe utilizada para comparar duas TaskInstance.
	 * 
	 * @author kpf
	 * 
	 */
	private class TaskInstanceComparator implements Comparator<TaskInstance> {

		/**
		 * Compara dois objetos TaskInstance. Retorna zero (0) se os objetos são
		 * "iguais", retorna (1) se a PRIORIDADE ou ID do primeiro objeto (o1)
		 * for maior doque o segundo objeto (o2). Retorna (-1), caso contrário.
		 * ESTE CÓDIGO ESTÁ DUPLICADO EM WfInboxAction.java.
		 */
		public int compare(TaskInstance o1, TaskInstance o2) {
			if (o1.getPriority() > o2.getPriority())
				return 1;
			if (o1.getPriority() < o2.getPriority())
				return -1;
			if (o1.getId() > o2.getId())
				return 1;
			if (o1.getId() < o2.getId())
				return -1;
			return 0;
		}

	}

	/**
	 * Retorna o mapa de mobil.
	 * 
	 * @return
	 */
	public Map<String, List<WfTaskVO>> getMobilMap() {
		return mobilMap;
	}

	/**
	 * Define o mapa de mobil.
	 * 
	 * @param mobilMap
	 */
	public void setMobilMap(Map<String, List<WfTaskVO>> mobilMap) {
		this.mobilMap = mobilMap;
	}

	/**
	 * Verifica se as tarefas que estão designadas para o usuário estão
	 * associadas a documentos do SIGA-DOC (por intermédio da variável iniciada
	 * com o prefixo "doc_". Se estiver, chama o método addTask(). Este método
	 * executado quando a action "doc.action" for chamada.
	 */
	public String execute() throws Exception {

		GraphSession graph = WfContextBuilder.getJbpmContext()
				.getGraphSession();

		// taskInstances = WfInboxAction.getTaskList(getCadastrante(),
		// getTitular(), getLotaTitular());
		taskInstances = Wf.getInstance().getBL().getTaskList(sigla);
		for (TaskInstance ti : taskInstances) {
			if (ti.getTask().getTaskController() != null) {
				List<VariableAccess> variableAccesses = (List<VariableAccess>) ti
						.getTask().getTaskController().getVariableAccesses();

				for (VariableAccess _variable : variableAccesses) {
					String name = _variable.getMappedName();
					if (name != null && name.startsWith("doc_")) {
						String value = (String) ti.getContextInstance()
								.getVariable(name);
						if (value != null) {
							if (value.startsWith(sigla)) {
								addTask(ti, value);
							}
						}
					}
				}
			}
		}

		return Action.SUCCESS;
	}

	/**
	 * Monta o objeto WfTaskVO (view object, que será usado na interface do
	 * usuário).
	 * 
	 * @param taskInstance
	 * @param variableAccesses
	 * @param siglaDoc
	 * @throws Exception
	 */
	private void addTask(TaskInstance taskInstance, String siglaDoc)
			throws Exception {
		WfTaskVO task = new WfTaskVO(taskInstance, sigla, getTitular(),
				getLotaTitular());
		if (!mobilMap.containsKey(siglaDoc)) {
			mobilMap.put(siglaDoc, new ArrayList<WfTaskVO>());
		}
		List<WfTaskVO> tasks = mobilMap.get(siglaDoc);
		tasks.add(task);
	}

	/**
	 * Retorna a sigla.
	 * 
	 * @return
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Define a sigla.
	 * 
	 * @param sigla
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * Retona o número máximo de vias.
	 * 
	 * @return
	 */
	public Integer getMaxvia() {
		return maxvia;
	}

	/**
	 * Define o número máximo de vias.
	 * 
	 * @param maxvia
	 */
	public void setMaxvia(Integer maxvia) {
		this.maxvia = maxvia;
	}

}
