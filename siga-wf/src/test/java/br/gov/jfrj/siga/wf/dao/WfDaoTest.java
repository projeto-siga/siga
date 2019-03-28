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
package br.gov.jfrj.siga.wf.dao;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.TaskInstance;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.util.WfExpressionAssignmentHandler;
import br.gov.jfrj.siga.wf.util.WfHibernateUtil;
import br.gov.jfrj.siga.wf.util.WfTaskInstance;

/**
 * Classe utilitária para testar o DAO do workflow.
 * 
 * @author kpf
 * 
 */
public class WfDaoTest extends TestCase {
	private WfDao dao;

	public WfDaoTest() throws Exception {
		if (false) {
			CpAmbienteEnumBL ambiente = CpAmbienteEnumBL.DESENVOLVIMENTO;
			Cp.getInstance().getProp().setPrefixo(ambiente.getSigla());
			Configuration cfg = CpDao.criarHibernateCfg(ambiente);
			// ModeloDao.configurarHibernateParaDebug(cfg);
			WfHibernateUtil.configurarHibernate(cfg);

			dao = WfDao.getInstance();
		}
	}

	/**
	 * @param args
	 * @throws AplicacaoException
	 */
	public void testDataUltimaAtualizacao() throws AplicacaoException {
		if (true)
			return;
		System.out.println("Data e hora da ultima atualização - "
				+ dao.consultarDataUltimaAtualizacao());
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public void testPodeInstanciarProcedimento() throws Exception {
		if (true)
			return;
		DpPessoa pesSigla1 = new DpPessoa();
		pesSigla1.setSesbPessoa("RJ");
		pesSigla1.setMatricula(13635L);
		DpPessoa pes1 = dao.consultarPorSigla(pesSigla1);

		Boolean b = Wf
				.getInstance()
				.getComp()
				.podeInstanciarProcedimento(pes1, pes1.getLotacao(),
						"ProcessDefinition(Contratação: fase de análise)");

		System.out.println("Pode instanciar procedimento - " + b);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public void testPodeUtilizarServicoPorConfiguracao() throws Exception {
		if (true)
			return;
		DpPessoa pesSigla1 = new DpPessoa();
		pesSigla1.setSesbPessoa("RJ");
		pesSigla1.setMatricula(13635L);
		DpPessoa pes1 = dao.consultarPorSigla(pesSigla1);

		Boolean b = Wf
				.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(
						pes1,
						pes1.getLotacao(),
						"SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow;OPERAR:Executar comandos da tela inicial");

		System.out.println("Pode utilizar servico por configuracao - " + b);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public void testLerGrupo() throws Exception {
		if (true)
			return;
		CpGrupo g = dao.consultar(169L, CpGrupo.class, false);
	}

	private class AssignableMock implements Assignable {
		private String actorId;
		private String[] pooledActors;

		public String getActorId() {
			return actorId;
		}

		public void setActorId(String actorId) {
			this.actorId = actorId;
		}

		public String[] getPooledActors() {
			return pooledActors;
		}

		public void setPooledActors(String[] pooledActors) {
			this.pooledActors = pooledActors;
		}
	}

	public void testExpressionAssignmentHandler() throws Exception {
		if (true)
			return;
		DpPessoa pesSigla1 = new DpPessoa();
		pesSigla1.setSesbPessoa("RJ");
		pesSigla1.setMatricula(13635L);
		DpPessoa pes1 = dao.consultarPorSigla(pesSigla1);

		AssignableMock a = new AssignableMock();

		WfExpressionAssignmentHandler eah = new WfExpressionAssignmentHandler();
		eah.setEntity(pes1);
		eah.assign("function(diretor.*)", a, null);
		assertEquals(a.getActorId(), "RJ13293");

		// TaskInstance ti = WfContextBuilder.getJbpmContext().getJbpmContext()
		// .getTaskInstance(12805);
		TaskInstance ti = dao.consultar(12805L, TaskInstance.class, false);
		System.out.println(ti.getName());
		ti = (TaskInstance) Objeto.getImplementation(ti);
		System.out.println(((WfTaskInstance) ti).getDays());
		assertTrue(ti instanceof WfTaskInstance);
		// if (ti instanceof WfTaskInstance)
		// System.out.println(((WfTaskInstance)ti).getDays());
	}

}
