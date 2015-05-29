/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

;

/**
 * Demonstration of delta day calculations.
 * 
 * @author Paul Hill
 * @copyright 2004 Paul Hill
 */
public class SigaCalendar extends GregorianCalendar {
	/**
	 * All minutes have this many milliseconds except the last minute of the day
	 * on a day defined with a leap second.
	 */
	public static final long MILLISECS_PER_MINUTE = 60 * 1000;

	/**
	 * Number of milliseconds per hour, except when a leap second is inserted.
	 */
	public static final long MILLISECS_PER_HOUR = 60 * MILLISECS_PER_MINUTE;

	protected static final long MILLISECS_PER_DAY = 24 * MILLISECS_PER_HOUR;

	/***************************************************************************
	 * Value to add to the day number returned by this calendar to find the
	 * Julian Day number. This is the Julian Day number for 1/1/1970. Note:
	 * Since the unix Day number is the same from local midnight to local
	 * midnight adding JULIAN_DAY_OFFSET to that value results in the
	 * chronologist, historians, or calenderists Julian Day number.
	 * 
	 * @see http://www.hermetic.ch/cal_stud/jdn.htm
	 */
	public static final long EPOCH_UNIX_ERA_DAY = 2440588L;

	/**
	 * @see java.util.GregorianCalendar#GregorianCalendar()
	 */
	public SigaCalendar() {
		super();
	}

	/**
	 * @param millisecondTime
	 *            - time as a binary Unix/Java time value.
	 * @see java.util.GregorianCalendar
	 */
	public SigaCalendar(long millisecondTime) {
		super();
		this.setTimeInMillis(millisecondTime);
	}

	/**
	 * @see java.util.GregorianCalendar#GregorianCalendar(int, int, int)
	 */
	public SigaCalendar(int y, int m, int d) {
		super(y, m, d);
	}

	/**
	 * @see java.util.GregorianCalendar#GregorianCalendar(int, int, int, int,
	 *      int, int)
	 */
	public SigaCalendar(int y, int m, int d, int h, int min, int s) {
		super(y, m, d, h, min, s);
	}

	/**
	 * @return Day number where day 0 is 1/1/1970, as per the Unix/Java
	 *         date/time epoch.
	 */
	public long getUnixDay() {
		long offset = get(Calendar.ZONE_OFFSET) + get(Calendar.DST_OFFSET);
		long day = (long) Math.floor((double) (getTime().getTime() + offset)
				/ ((double) MILLISECS_PER_DAY));
		return day;
	}

	/**
	 * @return LOCAL Chronologists Julian day number each day starting from
	 *         midnight LOCAL TIME.
	 * @see http://tycho.usno.navy.mil/mjd.html for more information about local
	 *      C-JDN
	 */
	public long getJulianDay() {
		return getUnixDay() + EPOCH_UNIX_ERA_DAY;
	}

	/**
	 * find the number of days from this date to the given end date. later end
	 * dates result in positive values. Note this is not the same as subtracting
	 * day numbers. Just after midnight subtracted from just before midnight is
	 * 0 days for this method while subtracting day numbers would yields 1 day.
	 * 
	 * @param end
	 *            - any Calendar representing the moment of time at the end of
	 *            the interval for calculation.
	 */
	public long diffDayPeriods(Calendar end) {
		long endL = end.getTimeInMillis()
				+ end.getTimeZone().getOffset(end.getTimeInMillis());
		long startL = this.getTimeInMillis()
				+ this.getTimeZone().getOffset(this.getTimeInMillis());
		return (endL - startL) / MILLISECS_PER_DAY;
	}

	/**
	 * DO NOT USE THIS Method it does not work across daylight savings
	 * boundaries for all difference intervals which include a day which has
	 * lost an hour and is only 23 hours long.
	 * 
	 * @param end
	 *            - any Calendar representing the moment of time at the end of
	 *            the interval for calculation.
	 * @deprecated
	 */
	public long diff24HourPeriods(Calendar end) {
		long endL = end.getTimeInMillis();
		long startL = this.getTimeInMillis();
		return (endL - startL) / MILLISECS_PER_DAY;
	}

	public String diffHHMMSS(Calendar end) {
		String result = "";
		long endL = this.getTimeInMillis();
		long startL = end.getTimeInMillis();
		long diffL = startL - endL;

		Long d = 0L;
		Long h = 0L;
		Long m = 0L;
		Long s = 0L;
		Long resto = 0L;

		d = diffL / MILLISECS_PER_DAY;
		resto = diffL % MILLISECS_PER_DAY;
		if (resto > 0) { // resto em milissegundos
			h = resto / MILLISECS_PER_HOUR;
			resto = resto % MILLISECS_PER_HOUR;
			if (resto > 0) { // resto em milissegundos
				m = resto / MILLISECS_PER_MINUTE;
				resto = resto % MILLISECS_PER_MINUTE;
				if (resto > 0) { // resto em milissegundos
					s = resto / 1000;
				}
			}
		}

		return d + ":" + h + ":" + m + ":" + s;
	}

