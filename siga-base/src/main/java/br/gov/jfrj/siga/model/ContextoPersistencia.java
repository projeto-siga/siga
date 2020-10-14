package br.gov.jfrj.siga.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ContextoPersistencia {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();
	private final static ThreadLocal<String> userPrincipalByThread = new ThreadLocal<String>();

	static public void setEntityManager(EntityManager em) {
		emByThread.set(em);
	}

	static public EntityManager getEntityManager() {
		EntityManager em = emByThread.get();
		return em;
	}
	
	static public EntityManager em() {
		EntityManager em = emByThread.get();
		if (em == null)
			throw new RuntimeException("EM nulo!");
		return em;
	}

	static public void flushTransaction() {
		if (em().getTransaction() != null && em().getTransaction().isActive()) {
			em().flush();
			em().getTransaction().commit();
			em().getTransaction().begin();
		}
	}
	
	public static void begin() {
		EntityTransaction transaction = em().getTransaction();
		if (transaction != null && !transaction.isActive()) {
			transaction.begin();
		}
	}

	public static void commit() {
		EntityTransaction transaction = em().getTransaction();
 		em().flush();
		if (transaction != null && transaction.isActive()) {
			em().getTransaction().commit();
		}
 	}
	
	static public void setUserPrincipal(String userPrincipal) {
		userPrincipalByThread.set(userPrincipal);
	}

	static public String getUserPrincipal() {
		return userPrincipalByThread.get();
	}

	static public void removeUserPrincipal() {
		userPrincipalByThread.remove();
	}
}