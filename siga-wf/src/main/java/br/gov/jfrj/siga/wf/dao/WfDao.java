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
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.enterprise.inject.Specializes;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.wf.model.WfConhecimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeResponsavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.WfResponsavel;
import br.gov.jfrj.siga.wf.model.WfVariavel;
import br.gov.jfrj.siga.wf.util.SiglaUtils;
import br.gov.jfrj.siga.wf.util.SiglaUtils.SiglaDecodificada;
import br.gov.jfrj.siga.wf.util.WfTarefa;

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
		q.where(cb().equal(c.get("nome"), nome));
		try {
			return em().createQuery(q).getSingleResult();
		} catch (Exception ex) {
			throw new RuntimeException("Não foi possível localizar definição de procedimento com nome '" + nome + "'",
					ex);
		}
	}

	public SortedSet<WfTarefa> consultarTarefasDeLotacao(DpLotacao lotaTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<WfProcedimento> consultarProcedimentosAtivosPorEvento(String evento) {
		String sql = "from WfProcedimento p where p.eventoNome like :eventoNome";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("eventoNome", evento + "%");
		List<WfProcedimento> result = query.getResultList();
		return result;
	}

	public void gravarInstanciaDeProcedimento(WfProcedimento pi) {
		SortedSet<Sincronizavel> setDepois = new TreeSet<>();
		SortedSet<Sincronizavel> setAntes = new TreeSet<>();

		setAntes.addAll(pi.getVariaveis());

		if (pi.getVariavelMap() != null) {
			for (String k : pi.getVariavelMap().keySet()) {
				WfVariavel v = new WfVariavel();
				v.setProcedimento(pi);
				v.setNome(k);

				Object o = pi.getVariavelMap().get(k);
				if (o != null) {
					if (o instanceof Date)
						v.setDate((Date) o);
					else if (o instanceof Double)
						v.setNumber((Double) o);
					else if (o instanceof Boolean)
						v.setBool((Boolean) o);
					else
						v.setString((String) o);
				}
				setDepois.add(v);
			}
		}

		// Utilizaremos o sincronizador para perceber apenas as diferenças entre a
		// as variáveis que estão no banco e as variáveis no mapa..
		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo(setDepois);
		sinc.setSetAntigo(setAntes);
		List<Item> list = sinc.getEncaixe();
		sinc.ordenarOperacoes();

		gravar(pi);
		for (Item i : list) {
			switch (i.getOperacao()) {
			case alterar:
				WfVariavel antigo = (WfVariavel) i.getAntigo();
				WfVariavel novo = (WfVariavel) i.getNovo();
				antigo.setBool(novo.getBool());
				antigo.setDate(novo.getDate());
				antigo.setNumber(novo.getNumber());
				antigo.setString(novo.getString());
				gravar(antigo);
				break;
			case incluir:
				pi.getVariaveis().add((WfVariavel) i.getNovo());
				gravar(i.getNovo());
				break;
			case excluir:
				pi.getVariaveis().remove((WfVariavel) i.getAntigo());
				excluir(i.getAntigo());
				break;
			}
		}
		gravar(pi);
	}

	@Override
	public void persist(WfProcedimento pi) {
		gravarInstanciaDeProcedimento((WfProcedimento) pi);
	}

	@Override
	public List<WfProcedimento> listByEvent(String event) {
		List<WfProcedimento> l = new ArrayList<>();
		l.addAll(consultarProcedimentosAtivosPorEvento(event));
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

	public List<WfProcedimento> consultarProcedimentosPorPessoaOuLotacao(DpPessoa titular, DpLotacao lotaTitular) {
		String sql = "select p from WfProcedimento p left join p.eventoPessoa pes left join p.eventoLotacao lot where pes.idPessoaIni = :idPessoaIni or lot.idLotacaoIni = :idLotacaoIni";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("idPessoaIni", titular.getIdPessoaIni());
		query.setParameter("idLotacaoIni", lotaTitular.getIdLotacaoIni());
		List<WfProcedimento> result = query.getResultList();
		return result;
	}

	public List<WfResponsavel> consultarResponsaveisPorDefinicaoDeResponsavel(WfDefinicaoDeResponsavel dr) {
		String sql = "from WfResponsavel o where o.definicaoDeResponsavel.hisIdIni = :idIni and o.hisDtFim is null";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("idIni", dr.getHisIdIni());
		List<WfResponsavel> result = query.getResultList();
		if (result == null || result.size() == 0)
			return null;
		return result;
	}

	public WfResponsavel consultarResponsavelPorOrgaoEDefinicaoDeResponsavel(CpOrgaoUsuario ou,
			WfDefinicaoDeResponsavel dr) {
		String sql = "from WfResponsavel o where o.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu and o.definicaoDeResponsavel.hisIdIni = :idIni and o.hisDtFim is null";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("idOrgaoUsu", ou.getIdOrgaoUsu());
		query.setParameter("idIni", dr.getHisIdIni());
		WfResponsavel result = (WfResponsavel) query.getSingleResult();
		return result;
	}

	public List<WfProcedimento> consultarProcedimentosParaEstatisticas(WfDefinicaoDeProcedimento pd, Date dataInicialDe,
			Date dataInicialAte, Date dataFinalDe, Date dataFinalAte) {
		String sql = "select p from WfProcedimento p inner join p.definicaoDeProcedimento pd where pd.hisIdIni = :hisIdIni and p.hisDtFim is not null "
				+ " and p.hisDtIni >= :dataInicialDe and p.hisDtIni <= :dataInicialAte "
				+ " and p.hisDtFim >= :dataFinalDe and p.hisDtFim <= :dataFinalAte";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("hisIdIni", pd.getHisIdIni());
		query.setParameter("dataInicialDe", dataInicialDe);
		query.setParameter("dataInicialAte", dataInicialAte);
		query.setParameter("dataFinalDe", dataFinalDe);
		query.setParameter("dataFinalAte", dataFinalAte);
		List<WfProcedimento> result = query.getResultList();
		return result;
	}

	public <T> T consultarPorSigla(String sigla, Class<T> clazz) {
		String acronimo = null;
		if (clazz.isAssignableFrom(WfProcedimento.class)) {
			acronimo = "WF";
		} else if (clazz.isAssignableFrom(WfDefinicaoDeProcedimento.class)) {
			acronimo = "DP";
		} else {
			throw new RuntimeException("Não é permitido consultar por sigla registros da classe " + clazz.getName());
		}
		SiglaDecodificada d = SiglaUtils.parse(sigla, acronimo, null);
		Integer ano = d.ano;
		Integer numero = d.numero;
		CpOrgaoUsuario orgaoUsuario = d.orgaoUsuario;

		final CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
		CriteriaQuery<T> q = criteriaBuilder.createQuery(clazz);
		Root<T> c = q.from(clazz);
		Join<T, CpOrgaoUsuario> joinOrgao = c.join("orgaoUsuario", JoinType.INNER);
		q.where(cb().equal(c.get("numero"), numero), cb().equal(c.get("ano"), ano),
				cb().equal(joinOrgao.get("idOrgaoUsu"), orgaoUsuario.getId()));
		return em().createQuery(q).getSingleResult();
	}

}