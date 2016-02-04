package br.gov.jfrj.siga.sr.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SrCalendar extends GregorianCalendar {

	private long meiaNoiteMillis(int diasAtras) {
		Calendar calMeiaNoite = new GregorianCalendar();
		calMeiaNoite.setTime(new Date());
		calMeiaNoite.set(Calendar.HOUR_OF_DAY, 0);
		calMeiaNoite.set(Calendar.MINUTE, 0);
		calMeiaNoite.set(Calendar.SECOND, 0);
		calMeiaNoite.set(Calendar.MILLISECOND, 0);
		calMeiaNoite.add(Calendar.DAY_OF_MONTH, diasAtras * (-1));
		return calMeiaNoite.getTimeInMillis();
	}

	private long semanaPassadaMillis() {
		return meiaNoiteMillis(6);
	}

	public long momentoOcorrenciaMillis() {
		return this.getTimeInMillis()
				+ this.getTimeZone().getOffset(this.getTimeInMillis());
	}

	public long tempoTranscorridoMillis() {

		Calendar calAgora = new GregorianCalendar();
		calAgora.setTime(new Date());
		long agoraLong = calAgora.getTimeInMillis()
				+ calAgora.getTimeZone().getOffset(calAgora.getTimeInMillis());

		long dataLong = momentoOcorrenciaMillis();

		return agoraLong - dataLong;
	}

	public long tempoTranscorridoMinutos() {
		return tempoTranscorridoMillis() / (60 * 1000);
	}

	public boolean fazMenosDeUmMinuto() {
		return tempoTranscorridoMillis() < (1000 * 60);
	}

	public boolean fazMenosDeDoisMinutos() {
		return tempoTranscorridoMillis() < (1000 * 60 * 2);
	}

	public boolean fazMenosDeUmaHora() {
		return tempoTranscorridoMillis() < (1000 * 60 * 60);
	}

	public boolean foiOntemOuHoje() {
		return momentoOcorrenciaMillis() > meiaNoiteMillis(1);
	}

	public boolean foiHoje() {
		return momentoOcorrenciaMillis() > meiaNoiteMillis(0);
	}

	public boolean fazMenosDeUmaSemana() {
		return momentoOcorrenciaMillis() > semanaPassadaMillis();
	}

	public String getTempoTranscorridoString() {

		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern("HH:mm");
		String haQuantoTempo = "";

		if (fazMenosDeUmMinuto())
			haQuantoTempo = "Neste instante";
		else if (fazMenosDeDoisMinutos())
			haQuantoTempo = "HÃ¡ 1 minuto";
		else if (fazMenosDeUmaHora())
			haQuantoTempo = "HÃ¡ " + tempoTranscorridoMinutos() + " minutos";
		else if (foiHoje())
			haQuantoTempo = "Ãs " + format.format(this.getTime());
		else if (foiOntemOuHoje())
			haQuantoTempo = "Ontem, Ã s " + format.format(this.getTime());
		else if (fazMenosDeUmaSemana()) {
			switch (this.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				haQuantoTempo = "Segunda-feira"; break;
			case Calendar.TUESDAY:
				haQuantoTempo = "TerÃ§a-feira"; break;
			case Calendar.WEDNESDAY:
				haQuantoTempo = "Quarta-feira"; break;
			case Calendar.THURSDAY:
				haQuantoTempo = "Quinta-feira"; break;
			case Calendar.FRIDAY:
				haQuantoTempo = "Sexta"; break;
			case Calendar.SATURDAY:
				haQuantoTempo = "SÃ¡bado"; break;
			case Calendar.SUNDAY:
				haQuantoTempo = "Domingo"; break;
			}

			haQuantoTempo += ", Ã s " + format.format(this.getTime());

		} else {
			format.applyPattern("dd/MM/yy HH:mm");
			haQuantoTempo = format.format(this.getTime());
		}
		return haQuantoTempo;
	}
}
