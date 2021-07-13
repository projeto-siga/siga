package br.gov.jfrj.siga.model.dao.extended;

import static br.gov.jfrj.siga.model.dao.extended.TxManager.debugLogging;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAQuery;

@SuppressWarnings("rawtypes")
public final class QueryDslExtendedThreadScopedEntityManager implements EntityManager, AutoCloseable {

	private static final Logger log = Logger.getLogger(QueryDslExtendedThreadScopedEntityManager.class.getCanonicalName());

	private static final String DEFAULT_EMF_PU = "default";

	private static final ThreadLocal<Entry<QueryDslExtendedThreadScopedEntityManager, Integer>> THREADBINDER = new ThreadLocal<>();

	private static EntityManagerFactory lazyFactory;

	/**
	 * Initializes the database connection using persistence unit definitions.
	 * 
	 * @return {@link EntityManagerFactory}
	 */
	private static EntityManagerFactory getFactory() {
		if (lazyFactory == null) {
			lazyFactory = Persistence.createEntityManagerFactory(DEFAULT_EMF_PU);
			log.info("Global EntityManagerFactory successfully created.");
		}
		return lazyFactory;
	}

	/**
	 * Creates an instance of {@link EntityManager} and binds it to the request
	 * thread (resource will be released on try close).
	 * 
	 * @return
	 */
	public static QueryDslExtendedThreadScopedEntityManager getScopedManager() {
		Entry<QueryDslExtendedThreadScopedEntityManager, Integer> entry = THREADBINDER.get();
		QueryDslExtendedThreadScopedEntityManager manager;
		if (entry == null) {
			EntityManager delegate = getFactory().createEntityManager();
			manager = new QueryDslExtendedThreadScopedEntityManager(delegate);
			entry = new AbstractMap.SimpleEntry<>(manager, 1); // Initializing stack index to 1.
			THREADBINDER.set(entry);
			if (debugLogging) {
				log.finest("Successfully initialized thread-binded EntityManager UUID " + manager.uuid + "/" + entry.getValue() + ".");
			}
		} else {
			manager = entry.getKey();
			entry.setValue(entry.getValue() + 1); // Increasing stack index.
			if (debugLogging) {
				log.finest("Getting EntityManager UUID " + manager.uuid + "/" + entry.getValue() + " previously binded in this thread.");
			}
		}
		return manager;
	}

	/**
	 * Properly frees the EntityManager resources.
	 */
	private static void freeScopedManager() {
		Entry<QueryDslExtendedThreadScopedEntityManager, Integer> entry = THREADBINDER.get();
		if (entry != null) {
			int newStackIndex = entry.getValue() - 1;
			entry.setValue(newStackIndex);
			QueryDslExtendedThreadScopedEntityManager manager = entry.getKey();
			if (newStackIndex < 1) {
				if (manager.isOpen()) {
					manager.delegate.close();
				}
				THREADBINDER.remove();
				if (debugLogging) {
					log.finest("Thread-binded EntityManager " + manager.uuid + "/" + entry.getValue() + " properly invalidated.");
				}
			} else {
				if (debugLogging) {
					log.finest("Thread-binded EntityManager " + manager.uuid + "/" + entry.getValue() + " still working on this thread.");
				}
			}
		} else {
			log.severe("EntityManager illegal state: this is likely result of a MEMORY LEAK and never should happen!");
			throw new IllegalStateException("Unable to properly close EntityManager");
		}
	}

	/*
	 * EntityManager properties.
	 */

	private final String uuid = UUID.randomUUID().toString();

	private final EntityManager delegate;

	public QueryDslExtendedThreadScopedEntityManager(EntityManager entityManager) {
		this.delegate = entityManager;
	}

	/*
	 * EntityManager methods.
	 */

	@Override
	public void persist(Object entity) {
		delegate.persist(entity);
	}

	@Override
	public <T> T merge(T entity) {
		return delegate.merge(entity);
	}

	@Override
	public void remove(Object entity) {
		delegate.remove(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return delegate.find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return delegate.find(entityClass, primaryKey, properties);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return delegate.find(entityClass, primaryKey, lockMode);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return delegate.find(entityClass, primaryKey, lockMode, properties);
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return delegate.getReference(entityClass, primaryKey);
	}

	@Override
	public void flush() {
		delegate.flush();
	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		delegate.setFlushMode(flushMode);
	}

	@Override
	public FlushModeType getFlushMode() {
		return delegate.getFlushMode();
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		delegate.lock(entity, lockMode);
	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		delegate.lock(entity, lockMode, properties);
	}

	@Override
	public void refresh(Object entity) {
		delegate.refresh(entity);
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		delegate.refresh(entity, properties);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		delegate.refresh(entity, lockMode);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		delegate.refresh(entity, lockMode, properties);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public void detach(Object entity) {
		delegate.detach(entity);
	}

	@Override
	public boolean contains(Object entity) {
		return delegate.contains(entity);
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return delegate.getLockMode(entity);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		delegate.setProperty(propertyName, value);
	}

	@Override
	public Map<String, Object> getProperties() {
		return delegate.getProperties();
	}

	@Override
	public Query createQuery(String qlString) {
		return delegate.createQuery(qlString);
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return delegate.createQuery(criteriaQuery);
	}

	@Override
	public Query createQuery(CriteriaUpdate updateQuery) {
		return delegate.createQuery(updateQuery);
	}

	@Override
	public Query createQuery(CriteriaDelete deleteQuery) {
		return delegate.createQuery(deleteQuery);
	}

	@Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return delegate.createQuery(qlString, resultClass);
	}

	@Override
	public Query createNamedQuery(String name) {
		return delegate.createNamedQuery(name);
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return delegate.createNamedQuery(name, resultClass);
	}

	@Override
	public Query createNativeQuery(String sqlString) {
		return delegate.createNativeQuery(sqlString);
	}

	@Override
	public Query createNativeQuery(String sqlString, Class resultClass) {
		return delegate.createNativeQuery(sqlString, resultClass);
	}

	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return delegate.createNativeQuery(sqlString, resultSetMapping);
	}

	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return delegate.createNamedStoredProcedureQuery(name);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return delegate.createStoredProcedureQuery(procedureName);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return delegate.createStoredProcedureQuery(procedureName, resultClasses);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return delegate.createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	@Override
	public void joinTransaction() {
		delegate.joinTransaction();
	}

