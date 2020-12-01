package br.gov.jfrj.siga.api.v1;

public class CurrentRequest {

	private static final ThreadLocal<RequestInfo> INSTANCIA = new ThreadLocal<>();
	
	public static void set(RequestInfo info) {
		INSTANCIA.set(info);
	}
	
	public static RequestInfo get() {
		return INSTANCIA.get();
	}
	
}