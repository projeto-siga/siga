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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.jpdl.el.FunctionMapper;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfFunctionMapper implements FunctionMapper {

	public Method resolveFunction(String namespace, String function) {
		if (namespace.equals("ex") || namespace.equals("wf")) {
			for (Method m : this.getClass().getDeclaredMethods()) {
				if (m.getName().equals(namespace + "_" + function))
					return m;
			}
		}
		return null;
	}

	public static boolean ex_isAssinado(String codigoDocumento)
			throws Exception {
		if (codigoDocumento == null || codigoDocumento.length() == 0)
			throw new AplicacaoException(
					"Código do documento precisa ser informado quando é feita uma chamada para ex:isAssinado.");
		Boolean b = Service.getExService().isAssinado(codigoDocumento, null);
		return b;
	}

	public static String ex_form(String codigoDocumento, String variavel)
			throws Exception {
		if (codigoDocumento == null || codigoDocumento.length() == 0)
			throw new AplicacaoException(
					"Código do documento precisa ser informado quando é feita uma chamada para ex:form.");
		String s = Service.getExService().form(codigoDocumento, variavel);
		return s;
	}

	public static String wf_prosseguirCondicional() {
		return "br.gov.jfrj.siga.wf.util.WfProsseguirCondicionalActionHandler";
	}

	public static Long wf_getDays(TaskInstance ti) throws Exception {
		if (ti.getCreate() == null)
			return null;

		SigaCalendar lIni = new SigaCalendar(ti.getCreate().getTime());
		SigaCalendar lFim = new SigaCalendar(WfDao.getInstance().dt().getTime());
		long l = lFim.getUnixDay() - lIni.getUnixDay();

		return l;
	}

	public static String wf_getGroup(TaskInstance ti) {
		DpLotacao lota = null;
		if (ti.getActorId() != null) {
			DpPessoa ator = WfDao.getInstance().getPessoaFromSigla(
					ti.getActorId());
			if (ator == null)
				return null;
			lota = ator.getLotacao();
		} else if (ti.getPooledActors().size() != 0) {
			String groupId = (String) ((PooledActor) (ti.getPooledActors()
					.toArray()[0])).getActorId();
			lota = WfDao.getInstance().getLotacaoFromSigla(groupId);
		}
		return lota.getSiglaCompleta();
	}

}
