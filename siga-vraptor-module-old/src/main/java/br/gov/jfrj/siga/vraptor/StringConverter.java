package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

@Alternative
@Convert(String.class)
@ApplicationScoped
public class StringConverter implements Converter<String> {

	public String convert(String value, Class<? extends String> type) {
		String s = stringOrNull(value);
		if (s == null) {
			return null;
		}
		return s;
	}
	
	private static String stringOrNull(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s;
	}

}