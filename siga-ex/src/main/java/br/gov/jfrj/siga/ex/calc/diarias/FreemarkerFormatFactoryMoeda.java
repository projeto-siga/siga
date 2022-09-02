package br.gov.jfrj.siga.ex.calc.diarias;

import java.util.Locale;

import freemarker.core.Environment;
import freemarker.core.InvalidFormatParametersException;
import freemarker.core.TemplateFormatUtil;
import freemarker.core.TemplateNumberFormat;
import freemarker.core.TemplateNumberFormatFactory;
import freemarker.core.UnformattableValueException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.utility.NumberUtil;

public class FreemarkerFormatFactoryMoeda extends TemplateNumberFormatFactory {

	public static final FreemarkerFormatFactoryMoeda INSTANCE = new FreemarkerFormatFactoryMoeda();

	private FreemarkerFormatFactoryMoeda() {
	}

	@Override
	public TemplateNumberFormat get(String params, Locale locale, Environment env)
			throws InvalidFormatParametersException {
		TemplateFormatUtil.checkHasNoParameters(params);
		return FormatMoeda.INSTANCE;
	}

	private static class FormatMoeda extends TemplateNumberFormat {

		private static final FormatMoeda INSTANCE = new FormatMoeda();

		private FormatMoeda() {
		}

		@Override
		public String formatToPlainText(TemplateNumberModel numberModel)
				throws UnformattableValueException, TemplateModelException {
			Number n = TemplateFormatUtil.getNonNullNumber(numberModel);
			double d = n.doubleValue();
			if (d == 0)
				return "-";
			try {
				return String.format("%,.2f", d);
			} catch (ArithmeticException e) {
				throw new UnformattableValueException(n + " não pode ser formatado para moeda");
			}
		}

		@Override
		public boolean isLocaleBound() {
			return false;
		}

		@Override
		public String getDescription() {
			return "formatador de moeda";
		}

	}

}