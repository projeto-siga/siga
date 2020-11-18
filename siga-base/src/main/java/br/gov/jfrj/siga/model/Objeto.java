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
package br.gov.jfrj.siga.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.collection.internal.PersistentMap;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.CollectionEntry;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.internal.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;


//@MappedSuperclass
public class Objeto extends ObjetoBase{

//	@Override
//	public String toString() {
//		return ClassUtils.toString(this);
//	}
//
//	public boolean equals(Object obj) {
//		return this == getImplementation(obj);
//	}

	public static Object getImplementation(Object obj) {
		if (obj instanceof HibernateProxy) {
			return ((HibernateProxy) obj).getHibernateLazyInitializer()
					.getImplementation();
		} else {
			return obj;
		}
	}

	public Object getImplementation() {
		if (this instanceof HibernateProxy) {
			return ((HibernateProxy) this).getHibernateLazyInitializer()
					.getImplementation();
		} else {
			return this;
		}
	}
	
	public boolean isInstance(Class clazz) {
		return this.getClass().isAssignableFrom(clazz);
	}

	public Objeto unproxy() {
		return this;
	}

	public Objeto unproxyIfInstance(Class clazz) {
		return isInstance(clazz) ? this : null;
	}
	
	private static final long serialVersionUID = -7830448334427331897L;

	protected static EntityManager em() {
		return ContextoPersistencia.em();
	}

	public void save() {
		if (!em().contains(this)) {
			em().persist(this);
		}

		/*
	  	avoidCascadeSaveLoops.set(new HashSet<Objeto>());
		try {
			saveAndCascade(true);
		} catch (UnexpectedException e) {
			throw new RuntimeException(e);
		} finally {
			avoidCascadeSaveLoops.get().clear();
		} 
		try {
			em().flush();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof GenericJDBCException) {
				throw new PersistenceException(
						((GenericJDBCException) e.getCause()).getSQL(), e);
			} else {
				throw e;
			}
		}
		avoidCascadeSaveLoops.set(new HashSet<Objeto>());
		try {
			saveAndCascade(false);
		} catch (UnexpectedException e) {
			throw new RuntimeException(e);
		} finally {
			avoidCascadeSaveLoops.get().clear();
		}
		*/
	}
	
	public void refresh() {
		em().refresh(this);
	}

