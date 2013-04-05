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
package br.gov.jfrj.siga.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.util.ReflectHelper;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExBoletimDoc;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.hibernate.ext.IMontadorQuery;
import br.gov.jfrj.siga.hibernate.ext.MontadorQuery;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.Aguarde;
import br.gov.jfrj.siga.persistencia.ExClassificacaoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExDocumentoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

public class ExDao extends CpDao {

	private static final Logger log = Logger.getLogger(ExDao.class);

	IMontadorQuery montadorQuery = null;

	public static ExDao getInstance() {
		return ModeloDao.getInstance(ExDao.class);
	}

	public ExDao() {
		try {
			montadorQuery = (IMontadorQuery) Class.forName(
					SigaExProperties.getMontadorQuery()).newInstance();
			montadorQuery.setMontadorPrincipal(new MontadorQuery());
		} catch (Exception e) {
			montadorQuery = new MontadorQuery();
		}
	}

	public void reindexarVarios(List<ExDocumento> docs, boolean apenasExcluir)
			throws Exception {
		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());
		Transaction tx = fullTextSession.beginTransaction();
		for (ExDocumento doc : docs) {
			fullTextSession.purge(ExDocumento.class, doc);
			if ((!apenasExcluir) && doc.isIndexavel())
				fullTextSession.index(doc);
		}
		tx.commit();
		fullTextSession.clear();
		getSessao().clear();
	}

	public List<ExDocumento> consultarEmLotePorId(Object[] ids) {
		if (ids.length > 0) {
			String clausula = " where ";
			StringBuffer appender = new StringBuffer("from ExDocumento doc");
			for (int k = 0; k < ids.length; k++) {
				appender.append(clausula);
				appender.append("doc.idDoc = ");
				appender.append(ids[k]);
				if (k == 0)
					clausula = " or ";
			}
			Query query = getSessao().createQuery(appender.toString());
			return query.list();
		}
		return null;
	}

	public Long obterProximoNumero(final ExDocumento doc, Long anoEmissao)
			throws SQLException {
		Query query = getSessao().getNamedQuery("obterProximoNumero");
		query.setLong("idOrgaoUsu", doc.getOrgaoUsuario().getId());
		query.setLong("idFormaDoc", doc.getExFormaDocumento().getId());
		query.setLong("anoEmissao", anoEmissao);

		return (Long) query.uniqueResult();
	}

	public List consultarPorFiltro(final ExMobilDaoFiltro flt) {
		return consultarPorFiltro(flt, 0, 0);
	}

	public List consultarPorFiltro(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina) {
		return consultarPorFiltro(flt, 0, 0, new DpPessoa(), new DpLotacao());
	}

	public List consultarPorFiltro(final ExMobilDaoFiltro flt,
			DpPessoa titular, DpLotacao lotaTitular) {
		return consultarPorFiltro(flt, 0, 0, titular, lotaTitular);
	}

	public List consultarPorFiltroOtimizado(final ExMobilDaoFiltro flt) {
		return consultarPorFiltroOtimizado(flt, 0, 0);
	}

	public List consultarPorFiltroOtimizado(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina) {
		return consultarPorFiltroOtimizado(flt, 0, 0, new DpPessoa(),
				new DpLotacao());
	}

	public List consultarPorFiltroOtimizado(final ExMobilDaoFiltro flt,
			DpPessoa titular, DpLotacao lotaTitular) {
		return consultarPorFiltroOtimizado(flt, 0, 0, titular, lotaTitular);
	}

	public List consultarPorFiltro(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina, DpPessoa titular,
			DpLotacao lotaTitular) {

		Query query = getSessao().getNamedQuery("consultarPorFiltro");
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}
		query.setProperties(flt);
		query.setLong("titular",
				titular.getIdPessoaIni() != null ? titular.getIdPessoaIni() : 0);
		query.setLong(
				"lotaTitular",
				lotaTitular.getIdLotacaoIni() != null ? lotaTitular
						.getIdLotacaoIni() : 0);
		List l = query.list();
		return l;
	}

	public List consultarPorFiltroOtimizado(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina, DpPessoa titular,
			DpLotacao lotaTitular) {
		Query query = getSessao().createQuery(
				montadorQuery.montaQueryConsultaporFiltro(flt, titular,
						lotaTitular, false));
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}
		List l = query.list();
		return l;
	}

	public Integer consultarQuantidadePorFiltroOtimizado(
			final ExMobilDaoFiltro flt, DpPessoa titular, DpLotacao lotaTitular) {
		long tempoIni = System.nanoTime();
		String s = montadorQuery.montaQueryConsultaporFiltro(flt, titular,
				lotaTitular, true);
		Query query = getSessao().createQuery(s);
		Long l = (Long) query.uniqueResult();
		long tempoTotal = System.nanoTime() - tempoIni;
		System.out.println("consultarQuantidadePorFiltroOtimizado: "
				+ tempoTotal / 1000000 + " ms -> " + s + ", resultado: " + l);
		return l.intValue();
	}

	public List consultarPorFiltroOld(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina, DpPessoa titular,
			DpLotacao lotaTitular) {
		try {
			final Query query = getSessao().getNamedQuery("consultarPorFiltro");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			query.setProperties(flt);
			if (titular.getIdPessoaIni() != null)
				query.setLong("titular", titular.getIdPessoaIni());
			else
				query.setLong("titular", 0);
			if (lotaTitular.getIdLotacaoIni() != null)
				query.setLong("lotaTitular", lotaTitular.getIdLotacaoIni());
			else
				query.setLong("lotaTitular", 0);

			if (flt.getDescrDocumento() != null)
				query.setString("descrDocumento", flt.getDescrDocumento()
						.toUpperCase().replace(' ', '%'));

			return query.list();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public Integer consultarQuantidadePorFiltro(final ExMobilDaoFiltro flt,
			DpPessoa titular, DpLotacao lotaTitular) {
		Query query = getSessao().getNamedQuery("consultarQuantidadePorFiltro");
		query.setProperties(flt);
		query.setLong("titular", titular.getIdPessoaIni());
		query.setLong("lotaTitular", lotaTitular.getIdLotacaoIni());
		long tempoIni = System.nanoTime();
		Long l = (Long) query.uniqueResult();
		long tempoTotal = System.nanoTime() - tempoIni;
		System.out.println("consultarQuantidadePorFiltro: " + tempoTotal
				/ 1000000 + ", resultado: " + l);
		return l.intValue();
	}

	public int consultarQuantidade(final ExDocumentoDaoFiltro flt) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ExMobil consultarPorSigla(final ExMobilDaoFiltro flt) {
		try {
			if (flt.getIdDoc() != null
					&& (flt.getIdTipoMobil() == null || flt.getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_GERAL)) {
				final ExDocumento d = consultar(flt.getIdDoc(),
						ExDocumento.class, false);
				return d.getMobilGeral();
			}

			if (flt.getAnoEmissao() == null)
				flt.setAnoEmissao(Long.valueOf(new Date().getYear()) + 1900);

			if (flt.getNumSequencia() == null) {
				final Query query = getSessao().getNamedQuery(
						"consultarPorSiglaDocumento");
				query.setProperties(flt);

				final List<ExDocumento> l = query.list();

				if (l.size() != 1)
					return null;

				return l.get(0).getMobilGeral();
			}

			final Query query = getSessao().getNamedQuery("consultarPorSigla");
			query.setProperties(flt);

			final List<ExMobil> l = query.list();

			if (l.size() != 1)
				return null;

			final ExMobil retorno = new ExMobil();

			return l.get(0);

		} catch (final NullPointerException e) {
			return null;
		}
	}

	public long obterProximoNumeroVia(final ExDocumento doc) throws Exception {

		return doc.getNumUltimaVia() + 1;

	}

	public List<ExFormaDocumento> listarTodosOrdenarPorDescricao() {
		final Criteria crit = getSessao()
				.createCriteria(ExFormaDocumento.class);
		crit.addOrder(Order.asc("descrFormaDoc"));
		return crit.list();
	}

	public List<ExDocumento> consultarPorModeloEAssinatura(
			CpOrgaoUsuario orgaoUsu, ExModelo mod, Date dtAssinaturaIni,
			Date dtAssinaturaFim) {
		if (mod != null) {
			final Query query = getSessao().getNamedQuery(
					"consultarPorModeloEAssinatura");
			query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
			query.setLong("idMod", mod.getIdMod());
			query.setDate("dataIni", dtAssinaturaIni);
			query.setDate("dataFim", dtAssinaturaFim);
			return query.list();
		}
		return null;
	}

	public Object[] consultarPorTexto(String query) throws Exception {
		return consultarPorTexto(query, 0, 0);
	}

	public Object[] consultarPorTexto(String query, int offset, int itemPagina)
			throws Exception {

		// Substitui n espaços brancos seguidos de letra ou número por " +"
		// As duas linhas abaixo poderão ser substituídas por uma
		// query = "+"+query.replaceAll("(\\s+)", " +");
		// query = query.replaceAll("\\+\\-", "-");

		QueryParser parser = new MultiFieldQueryParser(new String[] {
				"conteudoBlobDocHtml", "nmMod", "descrDocumento",
				"exMovimentacaoIndexacaoSet.descrMov",
				"exMovimentacaoIndexacaoSet.descrTipoMovimentacao",
				"exMovimentacaoIndexacaoSet.conteudoBlobMovHtml",
				"exMovimentacaoIndexacaoSet.conteudoBlobMovPdf" },
				new BrazilianAnalyzer());
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);

		org.apache.lucene.search.Query luceneQuery = parser.parse(query);

		// ------------------------------- hibernate search query:

		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());

		FullTextQuery hbQuery = fullTextSession.createFullTextQuery(
				luceneQuery, ExDocumento.class);

		if (offset > 0) {
			hbQuery.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			hbQuery.setMaxResults(itemPagina);
		}

		hbQuery.setCacheable(true);

		hbQuery.setProjection("descrDocumento", "nmMod", "codigo", "idDoc",
				"dtDocDDMMYY", "subscritorString", "destinatarioString",
				"nivelAcesso");

		return new Object[] { hbQuery.list(), hbQuery.getResultSize() };
	}

	public void listarNaoIndexados(int aPartir, boolean irIndexando)
			throws Exception {

		long tempoIni = System.currentTimeMillis();
		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());
		QueryParser parser = new QueryParser("idDoc", new StandardAnalyzer());

		final Criteria crit = getSessao().createCriteria(ExDocumento.class)
				.add(Restrictions.gt("idDoc", new Long(aPartir)));
		crit.setMaxResults(300);
		crit.addOrder(Order.asc("idDoc"));
		List<ExDocumento> list = new ArrayList<ExDocumento>();
		int firstResult = 0;
		do {
			crit.setFirstResult(firstResult);
			list = crit.list();
			for (ExDocumento doc : list) {
				firstResult++;
				if (doc.isIndexavel()
						&& (fullTextSession.createFullTextQuery(
								new TermQuery(new Term("idDoc",
										String.valueOf(doc.getIdDoc()))),
								ExDocumento.class).getResultSize() == 0)) {

					if (irIndexando) {
						System.out.println("listarNaoIndexados - indexando "
								+ doc.getCodigo());
						indexar(doc);
					} else {
						System.out.println("listarNaoIndexados - nao indexar "
								+ doc.getCodigo());
					}
				}
			}
			System.out.println("listarNaoIndexados - " + firstResult
					+ " varridos");
			getSessao().clear();
		} while (list.size() > 0);
		System.out.println("listarNaoIndexados - FIM    "
				+ (System.currentTimeMillis() - tempoIni) / 3600000
				+ " minutos");
	}

	public void indexarFila(String path) throws Exception {
		String _path = path;
		if (_path == null)
			_path = SigaExProperties.getString("siga.lucene.index.path")
					+ "/siga-ex-lucene-index-fila";
		File dir = new File(_path);
		ExDocumento doc = null;
		List<ExDocumento> listaDocs = new ArrayList<ExDocumento>();

		File[] children = dir.listFiles();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				String filename = children[i].getName();

				try {
					doc = consultar(Long.valueOf(filename), ExDocumento.class,
							false);
					listaDocs.add(doc);
					children[i].delete();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			reindexarVarios(listaDocs, false);
		}
	}

	public void indexarTudo() throws Exception {
		indexarTudo(null);
		return;
	}

	public void indexarTudo(Aguarde aguarde) throws Exception {

		System.out.println("Indexando documentos...");
		long inicio = new Date().getTime();

		try {
			FullTextSession fullTextSession = Search
					.createFullTextSession(getSessao());
			Transaction deleteTx = fullTextSession.beginTransaction();
			fullTextSession.purgeAll(ExDocumento.class);
			deleteTx.commit();
			fullTextSession.clear();
			getSessao().clear();
		} catch (Throwable t) {
			// Não havia documentos a excluir
		}

		final Criteria crit = getSessao().createCriteria(ExDocumento.class);

		int index = 0;

		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());
		fullTextSession.setFlushMode(FlushMode.MANUAL);
		fullTextSession.setCacheMode(CacheMode.IGNORE);
		crit.setMaxResults(30);
		crit.addOrder(Order.desc("idDoc"));
		List<ExDocumento> list;
		do {
			crit.setFirstResult(index);
			list = crit.list();
			Transaction tx = fullTextSession.beginTransaction();
			for (ExDocumento doc : list) {
				index++;
				// if (aguarde != null)
				// aguarde.setMensagem(String.valueOf(index)
				// + " documentos já indexados.");
				if (doc.isIndexavel())
					fullTextSession.index(doc);

				if (index % 100 == 0) {
					System.gc();
					// fullTextSession.flush();
					// fullTextSession.clear();
				}
			}
			tx.commit();
			fullTextSession.clear();
			getSessao().clear();
			System.out.print(String.valueOf(index)
					+ " documentos já indexados. --  -- ");
		} while (list.size() > 0);
		System.gc();

		// fullTextSession.close();
		System.out.println("Duração da indexação de documentos: "
				+ (new Date().getTime() - inicio));

		if (aguarde != null)
			aguarde.setMensagem(String.valueOf(index));

	}

	public void indexarUltimas(int desde) throws Exception {

		System.out.println("Indexando documentos...");
		long inicio = new Date().getTime();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, desde);
		Date dtIni = cal.getTime();

		Criteria crit = getSessao().createCriteria(ExDocumento.class);
		crit.createCriteria("exMovimentacaoSet").add(
				Restrictions.gt("dtIniMov", dtIni));
		crit.setResultTransformer(crit.DISTINCT_ROOT_ENTITY);

		/*
		 * Query indexQuery = getSessao() .createQuery( "from ExDocumento as doc
		 * inner join doc.exMovimentacaoSet as mov where mov.dtIniMov >=
		 * :dtIni"); indexQuery.setParameter("dtIni", dtIni);
		 */

		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());
		// List<ExDocumento> list = indexQuery.list();
		List<ExDocumento> list = crit.list();
		Transaction tx = fullTextSession.beginTransaction();
		for (ExDocumento doc : list) {
			System.out.println(" . " + doc.getIdDoc());
			fullTextSession.purge(ExDocumento.class, doc);
			if (doc.isIndexavel())
				fullTextSession.index(doc);
		}
		tx.commit();
		fullTextSession.clear();
		getSessao().clear();
		System.gc();

		// fullTextSession.close();
		System.out.println("Duração da indexação de documentos: "
				+ (new Date().getTime() - inicio));

	}

	public List<ExDocumento> listarAgendados() {
		final Query query = getSessao().getNamedQuery("listarAgendados");
		return query.list();
	}

	public List<ExDocumento> consultarPorModeloParaPublicar(
			CpOrgaoUsuario orgaoUsu) {
		final Query query = getSessao().getNamedQuery(
				"consultarPorModeloParaPublicarBI");
		query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		return query.list();
	}

	public List<ExDocumento> consultarPorBoletimParaPublicar(ExDocumento doc) {
		final Query query = getSessao().getNamedQuery(
				"consultarPorBoletimParaPublicarBI");

		query.setLong("idDoc", doc.getIdDoc());
		return query.list();
	}

	public ExBoletimDoc consultarBoletimPorDocumento(ExDocumento doc) {
		final Query query = getSessao().getNamedQuery(
				"consultarBoletimPorDocumento");

		query.setLong("idDoc", doc.getIdDoc());

		final List<ExBoletimDoc> l = query.list();
		if (l.size() != 1)
			return null;

		return l.get(0);
	}

	public List<ExBoletimDoc> consultarBoletimPorBoletim(ExDocumento doc) {
		final Query query = getSessao().getNamedQuery(
				"consultarBoletimPorBoletim");

		query.setLong("idDoc", doc.getIdDoc());

		return query.list();
	}

	public List<String> consultarEmailNotificacao(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarEmailPorLotacao");

		query.setLong("idLotacaoIni", lot.getIdLotacaoIni());

		return query.list();
	}

	public void indexar(ExDocumento entidade) {
		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());
		Transaction tx = fullTextSession.beginTransaction();
		fullTextSession.index(entidade);
		tx.commit();
	}

	public void desindexar(ExDocumento entidade) {
		FullTextSession fullTextSession = Search
				.createFullTextSession(getSessao());
		try {
			Transaction tx = fullTextSession.beginTransaction();
			fullTextSession.purge(ExDocumento.class, entidade.getIdDoc());
			tx.commit();
		} catch (Throwable t) {
			// Não havia aquela movimentação no índice
		}
	}

	public List consultarPorResponsavel(final DpPessoa o, final DpLotacao lot)
			throws SQLException {
		try {
			String s = " SELECT ID, DESCR, ORDEM, SUM(C1) C1, SUM(C2) C2 FROM ( "
					+
					// " -- pes " +
					" SELECT EST.ID_ESTADO_DOC ID, EST.DESC_ESTADO_DOC DESCR,  EST.ORDEM_ESTADO_DOC ORDEM, COUNT(1) C1, 0 C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_PESSOA_SIN PES, EX_MOVIMENTACAO MOVR "
					+ " WHERE "
					+ " ID_PESSOA_INICIAL = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND MOVR.ID_RESP = PES.ID_PESSOA "
					+ " AND EST.ID_ESTADO_DOC <> 9 AND EST.ID_ESTADO_DOC <> 10 AND EST.ID_ESTADO_DOC <> 11 AND EST.ID_ESTADO_DOC <> 6 AND EST.ID_ESTADO_DOC <> 8 AND EST.ID_ESTADO_DOC <> 12 AND EST.ID_ESTADO_DOC <> 13 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT EST.ID_ESTADO_DOC ID, EST.DESC_ESTADO_DOC DESCR,  EST.ORDEM_ESTADO_DOC ORDEM, COUNT(1) C1, 0 C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_PESSOA_SIN PES, EX_MOVIMENTACAO MOVR "
					+ " WHERE "
					+ " ID_PESSOA_INICIAL = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND MOVR.ID_ESTADO_DOC = 11 AND MOVR.ID_CADASTRANTE = PES.ID_PESSOA "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT EST.ID_ESTADO_DOC ID, EST.DESC_ESTADO_DOC DESCR,  EST.ORDEM_ESTADO_DOC ORDEM, COUNT(1) C1, 0 C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_PESSOA_SIN PES, EX_DOCUMENTO DOCR "
					+ " WHERE "
					+ " ID_PESSOA_INICIAL = ? "
					+ " AND DOCR.ID_CADASTRANTE = PES.ID_PESSOA "
					+ " AND DOCR.DT_FECHAMENTO IS NULL "
					+ " AND EST.ID_ESTADO_DOC = 1 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT -1 ID, 'Em Trânsito' DESCR, 3, COUNT(1) C1, 0 C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_PESSOA_SIN PES, EX_MOVIMENTACAO MOVR "
					+ " WHERE "
					+ " ID_PESSOA_INICIAL = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND (MOVR.ID_SUBSCRITOR = PES.ID_PESSOA) "
					+ " AND EST.ID_ESTADO_DOC = 3 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT -3 ID, 'Em Trânsito Eletrônico' DESCR, 4, COUNT(1) C1, 0 C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_PESSOA_SIN PES, EX_MOVIMENTACAO MOVR, EX_DOCUMENTO DOC "
					+ " WHERE "
					+ " ID_PESSOA_INICIAL = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND (MOVR.ID_SUBSCRITOR = PES.ID_PESSOA) "
					+ " AND EST.ID_ESTADO_DOC = 3 "
					+ " AND DOC.FG_ELETRONICO = 'S' "
					+ " AND MOVR.ID_DOC = DOC.ID_DOC "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+
					// " -- lot " +
					" UNION "
					+ " SELECT EST.ID_ESTADO_DOC ID, EST.DESC_ESTADO_DOC DESCR,  EST.ORDEM_ESTADO_DOC ORDEM, 0 C1, COUNT(1) C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_LOTACAO_SIN LOT, EX_MOVIMENTACAO MOVR "
					+ " WHERE "
					+ " ID_LOTACAO_INI = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND MOVR.ID_LOTA_RESP = LOT.ID_LOTACAO "
					+ " AND EST.ID_ESTADO_DOC <> 9 AND EST.ID_ESTADO_DOC <> 10 AND EST.ID_ESTADO_DOC <> 11 AND EST.ID_ESTADO_DOC <> 6 AND EST.ID_ESTADO_DOC <> 8 AND EST.ID_ESTADO_DOC <> 12 AND EST.ID_ESTADO_DOC <> 13 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT EST.ID_ESTADO_DOC ID, EST.DESC_ESTADO_DOC DESCR,  EST.ORDEM_ESTADO_DOC ORDEM, 0 C1, COUNT(1) C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_LOTACAO_SIN LOT, EX_MOVIMENTACAO MOVR "
					+ " WHERE "
					+ " ID_LOTACAO_INI = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND MOVR.ID_ESTADO_DOC = 11 AND MOVR.ID_LOTA_CADASTRANTE = LOT.ID_LOTACAO "
					+ " AND EST.ID_ESTADO_DOC <> 9 AND EST.ID_ESTADO_DOC <> 10 AND EST.ID_ESTADO_DOC <> 11 AND EST.ID_ESTADO_DOC <> 6 AND EST.ID_ESTADO_DOC <> 8 AND EST.ID_ESTADO_DOC <> 12 AND EST.ID_ESTADO_DOC <> 13 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT EST.ID_ESTADO_DOC ID, EST.DESC_ESTADO_DOC DESCR,  EST.ORDEM_ESTADO_DOC ORDEM, 0 C1, COUNT(1) C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_LOTACAO_SIN LOT, EX_DOCUMENTO DOCR "
					+ " WHERE "
					+ " ID_LOTACAO_INI = ? "
					+ " AND DOCR.ID_LOTA_CADASTRANTE = LOT.ID_LOTACAO "
					+ " AND DOCR.DT_FECHAMENTO IS NULL "
					+ " AND EST.ID_ESTADO_DOC = 1 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT -1 ID, 'Em Trânsito' DESCR, 3, 0 C1, COUNT(1) C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_LOTACAO_SIN LOT, EX_MOVIMENTACAO MOVR "
					+ " WHERE "
					+ " ID_LOTACAO_INI = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND (MOVR.ID_LOTA_SUBSCRITOR = LOT.ID_LOTACAO) "
					+ " AND EST.ID_ESTADO_DOC = 3 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT -3 ID, 'Em Trânsito Eletrônico' DESCR, 4, 0 C1, COUNT(1) C2 "
					+ " FROM EX_ESTADO_DOC EST, DP_LOTACAO_SIN LOT, EX_MOVIMENTACAO MOVR, EX_DOCUMENTO DOC "
					+ " WHERE "
					+ " ID_LOTACAO_INI = ? "
					+ " AND MOVR.DT_FIM_MOV IS NULL "
					+ " AND NOT MOVR.NUM_VIA IS NULL "
					+ " AND NOT MOVR.NUM_VIA = 0 "
					+ " AND MOVR.ID_ESTADO_DOC =  EST.ID_ESTADO_DOC "
					+ " AND (MOVR.ID_LOTA_SUBSCRITOR = LOT.ID_LOTACAO) "
					+ " AND EST.ID_ESTADO_DOC = 3 "
					+ " AND DOC.FG_ELETRONICO = 'S' "
					+ " AND MOVR.ID_DOC = DOC.ID_DOC "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " " + " ) GROUP BY ID, DESCR, ORDEM ORDER BY ORDEM";

			final Connection conn = getSessao().connection();
			final PreparedStatement psBlob = conn.prepareStatement(s);
			psBlob.setLong(1, o.getIdPessoaIni());
			psBlob.setLong(2, o.getIdPessoaIni());
			psBlob.setLong(3, o.getIdPessoaIni());
			psBlob.setLong(4, o.getIdPessoaIni());
			psBlob.setLong(5, o.getIdPessoaIni());
			psBlob.setLong(6, lot.getIdLotacaoIni());
			psBlob.setLong(7, lot.getIdLotacaoIni());
			psBlob.setLong(8, lot.getIdLotacaoIni());
			psBlob.setLong(9, lot.getIdLotacaoIni());
			psBlob.setLong(10, lot.getIdLotacaoIni());

			final String j = psBlob.toString();

			final ResultSet rset = psBlob.executeQuery();

			final List result = new ArrayList<Object[]>();
			while (rset.next()) {
				final Object[] ao = new Object[4];
				ao[0] = rset.getObject(1);
				ao[1] = rset.getObject(2);
				ao[2] = rset.getObject(4);
				ao[3] = rset.getObject(5);
				result.add(ao);
			}

			return result;

			/*
			 * final Query query = getSessao().getNamedQuery(
			 * "consultarPorResponsavelSQL"); query.setLong("pessoa",
			 * o.getIdPessoaIni()); query.setLong("lotacao",
			 * o.getLotacao().getIdLotacaoIni());
			 * 
			 * return query.list();
			 */
		} catch (final NullPointerException e) {
			return null;
		}
	}

	/**
	 * Le da tabela o campo do tipo BLOB e converte para um array de bytes.
	 * método não usa as facilidade do HIBERNATE em virtude da ausencia de
	 * suporte para estes campos.
	 */
	// public ExModelo consultarConteudoBlob(final ExModelo modelo)
	// throws SQLException {
	// final StringBuilder cmd = new StringBuilder(
	// "SELECT CONTEUDO_BLOB_MOD FROM EX_MODELO " + "WHERE ID_MOD= ? ");
	// final Connection conn = getSessao().connection();
	// final PreparedStatement psBlob = conn.prepareStatement(cmd.toString());
	// psBlob.setLong(1, modelo.getIdMod());
	// final ResultSet rset = psBlob.executeQuery();
	// rset.next();
	// final Blob blob = rset.getBlob("CONTEUDO_BLOB_MOD");
	// if (blob != null) {
	// final byte[] ba = blob.getBytes(1, (int) blob.length());
	// modelo.setConteudoBlobMod2(ba);
	// }
	// return modelo;
	// }

	public List consultarPaginaInicial(DpPessoa pes, DpLotacao lot,
			Integer idTipoForma) {
		try {
			SQLQuery sql = (SQLQuery) getSessao().getNamedQuery(
					"consultarPaginaInicial");

			sql.setLong("idPessoaIni", pes.getIdPessoaIni());
			sql.setLong("idLotacaoIni", lot.getIdLotacaoIni());
			sql.setInteger("idTipoForma", idTipoForma);

			List result = sql.list();

			return result;

		} catch (final NullPointerException e) {
			return null;
		}
	}

	/**
	 * Grava na tabela Ex_Modelo o conteudo do array de byte como um blob. O
	 * método não usa as facilidade do HIBERNATE em virtude da ausencia de
	 * suporte para estes campos.
	 * 
	 * @param modelo
	 *            - O objeto da Classe contendo um array de bytes para gravar.
	 * @return O objeto após a gravação
	 * @throws SQLException
	 * @throws AplicacaoException
	 */
	// public void gravarConteudoBlob(final ExModelo modelo) throws
	// SQLException,
	// AplicacaoException {
	//
	// final StringBuilder upd = new StringBuilder("UPDATE  EX_MODELO "
	// + "SET CONTEUDO_BLOB_MOD = empty_blob() WHERE ID_MOD = ?");
	// final StringBuilder cmd = new StringBuilder(
	// "SELECT CONTEUDO_BLOB_MOD FROM EX_MODELO "
	// + "WHERE ID_MOD= ?  FOR UPDATE");
	// final Connection conn = getSessao().connection();
	// PreparedStatement psBlob = conn.prepareStatement(upd.toString());
	// psBlob.setLong(1, modelo.getIdMod());
	// final int i = psBlob.executeUpdate();
	// if (i < 1)
	// throw new AplicacaoException(
	// "Ocorreu ao inserir Blob em Documento " + "que não existe");
	// psBlob.close();
	// psBlob = conn.prepareStatement(cmd.toString());
	// psBlob.setLong(1, modelo.getIdMod());
	// final ResultSet rset = psBlob.executeQuery();
	// rset.next();
	// final Blob blob = rset.getBlob("CONTEUDO_BLOB_MOD");
	// final OutputStream blobOutputStream = blob.setBinaryStream(0);
	// try {
	// blobOutputStream.write(modelo.getConteudoBlobMod2());
	// blobOutputStream.close();
	// } catch (final IOException e) {
	// throw new AplicacaoException("Ocorreu um erro ao inserir o blob",
	// 0, e);
	// } finally {
	// psBlob.close();
	// }
	//
	// }

	public List<ExNivelAcesso> listarOrdemNivel() {
		Query query = getSessao().getNamedQuery("listarOrdemNivel");
		query.setCacheable(true);
		query.setCacheRegion("query.ExNivelAcesso");
		return query.list();
	}

	public void gravarPreenchimentoBlob(final ExPreenchimento exPreenchimento)
			throws SQLException, AplicacaoException {

		getSessao().flush();
		final Connection conn = getSessao().connection();

		if (exPreenchimento.getPreenchimentoBlob() == null) {
			final StringBuilder upd = new StringBuilder(
					"UPDATE  EX_PREENCHIMENTO "
							+ "SET PREENCHIMENTO_BLOB = NULL WHERE ID_PREENCHIMENTO = ?");
			final PreparedStatement psBlob = conn.prepareStatement(upd
					.toString());
			psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
			try {
				final int i = psBlob.executeUpdate();
				if (i < 1)
					throw new AplicacaoException("Ocorreu ao apagar Blob");
			} finally {
				psBlob.close();
			}
		} else {
			final StringBuilder upd = new StringBuilder(
					"UPDATE  EX_PREENCHIMENTO "
							+ "SET PREENCHIMENTO_BLOB = empty_blob() WHERE ID_PREENCHIMENTO = ?");
			final StringBuilder cmd = new StringBuilder(
					"SELECT PREENCHIMENTO_BLOB FROM ex_preenchimento "
							+ "WHERE ID_PREENCHIMENTO = ?  FOR UPDATE");
			PreparedStatement psBlob = conn.prepareStatement(upd.toString());
			psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
			final int i = psBlob.executeUpdate();
			if (i < 1)
				throw new AplicacaoException("Ocorreu ao inserir Blob");
			psBlob.close();
			psBlob = conn.prepareStatement(cmd.toString());
			psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
			final ResultSet rset = psBlob.executeQuery();
			boolean b = rset.next();
			final Blob blob = rset.getBlob("PREENCHIMENTO_BLOB");
			final OutputStream blobOutputStream = blob.setBinaryStream(0);
			try {
				blobOutputStream.write(exPreenchimento.getPreenchimentoBA());
				blobOutputStream.close();
			} catch (final IOException e) {
				throw new AplicacaoException(
						"Ocorreu um erro ao inserir o blob", 0, e);
			} finally {
				psBlob.close();
			}
		}
	}

	public ExPreenchimento consultarPreenchimentoBlob(
			ExPreenchimento exPreenchimento) throws SQLException {
		final StringBuilder cmd = new StringBuilder(
				"SELECT PREENCHIMENTO_BLOB FROM ex_preenchimento "
						+ "WHERE ID_PREENCHIMENTO= ? ");
		final Connection conn = getSessao().connection();
		final PreparedStatement psBlob = conn.prepareStatement(cmd.toString());
		psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
		final ResultSet rset = psBlob.executeQuery();
		rset.next();
		final Blob blob = rset.getBlob("PREENCHIMENTO_BLOB");
		if (blob != null) {
			final byte[] ba = blob.getBytes(1, (int) blob.length());
			exPreenchimento.setPreenchimentoBA(ba);
		} else {
			exPreenchimento.setPreenchimentoBlob(null);
		}
		return exPreenchimento;
	}

	public List<ExPreenchimento> consultar(ExPreenchimento exPreenchimento) {
		try {
			final Query query = getSessao().getNamedQuery(
					"consultarPorFiltroExPreenchimento");
			if (exPreenchimento.getNomePreenchimento() != null)
				query.setString("nomePreenchimento", exPreenchimento
						.getNomePreenchimento().toUpperCase().replace(' ', '%'));
			else
				query.setString("nomePreenchimento", "");
			if (exPreenchimento.getDpLotacao() != null)
				query.setLong("lotacao", exPreenchimento.getDpLotacao()
						.getIdLotacao());
			else
				query.setLong("lotacao", 0);
			if (exPreenchimento.getExModelo() != null)
				query.setLong("modelo", exPreenchimento.getExModelo()
						.getIdMod());
			else
				query.setLong("modelo", 0);
			final List<ExPreenchimento> l = query.list();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	/*
	 * public void excluirPorId(Long id) throws SQLException, CsisException {
	 * try { final Query query = getSessao().getNamedQuery(
	 * "excluirPorIdExPreenchimento"); query.setLong("id", id);
	 * query.executeUpdate(); } catch (final NullPointerException e) { throw e;
	 * } }
	 */

	public void alterar(ExPreenchimento exPreenchimento) throws SQLException,
			AplicacaoException {
		getSessao().flush();
		final Connection conn = getSessao().connection();
		final StringBuilder upd = new StringBuilder(
				"UPDATE  EX_PREENCHIMENTO "
						+ "SET PREENCHIMENTO_BLOB = empty_blob() WHERE ID_PREENCHIMENTO = ?");
		final StringBuilder cmd = new StringBuilder(
				"SELECT PREENCHIMENTO_BLOB FROM ex_preenchimento "
						+ "WHERE ID_PREENCHIMENTO = ?  FOR UPDATE");
		PreparedStatement psBlob = conn.prepareStatement(upd.toString());
		psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
		final int i = psBlob.executeUpdate();
		if (i < 1)
			throw new AplicacaoException("Ocorreu ao inserir Blob");
		psBlob.close();
		psBlob = conn.prepareStatement(cmd.toString());
		psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
		final ResultSet rset = psBlob.executeQuery();
		boolean b = rset.next();
		final Blob blob = rset.getBlob("PREENCHIMENTO_BLOB");
		final OutputStream blobOutputStream = blob.setBinaryStream(0);
		try {
			blobOutputStream.write(exPreenchimento.getPreenchimentoBA());
			blobOutputStream.close();
		} catch (final IOException e) {
			throw new AplicacaoException("Ocorreu um erro ao inserir o blob",
					0, e);
		} finally {
			psBlob.close();
		}

	}

	public List consultarAtivos() {
		final Query query = getSessao().getNamedQuery("consultarAtivos");
		return query.list();
	}

	public List<ExConfiguracao> consultar(final ExConfiguracao exemplo) {
		CpTipoConfiguracao tpConf = exemplo.getCpTipoConfiguracao();
		CpOrgaoUsuario orgao = exemplo.getOrgaoUsuario();
		
		StringBuffer sbf = new StringBuffer();

		sbf.append("select * from siga.ex_configuracao ex inner join corporativo.cp_configuracao cp on ex.id_configuracao_ex = cp.id_configuracao ");

		sbf.append("" + "where 1 = 1");

		if (tpConf != null && tpConf.getIdTpConfiguracao() != null && tpConf.getIdTpConfiguracao() != 0) {
			sbf.append(" and cp.id_tp_configuracao = ");
			sbf.append(exemplo.getCpTipoConfiguracao().getIdTpConfiguracao());
		} 
		
		if (orgao != null && orgao.getId() != null && orgao.getId() != 0) {
			sbf.append(" and (cp.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(" or cp.id_lotacao in (select id_lotacao from corporativo.dp_lotacao lot where lot.id_orgao_usu= ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_pessoa in (select id_pessoa from corporativo.dp_pessoa pes where pes.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_cargo in (select id_cargo from corporativo.dp_cargo cr where cr.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_funcao_confianca in (select id_funcao_confianca from corporativo.dp_funcao_confianca fc where fc.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");			
			sbf.append(" or (cp.id_orgao_usu is null and cp.id_lotacao is null and cp.id_pessoa is null and cp.id_cargo is null and cp.id_funcao_confianca is null");			
			sbf.append(")");		
			sbf.append(")");
			sbf.append("order by ex.id_configuracao_ex");			
			
		}		
		
		Query query = getSessao().createSQLQuery(sbf.toString()).addEntity(ExConfiguracao.class);
		
		query.setCacheable(true);
		query.setCacheRegion("query.ExConfiguracao");
		return query.list();

	}

	public List<ExClassificacao> listarAssuntosPrincipal() {
		final Query query = getSessao().getNamedQuery(
				"consultarAssuntosPrincipalExClassificacao");
		// query.setString("descricao", o.getDescricao());

		final List<ExClassificacao> l = query.list();
		/*
		 * if (l.size() == 0) return null;
		 */
		return l;
	}

	public List<ExClassificacao> listarAssuntos() {
		final Query query = getSessao().getNamedQuery(
				"consultarAssuntosExClassificacao");
		final List<ExClassificacao> l = query.list();
		return l;
	}

	public List<ExClassificacao> listarAssuntosSecundario(
			final int codAssuntoPrincipal) {
		final Query query = getSessao().getNamedQuery(
				"consultarAssuntosSecundarioExClassificacao");
		query.setInteger("assuntoPrincipal", codAssuntoPrincipal);

		final List<ExClassificacao> l = query.list();
		/*
		 * if (l.size() == 0) return null;
		 */
		return l;
	}

	public List<ExClassificacao> listarClassesAntigo(
			final int codAssuntoPrincipal, final int codAssuntoSecundario) {
		final Query query = getSessao().getNamedQuery(
				"consultarClassesExClassificacao");
		query.setInteger("assuntoPrincipal", codAssuntoPrincipal);
		query.setInteger("assuntoSecundario", codAssuntoSecundario);

		final List<ExClassificacao> l = query.list();
		/*
		 * if (l.size() == 0) return null;
		 */
		return l;
	}

	public List<ExClassificacao> listarClasses(final int codAssunto) {
		final Query query = getSessao().getNamedQuery(
				"consultarClassesExClassificacao");
		query.setInteger("assunto", codAssunto);
		final List<ExClassificacao> l = query.list();
		return l;
	}

	public List<ExClassificacao> listarSubClassesAntigo(
			final int codAssuntoPrincipal, final int codAssuntoSecundario,
			final int codClasse) {
		final Query query = getSessao().getNamedQuery(
				"consultarSubClassesExClassificacao");
		query.setInteger("assuntoPrincipal", codAssuntoPrincipal);
		query.setInteger("assuntoSecundario", codAssuntoSecundario);
		query.setInteger("classe", codClasse);

		final List<ExClassificacao> l = query.list();
		return l;
	}

	public List<ExClassificacao> listarSubClasses(final int codAssunto,
			final int codClasse) {
		final Query query = getSessao().getNamedQuery(
				"consultarSubClassesExClassificacao");
		query.setInteger("assunto", codAssunto);
		query.setInteger("classe", codClasse);
		final List<ExClassificacao> l = query.list();
		return l;
	}

	public List<ExClassificacao> listarAtividades(
			final int codAssuntoPrincipal, final int codAssuntoSecundario,
			final int codClasse, final int codSubClasse,
			final boolean ultimoNivel) {
		final Query query = getSessao().getNamedQuery(
				"consultarAtividadesExClassificacao");
		query.setInteger("assuntoPrincipal", codAssuntoPrincipal);
		query.setInteger("assuntoSecundario", codAssuntoSecundario);
		query.setInteger("classe", codClasse);
		query.setInteger("subclasse", codSubClasse);
		query.setBoolean("ultimoNivel", ultimoNivel);

		final List<ExClassificacao> l = query.list();
		/*
		 * if (l.size() == 0) return null;
		 */
		return l;
	}

	public List<ExClassificacao> consultarPorFiltro(
			final ExClassificacaoDaoFiltro flt) {
		return consultarPorFiltro(flt, 0, 0);
	}

	public List<ExClassificacao> consultarPorFiltro(
			final ExClassificacaoDaoFiltro flt, final int offset,
			final int itemPagina) {
//		int codAssuntoPrincipal = -1;
//		int codAssuntoSecundario = -1;
//		int codClasse = -1;
//		int codSubClasse = -1;
//		int codAtividade = -1;
//		int codAssunto = -1;
		boolean ultimoNivel = false;
		String descrClassificacao = "";
//		if (flt.getAssuntoPrincipal() != null) {
//			if (!flt.getAssuntoPrincipal().equals("")) {
//				codAssuntoPrincipal = new Integer(flt.getAssuntoPrincipal());
//			}
//		}
//		if (flt.getAssunto() != null) {
//			if (!flt.getAssunto().equals("")) {
//				codAssunto = new Integer(flt.getAssunto());
//			}
//		}
//
//		if (flt.getAssuntoSecundario() != null) {
//			if (!flt.getAssuntoSecundario().equals("")) {
//				codAssuntoSecundario = new Integer(flt.getAssuntoSecundario());
//			}
//		}
//		if (flt.getClasse() != null) {
//			if (!flt.getClasse().equals("")) {
//				codClasse = new Integer(flt.getClasse());
//			}
//		}
//		if (flt.getSubclasse() != null) {
//			if (!flt.getSubclasse().equals("")) {
//				codSubClasse = new Integer(flt.getSubclasse());
//			}
//		}
//		if (flt.getAtividade() != null) {
//			if (!flt.getAtividade().equals("")) {
//				codAtividade = new Integer(flt.getAtividade());
//			}
//		}
		if (flt.getDescricao() != null) {
			descrClassificacao = flt.getDescricao();
		}
		if (flt.getUltimoNivel() != null) {
			ultimoNivel = new Boolean(flt.getUltimoNivel());
		}

		final Query query = getSessao().getNamedQuery(
				"consultarPorFiltroExClassificacao");
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}

		query.setString("descrClassificacao", descrClassificacao.toUpperCase()
				.replace(' ', '%'));
		query.setBoolean("ultimoNivel", ultimoNivel);

		final List<ExClassificacao> l = query.list();
		/*
		 * if (l.size() == 0) return null;
		 */
		return l;
	}

	public int consultarQuantidade(final ExClassificacaoDaoFiltro flt) {
//		int codAssuntoPrincipal = -1;
//		int codAssuntoSecundario = -1;
//		int codClasse = -1;
//		int codSubClasse = -1;
//		int codAtividade = -1;
//		int codAssunto = -1;
		boolean ultimoNivel = false;
		String descrClassificacao = "";
//		if (flt.getAssuntoPrincipal() != null) {
//			if (!flt.getAssuntoPrincipal().equals("")) {
//				codAssuntoPrincipal = new Integer(flt.getAssuntoPrincipal());
//			}
//		}
//		if (flt.getAssunto() != null) {
//			if (!flt.getAssunto().equals("")) {
//				codAssunto = new Integer(flt.getAssunto());
//			}
//		}
//		if (flt.getAssuntoSecundario() != null) {
//			if (!flt.getAssuntoSecundario().equals("")) {
//				codAssuntoSecundario = new Integer(flt.getAssuntoSecundario());
//			}
//		}
//		if (flt.getClasse() != null) {
//			if (!flt.getClasse().equals("")) {
//				codClasse = new Integer(flt.getClasse());
//			}
//		}
//		if (flt.getSubclasse() != null) {
//			if (!flt.getSubclasse().equals("")) {
//				codSubClasse = new Integer(flt.getSubclasse());
//			}
//		}
//		if (flt.getAtividade() != null) {
//			if (!flt.getAtividade().equals("")) {
//				codAtividade = new Integer(flt.getAtividade());
//			}
//		}
		if (flt.getUltimoNivel() != null) {
			ultimoNivel = new Boolean(flt.getUltimoNivel());
		}
		if (flt.getDescricao() != null) {
			descrClassificacao = flt.getDescricao();
		}

		final Query query = getSessao().getNamedQuery(
				"consultarQuantidadeExClassificacao");

		query.setBoolean("ultimoNivel", ultimoNivel);
		query.setString("descrClassificacao", descrClassificacao.toUpperCase()
				.replace(' ', '%'));

		final int l = ((Long) query.uniqueResult()).intValue();
		/*
		 * if (l.size() == 0) return null;
		 */
		return l;
	}

	public ExClassificacao consultarPorSigla(final ExClassificacao o) {
		try {
			final Query query = getSessao().getNamedQuery(
					"consultarPorSiglaExClassificacao");
			query.setString("codificacao", o.getSigla());

			final List<ExClassificacao> l = query.list();
			if (l.size() != 1)
				return null;
			return l.get(0);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public ExClassificacao consultarAtual(final ExClassificacao o) {
		try {
			final Query query = getSessao()
					.getNamedQuery("consultarAtualPorId");
			query.setLong("idRegIni", o.getHisIdIni());
			return (ExClassificacao) query.uniqueResult();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public Selecionavel consultarPorSigla(final ExClassificacaoDaoFiltro flt) {
		final ExClassificacao o = new ExClassificacao();
		o.setSigla(flt.getSigla());
		return consultarPorSigla(o);
	}

	public List consultarMovimentacoesPorLotacaoEntreDatas(// DpPessoa titular,
			DpLotacao lotaTitular, Date dtIni, Date dtFim) {
		try {
			final Query query = getSessao().getNamedQuery(
					"consultarMovimentacoesPorLotacaoEntreDatas");
			// if (titular.getIdPessoaIni() != null)
			// query.setLong("titular", titular.getIdPessoaIni());
			// else
			// query.setLong("titular", 0);
			if (lotaTitular.getIdLotacaoIni() != null)
				query.setLong("lotaTitular", lotaTitular.getIdLotacaoIni());
			else
				query.setLong("lotaTitular", 0);
			// query.setDate("dtIni", dtIni);
			// query.setDate("dtFim", dtFim);

			return query.list();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public ExTpDocPublicacao consultarPorModelo(ExModelo mod) {
		try {
			final Query query = getSessao().getNamedQuery("consultarPorModelo");
			query.setLong("idMod", mod.getIdMod());

			return (ExTpDocPublicacao) query.list().get(0);
		} catch (final Throwable t) {
			// engolindo a exceção. Melhorar isso.
			return null;
		}
	}

	public List<ExDocumento> listarSolicitados(CpOrgaoUsuario orgaoUsu) {
		final Query query = getSessao().getNamedQuery("listarSolicitados");
		query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		return query.list();
	}

	public List<ExMobil> consultarParaArquivarEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaArquivarEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public List<ExMobil> consultarParaTransferirEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaTransferirEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public List<ExMobil> consultarParaAnotarEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaAnotarEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public ExFormaDocumento consultarPorSigla(ExFormaDocumento o) {
		final Query query = getSessao().getNamedQuery("consultarSiglaForma");
		query.setString("sigla", o.getSigla());

		query.setCacheable(true);
		query.setCacheRegion("query.ExFormaDocumento");

		final List<ExFormaDocumento> l = query.list();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public int obterProximoNumeroVolume(ExDocumento doc) {
		return doc.getNumUltimoVolume() + 1;
	}

	public int obterProximoNumeroSubdocumento(ExMobil mob) {
		return mob.getNumUltimoSubdocumento() + 1;
	}

	public List<ExMobil> consultarParaReceberEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaReceberEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public List<ExMobil> consultarParaViaDeProtocolo(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaViaDeProtocolo");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public List<ExMovimentacao> consultarMovimentacoes(DpPessoa pes, Date dt) {

		if (pes == null || dt == null) {
			log.error("[consultarMovimentacoes] - Os dados recebidos para realizar a consulta de movimentações não podem ser nulos.");
			throw new IllegalStateException(
					"A pessoa e/ou a data informada para a realização da consulta é nula.");
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		final Query query = getSessao().getNamedQuery("consultarMovimentacoes");
		ExMovimentacao mov = consultar(1122650L, ExMovimentacao.class, false);

		query.setLong("pessoaIni", pes.getIdPessoaIni());
		// query.setDate("data", dt);
		query.setString("data", df.format(dt));
		return query.list();
	}

	public Date getServerDateTime() {
		return null;
	}

	public List<ExModelo> listarTodosModelosOrdenarPorNome(String script)
			throws Exception {
		final Query q = getSessao().createQuery(
				"select m from ExModelo m left join m.exFormaDocumento as f "
						+ "order by f.descrFormaDoc, m.nmMod");
		List<ExModelo> l = new ArrayList<ExModelo>();
		for (ExModelo mod : (List<ExModelo>) q.list())
			if (script != null && script.trim().length() != 0) {
				if ("template/freemarker".equals(mod.getConteudoTpBlob())
						&& mod.getConteudoBlobMod2() != null
						&& (new String(mod.getConteudoBlobMod2(), "utf-8"))
								.contains(script))
					l.add(mod);
			} else
				l.add(mod);
		return l;
	}

	static public AnnotationConfiguration criarHibernateCfg(String datasource)
			throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(datasource);
		ExDao.configurarHibernate(cfg);
		return cfg;
	}

	static public AnnotationConfiguration criarHibernateCfg(
			String connectionUrl, String username, String password)
			throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(connectionUrl,
				username, password);
		ExDao.configurarHibernate(cfg);
		return cfg;
	}

	static private void configurarHibernate(AnnotationConfiguration cfg)
			throws Exception {
		// cfg.addResource("br/gov/jfrj/siga/ex/ExMobil.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExDocumento.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExFormaDocumento.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExConfiguracao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExSituacaoConfiguracao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExClassificacao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExModelo.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTemporalidade.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTipoDespacho.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTipoDestinacao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTipoDocumento.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExNivelAcesso.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExEstadoDoc.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExPreenchimento.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTipoFormaDoc.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTipoMovimentacao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExVia.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExMovimentacao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTpDocPublicacao.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExTipoMobil.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExBoletimDoc.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExPapel.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/ex/ExEmailNotificacao.hbm.xml");
		//
		// cfg.addResource("br/gov/jfrj/siga/dp/CpTipoMarcador.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/dp/CpMarcador.hbm.xml");
		// cfg.addResource("br/gov/jfrj/siga/dp/CpTipoMarca.hbm.xml");
		// <mapping resource="br/gov/jfrj/siga/dp/CpMarca.hbm.xml" />

		cfg.addClass(br.gov.jfrj.siga.ex.ExMobil.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExDocumento.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExFormaDocumento.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExConfiguracao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExSituacaoConfiguracao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExClassificacao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExModelo.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTemporalidade.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTipoDespacho.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTipoDestinacao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTipoDocumento.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExNivelAcesso.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExEstadoDoc.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExPreenchimento.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTipoFormaDoc.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTipoMovimentacao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExVia.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExMovimentacao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTpDocPublicacao.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExTipoMobil.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExBoletimDoc.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExPapel.class);
		cfg.addClass(br.gov.jfrj.siga.ex.ExEmailNotificacao.class);
		cfg.addClass(br.gov.jfrj.siga.dp.CpTipoMarcador.class);
		cfg.addClass(br.gov.jfrj.siga.dp.CpMarcador.class);
		cfg.addClass(br.gov.jfrj.siga.dp.CpTipoMarca.class);

		cfg.addAnnotatedClass(br.gov.jfrj.siga.ex.ExMarca.class);
		cfg.addAnnotatedClass(br.gov.jfrj.siga.dp.CpMarca.class);

		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExClassificacao",
				"read-only", "ex");
		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExConfiguracao",
		// "read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExEstadoDoc",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExFormaDocumento",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExNivelAcesso",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExSituacaoConfiguracao", "read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTemporalidade",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoDespacho",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoDestinacao",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoDocumento",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExTpDocPublicacao", "read-only", "ex");
		cfg.setCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExTipoMovimentacao", "read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoFormaDoc",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExVia",
				"read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoMobil",
				"read-only", "ex");
		cfg.setCollectionCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExTipoDocumento.exFormaDocumentoSet",
				"read-only", "ex");
		cfg.setCollectionCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExClassificacao.exViaSet", "read-only",
				"ex");
		// cfg.setCollectionCacheConcurrencyStrategy(
		// "br.gov.jfrj.siga.ex.ExFormaDocumento.exModeloSet",
		
		// "nonstrict-read-write", "ex");
		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExModelo",
		// "nonstrict-read-write", "ex");

		// Hibernate search configuration
		//
		if ("true".equals(SigaExProperties.getString("siga.lucene.ativo"))) {
			cfg.setProperty("hibernate.search.default.directory_provider",
					"org.hibernate.search.store.FSDirectoryProvider");
			cfg.setProperty("hibernate.search.default.indexBase",
					SigaExProperties.getString("siga.lucene.index.path")
							+ "/siga-ex-lucene-index/");
			cfg.setProperty(
					"hibernate.search.default.optimizer.operation_limit.max",
					"2000");
			cfg.setProperty("org.hibernate.worker.execution", "sync");
			cfg.setProperty("org.hibernate.worker.batch_size", "1000");
			cfg.setProperty("hibernate.search.indexing_strategy", "manual");
			cfg.getEventListeners()
					.setPostUpdateEventListeners(
							new PostUpdateEventListener[] { (PostUpdateEventListener) ReflectHelper
									.classForName(
											"org.hibernate.search.event.FullTextIndexEventListener")
									.newInstance() });
			cfg.getEventListeners()
					.setPostInsertEventListeners(
							new PostInsertEventListener[] { (PostInsertEventListener) ReflectHelper
									.classForName(
											"org.hibernate.search.event.FullTextIndexEventListener")
									.newInstance() });
			cfg.getEventListeners()
					.setPostDeleteEventListeners(
							new PostDeleteEventListener[] { (PostDeleteEventListener) ReflectHelper
									.classForName(
											"org.hibernate.search.event.FullTextIndexEventListener")
									.newInstance() });
		} else {
			cfg.setProperty("hibernate.search.autoregister_listeners", "false");
		}

	}

	public ExModelo consultarExModelo(String sForma, String sModelo) {
		final Criteria crit = getSessao().createCriteria(ExModelo.class);
		crit.add(Restrictions.eq("nmMod", sModelo));
		if (sForma != null) {
			crit.createAlias("exFormaDocumento", "f");
			crit.add(Restrictions.eq("f.descrFormaDoc", sForma));
		}
		return (ExModelo) crit.uniqueResult();
	}

	public List<ExDocumento> listarDocPendenteAssinatura(DpPessoa pessoa) {
		final Query query = getSessao().getNamedQuery(
				"listarDocPendenteAssinatura");
		query.setLong("idPessoaIni", pessoa.getIdPessoaIni());
		return query.list();
	}
}
