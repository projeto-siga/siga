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
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.cp.bl.CpPropriedadeBL;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExBoletimDoc;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExEmailNotificacao;
import br.gov.jfrj.siga.ex.ExEstadoDoc;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExItemDestinacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ext.IExMobilDaoFiltro;
import br.gov.jfrj.siga.hibernate.ext.IMontadorQuery;
import br.gov.jfrj.siga.hibernate.ext.MontadorQuery;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.Aguarde;
import br.gov.jfrj.siga.persistencia.ExClassificacaoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExDocumentoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@SuppressWarnings({"unchecked","rawtypes"})
public class ExDao extends CpDao {

	public static final String CACHE_EX = "ex";

	private static final Logger log = Logger.getLogger(ExDao.class);

	IMontadorQuery montadorQuery = null;

	public static ExDao getInstance() {
		return ModeloDao.getInstance(ExDao.class);
	}

	public ExDao() {
		try {
			montadorQuery = (IMontadorQuery) Class.forName(SigaExProperties.getMontadorQuery()).newInstance();
			montadorQuery.setMontadorPrincipal(new MontadorQuery());
		} catch (Exception e) {
			montadorQuery = new MontadorQuery();
		}
	}

	public void reindexarVarios(List<ExDocumento> docs, boolean apenasExcluir) throws Exception {
		FullTextSession fullTextSession = Search.getFullTextSession(getSessao());
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
		query.setLong("lotaTitular", lotaTitular.getIdLotacaoIni() != null ? lotaTitular.getIdLotacaoIni() : 0);
		List l = query.list();
		return l;
	}



	public void preencherParametros(final IExMobilDaoFiltro flt,
			final Query query) {
		if (flt.getUltMovIdEstadoDoc() != null
				&& flt.getUltMovIdEstadoDoc() != 0) {


			query.setLong("ultMovIdEstadoDoc", flt.getUltMovIdEstadoDoc());

		}

		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {


			query.setLong("ultMovRespSelId", flt.getUltMovRespSelId());
		}

		if (flt.getUltMovLotaRespSelId() != null
				&& flt.getUltMovLotaRespSelId() != 0) {


			query.setLong("ultMovLotaRespSelId", flt.getUltMovLotaRespSelId());
		}

		if (flt.getIdTipoMobil() != null && flt.getIdTipoMobil() != 0) {

			query.setLong("idTipoMobil", flt.getIdTipoMobil());
		}

		if (flt.getNumSequencia() != null && flt.getNumSequencia() != 0) {

			query.setLong("numSequencia", flt.getNumSequencia());
		}

		if (flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0) {

			query.setLong("idOrgaoUsu", flt.getIdOrgaoUsu());
		}

		if (flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0) {
			query.setLong("anoEmissao", flt.getAnoEmissao());
		}

		if (flt.getNumExpediente() != null && flt.getNumExpediente() != 0) {
			query.setLong("numExpediente", flt.getNumExpediente());
		}

		if (flt.getIdTpDoc() != null && flt.getIdTpDoc() != 0) {
			query.setLong("idTpDoc", flt.getIdTpDoc());
		}

		if (flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0) {
			query.setLong("idFormaDoc", flt.getIdFormaDoc());
		}

		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			query.setLong("idTipoFormaDoc", flt.getIdTipoFormaDoc());
		}

		if (flt.getClassificacaoSelId() != null
				&& flt.getClassificacaoSelId() != 0) {
			query.setLong("classificacaoSelId", flt.getClassificacaoSelId());
		}

		if (flt.getDescrDocumento() != null

				&& !flt.getDescrDocumento().trim().equals("")) {
			query.setString("descrDocumento", "%"
					+ flt.getDescrDocumento().toUpperCase() + "%");
		}


		if (flt.getDtDoc() != null) {
			query.setString("dtDoc",
					new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(flt.getDtDoc()));
		}


		if (flt.getDtDocFinal() != null) {
			query.setString("dtDocFinal", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
			.format(flt.getDtDocFinal()));
		}

		if (flt.getNumAntigoDoc() != null

				&& !flt.getNumAntigoDoc().trim().equals("")) {
			query.setString("numAntigoDoc", "%"
					+ flt.getNumAntigoDoc().toUpperCase() + "%");
		}

		if (flt.getDestinatarioSelId() != null
				&& flt.getDestinatarioSelId() != 0) {
			query.setLong("destinatarioSelId", flt.getDestinatarioSelId());
		}

		if (flt.getLotacaoDestinatarioSelId() != null
				&& flt.getLotacaoDestinatarioSelId() != 0) {
			query.setLong("lotacaoDestinatarioSelId",
					flt.getLotacaoDestinatarioSelId());
		}

