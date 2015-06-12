package br.gov.jfrj.siga.feature.entity.instantiator.vraptor;

import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.http.iogi.VRaptorInstantiator;

/**
 * Classe utilizada para instanciar atributos de entidades. Nesse caso, caso nao seja encontrado o ID do atributo eh retornado nulo.
 * 
 * @author DB1
 * 
 */
public class JpaEntityAttributeInstantiator extends AbstractJpaEntityInstantiator {

	public JpaEntityAttributeInstantiator(VRaptorInstantiator delegate, Converters converters, Localization localization) {
		super(delegate, converters, localization);
	}

	// TODO: Analisar como tratar quando for cadastro "cascade"
	@Override
	protected Object returnWhenMissing(Parameters parameters, Class<?> type) {
		return null;
	}

	@Override
	protected Instantiator<?> getAttributesIntantiator() {
		return this;
	}
}