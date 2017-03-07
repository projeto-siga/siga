package br.gov.jfrj.siga.uteis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SigaPlayCalendar extends GregorianCalendar {

    private static final long serialVersionUID = -6163961017957633088L;

    private long meiaNoiteMillis(int diasAtras) {
        Calendar calMeiaNoite = new GregorianCalendar();
        calMeiaNoite.setTime(new Date());
        calMeiaNoite.set(Calendar.HOUR_OF_DAY, 0);
        calMeiaNoite.set(Calendar.MINUTE, 0);
        calMeiaNoite.set(Calendar.SECOND, 0);
        calMeiaNoite.set(Calendar.MILLISECOND, 0);
        calMeiaNoite.add(Calendar.DAY_OF_MONTH, diasAtras * (-1));
        return calMeiaNoite.getTimeInMillis() + calMeiaNoite.getTimeZone().getOffset(calMeiaNoite.getTimeInMillis());
    }

    private long semanaPassadaMillis() {
        return meiaNoiteMillis(6);
    }

    public long momentoOcorrenciaMillis() {
        return this.getTimeInMillis() + this.getTimeZone().getOffset(this.getTimeInMillis());
    }

    public long tempoTranscorridoMillis() {

        Calendar calAgora = new GregorianCalendar();
        calAgora.setTime(new Date());
        long agoraLong = calAgora.getTimeInMillis() + calAgora.getTimeZone().getOffset(calAgora.getTimeInMillis());

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

    public boolean foiOntem() {
        return momentoOcorrenciaMillis() > meiaNoiteMillis(1);
    }

    public boolean foiHoje() {
        return momentoOcorrenciaMillis() > meiaNoiteMillis(0);
    }

    public boolean fazMenosDeUmaSemana() {
        return momentoOcorrenciaMillis() > semanaPassadaMillis();
    }

    public String getTempoTranscorridoString(boolean ocultarMinutos) {

        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("HH:mm");
        String haQuantoTempo = "";

        if (fazMenosDeUmMinuto())
            haQuantoTempo = "Neste instante";
        else if (fazMenosDeDoisMinutos())
            haQuantoTempo = "Há 1 minuto";
        else if (fazMenosDeUmaHora())
            haQuantoTempo = "Há " + tempoTranscorridoMinutos() + " minutos";
        else if (foiHoje())
            haQuantoTempo = "às " + format.format(this.getTime());
        else if (foiOntem())
            haQuantoTempo = "Ontem, às " + format.format(this.getTime());
        else if (fazMenosDeUmaSemana()) {
            switch (this.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                haQuantoTempo = "Segunda-feira";
                break;
            case Calendar.TUESDAY:
                haQuantoTempo = "Terça-feira";
                break;
            case Calendar.WEDNESDAY:
                haQuantoTempo = "Quarta-feira";
                break;
            case Calendar.THURSDAY:
                haQuantoTempo = "Quinta-feira";
                break;
            case Calendar.FRIDAY:
                haQuantoTempo = "Sexta";
                break;
            case Calendar.SATURDAY:
                haQuantoTempo = "Sábado";
                break;
            case Calendar.SUNDAY:
                haQuantoTempo = "Domingo";
                break;
            }

            haQuantoTempo += ", às " + format.format(this.getTime());

        } else {
            if (ocultarMinutos)
                format.applyPattern("dd/MM/yyyy");
            else
                format.applyPattern("dd/MM/yyyy HH:mm");
            haQuantoTempo = format.format(this.getTime());
        }
        return haQuantoTempo;
    }
}
