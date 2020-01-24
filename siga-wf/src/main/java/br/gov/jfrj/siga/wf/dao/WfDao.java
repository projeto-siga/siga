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

import javax.enterprise.inject.Specializes;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.model.WfConhecimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.WfTarefa;

/**
 * Classe que representa o DAO do sistema de workflow.
 * 
 */
@Specializes
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WfDao extends CpDao implements com.crivano.jflow.Dao<WfProcedimento> {

	public static final String CACHE_WF = "wf";

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

	public SortedSet<WfTarefa> consultarTarefasDeLotacao(DpLotacao lotaTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<WfProcedimento> consultarProcedimentosAtivosPorDocumento(String siglaDoc) {
		// TODO Auto-generated method stub
		return null;
	}

	public void gravarInstanciaDeProcedimento(WfProcedimento pi) {
		// TODO Auto-generated method stub

	}

	@Override
	public void persist(WfProcedimento pi) {
		gravarInstanciaDeProcedimento((WfProcedimento) pi);
	}

	@Override
	public List<WfProcedimento> listByEvent(String event) {
		List<WfProcedimento> l = new ArrayList<>();
		l.addAll(consultarProcedimentosAtivosPorDocumento(event));
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

	public List<WfProcedimento> consultarProcedimentosPorLotacao(DpLotacao lotaTitular) {
		String sql = "from WfProcedimento p where p.lotacao.idLotacaoIni = :idLotacaoIni";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("idLotacaoIni", lotaTitular.getIdLotacaoIni());
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<WfProcedimento> result = query.getResultList();
		if (result == null || result.size() == 0)
			return null;
		return result;
	}
}