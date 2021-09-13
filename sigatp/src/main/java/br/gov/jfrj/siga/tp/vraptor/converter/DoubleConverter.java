package br.gov.jfrj.siga.tp.vraptor.converter;

import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.converter.Converter;

/**
 * Conversor de valores para double
 * 
 * @author db1
 *
 */
@ApplicationScoped
@Convert(Double.class)
public class DoubleConverter implements Converter<Double> {

	@Override
	public Double convert(String value, Class<? extends Double> type) {
		try {
			String valorInput = (value.equals("") ? "0.000" : value.replace(".", "").replace(",", "."));
			return new Double(valorInput);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
