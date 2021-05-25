package br.gov.jfrj.siga.context;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;

import com.crivano.swaggerservlet.SwaggerApiContextSupport;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerContext;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;

abstract public class ApiContextSupport extends SwaggerApiContextSupport {

	EntityManager em;
	boolean transacional = false;
	long inicio = System.currentTimeMillis();
	SigaObjects sigaObjects = null;

	@Override
	public void init(SwaggerContext ctx) {
		super.init(ctx);

		try {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(),
					SwaggerServlet.getHttpServletResponse()));
		} catch (NullPointerException e) {
			// Acontece quando estamos apenas rodando um /api/v1/test
		}

		if (ctx != null && getCtx().getAction().getClass().isAnnotationPresent(Transacional.class))
			this.transacional = true;

		em = criarEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ModeloDao.freeInstance();
		inicializarDao();
		try {
			if (ctx != null
					&& !getCtx().getAction().getClass().isAnnotationPresent(NaoAtualizarCacheDeConfiguracoes.class))
				atualizarCacheDeConfiguracoes();
		} catch (Exception e1) {
			throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
		}
		if (this.transacional)
			em.getTransaction().begin();
	}

	abstract public void atualizarCacheDeConfiguracoes() throws Exception;

	abstract public CpDao inicializarDao();

	abstract public EntityManager criarEntityManager();

	/**
	 * Retorna uma instância de {@link SigaObjects} a partir do Request do
	 * {@link SwaggerServlet}.
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public SigaObjects getSigaObjects() throws Exception {
		if (sigaObjects == null)
			sigaObjects = new SigaObjects(getCtx().getRequest());
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
	public static String buscarEValidarUsuarioLogado() throws SwaggerAuthorizationException {
		String userPrincipal = ContextoPersistencia.getUserPrincipal();
		if (isNull(userPrincipal)) {
			throw new SwaggerAuthorizationException("Usuário não está logado");
		}

		return userPrincipal;
	}

	/**
	 * Verifica se o usuário tem acesso ao sistema e ao serviço informado no
	 * parâmetro acesso.
	 * 
	 * @param acesso Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public void assertAcesso(String acesso) throws Exception {
		getSigaObjects().assertAcesso(acesso);
	}

	@Override
	public void onTryBegin() throws Exception {
		if (!getCtx().getAction().getClass().isAnnotationPresent(AcessoPublico.class)) {
			try {
				String token = AuthJwtFormFilter.extrairAuthorization(getCtx().getRequest());
				Map<String, Object> decodedToken = AuthJwtFormFilter.validarToken(token);
				final long now = System.currentTimeMillis() / 1000L;
				if ((Integer) decodedToken.get("exp") < now + AuthJwtFormFilter.TIME_TO_RENEW_IN_S) {
					// Seria bom incluir o attributo HttpOnly
					String tokenNew = AuthJwtFormFilter.renovarToken(token);
					Map<String, Object> decodedNewToken = AuthJwtFormFilter.validarToken(token);
					Cookie cookie = AuthJwtFormFilter.buildCookie(tokenNew);
					getCtx().getResponse().addCookie(cookie);
				}
				ContextoPersistencia.setUserPrincipal((String) decodedToken.get("sub"));
			} catch (Exception e) {
				if (!getCtx().getAction().getClass().isAnnotationPresent(AcessoPublicoEPrivado.class))
					throw e;
			}
		}

		if (ContextoPersistencia.getUserPrincipal() != null)
			assertAcesso("");
	}

	@Override
	public void onCatch(Exception e) throws Exception {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		if (!RequestLoggerFilter.isAplicacaoException(e)) {
			RequestLoggerFilter.logException(null, inicio, e);
		}
		ContextoPersistencia.removeAll();
		if (e instanceof RegraNegocioException)
			throw new SwaggerException(e.getMessage(), 400, null, getCtx().getReq(), getCtx().getResp(), null);
		else
			throw e;
	}

	@Override
	public void onFinally() {
		ContextoPersistencia.removeUserPrincipal();
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

	public DpPessoa getCadastrante() throws Exception {
		return getSigaObjects().getCadastrante();
	}

	public DpLotacao getLotaCadastrante() throws Exception {
		return getSigaObjects().getCadastrante().getLotacao();
	}

	public DpPessoa getTitular() throws Exception {
		return getSigaObjects().getTitular();
	}

	public DpLotacao getLotaTitular() throws Exception {
		return getSigaObjects().getLotaTitular();
	}

	public CpIdentidade getIdentidadeCadastrante() throws Exception {
		return getSigaObjects().getIdentidadeCadastrante();
	}

}
