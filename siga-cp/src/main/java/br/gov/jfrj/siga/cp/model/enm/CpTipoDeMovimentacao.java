package br.gov.jfrj.siga.cp.model.enm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum CpTipoDeMovimentacao implements ITipoDeMovimentacao {

	NENHUMA(0, "NENHUMA");

	private static Map<Integer, ITipoDeMovimentacao> map = new HashMap<>();

	private final int id;
	private final String descr;

	CpTipoDeMovimentacao(int id, String descr) {
		this.id = id;
		this.descr = descr;
	}

	public int getId() {
		return id;
	}

	public String getDescr() {
		return this.descr;
	}

	public static ITipoDeMovimentacao getById(Integer id) {
		if (id == null)
			return null;
		return map.get(id);
	}

	public static void mapear(ITipoDeMovimentacao t) {
		map.put(t.getId(), t);
	}

	public static void mapear(ITipoDeMovimentacao[] l) {
		for (ITipoDeMovimentacao t : l)
			map.put(t.getId(), t);
	}

	public static Set<Integer> getIdsMapeadas() {
		return map.keySet();
	}

	public static Collection<ITipoDeMovimentacao> getValoresMapeados() {
		return map.values();
	}
}
