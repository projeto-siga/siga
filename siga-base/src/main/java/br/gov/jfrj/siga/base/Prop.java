package br.gov.jfrj.siga.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Prop {
	public interface IPropertyProvider {
		String getProp(String nome);

		void addPrivateProperty(String name);

		void addRestrictedProperty(String name);

		void addPublicProperty(String name);

		void addPrivateProperty(String name, String defaultValue);

		void addRestrictedProperty(String name, String defaultValue);

		void addPublicProperty(String name, String defaultValue);
	}

	static public IPropertyProvider provider = null;

	public static void setProvider(IPropertyProvider prov) {
		provider = prov;
	}

	public static String get(String nome) {
		return provider.getProp(nome);
	}

	public static Boolean getBool(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Boolean.valueOf(p.trim());
	}

	public static Integer getInt(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Integer.valueOf(p.trim());
	}

	public static List<String> getList(String nome) {
		String p = Prop.get(nome);
		if (p == null)
			return null;
		return Arrays.asList(p.split(","));
	}

	public static boolean isGovSP() {
		return "GOVSP".equals(get("/siga.local"));
	}

	public static Date getData(String nome) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

		String s = get(nome);

		try {
			if (s == null)
				return (Date) formatter.parse("31/12/2099");
			return (Date) formatter.parse(s);
		} catch (Exception nfe) {
			throw new RuntimeException("Erro ao converter propriedade string em data");
		}
	}

	public static void defineGlobalProperties() {
		provider.addPublicProperty("/siga.gsa.url", null);
		provider.addPublicProperty("/siga.http.proxy.host", null);
		provider.addPublicProperty("/siga.http.proxy.port", null);
		
		provider.addPublicProperty("/siga.ldap.ambiente", null);
		provider.addPublicProperty("/siga.ldap.dn.usuarios", null);
		provider.addPublicProperty("/siga.ldap.keystore", null);
		provider.addPublicProperty("/siga.ldap.modo.escrita", null);
		provider.addPublicProperty("/siga.ldap.orgaos", null);
		if (get("/siga.ldap.orgaos") != null) {
			for (String s : get("/siga.ldap.orgaos").split(",")) {
				provider.addPublicProperty("/siga.ldap." + s + ".integracao", null);
				provider.addPublicProperty("/siga.ldap." + s + ".modo", null);
				provider.addPublicProperty("/siga.ldap." + s + ".url", null);
			}
		}
		provider.addPublicProperty("/siga.ldap.porta", null);
		provider.addPublicProperty("/siga.ldap.senha", null);
		provider.addPublicProperty("/siga.ldap.servidor", null);
		provider.addPublicProperty("/siga.ldap.ssl.porta", null);
		provider.addPublicProperty("/siga.ldap.usuario", null);
		provider.addPublicProperty("/siga.ldap.ws.endereco.autenticacao", null);
		provider.addPublicProperty("/siga.ldap.ws.endereco.busca", null);
		provider.addPublicProperty("/siga.ldap.ws.endereco.troca.senha", null);
		provider.addPublicProperty("/siga.smtp.starttls.enable", null);

		provider.addPublicProperty("/siga.recaptcha.key", null);
		provider.addPublicProperty("/siga.recaptcha.pwd", null);
		provider.addPublicProperty("/siga.smtp", null);
		provider.addPublicProperty("/siga.smtp.auth", "false");
		provider.addPublicProperty("/siga.smtp.auth.senha", null);
		provider.addPublicProperty("/siga.smtp.auth.usuario", null);
		provider.addPublicProperty("/siga.smtp.porta", "25");
		provider.addPublicProperty("/siga.smtp.usuario.remetente", "siga@projeto-siga.github.com");
		provider.addPublicProperty("/siga.ambiente", "desenv");
		provider.addPublicProperty("/siga.autenticacao.senha", null);
		provider.addPublicProperty("/siga.base.teste", "true");
		provider.addPublicProperty("/siga.base.url", "http://localhost:8080");
		provider.addPublicProperty("/siga.devolucao.dias", null);
		provider.addPublicProperty("/siga.jwt.cookie.domain", null);
		provider.addPublicProperty("/siga.jwt.secret");
		provider.addPublicProperty("/siga.jwt.token.ttl", "3600");
		provider.addPublicProperty("/siga.local", null);
		provider.addPublicProperty("/siga.mensagens", null);
		provider.addPublicProperty("/siga.mesa.carrega.lotacao", "true");
		provider.addPublicProperty("/siga.mesa.nao.revisar.temporarios", "false");
		provider.addPublicProperty("/siga.mesa.versao", "1");
		provider.addPublicProperty("/siga.municipios", null);
		provider.addPublicProperty("/siga.pagina.inicial.url", null);
		provider.addPublicProperty("/siga.versao.teste", "true");
		provider.addPublicProperty("/siga.ws.seguranca.token.jwt", null);
		provider.addPublicProperty("/sigaex.autenticidade.url", "http://localhost:8080/sigaex/public/app/autenticar");
		provider.addPublicProperty("/sigaex.url", "http://localhost:8080/sigaex");

		provider.addPublicProperty("/siga.xjus.jwt.secret", null);
		provider.addPublicProperty("/siga.xjus.password", null);
		provider.addPublicProperty("/siga.xjus.permalink.url", null);
		provider.addPublicProperty("/siga.xjus.url", null);
	}
}