	public String diffCompact(Calendar end) {
		String result = "";
		long endL = this.getTimeInMillis();
		long startL = end.getTimeInMillis();
		long diffL = startL - endL;

		Long d = 0L;
		Long h = 0L;
		Long m = 0L;
		Long s = 0L;
		Long resto = 0L;

		d = diffL / MILLISECS_PER_DAY;
		resto = diffL % MILLISECS_PER_DAY;
		if (resto > 0) { // resto em milissegundos
			h = resto / MILLISECS_PER_HOUR;
			resto = resto % MILLISECS_PER_HOUR;
			if (resto > 0) { // resto em milissegundos
				m = resto / MILLISECS_PER_MINUTE;
				resto = resto % MILLISECS_PER_MINUTE;
				if (resto > 0) { // resto em milissegundos
					s = resto / 1000;
				}
			}
		}
		if (d != 0)
			return d + "d";

		if (h != 0)
			return h + "h";

		if (m != 0)
			return m + "m";

		if (s != 0)
			return s + "s";

		return "";
	}

	public static String formatDDHHMMSS(Long mili) {

		Long d = 0L;
		Long h = 0L;
		Long m = 0L;
		Long s = 0L;
		Long resto = 0L;
		int signal = Long.signum(mili);

		mili = Math.abs(mili);

		d = mili / MILLISECS_PER_DAY;
		resto = mili % MILLISECS_PER_DAY;
		if (resto > 0) { // resto em milissegundos
			h = resto / MILLISECS_PER_HOUR;
			resto = resto % MILLISECS_PER_HOUR;
			if (resto > 0) { // resto em milissegundos
				m = resto / MILLISECS_PER_MINUTE;
				resto = resto % MILLISECS_PER_MINUTE;
				if (resto > 0) { // resto em milissegundos
					s = resto / 1000;
				}
			}
		}

		if (signal == -1) {
			return "-" + d + ":" + h + ":" + m + ":" + s;
		}

		return d + ":" + h + ":" + m + ":" + s;
	}

	public static String formatDHM(Long mili) {

		Long d = 0L;
		Long h = 0L;
		Long m = 0L;
		Long s = 0L;
		Long resto = 0L;
		int signal = Long.signum(mili);

		mili = Math.abs(mili);

		d = mili / MILLISECS_PER_DAY;
		resto = mili % MILLISECS_PER_DAY;
		if (resto > 0) { // resto em milissegundos
			h = resto / MILLISECS_PER_HOUR;
			resto = resto % MILLISECS_PER_HOUR;
			if (resto > 0) { // resto em milissegundos
				m = resto / MILLISECS_PER_MINUTE;
				resto = resto % MILLISECS_PER_MINUTE;
				if (resto > 0) { // resto em milissegundos
					s = resto / 1000;
				}
			}
		}

		String hora;
		String minuto;

		if (h > 9) {
			hora = h.toString();
		} else {
			hora = "0" + h.toString();
		}

		if (m > 9) {
			minuto = m.toString();
		} else {
			minuto = "0" + m.toString();
		}

		if (signal == -1) {
			if (d > 0) {
				return "-" + d + "d " + hora + "h " + minuto + "m";
			} else {
				return "-" + hora + "h " + minuto + "m";
			}

		}

		if (d > 0) {
			return d + "d " + hora + "h " + minuto + "m";
		} else {
			return hora + "h " + minuto + "m";
		}

	}

	/**
	 * Considera que um ano tem 360 dias (calendário contábil). Se a data
	 * inicial for o dia 31 de um mês, ela se tornará igual ao dia 30 do mesmo
	 * mês. Se a data final for o dia 31 de um mês e a data inicial for anterior
	 * ao trigésimo dia de um mês, a data final se tornará igual ao dia primeiro
	 * do próximo mês. Caso contrário, a data final se tornará igual ao
	 * trigésimo dia do mesmo mês.
	 * 
	 * @param end
	 * @return
	 */

	public long dias360(final Calendar end) {
		SigaCalendar inicio = new SigaCalendar(this.getTimeInMillis());
		SigaCalendar fim = new SigaCalendar(end.getTimeInMillis());

		if (this.get(DAY_OF_MONTH) == 30 || this.get(DAY_OF_MONTH) == 31
				|| fim.get(DAY_OF_MONTH) == 30 || fim.get(DAY_OF_MONTH) == 31) {
			if (this.get(DAY_OF_MONTH) == 31) {
				SigaCalendar cInicio = new SigaCalendar(this.get(YEAR),
						this.get(MONTH), 30);
			} else {

			}

			if ((fim.get(SigaCalendar.DAY_OF_MONTH) == 31)
					&& (this.get(DAY_OF_MONTH) < 30)) {
				fim.add(DAY_OF_MONTH, 1);
			} else {
				fim.add(DAY_OF_MONTH, -1);
			}
		}
		long fimL = fim.getTimeInMillis()
				+ fim.getTimeZone().getOffset(fim.getTimeInMillis());
		long inicioL = inicio.getTimeInMillis()
				+ inicio.getTimeZone().getOffset(inicio.getTimeInMillis());
		return (fimL - inicioL) / MILLISECS_PER_DAY;
	}

	static public Date converteStringEmData(String sDt)
			throws AplicacaoException {
		if (sDt.trim().length() <= 0) {
			return null;
		}
		try {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date resultado = df.parse(sDt);
			return resultado;
		} catch (ParseException e) {
			throw new AplicacaoException("Data inválida!");
		}
	}

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
			haQuantoTempo = "Às " + format.format(this.getTime());
		else if (foiOntemOuHoje())
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
				format.applyPattern("dd/MM/yy");
			else
				format.applyPattern("dd/MM/yy HH:mm");
			haQuantoTempo = format.format(this.getTime());
		}
		return haQuantoTempo;
	}
}
