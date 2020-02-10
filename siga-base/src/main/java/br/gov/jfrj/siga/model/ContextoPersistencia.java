package br.gov.jfrj.siga.model;

import java.util.Date;

import javax.persistence.EntityManager;

public class ContextoPersistencia {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();
	private final static ThreadLocal<String> userPrincipalByThread = new ThreadLocal<String>();
	private final static ThreadLocal<Date> dataEHoraDoServidor = new ThreadLocal<Date>();

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
		em().flush();
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

	static public void setDt(Date dt) {
		dataEHoraDoServidor.set(dt);
	}

	static public Date dt() {
		return dataEHoraDoServidor.get();
	}

}