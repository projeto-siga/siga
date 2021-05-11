package br.gov.jfrj.siga.wf.api.v1;

import static java.util.Objects.isNull;

import java.io.Closeable;
import java.io.IOException;

import javax.persistence.EntityManager;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.dao.WfStarter;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class ApiContext implements Closeable {
	EntityManager em;
	boolean transacional;
	long inicio = System.currentTimeMillis();
	private static final String WF_MÓDULO_DE_WORKFLOW = "WF:Módulo de Workflow;";

	public ApiContext(boolean transacional, boolean validaUser) throws SwaggerAuthorizationException {
		if (validaUser) {
			buscarEValidarUsuarioLogado();
		}

		this.transacional = transacional;
		em = WfStarter.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ModeloDao.freeInstance();
		WfDao.getInstance();
		try {
			Wf.getInstance().getConf().limparCacheSeNecessario();
		} catch (Exception e1) {
			throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
		}
		if (this.transacional)
			em.getTransaction().begin();
	}

	public void rollback(Exception e) {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		if (!RequestLoggerFilter.isAplicacaoException(e)) {
			RequestLoggerFilter.logException(null, inicio, e);
		}
		ContextoPersistencia.removeAll();
	}

	@Override
	public void close() throws IOException {
		try {
			if (this.transacional)
				em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
			ContextoPersistencia.removeAll();
		}
	}

	/**
	 * Retorna uma instância de {@link SigaObjects} a partir do Request do
	 * {@link SwaggerServlet}.
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	static SigaObjects getSigaObjects() throws Exception {
		SigaObjects sigaObjects = new SigaObjects(SwaggerServlet.getHttpServletRequest());
		return sigaObjects;
	}

	/**
	 * Verifica a presença de um usuário logado e o retorna.
	 * 
	 * @return O login do Usuário na sessão
	 * @throws SwaggerAuthorizationException Se não achar nenhum usuário logado na
	 *                                       sessão.
	 * @see ContextoPersistencia#getUserPrincipal()
	 */
	static String buscarEValidarUsuarioLogado() throws SwaggerAuthorizationException {
		String userPrincipal = ContextoPersistencia.getUserPrincipal();
		if (isNull(userPrincipal)) {
			throw new SwaggerAuthorizationException("Usuário não está logado");
		}

		return userPrincipal;
	}

	/**
	 * Verifica se o usuário tem acesso ao serviço
	 * <code>{@value #WF_MÓDULO_DE_WORKFLOW}<code> e ao serviço informado no
	 * parâmetro acesso.
	 * 
	 * @param acesso Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	static void assertAcesso(String acesso) throws Exception {
		ApiContext.getSigaObjects().assertAcesso(WF_MÓDULO_DE_WORKFLOW + acesso);
	}

	static void assertAcesso(final WfProcedimento mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		// Nato: Precisaremos restringir o acesso
		if (false)
			throw new AplicacaoException("Procedimento " + mob.getSigla() + " inacessível ao usuário "
					+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + ".");
	}

}