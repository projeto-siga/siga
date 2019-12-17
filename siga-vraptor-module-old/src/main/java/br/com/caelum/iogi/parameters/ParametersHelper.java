package br.com.caelum.iogi.parameters;

/**
 * Helper para possibilitar o acesso a lista de parametros
 * 
 * @author DB1
 *
 */
public class ParametersHelper {

	public static Parameter findIdParameter(Parameters parameters, String name) {
		for (Parameter parameter : parameters.getParametersList()) {
			/**
			 * Se o nome do parametro esta em branco ele representa o atributo sendo convertido
			 */
			if (parameter.getName().isEmpty() || parameter.getName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}
}