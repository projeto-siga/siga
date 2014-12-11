package br.gov.jfrj.siga.model;

import javax.persistence.EntityManager;

public class ContextoPersistencia {

	private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<EntityManager>();

	static public void setEntityManager(EntityManager em) {
		emByThread.set(em);
	}

	static public EntityManager em() {
		return emByThread.get();
	}
}