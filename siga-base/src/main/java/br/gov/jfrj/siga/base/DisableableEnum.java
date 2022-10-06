package br.gov.jfrj.siga.base;

import java.util.ArrayList;
import java.util.List;

interface DisableableEnum {
	public static <T extends DisableableEnum> List<T> enabledValues(T[] values) {
		List<T> l = new ArrayList<>();
		for (T service : values)
			if (!service.isDisabled())
				l.add(service);
		return l;
	}

	public String getDisablerProperty();

	public String name();

	public default Boolean isDisabled() {
		List<String> list = Prop.getList(getDisablerProperty());
		if (list == null)
			return false;
		return list.contains(this.name());
	}
}
