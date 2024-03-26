package br.gov.jfrj.siga.util;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.base.util.Texto;

public class ListaHierarquicaEspecie {

	static final String DIVIDER = ": ";
	List<String> groups = new ArrayList<String>();
	List<ListaHierarquicaEspecieItem> l = new ArrayList<ListaHierarquicaEspecieItem>();

	public void add(String idInicial, String text, Long value, boolean selected, String especie) {
		if (text == null || value == null)
			return;
		String as[] = text.split(DIVIDER);
		for (int i = 0; i < as.length; i++) {
			String s = as[i];

			// igual ao anterior
			if (i < groups.size() && s.equals(groups.get(i)))
				continue;
			else {
				groups = groups.subList(0, i);
				groups.add(s);
				boolean group = i < as.length - 1;
				l.add(new ListaHierarquicaEspecieItem(i + 1, (group ? null : idInicial), s, group ? null : Texto.removeAcentoMaiusculas(text),
						group ? null : value, group, group ? false : selected, s));
			}
		}
	}
	
	/* Com Busca complementar*/
	public void add(String idInicial, String text, String keywords, Long value, boolean selected, String especie) {
		if (text == null || value == null)
			return;
		String as[] = text.split(DIVIDER);
		for (int i = 0; i < as.length; i++) {
			String s = as[i];

			// igual ao anterior
			if (i < groups.size() && s.equals(groups.get(i)))
				continue;
			else {
				groups = groups.subList(0, i);
				groups.add(s);
				boolean group = i < as.length - 1;
				l.add(new ListaHierarquicaEspecieItem(i + 1, (group ? null : idInicial), s, group ? null : Texto.removeAcentoMaiusculas(text.concat(" - " + keywords)), keywords,
						group ? null : value, group, group ? false : selected, s));
			}
		}
	}

	public List<ListaHierarquicaEspecieItem> getList() {
		return l;
	}
}
