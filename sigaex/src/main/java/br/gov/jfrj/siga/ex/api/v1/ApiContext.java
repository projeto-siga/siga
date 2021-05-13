package br.gov.jfrj.siga.ex.api.v1;

import static java.util.Objects.isNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
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
	 * <code>{@value #DOC_MÓDULO_DE_DOCUMENTOS}<code> e ao serviço informado no
	 * parâmetro acesso.
	 * 
	 * @param acesso Caminho do serviço a ser verificado a permissão de acesso
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

	public static ExMobil getMob(String sigla) {
		ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		flt.setSigla(sigla);

		return ExDao.getInstance().consultarPorSigla(flt);
	}

	public static ExMovimentacao getMov(ExMobil mob, String id) {
		Long movId = Long.parseLong(id);
		ExMovimentacao mov = ExDao.getInstance().consultar(movId, ExMovimentacao.class, false);
		if (!mov.getExMobil().equals(mob)) 
			throw new AplicacaoException("Movimentação não se refere ao mobil informado");
		return mov;
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
	
	/**
	 * Retorna um {@link ExMobil Mobil} relacionado a uma certa
	 * {@link ExMobil#getSigla() sigla} contanto que esse exista e que o usuário
	 * tenha autorização para acessá-lo.
	 * 
	 * @param sigla              Sigla do documeto solicitado
	 * @param so                 SigaObjects previamente carregado via
	 *                           {@link #getSigaObjects()} . Será usado na
	 *                           validação.
	 * @param req                Requisição do Swagger. Usado no disparo da exceção.
	 * @param resp               Resposta do Swagger. Usado no disparo da exceção.
	 * @param descricaoDocumento Descrição do documento a ser usada em caso de erro.
	 * @return Mobil relacionado a sigla soliciatada.
	 * @throws SwaggerException Se não achar um Mobil com a sigla solicitada (
	 *                          {@link SwaggerException#getStatus() Status} 404) ou
	 *                          se o usário não tiver autorização para tratá-lo
	 *                          ({@link SwaggerException#getStatus() Status} 403)
	 */
	ExMobil buscarEValidarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp,
			String descricaoDocumento) throws Exception {
		final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
		filter.setSigla(sigla);
		ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);

		if (isNull(mob)) {
			throw new SwaggerException("Número do " + descricaoDocumento + " não existe", 404, null, req, resp,
					null);
		}
		if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob))
			throw new SwaggerException("Acesso ao " + descricaoDocumento + " " + mob.getSigla()
					+ " permitido somente a usuários autorizados. (" + getTitular().getSigla() + "/"
					+ getLotaTitular().getSiglaCompleta() + ")", 403, null, req, resp, null);

		return mob;
	}

	/**
	 * Retorna um {@link ExMobil Mobil} relacionado a uma certa
	 * {@link ExMobil#getSigla() sigla} contanto que esse exista e que o usuário
	 * tenha autorização para acessá-lo.
	 * 
	 * @param sigla Sigla do documeto solicitado
	 * @param req   Requisição do Swagger. Usado no disparo da exceção.
	 * @param resp  Resposta do Swagger. Usado no disparo da exceção.
	 * @return Mobil relacionado a sigla soliciatada.
	 * @throws SwaggerException Se não achar um Mobil com a sigla solicitada (
	 *                          {@link SwaggerException#getStatus() Status} 404) ou
	 *                          se o usário não tiver autorização para tratá-lo
	 *                          ({@link SwaggerException#getStatus() Status} 403).
	 * @see #buscarEValidarMobil(String, SigaObjects, ISwaggerRequest,
	 *      ISwaggerResponse, String)
	 */
	ExMobil buscarEValidarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp)
			throws Exception {
		return buscarEValidarMobil(sigla, req, resp, "Documento");
	}

}