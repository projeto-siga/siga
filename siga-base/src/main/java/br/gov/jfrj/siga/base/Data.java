package br.gov.jfrj.siga.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

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

	public static Boolean validaDDMMYYYY(String dataDDMMYYYY) {
        Date data = null;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.setLenient(false);
            data = format.parse(dataDDMMYYYY);
            if (dataDentroSeculo21(data)) {
            	return true;
            } else {
            	return false;
            }
        } catch (ParseException e) {
        	return false;
        }
	}
	
	public static Boolean valida(String dt, String formato) {
        Date data = null;
		SimpleDateFormat format = new SimpleDateFormat(formato);
        try {
            format.setLenient(false);
            data = format.parse(dt);
            if (dataDentroSeculo21(data)) {
            	return true;
            } else {
            	return false;
            }
        } catch (ParseException e) {
        	return false;
        }
	}
	
	public static Date parse(String s) {
		if (s == null || s.trim().length() == 0)
			return null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return df.parse(s);
		} catch (Exception ex) {
			throw new RuntimeException("Data inválida " + s, ex);
		}
	}

	public static String formatDDMMYY_AS_HHMMSS(Date dt) {
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy _ HH:mm:ss");
			return df.format(dt).replaceAll("_", "às");
		}
		return null;
	}
	public static String formatDDMMYYYY_AS_HHMMSS(Date dt) {
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy _ HH:mm:ss");
			return df.format(dt).replaceAll("_", "às");
		}
		return null;
	}
	
	public static String formatDDMMYY(Date dt) {
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(dt);
		}
		return null;
	}
	
	public static String formatDDMMYYYY(Date dt) {
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(dt);
		}
		return null;
	}
	
	public static String calcularTempoRelativo(Date anterior) {
		PrettyTime p = new PrettyTime(new Date(), new Locale("pt"));

		String tempo = p.format(anterior);
		tempo = abreviarTempoRelativo(tempo, true);
		return tempo;
	}

	private static String abreviarTempoRelativo(String tempo, boolean omitirPassado) {
		if (omitirPassado)
			tempo = tempo.replace(" atrás", "");
		tempo = tempo.replace(" dias", " dias");
		tempo = tempo.replace(" horas", "h");
		tempo = tempo.replace(" minutos", "min");
		tempo = tempo.replace(" segundos", "s");
		tempo = tempo.replace("agora há pouco", "agora");
		return tempo;
	}

	public static String calcularTempoRelativoEmDias(Date anterior) {
		// Date agora = Date.from(new Date().toInstant().truncatedTo(ChronoUnit.DAYS));
		Long time = new Date().getTime();
		Date agora = new Date(time - time % (24 * 60 * 60 * 1000));
		Long timeAnterior = anterior.getTime();
		anterior = new Date(timeAnterior - timeAnterior % (24 * 60 * 60 * 1000));
//		Instant instant = anterior.toInstant();
//		Instant truncatedTo = instant.truncatedTo(ChronoUnit.DAYS);
//		anterior = Date.from(truncatedTo);
		PrettyTime p = new PrettyTime(agora, new Locale("pt"));

		String tempo = p.format(anterior);
		tempo = abreviarTempoRelativo(tempo, false);
		tempo = tempo.replace("agora", "hoje");
		return tempo;
	}

	public static String formatDataETempoRelativo(Date dt) {
		if (dt != null) {
			return formatDDMMYY(dt) + " (" + calcularTempoRelativo(dt) + ")";
		}
		return null;
	}

	public static LocalDateTime toLocalDateTime(Date dt) {
	    return dt.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDateTime();
	}

}