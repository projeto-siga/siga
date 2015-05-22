package br.gov.jfrj.siga.vraptor.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class Resources {

	private static HashMap<Class<?>, List<Method>> classAndYourMethods = new HashMap<>();

	protected static void setClassAndMethods(Class<?> classe, List<Method> methods) {
		if(!classAndYourMethods.containsKey(classe)) {
			classAndYourMethods.put(classe, methods);
		}
	}

	public static Method getMethod(Class<?> classe, final String nomeDoMetodo) {

		List<Method> metodos = classAndYourMethods.get(classe);

		Method encontrado = Iterables.find(metodos, new Predicate<Method>() {

			@Override
			public boolean apply(Method metodoDaVez) {
				return metodoDaVez.getName().equals(nomeDoMetodo);
			}
	    });

		return encontrado;
	}
}
