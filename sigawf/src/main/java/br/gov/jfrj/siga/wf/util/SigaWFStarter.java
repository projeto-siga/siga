package br.gov.jfrj.siga.wf.util;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.JbpmConfiguration;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.wf.bl.Wf;

/**
 * 
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 */
@Component
@ApplicationScoped
public class SigaWFStarter {

	private static final String JBPM_CFG_XML = "jbpm.cfg.xml";
	private static final String DATASOURCE = "java:jboss/datasources/SigaWfDS";

	@PostConstruct
	public void init() {
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
		ContextoPersistencia.initEntityManagerFactory("default-wf");

		Wf.getInstance();
		// Configuration cfg = WfDao.criarHibernateCfg(DATASOURCE);
		// HibernateUtil.configurarHibernate(cfg);
		// SigaAuditor.configuraAuditoria(new SigaHibernateChamadaAuditor(cfg));
	}

	private void initJBPM() {
		JbpmConfiguration configuration = JbpmConfiguration
				.getInstance(JBPM_CFG_XML);
		configuration.createJbpmContext().setSessionFactory(
				HibernateUtil.getSessao().getSessionFactory());
		configuration.startJobExecutor();
		WfContextBuilder.setConfiguration(configuration);
	}

}
