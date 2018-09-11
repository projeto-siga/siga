package br.gov.jfrj.siga.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ContextoPersistencia {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();
	private final static ThreadLocal<String> userPrincipalByThread = new ThreadLocal<String>();

	public static EntityManagerFactory emf;

	static public void initEntityManagerFactory(String name) {
		emf = Persistence.createEntityManagerFactory(name);
	}

	static public void setEntityManager(EntityManager em) {
		emByThread.set(em);
	}

	static public EntityManager em() {
		return emByThread.get();
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