package br.gov.jfrj.siga.wf.vraptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

@Component
@RequestScoped
public class WfDaoCreator implements ComponentFactory<WfDao> {
	//private final SessionFactory factory;
	//private Session session;
	//private WfDao dao;

//	public WfDaoCreator(SessionFactory factory) {
//		this.factory = factory;
//	}

	@PostConstruct
	public void create() {
//		HibernateUtil.getSessao();
//		ModeloDao.freeInstance();
//		//WfDao.getInstance();
//		//Wf.getInstance().getConf().limparCacheSeNecessario();
//		
//		//this.session = factory.openSession();
//		dao = (WfDao) WfDao.getInstance(this.session);
//		
//		// Novo
//		WfContextBuilder.getConfiguration();
//		WfContextBuilder.createJbpmContext();
//		WfContextBuilder.getJbpmContext().getJbpmContext()
//		.setSession(WfDao.getInstance().getSessao());
//		WfDao.iniciarTransacao();
	}

	public WfDao getInstance() {
		return WfDao.getInstance();
//		return this.dao;
	}

	@PreDestroy
	public void destroy() {
//		WfDao.commitTransacao();
//		if (this.session != null)
//			this.session.close();
//		ModeloDao.freeInstance();
	}

}