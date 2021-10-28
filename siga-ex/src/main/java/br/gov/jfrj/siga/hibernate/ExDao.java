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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.AbstractExMovimentacao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExDocumentoNumeracao;
import br.gov.jfrj.siga.ex.ExEmailNotificacao;
import br.gov.jfrj.siga.ex.ExEstadoDoc;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExItemDestinacao;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.ExSequencia;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.ex.bl.Mesa2.GrupoItem;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ext.IExMobilDaoFiltro;
import br.gov.jfrj.siga.hibernate.ext.IMontadorQuery;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.ExClassificacaoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExDocumentoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExMobilApiBuilder;
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
	
	
	/*****************************/
	public ExDocumentoNumeracao obterNumeroDocumento(Long idOrgaoUsu, Long idFormaDoc, Long anoEmissao, Boolean lock )
			throws SQLException {
	    
		final Query query = em().createNamedQuery("ExDocumentoNumeracao.obterDocumentoNumeracao");
		query.setParameter("idOrgaoUsu", idOrgaoUsu);
		query.setParameter("idFormaDoc", idFormaDoc);
		query.setParameter("anoEmissao", anoEmissao);
		query.setParameter("flAtivo", "1");
		
		if (lock) {
			query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
		}
		
		try {
			return (ExDocumentoNumeracao) query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
	}
	
	public Long obterNumeroGerado(Long idOrgaoUsu, Long idFormaDoc, Long anoEmissao)
			throws SQLException {
		Query query = em().createNamedQuery("ExDocumentoNumeracao.obterNumeroGerado");
		query.setParameter("idOrgaoUsu", idOrgaoUsu);
		query.setParameter("idFormaDoc", idFormaDoc);
		query.setParameter("anoEmissao", anoEmissao);
		query.setParameter("flAtivo", "1");
		return (Long) query.getSingleResult();
	}
	

	public void incrementNumeroDocumento(Long docNumeracao)
			throws SQLException {
		
		final Query query = em().createNamedQuery("ExDocumentoNumeracao.incrementaNumeroDocumento");
		query.setParameter("increment", 1L);
		query.setParameter("id", docNumeracao);
		
		query.executeUpdate();
		
	}
	
	public void insertNumeroDocumento(Long idOrgaoUsu, Long idFormaDoc, Long anoEmissao)
			throws SQLException {
	    
		final Query query = em().createNamedQuery("ExDocumentoNumeracao.insertRangeNumeroDocumento");
		query.setParameter("idOrgaoUsu", idOrgaoUsu);
		query.setParameter("idFormaDoc", idFormaDoc);
		query.setParameter("anoEmissao", anoEmissao);
		query.setParameter("nrDocumento", 1L);
		
		query.executeUpdate();

	}
	
	public Long existeRangeNumeroDocumento(Long idOrgaoUsu, Long idFormaDoc)
			throws SQLException {
	    
		final Query query = em().createNamedQuery("ExDocumentoNumeracao.existeRangeDocumentoNumeracao");
		query.setParameter("idOrgaoUsu", idOrgaoUsu);
		query.setParameter("idFormaDoc", idFormaDoc);
		query.setMaxResults(1);
		
		try {
			return (Long) query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
	}
	
	
	public void updateMantemRangeNumeroDocumento(Long docNumeracao)
			throws SQLException {
		
		final Query query = em().createNamedQuery("ExDocumentoNumeracao.mantemRangeNumeroDocumento");
		
		Calendar c = Calendar.getInstance();
		
		query.setParameter("anoEmissao", c.get(Calendar.YEAR));
		query.setParameter("flAtivo", 1);
		query.setParameter("increment", 1L);
		query.setParameter("id", docNumeracao);
		
		query.executeUpdate();
		
	}
	
	/*****************************/

	public ExSequencia obterSequencia(Integer tipoSequencia, Long anoEmissao, Boolean lock )
			throws SQLException {
	    
		final Query query = em().createNamedQuery("ExSequencia.obterSequencia");
		query.setParameter("tipoSequencia", tipoSequencia);
		query.setParameter("anoEmissao", anoEmissao);
		query.setParameter("flAtivo", "1");
		
		if (lock) {
			query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
		}
		
		try {
			return (ExSequencia) query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
	}
	
	public Long obterNumeroGerado(Integer tipoSequencia, Long anoEmissao)
			throws SQLException {
	    
		final Query query = em().createNamedQuery("ExSequencia.obterNumeroGerado");
		query.setParameter("tipoSequencia", tipoSequencia);
		query.setParameter("anoEmissao", anoEmissao);
		query.setParameter("flAtivo", "1");
		
		try {
			return (Long) query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
	}
	
	public void incrementNumero(Long idSeq)
			throws SQLException {
		
		final Query query = em().createNamedQuery("ExSequencia.incrementaSequencia");
		query.setParameter("increment", 1L);
		query.setParameter("id", idSeq);
		
		query.executeUpdate();
		
	}
	
	public ExSequencia existeRangeSequencia(Integer tipoSequencia)
			throws SQLException {
	    
		final Query query = em().createNamedQuery("ExSequencia.existeRangeSequencia");
		query.setParameter("tipoSequencia", tipoSequencia);
		query.setMaxResults(1);		
		try {
			return (ExSequencia) query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
	}
	
	public void updateMantemRangeSequencia(Long idSeq)
			throws SQLException {
		
		final Query query = em().createNamedQuery("ExSequencia.mantemRangeNumero");
		
		Calendar c = Calendar.getInstance();
		
		query.setParameter("anoEmissao", c.get(Calendar.YEAR));
		query.setParameter("flAtivo", "1");
		query.setParameter("increment", 1L);
		query.setParameter("id", idSeq);
		
		query.executeUpdate();
		
	}
	
	 	/*
		 * Adicionado dia 23/01/2020
		 */
		public ExProtocolo obterProtocoloPorNumero(Long numero, Integer ano) {
			final Query query = em().createNamedQuery("ExProtocolo.obterProtocoloPorNumeroAno");
			query.setParameter("numero", numero);
			query.setParameter("ano", ano);
			return (ExProtocolo) query.getSingleResult();
		}

		public ExProtocolo obterProtocoloPorCodigo(String codigo) {
			
			CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
			CriteriaQuery<ExProtocolo> criteriaQuery = criteriaBuilder.createQuery(ExProtocolo.class);	
			Root<ExProtocolo> cpExProtocoloRoot = criteriaQuery.from(ExProtocolo.class);

			Predicate predicateAnd;
			Predicate predicateEqualTipoMarcador  = criteriaBuilder.equal(cpExProtocoloRoot.get("codigo"), codigo);
			
			predicateAnd = criteriaBuilder.and(predicateEqualTipoMarcador);
			criteriaQuery.where(predicateAnd);
			
			List<ExProtocolo> l = em().createQuery(criteriaQuery).getResultList(); 
			if(l.size() > 0) {
				return l.get(0);
			} else {
				return null;
			}			
		}

		public List<ExMobil> consultarMobilPorDocumento(ExDocumento doc){
			
			CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
			CriteriaQuery<ExMobil> criteriaQuery = criteriaBuilder.createQuery(ExMobil.class);	
			Root<ExMobil> cpExMobilRoot = criteriaQuery.from(ExMobil.class);

			Predicate predicateAnd;
			Predicate predicateEqualTipoMarcador  = criteriaBuilder.equal(cpExMobilRoot.get("exDocumento"), doc);
			
			predicateAnd = criteriaBuilder.and(predicateEqualTipoMarcador);
			criteriaQuery.where(predicateAnd);
			return em().createQuery(criteriaQuery).getResultList();	
		}
		
		public List<ExMovimentacao> consultarMovimentoPorMobil(ExMobil mob){
			CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
			CriteriaQuery<ExMovimentacao> criteriaQuery = criteriaBuilder.createQuery(ExMovimentacao.class);	
			Root<ExMovimentacao> cpExMovimentacaoRoot = criteriaQuery.from(ExMovimentacao.class);
			Predicate predicateAnd;
			Predicate predicateEqualTipoMarcador  = criteriaBuilder.equal(cpExMovimentacaoRoot.get("exMobil"), mob);
			
			predicateAnd = criteriaBuilder.and(predicateEqualTipoMarcador);
			criteriaQuery.where(predicateAnd);
			return em().createQuery(criteriaQuery).getResultList();	
		}
		
		public List<ExMovimentacao> consultarMovimentoIncluindoJuntadaPorMobils(List<ExMobil> mobils){			
			CriteriaBuilder builder = em().getCriteriaBuilder();
			CriteriaQuery<ExMovimentacao> query = builder.createQuery(ExMovimentacao.class);	
			Root<ExMovimentacao> root = query.from(ExMovimentacao.class);
			
			Predicate predicate, predicateMobilIgnorandoMovimentacaoDeJuntada, predicateMobilRefComoMovimentacaoDeJuntadaEDesentranhamento;
			Expression<Long> mobil = root.get("exMobil");
			Expression<Long> mobilRef = root.get("exMobilRef");																		
			Join<ExMovimentacao, ExTipoMovimentacao> joinTipoMovimentacao = root.join("exTipoMovimentacao");
			
			predicateMobilRefComoMovimentacaoDeJuntadaEDesentranhamento = builder.and(mobilRef.in(mobils),
					builder.or(builder.equal(root.get("exTipoMovimentacao"), ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA),
							builder.equal(root.get("exTipoMovimentacao"), ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)),
					builder.isNull(root.get("exMovimentacaoCanceladora"))
					);
			
			predicate = builder.or(mobil.in(mobils), predicateMobilRefComoMovimentacaoDeJuntadaEDesentranhamento);
			
			query.where(predicate)
				.orderBy(builder.desc(root.get("dtTimestamp")));
			
			return em().createQuery(query).getResultList();	
		}
		
		public String gerarCodigoProtocolo() {
			boolean existe = true;		
			String codigo = "";
			while(existe) {
				codigo = randomAlfanumerico(10);
				if(obterProtocoloPorCodigo(codigo) == null)
					existe = false;
			}			
			return codigo;
		}
		
		/*
		 * Funcao para geracao de codigos alfanumericos randomicos
		 * recebendo apenas a quantidade de caracteres que o codigo deve conter
		 */
		public static String randomAlfanumerico(int contador) {
			final String STRING_ALFANUMERICA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			StringBuilder sb = new StringBuilder();
			while (contador-- != 0) {	
				int caracteres = (int)(Math.random()*STRING_ALFANUMERICA.length());	
				sb.append(STRING_ALFANUMERICA.charAt(caracteres));	
			}	
			return sb.toString();
		}
		
		
		/*
		 * Fim da adicao
		 */

		public ExProtocolo obterProtocoloPorDocumento(ExDocumento doc) throws SQLException {


			CriteriaQuery<ExProtocolo> q = cb().createQuery(ExProtocolo.class);
			Root<ExProtocolo> c = q.from(ExProtocolo.class);
			q.where(cb().equal(c.get("exDocumento"), doc));
			q.select(c);
			List<ExProtocolo> l = em().createQuery(q).getResultList();
			if(l.size() > 0)
				return l.get(0);
			else 
				return null;
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
		if (flt.getUltMovIdEstadoDoc() != null	&& flt.getUltMovIdEstadoDoc() != 0) {

//			query.setParameter("ultMovIdEstadoDoc", flt.getUltMovIdEstadoDoc());
			CpMarcador marcador = ExDao.getInstance().consultar(flt.getUltMovIdEstadoDoc(), CpMarcador.class, false);
			query.setParameter("idMarcadorIni", marcador.getHisIdIni());
			query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());

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
			query.setParameter("idTpDoc", new Long(flt.getIdTpDoc()));
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
			query.setParameter("dtDoc",flt.getDtDoc());
		}

		if (flt.getDtDocFinal() != null) {
			query.setParameter("dtDocFinal", flt.getDtDocFinal());
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
		List<Object[]> l2 = new ArrayList<Object[]>();
		Query query = null;
		
		if(itemPagina > 0) {
			query = em().createQuery(
					montadorQuery.montaQueryConsultaporFiltro(flt, false));
			preencherParametros(flt, query);
	
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			List l = query.getResultList();
			 
			if (l != null && l.size() > 0) {
				query = em().createQuery("select doc, mob, label from ExMarca label"
						+ " inner join label.exMobil mob inner join mob.exDocumento doc"
						+ " where label.idMarca in (:listIdMarca)");
				query.setParameter("listIdMarca", l);
				l2 = query.getResultList();
				Collections.sort(l2, Comparator.comparing( item -> l.indexOf(
					    		Long.valueOf (((ExMarca) (item[2])).getIdMarca()))));
			}
		} else { //exportar excel
			flt.setOrdem(-1);
			query = em().createQuery("select doc, mob, label from ExMarca label"
					+ " inner join label.exMobil mob inner join mob.exDocumento doc"
					+ " where label.idMarca in ("+montadorQuery.montaQueryConsultaporFiltro(flt, false)+")");
			preencherParametros(flt, query);
			l2 = query.getResultList();
		}
		long tempoTotal = System.nanoTime() - tempoIni;
		
		if (Prop.getBool("limita.acesso.documentos.por.configuracao")) {
		
			Iterator<Object[]> listaObjetos = l2.iterator();
			while (listaObjetos.hasNext()) {
				   Object[] objeto = listaObjetos.next(); // must be called before you can call i.remove()
				   ExDocumento doc = ((ExDocumento) objeto[0]);
				   if (! ExBL.exibirQuemTemAcessoDocumentosLimitados(doc, titular, lotaTitular))
				   		listaObjetos.remove();
			}
		
		}

		// System.out.println("consultarPorFiltroOtimizado: " +
		// tempoTotal/1000000 + " ms -> " + query + ", resultado: " + l)RExRR;
		return l2;
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
				
				try {
					return d.getMobilGeral();
				} catch (Exception e2) {
					throw new AplicacaoException("Não foi possível localizar o documento");
				}
				
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
		q.orderBy(cb().asc(c.get("siglaFormaDoc")));
		return em().createQuery(q).getResultList();
	}

	public List<ExFormaDocumento> listarPorEspecieOrdenarPorSigla(ExTipoFormaDoc tpFormaDoc) {
		CriteriaQuery<ExFormaDocumento> q = cb().createQuery(ExFormaDocumento.class);
		Root<ExFormaDocumento> c = q.from(ExFormaDocumento.class);
		q.select(c);
		if (tpFormaDoc != null)
			q.where(cb().equal(c.get("exTipoFormaDoc"), tpFormaDoc));
		q.orderBy(cb().asc(c.get("siglaFormaDoc")));
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

			query.setParameter("idLotacaoIni", lot.getIdInicial());
		}

		return query.getResultList();
	}

	public List consultarPaginaInicial(DpPessoa pes, DpLotacao lot,
			Integer idTipoForma) {
		try {
			Query sql = em().createNamedQuery(
					"consultarPaginaInicial");
			
			Date dt = super.consultarDataEHoraDoServidor();
			Date amanha = new Date(dt.getTime() + 24*60*60*1000L);
			sql.setParameter("amanha", amanha, TemporalType.DATE);
			sql.setParameter("idPessoaIni", pes.getIdPessoaIni());
			sql.setParameter("idLotacaoIni", lot.getIdLotacaoIni());
			sql.setParameter("idTipoForma", Long.valueOf(idTipoForma));

			List result = sql.getResultList();
			
			result.sort(new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					CpMarcadorFinalidadeEnum f1 = (CpMarcadorFinalidadeEnum) ((Object[]) o1)[4];
					CpMarcadorFinalidadeEnum f2 = (CpMarcadorFinalidadeEnum) ((Object[]) o2)[4];
					return f1.getGrupo().compareTo(f2.getGrupo());
				}

			});

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
			final Query query;
			if (exPreenchimento.getNomePreenchimento() != null) {
				query = em().createNamedQuery(
						"consultarPorFiltroExPreenchimento");
				query.setParameter("nomePreenchimento", exPreenchimento
						.getNomePreenchimento().toUpperCase().replace(' ', '%'));
			} else {
				query = em().createNamedQuery(
						"consultarPorLotacaoModeloExPreenchimento");
			}
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
	
	public int consultarQtdeLotacaoModeloNomeExPreenchimento(ExPreenchimento exPreenchimento) {
		try {
			final Query query;
			
			query = em().createNamedQuery(
					"consultarQtdeLotacaoModeloNomeExPreenchimento");
			
			if (exPreenchimento.getDpLotacao() != null)
				query.setParameter("lotacao", exPreenchimento.getDpLotacao().getIdLotacaoIni());
			else
				query.setParameter("lotacao", 0);
			if (exPreenchimento.getExModelo() != null)
				query.setParameter("modelo", exPreenchimento.getExModelo()
						.getHisIdIni());
			else
				query.setParameter("modelo", 0);
			
			if(exPreenchimento.getNomePreenchimento() != null && !"".equals(exPreenchimento.getNomePreenchimento())) {
				query.setParameter("nomePreenchimento", exPreenchimento.getNomePreenchimento());
			} else {
				query.setParameter("nomePreenchimento", exPreenchimento.getNomePreenchimento());
			}
			
			final int l = ((Long) query.getSingleResult()).intValue();
			
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	public List consultarAtivos() {
		final Query query = em().createNamedQuery("consultarAtivos");
		return query.getResultList();
	}

	public List<ExConfiguracao> consultar(final ExConfiguracao exemplo) {
		ITipoDeConfiguracao tpConf = exemplo.getCpTipoConfiguracao();
		CpOrgaoUsuario orgao = exemplo.getOrgaoUsuario();

		StringBuffer sbf = new StringBuffer();

		sbf.append("select * from siga.ex_configuracao ex inner join "
				+ "corporativo"
				+ ".cp_configuracao cp on ex.id_configuracao_ex = cp.id_configuracao ");

		sbf.append("" + "where 1 = 1");

		if (tpConf != null) {
			sbf.append(" and cp.id_tp_configuracao = ");
			sbf.append(exemplo.getCpTipoConfiguracao().getId());
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
		final Query query = em().createNamedQuery("consultarPorModelo");
		query.setParameter("idMod", mod.getHisIdIni());
		return (ExTpDocPublicacao) query.getResultList().get(0);
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
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
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
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
		return ((Long) query.getSingleResult()).intValue();
	}

	public List<ExItemDestinacao> consultarParaArquivarPermanenteEmLote(
			DpLotacao lot, int offset) {
		final Query query = em().createNamedQuery(
				"consultarParaArquivarPermanenteEmLote");
		query.setParameter("idOrgaoUsu", lot.getOrgaoUsuario().getIdOrgaoUsu());
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
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
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
		return ((Long) query.getSingleResult()).intValue();
	}

	public List<ExMobil> consultarParaTransferirEmLote(DpLotacao lot, Integer offset, Integer tamPagina) {
		final Query query = em().createNamedQuery("consultarParaTransferirEmLote").setParameter("lotaIni",
				lot.getIdLotacaoIni());
		if (Objects.nonNull(offset)) {
			query.setFirstResult(offset);
		}
		if (Objects.nonNull(tamPagina)) {
			query.setMaxResults(tamPagina);
		}

		return query.getResultList();
	}

	public Long consultarQuantidadeParaTransferirEmLote(DpLotacao lot) {
		return (Long) em().createNamedQuery("consultarQuantidadeParaTransferirEmLote", Long.class)
				.setParameter("lotaIni", lot.getIdLotacaoIni()).getSingleResult();
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
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
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
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
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
							"select m from ExModelo m where m.hisAtivo = 1"
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
		q.where(whereList.toArray(new Predicate[0]));
		try {
			return em().createQuery(q).getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
	}

	public ExFormaDocumento consultarExFormaPorId(Long idFormaDoc) {
		CriteriaQuery<ExFormaDocumento> q = cb().createQuery(ExFormaDocumento.class);
		Root<ExFormaDocumento> c = q.from(ExFormaDocumento.class);
		q.select(c);
		
		q.where(cb().equal(c.get("idFormaDoc"), idFormaDoc));
		return (ExFormaDocumento) em().createQuery(q).getSingleResult();
	}	
	
	public ExFormaDocumento consultarExForma(String sForma) {
		CriteriaQuery<ExFormaDocumento> q = cb().createQuery(ExFormaDocumento.class);
		Root<ExFormaDocumento> c = q.from(ExFormaDocumento.class);
		q.select(c);
		q.where(cb().equal(c.get("descrFormaDoc"), sForma));
		return em().createQuery(q).getSingleResult();
	}
	
	public boolean isExFormaComDocumentoVinculado(Long idFormaDoc) {	   
        Query query = em().createQuery("SELECT DISTINCT forma.idFormaDoc FROM ExFormaDocumento forma JOIN ExDocumento doc ON forma.idFormaDoc = doc.exFormaDocumento.idFormaDoc WHERE forma.idFormaDoc = :idFormaDoc")
        		.setParameter("idFormaDoc", idFormaDoc);
        
        Long idFormaDocEncontrado = (Long) query.getResultStream().findFirst().orElse(0L);
        
        return idFormaDocEncontrado > 0L;	    
	}

	public ExTipoDocumento consultarExTipoDocumento(String descricao) {
		CriteriaQuery<ExTipoDocumento> q = cb().createQuery(ExTipoDocumento.class);
		Root<ExTipoDocumento> c = q.from(ExTipoDocumento.class);
		q.select(c);
		q.where(cb().equal(c.get("descrTipoDocumento"), descricao));
		return em().createQuery(q).getSingleResult();
	}

	public ExNivelAcesso consultarExNidelAcesso(String nome) {
		CriteriaQuery<ExNivelAcesso> q = cb().createQuery(ExNivelAcesso.class);
		Root<ExNivelAcesso> c = q.from(ExNivelAcesso.class);
		q.select(c);
		q.where(cb().equal(c.get("nmNivelAcesso"), nome));
		return em().createQuery(q).getSingleResult();
	}

	public ExModelo consultarModeloAtual(ExModelo mod) {
		final Query query = em().createNamedQuery("consultarModeloAtual");

		query.setParameter("hisIdIni", mod.getHisIdIni());
		try {
			return (ExModelo) query.getSingleResult();
		} catch (NoResultException ne) {
			return null;
		}
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
	
	public ExDocumento consultarExDocumentoPorId(
			Long idDoc) {
		Query q;

			q = em().createNamedQuery(
					"consultarExDocumentoId");
			q.setParameter("idDoc", idDoc);
	
		return (ExDocumento) q.getSingleResult();
	}	

	public List<ExPapel> listarExPapeis() {
		return findByCriteria(ExPapel.class);
	}

//	public List<CpMarcador> listarCpMarcadoresGerais() {
//		CpTipoMarcadorEnum marcador = CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL;
//		
//		CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
//		CriteriaQuery<CpMarcador> criteriaQuery = criteriaBuilder.createQuery(CpMarcador.class);
//		Root<CpMarcador> cpMarcadorRoot = criteriaQuery.from(CpMarcador.class);
//		Predicate predicateAnd;
//		Predicate predicateEqualTipoMarcador  = criteriaBuilder.equal(cpMarcadorRoot.get("cpTipoMarcador"), marcador);
//		if("GOVSP".equals(Prop.get("/siga.local"))) {
//
//			Predicate predicateNotEqualTipoMarcador1  = criteriaBuilder.notEqual(cpMarcadorRoot.get("idMarcador"), MarcadorEnum.COMO_REVISOR.getId());
//			Predicate predicateNotEqualTipoMarcador2  = criteriaBuilder.notEqual(cpMarcadorRoot.get("idMarcador"), MarcadorEnum.PRONTO_PARA_ASSINAR.getId());
//			predicateAnd = criteriaBuilder.and(predicateEqualTipoMarcador,predicateNotEqualTipoMarcador1,predicateNotEqualTipoMarcador2);
//			/*			return findByCriteria(CpMarcador.class,
//					Restrictions.and(
//							Restrictions.eq("cpTipoMarcador", marcador),
//							Restrictions.ne("idMarcador", MarcadorEnum.COMO_REVISOR.getId()), 
//							Restrictions.ne("idMarcador", MarcadorEnum.PRONTO_PARA_ASSINAR.getId()))); */
//		} else {
//			predicateAnd = criteriaBuilder.and(predicateEqualTipoMarcador);
//
//	/*		return findByCriteria(CpMarcador.class,
//					Restrictions.eq("cpTipoMarcador", marcador)); */
//		}
//		
//		criteriaQuery.where(predicateAnd);
//		List<CpMarcador> resultList = em().createQuery(criteriaQuery).getResultList();
//		resultList.sort(CpMarcador.ORDEM_COMPARATOR);
//		resultList.removeIf(m -> m.getHisDtFim() != null);
//		return resultList;
//	}
//	
//	
//
//	public List<CpMarcador> listarCpMarcadoresDaLotacao(DpLotacao lot) {
//		DpLotacao lotIni = lot.getLotacaoInicial();
//		CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
//		CriteriaQuery<CpMarcador> criteriaQuery = criteriaBuilder.createQuery(CpMarcador.class);
//		Root<CpMarcador> cpMarcadorRoot = criteriaQuery.from(CpMarcador.class);
//		Predicate predicateAnd;
//		Predicate predicateEqualTipoMarcador  = criteriaBuilder.equal(cpMarcadorRoot.get("cpTipoMarcador"), CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO);
//		Predicate predicateEqualLotacaoIni  = criteriaBuilder.equal(cpMarcadorRoot.get("dpLotacaoIni"), lotIni);
//		
//		predicateAnd = criteriaBuilder.and(predicateEqualTipoMarcador, predicateEqualLotacaoIni);
//		
//		criteriaQuery.where(predicateAnd);
//		return em().createQuery(criteriaQuery).getResultList();
//	}
	
	public List<CpMarcador> listarCpMarcadoresDisponiveis(DpLotacao lot) {
		List<CpMarcador> listaConcatenada = listarCpMarcadoresGerais(true);
		List<CpMarcador> listaLotacao = listarCpMarcadoresPorLotacao(lot, true);
		
		if (listaLotacao != null) {
			listaConcatenada.addAll(listaLotacao);	
		}
		
		listaConcatenada.sort(CpMarcador.ORDEM_COMPARATOR);
		listaConcatenada.removeIf(m -> m.getHisDtFim() != null);

		return listaConcatenada;
		
		
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
		q.where(cb().equal(c.get("exClassificacao"), cl));
		q.orderBy(cb().asc(c.get("idDoc")));
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
		String q = "select marca, marcador, mobil from ExMarca marca"
				+ " inner join marca.cpMarcador marcador"
				+ " inner join marca.exMobil mobil"
				+ " where (marca.dtIniMarca is null or marca.dtIniMarca < :dbDatetime)"
				+ " and (marca.dtFimMarca is null or marca.dtFimMarca > :dbDatetime)"
				+ " and (marca.dpPessoaIni.idPessoa = :titular or "
				+ (titular != null? " (marca.dpPessoaIni.idPessoa = null and ": "(")
				+ " marca.dpLotacaoIni.idLotacao = :lotaTitular))";
		if(Prop.isGovSP()) {
			q += " and ((mobil.exDocumento.numExpediente is null and marcador = 1) or (mobil.exDocumento.numExpediente is not null))"
					+ " and marcador <> 10";
		}
		long tempoIni = System.nanoTime();
		Query query = em()
				.createQuery(
						q);

		if (titular != null)
			query.setParameter("titular", titular.getIdPessoaIni());
		else
			query.setParameter("titular", null);
		if (lotaTitular != null)
			query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());
		else
			query.setParameter("lotaTitular", null);

		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
		List l = query.getResultList();
 		long tempoTotal = System.nanoTime() - tempoIni;
		// System.out.println("consultarPorFiltroOtimizado: " + tempoTotal
		// / 1000000 + " ms -> " + query + ", resultado: " + l);
		return l;
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
			Expression<String> path = c.get("nmMod");
			path = cb().upper(path);
			whereList.add(cb().like(path, "%" + flt.getSigla() + "%"));
			whereList.add(cb().equal(c.get("hisAtivo"), 1));
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

	public List listarDocumentosCxEntradaPorPessoaOuLotacao(DpPessoa titular,
			DpLotacao lotaTitular) {

		long tempoIni = System.nanoTime();
		Query query = em()
				.createQuery(
						"select marca, marcador, mobil from ExMarca marca"
								+ " inner join marca.cpMarcador marcador"
								+ " inner join marca.exMobil mobil"
								+ " where (marca.dtIniMarca is null or marca.dtIniMarca < :dbDatetime)"
								+ " and (marca.dtFimMarca is null or marca.dtFimMarca > :dbDatetime)"
								+ " and (marca.cpMarcador.idMarcador = 14L)"
								+ (titular != null ? " and (marca.dpPessoaIni.idPessoaIni = :titular)"
										: " and (marca.dpLotacaoIni.idLotacaoIni = :lotaTitular)"));
		if (titular != null)
			query.setParameter("titular", titular.getIdPessoaIni());
		else if (lotaTitular != null)
			query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());
        
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
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

	public List consultarTotaisPorMarcador(DpPessoa pes, DpLotacao lot, List<GrupoItem> grupos, 
			boolean exibeLotacao, List<Integer> marcasAIgnorar) {
		try {
//			long tempoIni = System.nanoTime();
			String query = "";
			// Não conta os documentos que tiverem marcas na lista marcasAIgnorar
			String queryMarcasAIgnorar = marcasAIgnorar.toString().replaceAll("\\[|\\]", "");
			String queryMarcasAIgnorarFinal = "";
			int i = 0;
			
			// Para cada grupo solicitado, gera a query para contagem
			for (GrupoItem grupoItem : grupos) {
				i++;
				if (!grupoItem.grupoHide && grupoItem.grupoMarcadores.size() > 0) {
					queryMarcasAIgnorarFinal = queryMarcasAIgnorar;
					// Se for do grupo AGUARDANDO_ANDAMENTO, nao conta se tiver marca do grupo CAIXA_DE_ENTRADA
					if(grupoItem.grupoNome.equals(CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO.getNome())) {
						queryMarcasAIgnorarFinal = queryMarcasAIgnorarFinal 
							+ ( "".equals(queryMarcasAIgnorarFinal) ? "" : ", ") 
							+ String.valueOf(CpMarcadorEnum.CAIXA_DE_ENTRADA.getId())
							+ ", " + String.valueOf(CpMarcadorEnum.A_RECEBER.getId())
							+ ", " + String.valueOf(CpMarcadorEnum.EM_TRANSITO.getId())
							+ ", " + String.valueOf(CpMarcadorEnum.SOLICITACAO_A_RECEBER.getId());
					}
					query += "SELECT "
						+ grupoItem.grupoOrdem + ", "
						+ " SUM(CASE WHEN cont_pessoa > 0 THEN 1 ELSE 0 END), " 
						+ " SUM(CASE WHEN cont_lota > 0 THEN 1 ELSE 0 END) "
						+ " FROM (SELECT distinct marca.id_ref, "
						 + " SUM(CASE WHEN marca.id_pessoa_ini = :idPessoaIni THEN 1 ELSE 0 END) cont_pessoa,"
						 + " SUM(CASE WHEN marca.id_lotacao_ini = :idLotacaoIni THEN 1 ELSE 0 END) cont_lota"
						 + " FROM corporativo.cp_marca marca"
						 + " INNER JOIN siga.ex_mobil mob on marca.id_ref = mob.id_mobil"
						 + " INNER JOIN corporativo.cp_marcador marcador ON marca.id_marcador = marcador.id_marcador"
						 + " LEFT OUTER JOIN corporativo.cp_marca marca2 ON "
						  + " marca2.id_ref = marca.id_ref "
						  + " AND (marca2.dt_ini_marca IS NULL OR marca2.dt_ini_marca < :dbDatetime)"
						  + " AND (marca2.dt_fim_marca IS NULL OR marca2.dt_fim_marca > :dbDatetime)"
						  + " AND ((marcador.GRUPO_MARCADOR <> " + String.valueOf(CpMarcadorGrupoEnum.EM_ELABORACAO.getId()) 
						  	+ " AND marca2.ID_MARCADOR = " + String.valueOf(CpMarcadorEnum.EM_ELABORACAO.getId()) + ") "
						  	+ ( "".equals(queryMarcasAIgnorarFinal) ? ")" : 
						  		"OR (marca.id_marcador <> " + String.valueOf(CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId()) + " and  marca2.id_marcador in (" + queryMarcasAIgnorarFinal + ")))" )
//  						  + (!grupoItem.grupoNome.equals(Mesa2.GrupoDeMarcadorEnum.EM_ELABORACAO.getNome())?
//  								  " OR marca2.id_marcador = " + String.valueOf(MarcadorEnum.EM_ELABORACAO.getId()) + ")" : ")")
						 + " WHERE (marca.dt_ini_marca IS NULL OR marca.dt_ini_marca < :dbDatetime)"
						  + " AND (marca.dt_fim_marca IS NULL OR marca.dt_fim_marca > :dbDatetime)"
						  + " AND ((marca.id_pessoa_ini = :idPessoaIni) OR (marca.id_lotacao_ini = :idLotacaoIni))"
						  + " AND marca.id_tp_marca = 1"
						  + " AND marcador.grupo_marcador = " + grupoItem.grupoOrdem 
						  + " AND marca.id_marcador <> :marcaAssinSenha "
						  + " AND marca.id_marcador <> :marcaMovAssinSenha "
						  + " AND marca2.id_marca IS null "
						 + " GROUP BY marca.id_ref ) t" + i 
//								+ " GROUP BY tab1.grupo_marcador ORDER BY tab1.grupo_marcador asc"
						+ " UNION ALL ";
				}
			}
			List<Object[]> result = new ArrayList<Object[]>();
			if (query.length() > 0) { 
				query = query.substring(0, query.length() - 10);

				Query sql = em().createNativeQuery(query);
				sql.setParameter("idPessoaIni", pes.getIdPessoaIni());
				sql.setParameter("idLotacaoIni", lot.getIdLotacaoIni());
				sql.setParameter("marcaAssinSenha", CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId());
				sql.setParameter("marcaMovAssinSenha", CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId());
				sql.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
				
				result = sql.getResultList();
			}

			return result;

		} catch (final NullPointerException e) {
			return null;
		}
	}

	public List listarMobilsPorMarcas(DpPessoa titular,	DpLotacao lotaTitular, boolean exibeLotacao, 
			boolean ordemCrescenteData, List<Integer> marcasAIgnorar) {
		String queryString;
		String queryMarcasAIgnorar = "";
		String queryMarcasAIgnorarWhere = "";
		String ordem = " DESC ";
		if (ordemCrescenteData) {
			ordem = " ASC ";
		}
		if (marcasAIgnorar != null && marcasAIgnorar.size() > 0) {
			queryMarcasAIgnorar += " left join ExMarca marca2 on "
					+ " marca2.exMobil = marca.exMobil AND (";
			for (Integer marcaAIgnorar : marcasAIgnorar) {
				queryMarcasAIgnorar += " marca2.cpMarcador.idMarcador = " + marcaAIgnorar.toString() + " OR"; 
			}
			queryMarcasAIgnorar = queryMarcasAIgnorar.substring(0, queryMarcasAIgnorar.length() - 2) + ") ";
			queryMarcasAIgnorarWhere = " and marca2 is null ";
		}		
		List<List<String>> l = new ArrayList<List<String>> ();
//		long tempoIni = System.nanoTime();
		queryString =
					"select "
					+ " marca, marcador, mobil, doc.dtAltDoc, doc.numExpediente, mov.dtParam1, mov.dtParam2 "
					+ " from ExMarca marca "
					+ " inner join marca.exMobil mobil"
					+ " inner join marca.cpMarcador marcador"
					+ " inner join mobil.exDocumento doc"
					+ " left join marca.exMovimentacao mov"
					+ queryMarcasAIgnorar
					+ " where (marca.dtIniMarca is null or marca.dtIniMarca < :dbDatetime)"
					+ " and (marca.dtFimMarca is null or marca.dtFimMarca > :dbDatetime)"
					+ (!exibeLotacao && titular != null ? " and (marca.dpPessoaIni.idPessoaIni = :titular)" : "") 
					+ (exibeLotacao && lotaTitular != null ? " and (marca.dpLotacaoIni.idLotacaoIni = :lotaTitular)" : "")
					+ queryMarcasAIgnorarWhere
					+ " AND marca.cpMarcador.idMarcador <> :marcaAssinSenha "
					+ " AND marca.cpMarcador.idMarcador <> :marcaMovAssinSenha "
					+ " order by  doc.dtAltDoc " + ordem + ", marca ";
			
		Query query = em()
				.createQuery(queryString);
		if (!exibeLotacao && titular != null)
			query.setParameter("titular", titular.getIdPessoaIni());
		
		if (exibeLotacao && lotaTitular != null)
			query.setParameter("lotaTitular", lotaTitular.getIdLotacaoIni());

		query.setParameter("marcaAssinSenha", CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId());
		query.setParameter("marcaMovAssinSenha", CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId());
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
		l = query.getResultList();
//		long tempoTotal = System.nanoTime() - tempoIni;
//		System.out.println("listarMobilsPorMarcas: " + tempoTotal
//		/ 1000000 + " ms ==> " + query);
		return l;
	}
	
	public List listarMovimentacoesMesa(List<Long> listIdMobil, boolean trazerComposto) {
//		long tempoIni = System.nanoTime();
		List<List<String>> l = new ArrayList<List<String>> ();
		Query query = em()
				.createQuery(
						"select "
						+ "mob, "
						+ (trazerComposto ? " frm.isComposto, " : "0, ")
						+ "(select movUltima from ExMovimentacao movUltima "
						+ " where movUltima.exMobil.idMobil = mob.idMobil and movUltima.dtTimestamp = ("
						+ " 	select max(movUltima1.dtTimestamp) from ExMovimentacao movUltima1"
						+ " 		where movUltima1.exMobil.idMobil = mob.idMobil " 
						+ " 		and movUltima1.exMovimentacaoCanceladora.idMov = null ) ), "
						+ "(select movTramite from ExMovimentacao movTramite"
						+ " where movTramite.exMobil.idMobil = mob.idMobil and movTramite.dtTimestamp = ("
						+ " 	select max(movTramite1.dtTimestamp) from ExMovimentacao movTramite1"
						+ " 		where movTramite1.exTipoMovimentacao.idTpMov = 3 "
						+ "			and movTramite1.exMobil.idMobil = mob.idMobil " 
						+ " 		and movTramite1.exMovimentacaoCanceladora.idMov = null ) ), "
						+ "doc "
						+ "from ExMobil mob "
						+ "inner join mob.exDocumento doc "
						+ (trazerComposto ? "inner join doc.exFormaDocumento frm " : "")
						+ "where mob.idMobil in (:listIdMobil) "
				);
		if (listIdMobil != null) {
			query.setParameter("listIdMobil", listIdMobil);
		}
		l = query.getResultList();
//		long tempoTotal = System.nanoTime() - tempoIni;
//		System.out.println("listarMobilsPorMarcas: " + tempoTotal
//		/ 1000000 + " ms ==> " + query);
		
		return l;
	}

	/**
	 * Realiza a consulta das {@link ExMovimentacao Movimentações} para o histórico
	 * de tramitações de uma {@link ExMobil} relacionada a um determinado
	 * {@link ExDocumento Documento} em ordem cronológica decrescente (
	 * {@link ExMovimentacao#getDtTimestamp()}) a partir da primeira
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA tramitação} das
	 * {@link ExMobil}s do Documento. As movimentações retornadas devm ser dos
	 * seguintes {@link ExMovimentacao#getExTipoMovimentacao() Tipos}:
	 * <ul>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA }
	 * (Tramitação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_JUNTADA } (Juntada)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE }
	 * (Arquivamento Corrente)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO }
	 * (Arquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE }
	 * (Desarquivamento)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO }
	 * (Desarquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO }
	 * (Cancelamento de Movimentação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO }
	 * (Cancelamento)</li>
	 * </ul>
	 * As movimentações do tipo
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO } não serão exibidas.
	 * Elas são apenas usadas para indicar a hora de recebimento da Movimentação de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA } imediatamente
	 * anterior.
	 * 
	 * @param idMobil ID da Mobilização
	 * @return As Movimentações dos tipos relacionados acima.
	 */
	public List<ExMovimentacao> consultarTramitacoesPorMovimentacao(Long idMobil) {
		return em() //
				.createNamedQuery(AbstractExMovimentacao.CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_NAMED_QUERY,
						ExMovimentacao.class)
				.setParameter("idMobil", idMobil) //
				.getResultList();
	}

	/**
	 * Realiza a consulta das {@link ExMovimentacao Movimentações} para o histórico
	 * de tramitações do {@link ExDocumento Documento} Cancelado de uma
	 * {@link ExMobil} relacionada a um determinado em ordem cronológica decrescente
	 * ( {@link ExMovimentacao#getDtTimestamp()}) a partir da primeira
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA tramitação} das
	 * {@link ExMobil}s do Documento. As movimentações retornadas devm ser dos
	 * seguintes {@link ExMovimentacao#getExTipoMovimentacao() Tipos}:
	 * <ul>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA }
	 * (Tramitação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_JUNTADA } (Juntada)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE }
	 * (Arquivamento Corrente)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO }
	 * (Arquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE }
	 * (Desarquivamento)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO }
	 * (Desarquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO }
	 * (Cancelamento de Movimentação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO }
	 * (Cancelamento)</li>
	 * </ul>
	 * As movimentações do tipo
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO } não serão exibidas.
	 * Elas são apenas usadas para indicar a hora de recebimento da Movimentação de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA } imediatamente
	 * anterior.
	 * 
	 * @param idMobil ID da Mobilização
	 * @return As Movimentações dos tipos relacionados acima.
	 */
	public List<ExMovimentacao> consultarTramitacoesPorMovimentacaoDocumentoCancelado(Long idMobil) {
		return em() //
				.createNamedQuery(
						AbstractExMovimentacao.CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_DOC_CANCELADO_NAMED_QUERY,
						ExMovimentacao.class)
				.setParameter("idMobil", idMobil) //
				.getResultList();
	}
	
	/**
	 * Conta o total de movimentações assinadas
	 * @param idDoc
	 * @return
	 */
	public Long contarMovimentacaoAssinada(Long idDoc) {
		return em().createQuery(
				"select count(m) from ExMovimentacao m "
				+ "left join m.exMobil mb "
				+ "where mb.exDocumento.idDoc = :idDoc "
				+ "and m.exMovimentacaoCanceladora is null "
				+ "and (m.exTipoMovimentacao.idTpMov = :idAssinatura or m.exTipoMovimentacao.idTpMov = :idAssinaturaSenha)", Long.class)
				.setParameter("idDoc", idDoc)
				.setParameter("idAssinatura", 11L)
				.setParameter("idAssinaturaSenha", 58L)
				.getSingleResult();
	}

	/**
	 * Pesquisa documentos por lista de marcadores, pessoas e lotações
	 * @param List<Long> idMarcadores
	 * @param List<Long> idPessoasIni, 
	 * @param List<Long> idLotacoesIni>
	 * @return List<Object[]>
	 */
	public List consultarPorFiltro(ExMobilApiBuilder flt) {
//		long tempoIni = System.nanoTime();
		String queryDt = "";
		String queryOrdenacao = "";
		if (flt.getDtDocIni() != null)
			if ((flt.getGrupoMarcador() != null 
					&& flt.getGrupoMarcador() == CpMarcadorGrupoEnum.EM_ELABORACAO) 
				|| (flt.getIdMarcador() != null 
					&& flt.getIdMarcador() == CpMarcadorEnum.EM_ELABORACAO.getId())) {
				queryDt = " and doc.dtRegDoc >= :dtDocIni ";
			} else {
				queryDt = " and doc.dtDoc >= :dtDocIni ";
			}

		if (flt.getDtDocFim() != null)
			if ((flt.getGrupoMarcador() != null 
					&& flt.getGrupoMarcador() == CpMarcadorGrupoEnum.EM_ELABORACAO) 
				|| (flt.getIdMarcador() != null 
					&& flt.getIdMarcador() == CpMarcadorEnum.EM_ELABORACAO.getId())) {
				queryDt = queryDt + " and doc.dtRegDoc <= :dtDocFim ";
			} else {
				queryDt = queryDt + " and doc.dtDoc <= :dtDocFim ";
			}

		switch ((int) flt.getOrdenacao().intValue()) {
			case 1:
				queryOrdenacao = " order by doc.dtDoc desc, doc.idDoc desc, marca.idMarca desc";
				break;
			case 2:
				queryOrdenacao = " order by doc.dtDoc asc, doc.idDoc asc, marca.idMarca asc";
				break;
			case 3:
				queryOrdenacao = " order by doc.dtFinalizacao desc, doc.idDoc desc, marca.idMarca desc";
				break;
			case 4:
				queryOrdenacao = " order by doc.dtFinalizacao asc, doc.idDoc asc, marca.idMarca asc";
				break;
			case 5:
				queryOrdenacao = " order by marca.dtIniMarca desc, doc.idDoc desc, marca.idMarca desc";
				break;
			case 6:
				queryOrdenacao = " order by marca.dtIniMarca asc, doc.idDoc asc, marca.idMarca asc";
				break;
			case 7:
				queryOrdenacao = " order by doc.idDoc desc, marca.idMarca desc";
				break;
			case 8:
				queryOrdenacao = " order by doc.idDoc asc, marca.idMarca asc";
				break;
		}		
		Query query = em().createQuery(
			"select marca, marcador, mobil, doc from ExMarca marca"
					+ " inner join marca.cpMarcador marcador"
					+ " inner join marca.exMobil mobil"
					+ " inner join mobil.exDocumento doc"
					+ " where (marca.dtIniMarca is null or marca.dtIniMarca < :dbDatetime)"
					+ " and (marca.dtFimMarca is null or marca.dtFimMarca > :dbDatetime)"
					+ queryDt
					+ (flt.getIdMarcador() != null ? " and marca.cpMarcador.idMarcador = :idMar " : "")
					+ (flt.getGrupoMarcador() != null ? " and marca.cpMarcador.idGrupo = :idGrupo " : "")
					+ (flt.getIdCadastrante() != null ? " and (marca.dpPessoaIni.idPessoaIni = :pesIni)" : "")
					+ (flt.getIdLotaCadastrante() != null ? " and (marca.dpLotacaoIni.idLotacaoIni = :lotaIni)" : "")
					+ queryOrdenacao
		);
		if (flt.getIdMarcador() != null)
			query.setParameter("idMar", flt.getIdMarcador());
		if (flt.getGrupoMarcador() != null)
			query.setParameter("idGrupo", flt.getGrupoMarcador());
		if (flt.getIdCadastrante() != null)
			query.setParameter("pesIni", flt.getIdCadastrante());
		if (flt.getIdLotaCadastrante() != null)
			query.setParameter("lotaIni", flt.getIdLotaCadastrante());
		if (flt.getDtDocIni() != null)
			query.setParameter("dtDocIni", flt.getDtDocIni());
		if (flt.getDtDocFim() != null)
			query.setParameter("dtDocFim", flt.getDtDocFim());
		query.setParameter("dbDatetime", this.consultarDataEHoraDoServidor());
		
		if (flt.getOffset() != null && flt.getOffset() > 0) {
			query.setFirstResult(flt.getOffset().intValue());
		}
		if (flt.getQtdMax() != null && flt.getQtdMax() > 0) {
			query.setMaxResults(flt.getQtdMax().intValue());
		}
		List l = query.getResultList();
		// long tempoTotal = System.nanoTime() - tempoIni;
		// System.out.println("consultarPorFiltroOtimizado: " + tempoTotal
		// 			/ 1000000 + " ms -> " + query + ", resultado: " + l);
		return l;
	}

	
	public ExModelo consultarModeloPeloNome(String nmMod) {
		final Query query = em().createNamedQuery("consultarModeloPeloNome");

		query.setParameter("nmMod", nmMod);
		try {
			List<ExModelo> modelos = query.getResultList(); 
			return modelos.get(0);
		} catch (Exception ne) {
			return null;
		}
	}
	
}
