package br.gov.jfrj.siga.util;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class SigaThreadFilter extends ThreadFilter {

	public void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		EntityManager em = SigaStarter.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);
		
		ModeloDao.freeInstance();
		CpDao.getInstance();
		try {
			Cp.getInstance().getConf().limparCacheSeNecessario();
		} catch (Exception e1) {
			throw new RuntimeException(
					"Não foi possível atualizar o cache de configurações", e1);
		}

		em.getTransaction().begin();

		try {
			chain.doFilter(request, response);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();

			throw new ServletException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
		}
	}

	@Override
	protected String getLoggerName() {
		return "br.gov.jfrj.siga";
	}
}