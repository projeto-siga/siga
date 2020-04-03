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
package br.gov.jfrj.siga.dp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.DateUtils;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoGrupo;
import br.gov.jfrj.siga.cp.CpTipoPapel;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.cp.bl.SituacaoFuncionalEnum;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.cp.util.MatriculaUtils;
import br.gov.jfrj.siga.dp.CpAplicacaoFeriado;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpPersonalizacao;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.CpTipoPessoa;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpPessoaTrocaEmailDTO;
import br.gov.jfrj.siga.dp.DpPessoaUsuarioDTO;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.model.CarimboDeTempo;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltro;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class CpDao extends ModeloDao {

	public static final String CACHE_QUERY_SUBSTITUICAO = "querySubstituicao";
	public static final String CACHE_QUERY_CONFIGURACAO = "queryConfiguracao";
	public static final String CACHE_QUERY_SECONDS = "querySeconds";
	public static final String CACHE_QUERY_HOURS = "queryHours";
	public static final String CACHE_CORPORATIVO = "corporativo";
	public static final String CACHE_HOURS = "hours";
	public static final String CACHE_SECONDS = "seconds";
	

	private static Map<String, CpServico> cacheServicos = null;

	public static CpDao getInstance() {
		return ModeloDao.getInstance(CpDao.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgao> consultarPorFiltro(final CpOrgaoDaoFiltro o) {
		return consultarPorFiltro(o, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgao> consultarPorFiltro(final CpOrgaoDaoFiltro o,
			final int offset, final int itemPagina) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorFiltroCpOrgao");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			final List<CpOrgao> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgao> consultarCpOrgaoOrdenadoPorNome(Integer offset, Integer itemPagina) {
		try {
			final Query query = em().createNamedQuery(
					"consultarCpOrgaoOrdenadoPorNome");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			final List<CpOrgao> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}
	
	public int consultarQuantidadeOrgao() {
		try {
			final Query query = em().createNamedQuery(
					"consultarQuantidadeCpOrgaoTodos");
			
			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CpFeriado> listarCpFeriadoPorDescricao() {
		try {
			final Query query = em().createNamedQuery(
					"listarCpFeriadoOrdenadoPorDescricao");
			final List<CpFeriado> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public CpOrgao consultarPorSigla(final CpOrgao o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaCpOrgao");
		query.setParameter("siglaOrgao", o.getSiglaOrgao());

		final List<CpOrgao> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}
	
	public void inicializarCacheDeServicos() {
		synchronized (CpDao.class) {
			cacheServicos = new TreeMap<>();
			List<CpServico> l = listarTodos(CpServico.class, "siglaServico");
			for (CpServico s : l) {
				cacheServicos.put(s.getSigla(), s);
			}
		}
	}
	
	public CpServico acrescentarServico(CpServico srv) {
		synchronized (CpDao.class) {
			iniciarTransacao();
			CpServico srvGravado = gravar(srv);
			commitTransacao();
			cacheServicos.put(srv.getSigla(), srv);
			return srvGravado;
		}
	}

	@SuppressWarnings("unchecked")
	public CpServico consultarPorSigla(final CpServico o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaCpServico");
		query.setParameter("siglaServico", o.getSiglaServico());
		query.setParameter("idServicoPai", o.getCpServicoPai() == null ? 0L : o
				.getCpServicoPai().getIdServico());

		// Renato: Comentei a linha abaixo pois nao entendi porque foi feito
		// dessa forma.
		// query.setFlushMode(FlushMode.MANUAL);
		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

		final List<CpServico> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	public CpServico consultarCpServicoPorChave(String chave) {
		StringBuilder sb = new StringBuilder(50);
		boolean supress = false;
		boolean separator = false;
		for (int i = 0; i < chave.length(); i++) {
			final char ch = chave.charAt(i);
			if (ch == ';') {
				supress = false;
				separator = true;
				continue;
			}
			if (ch == ':') {
				supress = true;
				continue;
			}
			if (!supress) {
				if (separator) {
					sb.append('-');
					separator = false;
				}
				sb.append(ch);
			}
		}
		String sigla = sb.toString();

		if (cacheServicos == null) 
			inicializarCacheDeServicos();
		return cacheServicos.get(sigla);
	}

	public Selecionavel consultarPorSigla(final CpOrgaoDaoFiltro flt) {
		final CpOrgao o = new CpOrgao();
		o.setSigla(flt.getSigla());
		return consultarPorSigla(o);
	}

	// public int consultarQuantidade(final DaoFiltro o) {
	// if (o instanceof CpOrgaoDaoFiltro)
	// return consultarQuantidade((CpOrgaoDaoFiltro) o);
	// if (o instanceof CpOrgaoUsuarioDaoFiltro)
	// return consultarQuantidade((CpOrgaoUsuarioDaoFiltro) o);
	// if (o instanceof DpCargoDaoFiltro)
	// return consultarQuantidade((DpCargoDaoFiltro) o);
	// if (o instanceof DpFuncaoConfiancaDaoFiltro)
	// return consultarQuantidade((DpFuncaoConfiancaDaoFiltro) o);
	// if (o instanceof DpLotacaoDaoFiltro)
	// return consultarQuantidade((DpLotacaoDaoFiltro) o);
	// if (o instanceof DpPessoaDaoFiltro)
	// return consultarQuantidade((DpPessoaDaoFiltro) o);
	// return 0;
	// }

	public int consultarQuantidade(final DaoFiltro o) throws Exception,
			SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Class[] argType = { o.getClass() };
		return (Integer) this.getClass()
				.getMethod("consultarQuantidade", argType).invoke(this, o);
	}

	public Selecionavel consultarPorSigla(final DaoFiltro o) throws Exception,
			SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Class[] argType = { o.getClass() };
		return (Selecionavel) this.getClass()
				.getMethod("consultarPorSigla", argType).invoke(this, o);
	}

	public int consultarQuantidade(final CpOrgaoDaoFiltro o) {
		try {
			final Query query = em().createNamedQuery(
					"consultarQuantidadeCpOrgao");
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	public List consultarPorFiltro(final DaoFiltro o) throws Exception {
		return consultarPorFiltro(o, 0, 0);
	}

	public List consultarPorFiltro(final DaoFiltro o, final int offset,
			final int itemPagina) throws Exception {
		Class[] argType = { o.getClass(), Integer.TYPE, Integer.TYPE };
		return (List) this.getClass().getMethod("consultarPorFiltro", argType)
				.invoke(this, o, offset, itemPagina);
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgaoUsuario> consultarPorFiltro(
			final CpOrgaoUsuarioDaoFiltro o) {
		return consultarPorFiltro(o, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgaoUsuario> consultarPorFiltro(
			final CpOrgaoUsuarioDaoFiltro o, final int offset,
			final int itemPagina) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorFiltroCpOrgaoUsuario");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

			final List<CpOrgaoUsuario> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CpOrgaoUsuario> consultarPorFiltroComContrato(
			final CpOrgaoUsuarioDaoFiltro o, final int offset,
			final int itemPagina) {
		try {
			Query query = em()
					.createQuery("select org, (select dtContrato from CpContrato contrato "
							+ " where contrato.idOrgaoUsu = org.idOrgaoUsu) from CpOrgaoUsuario org "
							+ " where (upper(org.nmOrgaoUsu) like upper('%' || :nome || '%'))"
							+ "	order by org.nmOrgaoUsu");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

            query.setHint("org.hibernate.cacheable", true);
            query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);
            final List<CpOrgaoUsuario> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
    public CpOrgaoUsuario consultarPorId(final CpOrgaoUsuario o) {
        final Query query = em().createNamedQuery(
                "consultarIdOrgaoUsuario");
        query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());

        query.setHint("org.hibernate.cacheable", true);
        query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

        final List<CpOrgaoUsuario> l = query.getResultList();
        if (l.size() != 1)
            return null;
        return l.get(0);
    }

	@SuppressWarnings("unchecked")
	public CpOrgaoUsuario consultarPorSigla(final CpOrgaoUsuario o) {
		final Query query = em().createNamedQuery(
				"consultarSiglaOrgaoUsuario");
		query.setParameter("sigla", o.getSiglaOrgaoUsu());

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

		final List<CpOrgaoUsuario> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public CpOrgaoUsuario consultarPorNome(final CpOrgaoUsuario o) {
		final Query query = em().createNamedQuery(
				"consultarNomeOrgaoUsuario");
		query.setParameter("nome", o.getNmOrgaoUsu());

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

		final List<CpOrgaoUsuario> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public Selecionavel consultarPorSigla(final CpOrgaoUsuarioDaoFiltro flt) {
		final CpOrgaoUsuario o = new CpOrgaoUsuario();
		o.setSigla(flt.getSigla());
		return consultarPorSigla(o);
	}

	public int consultarQuantidade(final CpOrgaoUsuarioDaoFiltro o) {
		try {
			final Query query = em().createNamedQuery(
					"consultarQuantidadeCpOrgaoUsuario");
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DpCargo> consultarPorFiltro(final DpCargoDaoFiltro o) {
		return consultarPorFiltro(o, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<DpCargo> consultarPorFiltro(final DpCargoDaoFiltro o,
			final int offset, final int itemPagina) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorFiltroDpCargo");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			if (o.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0);

			final List<DpCargo> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public DpCargo consultarPorSigla(final DpCargo o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaDpCargo");
		query.setParameter("siglaCargo", o.getSiglaCargo());

		final List<DpCargo> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public DpCargo consultarPorNomeOrgao(final DpCargo o) {
		final Query query = em().createNamedQuery(
				"consultarPorNomeDpCargoOrgao");
		query.setParameter("nome", o.getNomeCargo());
		query.setParameter("idOrgaoUsuario", o.getOrgaoUsuario().getIdOrgaoUsu());

		final List<DpCargo> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public Selecionavel consultarPorSigla(final DpCargoDaoFiltro flt) {
		final DpCargo o = new DpCargo();
		o.setSigla(flt.getSigla());
		o.setIdCargoIni(flt.getIdCargoIni());
		return consultarPorSigla(o);
	}

	public int consultarQuantidade(final DpCargoDaoFiltro o) {
		try {
			final Query query = em().createNamedQuery(
					"consultarQuantidadeDpCargo");
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			if (o.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0);

			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DpFuncaoConfianca> consultarPorFiltro(
			final DpFuncaoConfiancaDaoFiltro o) {
		return consultarPorFiltro(o, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<DpFuncaoConfianca> consultarPorFiltro(
			final DpFuncaoConfiancaDaoFiltro o, final int offset,
			final int itemPagina) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorFiltroDpFuncaoConfianca");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);

			if (o.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0);

			final List<DpFuncaoConfianca> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CpAplicacaoFeriado> listarAplicacoesFeriado(
			final CpAplicacaoFeriado apl) {
		final Query query = em()
				.createNamedQuery("listarAplicacoesFeriado");
		query.setParameter("cpOcorrenciaFeriado", apl.getCpOcorrenciaFeriado()
				.getId());

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

		final List<CpAplicacaoFeriado> l = query.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public DpFuncaoConfianca consultarPorSigla(final DpFuncaoConfianca o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaDpFuncaoConfianca");
		query.setParameter("idFuncao", o.getIdFuncao());
		if (o.getOrgaoUsuario() != null)
			query.setParameter("idOrgaoUsu", o.getOrgaoUsuario().getIdOrgaoUsu());
		else
			query.setParameter("idOrgaoUsu", 0);

		final List<DpFuncaoConfianca> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public DpFuncaoConfianca consultarPorNomeOrgao(final DpFuncaoConfianca o) {
		final Query query = em().createNamedQuery(
				"consultarPorNomeOrgaoDpFuncaoConfianca");
		query.setParameter("nome", o.getNomeFuncao());
		query.setParameter("idOrgaoUsuario", o.getOrgaoUsuario().getIdOrgaoUsu());
		
		final List<DpFuncaoConfianca> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public Selecionavel consultarPorSigla(final DpFuncaoConfiancaDaoFiltro flt) {
		final DpFuncaoConfianca o = new DpFuncaoConfianca();
		o.setSigla(flt.getSigla());
		CpOrgaoUsuario cpOrgao = new CpOrgaoUsuario();
		cpOrgao.setIdOrgaoUsu(flt.getIdOrgaoUsu());
		o.setOrgaoUsuario(cpOrgao);
		return consultarPorSigla(o);
	}

	public int consultarQuantidade(final DpFuncaoConfiancaDaoFiltro o) {
		try {
			final Query query = em().createNamedQuery(
					"consultarQuantidadeDpFuncaoConfianca");
			String s = o.getNome();
			if (s != null)
				s = s.replace(' ', '%');
			query.setParameter("nome", s);
			if (o.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0);

			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	public List<DpPessoa> consultarPessoasComFuncaoConfianca(Long idFuncao) {
		final Query query = em().createNamedQuery(
				"consultarPessoasComFuncaoConfianca");
		query.setParameter("idFuncaoConfianca", idFuncao);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarPessoasComCargo(Long idCargo) {
		final Query query = em().createNamedQuery(
				"consultarPessoasComCargo");
		query.setParameter("idCargo", idCargo);
		return query.getResultList();
	}

	public List<DpLotacao> consultarPorFiltro(final DpLotacaoDaoFiltro o) {
		return consultarPorFiltro(o, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<DpLotacao> consultarPorFiltro(final DpLotacaoDaoFiltro o,
			final int offset, final int itemPagina) {
		try {
			final Query query;

			if (!o.isBuscarFechadas())
				query = em()
						.createNamedQuery("consultarPorFiltroDpLotacao");
			else
				query = em().createNamedQuery(
						"consultarPorFiltroDpLotacaoInclusiveFechadas");
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			query.setParameter("nome", o.getNome() == null ? "" : o.getNome()
					.replace(' ', '%'));

			if (o.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);

			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
			final List<DpLotacao> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public DpLotacao consultarPorSigla(final DpLotacao o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaDpLotacao");
		query.setParameter("siglaLotacao", o.getSiglaLotacao());
		if (o.getOrgaoUsuario() != null)
			if (o.getOrgaoUsuario().getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getOrgaoUsuario().getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu",
						consultarPorSigla(o.getOrgaoUsuario()).getId());
		else
			query.setParameter("idOrgaoUsu", 0);

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		final List<DpLotacao> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public DpLotacao consultarPorNomeOrgao(final DpLotacao o) {
		final Query query = em().createNamedQuery(
				"consultarPorNomeOrgaoDpLotacao");
		query.setParameter("nome", o.getNomeLotacao());
		query.setParameter("idOrgaoUsu", o.getOrgaoUsuario().getIdOrgaoUsu());

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		final List<DpLotacao> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	public DpLotacao consultarPorIdInicial(Class<DpLotacao> clazz,
			final Long idInicial) {
		final Query query = em().createNamedQuery(
				"consultarPorIdInicialDpLotacao");
		query.setParameter("idLotacaoIni", idInicial);

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		final List<DpLotacao> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	public DpLotacao consultarPorIdInicialInclusiveLotacaoFechada(
			Class<DpLotacao> clazz, final Long idInicial) {
		final Query query = em().createNamedQuery(
				"consultarPorIdInicialDpLotacaoInclusiveFechada");
		query.setParameter("idLotacaoIni", idInicial);

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		final List<DpLotacao> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<DpLotacao> listarPorIdInicialDpLotacao(final Long idInicial) {
		final Query query = em().createNamedQuery(
				"listarPorIdInicialDpLotacao");
		query.setParameter("idLotacaoIni", idInicial);

		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		return query.getResultList();
	}

	public Selecionavel consultarPorSigla(final DpLotacaoDaoFiltro flt) {
		final DpLotacao o = new DpLotacao();
		o.setSigla(flt.getSigla());
		if (o.getOrgaoUsuario() == null && flt.getIdOrgaoUsu() != null) {
			CpOrgaoUsuario cpOrgaoUsu = consultar(flt.getIdOrgaoUsu(),
					CpOrgaoUsuario.class, false);
			o.setOrgaoUsuario(cpOrgaoUsu);
		}
		DpLotacao lotacao = consultarPorSigla(o);
		if (lotacao == null) {
			o.setSiglaLotacao(flt.getSigla());
			o.setOrgaoUsuario(null);
			return consultarPorSigla(o);
		}
		return lotacao;
	}

	public int consultarQuantidade(final DpLotacaoDaoFiltro o) {
		try {
			final Query query;

			if (!o.isBuscarFechadas())
				query = em().createNamedQuery(
						"consultarQuantidadeDpLotacao");
			else
				query = em().createNamedQuery(
						"consultarQuantidadeDpLotacaoInclusiveFechadas");

			query.setParameter("nome", o.getNome() != null ? o.getNome().replace(' ', '%') : "%");

			if (o.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", o.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);

			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	public Selecionavel consultarPorSigla(final CpGrupoDaoFiltro flt)
			throws AplicacaoException {
		final CpGrupo o = CpGrupo.getInstance(flt.getIdTpGrupo());
		o.setSigla(flt.getSigla());
		return consultarPorSigla(o);
	}

	@SuppressWarnings("unchecked")
	public CpGrupo consultarPorSigla(final CpGrupo o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaCpGrupo");
		query.setParameter("siglaGrupo", o.getSigla());
		if (o.getOrgaoUsuario() != null)
			query.setParameter("idOrgaoUsu", o.getOrgaoUsuario().getIdOrgaoUsu());
		else
			query.setParameter("idOrgaoUsu", 0);
		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);
		final List<CpGrupo> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	public CpPerfil consultarPorSigla(final CpPerfil o) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaCpGrupo");
		query.setParameter("siglaGrupo", o.getSigla());
		if (o.getOrgaoUsuario() != null)
			query.setParameter("idOrgaoUsu", o.getOrgaoUsuario().getIdOrgaoUsu());
		else
			query.setParameter("idOrgaoUsu", 0);
		query.setHint("org.hibernate.cacheable", true);
		query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);
		final List<CpPerfil> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public int consultarQuantidade(final CpGrupoDaoFiltro o) {
		try {
			final Query query;
			if (o.getNome() != null) {
				query = em().createNamedQuery(
						"consultarQuantidadeCpGrupoPorCpTipoGrupoIdENome");
				query.setParameter("siglaGrupo", o.getNome());
			} else {
				query = em().createNamedQuery(
						"consultarQuantidadeCpGrupoPorCpTipoGrupoId");
			}

			if (o.getIdTpGrupo() != null) {
				query.setParameter("idTpGrupo", o.getIdTpGrupo());
			} else {
				query.setParameter("idTpGrupo", 0);
			}
			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CpGrupo> consultarPorFiltro(final CpGrupoDaoFiltro o,
			final int offset, final int itemPagina) {
		try {
			final Query query;
			if (o.getNome() != null) {
				query = em().createNamedQuery(
						"consultarCpGrupoPorCpTipoGrupoIdENome");
				query.setParameter("siglaGrupo", o.getNome());
			} else {
				query = em().createNamedQuery(
						"consultarCpGrupoPorCpTipoGrupoId");
			}
			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			if (o.getIdTpGrupo() != null) {
				query.setParameter("idTpGrupo", o.getIdTpGrupo());
			} else {
				query.setParameter("idTpGrupo", 0);
			}
			final List<CpGrupo> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public DpPessoa consultarPorCpf(final long cpf) {

		final Query qry = em().createNamedQuery("consultarPorCpf");
		qry.setParameter("cpfPessoa", cpf);
		final DpPessoa pes = (DpPessoa) qry.getSingleResult();
		return pes;
	}

	public List<DpPessoa> listarPorCpf(final long cpf) {

		final Query qry = em().createNamedQuery("consultarPorCpf");
		qry.setParameter("cpfPessoa", cpf);
		final List<DpPessoa> l = qry.getResultList();
		return l;
	}
	
	/*
	 * Alteracao alteracao email Cartao 859
	 */

	public List<DpPessoaTrocaEmailDTO> listarTrocaEmailCPF(final long cpf) {
		List<DpPessoa> lst = listarPorCpf(cpf);
		List<DpPessoaTrocaEmailDTO> lstDto = new ArrayList<DpPessoaTrocaEmailDTO>();
		if (!lst.isEmpty())
			for (DpPessoa p : lst) {
				lstDto.add(new DpPessoaTrocaEmailDTO(p.getNomePessoa(),
						p.getOrgaoUsuario().getNmOrgaoUsu(), p.getCpfFormatado(), p.getEmailPessoaAtual(),
						p.getSigla(), p.getLotacao().getNomeLotacao()));
			}
		return lstDto;
	}
	
	public int countPorCpf(final long cpf) {
		return listarTrocaEmailCPF(cpf).size();
	}	

	public List<DpPessoa> listarCpfAtivoInativo(final long cpf) {

		final Query qry = em().createNamedQuery("consultarPorCpfAtivoInativo");
		qry.setParameter("cpfPessoa", cpf);
		final List<DpPessoa> l = qry.getResultList();
		return l;
	}
	
	public List<DpPessoa> listarCpfAtivoInativoNomeDiferente(final long cpf, final String nome) {

		final Query qry = em().createNamedQuery("consultarPorCpfAtivoInativoNomeDiferente");
		qry.setParameter("cpfPessoa", cpf);
		qry.setParameter("nomePessoa", nome);
		final List<DpPessoa> l = qry.getResultList();
		return l;
	}

	public List<DpPessoa> consultarPessoasAtivasPorCpf(final long cpf) {

		final Query qry = em().createNamedQuery("consultarPorCpf");
		qry.setParameter("cpfPessoa", cpf);
		final List<DpPessoa> l = qry.getResultList();
		return l;
	}

	public DpPessoa consultarPorEmail(final String email) {

		final Query qry = em().createNamedQuery("consultarPorEmail");
		qry.setParameter("emailPessoa", email);
		final DpPessoa pes = (DpPessoa) qry.getSingleResult();
		return pes;
	}
	
	public int consultarQtdePorEmailIgualCpfDiferente(final String email, final long cpf, final Long idPessoaIni) {
		final Query qry = em().createNamedQuery("consultarPorEmailIgualCpfDiferente");
		qry.setParameter("emailPessoa", email);
		qry.setParameter("cpf", cpf);
		qry.setParameter("idPessoaIni", idPessoaIni);
		final int l = ((Long) qry.getSingleResult()).intValue();
		return l;
	}

	@SuppressWarnings("unchecked")
	public DpPessoa consultarPorSigla(final DpPessoa o) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorSiglaDpPessoa");
			query.setParameter("sesb", o.getSesbPessoa());
			query.setParameter("matricula", o.getMatricula());
			/*
			 * if (o.getOrgaoUsuario().getIdOrgaoUsu() != null)
			 * query.setParameter("idOrgaoUsu", o.getOrgaoUsuario().getIdOrgaoUsu());
			 * else query.setParameter("idOrgaoUsu", 0);
			 */

			final List<DpPessoa> l = query.getResultList();
			if (l.size() != 1)
				return null;
			return l.get(0);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	/**
	 * retorna a pessoa pelo sesb+matricula
	 * 
	 * @param principal
	 * @return
	 */
	public DpPessoa getPessoaPorPrincipal(String principal) {
		DpPessoa pessoaTemplate = new DpPessoa();
		pessoaTemplate.setSesbPessoa(MatriculaUtils.getSiglaDoOrgaoDaMatricula(principal));
		pessoaTemplate.setMatricula(MatriculaUtils.getParteNumericaDaMatricula(principal));
		DpPessoa pessoaNova = CpDao.getInstance().consultarPorSigla(
				pessoaTemplate);
		return pessoaNova;
	}

	@SuppressWarnings("unchecked")
	public DpPessoa consultarPorIdInicial(final Long idInicial) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorIdInicialDpPessoa");
			query.setParameter("idPessoaIni", idInicial);

//			query.setHint("org.hibernate.cacheable", true);
//			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

			final List<DpPessoa> l = query.getResultList();
			if (l.size() != 1)
				return null;
			return l.get(0);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarPorIdInicialInclusiveFechadas(
			final Long idInicial) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorIdInicialDpPessoaInclusiveFechadas");
			query.setParameter("idPessoaIni", idInicial);

			return query.getResultList();
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public DpPessoa consultarPorIdInicialInclusiveLotacaoFechada(
			final Long idInicial) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorIdInicialDpLotacaoInclusiveFechada");
			query.setParameter("idPessoaIni", idInicial);

			final List<DpPessoa> l = query.getResultList();
			if (l.size() != 1)
				return null;
			return l.get(0);
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CpUF> consultarUF() {

		Query query = em().createQuery("from CpUF l order by l.nmUF");
		List l = query.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<CpLocalidade> consultarLocalidadesPorUF(final CpUF cpuf) {
		Query query = em().createQuery(
				"from CpLocalidade l where l.UF.idUF = :idUf order by remove_acento(upper(l.nmLocalidade)");
		query.setParameter("idUf", cpuf.getIdUF().intValue());
		List l = query.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<CpLocalidade> consultarLocalidadesPorUF(final String siglaUF) {

		Query query = em().createQuery(
				"from CpLocalidade l where l.UF.nmUF = :siglaUF");
		query.setParameter("siglaUF", siglaUF);
		List l = query.getResultList();
		return l;
	}

	public CpLocalidade consultarLocalidadesPorNomeUF(final CpLocalidade localidade) {
		Query query = em().createQuery(
				"from CpLocalidade lot where "
				+ "      upper(TRANSLATE(lot.nmLocalidade,'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚçÇ''','AAAAAAAAEEEEIIOOOOOOUUUUCC ')) = upper(:nome) and lot.UF.id = :idUf");
		query.setParameter("idUf", localidade.getUF().getId());
		query.setParameter("nome", localidade.getNmLocalidade());
		
		List l = query.getResultList();
		
		if(l.size() != 1) {
			return null;
		}
		return (CpLocalidade)l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<CpLocalidade> consultarLocalidades() {

		Query query = em().createQuery(
				"from CpLocalidade l order by l.nmLocalidade");
		List l = query.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public CpLocalidade consultarLocalidade(CpLocalidade localidade) {

		Query query = em().createQuery(
				"from CpLocalidade l where l.idLocalidade = :idLocalidade");
		query.setParameter("idLocalidade", localidade.getId());
		List l = query.getResultList();
		return (CpLocalidade) l.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public CpPersonalizacao consultarPersonalizacao(DpPessoa pes) {
		final Query query = em()
				.createNamedQuery("consultarPersonalizacao");
		query.setParameter("idPessoaIni", pes.getIdPessoaIni());

		// query.setHint("org.hibernate.cacheable", true);
		// query.setHint("org.hibernate.cacheRegion", "query.CpPersonalizacao");
		final List<CpPersonalizacao> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public List<DpPessoa> consultarPorFiltro(final DpPessoaDaoFiltro flt) {
		return consultarPorFiltro(flt, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarAtivasNaDataOrgao(final Date dt,
			final CpOrgaoUsuario org) {
		final Query query;
		query = em().createNamedQuery("consultarAtivasNaDataOrgao");
		query.setParameter("idOrgaoUsu", org.getIdOrgaoUsu());
		query.setParameter("dt", dt);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarPorFiltroSemIdentidade(final DpPessoaDaoFiltro flt,
			final int offset, final int itemPagina) {
		try {
			final Query query;
			boolean isFiltrarPorListaDeUsuario = (flt.getIdPessoaSelecao() != null && flt.getIdPessoaSelecao().length > 0);
			boolean isFiltrarPorListaDeLotacao = (flt.getIdLotacaoSelecao() != null && flt.getIdLotacaoSelecao().length > 0 && !isFiltrarPorListaDeUsuario);
			double quantidadeDeLotacaoOuUsuario = isFiltrarPorListaDeUsuario ? flt.getIdPessoaSelecao().length : isFiltrarPorListaDeLotacao ? flt.getIdLotacaoSelecao().length : 0;
			
			if (isFiltrarPorListaDeLotacao || isFiltrarPorListaDeUsuario) {	
				query = queryConsultarPorFiltroDpPessoaSemIdentidadeComListaDeLotacaoOuListaDeUsuario(quantidadeDeLotacaoOuUsuario, false);
			} else {
				query = em().createNamedQuery("consultarPorFiltroDpPessoaSemIdentidade");
			}

			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			query.setParameter("nome",
					flt.getNome().toUpperCase().replace(' ', '%'));

			if(flt.getCpf() != null && !"".equals(flt.getCpf())) {
				query.setParameter("cpf", Long.valueOf(flt.getCpf()));
			} else {
				query.setParameter("cpf", 0L);
			}
			
			if (flt.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", flt.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);
																							
			if (isFiltrarPorListaDeUsuario) {
				enviarParametrosLotacaoOuUsuario(query, true, flt.getIdPessoaSelecao());							
			} else if (isFiltrarPorListaDeLotacao) {
				enviarParametrosLotacaoOuUsuario(query, false, flt.getIdLotacaoSelecao());										
			} else if (flt.getLotacao() != null) {
				query.setParameter("lotacao", flt.getLotacao().getId());				
			} else {
				query.setParameter("lotacao", 0L);
			}

			final List<DpPessoa> l = query.getResultList();
			return l;
 		} catch (final NullPointerException e) {
			return null;
		}
	}	
		
	@SuppressWarnings("unchecked")
	public List<DpPessoaUsuarioDTO> consultarUsuariosComEnvioDeEmailPendenteFiltrandoPorLotacao(final DpPessoaDaoFiltro flt) {
		try {
			final Query query;
			boolean isFiltrarPorListaDeLotacao = (flt.getIdLotacaoSelecao() != null && flt.getIdLotacaoSelecao().length > 0);
						
			query = em().createNamedQuery("consultarUsuariosComEnvioDeEmailPendenteFiltrandoPorLotacao");
			
			if (flt.getIdOrgaoUsu() != null) 
				query.setParameter("idOrgaoUsu", flt.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);

			if (isFiltrarPorListaDeLotacao) {								
				query.setParameter("idLotacaoLista", flt.getIdLotacaoSelecao());								
			} else if (flt.getLotacao() != null) {
				query.setParameter("idLotacaoLista", flt.getLotacao().getId());
			} else {
				query.setParameter("idLotacaoLista", 0L);
			}
						
			return (List<DpPessoaUsuarioDTO>) query.getResultList();
 		} catch (Exception e) {
			return null;
		}
	}
	
	public int consultarQuantidadeDpPessoaSemIdentidade(final DpPessoaDaoFiltro flt) {
		try {
			final Query query;			
			boolean isFiltrarPorListaDeUsuario = (flt.getIdPessoaSelecao() != null && flt.getIdPessoaSelecao().length > 0);
			boolean isFiltrarPorListaDeLotacao = (flt.getIdLotacaoSelecao() != null && flt.getIdLotacaoSelecao().length > 0 && !isFiltrarPorListaDeUsuario);
			double quantidadeDeLotacaoOuUsuario = isFiltrarPorListaDeUsuario ? flt.getIdPessoaSelecao().length : isFiltrarPorListaDeLotacao ? flt.getIdLotacaoSelecao().length : 0;
			
			if (isFiltrarPorListaDeLotacao || isFiltrarPorListaDeUsuario) {	
				query = queryConsultarPorFiltroDpPessoaSemIdentidadeComListaDeLotacaoOuListaDeUsuario(quantidadeDeLotacaoOuUsuario, true);						
			} else {
				query = em().createNamedQuery("consultarQuantidadeDpPessoaSemIdentidade");
			}					
			
			query.setParameter("nome",
					flt.getNome().toUpperCase().replace(' ', '%'));

			if(flt.getCpf() != null && !"".equals(flt.getCpf())) {
				query.setParameter("cpf", Long.valueOf(flt.getCpf()));
			} else {
				query.setParameter("cpf", 0L);
			}
			
			if (flt.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", flt.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);

			if (isFiltrarPorListaDeUsuario) {
				enviarParametrosLotacaoOuUsuario(query, true, flt.getIdPessoaSelecao());							
			} else if (isFiltrarPorListaDeLotacao) {
				enviarParametrosLotacaoOuUsuario(query, false, flt.getIdLotacaoSelecao());									
			} else if (flt.getLotacao() != null) {
				query.setParameter("lotacao", flt.getLotacao().getId());				
			} else {
				query.setParameter("lotacao", 0L);
			}

			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}
	
	public void enviarParametrosLotacaoOuUsuario(Query query, boolean isFiltrarPorListaDeUsuario, Long[] itens) {		
		List<Long> parametros = Arrays.asList(itens);				
		int indiceInicial = 0, indiceFinal = 1000, indiceMaximo = itens.length, tamanho = 1000;
		double quantidadeDeClausulaIN = Math.ceil(Double.valueOf(indiceMaximo) / 1000);				
		
		for (int i = 1; i <= quantidadeDeClausulaIN; i++) {	
			
			if (quantidadeDeClausulaIN == 1) {
				indiceFinal = indiceMaximo;
				tamanho = indiceMaximo;
			}
			
			Long[] parametro = parametros.subList(indiceInicial, indiceFinal).toArray(new Long[tamanho]);
			
			indiceInicial = indiceFinal;
			if ((indiceMaximo - indiceFinal) >= 1000) {
				indiceFinal += 1000; 
			} else {				
				tamanho = indiceMaximo - indiceFinal;
				indiceFinal = indiceMaximo;
			}							
			
			if (isFiltrarPorListaDeUsuario) {
				query.setParameter("idPessoaLista" + i, parametro);										
				query.setParameter("idLotacaoLista" + i, 0L);
			} else {
				query.setParameter("idLotacaoLista" + i, parametro);										
				query.setParameter("idPessoaLista" + i, 0L);
			}						
		}				
	}
	
	public Query queryConsultarPorFiltroDpPessoaSemIdentidadeComListaDeLotacaoOuListaDeUsuario(double quantidadeDeLotacaoOuUsuario, boolean apenasContarItens) {
		Query query;	
		String queryTemp = "";		
		double quantidadeDeClausulaIN = Math.ceil(Double.valueOf(quantidadeDeLotacaoOuUsuario) / 1000);
		if (quantidadeDeClausulaIN <= 0) quantidadeDeClausulaIN = 1;
		
		if (apenasContarItens)
			queryTemp =	 "select count(pes) from DpPessoa pes";
		else		
			queryTemp = "from DpPessoa pes ";
						
		queryTemp += "  where (upper(pes.nomePessoaAI) like upper('%' || :nome || '%'))"
				+ " and (pes.cpfPessoa = :cpf or :cpf = 0)"
				+ " and pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"	
				+ "  and (";
		for (int i = 1; i <= quantidadeDeClausulaIN; i++) {					
			if (i > 1) queryTemp += " or ";
			queryTemp += " pes.lotacao.idLotacao in (:idLotacaoLista" + i + ") or pes.idPessoa in (:idPessoaLista" + i + ")";				
		}
		queryTemp += ")"
					+ " and pes.dataFimPessoa = null"
					+ " and not exists (select ident.dpPessoa.idPessoa from CpIdentidade ident where pes.idPessoa = ident.dpPessoa.idPessoa)"
					+ "  order by pes.cpfPessoa";			
		
		query = em().createQuery(queryTemp);
							
		return query;
	}

	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarPorFiltro(final DpPessoaDaoFiltro flt,
			final int offset, final int itemPagina) {
		try {
			final Query query;

			if (!flt.isBuscarFechadas()) {
				query = em().createNamedQuery("consultarPorFiltroDpPessoa");
				if(flt.getId() != null && !"".equals(flt.getId())) {
					query.setParameter("id", Long.valueOf(flt.getId()));
				} else {
					query.setParameter("id", 0L);
				}
			} else
				query = em().createNamedQuery(
						"consultarPorFiltroDpPessoaInclusiveFechadas");

			if (offset > 0) {
				query.setFirstResult(offset);
			}
			if (itemPagina > 0) {
				query.setMaxResults(itemPagina);
			}
			
			query.setParameter("nome",
					flt.getNome().toUpperCase().replace(' ', '%'));
			
			if(flt.getEmail() != null) {
				query.setParameter("email",flt.getEmail().toUpperCase().replace(' ', '%'));
			} else {
				query.setParameter("email", null);
			}

			if (!flt.isBuscarFechadas())
				query.setParameter("situacaoFuncionalPessoa",
						flt.getSituacaoFuncionalPessoa());

			if(flt.getCpf() != null && !"".equals(flt.getCpf())) {
				query.setParameter("cpf", Long.valueOf(flt.getCpf()));
			} else {
				query.setParameter("cpf", 0L);
			}
			
			if (flt.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", flt.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);

			if (flt.getLotacao() != null)
				query.setParameter("lotacao", flt.getLotacao().getId());
			else
				query.setParameter("lotacao", 0L);

			if (flt.getCargo() != null)
				query.setParameter("cargo", flt.getCargo().getId());
			else
				query.setParameter("cargo", 0L);
			
			if (flt.getFuncaoConfianca() != null)
				query.setParameter("funcao", flt.getFuncaoConfianca().getId());
			else
				query.setParameter("funcao", 0L);

			final List<DpPessoa> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarPessoaComOrgaoFuncaoCargo(final DpPessoa pes) {
		try {
			final Query query;

			query = em().createNamedQuery(
						"consultarPessoaComOrgaoFuncaoCargo");

			query.setParameter("nome",
					pes.getNomePessoa().toUpperCase().replace(' ', '%'));
			
			if(pes.getEmailPessoa() != null) {
				query.setParameter("email",pes.getEmailPessoa().toUpperCase().replace(' ', '%'));
			} else {
				query.setParameter("email",null);
			}
			
			if(pes.getCpfPessoa() != null && !"".equals(pes.getCpfPessoa())) {
				query.setParameter("cpf", Long.valueOf(pes.getCpfPessoa()));
			} else {
				query.setParameter("cpf", 0L);
			}
			
			if (pes.getOrgaoUsuario() != null)
				query.setParameter("idOrgaoUsu", pes.getOrgaoUsuario().getId());
			else
				query.setParameter("idOrgaoUsu", 0L);

			if (pes.getLotacao() != null)
				query.setParameter("lotacao", pes.getLotacao().getId());
			else
				query.setParameter("lotacao", 0L);

			if (pes.getCargo() != null)
				query.setParameter("cargo", pes.getCargo().getId());
			else
				query.setParameter("cargo", 0L);
			
			if (pes.getFuncaoConfianca() != null)
				query.setParameter("funcao", pes.getFuncaoConfianca().getId());
			else
				query.setParameter("funcao", 0L);

			final List<DpPessoa> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DpPessoa> consultarPorOrgaoUsuDpPessoaInclusiveFechadas(
			final long idOrgaoUsu) {
		try {
			final Query query = em().createNamedQuery(
					"consultarPorOrgaoUsuDpPessoaInclusiveFechadas");

			query.setParameter("idOrgaoUsu", idOrgaoUsu);

			final List<DpPessoa> l = query.getResultList();
			return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}

	public int consultarQuantidade(final DpPessoaDaoFiltro flt) {
		try {
			final Query query;

			if (!flt.isBuscarFechadas()) {
				query = em()
						.createNamedQuery("consultarQuantidadeDpPessoa");
				if (flt.getId() != null)
					query.setParameter("id", flt.getId());
				else
					query.setParameter("id", 0L);
			} else
				query = em().createNamedQuery(
						"consultarQuantidadeDpPessoaInclusiveFechadas");

			query.setParameter("nome",
					flt.getNome().toUpperCase().replace(' ', '%'));

			if (!flt.isBuscarFechadas())
				query.setParameter("situacaoFuncionalPessoa",
						flt.getSituacaoFuncionalPessoa());

			if (flt.getCpf() != null)
				query.setParameter("cpf", flt.getCpf());
			else
				query.setParameter("cpf", 0L);
			
			if (flt.getEmail() != null)
				query.setParameter("email", flt.getEmail());
			else
				query.setParameter("email", null);
			
			if (flt.getIdOrgaoUsu() != null)
				query.setParameter("idOrgaoUsu", flt.getIdOrgaoUsu());
			else
				query.setParameter("idOrgaoUsu", 0L);
			if (flt.getLotacao() != null)
				query.setParameter("lotacao", flt.getLotacao().getId());
			else
				query.setParameter("lotacao", 0L);
			
			if (flt.getCargo() != null)
				query.setParameter("cargo", flt.getCargo().getId());
			else
				query.setParameter("cargo", 0L);
			
			if (flt.getFuncaoConfianca() != null)
				query.setParameter("funcao", flt.getFuncaoConfianca().getId());
			else
				query.setParameter("funcao", 0L);

			final int l = ((Long) query.getSingleResult()).intValue();
			return l;
		} catch (final NullPointerException e) {
			return 0;
		}
	}

	public Selecionavel consultarPorSigla(final DpPessoaDaoFiltro flt) {
		final DpPessoa o = new DpPessoa();
		o.setSigla(flt.getSigla());
		/*
		 * CpOrgaoUsuario cpOrgao = new CpOrgaoUsuario();
		 * cpOrgao.setIdOrgaoUsu(flt.getIdOrgaoUsu());
		 * o.setOrgaoUsuario(cpOrgao);
		 */
		return consultarPorSigla(o);
	}

	@SuppressWarnings("unchecked")
	public List<DpSubstituicao> consultarSubstituicoesPermitidas(
			final DpSubstituicao exemplo) throws SQLException {
		try {
			Query query = null;
			query = em().createNamedQuery(
					"consultarSubstituicoesPermitidas");
			query.setParameter("idSubstitutoIni", exemplo.getSubstituto()
					.getIdPessoaIni());
			query.setParameter("idLotaSubstitutoIni", exemplo.getLotaSubstituto()
					.getIdLotacaoIni());
			// Reativado pois esse query é executado a cada chamada, inclusive
			// as ajax.
			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SUBSTITUICAO);
			return query.getResultList();
		} catch (final IllegalArgumentException e) {
			throw e;
		} catch (final Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DpSubstituicao> consultarOrdemData(final DpSubstituicao exemplo)
			throws SQLException {
		try {
			Query query = null;
			query = em().createNamedQuery("consultarOrdemData");
			query.setParameter("idTitularIni", exemplo.getTitular().getIdPessoaIni());
			query.setParameter("idLotaTitularIni", exemplo.getLotaTitular()
					.getIdLotacaoIni());
			// query.setHint("org.hibernate.cacheable", true);
			// query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SUBSTITUICAO);
			return query.getResultList();
		} catch (final IllegalArgumentException e) {
			throw e;
		} catch (final Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DpVisualizacao> consultarVisualizacoesPermitidas(
			final DpVisualizacao exemplo) throws SQLException {
		try {
			Query query = null;
			query = em().createNamedQuery(
					"consultarVisualizacoesPermitidas");
			query.setParameter("idDelegadoIni", exemplo.getDelegado()
					.getIdPessoaIni());
			query.setHint("org.hibernate.cacheable", true);
			query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SUBSTITUICAO);
			return query.getResultList();
		} catch (final IllegalArgumentException e) {
			throw e;
		} catch (final Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DpVisualizacao> consultarOrdemData(final DpVisualizacao exemplo)
			throws SQLException {
		try {
			Query query = null;
			query = em().createNamedQuery("consultarOrdem");
			query.setParameter("idTitularIni", exemplo.getTitular().getIdPessoaIni());
			return query.getResultList();
		} catch (final IllegalArgumentException e) {
			throw e;
		} catch (final Exception e) {
			return null;
		}
	}

	public CpIdentidade consultaIdentidadeCadastrante(final String nmUsuario,
			boolean fAtiva) throws AplicacaoException {
		List<CpIdentidade> lista = consultaIdentidadesCadastrante(nmUsuario,
				fAtiva);
		// obtem preferencialmente identidade de formulario - unico formato que
		// existia anteriormente
		for (CpIdentidade idLista : lista) {
			if (idLista.getCpTipoIdentidade().isTipoFormulario()) {
				return idLista;
			}
		}
		// se nao encontrar, retorna o primeiro, como era antes.
		final CpIdentidade id = lista.get(0);
		return id;
	}

	@SuppressWarnings("unchecked")
	public List<CpIdentidade> consultaIdentidadesCadastrante(
			final String nmUsuario, boolean fAtiva) throws AplicacaoException {
		try {
			final Query qry = em().createNamedQuery(
					fAtiva ? "consultarIdentidadeCadastranteAtiva"
							: "consultarIdentidadeCadastrante");
			if(Pattern.matches( "\\d+", nmUsuario )) {
				qry.setParameter("cpf", Long.valueOf(nmUsuario));
				qry.setParameter("nmUsuario", null);
				qry.setParameter("sesbPessoa", null);
			} else {
				qry.setParameter("nmUsuario", nmUsuario);
				qry.setParameter("sesbPessoa", MatriculaUtils.getSiglaDoOrgaoDaMatricula(nmUsuario));
				qry.setParameter("cpf", null);
			}
			
			/* Constantes para Evitar Parse Oracle */
			qry.setParameter("cpfZero", 0L);
			qry.setParameter("sfp1","1");
			qry.setParameter("sfp2","2");
			qry.setParameter("sfp12","12");
			qry.setParameter("sfp22","22");
			qry.setParameter("sfp31","31");

			
			// Cache was disabled because it would interfere with the
			// "change password" action.
//			qry.setHint("org.hibernate.cacheable", true); 
//			qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SECONDS);
			final List<CpIdentidade> lista = (List<CpIdentidade>) qry.getResultList();
			if (lista.size() == 0) {
				throw new AplicacaoException(
						"Nao foi possivel localizar a identidade do usuario '"
								+ nmUsuario + "'.");
			}
			return lista;
		} catch (Throwable e) {
			throw new AplicacaoException(
					"Ocorreu um erro tentando localizar a identidade do usuario '"
							+ nmUsuario + "'.", 0, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CpIdentidade> consultaIdentidadesPorCpf(
			final String nmUsuario) throws AplicacaoException {
		try {
			final Query qry = em().createNamedQuery("consultarIdentidadeCadastranteAtiva");
			
			qry.setParameter("cpf", Long.valueOf(nmUsuario));
			qry.setParameter("nmUsuario", null);
			qry.setParameter("sesbPessoa", null);
			
			/* Constantes para Evitar Parse Oracle */
			qry.setParameter("cpfZero","0");
			qry.setParameter("sfp1","1");
			qry.setParameter("sfp2","2");
			qry.setParameter("sfp12","12");
			qry.setParameter("sfp22","22");
			qry.setParameter("sfp31","31");
			
			qry.setHint("org.hibernate.cacheable", true);
			qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SECONDS);
			final List<CpIdentidade> lista = (List<CpIdentidade>) qry.getResultList();
			
			return lista;
		} catch (Throwable e) {
			throw new AplicacaoException(
					"Ocorreu um erro tentando localizar a identidade do usuario '"
							+ nmUsuario + "'.", 0, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CpIdentidade> consultaIdentidadesPorCpfEmail(
			final String nmUsuario, String email) throws AplicacaoException {
		try {
			final Query qry = em().createNamedQuery("consultarIdentidadeCpfEmail");
			
			qry.setParameter("cpf", Long.valueOf(nmUsuario));
			qry.setParameter("email", email);
			
			/* Constantes para Evitar Parse Oracle */
			qry.setParameter("cpfZero","0");
			qry.setParameter("sfp1","1");
			qry.setParameter("sfp2","2");
			qry.setParameter("sfp12","12");
			qry.setParameter("sfp22","22");
			qry.setParameter("sfp31","31");
			
			qry.setHint("org.hibernate.cacheable", true);
			qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SECONDS);
			final List<CpIdentidade> lista = (List<CpIdentidade>) qry.getResultList();
			
			return lista;
		} catch (Throwable e) {
			throw new AplicacaoException(
					"Ocorreu um erro tentando localizar a identidade do usuario '"
							+ nmUsuario + "'.", 0, e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<CpIdentidade> consultaIdentidades(final DpPessoa pessoa) {
		final Query qry = em().createNamedQuery("consultarIdentidades");
		qry.setParameter("idPessoaIni", pessoa.getIdInicial());
		final List<CpIdentidade> lista = qry.getResultList();
		return lista;
	}

	/*
	 * @SuppressWarnings("unchecked") public Usuario
	 * consultaUsuarioCadastrante(final String nmUsuario) { try { final Query
	 * qry = em().createNamedQuery( "consultarUsuarioCadastrante");
	 * qry.setParameter("nmUsuario", nmUsuario); // Verifica se existe numeros no
	 * login do usuario if (nmUsuario.substring(2).matches("^[0-9]*$"))
	 * qry.setParameter("sesbPessoa", nmUsuario.substring(0, 2)); else
	 * qry.setParameter("sesbPessoa", "RJ"); // se nnao ha numeros atribui // RJ //
	 * por default
	 * 
	 * qry.setHint("org.hibernate.cacheable", true);  qry.setHint("org.hibernate.cacheRegion", "query.UsuarioCadastrante");
	 * final List<Object[]> lista = qry.getResultList(); if (lista.size() == 0) { throw
	 * new AplicacaoException( "Nao foi possivel localizar o usuario '" +
	 * nmUsuario + "'."); } final Object[] par = lista.get(0); final Usuario usu
	 * = (Usuario) par[0]; final DpPessoa pess = (DpPessoa) par[1];
	 * usu.setPessoa(pess); return usu; } catch (Throwable e) { Auto-generated
	 * catch block e.printStackTrace(); return null; } }
	 * 
	 * public Usuario consultaUsuarioCadastranteAtivo(final String nmUsuario)
	 * throws Exception { // Nato: comentei porque estava muito dificil de
	 * debugar erros de banco // de dados quando as excecoes nao sao lancadas
	 * aqui. // try { final Query qry = em().createNamedQuery(
	 * "consultarUsuarioCadastranteAtivo"); qry.setParameter("nmUsuario",
	 * nmUsuario); // Verifica se existe numeros no login do usuario if
	 * (nmUsuario.substring(2).matches("^[0-9]*$")) qry.setParameter("sesbPessoa",
	 * nmUsuario.substring(0, 2)); else qry.setParameter("sesbPessoa", "RJ"); // se
	 * nao ha numeros atribui // RJ // por default
	 * 
	 * qry.setHint("org.hibernate.cacheable", true); 
	 * qry.setHint("org.hibernate.cacheRegion", "query.UsuarioCadastranteAtivo"); final List<Object[]>
	 * lista = qry.getResultList(); if (lista.size() == 0) { throw new
	 * AplicacaoException( "Nao foi possivel localizar o usuario '" + nmUsuario
	 * + "'."); } final Object[] par = lista.get(0); final Usuario usu =
	 * (Usuario) par[0]; final DpPessoa pess = (DpPessoa) par[1];
	 * usu.setPessoa(pess); return usu; // Nato: comentei porque estava muito
	 * dificil de debugar erros de banco // de dados quando as excecoes nao sao
	 * lancadas aqui. // } catch (Throwable e) { // block //
	 * e.printStackTrace(); // return null; // } }
	 */
	public List<DpPessoa> pessoasPorLotacao(Long id,
			Boolean incluirSublotacoes, Boolean somenteServidor,
			SituacaoFuncionalEnum situacoesFuncionais)
			throws AplicacaoException {
		if (id == null || id == 0)
			return null;

		List<DpPessoa> lstCompleta = new ArrayList<DpPessoa>();

		DpLotacao lotacao = consultar(id, DpLotacao.class, false);

		lotacao = lotacao.getLotacaoAtual();

		if (lotacao == null)
			return lstCompleta;

		List<DpLotacao> sublotacoes = new ArrayList<DpLotacao>();
		sublotacoes.add(lotacao);
		if (incluirSublotacoes) {
			List<DpLotacao> lotacoes = listarLotacoes();
			boolean continuar = true;
			while (continuar) {
				continuar = false;
				for (DpLotacao lot : lotacoes) {
					if (sublotacoes.contains(lot))
						continue;
					if (sublotacoes.contains(lot.getLotacaoPai())) {
						if (!lot.isSubsecretaria()) {
							sublotacoes.add(lot);
							continuar = true;
						}
					}
				}
			}
		}

		for (DpLotacao lot : sublotacoes) {
			final String[] values = new String[] { "ESTAGIARIO", "JUIZ SUBSTITUTO", "JUIZ FEDERAL" };
			CriteriaQuery<DpPessoa> q = cb().createQuery(DpPessoa.class);
			Root<DpPessoa> c = q.from(DpPessoa.class);
			q.select(c);
			Join<DpPessoa, DpLotacao> joinLotacao = c.join("lotacao", JoinType.LEFT);
			q.where(cb().equal(joinLotacao.get("idLotacao"), lot.getId()));
			if (somenteServidor) { 
				Join<DpPessoa, DpCargo> joinCargo = c.join("cargo", JoinType.LEFT);
				q.where(joinCargo.get("nomeCargo").in(values));
			}
			q.where(c.get("situacaoFuncionalPessoa").in(situacoesFuncionais.getValor()));
			q.where(cb().isNull(c.get("dataFimPessoa")));
			q.orderBy(cb().asc(c.get("nomePessoa")));
			lstCompleta.addAll((List<DpPessoa>) em().createQuery(q).getResultList());
		}
		return lstCompleta;
	}

	public List<DpPessoa> pessoasPorLotacao(Long id,
			Boolean incluirSublotacoes, Boolean somenteServidor)
			throws AplicacaoException {
		return pessoasPorLotacao(id, incluirSublotacoes, somenteServidor,
				SituacaoFuncionalEnum.APENAS_ATIVOS);
	}

	public DpPessoa consultarPorCpfMatricula(final long cpf, long matricula) {

		final Query qry = em().createNamedQuery("consultarPorCpfMatricula");
		qry.setParameter("cpfPessoa", cpf);
		qry.setParameter("matricula", matricula);
		final DpPessoa pes = (DpPessoa) qry.getSingleResult();
		return pes;
	}

	public Date consultarDataEHoraDoServidor() throws AplicacaoException {
		Query sql = em().createNamedQuery(
				"consultarDataEHoraDoServidor");

		List result = sql.getResultList();
		if (result.size() != 1)
			throw new AplicacaoException(
					"Nao foi possivel obter a data e a hora atuais do servidor.");

		return (Date) ((result.get(0)));
	}

	public List<CpConfiguracao> consultarConfiguracoesDesde(Date desde) {
		CriteriaQuery<CpConfiguracao> q = cb().createQuery(CpConfiguracao.class);
		Root<CpConfiguracao> c = q.from(CpConfiguracao.class);
		q.select(c);
		if (desde != null) {
			Predicate confsAtivas = cb().greaterThan(c.<Date>get("hisDtIni"), desde);
			Predicate confsInativas = cb().greaterThanOrEqualTo(c.<Date>get("hisDtFim"), desde);
			q.where(cb().or(confsAtivas, confsInativas));
		}
		return em().createQuery(q).getResultList();
	}

	public Date consultarDataUltimaAtualizacao() throws AplicacaoException {
		Query sql = (Query) em().createNamedQuery(
				"consultarDataUltimaAtualizacao");
		
		List result = sql.getResultList();
		Date dtIni = (Date) ((Object[]) (result.get(0)))[0];
		Date dtFim = (Date) ((Object[]) (result.get(0)))[1];
		return DateUtils.max(dtIni, dtFim);
	}

	public Date dt() throws AplicacaoException {
		return consultarDataEHoraDoServidor();
	}

	public List<CpConfiguracao> consultar(final CpConfiguracao exemplo) {
		Query query = em().createNamedQuery("consultarCpConfiguracoes");

		query.setParameter("idTpConfiguracao", exemplo.getCpTipoConfiguracao()
				.getIdTpConfiguracao());

		
		// query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		return query.getResultList();
	}

	public List<CpConfiguracao> consultarConfiguracoesPorTipo(
			final Long idTipoConfig) {
		Query query = em().createNamedQuery(
				"consultarCpConfiguracoesPorTipo");

		query.setParameter("idTpConfiguracao", idTipoConfig);

		

		return query.getResultList();
	}

	public List<CpConfiguracao> consultarConfiguracoesAtivas() {
		Query query = em().createNamedQuery(
				"consultarCpConfiguracoesAtivas");
		
		return query.getResultList();
	}

	public List<CpConfiguracao> porLotacaoPessoaServicoTipo(
			final CpConfiguracao exemplo) {
		Query query = em().createNamedQuery(
				"consultarCpConfiguracoesPorLotacaoPessoaServicoTipo");
		query.setParameter("idPessoa", exemplo.getDpPessoa().getIdPessoa());
		//removido eap72: query.setParameter("idLotacao", exemplo.getLotacao().getIdLotacao());
		query.setParameter("idTpConfiguracao", exemplo.getCpTipoConfiguracao()
				.getIdTpConfiguracao());
		//removido eap72: query.setParameter("idServico", exemplo.getCpServico().getIdServico());
		query.setParameter("siglaServico", exemplo.getCpServico().getSiglaServico());
		query.setParameter("idSitConfiguracao", CpSituacaoConfiguracao.SITUACAO_PODE);
		// kpf: com o cache true, as configuracoes sao exibidas de forma forma
		// errada apos a primeira
		
		// query.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> listarAtivos(Class<T> clazz, String dtFim,
			long idOrgaoUsu) {
		final CriteriaBuilder criteriaBuilder = em().getCriteriaBuilder();
		CriteriaQuery<T> crit = criteriaBuilder.createQuery(clazz);
		Root<T> root = crit.from(clazz);
		crit.where(cb().isNull(root.get("hisDtFim")), cb().equal(root.get("orgaoUsuario.idOrgaoUsu"), idOrgaoUsu));
		return em().createQuery(crit).getResultList();
	}

	public <T> List<T> listarAtivos(Class<T> clazz, String orderBy) {
		CriteriaQuery<T> q = cb().createQuery(clazz);
		Root<T> c = q.from(clazz);
		q.select(c);
		q.where(cb().equal(c.get("hisAtivo"), 1));
		if (orderBy != null) {
			q.orderBy(cb().asc(c.get(orderBy)));
		}
		return em().createQuery(q).getResultList();
	}

	public <T> List<T> listarTodos(Class<T> clazz, String orderBy) {
		CriteriaQuery<T> q = cb().createQuery(clazz);
		Root<T> c = q.from(clazz);
		q.select(c);
		if (orderBy != null) {
			q.orderBy(cb().asc(c.get(orderBy)));
		}
		return em().createQuery(q).getResultList();
	}

	public <T> T consultarAtivoPorIdInicial(Class<T> clazz, Long hisIdIni) {
		CriteriaQuery<T> q = cb().createQuery(clazz);
		Root<T> c = q.from(clazz);
		q.select(c);
		q.where(cb().equal(c.get("hisIdIni"), hisIdIni));
		q.where(cb().equal(c.get("hisAtivo"), 1));

		T obj = null;
		try {
			obj = (T) em().createQuery(q).getResultList().get(0);
		} catch (Exception e) {

		}

		return obj;
	}

	public HistoricoAuditavel gravarComHistorico(HistoricoAuditavel oNovo,
			HistoricoAuditavel oAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		if (dt == null)
			dt = CpDao.getInstance().consultarDataEHoraDoServidor();
		oNovo.setHisDtIni(dt);
		if (oAntigo == null) {
			return gravarComHistorico(oNovo, identidadeCadastrante);
		}

		if (oNovo.semelhante(oAntigo, 0)) {
			return oAntigo;
		}

		oAntigo.setHisDtFim(dt);
		gravarComHistorico(oAntigo, identidadeCadastrante);
		return gravarComHistorico(oNovo, identidadeCadastrante);
	}

	public HistoricoAuditavel gravarComHistorico(
			final HistoricoAuditavel entidade,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		if (entidade.getHisDtIni() != null && entidade.getHisIdcIni() == null)
			entidade.setHisIdcIni(identidadeCadastrante);
		if (entidade.getHisDtFim() != null && entidade.getHisIdcFim() == null)
			entidade.setHisIdcFim(identidadeCadastrante);
		entidade.setHisAtivo(entidade.getHisDtFim() == null ? 1 : 0);
		gravar(entidade);
		if (entidade.getHisIdIni() == null && entidade.getId() != null) {
			entidade.setHisIdIni(entidade.getId());
			gravar(entidade);
		}
		descarregar();
		try {
			invalidarCache(entidade);
			// Edson: não há necessidade de limpar o cache de configs no próprio
			// request
			// pois, no request seguinte, a limpeza será feita. Além disso,
			// estava gerando
			// o erro #972 (ver comentários)
			// Cp.getInstance().getConf().limparCacheSeNecessario();
		} catch (Exception e) {
			throw new AplicacaoException("Nao foi possivel limpar o cache.", 0,
					e);
		}
		return entidade;
	}

	public <T> T gravar(final T entidade) {
		if (entidade instanceof CarimboDeTempo)
			((CarimboDeTempo) entidade).setHisDtAlt(this.dt());
		super.gravar(entidade);
		invalidarCache(entidade);
		return entidade;
	}

	public void invalidarCache(Object entidade) {
		if (entidade == null)
			return;
	// Não foi possivel utilizar evictQueries na JPA 2 - Verificar impacto na aplicação
	}
	
	public DpPessoa getPessoaFromSigla(String sigla) {
		DpPessoa p = new DpPessoa();
		p.setSigla(sigla);
		DpPessoa ator = consultarPorSigla(p);
		return ator;
	}

	public DpLotacao getLotacaoFromSigla(String sigla) {
		DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setSiglaCompleta(sigla);
		return (DpLotacao) consultarPorSigla(flt);
	}

	public CpOrgao getOrgaoFromSigla(String sigla) {
		CpOrgao o = new CpOrgao();
		o.setSigla(sigla);
		return consultarPorSigla(o);
	}

	public CpOrgao getOrgaoFromSiglaExata(String sigla) {
		CpOrgao o = new CpOrgao();
		o.setSigla(sigla);

		final Query query = em().createNamedQuery(
				"consultarPorSiglaExataCpOrgao");
		query.setParameter("siglaOrgao", o.getSiglaOrgao());

		final List<CpOrgao> l = query.getResultList();
		if (l.size() > 0)
			return l.get(0);

		return null;
	}

	public List<CpOrgaoUsuario> consultaCpOrgaoUsuario() {
		final Query qry = em().createNamedQuery("consultarCpOrgaoUsuario");

		// Renato: Alterei para fazer cache. Nao vejo porque nao possamos fazer
		// cache dessa consulta.
		qry.setHint("org.hibernate.cacheable", true); 
		qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_HOURS);

		final List<CpOrgaoUsuario> lista = qry.getResultList();
		return lista;
	}

	public List<CpModelo> consultaCpModelos() {
		final List<CpOrgaoUsuario> listaOrgUsu = consultaCpOrgaoUsuario();
		listaOrgUsu.add(0, null);
		final Query qry = em().createNamedQuery("consultarCpModelos");

		qry.setHint("org.hibernate.cacheable", true); 
		qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SECONDS);

		final List<CpModelo> lista = qry.getResultList();
		final List<CpModelo> listaFinal = new ArrayList<CpModelo>();

		for (CpOrgaoUsuario orgUsu : listaOrgUsu) {
			boolean fFound = false;
			for (CpModelo mod : lista) {
				if ((mod.getCpOrgaoUsuario() == null && orgUsu == null)
						|| (mod.getCpOrgaoUsuario() != null && orgUsu != null && mod
								.getCpOrgaoUsuario().getId()
								.equals(orgUsu.getId()))) {
					listaFinal.add(mod);
					fFound = true;
				}
			}
			if (!fFound) {
				CpModelo modNew = new CpModelo();
				modNew.setCpOrgaoUsuario(orgUsu);
				listaFinal.add(modNew);
			}
		}
		return listaFinal;
	}
	
	public CpModelo consultaCpModeloGeral() {
		final Query qry = em().createNamedQuery("consultarCpModeloGeral");
		qry.setHint("org.hibernate.cacheable", true); 
		qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SECONDS);

		final List<CpModelo> lista = qry.getResultList();
		if (lista.size() > 0)
			return lista.get(0);
		else return null;
	}

	public CpModelo consultaCpModeloPorNome(String nome) {
		final Query qry = em().createNamedQuery("consultarCpModeloPorNome");
		qry.setHint("org.hibernate.cacheable", true); 
		qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_SECONDS);
		qry.setParameter("nome", nome);

		final List<CpModelo> lista = qry.getResultList();
		if (lista.size() > 0)
			return lista.get(0);
		else return null;
	}

	public List<CpModelo> listarModelosOrdenarPorNome(String script)
			throws Exception {
		CriteriaQuery<CpModelo> q = cb().createQuery(CpModelo.class);
		Root<CpModelo> c = q.from(CpModelo.class);
		Join<CpModelo, CpOrgaoUsuario> joinOrgao = c.join("cpOrgaoUsuario", JoinType.LEFT);
		q.select(c);
		
		q.where(cb().isNull(c.get("hisDtFim")));
		
		q.orderBy(cb().desc(joinOrgao.get("siglaOrgaoUsu")));
		List<CpModelo> l = new ArrayList<CpModelo>();
		for (CpModelo mod : (List<CpModelo>) em().createQuery(q).getResultList())
			if (script != null && script.trim().length() != 0) {
				if (mod.getConteudoBlobString() != null
						&& mod.getConteudoBlobString().contains(script))
					l.add(mod);
			} else
				l.add(mod);
		return l;
	}

	@SuppressWarnings("unchecked")
	public CpModelo consultarPorIdInicialCpModelo(final Long idInicial) {
		final Query query = em().createNamedQuery(
				"consultarPorIdInicialCpModelo");
		query.setParameter("idIni", idInicial);

		
		final List<CpModelo> l = query.getResultList();
		if (l.size() != 1)
			return null;
		return l.get(0);
	}

	public CpServico consultarPorSiglaCpServico(String siglaServico) {
		final Query query = em().createNamedQuery(
				"consultarPorSiglaStringCpServico");
		query.setParameter("siglaServico", siglaServico);
		// query.setFlushMode(FlushMode.MANUAL);
		final List<CpServico> l = query.getResultList();
		if (l.size() != 1) {
			return null;
		}

		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgaoUsuario> listarOrgaosUsuarios() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpOrgaoUsuario.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpOrgao> listarOrgaos() {
		return findByCriteria(CpOrgao.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpServico> listarServicos() {
		return findAndCacheByCriteria(CACHE_QUERY_SECONDS, CpServico.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpServico> listarServicosPorPai(CpServico servicoPai) {
		CriteriaQuery<CpServico> q = cb().createQuery(CpServico.class);
		Root<CpServico> c = q.from(CpServico.class);
		q.select(c);
		q.where(cb().equal(c.get("cpServicoPai"), servicoPai));
		return em().createQuery(q).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CpTipoGrupo> listarTiposGrupo() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpTipoGrupo.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpConfiguracao> listarConfiguracoes() {
		return findByCriteria(CpConfiguracao.class);
	}

	@SuppressWarnings("unchecked")
	public List<DpLotacao> listarLotacoes() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, DpLotacao.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpTipoLotacao> listarTiposLotacao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpTipoLotacao.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpTipoPessoa> listarTiposPessoa() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpTipoPessoa.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpTipoPapel> listarTiposPapel() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpTipoPapel.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpFeriado> listarFeriados() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpFeriado.class);
	}

	@SuppressWarnings("unchecked")
	public List<CpSituacaoConfiguracao> listarSituacoesConfiguracao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS,
				CpSituacaoConfiguracao.class);
	}

	public List<CpTipoConfiguracao> listarTiposConfiguracao() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS,
				CpTipoConfiguracao.class);
	}

	public List<CpUnidadeMedida> listarUnidadesMedida() {
		return findAndCacheByCriteria(CACHE_QUERY_HOURS, CpUnidadeMedida.class);
	}

	public List<CpMarcador> listarMarcadores(Long[] ids) {
		CriteriaQuery<CpMarcador> q = cb().createQuery(CpMarcador.class);
		Root<CpMarcador> c = q.from(CpMarcador.class);
		q.select(c);
		q.where(c.get("idMarcador").in(ids));
		q.orderBy(cb().asc(c.get("descrMarcador")));
		return em().createQuery(q).getResultList();
	}

	protected CriteriaBuilder cb() {
		return em().getCriteriaBuilder();
	}

	public List<CpGrupoDeEmail> listarGruposDeEmail() {
		return findByCriteria(CpGrupoDeEmail.class);
	}

	public void excluirComHistorico(HistoricoAuditavel entidade, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {

		if (dt == null) {
			dt = consultarDataEHoraDoServidor();
		}
		entidade.setHisDtFim(dt);
		entidade.setHisIdcFim(identidadeCadastrante);
		entidade.setHisAtivo(0);

		gravarComHistorico(entidade, identidadeCadastrante);

	}

	public List<CpGrupo> getGruposGeridos(DpPessoa titular,
			DpLotacao lotaTitular, Long idCpTipoGrupo) throws Exception {
		CpGrupoDaoFiltro flt = new CpGrupoDaoFiltro();
		flt.setIdTpGrupo(idCpTipoGrupo.intValue());
		List<CpGrupo> itgGrupos = consultarPorFiltro(flt, 0, 0);

		Iterator<CpGrupo> it = itgGrupos.iterator();

		while (it.hasNext()) {
			CpGrupo cpGrp = it.next();
			CpConfiguracaoBL bl = Cp.getInstance().getConf();
			if (!bl.podePorConfiguracao(titular, lotaTitular, cpGrp,
					CpTipoConfiguracao.TIPO_CONFIG_GERENCIAR_GRUPO)) {
				it.remove();
			}

		}
		return itgGrupos;
	}

	@SuppressWarnings("unchecked")
	public Object consultaDadosBasicos(final String nmUsuario)
			throws AplicacaoException {
		try {
			final Query qry = em()
					.createNamedQuery("consultarDadosBasicos");
			qry.setParameter("nmUsuario", nmUsuario);
			// Verifica se existe numeros no login do usuario
			if (nmUsuario.substring(2).matches("^[0-9]*$"))
				qry.setParameter("sesbPessoa", nmUsuario.substring(0, 2));
			else
				// se nao ha numeros atribui RJ por default
				qry.setParameter("sesbPessoa", "RJ");

			// Cache was disabled because it would interfere with the
			// "change password" action.
			// qry.setHint("org.hibernate.cacheable", true); 
			// qry.setHint("org.hibernate.cacheRegion", "query.IdentidadeCadastrante");

			final Object obj = qry.getResultList();
			return obj;
		} catch (Throwable e) {
			throw new AplicacaoException(
					"Ocorreu um erro tentando carregar os dados basicos para o usuario '"
							+ nmUsuario + "'.", 0, e);
		}
	}

	public List<DpPessoa> consultarPorMatriculaEOrgao(Long matricula,
			Long idOrgaoUsu, boolean pessoasFinalizadas, boolean ordemDesc) {
		CriteriaQuery<DpPessoa> q = cb().createQuery(DpPessoa.class);
		Root<DpPessoa> c = q.from(DpPessoa.class);
		q.select(c);

		if(matricula != null) {
			q.where(cb().equal(c.get("matricula"), matricula));
		}
		q.where(cb().equal(c.get("orgaoUsuario.idOrgaoUsu"), idOrgaoUsu));

		if (pessoasFinalizadas) {
			q.where(cb().isNotNull(c.get("dataFimPessoa")));
		} else {
			q.where(cb().isNull(c.get("dataFimPessoa")));
		}
		if (ordemDesc) {
			q.orderBy(cb().desc(c.get("dataInicioPessoa")));
		} else {
			q.orderBy(cb().asc(c.get("dataInicioPessoa")));
		}

		return em().createQuery(q).getResultList();
	}

	public List<?> consultarFechadosPorIdExterna(Class<?> clazz,
			String idExterna, Long idOrgaoUsu) {
		if (clazz == DpLotacao.class) {
			CriteriaQuery<DpLotacao> q = cb().createQuery(DpLotacao.class);
			Root<DpLotacao> c = q.from(DpLotacao.class);
			q.select(c);
			q.where(cb().equal(c.get("ideLotacao"), idExterna));
			q.where(cb().equal(c.get("orgaoUsuario.idOrgaoUsu"), idOrgaoUsu));
			q.where(cb().isNotNull(c.get("dataFimLotacao")));
			q.orderBy(cb().desc(c.get("dataInicioLotacao")));
			return em().createQuery(q).getResultList();
		}

		if (clazz == DpCargo.class) {
			CriteriaQuery<DpCargo> q = cb().createQuery(DpCargo.class);
			Root<DpCargo> c = q.from(DpCargo.class);
			q.select(c);
			q.where(cb().equal(c.get("ideCargo"), idExterna));
			q.where(cb().equal(c.get("orgaoUsuario.idOrgaoUsu"), idOrgaoUsu));
			q.where(cb().isNotNull(c.get("dataFimCargo")));
			q.orderBy(cb().desc(c.get("dataInicioCargo")));
			return em().createQuery(q).getResultList();
		}

		if (clazz == DpFuncaoConfianca.class) {
			CriteriaQuery<DpFuncaoConfianca> q = cb().createQuery(DpFuncaoConfianca.class);
			Root<DpFuncaoConfianca> c = q.from(DpFuncaoConfianca.class);
			q.select(c);
			q.where(cb().equal(c.get("ideFuncao"), idExterna));
			q.where(cb().equal(c.get("orgaoUsuario.idOrgaoUsu"), idOrgaoUsu));
			q.where(cb().isNotNull(c.get("dataFimFuncao")));
			q.orderBy(cb().desc(c.get("dataInicioFuncao")));
			return em().createQuery(q).getResultList();
		}

		return null;
	}

	public DpLotacao obterLotacaoAtual(final DpLotacao lotacao) {
		try {

			final Query qry = em().createNamedQuery(
					"consultarLotacaoAtualPelaLotacaoInicial");
			qry.setParameter("idLotacaoIni", lotacao.getIdLotacaoIni());
			qry.setHint("org.hibernate.cacheable", true); 
			qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
			final DpLotacao lot = (DpLotacao) qry.getSingleResult();
			return lot;
		} catch (final IllegalArgumentException e) {
			throw e;

		} catch (final Exception e) {
			return null;
		}
	}

	public DpPessoa obterPessoaAtual(final DpPessoa pessoa) {
		try {
			final Query qry = em().createNamedQuery(
					"consultarPessoaAtualPelaInicial");
			qry.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
			qry.setHint("org.hibernate.cacheable", true); 
			qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
			final DpPessoa pes = (DpPessoa) qry.getSingleResult();
			return pes;
		} catch (final IllegalArgumentException e) {
			throw e;

		} catch (final Exception e) {
			return null;
		}
	}
	
	public CpIdentidade obterIdentidadeAtual(final CpIdentidade u) {
		try {
			final Query qry = em().createNamedQuery(
					"consultarIdentidadeAtualPelaInicial");
			qry.setParameter("idIni", u.getHisIdIni());
			final CpIdentidade id = (CpIdentidade) qry.getSingleResult();
			return id;
		} catch (final IllegalArgumentException e) {
			throw e;

		} catch (final Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public CpAcesso consultarAcessoAnterior(final DpPessoa pessoa) {
		String sql = "from CpAcesso a where a.cpIdentidade.dpPessoa.idPessoaIni = :idPessoaIni order by a.dtInicio desc";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
		query.setFirstResult(1);
		query.setMaxResults(1);
		List<CpAcesso> result = query.getResultList();
		if (result == null || result.size() == 0)
			return null;
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<CpAcesso> consultarAcessosRecentes(final DpPessoa pessoa) {
		String sql = "from CpAcesso a where a.cpIdentidade.dpPessoa.idPessoaIni = :idPessoaIni order by a.dtInicio desc";
		javax.persistence.Query query = ContextoPersistencia.em().createQuery(sql);
		query.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<CpAcesso> result = query.getResultList();
		if (result == null || result.size() == 0)
			return null;
		return result;
	}

	public int consultarQuantidadeDocumentosPorDpLotacao(final DpLotacao o) {
        try {
			Query sql = em().createNativeQuery(
					"consultarQuantidadeDocumentosPorDpLotacao");

			sql.setParameter("idLotacao", o.getId());
            sql.setParameter("idTipoMarca", CpTipoMarca.TIPO_MARCA_SIGA_EX);
        	
            final int l = ((BigDecimal) sql.getSingleResult()).intValue();
            return l;
        } catch (final NullPointerException e) {
            return 0;
        }
    }
	
	public List<CpOrgaoUsuario> consultarOrgaosMarcadosComo (final Long orgaoUsuId,
			final Long lotacaoId, final Long usuarioId, Date dataInicial, Date dataFinal, Long idMarcador) {
		String queryOrgao = "";
		if (orgaoUsuId != null) {
			queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
		}
		String queryLotacao = "";
		if (lotacaoId != null) {
			queryLotacao = "and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
		}
		String queryUsuario = "";
		if (usuarioId != null) {
			queryUsuario = "and mov.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :idUsuario) ";
		}
		String queryTemp = 
				"select distinct "
					+ "(select orgaoUsu from CpOrgaoUsuario orgaoUsu where "
					+ "		(orgaoUsu1 is null or orgaoUsu.idOrgaoUsu = orgaoUsu1.idOrgaoUsu) "	
					+ "		and (orgaoUsu2 is null or orgaoUsu.idOrgaoUsu = orgaoUsu2.idOrgaoUsu)) "
					+ "from ExMarca mar " 
					+ "inner join mar.exMobil mob " 
					+ "inner join mob.exDocumento doc "
					+ "inner join mar.cpMarcador as marcador "
					+ "left join mar.dpLotacaoIni.orgaoUsuario orgaoUsu1 "
					+ "left join mar.dpPessoaIni.orgaoUsuario orgaoUsu2 "
					+ "where doc.dtDoc >= :dtini and doc.dtDoc < :dtfim "
					+ "		and doc.dtFinalizacao is not null "
					+ queryOrgao
					+ queryLotacao
					+ queryUsuario
					+ "and marcador.idMarcador = :idMarcador " 
					+ "and (dt_ini_marca is null or dt_ini_marca < sysdate) " 
					+ "and (dt_fim_marca is null or dt_fim_marca > sysdate) " 
				;
		
		Query query = em().createQuery(queryTemp);
				
		query.setParameter("idMarcador", idMarcador);
		
		if (orgaoUsuId != null) {
			query.setParameter("orgao", orgaoUsuId);
		}
		if (lotacaoId != null) {
			query.setParameter("idLotacao", lotacaoId);
		}
		if (usuarioId != null) {
			query.setParameter("idUsuario", usuarioId);
		}
		query.setParameter("dtini", dataInicial);
		Date dtfimMaisUm = new Date( dataFinal.getTime() + 86400000L );
		query.setParameter("dtfim", dtfimMaisUm);
		
		List<CpOrgaoUsuario> l = query.getResultList();
		
		if(l.size() == 0) {
			return null;
		}
		return l;
	}

		
	public List<DpPessoa> obterPessoasDoUsuario(DpPessoa pessoa) {
		final Query qry = em().createNamedQuery(
				"consultarPorIdInicialDpPessoaInclusiveFechadas");
		qry.setParameter("idPessoaIni", pessoa.getIdPessoaIni());
		qry.setHint("org.hibernate.cacheable", true); 
		qry.setHint("org.hibernate.cacheRegion", CACHE_QUERY_CONFIGURACAO);
		return qry.getResultList();
	}

	public List<CpConfiguracao> consultarCpConfiguracoesPorTipoLotacao(long idTpLotacao) {
		final Query qry = em().createNamedQuery(
				"consultarCpConfiguracoesPorTipoLotacao");
		qry.setParameter("idTpLotacao", idTpLotacao);
		return qry.getResultList();
	}

	public Integer quantidadeDocumentos(DpPessoa pes) {
		try {
			Query sql = (Query) em().createNamedQuery(
					"quantidadeDocumentos");

			sql.setParameter("idPessoaIni", pes.getIdPessoaIni());
			List result = sql.getResultList();
			final int l = ((BigDecimal) sql.getSingleResult()).intValue();
            return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}
	
	public Integer consultarQtdeDocCriadosPossePorDpLotacao(Long idLotacao) {
		try {
			Query sql = (Query) em().createNamedQuery(
					"consultarQtdeDocCriadosPossePorDpLotacao");

			sql.setParameter("idLotacao", idLotacao);
			List result = sql.getResultList();
			final int l = ((BigDecimal) sql.getSingleResult()).intValue();
            return l;
		} catch (final NullPointerException e) {
			return null;
		}
	}
}