package br.gov.jfrj.siga.model;

import javax.persistence.EntityManager;

import org.hibernate.Session;

public class ContextoPersistencia {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();
	private final static ThreadLocal<String> userPrincipalByThread = new ThreadLocal<String>();

	static public void setEntityManager(EntityManager em) {
		emByThread.set(em);
	}

	static public EntityManager em() {
		return emByThread.get();
	}

	static public void flush() {
		em().flush();
		((Session) (em().getDelegate())).flush();
	}
	
	static public void flushTransaction() {
		flush();
		em().getTransaction().commit();
		em().getTransaction().begin();
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