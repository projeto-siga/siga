package br.gov.jfrj.siga.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaAuditor;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

/**
 * 
 * @author Rodrigo Ramalho
 * 		   hodrigohamalho@gmail.com
 *
 */
@Startup
@Singleton
public class SigaStarter {
	
	private static final String DATASOURCE = "java:/jboss/datasources/SigaCpDS";

	@PostConstruct
	public void init(){
		try {
			initHibernate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHibernate() throws Exception {
		Configuration cfg = CpDao.criarHibernateCfg(DATASOURCE);
		HibernateUtil.configurarHibernate(cfg);
		SigaAuditor.configuraAuditoria(new SigaHibernateChamadaAuditor(cfg));
		//Cp.getInstance().getConf().inicializarCache();
	}
	
}
