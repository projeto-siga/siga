package br.gov.jfrj.siga.wf.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.hibernate.cfg.Configuration;
import org.jbpm.JbpmConfiguration;

import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaAuditor;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * 
 * @author Rodrigo Ramalho
 * 		   hodrigohamalho@gmail.com
 *
 */
@Startup
@Singleton
public class SigaWFStarter {
	
	private static final String JBPM_CFG_XML = "jbpm.cfg.xml";
	private static final String DATASOURCE = "java:jboss/datasources/SigaWfDS";

	@PostConstruct
	public void init(){
		try {
			initHibernate();
			initJBPM();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initJBPM() {
		JbpmConfiguration configuration = JbpmConfiguration.getInstance(JBPM_CFG_XML);
		configuration.createJbpmContext().setSessionFactory(HibernateUtil.getSessionFactory());
		configuration.startJobExecutor();
		WfContextBuilder.setConfiguration(configuration);
	}

	private void initHibernate() throws Exception {
		Wf.getInstance();
		Configuration cfg = WfDao.criarHibernateCfg(DATASOURCE);
		HibernateUtil.configurarHibernate(cfg);
		SigaAuditor.configuraAuditoria(new SigaHibernateChamadaAuditor(cfg));
	}
	
}
