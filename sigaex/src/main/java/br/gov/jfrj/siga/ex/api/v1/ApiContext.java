package br.gov.jfrj.siga.ex.api.v1;

import static java.util.Objects.isNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class ApiContext implements Closeable {
	EntityManager em;
	boolean transacional;
	long inicio = System.currentTimeMillis();
	private static final String DOC_MÓDULO_DE_DOCUMENTOS = "DOC:Módulo de Documentos;";

	public ApiContext(boolean transacional, boolean validaUser) throws SwaggerAuthorizationException {
		if (validaUser) {
			buscarEValidarUsuarioLogado();
		}
		
		try {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(),
					SwaggerServlet.getHttpServletResponse()));
		} catch (NullPointerException ex) {
			// Engolindo exceção para garantir que está classe pode ser utilizada mesmo fora
			// de uma chamada à API REST
			CurrentRequest.set(null);
		}
		
		this.transacional = transacional;
		em = ExStarter.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ModeloDao.freeInstance();
		ExDao.getInstance();
		try {
			Ex.getInstance().getConf().limparCacheSeNecessario();
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
	 * <code>{@value #DOC_MÓDULO_DE_DOCUMENTOS}<code> e ao serviço informado 
	 * no parâmetro acesso.
	 * 
	 * @param acesso              Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	static void assertAcesso(String acesso) throws Exception {
		ApiContext.getSigaObjects().assertAcesso(DOC_MÓDULO_DE_DOCUMENTOS + acesso);
	}

	static void assertAcesso(final ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob)) {
			String s = "";
			s += mob.doc().getListaDeAcessosString();
			s = "(" + s + ")";
			s = " " + mob.doc().getExNivelAcessoAtual().getNmNivelAcesso() + " " + s;
	
			Map<ExPapel, List<Object>> mapa = mob.doc().getPerfis();
			boolean isInteressado = false;
	
			for (ExPapel exPapel : mapa.keySet()) {
				Iterator<Object> it = mapa.get(exPapel).iterator();
	
				if ((exPapel != null) && (exPapel.getIdPapel() == ExPapel.PAPEL_INTERESSADO)) {
					while (it.hasNext() && !isInteressado) {
						Object item = it.next();
						isInteressado = item.toString().equals(titular.getSigla()) ? true : false;
					}
				}
	
			}
	
			if (mob.doc().isSemEfeito()) {
				if (!mob.doc().getCadastrante().equals(titular) && !mob.doc().getSubscritor().equals(titular)
						&& !isInteressado) {
					throw new AplicacaoException("Documento " + mob.getSigla() + " cancelado ");
				}
			} else {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + "." + s);
			}
		}
	}
	
}