package br.gov.jfrj.siga.ex.api.v1;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import static java.util.Objects.isNull;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

/**
 * Métodos auxiliares.
 */
class SwaggerHelper {

	private static final String DOC_MÓDULO_DE_DOCUMENTOS = "DOC:Módulo de Documentos;";

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
	 * Retorna uma instância de {@link SigaObjects} a partir do Request do
	 * {@link SwaggerServlet}.
	 * @throws Exception Se houver algo de errado.
	 */
	static SigaObjects getSigaObjects(String acesso) throws Exception {
		SigaObjects sigaObjects = new SigaObjects(SwaggerServlet.getHttpServletRequest());
		sigaObjects.assertAcesso(DOC_MÓDULO_DE_DOCUMENTOS + acesso);

		return sigaObjects;
	}

	/**
	 * Retorna uma instância de {@link SigaObjects} a partir do Request do
	 * {@link SwaggerServlet}. Ainda verifica se o usuário tem aceso ao serviço
	 * <code>{@value #DOC_MÓDULO_DE_DOCUMENTOS}<code>.
	 * 
	 * @param acesso Acesso solicitado.
	 * @return Instância de {@link SigaObjects} a partir do Request do
	 *         {@link SwaggerServlet}.
	 * @throws Exception Se houver algo de errado.
	 */
	static SigaObjects getSigaObjects() throws Exception {
		return getSigaObjects("");
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
	static ExMobil buscarEValidarMobil(final String sigla, SigaObjects so, ISwaggerRequest req, ISwaggerResponse resp,
			String descricaoDocumento) throws SwaggerException {
		final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
		filter.setSigla(sigla);
		ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);

		if (isNull(mob)) {
			throw new SwaggerException("Número do " + descricaoDocumento + " não existe no SPSP", 404, null, req, resp,
					null);
		}
		if (!Ex.getInstance().getComp().podeAcessarDocumento(so.getTitular(), so.getLotaTitular(), mob))
			throw new SwaggerException("Acesso ao " + descricaoDocumento + " " + mob.getSigla()
					+ " permitido somente a usuários autorizados. (" + so.getTitular().getSigla() + "/"
					+ so.getLotaTitular().getSiglaCompleta() + ")", 403, null, req, resp, null);

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
	static ExMobil buscarEValidarMobil(final String sigla, ISwaggerRequest req, ISwaggerResponse resp)
			throws Exception {
		return buscarEValidarMobil(sigla, getSigaObjects(), req, resp, "Documento");
	}

	/**
	 * Realiza o <i>decode</i> de uma valor de uma {@link String} vinda numa url.
	 * Por exemplo: Uma string que originalmente seria algo como
	 * <code>abcd/efg hij ç:ã</code> apareceria na URL na como
	 * <code>abcd%2Fefg%20hij%20%C3%A7%3A%C3%A3</code>. Este método então
	 * "restauraria" a string no formato original.
	 * 
	 * @param pathParamValue String original "encodada", orignada de uma URL tal
	 *                       como <code>abcd%2Fefg%20hij%20%C3%A7%3A%C3%A3</code>.
	 * @return A String original "decodada"
	 * @throws UnsupportedEncodingException If character encoding needs to be
	 *                                      consulted, butnamed character encoding
	 *                                      is not supported. Provavelmente nunca
	 *                                      será disparada.
	 * @see {@link URLDecoder#decode(String, String)}
	 */
	static String decodePathParam(String pathParamValue) throws UnsupportedEncodingException {
		return URLDecoder.decode(pathParamValue, StandardCharsets.UTF_8.toString());
	}

}