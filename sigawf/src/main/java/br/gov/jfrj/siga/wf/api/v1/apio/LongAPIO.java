package br.gov.jfrj.siga.wf.api.v1.apio;

public class LongAPIO {
	public static String of(Long p) {
		if (p == null)
			return null;
		return Long.toString(p);
	}
}