	@Override
	public boolean isJoinedToTransaction() {
		return delegate.isJoinedToTransaction();
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return delegate.unwrap(cls);
	}

	@Override
	public Object getDelegate() {
		return delegate.getDelegate();
	}

	@Override
	public void close() {
		freeScopedManager();
	}

	@Override
	public boolean isOpen() {
		return delegate.isOpen();
	}

	@Override
	public EntityTransaction getTransaction() {
		return delegate.getTransaction();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return delegate.getEntityManagerFactory();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return delegate.getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return delegate.getMetamodel();
	}

	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return delegate.createEntityGraph(rootType);
	}

	@Override
	public EntityGraph<?> createEntityGraph(String graphName) {
		return delegate.createEntityGraph(graphName);
	}

	@Override
	public EntityGraph<?> getEntityGraph(String graphName) {
		return delegate.getEntityGraph(graphName);
	}

	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return delegate.getEntityGraphs(entityClass);
	}

	/*
	 * EntityManager extended methods.
	 */

	public <T> List<T> findAll(Class<T> type) {
		return delegate.createQuery("SELECT x FROM " + type.getSimpleName() + " x", type).getResultList();
	}

	public <T> T persistAndGet(T entity) {
		persist(entity);
		return entity;
	}

	public <T> T removeAndGet(T entity) {
		remove(entity);
		return entity;
	}

	public <T> List<T> persistMany(Iterable<T> entities) {
		List<T> results = new ArrayList<>();
		for (Iterator<T> i = entities.iterator(); i.hasNext();) {
			results.add(persistAndGet(i.next()));
		}
		return results;
	}

	public <T> List<T> mergeMany(Iterable<T> entities) {
		List<T> results = new ArrayList<>();
		for (Iterator<T> i = entities.iterator(); i.hasNext();) {
			results.add(merge(i.next()));
		}
		return results;
	}

	/*
	 * QueryDSL methods.
	 */

	protected <R> AbstractJPAQuery<R, JPAQuery<R>> createQuery() {
		return new JPAQuery<R>(delegate);
	}

	protected AbstractJPAQuery<Object, JPAQuery<Object>> createQuery(EntityPath<?> root) {
		return createQuery().from(root);
	}

	protected JPQLQuery<?> createQuery(EntityPath<?> root, Predicate... predicates) {
		JPQLQuery<?> query = createQuery(root);
		if (predicates != null && predicates.length > 0) {
			query.where(predicates);
		}
		return query;
	}

	protected <R> JPQLQuery<R> applyPagination(PageRequest request, JPQLQuery<R> query) {
		if (request == null) {
			return query;
		}
		query.offset(request.getOffset());
		query.limit(request.getSize());
		return query;
	}

	protected <R> JPQLQuery<R> applyOrders(OrderSpecifier<? extends Comparable<?>>[] orders, JPQLQuery<R> query) {
		if (orders == null) {
			return query;
		}
		return query.orderBy(orders);
	}

	public <R> Page<R> findAll(PageRequest pageRequest, EntityPath<R> root, Predicate predicates[], OrderSpecifier<? extends Comparable<?>>[] orders) {
		JPQLQuery<R> countQuery = createQuery(root, predicates).select(root);
		JPQLQuery<R> fetchQuery = applyPagination(pageRequest, createQuery(root, predicates).select(root));
		return Page.of(pageRequest, applyOrders(orders, fetchQuery).fetch(), countQuery.fetchCount());
	}

	public <R> Page<R> findAll(PageRequest pageRequest, EntityPath<R> root, Predicate... predicates) {
		return findAll(pageRequest, root, predicates, null);
	}

	public <R> List<R> findAll(EntityPath<R> root, Predicate[] predicates, OrderSpecifier<? extends Comparable<?>>[] orders) {
		JPQLQuery<R> query = createQuery(root, predicates).select(root);
		return applyOrders(orders, query).fetch();
	}

	public <R> List<R> findAll(EntityPath<R> root, Predicate... predicates) {
		return findAll(root, predicates, null);
	}

	public <R> R find(EntityPath<R> root, Predicate... predicates) {
		return createQuery(root, predicates).select(root).fetchOne();
	}

}
