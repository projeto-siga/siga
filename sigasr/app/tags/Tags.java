package tags;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import controllers.Application;

import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;

public class Tags {

	public static void _selecaott(Map<String, String> args, Closure body,
			PrintWriter out, ExecutableTemplate template, int fromLine) {

		String tipo = (String) args.get("tipo");
		String nome = (String) args.get("nome");

		args.put("URLSelecionar", URLSelecionar(tipo, nome));
		
		out.print("tetete");
	}

	private static String URL(String tipo, String nome, String operacao) {
		try {
			Application.class.getMethod(
					tipo + operacao.substring(0, 1).toUpperCase()
							+ operacao.substring(1), String.class);
		} catch (NoSuchMethodException nsme) {
			return "http://localhost:8080/siga/" + tipo + "/" + operacao
					+ ".action?propriedade=" + tipo + nome + "&sigla=";
		}
		return "http://localhost:9000/" + tipo + "/" + operacao
				+ "selecionar?sigla=";
	}

	public static String URLSelecionar(String tipo, String nome) {
		return URL(tipo, nome, "selecionar");
	}

	public static String URLBuscar(String tipo, String nome) {
		return URL(tipo, nome, "buscar");
	}

}
