package br.gov.jfrj.siga.api.v1;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import static java.util.Objects.isNull;

import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.vraptor.SigaObjects;

/**
 * Métodos auxiliares.
 */
class SwaggerHelper {

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
		sigaObjects.assertAcesso("SIGA: Sistema Integrado de Gestão Administrativa" + acesso);

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