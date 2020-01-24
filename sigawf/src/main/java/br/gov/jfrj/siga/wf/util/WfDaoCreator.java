package br.gov.jfrj.siga.wf.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import br.gov.jfrj.siga.wf.dao.WfDao;

@RequestScoped
public class WfDaoCreator {
	@Produces
	@RequestScoped
	public WfDao getInstance() {
		return WfDao.getInstance();
	}
}