		if (flt.getNmDestinatario() != null
				&& !flt.getNmDestinatario().trim().equals("")) {
			query.setString("nmDestinatario", "%" + flt.getNmDestinatario()
					+ "%");
		}

		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			query.setLong("cadastranteSelId", flt.getCadastranteSelId());
		}

		if (flt.getLotaCadastranteSelId() != null
				&& flt.getLotaCadastranteSelId() != 0) {
			query.setLong("lotaCadastranteSelId", flt.getLotaCadastranteSelId());
		}

		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			query.setLong("subscritorSelId", flt.getSubscritorSelId());
		}

		if (flt.getNmSubscritorExt() != null

				&& !flt.getNmSubscritorExt().trim().equals("")) {
			query.setString("nmSubscritorExt", "%"
					+ flt.getNmSubscritorExt().toUpperCase() + "%");
		}

		if (flt.getOrgaoExternoSelId() != null
				&& flt.getOrgaoExternoSelId() != 0) {
			query.setLong("orgaoExternoSelId", flt.getOrgaoExternoSelId());
		}

		if (flt.getNumExtDoc() != null && !flt.getNumExtDoc().trim().equals("")) {
			query.setString("numExtDoc", "%" + flt.getNumExtDoc() + "%");
		}

		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			ExModelo mod = ExDao.getInstance().consultar(flt.getIdMod(),
					ExModelo.class, false);
			query.setLong("hisIdIni", mod.getHisIdIni());
		}
	}

	public List consultarPorFiltroOtimizado(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina, DpPessoa titular,
			DpLotacao lotaTitular) {
		long tempoIni = System.nanoTime();
		Query query = getSessao().createQuery(montadorQuery.montaQueryConsultaporFiltro(flt, titular, lotaTitular, false));
		preencherParametros(flt, query);

		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}
		List l = query.list();
		long tempoTotal = System.nanoTime() - tempoIni;
		System.out.println("consultarPorFiltroOtimizado: " + tempoTotal/1000000 + " ms -> " + query + ", resultado: " + l);
		return l;
	}

	public Integer consultarQuantidadePorFiltroOtimizado(
			final ExMobilDaoFiltro flt, DpPessoa titular, DpLotacao lotaTitular) {
		long tempoIni = System.nanoTime();
		String s = montadorQuery.montaQueryConsultaporFiltro(flt, titular,
				lotaTitular, true);
		Query query = getSessao().createQuery(s);


		preencherParametros(flt, query);


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

	public List<ExFormaDocumento> listarTodosOrdenarPorSigla() {
		final Criteria crit = getSessao()
				.createCriteria(ExFormaDocumento.class);
		crit.addOrder(Order.asc("siglaFormaDoc"));
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

		// Substitui n espaï¿½os brancos seguidos de letra ou nï¿½mero por " +"
		// As duas linhas abaixo poderï¿½o ser substituï¿½das por uma
		// query = "+"+query.replaceAll("(\\s+)", " +");
		// query = query.replaceAll("\\+\\-", "-");

		QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36, new String[] {
				"conteudoBlobDocHtml", "nmMod", "descrDocumento",
				"exMovimentacaoIndexacaoSet.descrMov",
				"exMovimentacaoIndexacaoSet.descrTipoMovimentacao",
				"exMovimentacaoIndexacaoSet.conteudoBlobMovHtml",
		"exMovimentacaoIndexacaoSet.conteudoBlobMovPdf" },
		new BrazilianAnalyzer(Version.LUCENE_36));

		parser.setDefaultOperator(QueryParser.AND_OPERATOR);

		org.apache.lucene.search.Query luceneQuery = parser.parse(query);

		// ------------------------------- hibernate search query:

		FullTextSession fullTextSession = Search
				.getFullTextSession(getSessao());

		FullTextQuery hbQuery = fullTextSession.createFullTextQuery(luceneQuery, ExDocumento.class);

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
		FullTextSession fullTextSession = Search.getFullTextSession(getSessao());
		QueryParser parser = new QueryParser(Version.LUCENE_36, "idDoc", new StandardAnalyzer(Version.LUCENE_36));

		final Criteria crit = getSessao().createCriteria(ExDocumento.class).add(Restrictions.gt("idDoc", new Long(aPartir)));
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
			System.out.println("listarNaoIndexados - " + firstResult+ " varridos");
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
					.getFullTextSession(getSessao());
			Transaction deleteTx = fullTextSession.beginTransaction();
			fullTextSession.purgeAll(ExDocumento.class);
			deleteTx.commit();
			fullTextSession.clear();
			getSessao().clear();
		} catch (Throwable t) {
			// Nï¿½o havia documentos a excluir
		}

		final Criteria crit = getSessao().createCriteria(ExDocumento.class);

		int index = 0;

		FullTextSession fullTextSession = Search
				.getFullTextSession(getSessao());
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
				// + " documentos jï¿½ indexados.");
				if (doc.isIndexavel())
					fullTextSession.index(doc);

				if (index % 100 == 0) {
					//System.gc();
					// fullTextSession.flush();
					// fullTextSession.clear();
				}
			}
			tx.commit();
			fullTextSession.clear();
			getSessao().clear();
			System.out.print(String.valueOf(index)
					+ " documentos jï¿½ indexados. --  -- ");
		} while (list.size() > 0);
		//System.gc();

		// fullTextSession.close();
		System.out.println("Duraï¿½ï¿½o da indexaï¿½ï¿½o de documentos: "
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
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		/*
		 * Query indexQuery = getSessao() .createQuery( "from ExDocumento as doc
		 * inner join doc.exMovimentacaoSet as mov where mov.dtIniMov >=
		 * :dtIni"); indexQuery.setParameter("dtIni", dtIni);
		 */

		FullTextSession fullTextSession = Search
				.getFullTextSession(getSessao());
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
		//System.gc();

		// fullTextSession.close();
		System.out.println("Duraï¿½ï¿½o da indexaï¿½ï¿½o de documentos: "
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

	public List<ExEmailNotificacao> consultarEmailNotificacao(DpPessoa pess, DpLotacao lot) {
		final Query query;

		if(pess != null){
			query = getSessao().getNamedQuery("consultarEmailporPessoa");
			query.setLong("idPessoaIni", pess.getIdPessoaIni());
		} else {
			query = getSessao().getNamedQuery("consultarEmailporLotacao");


			query.setLong("idLotacaoIni", lot.getIdLotacaoIni());
		}		

		return query.list();
	}

	public void indexar(ExDocumento entidade) {
		FullTextSession fullTextSession = Search
				.getFullTextSession(getSessao());
		Transaction tx = fullTextSession.beginTransaction();
		fullTextSession.index(entidade);
		tx.commit();
	}

	public void desindexar(ExDocumento entidade) {
		FullTextSession fullTextSession = Search
				.getFullTextSession(getSessao());
		try {
			Transaction tx = fullTextSession.beginTransaction();
			fullTextSession.purge(ExDocumento.class, entidade.getIdDoc());
			tx.commit();
		} catch (Throwable t) {
			// Nï¿½o havia aquela movimentaï¿½ï¿½o no ï¿½ndice
		}
	}

	public List consultarPorResponsavel(final DpPessoa o, final DpLotacao lot)
			throws SQLException {
		try {
			final String s = " SELECT ID, DESCR, ORDEM, SUM(C1) C1, SUM(C2) C2 FROM ( "
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
					+ " AND DOCR.DT_FINALIZACAO IS NULL "
					+ " AND EST.ID_ESTADO_DOC = 1 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT -1 ID, 'Em Trï¿½nsito' DESCR, 3, COUNT(1) C1, 0 C2 "
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
					+ " SELECT -3 ID, 'Em Trï¿½nsito Eletrï¿½nico' DESCR, 4, COUNT(1) C1, 0 C2 "
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
					+ " AND DOCR.DT_FINALIZACAO IS NULL "
					+ " AND EST.ID_ESTADO_DOC = 1 "
					+ " GROUP BY EST.ID_ESTADO_DOC, EST.DESC_ESTADO_DOC,  EST.ORDEM_ESTADO_DOC "
					+ " "
					+ " UNION "
					+ " SELECT -1 ID, 'Em Trï¿½nsito' DESCR, 3, 0 C1, COUNT(1) C2 "
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
					+ " SELECT -3 ID, 'Em Trï¿½nsito Eletrï¿½nico' DESCR, 4, 0 C1, COUNT(1) C2 "
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

			final List result = new ArrayList<Object[]>();
			getSessao().doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					PreparedStatement psBlob = conn.prepareStatement(s);
					ResultSet rset = null;

					try {
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
						rset = psBlob.executeQuery();

						while (rset.next()) {
							final Object[] ao = new Object[4];
							ao[0] = rset.getObject(1);
							ao[1] = rset.getObject(2);
							ao[2] = rset.getObject(4);
							ao[3] = rset.getObject(5);
							result.add(ao);
						}
					}catch (Exception e){
						e.printStackTrace();
					}finally {
						if (!psBlob.isClosed())
							psBlob.close();
						if (!rset.isClosed())
							rset.close();
					}
				}
			});

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
	 * mï¿½todo nï¿½o usa as facilidade do HIBERNATE em virtude da ausencia de
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
	 * mï¿½todo nï¿½o usa as facilidade do HIBERNATE em virtude da ausencia de
	 * suporte para estes campos.
	 * 
	 * @param modelo
	 *            - O objeto da Classe contendo um array de bytes para gravar.
	 * @return O objeto apï¿½s a gravaï¿½ï¿½o
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
	// "Ocorreu ao inserir Blob em Documento " + "que nï¿½o existe");
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
		getSessao().doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				if (exPreenchimento.getPreenchimentoBlob() == null) {
					final StringBuilder upd = new StringBuilder(
							"UPDATE  EX_PREENCHIMENTO "
									+ "SET PREENCHIMENTO_BLOB = NULL WHERE ID_PREENCHIMENTO = ?");
					final PreparedStatement psBlob = conn.prepareStatement(upd.toString());
					psBlob.setLong(1, exPreenchimento.getIdPreenchimento());
					try {
						final int i = psBlob.executeUpdate();
						if (i < 1)
							throw new AplicacaoException("Ocorreu ao apagar Blob");
					} finally {
						psBlob.close();
					}
				} else {
					final StringBuilder upd = new StringBuilder("UPDATE  EX_PREENCHIMENTO SET PREENCHIMENTO_BLOB = empty_blob() WHERE ID_PREENCHIMENTO = ?");
					final StringBuilder cmd = new StringBuilder("SELECT PREENCHIMENTO_BLOB FROM ex_preenchimento WHERE ID_PREENCHIMENTO = ?  FOR UPDATE");
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
		});


	}

	public ExPreenchimento consultarPreenchimentoBlob(final ExPreenchimento exPreenchimento) throws SQLException {
		final StringBuilder cmd = new StringBuilder("SELECT PREENCHIMENTO_BLOB FROM ex_preenchimento WHERE ID_PREENCHIMENTO= ? ");
		getSessao().doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
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
			}
		});


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
						.getHisIdIni());
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

	public void alterar(final ExPreenchimento exPreenchimento) throws SQLException,AplicacaoException {
		getSessao().flush();
		getSessao().doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
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
		});

	}

	public List consultarAtivos() {
		final Query query = getSessao().getNamedQuery("consultarAtivos");
		return query.list();
	}

	public List<ExConfiguracao> consultar(final ExConfiguracao exemplo) {
		CpTipoConfiguracao tpConf = exemplo.getCpTipoConfiguracao();
		CpOrgaoUsuario orgao = exemplo.getOrgaoUsuario();

		StringBuffer sbf = new StringBuffer();

		sbf.append("select * from siga.ex_configuracao ex inner join " + "CORPORATIVO" + ".cp_configuracao cp on ex.id_configuracao_ex = cp.id_configuracao ");

		sbf.append("" + "where 1 = 1");

		if (tpConf != null && tpConf.getIdTpConfiguracao() != null
				&& tpConf.getIdTpConfiguracao() != 0) {
			sbf.append(" and cp.id_tp_configuracao = ");
			sbf.append(exemplo.getCpTipoConfiguracao().getIdTpConfiguracao());
		}

		if (exemplo.getExTipoMovimentacao() != null && exemplo.getExTipoMovimentacao().getIdTpMov() != null
				&& exemplo.getExTipoMovimentacao().getIdTpMov() != 0) {
			sbf.append(" and ex.id_tp_mov = ");
			sbf.append(exemplo.getExTipoMovimentacao().getIdTpMov());
		}

		if (exemplo.getExFormaDocumento() != null && exemplo.getExFormaDocumento().getIdFormaDoc() != null) {
			sbf.append(" and (ex.id_forma_doc = ");
			sbf.append(exemplo.getExFormaDocumento().getIdFormaDoc());
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc is null and ex.id_tp_forma_doc is null)");
			sbf.append(" or (ex.id_forma_doc is null and ex.id_tp_forma_doc = ");
			sbf.append(exemplo.getExFormaDocumento().getExTipoFormaDoc().getId());
			sbf.append(" ))");
		}

		if (exemplo.getExModelo() != null && exemplo.getExModelo().getIdMod() != null
				&& exemplo.getExModelo().getIdMod() != 0) {
			sbf.append(" and (ex.id_mod = ");
			sbf.append(exemplo.getExModelo().getIdMod());
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc is null and ex.id_tp_forma_doc is null)");
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc = ");
			sbf.append(exemplo.getExModelo().getExFormaDocumento().getId());
			sbf.append(")");
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc is null and ex.id_tp_forma_doc = ");
			sbf.append(exemplo.getExModelo().getExFormaDocumento().getExTipoFormaDoc().getId());
			sbf.append(" ))");
		}

		if (orgao != null && orgao.getId() != null && orgao.getId() != 0) {
			sbf.append(" and (cp.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(" or cp.id_lotacao in (select id_lotacao from " + "CORPORATIVO" + ".dp_lotacao lot where lot.id_orgao_usu= ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_pessoa in (select id_pessoa from " + "CORPORATIVO" + ".dp_pessoa pes where pes.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_cargo in (select id_cargo from " + "CORPORATIVO" + ".dp_cargo cr where cr.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_funcao_confianca in (select id_funcao_confianca from " + "CORPORATIVO" + ".dp_funcao_confianca fc where fc.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or (cp.id_orgao_usu is null and cp.id_lotacao is null and cp.id_pessoa is null and cp.id_cargo is null and cp.id_funcao_confianca is null");
			sbf.append(")");
			sbf.append(")");
			sbf.append("order by ex.id_configuracao_ex");

		}

		Query query = getSessao().createSQLQuery(sbf.toString()).addEntity(
				ExConfiguracao.class);

		query.setCacheable(true);
		query.setCacheRegion("query.ExConfiguracao");
		return query.list();

	}

	public List<ExClassificacao> listarExClassificacaoPorNivel(String mascara,
			String exceto) {
		Query q = getSessao().getNamedQuery(
				"consultarExClassificacaoComExcecao");
		q.setString("mascara", mascara);
		q.setString("exceto", exceto);
		return q.list();

	}

	public List<ExClassificacao> listarExClassificacaoPorNivel(String mascara) {
		Query q = getSessao().getNamedQuery(
				"consultarExClassificacaoPorMascara");
		q.setString("mascara", mascara);
		q.setString("descrClassificacao", "");
		return q.list();

	}

	public List<ExClassificacao> consultarPorFiltro(
			final ExClassificacaoDaoFiltro flt) {
		return consultarPorFiltro(flt, 0, 0);
	}

	public List<ExClassificacao> consultarPorFiltro(
			final ExClassificacaoDaoFiltro flt, final int offset,
			final int itemPagina) {
		String descrClassificacao = "";

		MascaraUtil m = MascaraUtil.getInstance();

		if (flt.getDescricao() != null) {
			String d = flt.getDescricao();
			if (d.length() > 0 && m.isCodificacao(d)) {
				descrClassificacao = m.formatar(d);
			} else {
				descrClassificacao = d;
			}
		}

		final Query query = getSessao().getNamedQuery(
				"consultarPorFiltroExClassificacao");
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}

		if (flt.getSigla() == null || flt.getSigla().equals("")) {
			query.setString("mascara", MascaraUtil.getInstance()
					.getMscTodosDoMaiorNivel());
		} else {
			query.setString(
					"mascara",
					MascaraUtil.getInstance().getMscFilho(
							flt.getSigla().toString(), true));
		}

		query.setString("descrClassificacao", descrClassificacao.toUpperCase()
				.replace(' ', '%'));
		query.setString("descrClassificacaoSemAcento", Texto
				.removeAcentoMaiusculas(descrClassificacao).replace(' ', '%'));

		final List<ExClassificacao> l = query.list();
		return l;
	}

	public int consultarQuantidade(final ExClassificacaoDaoFiltro flt) {
		String descrClassificacao = "";

		MascaraUtil m = MascaraUtil.getInstance();

		if (flt.getDescricao() != null) {
			String d = flt.getDescricao();
			if (d.length() > 0 && m.isCodificacao(d)) {
				descrClassificacao = m.formatar(d);
			} else {
				descrClassificacao = d;
			}
		}

		final Query query = getSessao().getNamedQuery(
				"consultarQuantidadeExClassificacao");

		if (flt.getSigla() == null || flt.getSigla().equals("")) {
			query.setString("mascara", MascaraUtil.getInstance()
					.getMscTodosDoMaiorNivel());
		} else {
			query.setString(
					"mascara",
					MascaraUtil.getInstance().getMscFilho(
							flt.getSigla().toString(), true));
		}

		query.setString("descrClassificacao", descrClassificacao.toUpperCase()
				.replace(' ', '%'));
		query.setString("descrClassificacaoSemAcento", Texto
				.removeAcentoMaiusculas(descrClassificacao).replace(' ', '%'));

		final int l = ((Long) query.uniqueResult()).intValue();
		return l;		
	}

	public ExClassificacao consultarPorSigla(final ExClassificacao o) {
		try {
			final Query query = getSessao().getNamedQuery(
					"consultarPorSiglaExClassificacao");
			query.setString("codificacao",
					MascaraUtil.getInstance().formatar(o.getSigla()));

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
			if (lotaTitular.getIdLotacaoIni() != null)
				query.setLong("lotaTitular", lotaTitular.getIdLotacaoIni());
			else
				query.setLong("lotaTitular", 0);

			return query.list();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public ExTpDocPublicacao consultarPorModelo(ExModelo mod) {
		try {
			final Query query = getSessao().getNamedQuery("consultarPorModelo");
			query.setLong("idMod", mod.getHisIdIni());

			return (ExTpDocPublicacao) query.list().get(0);
		} catch (final Throwable t) {
			// engolindo a exceï¿½ï¿½o. Melhorar isso.
			return null;
		}
	}

	public List<ExDocumento> listarSolicitados(CpOrgaoUsuario orgaoUsu) {
		final Query query = getSessao().getNamedQuery("listarSolicitados");
		query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		return query.list();
	}

	public List<ExMobil> consultarParaArquivarCorrenteEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(

				"consultarParaArquivarCorrenteEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public List<ExItemDestinacao> consultarParaArquivarIntermediarioEmLote(
			DpLotacao lot, int offset) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaArquivarIntermediarioEmLote");
		query.setLong("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		query.setFirstResult(offset);
		query.setMaxResults(100);
		List<Object[]> results = query.list();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		return listaFinal;
	}

	public int consultarQuantidadeParaArquivarIntermediarioEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarQuantidadeParaArquivarIntermediarioEmLote");
		query.setLong("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		return ((Long) query.uniqueResult()).intValue();
	}

	public List<ExItemDestinacao> consultarParaArquivarPermanenteEmLote(
			DpLotacao lot, int offset) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaArquivarPermanenteEmLote");
		query.setLong("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		query.setFirstResult(offset);
		query.setMaxResults(100);
		List<Object[]> results = query.list();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		return listaFinal;
	}

	public int consultarQuantidadeParaArquivarPermanenteEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarQuantidadeParaArquivarPermanenteEmLote");
		query.setLong("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		return ((Long) query.uniqueResult()).intValue();
	}

	//public Query consultarParaTransferirEmLote(DpLotacao lot) {
	@SuppressWarnings("unchecked")
	public Iterator<ExMobil> consultarParaTransferirEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaTransferirEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		//return query;
		return query.iterate();
	}

	public List<ExMobil> consultarParaAnotarEmLote(DpLotacao lot) {
		final Query query = getSessao().getNamedQuery(
				"consultarParaAnotarEmLote");
		query.setLong("lotaIni", lot.getIdLotacaoIni());
		return query.list();
	}

	public List<ExItemDestinacao> consultarAEliminar(CpOrgaoUsuario orgaoUsu,
			Date dtIni, Date dtFim) {
		final Query query = getSessao().getNamedQuery("consultarAEliminar");
		query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		query.setDate("dtIni", dtIni);
		query.setDate("dtFim", dtFim);
		long ini = System.currentTimeMillis();
		List<Object[]> results = query.list();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		long fim = System.currentTimeMillis() - ini;
		return listaFinal;
	}

	public int consultarQuantidadeAEliminar(CpOrgaoUsuario orgaoUsu,
			Date dtIni, Date dtFim) {
		final Query query = getSessao().getNamedQuery(
				"consultarQuantidadeAEliminar");
		query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		query.setDate("dtIni", dtIni);
		query.setDate("dtFim", dtFim);
		return ((Long) query.uniqueResult()).intValue();
	}

	public List<ExItemDestinacao> consultarEmEditalEliminacao(
			CpOrgaoUsuario orgaoUsu, Date dtIni, Date dtFim) {
		final Query query = getSessao().getNamedQuery(
				"consultarEmEditalEliminacao");
		query.setLong("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		query.setDate("dtIni", dtIni);
		query.setDate("dtFim", dtFim);
		List<Object[]> results = query.list();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		return listaFinal;
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
			log.error("[consultarMovimentacoes] - Os dados recebidos para realizar a consulta de movimentaï¿½ï¿½es nï¿½o podem ser nulos.");
			throw new IllegalStateException(
					"A pessoa e/ou a data informada para a realizaï¿½ï¿½o da consulta ï¿½ nula.");
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

	public List<ExModelo> listarTodosModelosOrdenarPorNome(String script) {
		final Query q = getSessao()
				.createQuery(
						"select m from ExModelo m left join m.exFormaDocumento as f where m.hisAtivo = 1"
								+ "order by f.descrFormaDoc, m.nmMod");
		List<ExModelo> l = new ArrayList<ExModelo>();		
		for (ExModelo mod : (List<ExModelo>) q.list()) {
			if (script != null && script.trim().length() != 0) {
				if(mod.getConteudoBlobMod2() != null) {
					String conteudo;
					try {
						conteudo = new String(mod.getConteudoBlobMod2(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						conteudo = new String(mod.getConteudoBlobMod2());
					}
					if ("template/freemarker".equals(mod.getConteudoTpBlob())&& (conteudo.contains(script)))
						l.add(mod);
				}
			} else
				l.add(mod);
		}
		return l;
	}

	static public Configuration criarHibernateCfg(String datasource)
			throws Exception {
		Configuration cfg = CpDao.criarHibernateCfg(datasource);
		ExDao.configurarHibernate(cfg);
		return cfg;
	}

	static public Configuration criarHibernateCfg(
			String connectionUrl, String username, String password)
					throws Exception {
		Configuration cfg = CpDao.criarHibernateCfg(connectionUrl,
				username, password);
		ExDao.configurarHibernate(cfg);
		return cfg;
	}

	static public Configuration criarHibernateCfg(
			CpAmbienteEnumBL ambiente) throws Exception {
		CpPropriedadeBL prop = Ex.getInstance().getProp();
		prop.setPrefixo("siga.ex." + ambiente.getSigla());
		Configuration cfg = CpDao.criarHibernateCfg(ambiente, prop);
		ExDao.configurarHibernate(cfg);
		return cfg;
	}

	static private void configurarHibernate(Configuration cfg)
			throws Exception {





























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

		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExClassificacao",
		// "read-only", "ex");
		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExConfiguracao",
		// "read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExEstadoDoc",
				"read-only", CACHE_EX);
		//cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExFormaDocumento",
		//	"read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExNivelAcesso",
				"read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExSituacaoConfiguracao", "read-only",
				CACHE_EX);
		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTemporalidade",
		// "read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoDespacho",
				"read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoDestinacao",
				"read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoDocumento",
				"read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExTpDocPublicacao", "read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExTipoMovimentacao", "read-only", CACHE_EX);
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoFormaDoc",
				"read-only", CACHE_EX);
		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExVia",
		// "read-only", "ex");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExTipoMobil",
				"read-only", CACHE_EX);
		cfg.setCollectionCacheConcurrencyStrategy(
				"br.gov.jfrj.siga.ex.ExTipoDocumento.exFormaDocumentoSet",
				"read-only", CACHE_EX);

		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.dp.CpTipoMarca",
				"transactional", "corporativo");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.dp.CpTipoMarcador",
				"transactional", "corporativo");
		cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.dp.CpMarcador",
				"transactional", "corporativo");

		CacheManager manager = CacheManager.getInstance();
		Cache cache;
		CacheConfiguration config;

		if (!manager.cacheExists(CACHE_EX)) {
			manager.addCache(CACHE_EX);
			cache = manager.getCache(CACHE_EX);
			config = cache.getCacheConfiguration();
			config.setTimeToIdleSeconds(3600);
			config.setTimeToLiveSeconds(36000);
			config.setMaxElementsInMemory(10000);
			config.setMaxElementsOnDisk(1000000);
		}

		// cfg.setCollectionCacheConcurrencyStrategy(
		// "br.gov.jfrj.siga.ex.ExClassificacao.exViaSet", "read-only",
		// "ex");
		// cfg.setCollectionCacheConcurrencyStrategy(
		// "br.gov.jfrj.siga.ex.ExFormaDocumento.exModeloSet",

		// "transactional", "ex");
		// cfg.setCacheConcurrencyStrategy("br.gov.jfrj.siga.ex.ExModelo",
		// "transactional", "ex");

		// Hibernate search configuration
		//
		if ("true".equals(SigaExProperties.getString("siga.lucene.ativo"))) {
			cfg.setProperty("hibernate.search.default.directory_provider", "org.hibernate.search.store.FSDirectoryProvider");
			cfg.setProperty("hibernate.search.default.indexBase", SigaExProperties.getString("siga.lucene.index.path") + "/siga-ex-lucene-index/");
			cfg.setProperty("hibernate.search.default.optimizer.operation_limit.max", "2000");
			cfg.setProperty("org.hibernate.worker.execution", "sync");
			cfg.setProperty("org.hibernate.worker.batch_size", "1000");
			cfg.setProperty("hibernate.search.indexing_strategy", "manual");
			//				cfg.getEventListeners()
			//				.setPostUpdateEventListeners(
			//						new PostUpdateEventListener[] { (PostUpdateEventListener) ReflectHelper
			//								.classForName(
			//										"org.hibernate.search.event.FullTextIndexEventListener")
			//										.newInstance() });
			//				cfg.getEventListeners()
			//				.setPostInsertEventListeners(
			//						new PostInsertEventListener[] { (PostInsertEventListener) ReflectHelper
			//								.classForName(
			//										"org.hibernate.search.event.FullTextIndexEventListener")
			//										.newInstance() });
			//				cfg.getEventListeners()
			//				.setPostDeleteEventListeners(
			//						new PostDeleteEventListener[] { (PostDeleteEventListener) ReflectHelper
			//								.classForName(
			//										"org.hibernate.search.event.FullTextIndexEventListener")
			//										.newInstance() });
		} else {
			cfg.setProperty("hibernate.search.autoregister_listeners", "false");
		}

	}

	public ExModelo consultarExModelo(String sForma, String sModelo) {
		final Criteria crit = getSessao().createCriteria(ExModelo.class);
		crit.add(Restrictions.eq("nmMod", sModelo));
		crit.add(Restrictions.eq("hisAtivo", 1));

		if (sForma != null) {
			crit.createAlias("exFormaDocumento", "f");
			crit.add(Restrictions.eq("f.descrFormaDoc", sForma));
		}
		return (ExModelo) crit.uniqueResult();
	}

	
	public ExTipoDocumento consultarExTipoDocumento(String descricao) {
		final Criteria crit = getSessao().createCriteria(ExTipoDocumento.class);
		crit.add(Restrictions.eq("descrTipoDocumento", descricao));

		return (ExTipoDocumento) crit.uniqueResult();
	}
	
	public ExNivelAcesso consultarExNidelAcesso(String nome) {
		final Criteria crit = getSessao().createCriteria(ExNivelAcesso.class);
		crit.add(Restrictions.eq("nmNivelAcesso", nome));

		return (ExNivelAcesso) crit.uniqueResult();
	}

	public ExModelo consultarModeloAtual(ExModelo mod) {
		final Query query = getSessao().getNamedQuery("consultarModeloAtual");

		query.setLong("hisIdIni", mod.getHisIdIni());
		return (ExModelo) query.uniqueResult();
	}

	public List<ExDocumento> listarDocPendenteAssinatura(DpPessoa pessoa) {
		final Query query = getSessao().getNamedQuery(
				"listarDocPendenteAssinatura");
		query.setLong("idPessoaIni", pessoa.getIdPessoaIni());
		return query.list();
	}
	
	public List<ExMovimentacao> listarDespachoPendenteAssinatura(DpPessoa pessoa) {
		final Query query = getSessao().getNamedQuery(
				"listarDespachoPendenteAssinatura");
		query.setLong("idPessoaIni", pessoa.getIdPessoaIni());
		return query.list();
	}

	public String consultarDescricaoExClassificacao(ExClassificacao exClass) {
		String[] pais = MascaraUtil.getInstance().getPais(
				exClass.getCodificacao());
		if (pais == null) {
			return exClass.getDescrClassificacao();
		}
		// pais = Arrays.copyOf(pais, pais.length+1);
		// pais[pais.length-1]= exClass.getCodificacao();

		Query q = getSessao().getNamedQuery("consultarDescricaoExClassificacao");
		q.setParameterList("listaCodificacao", pais);
		List<String> result = q.list();
		StringBuffer sb = new StringBuffer();
		for (String descr : result) {
			sb.append(descr);
			sb.append(": ");
		}

		sb.append(exClass.getDescrClassificacao());
		return sb.toString();
	}

	public List<ExClassificacao> consultarExClassificacaoVigente() {
		return consultarExClassificacao(MascaraUtil.getInstance()
				.getMscTodosDoMaiorNivel(), "");
	}

	public List<ExClassificacao> consultarFilhos(ExClassificacao exClass,
			boolean niveisAbaixo) {
		final Query query = getSessao().getNamedQuery("consultarFilhosExClassificacao");
		query.setString(
				"mascara",
				MascaraUtil.getInstance().getMscFilho(
						exClass.getCodificacao().toString(), niveisAbaixo));

		return query.list().subList(1, query.list().size());
	}

	public List<ExClassificacao> consultarExClassificacao(String mascaraLike, String descrClassificacao) {
		Query q = getSessao().getNamedQuery("consultarExClassificacaoPorMascara");
		q.setString("mascara", mascaraLike);
		q.setString("descrClassificacao", descrClassificacao.toUpperCase());

		return q.list();
	}

	public ExClassificacao consultarExClassificacao(String codificacao) {
		ExClassificacao flt = new ExClassificacao();
		flt.setSigla(codificacao);
		return ExDao.getInstance().consultarPorSigla(flt);
	}

	public List<ExDocumento> consultarExDocumentoPorClassificacao(
			DpLotacao lotacao, String mascara, CpOrgaoUsuario orgaoUsu) {
		Query q;
		if (lotacao == null) {
			q = getSessao().getNamedQuery("consultarExDocumentoClassificados");
		} else {
			q = getSessao().getNamedQuery("consultarExDocumentoClassificadosPorLotacao");
			q.setLong("idLotacao", lotacao.getId());
		}

		q.setString("mascara", mascara);
		q.setLong("idOrgaoUsuario", orgaoUsu.getIdOrgaoUsu());
		return q.list();
	}

	public List<ExPapel> listarExPapeis() {
		return findByCriteria(ExPapel.class);
	}

	public List<CpMarcador> listarCpMarcadoresGerais() {
		CpTipoMarcador marcador = consultar(CpTipoMarcador.TIPO_MARCADOR_GERAL, CpTipoMarcador.class, false);
		return findByCriteria(CpMarcador.class, Restrictions.eq("cpTipoMarcador", marcador));
	}

	public List<ExTpDocPublicacao> listarExTiposDocPublicacao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS,ExTpDocPublicacao.class);
	}

	public List<ExConfiguracao> listarExConfiguracoes() {
		return findByCriteria(ExConfiguracao.class);
	}

	public List<ExTipoDocumento> listarExTiposDocumento() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExTipoDocumento.class);
	}

	public List<ExEstadoDoc> listarExEstadosDoc() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExEstadoDoc.class);
	}

	public List<ExTipoDestinacao> listarExTiposDestinacao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExTipoDestinacao.class);
	}

	public List<ExTipoFormaDoc> listarExTiposFormaDoc() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExTipoFormaDoc.class);
	}

	public List<ExTipoMovimentacao> listarExTiposMovimentacao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS,
				ExTipoMovimentacao.class);
	}

	public List<ExModelo> listarExModelos() {
		return findByCriteria(ExModelo.class);
	}

	public List<ExVia> listarExVias() {
		return findByCriteria(ExVia.class);
	}

	public List<ExFormaDocumento> listarExFormasDocumento() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExFormaDocumento.class);
	}

	public List<ExTipoDespacho> listarExTiposDespacho() {
		return findByCriteria(ExTipoDespacho.class);
	}

}