	public void delete() {
		try {
			avoidCascadeSaveLoops.set(new HashSet<Objeto>());
			try {
				saveAndCascade(true);
			} finally {
				avoidCascadeSaveLoops.get().clear();
			}
			em().remove(this);
			try {
				em().flush();
			} catch (PersistenceException e) {
				if (e.getCause() instanceof GenericJDBCException) {
					throw new PersistenceException(
							((GenericJDBCException) e.getCause()).getSQL(), e);
				} else {
					throw e;
				}
			}
			avoidCascadeSaveLoops.set(new HashSet<Objeto>());
			try {
				saveAndCascade(false);
			} finally {
				avoidCascadeSaveLoops.get().clear();
			}
		} catch (PersistenceException e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public Object _key() {
		if (em() == null){
			return null;
		}
		try {
		Session session = (Session) (em().getDelegate());
		ClassMetadata meta = session.getSessionFactory().getClassMetadata(
				this.getClass());
		return meta.getIdentifier(this, (SessionImplementor) session);
		} catch (Exception ex) {
			return null;
		}
	}

	// ~~~ SAVING
	public transient boolean willBeSaved = false;
	static transient ThreadLocal<Set<Objeto>> avoidCascadeSaveLoops = new ThreadLocal<Set<Objeto>>();

	private void saveAndCascade(boolean willBeSaved) throws UnexpectedException {
		this.willBeSaved = willBeSaved;
		if (avoidCascadeSaveLoops.get().contains(this)) {
			return;
		} else {
			avoidCascadeSaveLoops.get().add(this);
			if (willBeSaved) {
			}
		}
		// Cascade save
		try {
			Set<Field> fields = new HashSet<Field>();
			Class clazz = this.getClass();
			while (!clazz.equals(Objeto.class)) {
				Collections.addAll(fields, clazz.getDeclaredFields());
				clazz = clazz.getSuperclass();
			}
			for (Field field : fields) {
				field.setAccessible(true);
				if (Modifier.isTransient(field.getModifiers())) {
					continue;
				}
				boolean doCascade = false;
				if (field.isAnnotationPresent(OneToOne.class)) {
					doCascade = cascadeAll(field.getAnnotation(OneToOne.class)
							.cascade());
				}
				if (field.isAnnotationPresent(OneToMany.class)) {
					doCascade = cascadeAll(field.getAnnotation(OneToMany.class)
							.cascade());
				}
				if (field.isAnnotationPresent(ManyToOne.class)) {
					doCascade = cascadeAll(field.getAnnotation(ManyToOne.class)
							.cascade());
				}
				if (field.isAnnotationPresent(ManyToMany.class)) {
					doCascade = cascadeAll(field
							.getAnnotation(ManyToMany.class).cascade());
				}
				if (doCascade) {
					Object value = field.get(this);
					if (value != null) {
						if (value instanceof PersistentMap) {
							if (((PersistentMap) value).wasInitialized()) {

								cascadeOrphans(this,
										(PersistentCollection) value,
										willBeSaved);

								for (Object o : ((Map) value).values()) {
									saveAndCascadeIfObjeto(o,
											willBeSaved);
								}
							}
						} else if (value instanceof PersistentCollection) {
							PersistentCollection col = (PersistentCollection) value;
							if (((PersistentCollection) value).wasInitialized()) {

								cascadeOrphans(this,
										(PersistentCollection) value,
										willBeSaved);

								for (Object o : (Collection) value) {
									saveAndCascadeIfObjeto(o,
											willBeSaved);
								}
							} else {
								cascadeOrphans(this, col, willBeSaved);

								for (Object o : (Collection) value) {
									saveAndCascadeIfObjeto(o,
											willBeSaved);
								}
							}
						} else if (value instanceof Collection) {
							for (Object o : (Collection) value) {
								saveAndCascadeIfObjeto(o,
										willBeSaved);
							}
						} else if (value instanceof HibernateProxy
								&& value instanceof Objeto) {
							if (!((HibernateProxy) value)
									.getHibernateLazyInitializer()
									.isUninitialized()) {
								((Objeto) ((HibernateProxy) value)
										.getHibernateLazyInitializer()
										.getImplementation())
										.saveAndCascade(willBeSaved);
							}
						} else if (value instanceof Objeto) {
							((Objeto) value)
									.saveAndCascade(willBeSaved);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new UnexpectedException("During cascading save()", e);
		}
	}

	private static void cascadeOrphans(Objeto base,
			PersistentCollection persistentCollection, boolean willBeSaved)
			throws UnexpectedException {
		SessionImpl session = ((SessionImpl) em().getDelegate());
		PersistenceContext pc = session.getPersistenceContext();
		CollectionEntry ce = pc.getCollectionEntry(persistentCollection);

		if (ce != null) {
			CollectionPersister cp = ce.getLoadedPersister();
			if (cp != null) {
				Type ct = cp.getElementType();
				if (ct instanceof EntityType) {
					EntityEntry entry = pc.getEntry(base);
					String entityName = entry.getEntityName();
					entityName = ((EntityType) ct)
							.getAssociatedEntityName(session.getFactory());
					if (ce.getSnapshot() != null) {
						Collection orphans = ce.getOrphans(entityName,
								persistentCollection);
						for (Object o : orphans) {
							saveAndCascadeIfObjeto(o, willBeSaved);
						}
					}
				}
			}
		}
	}

	private static void saveAndCascadeIfObjeto(Object o,
			boolean willBeSaved) throws UnexpectedException {
		if (o instanceof Objeto) {
			((Objeto) o).saveAndCascade(willBeSaved);
		}
	}

	private static boolean cascadeAll(CascadeType[] types) {
		for (CascadeType cascadeType : types) {
			if (cascadeType == CascadeType.ALL
					|| cascadeType == CascadeType.PERSIST) {
				return true;
			}
		}
		return false;
	}

	public boolean isPersistent() {
		return em().contains(this);
	}

	/**
	 * JPASupport instances a and b are equals if either <strong>a == b</strong>
	 * or a and b have same </strong>{@link #_key key} and class</strong>
	 *
	 * @param other
	 * @return true if equality condition above is verified
	 */
	@Override
	public boolean equals(Object other) {
		final Object key = this._key();

		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (key == null) {
			return false;
		}
		if (Objeto.class.isAssignableFrom(other.getClass())
				&& key.getClass().isArray()) {
			Object otherKey = ((Objeto) other)._key();
			if (otherKey.getClass().isArray()) {
				return Arrays.deepEquals((Object[]) key, (Object[]) otherKey);
			}
			return false;
		}

		if (!this.getClass().isAssignableFrom(other.getClass())) {
			return false;
		}

		return key.equals(((Objeto) other)._key());
	}

	@Override
	public int hashCode() {
		final Object key = this._key();
		if (key == null) {
			return 0;
		}
		if (key.getClass().isArray()) {
			return Arrays.deepHashCode((Object[]) key);
		}
		return key.hashCode();
	}

	@Override
	public String toString() {
		final Object key = this._key();
		String keyStr = "";
		if (key != null && key.getClass().isArray()) {
			for (Object object : (Object[]) key) {
				keyStr += object.toString() + ", ";
			}
			keyStr = keyStr.substring(0, keyStr.length() - 2);
		} else if (key != null) {
			keyStr = key.toString();
		}
		return getClass().getSimpleName() + "[" + keyStr + "]";
	}

	public static class JPAQueryException extends RuntimeException {

		public JPAQueryException(String message) {
			super(message);
		}

		public JPAQueryException(String message, Throwable e) {
			super(message + ": " + e.getMessage(), e);
		}

		public static Throwable findBestCause(Throwable e) {
			Throwable best = e;
			Throwable cause = e;
			int it = 0;
			while ((cause = cause.getCause()) != null && it++ < 10) {
				if (cause instanceof ClassCastException) {
					best = cause;
					break;
				}
				if (cause instanceof SQLException) {
					best = cause;
					break;
				}
			}
			return best;
		}
	}

}
