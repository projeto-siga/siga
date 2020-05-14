package br.gov.jfrj.siga.vraptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

@Convert(Date.class)
@ApplicationScoped
public class DateConverter implements Converter<Date> {

	public final static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public Date convert(String value, Class<? extends Date> type) {
		String s = stringOrNull(value);
		if (s == null) {
			return null;
		}

		try {
			Date d = df.parse(s);
			return d;
		} catch (final ParseException e) {
			return null;
		}
	}

	private static String stringOrNull(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s;
	}

}