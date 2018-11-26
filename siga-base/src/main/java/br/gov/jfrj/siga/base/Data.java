package br.gov.jfrj.siga.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {

	// Verifica se a data está entre o ano 2000 e o ano 2100
	@SuppressWarnings("deprecation")
	public static Boolean dataDentroSeculo21(Date data) {

		if ((data.before(new Date(100, 0, 1)) || data.after(new Date(200, 0, 1)))) {
			return false;
		} else {
			return true;
		}

	}

	public static String formatDDMMYY_AS_HHMMSS(Date dt) {
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy _ HH:mm:ss");
			return df.format(dt).replaceAll("_", "às");
		}
		return null;
	}

}
