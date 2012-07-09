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
package br.gov.jfrj.siga.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import br.gov.jfrj.siga.base.AplicacaoException;

public abstract class ModeloDao implements ApplicationContextAware {

	protected Session sessao;

	protected String cacheRegion = null;

	private static final ThreadLocal<ModeloDao> threadDao = new ThreadLocal<ModeloDao>();
	private static ApplicationContext appContext;

	protected ModeloDao() {

	}

	@SuppressWarnings("unchecked")
	protected static <T extends ModeloDao> T getInstance(Class<T> clazz,
			Session sessao) {
		T dao = null;

		try {
			dao = (T) ModeloDao.threadDao.get();
		} catch (Exception e) {
			// quando ocorrer algum problema, recriar o dao.
			System.out.println(e.getStackTrace());
		}

		// Cria um novo Dao se ainda não houver
		if (dao == null) {
			try {
				dao = clazz.newInstance();
				if (sessao == null) {
					Session s = getSpringSession();
					if (s == null) {
						s = HibernateUtil.getSessao();
					}
					dao.sessao = s;
				} else {
					dao.sessao = sessao;
				}
			} catch (Exception e) {
				throw new Error(e);
			}
			ModeloDao.threadDao.set(dao);
		}
		return dao;
	}

	protected static <T extends ModeloDao> T getInstance(Class<T> clazz) {
		return getInstance(clazz, null);
	}

	protected static Session getSpringSession() {
		if (appContext != null) {
			return ((SessionFactory) appContext.getBean("sessionFactory"))
					.getCurrentSession();
		}
		return (null);
	}

	public synchronized static void freeInstance() {
		HibernateUtil.fechaSessaoSeEstiverAberta();

		final ModeloDao dao = ModeloDao.threadDao.get();

		// fecha o dao e a seção do hibernate
		if (dao != null) {
			ModeloDao.threadDao.remove();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T> T consultar(final Serializable id, Class<T> clazz,
			final boolean lock) {
		T entidade;
		if (lock)
			entidade = (T) getSessao().load(clazz, id, LockMode.UPGRADE);
		else
			entidade = (T) getSessao().load(clazz, id);

		return entidade;

	}

	@SuppressWarnings("unchecked")
	public <T> List<T> consultar(final T exemplo, final String[] excluir) {
		final Criteria crit = getSessao().createCriteria(exemplo.getClass());
		final Example example = Example.create(exemplo);
		if (excluir != null) {
			for (final String exclude : excluir) {
				example.excludeProperty(exclude);
			}
		}
		crit.add(example);
		if (getCacheRegion() != null) {
			crit.setCacheable(true);
			crit.setCacheRegion(getCacheRegion());
		}
		return crit.list();
	}

	public void excluir(final Object entidade) {
		getSessao().delete(entidade);
	}

	/**
	 * @return Retorna o atributo sessao.
	 */
	public Session getSessao() {
		if (sessao == null)
			throw new IllegalStateException(
					"Variável Session não foi atribuída para este DAO");
		return sessao;
	}

	/**
	 * @return Retorna a sessionFactory do Hibernate
	 */
	public SessionFactory getFabricaDeSessao() {
		return HibernateUtil.getSessionFactory();
	}

	public <T> T gravar(final T entidade) {

		getSessao().saveOrUpdate(entidade);
		return entidade;
	}

	public <T> List<T> listarTodos(Class<T> clazz) {
		// Criteria crit = getSessao().createCriteria(getPersistentClass());
		// return crit.list();
		return findByCriteria(clazz);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> findByCriteria(Class<T> clazz,
			final Criterion... criterion) {
		final Criteria crit = getSessao().createCriteria(clazz);
		for (final Criterion c : criterion) {
			crit.add(c);
		}
		// if (getCacheRegion() != null) {
		// crit.setCacheable(true);
		// crit.setCacheRegion(getCacheRegion());
		// }
		return crit.list();
	}

	public <T> T consultarPorSigla(final T exemplo) {
		return null;
	}

	public String getCacheRegion() {
		return cacheRegion;
	}

	public void setCacheRegion(String cacheRegion) {
		this.cacheRegion = cacheRegion;
	}

	public static void iniciarTransacao() {
		HibernateUtil.iniciarTransacao();
	}

	public static void commitTransacao() throws AplicacaoException {
		HibernateUtil.commitTransacao();
	}

	public static void rollbackTransacao() {
		HibernateUtil.rollbackTransacao();
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		appContext = applicationContext;
	}

	public static void configurarHibernateParaDebug(AnnotationConfiguration cfg) {
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.format_sql", "true");
		cfg.setProperty("generate_statistics", "true");
		cfg.setProperty("hibernate.use_sql_comments", "true");		
	}
	
	/**
	 * @return true se a sessão do Hibernate não for nula e estiver aberta.
	 */
	public boolean sessaoEstahAberta(){
		return this.getSessao() != null && this.getSessao().isOpen(); 
	}
	
	/**
	 * @return true se a transacao da sessão do Hibernate estiver ativa
	 */
	public boolean transacaoEstaAtiva(){
		return this.getSessao() != null && this.getSessao().isOpen() && 
				this.getSessao().getTransaction() != null && 
					this.getSessao().getTransaction().isActive();
	}
}
