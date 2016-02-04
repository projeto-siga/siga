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

import org.hibernate.LockMode;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.wf.WfConfiguracao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Classe utilizada para designar um responsável para uma raia ou tarefa. Esta
 * classe é definida em siga-wf/src/jbpm.cfg.xml.
 * 
 * @author kpf
 * 
 */
public class WfAssignmentHandler implements AssignmentHandler {

	private static final long serialVersionUID = -5731439604710820114L;

	/**
	 * Designa um responsável.
	 * 
	 * @param assignable - raia ou tarefa 
	 * @param ctx - Contexto de execução. Contém as informações do processo, token, etc.
	 */
	public void assign(Assignable assignable, ExecutionContext ctx)
			throws Exception {

		ProcessDefinition pd;
		String procedimento;
		String raia;
		String tarefa;
		if (assignable instanceof TaskInstance) {
			TaskInstance t = (TaskInstance) assignable;
			pd = t.getTask().getProcessDefinition();
			raia = null;
			tarefa = t.getName();
		} else if (assignable instanceof SwimlaneInstance) {
			SwimlaneInstance s = (SwimlaneInstance) assignable;
			pd = s.getSwimlane().getTaskMgmtDefinition().getProcessDefinition();
			raia = s.getName();
			tarefa = null;
		} else
			return;
		procedimento = pd.getName();

		String iniciador = null;
		String lotaIniciador = null;
		DpPessoa titularIniciador = null;
		DpLotacao lotaTitularIniciador = null;
		iniciador = (String) ctx.getContextInstance().getVariable(
				Wf.getInstance().getBL().WF_TITULAR);
		ProcessInstance piTop = ctx.getProcessInstance();
		while (iniciador == null && piTop.getSuperProcessToken() != null) {
			piTop = piTop.getSuperProcessToken()
					.getProcessInstance();
			iniciador = (String) piTop.getContextInstance().getVariable(
					Wf.getInstance().getBL().WF_TITULAR);
		}
		if (iniciador != null) {
			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(iniciador);
			titularIniciador = (DpPessoa) WfDao.getInstance()
					.consultarPorSigla(flt);
			lotaIniciador = (String) piTop.getContextInstance().getVariable(
					Wf.getInstance().getBL().WF_LOTA_TITULAR);
			DpLotacaoDaoFiltro fltLota = new DpLotacaoDaoFiltro();
			fltLota.setSiglaCompleta(lotaIniciador);
			lotaTitularIniciador = (DpLotacao) WfDao.getInstance()
					.consultarPorSigla(fltLota);
		} else {
			Swimlane s = ctx.getTaskMgmtInstance().getTaskMgmtDefinition()
					.getStartTask().getSwimlane();
			if (s != null) {
				SwimlaneInstance si = ctx.getTaskMgmtInstance()
						.getSwimlaneInstance(s.getName());
				if (si!=null){
					iniciador = si.getActorId();
				}
				if (iniciador != null) {
					DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
					flt.setSigla(iniciador);
					titularIniciador = (DpPessoa) WfDao.getInstance()
							.consultarPorSigla(flt);
					lotaTitularIniciador = titularIniciador.getLotacao();
				}
			}
		}

		DpPessoa titularAnterior = null;
		DpLotacao lotaTitularAnterior = null;
		String anterior = ctx.getTaskInstance().getActorId();
		if (anterior != null) {
			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(anterior);
			titularAnterior = (DpPessoa) WfDao.getInstance().consultarPorSigla(
					flt);
			lotaTitularAnterior = titularAnterior.getLotacao();
		}

		WfConfiguracao cfg = Wf.getInstance().getComp().designar(
				titularIniciador, lotaTitularIniciador, titularAnterior,
				lotaTitularAnterior, procedimento, raia, tarefa);

		if (cfg == null) {
			throw new AplicacaoException(
					"Não foi possível localizar uma configuração para designar o responsável.");
		}

		if (cfg.getAtor() != null) {
			assignable.setActorId(cfg.getAtor().getSigla());
		}
		if (cfg.getLotaAtor() != null) {
			String[] a = new String[] { cfg.getLotaAtor().getSiglaCompleta() };
			assignable.setPooledActors(a);
		}
		if (cfg.getExpressao() != null) {
			WfExpressionAssignmentHandler expah = new WfExpressionAssignmentHandler();
			expah.assign(cfg.getExpressao(), assignable, ctx);
		}
		return;
	}
}
