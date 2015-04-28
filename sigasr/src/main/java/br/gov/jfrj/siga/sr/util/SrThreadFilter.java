package br.gov.jfrj.siga.sr.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.sr.dao.SrDao;
import br.gov.jfrj.siga.sr.model.Sr;

public class SrThreadFilter extends ThreadFilter{

	private static final Logger log = Logger.getLogger(SrThreadFilter.class);

	/**
	 * Pega a sessão.
	 */
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final StringBuilder csv = super.iniciaAuditoria(request);

		try {
			this.executaFiltro(request, response, chain);

		} catch (final Exception ex) {
			SrDao.rollbackTransacao();
			if (ex instanceof RuntimeException)
				throw (RuntimeException) ex;
			else
				throw new ServletException(ex);
		} finally {
			this.fechaSessaoHibernate();
			this.liberaInstanciaDao();
			Thread.interrupted();			
		}

		super.terminaAuditoria(csv);

	}
	private void executaFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws Exception, AplicacaoException {

		// HibernateUtil.getSessao();
		ModeloDao.freeInstance();
		SrDao.getInstance();
		Sr.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		if (!SrDao.getInstance().sessaoEstahAberta())
			throw new AplicacaoException("Erro: sessão do Hibernate está fechada.");

		SrDao.iniciarTransacao();
		doFiltro(request, response, chain);
		SrDao.commitTransacao();
	}

	private void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws Exception {

		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.info("Ocorreu um erro durante a execução da operação: "+ e.getMessage());
			throw e;
		}
	}

	private void fechaSessaoHibernate() {
		try {
			HibernateUtil.fechaSessaoSeEstiverAberta();
		} catch (Exception ex) {
			log.error("Ocorreu um erro ao fechar uma sessão do Hibernate", ex);
		}
	}

	private void liberaInstanciaDao() {
		try {
			SrDao.freeInstance();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Executa ao destruir o filtro.
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Executa ao inciar o filtro.
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
