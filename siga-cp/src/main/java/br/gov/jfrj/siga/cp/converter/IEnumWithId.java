package br.gov.jfrj.siga.cp.converter;

public interface IEnumWithId {
	Integer getId();

	static <T extends IEnumWithId> T getEnumFromId(Integer id, Class<T> clazz) {
		if (id == null)
			return null;
		for (T v : clazz.getEnumConstants())
			if (v.getId().equals(id))
				return v;
		return null;
	}
}
