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
		provider.addPublicProperty("/siga.base.url", "http://localhost:8080");
		String base = get("/siga.base.url");
		
		provider.addRestrictedProperty("/siga.base.interna.url", base);
		String baseInterna = get("/siga.base.interna.url");
		
		provider.addPublicProperty("/siga.gsa.url", null);
		
		/* proxy properties */
		provider.addRestrictedProperty("/http.proxyHost", null);
		if (get("/http.proxyHost") != null)
			provider.addRestrictedProperty("/http.proxyPort");
		else
			provider.addRestrictedProperty("/http.proxyPort", null);
		
		provider.addRestrictedProperty("/https.proxyHost", null);
		if (get("/https.proxyHost") != null)
			provider.addRestrictedProperty("/https.proxyPort");
		else
			provider.addRestrictedProperty("/https.proxyPort", null);

		provider.addRestrictedProperty("/http.nonProxyHosts", "localhost|127.0.0.1|");
		/* END proxy properties */
		
		/* Parâmetros para ativação de Login por SSO OAuth2/OIDC */
		provider.addPublicProperty("/siga.integracao.sso", null);
		provider.addPublicProperty("/siga.integracao.sso.dominio", null);
		provider.addPrivateProperty("/siga.integracao.sso.cliente.id", null);
		provider.addPrivateProperty("/siga.integracao.sso.client.secret", null);
		provider.addPrivateProperty("/siga.integracao.sso.redirect.uri", base + "/siga/callBack");
		provider.addPublicProperty("/siga.integracao.sso.btn.txt", "Entrar com o SSO");
		/* Parâmetros para ativação de Login por SSO OAuth2/OIDC */

		provider.addPublicProperty("/siga.omitir.metodo2", "true");
		
		provider.addPublicProperty("/siga.cabecalho.titulo", "Justiça Federal");
		provider.addPublicProperty("/sigawf.ativo", "true");
		
		
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
		provider.addRestrictedProperty("/siga.ldap.porta", null);
		provider.addPrivateProperty("/siga.ldap.senha", null);
		provider.addRestrictedProperty("/siga.ldap.servidor", null);
		provider.addRestrictedProperty("/siga.ldap.ssl.porta", null);
		provider.addRestrictedProperty("/siga.ldap.usuario", null);
		provider.addRestrictedProperty("/siga.ldap.ws.endereco.autenticacao", null);
		provider.addRestrictedProperty("/siga.ldap.ws.endereco.busca", null);
		provider.addRestrictedProperty("/siga.ldap.ws.endereco.troca.senha", null);
		provider.addPublicProperty("/siga.smtp.starttls.enable", "false");

		provider.addPublicProperty("/siga.recaptcha.key", null);
		provider.addPrivateProperty("/siga.recaptcha.pwd", null);
		provider.addPublicProperty("/siga.smtp", null);
		provider.addPublicProperty("/siga.smtp.auth", "false");
		provider.addPrivateProperty("/siga.smtp.auth.senha", null);
		provider.addPrivateProperty("/siga.smtp.auth.usuario", null);
		provider.addPublicProperty("/siga.smtp.porta", "25");
		provider.addPublicProperty("/siga.smtp.usuario.remetente", "siga@projeto-siga.github.com");
		provider.addPublicProperty("/siga.ambiente", "desenv");
		provider.addPublicProperty("/siga.base.teste", "true");
		provider.addPublicProperty("/siga.devolucao.dias", null);
		provider.addPublicProperty("/siga.jwt.cookie.domain", null);
		provider.addPrivateProperty("/siga.jwt.secret");
		provider.addPrivateProperty("/siga.autenticacao.senha", provider.getProp("/siga.jwt.secret"));
		provider.addPublicProperty("/siga.jwt.token.ttl", "3600");
		provider.addPublicProperty("/siga.local", null);
		provider.addPublicProperty("/siga.mensagens", null);
		provider.addPublicProperty("/siga.mesa.carrega.lotacao", "true");
		provider.addPublicProperty("/siga.mesa.nao.revisar.temporarios", "false");
		provider.addPublicProperty("/siga.mesa.versao", "2");
		provider.addPublicProperty("/siga.municipios", null);
		provider.addPublicProperty("/siga.pagina.inicial.url", null);
		provider.addPublicProperty("/siga.versao.teste", "true");
		provider.addPublicProperty("/siga.ws.seguranca.token.jwt", "false");
		provider.addPublicProperty("/sigaex.autenticidade.url", base + "/sigaex/public/app/autenticar");
		provider.addPublicProperty("/sigaex.url", base + "/sigaex");
		provider.addPublicProperty("/sigaex.manual.url", base + "/siga/arquivos/apostila_sigaex.pdf");
		provider.addPublicProperty("/siga.logo.topo.orgao","data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABsAAAAmCAYAAAA1MOAmAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAB3RJTUUH4QUPFCQBHI8nPQAAA2pJREFUWMO9l0uIllUYx3/P+DkalpO3RU1hoQ7jpUDSTS1Gogi6EAm1icxFi4aKFkHShSgolAgqyhYhtGhX1sKwoqhFg1BYQS1EjJpoSiq7OEmTM47+WvS8cnqZyzvTNz3wcs57zvme/3n/z/WDWYi6RO3l/xD1UnVopr/rmCXeGWDZnIKpZ6ezuWHHTIAiAnUrcF4FWFygbTaqxuv9R9aoJ9SuuQK6Ux0pwH5Wn27716l3+W9Zo36n/qVubgrY1GY3197HgdPAQmC/uh1YpXapi/PpUqP8Uash2Kna+xDwFXAJsAJ4BTgGjNTOfabeCpyJiMY0vlGjsaWuV0edXl5UO2dis/drChbm+lXqFw0AP1CXRQOgxcBBoKdai+SkiL2NwOa05WRyqDWVu6fOrcDqYnt/DWgNsBHozkDvzHG0br/pvmqbOlbQcUK9oLZ/vEbZM+omdUPxrFcXoS5K7rfk05cBfLBQcEodUvsKoC01kGPq5RPk0bMSajdwExC1rD5W2OAX4JOI+C0VzQM+Aq4sYzEi9lX0TiSt5Pt24OQkbP4KHADeK258IXBxcWYwIvYVdp7ULi11dwP3Hc2Mj7oyaa1kd6MSExHjEXEP8NA0ZzuBveraTFWljDUBaxVuvEvtAPprxbETWJJjAHuAG9tSSiZYP0e9Tj1U0HZRZv1KXmpbLcv5UvXbWokx43Ddf24LSs9Kt3++2J6XntoXEYfmoj3YnF/Tm7T2zGW/2JtgK9QFM20JWjPEq3gdjoixqbJFO5vUmDZbtAFsOJ+Ohh7cLL6mULaqqtQz7oizAPaq36iDxfhj1qsHM6a+Vr8EromIkxnY3+fZQfVAxuIj6h/pSG+pS+u3Xa7ep+5Ux9W38/0y9eUskNvU11PJberanO/Ns9vVc9Uj6gPqh7l/72Qcb1D/VB8t1vaogzm/Wj2t7lB7Ull/7eLzc7wl9+8/6/oNPEpgufoccANwGHgVOD/371A3AT8AT0VE1Xv0Zx8yUIVIU28czwZ0NfBxRBwtPHIk09Zw8c9mB3At8HBEfD6Zp01H4wtJzUp1Xc7vrumo6Hu8aCEmjLOO7N/n1+pZ5eqv5fhEsb+gAOoG3szieoX6LrBzsnR1FNgFvFOs7QU+zYwxoD4GHAd+Ap4FBoqzvwNPZsNUXf5Itfk35wXnXBwGnuIAAAAASUVORK5CYII="); 
				

		provider.addPrivateProperty("/xjus.jwt.secret", null);
		provider.addPrivateProperty("/xjus.password", null);
		provider.addPublicProperty("/xjus.permalink.url", null);
		provider.addPublicProperty("/xjus.url", null);

		provider.addPublicProperty("/siga.service.endpoint", baseInterna + "/siga/servicos/GiService?wsdl");
		provider.addPublicProperty("/siga.service.url", baseInterna + "/siga/servicos/GiService");
		provider.addPublicProperty("/siga.service.qname", "http://impl.service.gi.siga.jfrj.gov.br/");
		provider.addPublicProperty("/siga.service.name", "GiService");

		provider.addPublicProperty("/sigaex.service.endpoint", baseInterna + "/sigaex/servicos/ExService?wsdl");
		provider.addPublicProperty("/sigaex.service.url", baseInterna + "/sigaex/servicos/ExService");
		provider.addPublicProperty("/sigaex.service.qname", "http://impl.service.ex.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigaex.service.name", "ExService");

		provider.addPublicProperty("/sigawf.service.endpoint", baseInterna + "/sigawf/servicos/WfService?wsdl");
		provider.addPublicProperty("/sigawf.service.url", baseInterna + "/sigawf/servicos/WfService");
		provider.addPublicProperty("/sigawf.service.qname", "http://impl.service.wf.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigawf.service.name", "WfService");

		provider.addPublicProperty("/sigagc.service.endpoint", baseInterna + "/sigagc/servicos/GcService?wsdl");
		provider.addPublicProperty("/sigagc.service.url", baseInterna + "/sigagc/servicos/GcService");
		provider.addPublicProperty("/sigagc.service.qname", "http://impl.service.gc.siga.jfrj.gov.br/");
		provider.addPublicProperty("/sigagc.service.name", "GcService");

		provider.addPublicProperty("/blucservice.url", baseInterna + "/blucservice/api/v1");
		provider.addPublicProperty("/vizservice.url", baseInterna + "/vizservice");
		
		provider.addPublicProperty("/siga.sgp.bnf.url","/siga-beneficios");
		provider.addPublicProperty("/siga.sgp.aq.url","/sigarhaq");
		provider.addPublicProperty("/siga.sgp.cad.url","/sigarh");
		provider.addPublicProperty("/siga.sgp.bdp.url","/sigarhaq1");
		provider.addPublicProperty("/siga.sgp.dcn.url","/sigarhaq2");
		provider.addPublicProperty("/siga.sgp.cst.url","/sigarhdadoscadastrais");
		provider.addPublicProperty("/siga.sgp.lot.url","/sigarhlotacao");	
		provider.addPublicProperty("/siga.sgp.trn.url","/sigatr");	
		provider.addPublicProperty("/siga.sgp.terc.url","/sigarhterceirizados");
		
		provider.addPublicProperty("/sigaex.sgp.bnf.url","/siga-beneficios");
		provider.addPublicProperty("/sigaex.sgp.aq.url","/sigarhaq");
		provider.addPublicProperty("/sigaex.sgp.cad.url","/sigarh");
		provider.addPublicProperty("/sigaex.sgp.bdp.url","/sigarhaq1");
		provider.addPublicProperty("/sigaex.sgp.dcn.url","/sigarhaq2");
		provider.addPublicProperty("/sigaex.sgp.cst.url","/sigarhdadoscadastrais");
		provider.addPublicProperty("/sigaex.sgp.lot.url","/sigarhlotacao");	
		provider.addPublicProperty("/sigaex.sgp.trn.url","/sigatr");	
		provider.addPublicProperty("/sigaex.sgp.terc.url","/sigarhterceirizados");

	}
}
