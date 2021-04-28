package br.gov.jfrj.siga.jee;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public abstract class SoapContext implements Closeable {
	private WebServiceContext context;
	EntityManager em;
	boolean transacional;
	long inicio = System.currentTimeMillis();

	public SoapContext(WebServiceContext context, EntityManagerFactory emf, boolean transacional) {
		this.context = context;
		this.transacional = transacional;
		em = emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ServletContext ctx = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		HttpServletRequest request = (HttpServletRequest) context.getMessageContext()
				.get(MessageContext.SERVLET_REQUEST);

		// Identifica se a chamada está vindo com um usuário de sistema
		final String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();
			byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
			String credentials = new String(credDecoded, StandardCharsets.UTF_8);
			// credentials = username:password
			final String[] values = credentials.split(":", 2);

			UsuarioDeSistemaEnum origem = UsuarioDeSistemaEnum.valueOf(values[0]);
			ContextoPersistencia.setUsuarioDeSistema(origem);
			// System.out.println("*** usuario de sistema: " + ContextoPersistencia.getUsuarioDeSistema());
		}

		HttpServletResponse response = (HttpServletResponse) context.getMessageContext()
				.get(MessageContext.SERVLET_RESPONSE);

		CurrentRequest.set(new RequestInfo(ctx, request, response));

		ModeloDao.freeInstance();
		initDao();
		if (this.transacional)
			em.getTransaction().begin();
	}

	abstract public void initDao();

	public void rollback(Exception e) {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		if (!RequestLoggerFilter.isAplicacaoException(e)) {
			RequestLoggerFilter.logException(null, inicio, e);
		}
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
}