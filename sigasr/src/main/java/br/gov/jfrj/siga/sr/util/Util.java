package br.gov.jfrj.siga.sr.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.gov.jfrj.siga.dp.DpLotacao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {

	public static void copiar(Object dest, Object orig) {
		for (Method getter : orig.getClass().getDeclaredMethods()) {
			try {
				String getterName = getter.getName();
				if (!getterName.startsWith("get"))
					continue;
				if (Collection.class.isAssignableFrom(getter.getReturnType()))
					continue;
				String setterName = getterName.replace("get", "set");
				Object origValue = getter.invoke(orig);
				dest.getClass().getMethod(setterName, getter.getReturnType())
						.invoke(dest, origValue);
			} catch (NoSuchMethodException nSME) {
				int a = 0;
			} catch (IllegalAccessException iae) {
				int a = 0;
			} catch (IllegalArgumentException iae) {
				int a = 0;
			} catch (InvocationTargetException nfe) {
				int a = 0;
			}

		}
	}

	public static boolean isbetween(int low, int high, int n) {
		return n >= low && n <= high;
	}
	
	public static Gson createGson(String... exclusions) {
		return new GsonBuilder()
			.addSerializationExclusionStrategy(FieldNameExclusionEstrategy.notIn(exclusions))
			.create();
	}
	
	public static boolean isDiaUtil(DateTime data) {
		return (data.getDayOfWeek() != DateTimeConstants.SATURDAY 
				&& data.getDayOfWeek() != DateTimeConstants.SUNDAY);
	}
	
	public static boolean isMesmoDia(DateTime data, DateTime outraData) {
		return data.toLocalDate().isEqual(outraData.withZone(data.getZone()).toLocalDate());
	}
	
	public static Interval getIntervaloDeTempo(int horaInicio, int horaFim, DateTime dia) {
		return new Interval(new LocalDate(dia).toDateTime(new LocalTime(horaInicio, 0)),
				new LocalDate(dia).toDateTime(new LocalTime(horaFim, 0)));
	}
	
	public static int[] getHorarioDeTrabalho(DpLotacao lotaAtendente) {
		if (lotaAtendente.getIdInicial() == 20937 || //central 2r
				lotaAtendente.getIdInicial() == 19753)  //help desk
			return new int[] {8,20}; 
		else if (lotaAtendente.getSigla().contains("STI-SL")) //suporte local
			return new int[] {10,19};
		else	//demais lotacoes
			return new int[] {11,19};
	}
}
