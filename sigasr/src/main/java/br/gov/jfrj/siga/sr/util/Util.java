package br.gov.jfrj.siga.sr.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

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
	
	public static boolean notNullAndEmpty(String s) {
		return s != null && !"".equals(s);
	}
}
