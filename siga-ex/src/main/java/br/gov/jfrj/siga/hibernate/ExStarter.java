package br.gov.jfrj.siga.hibernate;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaAuditor;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

/**
 * 
 * @author Rodrigo Ramalho
 * 		   hodrigohamalho@gmail.com
 *
 */
@Startup
@Singleton
public class ExStarter {
	
	private static final String DATASOURCE = "java:jboss/datasources/SigaExDS";
	
	@PostConstruct
	public void init(){
		try {
			initHibernate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initHibernate() throws Exception {
		Ex.getInstance();
		Configuration cfg = ExDao.criarHibernateCfg(DATASOURCE);
		SigaAuditor.configuraAuditoria(new SigaHibernateChamadaAuditor(cfg));
		HibernateUtil.configurarHibernate(cfg);
	}

}
