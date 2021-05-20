package br.gov.jfrj.siga.api.v1;

import static java.util.Objects.isNull;

import java.io.Closeable;
import java.io.IOException;

import javax.persistence.EntityManager;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.util.SigaStarter;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class ApiContext implements Closeable {
	EntityManager em;
	boolean transacional;
	long inicio = System.currentTimeMillis();

	public ApiContext(boolean transacional, boolean autenticar) throws SwaggerAuthorizationException {
		if (autenticar) {
			buscarEValidarUsuarioLogado();
		}
		
		try {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		} catch (NullPointerException e) {
			// Acontece quando estamos apenas rodando um /api/v1/test
		}

		this.transacional = transacional;
		em = SigaStarter.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ModeloDao.freeInstance();
		CpDao.getInstance();
		try {
			Cp.getInstance().getConf().limparCacheSeNecessario();
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
	 * @throws Exception Se houver algo de errado.
	 */
	public static SigaObjects getSigaObjects() throws Exception {
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
	 * Verifica se o usuário tem acesso ao sistema e ao serviço informado 
	 * no parâmetro acesso.
	 * 
	 * @param acesso              Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public static void assertAcesso(String acesso) throws Exception {
		ApiContext.getSigaObjects().assertAcesso(acesso);
	}

}