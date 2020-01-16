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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import com.crivano.jflow.model.ProcessInstance;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.WfConhecimento;
import br.gov.jfrj.siga.wf.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.WfInstanciaDeProcedimento;
import br.gov.jfrj.siga.wf.util.WfInstanciaDeTarefa;

/**
 * Classe que representa o DAO do sistema de workflow.
 * 
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WfDao extends CpDao implements com.crivano.jflow.Dao {

	public static final String CACHE_EX = "ex";

	private static final Logger log = Logger.getLogger(WfDao.class);

	public static WfDao getInstance() {
		return ModeloDao.getInstance(WfDao.class);
	}

	public WfDao() {
	}

	public WfDefinicaoDeProcedimento consultarWfDefinicaoDeProcedimentoPorNome(String nome) {
		CriteriaQuery<WfDefinicaoDeProcedimento> q = cb().createQuery(WfDefinicaoDeProcedimento.class);
		Root<WfDefinicaoDeProcedimento> c = q.from(WfDefinicaoDeProcedimento.class);
		q.select(c);
		q.where(cb().equal(cb().parameter(String.class, "nome"), nome));
		return em().createQuery(q).getSingleResult();
	}

	public SortedSet<WfInstanciaDeTarefa> consultarTarefasDeLotacao(DpLotacao lotaTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	public SortedSet<WfInstanciaDeTarefa> consultarTarefasAtivasPorDocumento(String siglaDoc) {
		// TODO Auto-generated method stub
		return null;
	}

	public void gravarInstanciaDeProcedimento(WfInstanciaDeProcedimento pi) {
		// TODO Auto-generated method stub

	}

	@Override
	public void persist(ProcessInstance pi) {
		gravarInstanciaDeProcedimento((WfInstanciaDeProcedimento) pi);
	}

	@Override
	public List<ProcessInstance> listByEvent(String event) {
		SortedSet<WfInstanciaDeTarefa> s = consultarTarefasAtivasPorDocumento(event);
		List<ProcessInstance> l = new ArrayList<>();
		for (WfInstanciaDeTarefa t : s) {
			l.add(t.getInstanciaDeProcesso());
		}
		return l;
	}

	public WfConhecimento consultarConhecimento(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

//	public List<ExDocumento> consultarDocsInclusosNoBoletim(ExDocumento doc) {
//		final Query query = em().createNamedQuery(
//				"consultarDocsInclusosNoBoletim");
//		query.setHint("org.hibernate.cacheable", true);
//		query.setHint("org.hibernate.cacheRegion", ExDao.CACHE_QUERY_SECONDS);
//
//		query.setParameter("idDoc", doc.getIdDoc());
//		return query.getResultList();
//	}
}