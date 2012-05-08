package br.gov.jfrj.siga;

import br.gov.jfrj.siga.model.prop.ModeloPropriedade;

public class SigaWsProperties extends ModeloPropriedade {

	private static SigaWsProperties instance = new SigaWsProperties();
	
	@Override
	public String getPrefixoModulo() {
		return "siga.ws";
	}
	
	public static String getString(final String key) {
		try {
			return instance.obterPropriedade(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getSvcExRemoteHost() {
		return getString("service.ex.remoteHost");	}

	public static String getSvcExRemotePort() {
		return getString("service.ex.remotePort");
	}
	
}
