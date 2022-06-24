package br.gov.jfrj.siga.ex.api.v1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDadosEstaticosGet;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Objeto;

@AcessoPublico
public class DadosEstaticosGet implements IDadosEstaticosGet {

	@Override
	public String getContext() {
		return "obter lista de modelos";
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		JSONObject enums = new JSONObject();

		addEnum(enums, ExTipoDeMovimentacao.class);
		resp.enumsAsJson = enums.toString();

		JSONObject tabelas = new JSONObject();
		addTable(tabelas, ExNivelAcesso.class, "grauNivelAcesso");
		resp.tabelasAsJson = tabelas.toString();
	}

	public static <T> void addTable(JSONObject p, Class<T> c, String sort) throws Exception {

		JSONArray a = new JSONArray();
		p.put(c.getSimpleName(), a);

		Set<Field> fields = new HashSet<Field>();
		Class clazz = c;
		while (!clazz.equals(Objeto.class)) {
			Collections.addAll(fields, clazz.getDeclaredFields());
			clazz = clazz.getSuperclass();
		}

		for (T i : ExDao.getInstance().listarTodos(c, sort)) {
			JSONObject o = new JSONObject();
			a.put(o);
			// o.put("id", i.name());
			for (Field f : fields) {
				if (Modifier.isStatic(f.getModifiers()))
					continue;
				f.setAccessible(true);
				o.put(f.getName(), f.get(i));
			}
		}
	}

	public static void addEnum(JSONObject p, Class<?> c) throws Exception {
		if (!c.isEnum()) {
			throw new RuntimeException("Classe não é um enum");
		}
		JSONArray a = new JSONArray();
		p.put(c.getSimpleName(), a);

		Field[] flds = c.getDeclaredFields();

		for (Enum i : (Enum[]) c.getEnumConstants()) {
			JSONObject o = new JSONObject();
			a.put(o);
			o.put("name", i.name());
			for (Field f : flds) {
				f.setAccessible(true);
				if (f.isEnumConstant() || f.getName().equals("ENUM$VALUES"))
					continue;
				o.put(f.getName(), f.get(i));
			}
		}
	}

}
