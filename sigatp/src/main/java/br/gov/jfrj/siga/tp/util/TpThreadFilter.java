package br.gov.jfrj.siga.tp.util;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class TpThreadFilter implements Filter {
	
	private EntityManager em;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public TpThreadFilter() {
		super();
	}
	
	@Inject
	public TpThreadFilter(EntityManager em)  {
		this.em = em;
	}

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		try {
			ContextoPersistencia.setEntityManager(em);
			ModeloDao.freeInstance();
			CpDao.getInstance();
			Cp.getInstance().getConf().limparCacheSeNecessario();
			chain.doFilter(request, response);
		} catch (Exception e) {

			throw new ServletException(e);
		} finally {
			ModeloDao.freeInstance();
			ContextoPersistencia.setEntityManager(null);
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


	protected String getLoggerName() {
		return "br.gov.jfrj.siga.tp";
	}
}
