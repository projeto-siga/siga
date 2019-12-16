package br.gov.jfrj.siga.vraptor;

import java.util.ResourceBundle;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.ioc.ApplicationScoped;

@Convert(StringQualquer.class)
@ApplicationScoped
public class StringQualquerConverter implements Converter<StringQualquer> {

	public StringQualquer convert(String value,
			Class<? extends StringQualquer> type, ResourceBundle bundle) {
		return new StringQualquer(value);
	}

}