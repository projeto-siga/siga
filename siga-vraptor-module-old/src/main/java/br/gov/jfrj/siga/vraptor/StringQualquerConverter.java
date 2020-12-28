package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

@Convert(StringQualquer.class)
@ApplicationScoped
public class StringQualquerConverter implements Converter<StringQualquer> {

	public StringQualquer convert(String value,
			Class<? extends StringQualquer> type) {
		return new StringQualquer(value);
	}

}