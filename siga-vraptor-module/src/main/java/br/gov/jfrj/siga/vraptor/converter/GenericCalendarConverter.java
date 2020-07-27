package br.gov.jfrj.siga.vraptor.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.enterprise.context.RequestScoped;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;

@RequestScoped
@Convert(Calendar.class)
public class GenericCalendarConverter implements Converter<Calendar> {

	private static final String DATA_INICIO = "01/01/1900 ";
	private static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final String PATTERN_DD_MM_YYYY = "^(([0-2]\\d|[3][0-1])\\/([0]\\d|[1][0-2])\\/[2][0]\\d{2})$";
	private Validator validator;

	public GenericCalendarConverter(Validator validator) {
		this.validator = validator;
	}

	@Override
	public Calendar convert(String value, Class<? extends Calendar> type) {
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
			if (value.matches(PATTERN_DD_MM_YYYY))
				return getConvertedCalendar(value, DD_MM_YYYY);

			return getConvertedCalendar(value, DD_MM_YYYY_HH_MM);
		} catch (ParseException e) {
			validator.add(new I18nMessage("data", "data.validation.params", value));
		}
		return null;
	}

	private Calendar getConvertedCalendar(String value, String formatoData) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
		sdf.setLenient(false);
		calendar.setTime(sdf.parse(value));
		return calendar;
	}

	private Calendar converterQuandoApenasHorasMinutos(String value) {
		try {
			String dataHora = DATA_INICIO + value;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat(DD_MM_YYYY_HH_MM).parse(dataHora));
			return calendar;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
