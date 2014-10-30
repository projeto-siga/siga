package br.gov.jfrj.siga.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

@Singleton
@Startup
public class SigaStarter {

	@PostConstruct
	public void initHibernate(){
		try {
			Configuration cfg = CpDao.criarHibernateCfg("java:/jboss/datasources/SigaCpDS");
			HibernateUtil.configurarHibernate(cfg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
