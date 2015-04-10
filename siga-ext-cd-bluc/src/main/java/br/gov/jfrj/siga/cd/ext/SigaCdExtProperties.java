package br.gov.jfrj.siga.cd.ext;

import java.util.Map;

import br.gov.jfrj.siga.model.prop.ext.ModeloPropriedade;

public class SigaCdExtProperties extends ModeloPropriedade {

	private static SigaCdExtProperties instance = new SigaCdExtProperties();

	@Override
	public String getPrefixoModulo() {
		return "siga.cd.ext";
	}

	public static String getString(final String key) {
		try {
			return instance.obterPropriedade(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getCacheLCR() {
		return getString("cache.LCR");
	}

	public static String getOrganizationName() {
		return getString("ORGANIZATION_NAME");
	}

	public static String getOrganizationLicense() {
		return getString("ORGANIZATION_LICENSE");
	}

	public static String getCadeiaConfianca() {
		return getString("cadeia.confianca");
	}

	public static String getProxyEndereco() {
		return getString("proxy.endereco");
	}

	public static String getProxyPorta() {
		return getString("proxy.porta");
	}

	public static String getProxyUser() {
		return getString("proxy.user");
	}

	public static String getProxySenha() {
		return getString("proxy.senha");
	}

	public static String getPoliticaCRL() {
		return getString("politica.crl");
	}

	public static String getPoliticaOCSP() {
		return getString("politica.ocsp");
	}

	public static String getPoliticaSigner() {
		return getString("politica.signer");
	}

	public static String getPoliticaFolder() {
		return getString("politica.folder");
	}

	public static String getActCert() {
		return getString("act.cert");
	}

	public static String getActURL() {
		return getString("act.url");
	}

	public static String getArquivoPolitica(String oid) {
		return getString("politica.oid." + oid);
	}

	public static Map<String, String> getArquivosPolitica() throws Exception {
		return instance
				.obterPropriedadeListaNaoNumerada("siga.cd.ext.politica.oid");
	}

}
