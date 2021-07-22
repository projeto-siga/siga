package br.gov.jfrj.siga.tp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
			return "STR_TO_DATE('" + dataSaida + "', '%d/%m/%Y %H:%i')";
		}
		return "to_date('" + dataSaida + "', 'DD/MM/YYYY HH24:MI')";
	}
	
	public static String retorna_DataeHoraInvertido(String dataSaida) {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "STR_TO_DATE('" + dataSaida + "', '%Y-%m-%d %H:%i')";
		}
		return "to_date('" + dataSaida + "', 'DD/MM/YYYY HH24:MI')";
	}
	
	public static String retorna_DataeHoraeSegundo(String dataSaida) {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "STR_TO_DATE('" + dataSaida + "', '%d/%m/%Y %H:%i:%s')";
		}
		return "to_date('" + dataSaida + "', 'DD/MM/YYYY HH24:MI:SS')";
	}
	
	public static String retorna_DataeHoraeSegundoInvertido(String dataSaida) {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "STR_TO_DATE('" + dataSaida + "', '%Y-%m-%d %H:%i:%s')";
		}
		return "to_date('" + dataSaida + "', 'DD/MM/YYYY HH24:MI:SS')";
	}

	public static String recuperaFormato(String formato, String formatoMysql ) {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return formatoMysql;
		}
		return formato;
	}

	public static String recuperaDataFim() {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "9999-12-31 23:59";
		}
		return "31/12/9999 23:59";
	}

	public static String recuperaFuncaoTrunc() {
		String dialect = System.getProperty("siga.hibernate.dialect");
		if (dialect != null && dialect.contains("MySQL")) {
			return "date";
		}
		return "trunc";
	}
	
	 public static LocalDateTime toLocalDateTime(Calendar calendar) {
	      if (calendar == null) {
	          return null;
	      }
	      TimeZone tz = calendar.getTimeZone();
	      ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
	      return LocalDateTime.ofInstant(calendar.toInstant(), zid);
	  }
}