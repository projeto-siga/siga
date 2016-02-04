package br.gov.jfrj.siga.wf.util;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaAuditor;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import org.hibernate.cfg.Configuration;
import org.jbpm.JbpmConfiguration;

import javax.annotation.PostConstruct;

/**
 * 
 * @author Rodrigo Ramalho
 * 		   hodrigohamalho@gmail.com
 *
 */
@Component
@ApplicationScoped
public class SigaWFStarter {
	
	private static final String JBPM_CFG_XML = "jbpm.cfg.xml";
	private static final String DATASOURCE = "java:jboss/datasources/SigaWfDS";

   @PostConstruct
	public void init(){
		try {
            System.out.println("Inicializando o hibernate!");
			initHibernate();
            System.out.println("Hibernate iniciado!");
            System.out.println("Inicializando o JBPM!");
			initJBPM();
            System.out.println("JBPM incializado!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHibernate() throws Exception {
		Wf.getInstance();
		Configuration cfg = WfDao.criarHibernateCfg(DATASOURCE);
		HibernateUtil.configurarHibernate(cfg);
		SigaAuditor.configuraAuditoria(new SigaHibernateChamadaAuditor(cfg));
	}
	
	private void initJBPM() {
		JbpmConfiguration configuration = JbpmConfiguration.getInstance(JBPM_CFG_XML);
		configuration.createJbpmContext().setSessionFactory(HibernateUtil.getSessionFactory());
		configuration.startJobExecutor();
		WfContextBuilder.setConfiguration(configuration);
	}

}
