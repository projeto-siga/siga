package br.gov.jfrj.siga;


public class SigaWsProperties  {

	public static String getSvcExRemoteHost() {
		return System.getProperty("siga.ws.service.ex.remoteHost");	}

	public static String getSvcExRemotePort() {
		return System.getProperty("siga.ws.service.ex.remotePort");
	}
	
}
