package br.gov.jfrj.siga.model;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;

public class ContextoPersistencia {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();
	private final static ThreadLocal<String> userPrincipalByThread = new ThreadLocal<String>();
	private final static ThreadLocal<Date> dataEHoraDoServidor = new ThreadLocal<Date>();
	private final static ThreadLocal<UsuarioDeSistemaEnum> usuarioDeSistema = new ThreadLocal<UsuarioDeSistemaEnum>();
	
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
		if (transaction != null && transaction.isActive()) {
			transaction.commit();
			System.out.println(transaction.isActive());
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
	
	static public void setDt(Date dt) {
		dataEHoraDoServidor.set(dt);
	}

	static public Date dt() {
		return dataEHoraDoServidor.get();
	}
	
	static public void setUsuarioDeSistema(UsuarioDeSistemaEnum u) {
		usuarioDeSistema.set(u);
	}

	static public UsuarioDeSistemaEnum getUsuarioDeSistema() {
		return usuarioDeSistema.get();
	}

	static public void removeUsuarioDeSistema() {
		usuarioDeSistema.remove();
	}
	
	static public void removeAll() {
		removeUsuarioDeSistema();
		removeUserPrincipal();
		setDt(null);
	}
	
	
	

}