package br.gov.jfrj.siga.vraptor.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.validator.I18nMessage;

@RequestScoped
@Convert(Calendar.class)
public class GenericCalendarConverter implements Converter<Calendar> {

	private static final String DATA_INICIO = "01/01/1900 ";
	private static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private Validator validator;

	public GenericCalendarConverter(Validator validator) {
		this.validator = validator;
	}

	@Override
	public Calendar convert(String value, Class<? extends Calendar> type,
			ResourceBundle bundle) {
		if (dataPreenchida(value)) {
			if (value.matches("\\d\\d:\\d\\d")) {
				return converterQuandoApenasHorasMinutos(value);
			} else if (value.matches("\\d:\\d\\d")) {
				return converterQuandoApenasHorasMinutos("0" + value);
			}
			return converterUtilizandoPadroesAceitos(value);
		}
		return null;
	}

	private boolean dataPreenchida(String value) {
		return value != null && !value.isEmpty();
	}

	private Calendar converterUtilizandoPadroesAceitos(String value) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
			sdf.setLenient(false);
			calendar.setTime(sdf.parse(value));
			return calendar;
		} catch (ParseException e) {
			try {
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
				sdf.setLenient(false);
				calendar.setTime(sdf.parse(value));
				return calendar;				
			} catch (Exception e2) {
				validator.add(new I18nMessage("data", "data.validation"));
			}
		}
		return null;
	}

	private Calendar converterQuandoApenasHorasMinutos(String value) {
		try {
			String dataHora = DATA_INICIO + value;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat(DD_MM_YYYY_HH_MM)
					.parse(dataHora));
			return calendar;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
