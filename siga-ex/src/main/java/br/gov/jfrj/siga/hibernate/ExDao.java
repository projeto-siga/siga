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

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
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
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ext.IExMobilDaoFiltro;
import br.gov.jfrj.siga.hibernate.ext.IMontadorQuery;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.ExClassificacaoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExDocumentoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExModeloDaoFiltro;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ExDao extends CpDao {

	public static final String CACHE_EX = "ex";

	private static final Logger log = Logger.getLogger(ExDao.class);

	public static ExDao getInstance() {
		return ModeloDao.getInstance(ExDao.class);
	}

	public ExDao() {
	}

	public List<ExDocumento> consultarDocsInclusosNoBoletim(ExDocumento doc) {
		final Query query = em().createNamedQuery(
				"consultarDocsInclusosNoBoletim");
		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", ExDao.CACHE_QUERY_SECONDS);

		query.setParameter("idDoc", doc.getIdDoc());
		return query.getResultList();
	}

	public ExBoletimDoc consultarBoletimEmQueODocumentoEstaIncluso(
			ExDocumento doc) {
		final Query query = em().createNamedQuery(
				"consultarBoletimEmQueODocumentoEstaIncluso");

		query.setParameter("idDoc", doc.getIdDoc());

		final List<ExBoletimDoc> l = query.getResultList();
		if (l.size() != 1)
			return null;

		return l.get(0);
	}

	public List<ExDocumento> consultarDocsDisponiveisParaInclusaoEmBoletim(
			CpOrgaoUsuario orgaoUsu) {
		final Query query = em().createNamedQuery(
				"consultarDocsDisponiveisParaInclusaoEmBoletim");
		query.setParameter("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		return query.getResultList();
	}

	public List<ExBoletimDoc> consultarBoletim(ExDocumento doc) {
		final Query query = em().createNamedQuery("consultarBoletim");

		query.setParameter("idDoc", doc.getIdDoc());

		return query.getResultList();
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
			Query query = em().createQuery(appender.toString());
			return query.getResultList();
		}
		return null;
	}

	public Long obterProximoNumero(final ExDocumento doc, Long anoEmissao)
			throws SQLException {
		Query query = em().createNamedQuery("obterProximoNumero");
		query.setParameter("idOrgaoUsu", doc.getOrgaoUsuario().getId());
		query.setParameter("idFormaDoc", doc.getExFormaDocumento().getId());
		query.setParameter("anoEmissao", anoEmissao);

		return (Long) query.getSingleResult();
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

		Query query = em().createNamedQuery("consultarPorFiltro");
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}
		HibernateBeanProperties.setQueryProperties(query, flt);
		query.setParameter("titular",
				titular.getIdPessoaIni() != null ? titular.getIdPessoaIni() : 0);
		query.setParameter(
				"lotaTitular",
				lotaTitular.getIdLotacaoIni() != null ? lotaTitular
						.getIdLotacaoIni() : 0);
		List l = query.getResultList();
		return l;
	}

	public void preencherParametros(final IExMobilDaoFiltro flt,
			final Query query) {
		if (flt.getUltMovIdEstadoDoc() != null
				&& flt.getUltMovIdEstadoDoc() != 0) {

			query.setParameter("ultMovIdEstadoDoc", flt.getUltMovIdEstadoDoc());

		}

		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {

			query.setParameter("ultMovRespSelId", flt.getUltMovRespSelId());
		}

		if (flt.getUltMovLotaRespSelId() != null
				&& flt.getUltMovLotaRespSelId() != 0) {

			query.setParameter("ultMovLotaRespSelId", flt.getUltMovLotaRespSelId());
		}

		if (flt.getIdTipoMobil() != null && flt.getIdTipoMobil() != 0) {

			query.setParameter("idTipoMobil", flt.getIdTipoMobil());
		}

		if (flt.getNumSequencia() != null && flt.getNumSequencia() != 0) {

			query.setParameter("numSequencia", flt.getNumSequencia());
		}

		if (flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0) {

			query.setParameter("idOrgaoUsu", flt.getIdOrgaoUsu());
		}

		if (flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0) {
			query.setParameter("anoEmissao", flt.getAnoEmissao());
		}

		if (flt.getNumExpediente() != null && flt.getNumExpediente() != 0) {
			query.setParameter("numExpediente", flt.getNumExpediente());
		}

		if (flt.getIdTpDoc() != null && flt.getIdTpDoc() != 0) {
			query.setParameter("idTpDoc", flt.getIdTpDoc());
		}

		if (flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0) {
			query.setParameter("idFormaDoc", flt.getIdFormaDoc());
		}

		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			query.setParameter("idTipoFormaDoc", flt.getIdTipoFormaDoc());
		}

		if (flt.getClassificacaoSelId() != null
				&& flt.getClassificacaoSelId() != 0) {
			query.setParameter("classificacaoSelId", flt.getClassificacaoSelId());
		}

		if (flt.getDescrDocumento() != null

		&& !flt.getDescrDocumento().trim().equals("")) {
			query.setParameter("descrDocumento", "%"
					+ flt.getDescrDocumento().toUpperCase() + "%");
		}

		if (flt.getDtDoc() != null) {
			query.setParameter("dtDoc",
					new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(flt
							.getDtDoc()));
		}

		if (flt.getDtDocFinal() != null) {
			query.setParameter("dtDocFinal", new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss").format(flt.getDtDocFinal()));
		}

		if (flt.getNumAntigoDoc() != null

		&& !flt.getNumAntigoDoc().trim().equals("")) {
			query.setParameter("numAntigoDoc", "%"
					+ flt.getNumAntigoDoc().toUpperCase() + "%");
		}

		if (flt.getDestinatarioSelId() != null
				&& flt.getDestinatarioSelId() != 0) {
			query.setParameter("destinatarioSelId", flt.getDestinatarioSelId());
		}

		if (flt.getLotacaoDestinatarioSelId() != null
				&& flt.getLotacaoDestinatarioSelId() != 0) {
			query.setParameter("lotacaoDestinatarioSelId",
					flt.getLotacaoDestinatarioSelId());
		}

		if (flt.getNmDestinatario() != null
				&& !flt.getNmDestinatario().trim().equals("")) {
			query.setParameter("nmDestinatario", "%" + flt.getNmDestinatario()
					+ "%");
		}

		if (flt.getOrgaoExternoDestinatarioSelId() != null
				&& flt.getOrgaoExternoDestinatarioSelId() != 0) {
			query.setParameter("orgaoExternoDestinatarioSelId",
					flt.getOrgaoExternoDestinatarioSelId());
		}

		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			query.setParameter("cadastranteSelId", flt.getCadastranteSelId());
		}

		if (flt.getLotaCadastranteSelId() != null
				&& flt.getLotaCadastranteSelId() != 0) {
			query.setParameter("lotaCadastranteSelId", flt.getLotaCadastranteSelId());
		}

		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			query.setParameter("subscritorSelId", flt.getSubscritorSelId());
		}

		if (flt.getNmSubscritorExt() != null

		&& !flt.getNmSubscritorExt().trim().equals("")) {
			query.setParameter("nmSubscritorExt", "%"
					+ flt.getNmSubscritorExt().toUpperCase() + "%");
		}

		if (flt.getOrgaoExternoSelId() != null
				&& flt.getOrgaoExternoSelId() != 0) {
			query.setParameter("orgaoExternoSelId", flt.getOrgaoExternoSelId());
		}

		if (flt.getNumExtDoc() != null && !flt.getNumExtDoc().trim().equals("")) {
			query.setParameter("numExtDoc", "%" + flt.getNumExtDoc().toUpperCase()
					+ "%");
		}

		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			ExModelo mod = ExDao.getInstance().consultar(flt.getIdMod(),
					ExModelo.class, false);
			query.setParameter("hisIdIni", mod.getHisIdIni());
		}
	}

	public List consultarPorFiltroOtimizado(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina, DpPessoa titular,
			DpLotacao lotaTitular) {

		IMontadorQuery montadorQuery = carregarPlugin();

		long tempoIni = System.nanoTime();
		Query query = em().createQuery(
				montadorQuery.montaQueryConsultaporFiltro(flt, false));
		preencherParametros(flt, query);

		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}
		List l = query.getResultList();
		long tempoTotal = System.nanoTime() - tempoIni;
		// System.out.println("consultarPorFiltroOtimizado: " +
		// tempoTotal/1000000 + " ms -> " + query + ", resultado: " + l);
		return l;
	}

	private IMontadorQuery carregarPlugin() {
		CarregadorPlugin carregador = new CarregadorPlugin();
		IMontadorQuery montadorQuery = carregador.getMontadorQueryImpl();
		montadorQuery
				.setMontadorPrincipal(carregador.getMontadorQueryDefault());
		return montadorQuery;
	}

	public Integer consultarQuantidadePorFiltroOtimizado(
			final ExMobilDaoFiltro flt, DpPessoa titular, DpLotacao lotaTitular) {
		long tempoIni = System.nanoTime();
		IMontadorQuery montadorQuery = carregarPlugin();
		String s = montadorQuery.montaQueryConsultaporFiltro(flt, true);
		Query query = em().createQuery(s);

		preencherParametros(flt, query);

		Long l = (Long) query.getSingleResult();
		long tempoTotal = System.nanoTime() - tempoIni;
		// System.out.println("consultarQuantidadePorFiltroOtimizado: "
		// + tempoTotal / 1000000 + " ms -> " + s + ", resultado: " + l);
		return l.intValue();
	}

	public List consultarPorFiltroOld(final ExMobilDaoFiltro flt,
			final int offset, final int itemPagina, DpPessoa titular,
			DpLotacao lotaTitular) {
		try {
			final Query query = em().createNamedQuery("consultarPorFiltro");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			HibernateBeanProperties.setQueryProperties(query, flt);
			if (titular.getIdPessoaIni() != null)
				query.setParameter("titular", titular.getIdPessoaIni());
			else
				query.setParameter("titular", 0);
			if (lotaTitular.getIdLotacaoIni() != null)
				query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());
			else
				query.setParameter("lotaTitular", 0);

			if (flt.getDescrDocumento() != null)
				query.setParameter("descrDocumento", flt.getDescrDocumento()
						.toUpperCase().replace(' ', '%'));

			return query.getResultList();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public Integer consultarQuantidadePorFiltro(final ExMobilDaoFiltro flt,
			DpPessoa titular, DpLotacao lotaTitular) {
		Query query = em().createNamedQuery("consultarQuantidadePorFiltro");
		HibernateBeanProperties.setQueryProperties(query, flt);
		query.setParameter("titular", titular.getIdPessoaIni());
		query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());
		long tempoIni = System.nanoTime();
		Long l = (Long) query.getSingleResult();
		long tempoTotal = System.nanoTime() - tempoIni;
		// System.out.println("consultarQuantidadePorFiltro: " + tempoTotal
		// / 1000000 + ", resultado: " + l);
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
				final Query query = em().createNamedQuery(
						"consultarPorSiglaDocumento");
				HibernateBeanProperties.setQueryProperties(query, flt);

				final List<ExDocumento> l = query.getResultList();

				if (l.size() != 1)
					return null;

				return l.get(0).getMobilGeral();
			}

			final Query query = em().createNamedQuery("consultarPorSigla");
			HibernateBeanProperties.setQueryProperties(query, flt);

			final List<ExMobil> l = query.getResultList();

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
		CriteriaQuery<ExFormaDocumento> q = cb().createQuery(ExFormaDocumento.class);
		Root<ExFormaDocumento> c = q.from(ExFormaDocumento.class);
		q.select(c);
		q.orderBy(cb().asc(c.get("descrFormaDoc")));
		return em().createQuery(q).getResultList();
	}

	public List<ExFormaDocumento> listarTodosOrdenarPorSigla() {
		CriteriaQuery<ExFormaDocumento> q = cb().createQuery(ExFormaDocumento.class);
		Root<ExFormaDocumento> c = q.from(ExFormaDocumento.class);
		q.select(c);
		q.orderBy(cb().asc(cb().parameter(String.class, "siglaFormaDoc")));
		return em().createQuery(q).getResultList();
	}

	public List<ExDocumento> consultarPorModeloEAssinatura(
			CpOrgaoUsuario orgaoUsu, ExModelo mod, Date dtAssinaturaIni,
			Date dtAssinaturaFim) {
		if (mod != null) {
			final Query query = em().createNamedQuery(
					"consultarPorModeloEAssinatura");
			query.setParameter("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
			query.setParameter("idMod", mod.getIdMod());
			query.setParameter("dataIni", dtAssinaturaIni);
			query.setParameter("dataFim", dtAssinaturaFim);
			return query.getResultList();
		}
		return null;
	}

	public List<ExDocumento> listarAgendados() {
		final Query query = em().createNamedQuery("listarAgendados");
		return query.getResultList();
	}

	public List<ExEmailNotificacao> consultarEmailNotificacao(DpPessoa pess,
			DpLotacao lot) {
		final Query query;

		if (pess != null) {
			query = em().createNamedQuery("consultarEmailporPessoa");
			query.setParameter("idPessoaIni", pess.getIdInicial());
		} else {
			query = em().createNamedQuery("consultarEmailporLotacao");

			query.setParameter("idLotacaoIni", lot.getIdLotacaoIni());
		}

		return query.getResultList();
	}

	public List consultarPaginaInicial(DpPessoa pes, DpLotacao lot,
			Integer idTipoForma) {
		try {
			Query sql = em().createNamedQuery(
					"consultarPaginaInicial");

			sql.setParameter("idPessoaIni", pes.getIdPessoaIni());
			sql.setParameter("idLotacaoIni", lot.getIdLotacaoIni());
			sql.setParameter("idTipoForma", idTipoForma);

			List result = sql.getResultList();

			return result;

		} catch (final NullPointerException e) {
			return null;
		}
	}

	public List<ExNivelAcesso> listarOrdemNivel() {
		Query query = em().createNamedQuery("listarOrdemNivel");
		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", "query.ExNivelAcesso");
		return query.getResultList();
	}

	public List<ExPreenchimento> consultar(ExPreenchimento exPreenchimento) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorFiltroExPreenchimento");
			if (exPreenchimento.getNomePreenchimento() != null)
				query.setParameter("nomePreenchimento", exPreenchimento
						.getNomePreenchimento().toUpperCase().replace(' ', '%'));
			else
				query.setParameter("nomePreenchimento", "");
			if (exPreenchimento.getDpLotacao() != null)
				query.setParameter("lotacao", exPreenchimento.getDpLotacao()
						.getIdLotacao());
			else
				query.setParameter("lotacao", 0);
			if (exPreenchimento.getExModelo() != null)
				query.setParameter("modelo", exPreenchimento.getExModelo()
						.getHisIdIni());
			else
				query.setParameter("modelo", 0);
			final List<ExPreenchimento> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public List consultarAtivos() {
		final Query query = em().createNamedQuery("consultarAtivos");
		return query.getResultList();
	}

	public List<ExConfiguracao> consultar(final ExConfiguracao exemplo) {
		CpTipoConfiguracao tpConf = exemplo.getCpTipoConfiguracao();
		CpOrgaoUsuario orgao = exemplo.getOrgaoUsuario();

		StringBuffer sbf = new StringBuffer();

		sbf.append("select * from siga.ex_configuracao ex inner join "
				+ "corporativo"
				+ ".cp_configuracao cp on ex.id_configuracao_ex = cp.id_configuracao ");

		sbf.append("" + "where 1 = 1");

		if (tpConf != null && tpConf.getIdTpConfiguracao() != null
				&& tpConf.getIdTpConfiguracao() != 0) {
			sbf.append(" and cp.id_tp_configuracao = ");
			sbf.append(exemplo.getCpTipoConfiguracao().getIdTpConfiguracao());
		}

		if (exemplo.getExTipoMovimentacao() != null
				&& exemplo.getExTipoMovimentacao().getIdTpMov() != null
				&& exemplo.getExTipoMovimentacao().getIdTpMov() != 0) {
			sbf.append(" and ex.id_tp_mov = ");
			sbf.append(exemplo.getExTipoMovimentacao().getIdTpMov());
		}

		if (exemplo.getExFormaDocumento() != null
				&& exemplo.getExFormaDocumento().getIdFormaDoc() != null) {
			sbf.append(" and (ex.id_forma_doc = ");
			sbf.append(exemplo.getExFormaDocumento().getIdFormaDoc());
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc is null and ex.id_tp_forma_doc is null)");
			sbf.append(" or (ex.id_forma_doc is null and ex.id_tp_forma_doc = ");
			sbf.append(exemplo.getExFormaDocumento().getExTipoFormaDoc()
					.getId());
			sbf.append(" ))");
		}

		if (exemplo.getExModelo() != null
				&& exemplo.getExModelo().getIdMod() != null
				&& exemplo.getExModelo().getIdMod() != 0) {
			sbf.append(" and (ex.id_mod = ");
			sbf.append(exemplo.getExModelo().getIdMod());
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc is null and ex.id_tp_forma_doc is null)");
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc = ");
			sbf.append(exemplo.getExModelo().getExFormaDocumento().getId());
			sbf.append(")");
			sbf.append(" or (ex.id_mod is null and ex.id_forma_doc is null and ex.id_tp_forma_doc = ");
			sbf.append(exemplo.getExModelo().getExFormaDocumento()
					.getExTipoFormaDoc().getId());
			sbf.append(" ))");
		}

		if (orgao != null && orgao.getId() != null && orgao.getId() != 0) {
			sbf.append(" and (cp.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(" or cp.id_lotacao in (select id_lotacao from "
					+ "corporativo"
					+ ".dp_lotacao lot where lot.id_orgao_usu= ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_pessoa in (select id_pessoa from "
					+ "corporativo"
					+ ".dp_pessoa pes where pes.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_cargo in (select id_cargo from "
					+ "corporativo" + ".dp_cargo cr where cr.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or cp.id_funcao_confianca in (select id_funcao_confianca from "
					+ "corporativo"
					+ ".dp_funcao_confianca fc where fc.id_orgao_usu = ");
			sbf.append(orgao.getId());
			sbf.append(")");
			sbf.append(" or (cp.id_orgao_usu is null and cp.id_lotacao is null and cp.id_pessoa is null and cp.id_cargo is null and cp.id_funcao_confianca is null");
			sbf.append(")");
			sbf.append(")");
			sbf.append("order by ex.id_configuracao_ex");

		}

		Query query = em().createNativeQuery(sbf.toString(), ExConfiguracao.class);
		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", "query.ExConfiguracao");
		return query.getResultList();

	}

	public List<ExClassificacao> listarExClassificacaoPorNivel(String mascara,
			String exceto) {
		Query q = em().createNamedQuery(
				"consultarExClassificacaoComExcecao");
		q.setParameter("mascara", mascara);
		q.setParameter("exceto", exceto);
		return q.getResultList();

	}

	public List<ExClassificacao> listarExClassificacaoPorNivel(String mascara) {
		Query q = em().createNamedQuery(
				"consultarExClassificacaoPorMascara");
		q.setParameter("mascara", mascara);
		q.setParameter("descrClassificacao", "");
		return q.getResultList();

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

		final Query query = em().createNamedQuery(
				"consultarPorFiltroExClassificacao");
		if (offset > 0) {
			query.setFirstResult(offset);
		}
		if (itemPagina > 0) {
			query.setMaxResults(itemPagina);
		}

		if (flt.getSigla() == null || flt.getSigla().equals("")) {
			query.setParameter("mascara", MascaraUtil.getInstance()
					.getMscTodosDoMaiorNivel());
		} else {
			query.setParameter(
					"mascara",
					MascaraUtil.getInstance().getMscFilho(
							flt.getSigla().toString(), true));
		}

		query.setParameter("descrClassificacao", descrClassificacao.toUpperCase()
				.replace(' ', '%'));
		query.setParameter("descrClassificacaoSemAcento", Texto
				.removeAcentoMaiusculas(descrClassificacao).replace(' ', '%'));

		final List<ExClassificacao> l = query.getResultList();
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

		final Query query = em().createNamedQuery(
				"consultarQuantidadeExClassificacao");

		if (flt.getSigla() == null || flt.getSigla().equals("")) {
			query.setParameter("mascara", MascaraUtil.getInstance()
					.getMscTodosDoMaiorNivel());
		} else {
			query.setParameter(
					"mascara",
					MascaraUtil.getInstance().getMscFilho(
							flt.getSigla().toString(), true));
		}

		query.setParameter("descrClassificacao", descrClassificacao.toUpperCase()
				.replace(' ', '%'));
		query.setParameter("descrClassificacaoSemAcento", Texto
				.removeAcentoMaiusculas(descrClassificacao).replace(' ', '%'));

		final int l = ((Long) query.getSingleResult()).intValue();
		return l;
	}

	public ExClassificacao consultarPorSigla(final ExClassificacao o) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorSiglaExClassificacao");
			query.setParameter("codificacao",
					MascaraUtil.getInstance().formatar(o.getSigla()));

			final List<ExClassificacao> l = query.getResultList();
			if (l.size() != 1)
				return null;
			return l.get(0);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public ExClassificacao consultarAtual(final ExClassificacao o) {
		try {
			final Query query = em()
					.createNamedQuery("consultarAtualPorId");
			query.setParameter("idRegIni", o.getHisIdIni());
			return (ExClassificacao) query.getSingleResult();
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
			final Query query = em().createNamedQuery(
					"consultarMovimentacoesPorLotacaoEntreDatas");
			if (lotaTitular.getIdLotacaoIni() != null)
				query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());
			else
				query.setParameter("lotaTitular", 0);

			return query.getResultList();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public ExTpDocPublicacao consultarPorModelo(ExModelo mod) {
		try {
			final Query query = em().createNamedQuery("consultarPorModelo");
			query.setParameter("idMod", mod.getHisIdIni());

			return (ExTpDocPublicacao) query.getResultList().get(0);
		} catch (final Throwable t) {
			// engolindo a exceï¿½ï¿½o. Melhorar isso.
			return null;
		}
	}

	public List<ExDocumento> listarSolicitados(CpOrgaoUsuario orgaoUsu) {
		final Query query = em().createNamedQuery("listarSolicitados");
		query.setParameter("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		return query.getResultList();
	}

	public List<ExMobil> consultarParaArquivarCorrenteEmLote(DpLotacao lot) {
		final Query query = em().createNamedQuery(

		"consultarParaArquivarCorrenteEmLote");
		query.setParameter("lotaIni", lot.getIdLotacaoIni());
		return query.getResultList();
	}

	public List<ExItemDestinacao> consultarParaArquivarIntermediarioEmLote(
			DpLotacao lot, int offset) {
		final Query query = em().createNamedQuery(
				"consultarParaArquivarIntermediarioEmLote");
		query.setParameter("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		query.setFirstResult(offset);
		query.setMaxResults(100);
		List<Object[]> results = query.getResultList();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		return listaFinal;
	}

	public int consultarQuantidadeParaArquivarIntermediarioEmLote(DpLotacao lot) {
		final Query query = em().createNamedQuery(
				"consultarQuantidadeParaArquivarIntermediarioEmLote");
		query.setParameter("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		return ((Long) query.getSingleResult()).intValue();
	}

	public List<ExItemDestinacao> consultarParaArquivarPermanenteEmLote(
			DpLotacao lot, int offset) {
		final Query query = em().createNamedQuery(
				"consultarParaArquivarPermanenteEmLote");
		query.setParameter("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		query.setFirstResult(offset);
		query.setMaxResults(100);
		List<Object[]> results = query.getResultList();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		return listaFinal;
	}

	public int consultarQuantidadeParaArquivarPermanenteEmLote(DpLotacao lot) {
		final Query query = em().createNamedQuery(
				"consultarQuantidadeParaArquivarPermanenteEmLote");
		query.setParameter("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		return ((Long) query.getSingleResult()).intValue();
	}

	// public Query consultarParaTransferirEmLote(DpLotacao lot) {
	@SuppressWarnings("unchecked")
	public List<ExMobil> consultarParaTransferirEmLote(DpLotacao lot) {
		final Query query = em().createNamedQuery(
				"consultarParaTransferirEmLote");
		query.setParameter("lotaIni", lot.getIdLotacaoIni());
		// return query;s
		return query.getResultList();
	}

	public List<ExMobil> consultarParaAnotarEmLote(DpLotacao lot) {
		final Query query = em().createNamedQuery(
				"consultarParaAnotarEmLote");
		query.setParameter("lotaIni", lot.getIdLotacaoIni());
		return query.getResultList();
	}

	public List<ExItemDestinacao> consultarAEliminar(CpOrgaoUsuario orgaoUsu,
			Date dtIni, Date dtFim) {
		final Query query = em().createNamedQuery("consultarAEliminar");
		query.setParameter("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		query.setParameter("dtIni", dtIni);
		query.setParameter("dtFim", dtFim);
		long ini = System.currentTimeMillis();
		List<Object[]> results = query.getResultList();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		long fim = System.currentTimeMillis() - ini;
		return listaFinal;
	}

	public int consultarQuantidadeAEliminar(CpOrgaoUsuario orgaoUsu,
			Date dtIni, Date dtFim) {
		final Query query = em().createNamedQuery(
				"consultarQuantidadeAEliminar");
		query.setParameter("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		query.setParameter("dtIni", dtIni);
		query.setParameter("dtFim", dtFim);
		return ((Long) query.getSingleResult()).intValue();
	}

	public List<ExItemDestinacao> consultarEmEditalEliminacao(
			CpOrgaoUsuario orgaoUsu, Date dtIni, Date dtFim) {
		final Query query = em().createNamedQuery(
				"consultarEmEditalEliminacao");
		query.setParameter("idOrgaoUsu", orgaoUsu.getIdOrgaoUsu());
		query.setParameter("dtIni", dtIni);
		query.setParameter("dtFim", dtFim);
		List<Object[]> results = query.getResultList();
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		for (Object[] result : results) {
			listaFinal.add(new ExItemDestinacao(result));
		}
		return listaFinal;
	}

	public ExFormaDocumento consultarPorSigla(ExFormaDocumento o) {
		final Query query = em().createNamedQuery("consultarSiglaForma");
		query.setParameter("sigla", o.getSigla());

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", "query.ExFormaDocumento");

		final List<ExFormaDocumento> l = query.getResultList();
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
		final Query query = em().createNamedQuery(
				"consultarParaReceberEmLote");
		query.setParameter("lotaIni", lot.getIdLotacaoIni());
		return query.getResultList();
	}

	public List<ExMobil> consultarParaViaDeProtocolo(DpLotacao lot) {
		final Query query = em().createNamedQuery(
				"consultarParaViaDeProtocolo");
		query.setParameter("lotaIni", lot.getIdLotacaoIni());
		return query.getResultList();
	}

	public List<ExMovimentacao> consultarMovimentacoes(DpPessoa pes, Date dt) {

		if (pes == null || dt == null) {
			log.error("[consultarMovimentacoes] - Os dados recebidos para realizar a consulta de movimentaï¿½ï¿½es nï¿½o podem ser nulos.");
			throw new IllegalStateException(
					"A pessoa e/ou a data informada para a realizaï¿½ï¿½o da consulta ï¿½ nula.");
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		final Query query = em().createNamedQuery("consultarMovimentacoes");
		ExMovimentacao mov = consultar(1122650L, ExMovimentacao.class, false);

		query.setParameter("pessoaIni", pes.getIdPessoaIni());
		// query.setParameter("data", dt);
		query.setParameter("data", df.format(dt));
		return query.getResultList();
	}

	public Date getServerDateTime() {
		return null;
	}

	public List<ExModelo> listarTodosModelosOrdenarPorNome(
			ExTipoDocumento tipo, String script) {
		Query q = null;

		if (tipo != null) {
			q = em()
					.createQuery(
							"select m from ExModelo m left join m.exFormaDocumento as f join f.exTipoDocumentoSet as t where t = :tipo and m.hisAtivo = 1"
									+ "order by m.nmMod");
			q.setParameter("tipo", tipo);
		} else {
			q = em()
					.createQuery(
							"select m from ExModelo m left join m.exFormaDocumento as f where m.hisAtivo = 1"
									+ "order by m.nmMod");
		}
		List<ExModelo> l = new ArrayList<ExModelo>();
		for (ExModelo mod : (List<ExModelo>) q.getResultList()) {
			if (script != null && script.trim().length() != 0) {
				if (mod.getConteudoBlobMod2() != null) {
					String conteudo;
					try {
						conteudo = new String(mod.getConteudoBlobMod2(),
								"utf-8");
					} catch (UnsupportedEncodingException e) {
						conteudo = new String(mod.getConteudoBlobMod2());
					}
					if ("template/freemarker".equals(mod.getConteudoTpBlob())
							&& (conteudo.contains(script)))
						l.add(mod);
				}
			} else
				l.add(mod);
		}
		return l;
	}

	public ExModelo consultarExModelo(String sForma, String sModelo) {
		CriteriaQuery<ExModelo> q = cb().createQuery(ExModelo.class);
		Root<ExModelo> c = q.from(ExModelo.class);
		q.select(c);
		Join<ExModelo, ExFormaDocumento> joinForma = c.join("exFormaDocumento");
		
		List<Predicate> whereList = new LinkedList<Predicate>();
		whereList.add(cb().equal(c.get("nmMod"), sModelo));
		whereList.add(cb().equal(c.get("hisAtivo"), 1));
		if (sForma != null) {
			c.join("exFormaDocumento", JoinType.INNER);
			whereList.add(cb().equal(joinForma.get("descrFormaDoc"), sForma));
		}
		Predicate[] whereArray = new Predicate[whereList.size()];
		whereList.toArray(whereArray);
		q.where(whereArray);
		
		try {
			return em().createQuery(q).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public ExFormaDocumento consultarExForma(String sForma) {
		CriteriaQuery<ExFormaDocumento> q = cb().createQuery(ExFormaDocumento.class);
		Root<ExFormaDocumento> c = q.from(ExFormaDocumento.class);
		q.select(c);
		q.where(cb().equal(cb().parameter(String.class, "descrFormaDoc"), sForma));
		return em().createQuery(q).getSingleResult();
	}

	public ExTipoDocumento consultarExTipoDocumento(String descricao) {
		CriteriaQuery<ExTipoDocumento> q = cb().createQuery(ExTipoDocumento.class);
		Root<ExTipoDocumento> c = q.from(ExTipoDocumento.class);
		q.select(c);
		q.where(cb().equal(cb().parameter(String.class, "descrTipoDocumento"), descricao));
		return em().createQuery(q).getSingleResult();
	}

	public ExNivelAcesso consultarExNidelAcesso(String nome) {
		CriteriaQuery<ExNivelAcesso> q = cb().createQuery(ExNivelAcesso.class);
		Root<ExNivelAcesso> c = q.from(ExNivelAcesso.class);
		q.select(c);
		q.where(cb().equal(cb().parameter(String.class, "nmNivelAcesso"), nome));
		return em().createQuery(q).getSingleResult();
	}

	public ExModelo consultarModeloAtual(ExModelo mod) {
		final Query query = em().createNamedQuery("consultarModeloAtual");

		query.setParameter("hisIdIni", mod.getHisIdIni());
		return (ExModelo) query.getSingleResult();
	}

	public List<ExDocumento> listarDocPendenteAssinatura(DpPessoa pessoa, boolean apenasComSolicitacaoDeAssinatura) {
		final Query query = em().createNamedQuery(
				"listarDocPendenteAssinatura" + (apenasComSolicitacaoDeAssinatura ? "ERevisado" : ""));
		query.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
		return query.getResultList();
	}

	public List<ExMovimentacao> listarAnexoPendenteAssinatura(DpPessoa pessoa) {
		final Query query = em().createNamedQuery(
				"listarAnexoPendenteAssinatura");
		query.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
		return query.getResultList();
	}

	public List<ExMovimentacao> listarDespachoPendenteAssinatura(DpPessoa pessoa) {
		final Query query = em().createNamedQuery(
				"listarDespachoPendenteAssinatura");
		query.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
		return query.getResultList();
	}

	public String consultarDescricaoExClassificacao(ExClassificacao exClass) {
		String[] pais = MascaraUtil.getInstance().getPais(
				exClass.getCodificacao());
		if (pais == null) {
			return exClass.getDescrClassificacao();
		}
		// pais = Arrays.copyOf(pais, pais.length+1);
		// pais[pais.length-1]= exClass.getCodificacao();
		
		Query q = em()
				.createNamedQuery("consultarDescricaoExClassificacao");
		q.setParameter("listaCodificacao", Arrays.asList(pais));
		q.setHint("org.hibernate.cacheable", true);
		q.setHint("org.hibernate.cacheRegion", ExDao.CACHE_QUERY_SECONDS);
		
		List<String> result = q.getResultList();
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
		final Query query = em().createNamedQuery(
				"consultarFilhosExClassificacao");
		query.setParameter(
				"mascara",
				MascaraUtil.getInstance().getMscFilho(
						exClass.getCodificacao().toString(), niveisAbaixo));

		return query.getResultList().subList(1, query.getResultList().size());
	}

	public List<ExClassificacao> consultarExClassificacao(String mascaraLike,
			String descrClassificacao) {
		Query q = em().createNamedQuery(
				"consultarExClassificacaoPorMascara");
		q.setParameter("mascara", mascaraLike);
		q.setParameter("descrClassificacao", descrClassificacao.toUpperCase());

		return q.getResultList();
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
			q = em().createNamedQuery("consultarExDocumentoClassificados");
		} else {
			q = em().createNamedQuery(
					"consultarExDocumentoClassificadosPorLotacao");
			q.setParameter("idLotacao", lotacao.getId());
		}

		q.setParameter("mascara", mascara);
		q.setParameter("idOrgaoUsuario", orgaoUsu.getIdOrgaoUsu());
		return q.getResultList();
	}

	public List<ExPapel> listarExPapeis() {
		return findByCriteria(ExPapel.class);
	}

	public List<CpMarcador> listarCpMarcadoresGerais() {
		CpTipoMarcador marcador = consultar(CpTipoMarcador.TIPO_MARCADOR_GERAL,
				CpTipoMarcador.class, false);
		List<CpMarcador> l = new ArrayList<>();
		l.addAll(marcador.getCpMarcadorSet());
		return l;
	}

	public List<ExTpDocPublicacao> listarExTiposDocPublicacao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS,
				ExTpDocPublicacao.class);
	}

	public List<ExConfiguracao> listarExConfiguracoes() {
		return findByCriteria(ExConfiguracao.class);
	}

	public List<ExTipoDocumento> listarExTiposDocumento() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExTipoDocumento.class);
	}

	public List<ExPapel> listarExPapel() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, ExPapel.class);
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

	public List<ExDocumento> listarPorClassificacao(ExClassificacao cl)
			throws Exception {
		CriteriaQuery<ExDocumento> q = cb().createQuery(ExDocumento.class);
		Root<ExDocumento> c = q.from(ExDocumento.class);
		q.select(c);
		q.where(cb().equal(cb().parameter(String.class, "exClassificacao"), cl));
		q.orderBy(cb().asc(cb().parameter(Long.class, "idDoc")));
		return em().createQuery(q).getResultList();
	}

	public ExClassificacao obterClassificacaoAtual(
			final ExClassificacao classificacao) {
		try {
			final Query qry = em().createNamedQuery("consultarAtualPorId");
			qry.setParameter("hisIdIni", classificacao.getHisIdIni());
			final ExClassificacao c = (ExClassificacao) qry.getSingleResult();
			return c;
		} catch (final IllegalArgumentException e) {
			throw e;
		} catch (final Exception e) {
			return null;
		}
	}
	
	public List listarDocumentosPorPessoaOuLotacao(DpPessoa titular,
			DpLotacao lotaTitular) {

		long tempoIni = System.nanoTime();
		Query query = em()
				.createQuery(
						"select marca, marcador, mobil from ExMarca marca"
								+ " inner join marca.cpMarcador marcador"
								+ " inner join marca.exMobil mobil"
								+ " where (marca.dtIniMarca is null or marca.dtIniMarca < CURRENT_TIMESTAMP)"
								+ " and (marca.dtFimMarca is null or marca.dtFimMarca > CURRENT_TIMESTAMP)"
								+ " and (marca.dpPessoaIni.idPessoa = :titular or "
								+ " (marca.dpPessoaIni.idPessoa = null and marca.dpLotacaoIni.idLotacao = :lotaTitular))");

		if (titular != null)
			query.setParameter("titular", titular.getIdPessoaIni());
		else
			query.setParameter("titular", null);
		if (lotaTitular != null)
			query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());
		else
			query.setParameter("lotaTitular", null);

		List l = query.getResultList();
 		long tempoTotal = System.nanoTime() - tempoIni;
		// System.out.println("consultarPorFiltroOtimizado: " + tempoTotal
		// / 1000000 + " ms -> " + query + ", resultado: " + l);
		return l;
	}
	
	public List<Long> arquivamentosEmVolumes(int primeiro, int ultimo) {
		Query query = em().createQuery(
				"select distinct(doc.idDoc) from ExMarca mar "
						+ "inner join mar.exMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "where mar.cpMarcador.idMarcador = 6"
						+ "and mob.exTipoMobil.idTipoMobil = 4 "
						+ (primeiro > 0 ? "and doc.idDoc > " + primeiro : "")
						+ (ultimo > 0 ? "and doc.idDoc < " + ultimo : "")
						+ " order by doc.idDoc");
		return query.getResultList();
	}


	public List<ExModelo> consultarPorFiltro(
			final ExModeloDaoFiltro flt) {
		return consultarPorFiltro(flt, 0, 0);
	}
	
	public List<ExModelo> consultarPorFiltro(
			final ExModeloDaoFiltro flt, final int offset,
			final int itemPagina) {
		CriteriaQuery<ExModelo> q = cb().createQuery(ExModelo.class);
		Root<ExModelo> c = q.from(ExModelo.class);
		q.select(c);
		List<Predicate> whereList = new LinkedList<Predicate>();
		if(flt.getSigla() != null) {
			whereList.add(cb().like(c.get("nmMod").as(String.class), "%" + flt.getSigla() + "%"));
		}
		Predicate[] whereArray = new Predicate[whereList.size()];
		whereList.toArray(whereArray);
		q.where(whereArray);
			q.orderBy(cb().desc(c.get("nmMod")));
		return em().createQuery(q).getResultList();
	}

	public int consultarQuantidade(final ExModeloDaoFiltro flt) {
		return consultarPorFiltro(flt).size();
	}

}