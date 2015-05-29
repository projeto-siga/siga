package br.gov.jfrj.siga.cd;

import br.gov.jfrj.siga.model.prop.ModeloPropriedade;

public class SigaCdProperties extends ModeloPropriedade {

	private static SigaCdProperties instance = new SigaCdProperties();

	@Override
	public String getPrefixoModulo() {
		return "siga.cd";
	}

	public static String getString(final String key) {
		try {
			return instance.obterPropriedade(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getProxyHost() {
		return getString("http.proxyHost");
	}

	public static String getProxyPort() {
		return getString("http.proxyPort");
	}

	public static String getServidorCarimbo() {
		return getString("servidorCarimbo");
	}

	public static String getTSPUrl() {
		return getString("tspUrl");
	}

	public static String getAssinaturaDigital() {
		return getString("assinatura.digital");
	}

}
