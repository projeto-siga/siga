package br.gov.jfrj.siga.base;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;
import javax.xml.datatype.XMLGregorianCalendar;

public class SigaFormats {
	/**
	 * Aplica o formato de CPF. Ex: 12345678910 para 123.456.789-10
	 * 
	 * @param cpf - CPF formatado.
	 * @return
	 */
	public static String cpf(Object o) {
		String cpf = o.toString();

		// Se CPF já vem formatado, devolve cpf
		Pattern p = Pattern.compile("[0-9]{2,3}?\\.[0-9]{3}?\\.[0-9]{3}?\\-[0-9]{2}?");
		Matcher m = p.matcher(cpf);

		if (m.find()) {
			return cpf;
		}

		// O texto é truncado para 11 caracteres caso seja maior
		if (cpf.length() > 11) {
			cpf = cpf.substring(0, 11);
		}

		// Determina o número de zeros à esquerda
		int numZerosAEsquerda = 11 - cpf.length();

		// aplica os zeros à esquerda
		for (int i = 0; i < numZerosAEsquerda; i++) {
			cpf = "0" + cpf;
		}

		// extrai cada termo
		String termo1, termo2, termo3, termo4;
		termo1 = cpf.substring(0, 3);
		termo2 = cpf.substring(3, 6);
		termo3 = cpf.substring(6, 9);
		termo4 = cpf.substring(9);

		return termo1 + "." + termo2 + "." + termo3 + "-" + termo4;
	}

	/**
	 * Aplica o formato de CNP. Ex: 1234567891012 para 12.345.678/9101-23
	 * 
	 * @param cnpj - CNPJ formatado.
	 * @return
	 * @throws ParseException
	 */
	public static String cnpj(Object o) throws ParseException {
		String cnpj = o.toString();

		if (cnpj != null) {
			cnpj = cnpj.replaceAll("\\.", "").replaceAll("\\/", "").replaceAll("\\-", "");

			MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(cnpj);
		}

		return "";
	}

	public static BigDecimal bigdecimal(Object o) {
		if (o == null)
			return null;
		String s = null;
		BigDecimal dec = null;
		if (o instanceof String) {
			s = (String) o;
			if (s == null || s.trim().length() == 0)
				return null;
			String temp = s;
			temp = temp.replaceAll("\\.", "");
			temp = temp.replaceAll("\\,", ".");
			dec = new BigDecimal(temp);
		} else if (o instanceof BigDecimal)
			dec = (BigDecimal) o;
		else if (o instanceof Float)
			dec = new BigDecimal((Float) o);
		else if (o instanceof Double)
			dec = new BigDecimal((Double) o);
		return dec;
	}

	public static String reaisEmExtenso(Object o) {
		BigDecimal dec = bigdecimal(o);
		ReaisPorExtenso r = new ReaisPorExtenso(dec);
		return r.toString();
	}

	public static String reaisPorExtenso(Object o) {
		BigDecimal dec = bigdecimal(o);
		if (dec == null)
			return null;
		ReaisPorExtenso r = new ReaisPorExtenso(dec);
		return "R$ " + bigDecimalParaMonetario(dec) + " (" + r.toString() + ")";
	}

	public static String reais(Object o) {
		BigDecimal dec = bigdecimal(o);
		if (dec == null)
			return null;
		return "R$ " + bigDecimalParaMonetario(dec);
	}

	public static String monetario(Object o) {
		BigDecimal dec = bigdecimal(o);
		if (dec == null)
			return null;
		return bigDecimalParaMonetario(dec);
	}

	public static Float monetarioParaFloat(String monetario) {
		try {
			return Float.parseFloat(monetario.replace(".", "").replace(",", "."));
		} catch (Exception e) {
			return null;
		}
	}

	public static BigDecimal monetarioParaBigDecimal(String monetario) {
		try {
			return new BigDecimal(monetario.replace(".", "").replace(",", "."));
		} catch (Exception e) {
			return null;
		}
	}

	public static String floatParaMonetario(Float valor1) {
		Float valor2;
		if (valor1 < 0) {
			valor2 = Float.parseFloat(valor1.toString().replace("-", ""));
			DecimalFormat formatter = new DecimalFormat("#,##0.00");
			String s = formatter.format(valor2);
			if (s.substring(s.length() - 3, s.length() - 2).equals(".")) {
				return s.replace(".", "*").replace(",", ".").replace("*", ",");
			} else {
				return "-" + s;
			}
		} else {
			DecimalFormat formatter = new DecimalFormat("#,##0.00");
			String s = formatter.format(valor1);
			if (s.substring(s.length() - 3, s.length() - 2).equals(".")) {
				return s.replace(".", "*").replace(",", ".").replace("*", ",");
			} else {
				return s;
			}
		}
	}

	public static String bigDecimalParaMonetario(BigDecimal valor1) {
		BigDecimal valor2;
		if (valor1.compareTo(BigDecimal.ZERO) < 0) { // It could be ... if (valor1.signum() == -1)
			valor2 = valor1;
			DecimalFormat formatter = new DecimalFormat("#,##0.00");
			String s = formatter.format(valor2);
			if (s.substring(s.length() - 3, s.length() - 2).equals(".")) {
				return s.replace(".", "*").replace(",", ".").replace("*", ",");
			} else {
				return "-" + s;
			}
		} else {
			DecimalFormat formatter = new DecimalFormat("#,##0.00");
			String s = formatter.format(valor1);
			if (s.substring(s.length() - 3, s.length() - 2).equals(".")) {
				return s.replace(".", "*").replace(",", ".").replace("*", ",");
			} else {
				return s;
			}
		}
	}

	public static Date date(Object o) {
		if (o == null)
			return null;
		Date d = null;
		if (o instanceof String)
			d = Data.parse((String) o);
		else if (o instanceof Date)
			d = (Date) o;
		else if (o instanceof XMLGregorianCalendar)
			d = ((XMLGregorianCalendar)o).toGregorianCalendar().getTime();
		return d;
	}

	public static String data(Object o) {
		Date d = date(o);
		if (d == null)
			return null;
		return Data.formatDDMMYYYY(d);
	}

	public static String ddmmyy(Object o) {
		Date d = date(o);
		if (d == null)
			return null;
		return Data.formatDDMMYY(d);
	}

	public static String ddmmyyyyhhmmss(Object o) {
		Date d = date(o);
		if (d == null)
			return null;
		return Data.formatDDMMYYYY_AS_HHMMSS(d);
	}

}
