package br.gov.jfrj.siga.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;

public class ContextoPersistencia {

    private final static ThreadLocal<EntityManager> emByThread = new ThreadLocal<>();
	private final static ThreadLocal<String> userPrincipalByThread = new ThreadLocal<>();
	private final static ThreadLocal<Date> dataEHoraDoServidor = new ThreadLocal<>();
    private final static ThreadLocal<UsuarioDeSistemaEnum> usuarioDeSistema = new ThreadLocal<>();
    private final static ThreadLocal<List<AfterCommit>> afterCommit = new ThreadLocal<>();
	
	public interface AfterCommit {
	    void run();
	}
	
	public static void addAfterCommit(AfterCommit ac) {
	    List<AfterCommit> acs = afterCommit.get();
	    if (acs == null) {
	        acs = new ArrayList<>();
	        afterCommit.set(acs);
	    }
	    acs.add(ac);
	}
	
	public static void runAfterCommit() {
        List<AfterCommit> acs = afterCommit.get();
        if (acs == null) 
            return;
        for (AfterCommit ac : acs)
            ac.run();
        afterCommit.remove();
	}
	
	
   public static boolean upgradeToTransactional() {
        EntityTransaction transaction = em().getTransaction();
        if (!transaction.isActive()) {
            // System.out.println("*** UPGRADE para Transacional - " + thiz.method.toString());
            transaction.begin();
            return true;
        }
        return false;
    }
	
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

	static public boolean flushTransaction() {
		if (em().getTransaction() != null && em().getTransaction().isActive()) {
			em().flush();
			em().getTransaction().commit();
            runAfterCommit();
			em().getTransaction().begin();
			return true;
		}
		return false;
	}
	
	static public boolean flushTransactionAndDowngradeToNonTransactional() {
		if (em().getTransaction() != null && em().getTransaction().isActive()) {
			em().flush();
			em().getTransaction().commit();
			runAfterCommit();
			return true;
		}
		return false;
	}
	
	public static boolean begin() {
		EntityTransaction transaction = em().getTransaction();
		if (transaction != null && !transaction.isActive()) {
			transaction.begin();
			return true;
		}
		return false;
	}

	public static boolean commit() {
		EntityTransaction transaction = em().getTransaction();
		if (transaction != null && transaction.isActive()) {
			transaction.commit();
//			System.out.println(transaction.isActive());
			return true;
		}
		return false;
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
		afterCommit.remove();
	}

}