package br.gov.jfrj.siga.tp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatarDataHora {
    public static Date formatarDataHora(Calendar data, String hora) {
    	Date retorno = null;
        SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataFormatada = String.format("%02d", data.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", data.get(Calendar.MONTH) + 1) + "/" + String.format("%04d", data.get(Calendar.YEAR));
        try {
			retorno = formatar.parse(dataFormatada + " " + hora);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
        return retorno;
    }
    
    public static String formatarData(Calendar data)  {
        return String.format("%02d", data.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", data.get(Calendar.MONTH) + 1) + "/" + String.format("%04d", data.get(Calendar.YEAR));
    }
    
	public static String retorna_DataeHora(String dataSaida) {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "STR_TO_DATE('" + dataSaida + "', '%d/%m/%y %H:%i')";
		}
		return "to_date('" + dataSaida + "', 'DD/MM/YYYY HH24:MI')";
	}
	
	public static String retorna_DataeHoraeSegundo(String dataSaida) {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "STR_TO_DATE('" + dataSaida + "', '%d/%m/%y %H:%i:%s')";
		}
		return "to_date('" + dataSaida + "', 'DD/MM/YYYY HH24:MI:SS')";
	}
}