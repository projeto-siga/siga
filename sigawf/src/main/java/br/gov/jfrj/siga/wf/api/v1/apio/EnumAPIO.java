package br.gov.jfrj.siga.wf.api.v1.apio;

public class EnumAPIO {
	public static String of(Enum p) {
		if (p == null)
			return null;
		return p.name();
	}
}
