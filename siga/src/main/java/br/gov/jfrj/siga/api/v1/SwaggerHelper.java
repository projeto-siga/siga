package br.gov.jfrj.siga.api.v1;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;

import static java.util.Objects.isNull;

import br.gov.jfrj.siga.model.ContextoPersistencia;

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

}