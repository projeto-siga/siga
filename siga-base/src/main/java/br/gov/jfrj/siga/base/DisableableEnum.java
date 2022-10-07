package br.gov.jfrj.siga.base;

import java.util.ArrayList;
import java.util.List;

public interface DisableableEnum {
	public static <T extends DisableableEnum> List<T> enabledValues(T[] values) {
		List<T> l = new ArrayList<>();
		for (T enm : values)
			if (!enm.isDisabled())
				l.add(enm);
		return l;
	}

	public String name();

	public default Boolean isDisabled() {
		List<String> list = Prop.getList("/siga.enum.disable");
		if (list == null)
			return false;
		return list.contains(this.getClass().getSimpleName() + "." + this.name());
	}
}
